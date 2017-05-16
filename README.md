Cordova FileChooser Plugin

Requires Cordova >= 2.8.0

Install with Cordova CLI

```
$ cordova plugin add https://github.com/agilize/cordova-filechooser.git
```

Install with Plugman

```
$ plugman --platform android --project /path/to/project \
  --plugin https://github.com/agilize/cordova-filechooser.git
```

API

```
fileChooser.open(options, successCallback, failureCallback);
```
Options:

  * __allowedTypes__: Mime types allowed to select
  * __title__: Title of app choices

The success callback get the uri of the selected file

```
fileChooser.open({}, function(uri) {
  alert(uri);
});
```

Screenshot

![Screenshot](filechooser.png "Screenshot")

TODO rename `open` to pick, select, or choose.
