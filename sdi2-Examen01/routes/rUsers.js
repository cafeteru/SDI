module.exports = function (app, swig, repository) {
    app.get("/signup", function (req, res) {
        let answer = swig.renderFile('views/signup.html', {});
        res.send(answer);
        app.get("logger").info('Usuario se va a registrar');
    });

    app.post('/signup', function (req, res) {
        app.get("logger").info('Usuario se intenta registrar');
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
            city: req.body.city,
            password: password
        };
        let findByEmail = {
            email: req.body.email
        };
        repository.getElements(findByEmail, "users", function (users) {
            if (users == null || users.length == 0) {
                repository.addElement(user, "users", function (id) {
                    if (id == null) {
                        res.redirect("/signup?email=Error al añadir al usuario. Intentelo más tarde");
                    } else {
                        let textSearch = {
                            email: req.body.email,
                            password: password
                        };
                        app.get("logger").info('Usuario registrado como ' + req.body.email);
                        autoLogin(textSearch, req, res);
                    }
                });
            } else {
                app.get("logger").error('Error al registrar al usuarios');
                res.redirect("/signup?email=Este correo ya esta registrado.");
            }
        });
    });

    function checkErrorSignUp(req) {
        let errors = [];
        if (req.body.email == "") {
            app.get("logger").error('Email vacío al registrar el usuario');
            errors.push("email=Este campo no puede ser vacío");
        }
        if (req.body.name.length < 2) {
            app.get("logger").error('Longitud del nombre invalida');
            errors.push("name=El nombre debe tener entre 5 y 24 caracteres.");
        }
        if (req.body.surName.length < 2) {
            app.get("logger").error('Longitud del apellido invalida');
            errors.push("surName=El apellido debe tener entre 5 y 24 caracteres.");
        }
        if (req.body.password.length < 5) {
            app.get("logger").error('Longitud de la contraseña invalida');
            errors.push("password=La contraseña debe tener entre 5 y 24 caracteres.");
        }
        if (req.body.passwordConfirm.length < 5) {
            app.get("logger").error('Longitud de la recontraseña invalida');
            errors.push("passwordConfirm=La contraseña debe tener entre 5 y 24 caracteres.");
        }
        if (req.body.password != req.body.passwordConfirm) {
            app.get("logger").error('No coinciden las contraseñas');
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
        app.get("logger").info('Usuario se intenta loguear');
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
        repository.getElements(textSearch, "users", function (users) {
            if (users == null || users.length == 0) {
                req.session.user = null;
                res.redirect("/login?error=Email o password incorrecto");
                app.get("logger").error('Error al loguear al usuario');
            } else {
                req.session.user = users[0].email;
                res.redirect("/list");
                app.get("logger").info('El usuario ' + req.session.user + " se ha logueado correctamente");
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
        app.get("logger").info('El usuario ' + req.session.user + " ha cerrado sesión correctamente");
        req.session.user = null;
        res.redirect("/login?logout=Ha cerrado la sesión correctamente");
    });

    app.get("/list", function (req, res) {
        app.get("logger").info('El usuario ' + req.session.user + " lista los usuarios de la aplicación");
        let pg = parseInt(req.query.pg);
        if (req.query.pg == null) {
            pg = 1;
        }
        repository.getElements(repository.createQuery(req), "users", function (users) {
            if (users == null) {
                res.send("Error al listar ");
            } else {
                let email = {
                    email: req.session.user
                };
                repository.getElements(email, "users", function (user) {
                    let request = {
                        sender: user[0]._id.toString()
                    };
                    repository.getElements(request, "requests", function (requests) {
                        let i = 0;
                        for (; i < users.length; i++) {
                            for (let j = 0; j < requests.length; j++) {
                                if (requests[j].receiver == users[i]._id.toString()) {
                                    users[i].request = requests[j];
                                    break;
                                }
                            }
                        }
                        i = (pg - 1) * 5;
                        let answer = swig.renderFile('views/users/list.html', {
                            users: users.slice(i, i + 5),
                            pgCurrent: pg,
                            pgLast: calculatePgLast(users.length),
                            user: req.session.user
                        });
                        res.send(answer);
                    });
                });
            }
        });
    });

    app.get("/requests", function (req, res) {
        app.get("logger").info('El usuario ' + req.session.user + " lista su peticiones de amistades recibidas");
        searchPeople(req, res, "SENT", 'views/requests/receiver.html')
    });

    app.get("/friends", function (req, res) {
        app.get("logger").info('El usuario ' + req.session.user + " lista sus amigos");
        searchPeople(req, res, "ACCEPTED", 'views/users/friends.html')
    });

    function searchPeople(req, res, status, view) {
        let pg = parseInt(req.query.pg);
        if (req.query.pg == null) {
            pg = 1;
        }
        repository.getElements({}, "users", function (users) {
            if (users == null) {
                res.send("Error al listar");
            } else {
                let email = {
                    email: req.session.user
                };
                repository.getElements(email, "users", function (user) {
                    let request = {
                        receiver: user[0]._id.toString(),
                        status: status
                    };
                    repository.getElements(request, "requests", function (requests) {
                        let collection = users.filter(function (user) {
                            for (let i = 0; i < requests.length; i++) {
                                if (user._id.toString() == requests[i].sender) {
                                    user.request = requests[i]._id.toString();
                                    return true;
                                }
                            }
                            return false;
                        });
                        let limit = (pg - 1) * 5;
                        let answer = swig.renderFile(view, {
                            users: collection.slice(limit, limit + 5),
                            pgCurrent: pg,
                            pgLast: calculatePgLast(collection.length)
                        });
                        res.send(answer);
                    });
                });
            }
        });
    }

    function calculatePgLast(value) {
        let pgLast = value / 5;
        if (value % 5 > 0) {
            return pgLast + 1;
        } else if (value == 0) {
            return 0;
        }
    }
};