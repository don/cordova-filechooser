module.exports = {
    open: function (filter, success, failure) {
        cordova.exec(success, failure, "FileChooser", "open", [ filter ]);
    }
};
