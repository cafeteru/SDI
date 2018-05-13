module.exports = {
    mongo: null,
    app: null,
    init: function (app, mongo) {
        this.mongo = mongo;
        this.app = app;
    },
    getElements: function (element, nameCollection, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection(nameCollection);
                collection.find(element).toArray(function (err, elements) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(elements);
                    }
                    db.close();
                });
            }
        });
    },
    addElement: function (element, nameCollection, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection(nameCollection);
                collection.insert(element, function (err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result.ops[0]._id);
                    }
                    db.close();
                });
            }
        });
    },
    updateElement: function (element, updateElement, nameCollection, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection(nameCollection);
                collection.update(element, {
                    $set: updateElement
                }, function (err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result);
                    }
                    db.close();
                });
            }
        });
    },
    createQuery(req) {
        let textSearch = {
            email: {
                $ne: req.session.user
            }
        };
        if (req.query.searchText != null) {
            let searchText = req.query.searchText;
            textSearch = {
                $and: [{
                        email: {
                            $ne: req.session.user
                        }
                    },
                    {
                        $or: [{
                                email: {
                                    $regex: ".*" + searchText + ".*"
                                }
                            },
                            {
                                name: {
                                    $regex: ".*" + searchText + ".*"
                                }
                            },
                            {
                                surName: {
                                    $regex: ".*" + searchText + ".*"
                                }
                            }
                        ]
                    }
                ]
            };
        }
        return textSearch;
    },
    getMessagesBySenderAndReceiver(req, res) {
        let messages = {
            $or: [{
                    $and: [{
                            sender: res.user
                        },
                        {
                            receiver: req.query.email
                        },
                    ]
                },
                {
                    $and: [{
                            sender: req.query.email
                        },
                        {
                            receiver: res.user
                        },
                    ]
                }
            ]
        };
        return messages;
    },
    getAllMessages(res) {
        let user = {
            $or: [{
                    sender: res.user
                },
                {
                    receiver: res.user
                }
            ]
        };
    }
};