#!/bin/python
#coding=utf-8
from argparse import ArgumentParser
import os
import re
import shutil


p = ArgumentParser(usage='It is usage tip', description='build android argv')
p.add_argument('--name', default='ebank', type=str, help='The android package name')
p.add_argument('--zip', default='y', type=str, help='The Zip file generated [y|n]')
p.add_argument('--version', type=str, help='The upload version')
p.add_argument('--time', type=str, help='The upload version time')
p.add_argument('--path', default='file.zip', type=str, help='The offline file path')
p.add_argument('--offline', default="true", type=str, help='The package offline')

args = p.parse_args()

def runCommand(osVal):
    num = os.system(osVal)
    if num != 0:
        print osVal
        raise Exception("命令行异常代码:%d"%(num))

def make_dir(path):
    if os.path.exists(path) == False:
        os.mkdir(path)

def delFolder(path):
    if os.path.exists(path) == True:
        shutil.rmtree(path)

def outputXml(app, version, time):
    uploadSh = "--appid %s --label %s --getVersionFrom ./androidVersion --xmlPath ./AndroidManifest.xml --appPath ./output/%s/appInfo.xml"%(args.name,app,app)
    if args.version != None:
        uploadSh = "%s --setVersion %s"%(uploadSh,version)
    if args.time != None:
        uploadSh = "%s --setTime %s"%(uploadSh,time)
    runCommand("node ./assets/autobuild/generAppInfo.js %s"%(uploadSh))

def unzipFile(path):
    runCommand("unzip %s -d ./assets/offline"%(path))
    runCommand('unzip ./assets/offline/"*.zip" -d ./assets/offline/')
    runCommand("rm ./assets/offline/*.zip")

def clearFileContent(file):
    outfile = open(file, 'w')
    outfile.truncate()
    outfile.close()

def editPackageName(name):
    outfile1 = open('./res/values/strings.xml')
    fileVal2 = outfile1.read()
    p1 = re.compile(r'app_name.*<')
    fileVal3 = p1.sub('app_name">%s<'%(name), str(fileVal2))
    outfile1.close()
    output1 = open('./res/values/strings.xml', 'w')
    output1.write(fileVal3)
    output1.close()

def buildApk(app):
    delFolder("./output/%s"%(app))
    make_dir("./output")
    make_dir("./output/%s"%(app))
    delFolder("./assets/offline/")
    if args.offline == "true":
        unzipFile(args.path)
        runCommand("mv ./assets/offline/*.desc ./assets/offline.json")
    else:
        clearFileContent('./assets/offline.json')
    shutil.copyfile("./EMP/libs/armeabi/libluajava.so","./libs/armeabi/libluajava.so")
    editPackageName(app)
#    runCommand("cd EmpEditor && mvn clean install")
#    runCommand("cd EMP/RYTTrack && mvn clean install")
    runCommand("cd EMP && mvn clean install")
    runCommand("cd ViewServer && mvn clean install")
    runCommand("mvn clean package")
    if os.path.exists("./output/%s.zip"%(app)) == True:
        os.remove("./output/%s.zip"%(app))
    outputXml(app, args.version, args.time)
    shutil.copy("./target/Emp.apk","./output/%s"%(app))
    if args.zip == "y":
        runCommand("zip ./output/%s.zip ./output/%s/*"%(app,app))
        print "~~~~~~BUILD ZIP SUCCEEDED~~~~~~"
    else:
        print "~~~~~~BUILD SUCCEEDED~~~~~~"

buildApk(args.name)
