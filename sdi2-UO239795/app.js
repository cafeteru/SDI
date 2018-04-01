// Servidor Web
var express = require('express');
var app = express();

// Motor de plantillas
var swig = require('swig');

// Leer los cuerpos POST
var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

// Para usar los img, css y js
app.use(express.static('public'));

// Variables
app.set('port', 80);
app.set('db', 'mongodb://uo239795:123456@ds231529.mlab.com:31529/sdi2-uo239795');

// Controladores
require("./routes/rUsers.js")(app, swig);
require("./routes/rRequests.js")(app, swig);
require("./routes/rFriendship.js")(app, swig);

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