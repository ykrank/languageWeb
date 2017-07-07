#!/usr/bin/env node

var argv = require('optimist')
    .usage('Usage: $0 --name [emp|emas]')
    .default({
        name: 'emp'
    })
    .demand('name')
    .describe('name', 'The build package name')
    .describe('zip', 'The Zip file generated [y|n]')
    .describe('version', 'The upload version')
    .describe('time', 'The upload version time')
    .argv;

var mkdirp = require('mkdirp');
var fs = require('fs');
var process = require('child_process');
var async = require('async');

var uploadSh = 'node ./assets/autobuild/generAppInfo.js --appid ' + argv.name +' --label ' + argv.name + ' --getVersionFrom ./androidVersion --xmlPath ./AndroidManifest.xml --appPath ./output/' + argv.name + '/appInfo.xml';
if(argv.version !== undefined){
  uploadSh = uploadSh + ' --setVersion ' + argv.version;
}
if(argv.time !== undefined){
  uploadSh = uploadSh + ' --setTime ' + argv.time;
}

var editPackageName = function(inputEdit, eventFunction){
  var fileVal = fs.readFileSync('./AndroidManifest.xml', 'utf8');
  var fileVal1 = fileVal.replace(/package=".*"/, 'package="com.rytong.emp.' + inputEdit + '"');
  fs.writeFileSync('./AndroidManifest.xml', fileVal1, 'utf8');
  var fileVal2 = fs.readFileSync('./res/values/strings.xml', 'utf8');
  var fileVal3 = fileVal2.replace(/app_name.*</, 'app_name">' + inputEdit + '<');
  fs.writeFileSync('./res/values/strings.xml', fileVal3, 'utf8');
  eventFunction();
};

var MakeShell = function(pathShell, eventFunction) {
    process.exec(pathShell, function(error, stdout, stderr) {
        if(stdout !== ''){
          console.log('stdout: ' + stdout);
        }
        if(stderr !== ''){
          console.log('stderr: ' + stderr);
        }
        if (error) {
            console.log(error);
        } else {
            eventFunction(error);
        }
    });
};

var checkPath = function(path, eventFunction) {
  fs.exists(path, function(exists) {
    if (!exists) {
      mkdirp(path, function(err) {
        if (err) {
          console.log(err);
          throw err;
        }
        eventFunction();
      });
    }
    eventFunction();
  });
};

function done(err, results){
  if(err){
    throw err;
  }
  // console.log('results: %j', results);
}

async.series([
  function(next){
    MakeShell('rm -rf ./assets/offline/* && cp ./EMP/libs/armeabi/libluajava.so ./libs/armeabi/libluajava.so', next);
  },

  function(next){
    checkPath('./output/' + argv.name, next);
  },

  function(next){
    MakeShell('unzip file.zip -d ./assets/offline && cd ./assets/offline && unzip "*.zip" -d ./ && rm *.zip', next);
  },

  function(next){
    fs.readdir('./assets/offline/', function(err, files){
      for(var i=0; i < files.length; i++){
        if(files[i].search(/\.desc/) !== -1){
            fs.rename('./assets/offline/' + files[i], './assets/offline.json', next);
        }
      }
    });
  },

  function(next){
    MakeShell('cd ./EmpEditor && mvn clean install && cd ../EMP/RYTTrack && mvn clean install && cd ../ && mvn clean install && cd ../ViewServer && mvn clean install && cd ../ && mvn clean package', next);
  }, 

  function(next){
    editPackageName(argv.name, next);
  },

  function(next){
    MakeShell('rm -rf ./output/' + argv.name +'/* && rm -rf ./output/' + argv.name +'.zip', next);
  },

  function(next){
    MakeShell('mv ./target/Emp.apk ./output/' + argv.name, function() {
        if (argv.zip === 'y') {
            MakeShell(uploadSh, function() {
                MakeShell('zip  ./output/' + argv.name + '.zip ./output/' + argv.name + '/*', function() {
                    console.log('ZipFile is ok!');
                    next();
                });
            });
        } else {
            console.log('build apk package success');
            next();
        }
    });
  }

], done);
