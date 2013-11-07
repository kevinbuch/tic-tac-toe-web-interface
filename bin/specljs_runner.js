#! /usr/bin/env phantomjs

var fs = require("fs");
var p = require('webpage').create();
var sys = require('system');
p.injectJs('resources/public/jquery.js');

p.onConsoleMessage = function (x) {
    fs.write("/dev/stdout", x, "w");
};

p.injectJs(phantom.args[0]);

var result = p.evaluate(function () {
  specljs.run.standard.armed = true;
  return specljs.run.standard.run_specs(
     cljs.core.keyword("color"), true
  );
});

phantom.exit(result);
