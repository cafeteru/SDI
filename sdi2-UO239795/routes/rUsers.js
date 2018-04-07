module.exports = function (app, swig, usersRepository, requestsRepository) {
    app.get("/signup", function (req, res) {
        let answer = swig.renderFile('views/signup.html', {});
        res.send(answer);
    });

    app.post('/signup', function (req, res) {
        let errors = checkErrorSignUp(req);
        if (errors.length > 0) {
            res.redirect("/signup?" + errors);
            return;
        }
        let password = app.get("crypto").createHmac('sha256', app.get('key'))
            .update(req.body.password).digest('hex');
        let user = {
            email: req.body.email,
            name: req.body.name,
            surName: req.body.surName,
            password: password,
            request: undefined
        };
        let findByEmail = {email: req.body.email};
        usersRepository.getUsers(findByEmail, function (users) {
            if (users == null || users.length === 0) {
                usersRepository.addUser(user, function (id) {
                    if (id == null) {
                        res.redirect("/signup");
                    } else {
                        let textSearch = {
                            email: req.body.email,
                            password: password
                        };
                        autoLogin(textSearch, req, res);
                    }
                });
            } else {
                res.redirect("/signup?email=Este correo ya esta registrado.");
            }
        });
    });

    function checkErrorSignUp(req) {
        let errors = new Array();
        if (req.body.email === "") {
            errors.push("email=Este campo no puede ser vacío");
        }
        if (req.body.name.length < 2) {
            errors.push("name=El nombre debe tener entre 5 y 24 caracteres.");
        }
        if (req.body.surName.length < 2) {
            errors.push("surName=El apellido debe tener entre 5 y 24 caracteres.");
        }
        if (req.body.password.length < 5) {
            errors.push("password=La contraseña debe tener entre 5 y 24 caracteres.");
        }
        if (req.body.passwordConfirm.length < 5) {
            errors.push("passwordConfirm=La contraseña debe tener entre 5 y 24 caracteres.");
        }
        if (req.body.password !== req.body.passwordConfirm) {
            errors.push("coincidence=Las contraseñas no coinciden.");
        }

        let url = "";
        for (let i = 0; i < errors.length; i++) {
            url += "&" + errors[i];
        }
        return url.substr(1, url.length);
    }

    app.get("/login", function (req, res) {
        let answer = swig.renderFile('views/login.html', {});
        res.send(answer);
    });

    app.post("/login", function (req, res) {
        let password = app.get("crypto").createHmac('sha256', app.get('key'))
            .update(req.body.password).digest('hex');
        let textSearch = {
            email: req.body.email,
            password: password
        };
        autoLogin(textSearch, req, res);
    });

    function autoLogin(textSearch, req, res) {
        usersRepository.getUsers(textSearch, function (users) {
            if (users == null || users.length === 0) {
                req.session.user = null;
                res.redirect("/login?error=Email o password incorrecto");
            } else {
                req.session.user = users[0].email;
                res.redirect("/list");
            }
        });
    }

    app.get("/home", function (req, res) {
        let answer = swig.renderFile('views/home.html', {
            user: req.session.user
        });
        res.send(answer);
    });

    app.get('/logout', function (req, res) {
        req.session.user = null;
        res.redirect("/login?logout=Ha cerrado la sesión correctamente");
    });

    app.get("/list", function (req, res) {
        let textSearch = {email: {$ne: req.session.user}};
        if (req.query.searchText != null) {
            let searchText = req.query.searchText.toLowerCase();
            textSearch = {
                $and: [
                    {email: {$ne: req.session.user}},
                    {
                        $or:
                            [
                                {email: {$regex: ".*" + searchText + ".*"}},
                                {name: {$regex: ".*" + searchText + ".*"}},
                                {surName: {$regex: ".*" + searchText + ".*"}}
                            ]
                    }
                ]
            };
        }

        let pg = parseInt(req.query.pg);
        if (req.query.pg == null) {
            pg = 1;
        }
        usersRepository.getUsersPg(textSearch, pg, function (users, total) {
            if (users == null) {
                res.send("Error al listar ");
            } else {
                let pgLast = total / 5;
                if (total % 5 > 0) {
                    pgLast = pgLast + 1;
                }
                let email = {
                    email: req.session.user
                };
                usersRepository.getUsers(email, function (user) {
                    let request = {
                        sender: user[0]._id.toString()
                    };
                    requestsRepository.getRequests(request, function (requests) {
                        for (let i = 0; i < users.length; i++) {
                            for (let j = 0; j < requests.length; j++) {
                                if (requests[j].receiver === users[i]._id.toString()) {
                                    users[i].request = requests[j];
                                    break;
                                }
                            }
                        }
                        let answer = swig.renderFile('views/users/list.html', {
                            users: users,
                            pgActual: pg,
                            pgLast: pgLast
                        });
                        res.send(answer);
                    });
                });
            }
        });
    });

    app.get("/requests", function (req, res) {
        let pg = parseInt(req.query.pg);
        if (req.query.pg == null) {
            pg = 1;
        }
        usersRepository.getUsers({}, function (users) {
            if (users == null) {
                res.send("Error al listar ");
            } else {
                let pgLast = users.length / 5;
                if (pgLast % 5 > 0) {
                    pgLast = pgLast + 1;
                }
                let email = {
                    email: req.session.user
                };
                usersRepository.getUsers(email, function (user) {
                    let request = {
                        receiver: user[0]._id.toString(),
                        status: "SENT"
                    };
                    requestsRepository.getRequests(request, function (requests) {
                        let collection = {};
                        let size = 0;
                        let i = 0;
                        for (; i < users.length; i++) {
                            for (let j = 0; j < requests.length; j++) {
                                if (users[i]._id.toString() === requests[j].sender) {
                                    collection[size++] = users[i];
                                    break;
                                }
                            }
                        }
                        i = (pg - 1) * 5;
                        let j = i + 5;
                        let pagUser = {};
                        let countLimit = 0;
                        for (; i < j; i++) {
                            if (i < size)
                                pagUser[countLimit++] = collection[i];
                        }
                        let answer = swig.renderFile('views/requests/receiver.html', {
                            users: pagUser,
                            pgActual: pg,
                            pgLast: pgLast
                        });
                        res.send(answer);
                    });
                });
            }
        });
    });
};