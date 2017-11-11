const pickerLocationId = Windows.Storage.Pickers.PickerLocationId;
const pickerCancelMessage = "File uri was null"
const pickerErrorMessage = "Error picking file!";

module.exports = {

    open: function(successCallback, errorCallback, args) {
        // create picker
        var fileOpenPicker = new Windows.Storage.Pickers.FileOpenPicker();

        // set file mask to all files
        fileOpenPicker.fileTypeFilter.replaceAll(['*']);
        // and start location to documents library
        fileOpenPicker.suggestedStartLocation = pickerLocationId.documentsLibrary;

        // open picker async and return file path on success
        fileOpenPicker.pickSingleFileAsync().done(function (file) {
            if (!file || !file.path) {
                errorCallback(pickerCancelMessage);
                return;
            }
            successCallback(file.path);
        }, function () {
            errorCallback(pickerErrorMessage);
        });
    }

}

require('cordova/exec/proxy').add('FileChooser', module.exports);
