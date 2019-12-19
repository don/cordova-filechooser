const pickerLocationId = Windows.Storage.Pickers.PickerLocationId;
const pickerCancelMessage = "File uri was null"
const pickerErrorMessage = "Error picking file!";

module.exports = {

    open: function(successCallback, errorCallback, args) {
        // create picker
        var fileOpenPicker = new Windows.Storage.Pickers.FileOpenPicker();
        var mimes = args[0];
        var mimeTypesArray = mimes.mime.split(", ");
        
        // if no mime types are being passed, then accept all files.
        if(mimeTypesArray.length < 1) {
            mimeTypesArray.push('*');
        }
        
        // set file mask to all files
        fileOpenPicker.fileTypeFilter.replaceAll(mimeTypesArray);
        // and start location to documents library
        fileOpenPicker.suggestedStartLocation = pickerLocationId.documentsLibrary;

        // open picker async and return file path on success
        fileOpenPicker.pickSingleFileAsync().done(function (file) {
            if (!file || !file.path) {
                errorCallback(pickerCancelMessage);
                return;
            }

            // file must be copied to local folder to be accessible by app..
            const localFolder = Windows.Storage.ApplicationData.current.localFolder;
            file.copyAsync(localFolder, file.name, Windows.Storage.NameCollisionOption.replaceExisting)
            .done(function (savedFile) {
                successCallback('ms-appdata:///local/' + savedFile.name);
            }, function(err) {
                errorCallback(pickerErrorMessage);
            });
        }, function () {
            errorCallback(pickerErrorMessage);
        });
    }

}

require('cordova/exec/proxy').add('FileChooser', module.exports);
