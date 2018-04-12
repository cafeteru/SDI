module.exports = function (app, swig, usersRepository, requestsRepository,  ObjectId) {
    app.post('/send/:id', function (req, res) {
        let email = {
            email: req.session.user
        };
        usersRepository.getUsers(email, function (users) {
            if (users == null || users.length == 0) {
                res.redirect("/list?error=Usuario no existe");
            } else {
                let request = {
                    sender: users[0]._id.toString(),
                    receiver: req.params.id,
                    status: "SENT"
                };
                requestsRepository.getRequests(request, function (requests) {
                    if (requests == null || requests.length == 0) {
                        requestsRepository.addRequest(request, function () {
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
        requestsRepository.getRequests(request, function (requests) {
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
        requestsRepository.getRequests(request, function (requests) {
            let updateRequest;
            if (requests == null || requests.length == 0) {
                updateRequest = {
                    sender: senderParam,
                    receiver: receiverParam,
                    status: "ACCEPTED"
                };
                requestsRepository.addRequest(updateRequest, function (result) {
                    if (result == null) {
                        res.redirect("/list?error=No se ha podido aceptar la peticion");

                    }
                });
            } else {
                updateRequest = {
                    sender: requests[0].sender,
                    receiver: requests[0].receiver,
                    status: "ACCEPTED"
                };
                requestsRepository.updateRequest(requests[0], updateRequest, function (result) {
                    if (result == null) {
                        res.redirect("/list?error=No se ha podido aceptar la peticion");
                    }
                });
            }
        });
    }
};