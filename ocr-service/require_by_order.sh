#! /bin/sh

for line in `cat ./requirements.txt`

do
   echo print $line
   pip3 install $line
done
