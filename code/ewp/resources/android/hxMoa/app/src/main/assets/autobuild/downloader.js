#!/usr/bin/env node

var argv = require('optimist').usage('Usage: $0 --platform [iphone|android|wp|qt] --path [path]')
    .demand(['platform'])
    .default({
        path: 'file.zip', user: 'admin', password: 'admin', app: 'ebank', host: '192.168.64.127', port: '4000', version: '1'
    })
    .describe('platform', 'The platform of the offline resource to be downloaded')
    .describe('path', 'The path of the downloaded file to be stored including the file name.')
    .describe('user', 'The user name used to login.')
    .describe('password', 'The password used to login.')
    .describe('app', 'The app name need to be downloaded.')
    .describe('host', 'The IP of the server.')
    .describe('port', 'The port of the server.')
    .describe('version', 'The version of the offlines version.')
    .argv;


var http = require('http');
var fs = require('fs');
var ProgressBar = require('progress');

var bar = new ProgressBar(':bar', {
    total: 10
});
var offlineResourceConfig = {
    iphone: '/admin/resource/download_package?download_platform=iphone&download_resolution=480*320',
    wp: '/admin/resource/download_package?download_platform=wp&download_resolution=480*320',
    android: '/admin/resource/download_package?download_platform=android&download_resolution=480*320',
    qt: '/admin/resource/download_package?download_platform=qt&download_resolution=480*320'
};
var data = {
    user: argv.user,
    password: argv.password,
    app: argv.app
}
data = require('querystring').stringify(data);

var options = {
    method: 'POST',
    host: argv.host,
    port: argv.port,
    path: '/admin/login',
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Content-Length': data.length
    }
};
var req = http.request(options, function (serverFeedback) {
    if (serverFeedback.statusCode == 200) {
        var body = '';
        serverFeedback.on('data',function (data) {
            body += data;
        }).on('end', function () {
                console.log("login response: " + body);
                var r = JSON.parse(body);
                if (r.message === 'ok') {
                    var cookie = serverFeedback.headers['set-cookie'];

                    options.path = offlineResourceConfig[argv.platform] + '&version=' + argv.version;
                    options.headers = {
                        'Cookie': cookie[0]
                    };

                    var file = fs.createWriteStream(argv.path);
                    var request = http.get(options, function (res) {
                        res.pipe(file);
                    })
                    //add progress bar
                    request.on('response', function (res) {
                        var len = parseInt(res.headers['content-length'], 10);

                        var bar = new ProgressBar('downloading [:bar] :percent :etas', {
                            complete: '=',
                            incomplete: ' ',
                            width: 20,
                            total: len
                        });

                        res.on('data', function (chunk) {
                            bar.tick(chunk.length);
                        });

                        res.on('end', function () {
                            console.log('\n');
                        });
                    });
                }
            });
    } else {
        console.log('error');
    }
});
req.write(data);
req.end();
