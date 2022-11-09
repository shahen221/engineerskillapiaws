#!/bin/bash

if [ "$LIFECYCLE_EVENT" == "ApplicationStart" ]; then
    sudo docker run -p8001:8001 shahen221/engineerskillapiaws:latest 
    exit
fi