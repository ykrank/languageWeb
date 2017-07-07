var argv = require('optimist').usage('Usage: $0 --appid [appid] --getVersionFrom [path] --label [label] --xmlPath [path] --appPath [path]')
    .demand(['appid','label','getVersionFrom'])
    .default ({appPath: './appInfo.xml'})
    .describe('appid', 'appid')
    .describe('label', 'label')
    .describe('getVersionFrom', 'the path of the js file to get the version')
    .describe('setVersion', 'set the deploy version')
    .describe('xmlPath', 'the path of the xml file including the version')
    .describe('appPath', 'the path of the file to be output')
    .describe('setTime', 'the setTmie of the version-time')
    .argv;

var fs = require('fs');
var moment = require('moment');

var Ver = (require(argv.getVersionFrom).getVersion)();
var versionVal = function(setVal, getVal){
  if(setVal !== undefined){
    if (argv.setTime !== undefined) {
        return setVal + '.' + moment().format('YYYYMMDDHm');
    }else{
        return setVal;
    }
  }else{
    if (argv.setTime !== undefined) {
        return getVal + '.' + moment().format('YYYYMMDDHm');
    }else{
        return getVal;
    }
  }
};

function generAppInfo() {
    var text = '<?xml version="1.0" encoding="UTF-8"?>\n';
    text = text + '<appInfo>\n';
    text = text + ' <appid>' + argv.appid + '</appid>\n';
    text = text + ' <version>' + versionVal(argv.setVersion, Ver) + '</version>\n';
    text = text + ' <label>' + argv.label + '</label>\n';
    text = text + '</appInfo>';

    fs.writeFile(argv.appPath, text, 'utf8', function(err) {
        if (err) {
            console.log('Write AppInfo Error:' + err);
        } else {
            console.log('Write AppInfo Ok!\n' + '-------content of appInfo:---------\n' + text + '\n--------------------------------');
        }
    });
}


generAppInfo();
