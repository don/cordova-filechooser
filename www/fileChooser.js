module.exports = {
    open: function (success, failure, close) {
        var _success = function (args) {
            if(args == "FileChooser::CLOSE") {
                close();
            }
            success(args);
        } 
        cordova.exec(_success, failure, "FileChooser", "open", []);
    }
};
