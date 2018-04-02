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
        }
        var findByEmail = {email: req.body.email};
        usersRepository.getUsers(findByEmail, function (users) {
            if (users == null || users.length == 0) {
                usersRepository.addUser(user, function (id) {
                    if (id == null) {
                        res.redirect("/signup");
                        return;
                    } else {
                        res.redirect("/login");
                        return;
                    }
                });
            } else {npm install express --save
                npm install mongodb@2.2.33 --save // Base de datos
                npm install moment --save  // Librería para fechas
                npm install express-session --save // Para crear el objeto session
                npm install express-fileupload --save // Subir archivos
                npm install body-parser --save // Para poder acceder a los parámetros POST
                npm install swig --save // motor de plantillasignup?email=Este correo ya esta registrado.");
                return;
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
};