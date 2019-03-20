module.exports = function (app, repository, ObjectId) {
    app.post("/api/login/", function (req, res) {
        let seguro = app.get("crypto").createHmac('sha256', app.get('key')).update(req.body.password).digest('hex');
        let user = {
            email: req.body.email,
            password: seguro
        };
        repository.getElements(user, "users", function (users) {
            if (users == null || users.length == 0) {
                res.status(401);
                res.json({
                    authenticated: false,
                    error: 'Identificación no válida'
                });
                app.get("logger").error('Identificación no válida con token en la API');
            } else {
                let token = app.get('jwt').sign({
                    user: user.email,
                    time: Date.now() / 1000
                }, app.get('keyApi'));
                res.status(200);
                res.json({
                    authenticated: true,
                    token: token
                });
                app.get("logger").info('Identificación válida con token en la API del usuario ' + user.email);
            }
        });
    });

    app.get("/api/users/", function (req, res) {
        let email = {
            email: res.user
        };
        getFriends(res, email, function (emails) {
            res.status(200);
            res.send(JSON.stringify(emails));
            app.get("logger").info('Listando los amigos del usuario ' + res.user + ' en la API');
        });
    });

    app.post("/api/messages/", function (req, res) {
        let email = {
            email: res.user
        };
        getFriends(res, email, function (friends) {
            let friend = friends.filter(function (element) {
                return element.email === req.body.email;
            });
            if (friend.length === 0) {
                res.status(401);
                res.json({
                    error: 'El receptor no es amigo'
                });
                app.get("logger").error('El usuario ' + req.body.email + ' no es amigo del usuario ' + res.user);
            } else {
                let message = {
                    sender: res.user,
                    receiver: req.body.email,
                    message: req.body.message,
                    date: new Date(),
                    read: false
                };
                repository.addElement(message, "messages", function (id) {
                    if (id == null) {
                        res.status(501);
                        res.json({
                            error: 'Error en el servidor al crear el mensaje'
                        });
                        app.get("logger").error('Error al crear el mensaje entre los usuarios ' + req.body.email + ' y ' + res.user);
                    } else {
                        res.status(200);
                        res.send(JSON.stringify(message));
                        app.get("logger").info('Exito al crear el mensaje entre los usuarios ' + req.body.email + ' y ' + res.user);
                    }
                });
            }
        });
    });

    app.get("/api/messages/", function (req, res) {
        repository.getElements(repository.getMessagesBySenderAndReceiver(req, res), "messages", function (conversation) {
            res.status(200);
            res.send(JSON.stringify(conversation));
            app.get("logger").info('Listando los mensajes entre los usuarios ' + res.user + " y " + req.query.email);
        });
    });

    app.get("/api/messages/all", function (req, res) {
        repository.getElements(repository.getAllMessages(res), "messages", function (conversation) {
            res.status(200);
            res.send(JSON.stringify(conversation));
            app.get("logger").info('Listando todos los mensajes del usuario ' + res.user);
        });
    });

    app.put("/api/messages/:id", function (req, res) {
        var message = {
            "_id": new ObjectId(req.params.id)
        };
        repository.getElements(message, "messages", function (conversation) {
            if (conversation == null || conversation.length === 0) {
                res.status(403);
                res.json({
                    error: 'No existe el mensaje'
                });
                app.get("logger").error('No existe el mensaje con id ' + req.params.id);
            } else {
                if (conversation[0].receiver === res.user) {
                    var updateMessage = {
                        sender: conversation[0].sender,
                        receiver: conversation[0].receiver,
                        message: conversation[0].message,
                        date: conversation[0].date,
                        read: true
                    };
                    repository.updateElement(conversation[0], updateMessage, "messages", function (result) {
                        if (result == null) {
                            res.status(501);
                            res.json({
                                error: 'No se puede actualizar el mensaje'
                            });
                            app.get("logger").error('No se ha podido actualizar el mensaje con id ' + req.params.id);
                        } else {
                            res.status(200);
                            res.json({
                                result: 'Mensaje marcado correctamente'
                            });
                            app.get("logger").info('Actualizado correctamente el mensaje con id ' + req.params.id);
                        }
                    });
                }
            }
        });
    });

    function getFriends(res, email, functionCallBack) {
        repository.getElements({}, "users", function (users) {
            if (users == null || users.length === 0) {
                res.status(403); // Forbidden
                res.json({
                    error: 'No hay usuarios'
                });
                app.get("logger").error('No existen usuarios');
            } else {
                repository.getElements(email, "users", function (user) {
                    let request = {
                        receiver: user[0]._id.toString(),
                        status: "ACCEPTED"
                    };
                    repository.getElements(request, "requests", function (requests) {
                        if (requests == null || requests.length === 0) {
                            res.status(400);
                            res.json({
                                error: 'El usuario ' + email.email + " no tiene amistades."
                            });
                            app.get("logger").error('\'El usuario \' + email.email + " no tiene amistades"');
                        } else {
                            let collection = users.filter(function (user) {
                                for (let i = 0; i < requests.length; i++) {
                                    if (user._id.toString() === requests[i].sender) {
                                        delete user.password;
                                        return true;
                                    }
                                }
                                return false;
                            });
                            functionCallBack(collection)
                        }
                    });
                });
            }
        });
    }
};