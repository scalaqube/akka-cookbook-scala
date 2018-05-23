#!/bin/bash
echo "====================Removing target before making assembly================================"
rm -fr ../target && rm -fr docker/*
echo "====================Entering to Hello-Akka directory and creating assembly================"
cd ../ && sbt assembly
echo "====================Coming back to dockerApp directory===================================="
cd -
echo "====================Copying application jar to docker folder=============================="
sudo cp ../target/scala-2.11/Hello-Akka-assembly-1.0.jar docker/
echo "====================Building docker image================================================="
sudo docker build -t akka-app .
echo "====================Running docker container=============================================="
sudo docker run akka-app
