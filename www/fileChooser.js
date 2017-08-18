module.exports = {
    open: function (success, failure) {
        cordova.exec(success, failure, "FileChooser", "open", []);
    },
    create: function (success, failure) {
        cordova.exec(success, failure, "FileChooser", "create", []);
    }
};
