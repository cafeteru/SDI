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
            } else {
                let token = app.get('jwt').sign({
                    user: user.email,
                    time: Date.now() / 1000
                }, "secreto");
                res.status(200);
                res.json({
                    authenticated: true,
                    token: token
                });
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
        });
    });

    app.post("/api/message/", function (req, res) {
        let email = {
            email: res.user
        };
        getFriends(res, email, function (friends) {
            let friend = friends.filter(function (element) {
                return element.email == req.body.email;
            });
            if (friend.length == 0) {
                res.status(401);
                res.json({
                    error: 'El receptor no es amigo'
                });
            } else {
                let message = {
                    sender: res.user,
                    receiver: req.body.email,
                    message: req.body.message,
                    date: timeStamp(),
                    read: false
                }
                repository.addElement(message, "messages", function (id) {
                    if (id == null) {
                        res.status(501);
                        res.json({
                            error: 'Error en el servidor al crear el mensaje'
                        });
                    } else {
                        res.status(200);
                        res.send(JSON.stringify(message));
                    }
                });
            }
        });
    });

    function getFriends(res, email, functionCallBack) {
        repository.getElements({}, "users", function (users) {
            if (users == null || users.length == 0) {
                res.status(403); // Forbidden
                res.json({
                    error: 'No hay usuarios'
                });
            } else {
                repository.getElements(email, "users", function (user) {
                    let request = {
                        receiver: user[0]._id.toString(),
                        status: "ACCEPTED"
                    };
                    repository.getElements(request, "requests", function (requests) {
                        if (requests == null || requests.length == 0) {
                            res.status(403);
                            res.json({
                                error: 'El usuario ' + email.email + " no tiene petición."
                            });
                        } else {
                            let collection = users.filter(function (user) {
                                for (let i = 0; i < requests.length; i++) {
                                    if (user._id.toString() == requests[i].sender) {
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

    function timeStamp() {
        // Create a date object with the current time
        var now = new Date();

        // Create an array with the current month, day and time
        var date = [now.getMonth() + 1, now.getDate(), now.getFullYear()];

        // Create an array with the current hour, minute and second
        var time = [now.getHours(), now.getMinutes(), now.getSeconds()];

        // Determine AM or PM suffix based on the hour
        var suffix = (time[0] < 12) ? "AM" : "PM";

        // Convert hour from military time
        time[0] = (time[0] < 12) ? time[0] : time[0] - 12;

        // If hour is 0, set it to 12
        time[0] = time[0] || 12;

        // If seconds and minutes are less than 10, add a zero
        for (var i = 1; i < 3; i++) {
            if (time[i] < 10) {
                time[i] = "0" + time[i];
            }
        }
        // Return the formatted string
        return date.join("/") + " " + time.join(":") + " " + suffix;
    }

    app.get("/api/message/", function (req, res) {
        let messages = {
            $or: [
                {
                    $and: [
                        {sender: res.user},
                        {receiver: req.query.email},
                    ]
                },
                {
                    $and: [
                        {sender: req.query.email},
                        {receiver: res.user},
                    ]
                }
            ]
        };
        repository.getElements(messages, "messages", function (conversation) {
            if (conversation == null || conversation.length == 0) {
                res.status(403);
                res.json({
                    error: 'No hay mensajes entre ' + res.user + " y " + req.params.email
                });
            } else {
                res.status(200);
                res.send(JSON.stringify(conversation));
            }
        });
    });

    app.get("/api/message/:id", function (req, res) {
        var message = {
            "_id": new ObjectId(req.params.id)
        };
        repository.getElements(message, "messages", function (conversation) {
            if (conversation == null || conversation.length == 0) {
                res.status(403);
                res.json({
                    error: 'No existe el mensaje'
                });
            } else {
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
                    } else {
                        res.status(200);
                        res.json({
                            result: 'Mensaje marcado correctamente'
                        });
                    }
                });
            }
        });
    });
};