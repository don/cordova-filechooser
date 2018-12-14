module.exports = {
    open: function (filter, success, failure) {
        if (typeof filter === 'function') {
            failure = success;
            success = filter;
            filter = {};
        }

        cordova.exec(success, failure, "FileChooser", "open", [ filter ]);
    }
};
