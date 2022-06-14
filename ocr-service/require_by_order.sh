#! /bin/sh

yum install mesa-libGL
for line in `cat ./requirements.txt`
do
   echo print $line
   pip3 install $line
done
