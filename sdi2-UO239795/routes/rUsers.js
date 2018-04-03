module.exports = function (app, swig, usersRepository) {
    app.get("/signup", function (req, res) {
        var respuesta = swig.renderFile('views/signup.html', {});
        res.send(respuesta);
    });

    app.post('/signup', function (req, res) {
        var errors = checkErrorSignUp(req);
        if (errors.length > 0) {
            res.redirect("/signup?" + errors);
            return;
        }
        var password = app.get("crypto").createHmac('sha256', app.get('key'))
            .update(req.body.password).digest('hex');
        var user = {
            email: req.body.email,
            name: req.body.name,
            surName: req.body.surName,
            password: password
        };
        var findByEmail = {email: req.body.email};
        usersRepository.getUsers(findByEmail, function (users) {
            if (users == null || users.length == 0) {
                usersRepository.addUser(user, function (id) {
                    if (id == null) {
                        res.redirect("/signup");
                    } else {
                        var textSearch = {
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
        var errors = new Array();
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

        var url = "";
        for (var i = 0; i < errors.length; i++) {
            url += "&" + errors[i];
        }
        return url.substr(1, url.length);
    }

    app.get("/login", function (req, res) {
        var respuesta = swig.renderFile('views/login.html', {});
        res.send(respuesta);
    });

    app.post("/login", function (req, res) {
        var password = app.get("crypto").createHmac('sha256', app.get('key'))
            .update(req.body.password).digest('hex');
        var textSearch = {
            email: req.body.email,
            password: password
        };
        autoLogin(textSearch, req, res);
    });

    function autoLogin(textSearch, req, res) {
        usersRepository.getUsers(textSearch, function (users) {
            if (users == null || users.length == 0) {
                req.session.user = null;
                res.redirect("/login?error=Email o password incorrecto");
            } else {
                req.session.user = users[0].email;
                res.redirect("/home");
            }
        });
    }

    app.get("/home", function (req, res) {
        var respuesta = swig.renderFile('views/home.html', {
            user : req.session.user
        });
        res.send(respuesta);
    });

    app.get('/logout', function (req, res) {
        req.session.user = null;
        res.redirect("/login?logout=Ha cerrado la sesión correctamente");
    });
};