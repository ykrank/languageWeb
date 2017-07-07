#!/usr/bin/env node
var argv = require('optimist').usage('Usage: $0 --zipPath [the path of the zip file to be uploaded] --isInstaller [0|1] ')
    .demand(['zipPath'])
    .
default ({
    user: 'admin',
    password: 'admin',
    host: '192.168.64.127',
    port: '4444',
    isInstaller: '0',
    isRecommended: '0',
    isActive:'0',
    isUpdateNotify:'1',
    oldestOsVer:'',
    latestOsVer:'',
    pubDesc:'upload auto'
})
    .describe('zipPath', 'The path of the file to be uploaded including the file name.')
    .describe('user', 'The user name used to login.')
    .describe('password', 'The password used to login.')
    .describe('isInstaller', ' 0 : Download from Web(default) ; 1 : Download from AppShop')
    .describe('isRecommended', '0 ：Recommended(default); 1 Not Recommended ')
    .describe('isActive', '0 :Active(default) ; 1 : not Active')
    .describe('isUpdateNotify', '0: Update notify ;1: Not Update Notify (default)')
    .describe('oldestOsVer', 'The oldest Os Version.')
    .describe('latestOsVer', 'The latest Os Version.')
    .describe('pubDesc', 'The publish description.')
    
    .argv;


var http = require('http');
var fs = require('fs');
var events = require('events');
var emitter = new events.EventEmitter();
var xmlreader = require('xmlreader');

/****************************登录********************************/

function loginEmas() {
    console.log('**********************logining******************');
    var data_login = {
        loginName: argv.user,
        password: argv.password
    };
    data_login = require('querystring').stringify(data_login);

    var options_login = {
        method: 'POST',
        host: argv.host,
        port: argv.port,
        path: '/session',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Content-Length': data_login.length
        }
    };

    var req_login = http.request(options_login, function(res_login) {

        if (res_login.statusCode === 200) {
            var body = '';
            res_login.on('data', function(chunk) {
                body += chunk;
            }).on('end', function() {
                console.log("login response: " + body);
                var r = JSON.parse(body);
                if (r.header.errorMsg === 'success') {
                    console.log("Login Success!");
                    var cookie = res_login.headers['set-cookie'];
                    emitter.emit("login_success", cookie);
                } else {
                    console.log("Login Failed!");
                }
            });
        } else {
            console.log('statusCode: ' + res_login.statusCode);
        }

    });
    req_login.write(data_login);
    req_login.end();
}

/***********************拼接请求数据*******************************/

function buildRequest(path) {
    var boundary = '----multipartformboundary' + (new Date()).getTime();
    var dashdash = '--';
    var crlf = '\r\n';

    /* Build RFC2388. */
    var builder = '';

    builder += dashdash;
    builder += boundary;
    builder += crlf;

    builder += 'Content-Disposition: form-data; name="' + 'file' + '"';
    //支持文件名为中文
    builder += '; filename="' + encodeURIComponent(path.replace(/.+\//, '')) + '"';
    builder += crlf;

    builder += 'Content-Type: application/zip';
    builder += crlf;
    builder += crlf;

    /* 写入文件 */
    builder += fs.readFileSync(path, 'binary');
    builder += crlf;
    /* 写入边界 */
    builder += dashdash;
    builder += boundary;
    builder += dashdash;
    builder += crlf;

    return {
        contentType: 'multipart/form-data; boundary=' + boundary,
        body: builder
    };
}

/****************************上传********************************/

function uploadFile(cookie) {
    console.log('***********************uploading**********************');
    var requestData = buildRequest(argv.zipPath);
    var options_upload = {
        method: 'POST',
        host: argv.host,
        port: argv.port,
        path: '/app',
        headers: {
            'Cookie': cookie[0],
            'Content-Length': requestData.body.length,
            'Content-Type': requestData.contentType
        }
    };

    var req_upload = http.request(options_upload, function(res_upload) {
        if (res_upload.statusCode === 200) {
            var data = '';
            res_upload.on('data', function(chunk) {
                data += chunk;
            }).on('end', function() {
                console.log("upload response: " + data);
                var r = JSON.parse(data);
                if (r.header.errorMsg === 'success') {
                    console.log('Upload File Success!');
                    var appInfo = r.body;
                    emitter.emit("upload_success", appInfo, cookie);
                } else {
                    throw r.header.errorMsg;
                }

            });
        } else {
            console.log('res_upload.statusCode:' + res_upload.statusCode);
        }
    });

    req_upload.write(requestData.body, "binary");
    req_upload.end();
}

/****************************处理************************************/

function configureFile(appInfo, cookie) {
    console.log('**********************configuring******************');
    var data_configure = {
        label: appInfo.label,
        description: appInfo.label,
        isRecommended: argv.isRecommended,
        isInstaller: argv.isInstaller,
        isUpdateNotify: argv.isUpdateNotify,
        isActive: argv.isActive,
        intVer: appInfo.intVer,
        comVer: appInfo.comVer,
        oldestOsVer: argv.oldestOsVer,
        latestOsVer: argv.latestOsVer,
        pubDesc: argv.pubDesc
    };
    console.log(data_configure);
    data_configure = require('querystring').stringify(data_configure);
    var options_configure = {
        method: 'PUT',
        host: argv.host,
        port: argv.port,
        path: '/app/' + appInfo.appID,
        headers: {
            'Cookie': cookie[0],
            'Content-Length': data_configure.length,
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    };
    var req_configure = http.request(options_configure, function(res_configure) {
        if (res_configure.statusCode === 200) {
            var data = '';
            res_configure.on('data', function(chunk) {
                data += chunk;
            }).on('end', function() {
                console.log("configure response: " + data);
                var r = JSON.parse(data);
                if (r.header.errorMsg === 'success') {
                    console.log('Finish Upload!');
                } else {
                    console.log("Configure File Failed!");
                }
            });
        }
    });
    req_configure.write(data_configure);
    req_configure.end();
}


/*****************************执行****************************************/
emitter.on("login", function() {
    loginEmas();
});

emitter.on("login_success", function(cookie) {
    uploadFile(cookie);
});

emitter.on("upload_success", function(appInfo, cookie) {
    configureFile(appInfo, cookie);
});

emitter.emit("login");
