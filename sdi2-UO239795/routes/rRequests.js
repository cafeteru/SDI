module.exports = function (app, swig, usersRepository, requestsRepository) {
    app.post('/send/:id', function (req, res) {
        var email = {
            email: req.session.user
        }
        usersRepository.getUsers(email, function (users) {
            if (users == null || users.length == 0) {
                var request = {
                    sender: users[0]._id.toString(),
                    receiver: req.params.id,
                    status: "sent"
                };
                requestsRepository.addRequest(request, function () {
                    res.redirect("/list");
                });
            } else {
                res.redirect("/list");
            }
        });
    });
};