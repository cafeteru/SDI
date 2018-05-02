module.exports = function (app, swig, repository, ObjectId) {
    app.post('/send/:id', function (req, res) {
        let email = {
            email: req.session.user
        };
        repository.getElements(email, "users", function (users) {
            if (users == null || users.length == 0) {
                res.redirect("/list?error=Usuario no existe");
            } else {
                let request = {
                    sender: users[0]._id.toString(),
                    receiver: req.params.id,
                    status: "SENT"
                };
                repository.getElements(request, "requests", function (requests) {
                    if (requests == null || requests.length == 0) {
                        repository.addElement(request, "requests", function () {
                            res.redirect("/list?success=Petición enviada correctamente");
                        });
                    } else {
                        res.redirect("/list?error=Petición ya enviada");
                    }
                });
            }
        });
    });

    app.post('/accepted/:id', function (req, res) {
        let request = {
            "_id": new ObjectId(req.params.id)
        };
        repository.getElements(request, "requests", function (requests) {
            if (requests != null || requests.length > 0) {
                acceptedRequest(requests[0].sender, requests[0].receiver);
                acceptedRequest(requests[0].receiver, requests[0].sender);
                res.redirect("/list?success=Petición aceptada correctamente");
            } else {
                res.redirect("/list?error=No se ha podido aceptar la peticion");
            }
        });
    });

    function acceptedRequest(senderParam, receiverParam) {
        let request = {
            sender: senderParam,
            receiver: receiverParam
        };
        repository.getElements(request, "requests", function (requests) {
            let updateRequest = {
                sender: senderParam,
                receiver: receiverParam,
                status: "ACCEPTED"
            };
            if (requests == null || requests.length == 0) {
                repository.addElement(updateRequest, "requests", function (result) {
                    if (result == null) {
                        res.redirect("/list?error=No se ha podido aceptar la peticion");
                    }
                });
            } else {
                repository.updateElement(requests[0], updateRequest, "requests", function (result) {
                    if (result == null) {
                        res.redirect("/list?error=No se ha podido aceptar la peticion");
                    }
                });
            }
        });
    }
};