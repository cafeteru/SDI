// Servidor Web
var express = require('express');
var app = express();

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

var swig = require('swig-templates');

var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

// Para fechas
var moment = require('moment');
moment.locale("es");

// Objeto para manejar base de datos
var gestorBD = require("./modules/gestorBD.js");
gestorBD.init(app, mongo);

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


//routerAudios
var routerAudios = express.Router();
routerAudios.use(function (req, res, next) {
    console.log("routerAudios");
    var path = require('path');
    var idCancion = path.basename(req.originalUrl, '.mp3');
    gestorBD.obtenerCanciones({_id: mongo.ObjectID(idCancion)}, function (canciones) {
        if (canciones[0].autor == req.session.usuario) {
            next();
        } else {
            res.redirect("/tienda");
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

// lanzar el servidor
app.listen(app.get("port"), function () {
    console.log("Servidor activo");
});