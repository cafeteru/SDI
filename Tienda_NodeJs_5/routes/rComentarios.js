module.exports = function (app, swig, gestorBD, moment) {
    app.get("/comentario/listar", function (req, res) {
        var criterio = {};
        gestorBD.obtenerComentarios(criterio, function (comentarios) {
            if (comentarios == null) {
                res.send("Error al listar ");
            } else {
                var respuesta = swig.renderFile('views/bComentarios.html',
                    {
                        comentarios: comentarios
                    });
                res.send(respuesta);
            }
        });
    });

    app.get("/comentario/agregar", function (req, res) {
        var respuesta = swig.renderFile('views/bAgregarComentarios.html', {});
        res.send(respuesta);
    });

    app.post("/comentario/agregar", function (req, res) {
        var comentario = {
            texto: req.body.texto,
            fecha: moment().format('LLLL'),
            autor: req.session.usuario
        }
        gestorBD.insertarComentario(comentario, function (id) {
            if (id == null) {
                res.redirect("/comentario/agregar?mensaje=Error al agregar el comentario");
            } else {
                res.redirect("/comentario/listar");
            }
        });
    });

    app.get('/comentario/modificar/:id', function (req, res) {
        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerComentarios(criterio, function (comentarios) {
            if (comentarios == null) {
                res.send(comentarios);
            } else {
                var respuesta = swig.renderFile('views/bModificarComentarios.html',
                    {
                        comentario: comentarios[0]
                    });
                res.send(respuesta);
            }
        });
    });

    app.post('/comentario/modificar/:id', function (req, res) {
        var id = req.params.id;
        var criterio = {"_id": gestorBD.mongo.ObjectID(id)};
        var comentario = {
            texto: req.body.texto,
            fecha: moment().format('LLLL'),
            autor: req.session.usuario
        }
        gestorBD.modificarComentario(criterio, comentario, function (result) {
            if (result == null) {
                res.redirect("/comentario/modificar/:id?mensaje=Error al modificar el comentario");
            } else {
                res.redirect("/comentario/listar");
            }
        });
    });

    app.get('/comentario/eliminar/:id', function (req, res) {
        var criterio = { "_id" : gestorBD.mongo.ObjectID(req.params.id) };
        gestorBD.eliminarComentario(criterio,function(comentarios){
            if ( comentarios == null ){
                res.send(respuesta);
            } else {
                res.redirect("/comentario/listar");
            }
        });
    });
};