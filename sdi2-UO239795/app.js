// Servidor Web
let express = require('express');
let app = express();

let log4js = require('log4js');
log4js.configure({
    appenders: { sdi: { type: 'file', filename: 'sdi.log' } },
    categories: { default: { appenders: ['sdi'], level: 'trace' } }
});
let logger = log4js.getLogger('sdi');

/*logger.trace('Entering sdi testing');
logger.debug('Got sdi.');
logger.info('sdi is Gouda.');
logger.warn('sdi is quite smelly.');
logger.error('sdi is too ripe!');
logger.fatal('sdi was breeding ground for listeria.');*/

// Objeto sessión para guardar al usuario actual
let expressSession = require('express-session');
app.use(expressSession({
    secret: 'abcdefg',
    resave: true,
    saveUninitialized: true
}));

// Motor de plantillas
let swig = require('swig');

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
//Aplicar routerUsuarioSession
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

// routerUsuarioToken
let routerUsuarioToken = express.Router();
routerUsuarioToken.use(function (req, res, next) {
    // obtener el token, puede ser un parámetro GET , POST o HEADER
    let token = req.body.token || req.query.token || req.headers['token'];
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
app.use('/api/users', routerUsuarioToken);
app.use('/api/messages', routerUsuarioToken);

// Variables
app.set('port', 8081);
app.set('db', 'mongodb://uo239795:123456@ds231529.mlab.com:31529/sdi2-uo239795');
app.set('key', 'abcdefg');
app.set('crypto', crypto);
app.set('logger', logger);

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
    console.log("Servidor activo");
});