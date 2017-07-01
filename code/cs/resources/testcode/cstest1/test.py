#!/usr/bin/python
# Hello World using the ClearSilver CGI Kit and Python
import neo_cgi
# create a CGI handler context
ncgi = neo_cgi.CGI()
# parse the form data (and post upload data)
ncgi.parse()
# Load our static data
ncgi.hdf.readFile("hello.hdf")
ncgi.display("hello.cst")