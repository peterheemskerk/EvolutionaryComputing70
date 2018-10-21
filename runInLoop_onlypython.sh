#!/bin/bash

# Define a timestamp function
timestamp() {
  date +"%T"
}

numberOfChildrenList=(10 30 50 70 90)
tournamentSizeList=(5 10 15 20 40)
childrenPerParentList=(2)
runsPerSet=25

mkdir results

for childrenPerParent in ${childrenPerParentList[@]}
do
	for tournamentSize in ${tournamentSizeList[@]}
do
	for numberOfChildren in ${numberOfChildrenList[@]}
do
outputDirName="chNum_${numberOfChildren}_ts_${tournamentSize}_chPar_${childrenPerParent}"

echo "-------------------"
echo "PARAMETER SET $outputDirName"
echo "-------------------"
echo ""

cd ./results
mkdir $outputDirName
cd ..

	echo "done with runset, saving results from set..."

	python saveRunsetResult.py $runsPerSet $outputDirName

done
done
done

