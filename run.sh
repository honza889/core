#!/bin/sh
mvn -Pdev install
cd build/app
chromium-browser http://127.0.0.1:8888/App.html?gwt.codesvr=127.0.0.1:9997 &
mvn gwt:run
cd ../..

