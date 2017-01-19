package com.megster.cordova;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import android.database.Cursor;
import android.provider.OpenableColumns;

import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;

public class FileChooser extends CordovaPlugin {

    private static final String TAG = "FileChooser";
    private static final String ACTION_OPEN = "open";
    private static final int PICK_FILE_REQUEST = 1;
    CallbackContext callback;

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {

        if (action.equals(ACTION_OPEN)) {
            chooseFile(callbackContext);
            return true;
        }

        return false;
    }

    public void chooseFile(CallbackContext callbackContext) {

        // type and title should be configurable

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        //intent.putExtra(Intent.EXTRA_LOCAL_ONLY, false);

        Intent chooser = Intent.createChooser(intent, "Select File");
        cordova.startActivityForResult(this, chooser, PICK_FILE_REQUEST);

        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callback = callbackContext;
        callbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_FILE_REQUEST && callback != null) {

            if (resultCode == Activity.RESULT_OK) {

                Uri uri = data.getData();

                if (uri != null) {

                    Log.w(TAG, uri.toString());

                    try {
                        Context context = cordova.getActivity();
                        String mimeType = context.getContentResolver().getType(uri);

                        Uri returnUri = data.getData();
                        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();
                        String filename = returnCursor.getString(nameIndex);

                        File fileSave = context.getExternalFilesDir(null);
                        String sourcePath = context.getExternalFilesDir(null).toString();

                        try {

                            File savedFile = new File(sourcePath + "/" + filename);

                            copyFileStream(savedFile, uri, context);

                            callback.success(savedFile.toURI().toString());

                        } catch (Exception e) {
                            Log.w(TAG, e.getMessage());
                        }

                    } catch (Exception e) {
                        Log.w(TAG, e.getMessage());
                    }
                }


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

    private void copyFileStream(File dest, Uri uri, Context context) throws IOException {
        java.io.InputStream is = null;
        java.io.OutputStream os = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            os = new java.io.FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
        }
    }

}
