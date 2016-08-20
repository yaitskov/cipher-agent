#!/bin/bash

java -cp ./target/cipher-demo-1.0-SNAPSHOT.jar \
    -javaagent:$PWD/smith.jar=$PWD/props.json \
    org.dan.cipher.demo.Main
