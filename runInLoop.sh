#!/bin/bash

export LD_LIBRARY_PATH=$pwd:

for i in $(seq 1 10)
do
echo "run $i"
java -jar testrun.jar -submission=player70 -evaluation=BentCigarFunction -seed=1
done
