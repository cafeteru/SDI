module.exports = {
    mongo: null,
    app: null,
    client: null,
    db: null,
    init: function (app, mongo) {
        this.mongo = mongo;
        this.app = app;
        this.client = new mongo.MongoClient(this.app.get('db'));
    },
    getDb: async function () {
        if (this.db) return this.db;
        try {
            await this.client.connect();
            console.log("INFO: Conexión exitosa a la base de datos");
            this.db = this.client.db();
            return this.db;
        } catch (err) {
            console.log("CRITICAL ERROR: Fallo al conectar a MongoDB: " + err);
            return null;
        }
    },
    getElements: function (element, nameCollection, funcionCallback) {
        this.getDb().then(db => {
            if (!db) return funcionCallback(null);
            db.collection(nameCollection).find(element).toArray()
                .then(elements => {
                    funcionCallback(elements);
                })
                .catch(err => {
                    console.log("ERROR en getElements: " + err);
                    funcionCallback(null);
                });
        }).catch(err => {
            console.log("ERROR: Excepción en getElements: " + err);
            funcionCallback(null);
        });
    },
    addElement: function (element, nameCollection, funcionCallback) {
        this.getDb().then(db => {
            if (!db) return funcionCallback(null);
            db.collection(nameCollection).insertOne(element)
                .then(result => {
                    funcionCallback(result.insertedId);
                })
                .catch(err => {
                    console.log("ERROR en addElement: " + err);
                    funcionCallback(null);
                });
        });
    },
    updateElement: function (element, updateElement, nameCollection, funcionCallback) {
        this.getDb().then(db => {
            if (!db) return funcionCallback(null);
            db.collection(nameCollection).updateOne(element, { $set: updateElement })
                .then(result => {
                    funcionCallback(result);
                })
                .catch(err => {
                    console.log("ERROR en updateElement: " + err);
                    funcionCallback(null);
                });
        });
    },
    createQuery(req) {
        let textSearch = { email: { $ne: req.session.user } };
        if (req.query.searchText != null) {
            let searchText = req.query.searchText;
            textSearch = {
                $and: [
                    { email: { $ne: req.session.user } },
                    {
                        $or: [
                            { email: { $regex: ".*" + searchText + ".*", $options: 'i' } },
                            { name: { $regex: ".*" + searchText + ".*", $options: 'i' } },
                            { surName: { $regex: ".*" + searchText + ".*", $options: 'i' } }
                        ]
                    }
                ]
            };
        }
        return textSearch;
    },
    getMessagesBySenderAndReceiver(req, res) {
        return {
            $or: [
                { $and: [{ sender: res.user }, { receiver: req.query.email }] },
                { $and: [{ sender: req.query.email }, { receiver: res.user }] }
            ]
        };
    },
    getAllMessages(res) {
        return {
            $or: [{ sender: res.user }, { receiver: res.user }]
        };
    }
};