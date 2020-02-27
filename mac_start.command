#!/bin/bash

path=$(find ~/ -type d -name FBLA-Coding-master 2>/dev/null)

java -classpath $path"/sqlite-jdbc-3.27.2.1.jar" --module-path $path"/javafx-sdk-13/lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.web -jar $path"/FBLA-Coding.jar"
