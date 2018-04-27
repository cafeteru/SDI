module.exports = function (app, usersRepository, requestsRepository) {
    app.post("/api/login/", function (req, res) {
        let seguro = app.get("crypto").createHmac('sha256', app.get('key')).update(req.body.password).digest('hex');
        let criterio = {
            email: req.body.email,
            password: seguro
        };
        usersRepository.getUsers(criterio, function (users) {
            if (users == null || users.length == 0) {
                res.status(401); // Unauthorized
                res.json({
                    authenticated: false,
                    error: 'Identificación no válida'
                });
            } else {
                let token = app.get('jwt').sign({
                    user: criterio.email,
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

    app.get("/api/friends/", function (req, res) {
        let email = {
            email: res.user
        };
        getFriends(res, email, function (emails) {
            res.status(200);
            res.send(JSON.stringify(emails));
        });
    });

    function getFriends(res, email, functionCallBack) {
        usersRepository.getUsers({}, function (users) {
            if (users == null || users.length == 0) {
                res.status(403); // Forbidden
                res.json({
                    error: 'No hay usuarios'
                });
            } else {
                usersRepository.getUsers(email, function (user) {
                    let request = {
                        receiver: user[0]._id.toString(),
                        status: "ACCEPTED"
                    };
                    requestsRepository.getRequests(request, function (requests) {
                        if (requests == null || requests.length == 0) {
                            res.status(403);
                            res.json({
                                error: 'El usuario ' + email.email + " no tiene petición."
                            });
                        } else {
                            let collection = users.filter(function (user) {
                                for (let i = 0; i < requests.length; i++) {
                                    if (user._id.toString() == requests[i].sender) {
                                        return true;
                                    }
                                }
                                return false;
                            });

                            let emails = [];
                            let count = 0;
                            for (let i = 0; i < collection.length; i++) {
                                emails[count++] = collection[i].email;
                            }
                            functionCallBack(emails)
                        }
                    });
                });
            }
        });
    }


};


