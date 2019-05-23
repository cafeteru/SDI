// Servidor Web
let express = require('express');
let app = express();

// Log
let log4js = require('log4js');
log4js.configure({
    appenders: {sdi: {type: 'file', filename: 'logs/sdi.log'}},
    categories: {default: {appenders: ['sdi'], level: 'trace'}}
});
let logger = log4js.getLogger('sdi');

// Objeto sessión para guardar al usuario actual
let expressSession = require('express-session');
app.use(expressSession({
    secret: 'abcdefg', // Codifica la contraseña
    resave: true,
    saveUninitialized: true
}));

// Motor de plantillas
let swig = require('swig-templates');

// Lee los json
let jwt = require('jsonwebtoken');
app.set('jwt', jwt);

// Base de datos
let mongo = require('mongodb');
let ObjectId = require('mongodb').ObjectID;

// Objeto para manejar base de datos
let repository = require("./modules/repository.js");
repository.init(app, mongo);

// routerUserSession
let routerUserSession = express.Router();
routerUserSession.use(function (req, res, next) {
    if (req.session.user) { // dejamos correr la petición
        next();
    } else {
        res.redirect("/login");
    }
});

// Aplicar routerUsuarioSession
app.use("/home", routerUserSession);
app.use("/list", routerUserSession);
app.use("/send", routerUserSession);
app.use("/requests", routerUserSession);
app.use("/accepted", routerUserSession);
app.use("/friends", routerUserSession);


// Leer los cuerpos POST
let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

// Para usar los img, css y js
app.use(express.static('public'));

// Encriptación de contraseñas
let crypto = require('crypto');

// Variables
app.set('port', 8081);
app.set('db', 'mongodb://admin:123456a@ds145184.mlab.com:45184/sdi-enero-2019');
app.set('key', 'abcdefg');
app.set('crypto', crypto);
app.set('logger', logger);
app.set('keyApi', 'secreto');

// routerUsuarioToken
let apiToken = express.Router();
apiToken.use(function (req, res, next) {
    // obtener el token, puede ser un parámetro GET , POST o HEADER
    let token = req.body.token || req.query.token || req.headers['token'];
    if (token != null) {
        // verificar el token
        jwt.verify(token, app.get('keyApi'), function (err, infoToken) {
            // Si esta caducado lo echa para fuera
            if (err || (Date.now() / 1000 - infoToken.tiempo) > 24000) {
                res.status(403); // Forbidden
                res.json({
                    acceso: false,
                    error: 'Token invalido o caducado'
                });
                return;
            } else {
                // si es correcto, deja pasar la peticion
                res.user = infoToken.user;
                next();
            }
        });
    } else {
        // No hay token en la petición
        res.status(403); // Forbidden
        res.json({
            acceso: false,
            mensaje: 'No hay Token'
        });
    }
});

// Indica en que urls aplicar routerUsuarioToken
app.use('/api/users', apiToken);
app.use('/api/messages', apiToken);

// Controladores
require("./routes/rUsers.js")(app, swig, repository);
require("./routes/rRequests.js")(app, swig, repository, ObjectId);
require("./routes/api.js")(app, repository, ObjectId);

app.get('/', function (req, res) {
    let respuesta = swig.renderFile('views/index.html', {});
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
    console.log("Servidor activo en puerto " + app.get('port'));
});