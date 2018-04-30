// Servidor Web
var express = require('express');
var app = express();

// Objeto sessión para guardar al usuario actual
var expressSession = require('express-session');
app.use(expressSession({
    secret: 'abcdefg',
    resave: true,
    saveUninitialized: true
}));

// Motor de plantillas
var swig = require('swig');

var jwt = require('jsonwebtoken');
app.set('jwt', jwt);

// Base de datos
var mongo = require('mongodb');
var ObjectId = require('mongodb').ObjectID;

// Objeto para manejar base de datos
var usersRepository = require("./modules/usersRepository.js");
usersRepository.init(app, mongo);
var requestsRepository = require("./modules/requestsRepository.js");
requestsRepository.init(app, mongo);
var messageRepository = require("./modules/messageRepository.js");
messageRepository.init(app, mongo);

// routerUserSession
var routerUserSession = express.Router();
routerUserSession.use(function (req, res, next) {
    if (req.session.user) { // dejamos correr la petición
        next();
    } else {
        res.redirect("/login");
    }
});
//Aplicar routerUsuarioSession
app.use("/home", routerUserSession);
app.use("/list", routerUserSession);
app.use("/send", routerUserSession);
app.use("/requests", routerUserSession);
app.use("/accepted", routerUserSession);
app.use("/friends", routerUserSession);


// Leer los cuerpos POST
var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

// Para usar los img, css y js
app.use(express.static('public'));

// Encriptación de contraseñas
var crypto = require('crypto');

// routerUsuarioToken
var routerUsuarioToken = express.Router();
routerUsuarioToken.use(function (req, res, next) {
    // obtener el token, puede ser un parámetro GET , POST o HEADER
    var token = req.body.token || req.query.token || req.headers['token'];
    if (token != null) {
        // verificar el token
        jwt.verify(token, 'secreto', function (err, infoToken) {
            if (err || (Date.now() / 1000 - infoToken.tiempo) > 24000) {
                res.status(403); // Forbidden
                res.json({
                    acceso: false,
                    error: 'Token invalido o caducado'
                });
                return;
            } else {
                res.user = infoToken.user;
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
app.use('/api/friends', routerUsuarioToken);
app.use('/api/send', routerUsuarioToken);

// Variables
app.set('port', 80);
app.set('db', 'mongodb://uo239795:123456@ds231529.mlab.com:31529/sdi2-uo239795');
app.set('key', 'abcdefg');
app.set('crypto', crypto);

// Controladores
require("./routes/rUsers.js")(app, swig, usersRepository, requestsRepository);
require("./routes/rRequests.js")(app, swig, usersRepository, requestsRepository, ObjectId);
require("./routes/api.js")(app, usersRepository, requestsRepository, messageRepository);

app.get('/', function (req, res) {
    var respuesta = swig.renderFile('views/index.html', {});
    res.send(respuesta);
});

app.use(function (err, req, res, next) {
    console.log("Error producido: " + err); //we log the error in our db
    if (!res.headersSent) {
        res.status(400);
        res.send("Recurso no disponible");
    }
});

app.listen(app.get('port'), function () {
    console.log("Servidor activo");
});