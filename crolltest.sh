#!/bin/bash

Q=$1
MIN=$2
MAX=$3

curl -X POST -H "Content-Type: application/json" -d \
"{\"q\":\"$Q\", \"minPrice\":\"$MIN\", \"maxPrice\":\"$MAX\"}" http://localhost:8080/crolling