#!/bin/bash

echo "Configuring and building SLAM_SDVL ..."

mkdir build
cd build
cmake .. -DCMAKE_BUILD_TYPE=Release
make -j4
