module.exports = {
  open: function (options, success, failure, close) {
    close = close || function () {};

    var _success = function (args) {
      if(args === "FileChooser::CLOSE") {
        close();
      }

      success(args);
    };

    cordova.exec(_success, failure, "FileChooser", "open", [options || {}]);
  }
};
