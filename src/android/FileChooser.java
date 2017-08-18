package com.megster.cordova;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;

public class FileChooser extends CordovaPlugin {

    private static final String TAG = "FileChooser";
    private static final String ACTION_OPEN = "open";
    private static final String ACTION_CREATE = "create";
    private static final int PICK_FILE_REQUEST = 1;
    private static final int CREATE_FILE_REQUEST = 2;
    CallbackContext callback;

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {

        if (action.equals(ACTION_OPEN)) {
            chooseFile(callbackContext);
            return true;
        }

        if (action.equals(ACTION_CREATE)) {
		chooseFileToCreate(callbackContext);
		return true;
	}

        return false;
    }

    public void chooseFile(CallbackContext callbackContext) {

        // type and title should be configurable

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        Intent chooser = Intent.createChooser(intent, "Select File");
        cordova.startActivityForResult(this, chooser, PICK_FILE_REQUEST);

        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callback = callbackContext;
        callbackContext.sendPluginResult(pluginResult);
    }

    /* 
     * File "creation" version of the "open" method, by Pat Deegan, https://psychogenic.com
     *
     * This android-native dialog allows you to move around the filesystem, create folders,
     * and select or enter a filename for creation.
     *  
     * NOTE 1: this file _is_ created.  For instance, I've been using this with the File plugin/lib
     * so I run filechooser create(), then use the cordova-plugin-file to _overwrite_ the contents of that
     * file with my data.
     *
     * NOTE 2: the returned URI is a funky content://XYZ/%2Fencoded%2FURI%2FtoFile and I had to muck about 
     * to make it usable in my cordova app.
     *
     * WARNING: if the user in in some directory and hits save without entering a filename, you get the content
     * URI back, but it points to a _directory_ so... caveat emptor.
     */
    // TODO: would be nice to pass in: suggested file name, mime-type.
    public void chooseFileToCreate(CallbackContext callbackContext) {
	        Intent intent = new Intent().setAction(Intent.ACTION_CREATE_DOCUMENT).setType("*/*");
		intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

		Intent chooser = Intent.createChooser(intent, "Select file to create");


		cordova.startActivityForResult(this, chooser, CREATE_FILE_REQUEST);


        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callback = callbackContext;
        callbackContext.sendPluginResult(pluginResult);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if ((requestCode == CREATE_FILE_REQUEST || requestCode == PICK_FILE_REQUEST ) && callback != null) {

            if (resultCode == Activity.RESULT_OK) {

                Uri uri = data.getData();

                if (uri != null) {

                    Log.w(TAG, uri.toString());
                    callback.success(uri.toString());

                } else {

                    callback.error("File uri was null");

                }

            } else if (resultCode == Activity.RESULT_CANCELED) {

                // TODO NO_RESULT or error callback?
                PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
                callback.sendPluginResult(pluginResult);

            } else {

                callback.error(resultCode);
            }
        }
    }
}
