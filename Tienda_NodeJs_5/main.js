// Servidor Web
var express = require('express');
var app = express();

var rest = require('request');
app.set('rest', rest);

app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Credentials", "true");
    res.header("Access-Control-Allow-Methods", "POST, GET, DELETE, UPDATE, PUT");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
// Debemos especificar todas las headers que se aceptan. Content-Type , token
    next();
});

var jwt = require('jsonwebtoken');
app.set('jwt', jwt);

// Objeto sessión
var expressSession = require('express-session');
app.use(expressSession({
    secret: 'abcdefg',
    resave: true,
    saveUninitialized: true
}));

// Encriptación de contraseñas
var crypto = require('crypto');

// Subir ficheros
var fileUpload = require('express-fileupload');
app.use(fileUpload());

// Base de datos
var mongo = require('mongodb');

var swig = require('swig');

var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

// Para fechas
var moment = require('moment');
moment.locale("es");

// Objeto para manejar base de datos
var gestorBD = require("./modules/gestorBD.js");
gestorBD.init(app, mongo);

// routerUsuarioToken
var routerUsuarioToken = express.Router();
routerUsuarioToken.use(function (req, res, next) {
    // obtener el token, puede ser un parámetro GET , POST o HEADER
    var token = req.body.token || req.query.token || req.headers['token'];
    if (token != null) {
        // verificar el token
        jwt.verify(token, 'secreto', function (err, infoToken) {
            if (err || (Date.now() / 1000 - infoToken.tiempo) > 240) {
                res.status(403); // Forbidden
                res.json({
                    acceso: false,
                    error: 'Token invalido o caducado'
                });
                // También podríamos comprobar que intoToken.usuario existe
                return;
            } else {
                console.log(infoToken.usuario)
                // dejamos correr la petición
                res.usuario = infoToken.usuario;
                next();
            }
        });
    } else {
        res.status(403); // Forbidden
        res.json({
            acceso: false,
            mensaje: 'No hay Token'
        });
    }
});
// Aplicar routerUsuarioToken
app.use('/api/cancion', routerUsuarioToken);

// routerUsuarioSession
var routerUsuarioSession = express.Router();
routerUsuarioSession.use(function (req, res, next) {
    console.log("routerUsuarioSession");
    if (req.session.usuario) { // dejamos correr la petición
        next();
    } else {
        console.log("va a : " + req.session.destino);
        res.redirect("/identificarse");
    }
});
//Aplicar routerUsuarioSession
app.use("/canciones/agregar", routerUsuarioSession);
app.use("/publicaciones", routerUsuarioSession);
app.use("/comentario/listar", routerUsuarioSession);
app.use("/comentario/agregar", routerUsuarioSession);
app.use("/comentario/modificar", routerUsuarioSession);
app.use("/cancion/comprar", routerUsuarioSession);
app.use("/compras", routerUsuarioSession);


//routerUsuarioAutor
var routerUsuarioAutor = express.Router();
routerUsuarioAutor.use(function (req, res, next) {
    console.log("routerUsuarioAutor");
    var path = require('path');
    var id = path.basename(req.originalUrl);
    // Cuidado porque req.params no funciona
    // en el router si los params van en la URL.
    gestorBD.obtenerCanciones({
        _id: mongo.ObjectID(id)
    }, function (canciones) {
        if (canciones[0].autor == req.session.usuario) {
            next();
        } else {
            res.redirect("/tienda");
        }
    })
});

//Aplicar routerUsuarioAutor
app.use("/cancion/modificar", routerUsuarioAutor);
app.use("/cancion/eliminar", routerUsuarioAutor);


//routerUsuarioAutorComentario
var routerUsuarioAutorComentario = express.Router();
routerUsuarioAutorComentario.use(function (req, res, next) {
    console.log("routerUsuarioAutorComentario");
    var path = require('path');
    var id = path.basename(req.originalUrl);
    // Cuidado porque req.params no funciona
    // en el router si los params van en la URL.
    gestorBD.obtenerComentarios({
        _id: mongo.ObjectID(id)
    }, function (comentarios) {
        if (comentarios[0].autor == req.session.usuario) {
            next();
        } else {
            res.redirect("/comentario/listar");
        }
    })
});
app.use("/comentario/modificar", routerUsuarioAutorComentario);
app.use("/comentario/eliminar", routerUsuarioAutorComentario);

//routerAudios
var routerAudios = express.Router();
routerAudios.use(function (req, res, next) {
    console.log("routerAudios");
    var path = require('path');
    var idCancion = path.basename(req.originalUrl, '.mp3');
    gestorBD.obtenerCanciones({
        _id: mongo.ObjectID(idCancion)
    }, function (canciones) {
        if (canciones[0].autor == req.session.usuario) {
            next();
        } else {
            var criterio = {
                usuario: req.session.usuario,
                cancionId: mongo.ObjectID(idCancion)
            };
            gestorBD.obtenerCompras(criterio, function (compras) {
                if (compras != null && compras.length > 0) {
                    next();
                    return;
                } else {
                    res.redirect("/tienda");
                }
            });
        }
    })
});
//Aplicar routerAudios
app.use("/audios/", routerAudios);

app.use(express.static('public')); // Funcionalidad adicional a la aplicación

//Variables
app.set("port", 8081);
app.set('db', 'mongodb://uo239795:123456@ds121349.mlab.com:21349/tiendamusicauo239795');
app.set('clave', 'abcdefg');
app.set('crypto', crypto);

// Controladores
require("./routes/rusuarios.js")(app, swig, gestorBD);
require("./routes/rcanciones.js")(app, swig, gestorBD);
require("./routes/rcomentarios.js")(app, swig, gestorBD, moment);
require("./routes/rapicanciones.js")(app, gestorBD);

app.get('/', function (req, res) {
    res.redirect('/tienda');
});

app.use(function (err, req, res, next) {
    console.log("Error producido: " + err); //we log the error in our db
    if (!res.headersSent) {
        res.status(400);
        res.send("Recurso no disponible");
    }
});

// lanzar el servidor
app.listen(app.get("port"), function () {
    console.log("Servidor activo");
});