#!/bin/bash

DOCKER_USER=floriansdocker
IMAGE_NAME=lineage-backend
IMAGE_VERSION=1.0.11

docker build -t $IMAGE_NAME:$IMAGE_VERSION .
docker tag $IMAGE_NAME:$IMAGE_VERSION $DOCKER_USER/$IMAGE_NAME:$IMAGE_VERSION
docker push $DOCKER_USER/$IMAGE_NAME:$IMAGE_VERSION