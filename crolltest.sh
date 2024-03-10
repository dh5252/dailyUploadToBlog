#!/bin/bash

Q=$1
MIN=$2
MAX=$3

rm srcImage/description/0/origin/*
rm srcImage/description/0/result/*
rm srcImage/description/1/origin/*
rm srcImage/description/1/result/*
rm srcImage/description/2/origin/*
rm srcImage/description/2/result/*
rm srcImage/thumbnail/origin/*
rm srcImage/thumbnail/result/0/*
rm srcImage/thumbnail/result/1/*
rm srcImage/thumbnail/result/2/*

curl -X POST -H "Content-Type: application/json" -d \
"{\"q\":\"$Q\", \"minPrice\":\"$MIN\", \"maxPrice\":\"$MAX\"}" http://localhost:8080/crolling


