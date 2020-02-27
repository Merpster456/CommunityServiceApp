#!/bin/bash

path=$(find ~/ -type d -name FBLA-Coding-master 2>/dev/null)

cd $path

java -classpath ".:sqlite-jdbc-3.27.2.1.jar" --module-path "javafx-sdk-13/lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.web -jar "FBLA-Coding.jar"
