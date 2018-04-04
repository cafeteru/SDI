module.exports = function (app, swig, usersRepository, requestsRepository) {
    app.post('/send/:id', function (req, res) {
        var email = {
            email: req.session.user
        }
        usersRepository.getUsers(email, function (users) {
            if (users == null || users.length == 0) {
                res.redirect("/list?error=Usuario no existe");
            } else {
                var request = {
                    sender: users[0]._id.toString(),
                    receiver: req.params.id,
                    status: "SENT"
                };
                requestsRepository.getRequests(request, function (requests) {
                    if (requests == null || requests.length === 0) {
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
};