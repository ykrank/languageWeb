var argv = require('optimist').usage('Usage: $0 --xmlPath [path] ')
    .demand(['xmlPath'])
    .argv;

var fs = require('fs');
var xmlreader = require('xmlreader');

function getVersion() {
    var version = '';
    var xmlFile = fs.readFileSync(argv.xmlPath, 'utf8').replace("\ufeff", "");
    xmlreader.read(xmlFile, function(err, content) {
        if (err) {
            console.log(err);
        } else {
            var root = content.manifest;
            var att= root.attributes();
            version = att['android:versionName'];
        }

    });
    return version;
}

exports.getVersion=getVersion;

