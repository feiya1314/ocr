#! /bin/sh

for line in `cat ./requirements.txt`
yum install mesa-libGL
do
   echo print $line
   pip3 install $line
done
