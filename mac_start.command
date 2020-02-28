#!/bin/bash

path=$(find ~/ -type d -name FBLA-Coding-master 2>/dev/null)

cd $path

java_path=$(find / -type d -name jdk-13*.jdk 2>/dev/null)

$java_path"/Contents/Home/bin/java" -classpath ".:sqlite-jdbc-3.27.2.1.jar" --module-path "javafx-sdk-13/lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.web -jar "FBLA-Coding.jar"
