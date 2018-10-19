#!/bin/bash

# Define a timestamp function
timestamp() {
  date +"%T"
}

numberOfChildrenList=(10 30 50 70 90)
tournamentSizeList=(5 10 15 20 40)
childrenPerParentList=(1)
runsPerSet=100

mkdir results


export LD_LIBRARY_PATH=$pwd:

javac -cp contest.jar player70.java Individual.java
jar cmf MainClass.txt submission.jar player70.class Individual.class

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
	for runInd in $(seq 1 $runsPerSet)
	do
		echo "" #newline moest ik toch even zo doen want die newline command werkt niet goed
		echo "run $runInd - $(timestamp)"
		java -DchNum=$numberOfChildren -Dts=$tournamentSize -DchPar=$childrenPerParent -DrunIndex=$runInd -DoutDir=$outputDirName -jar testrun.jar -submission=player70 -evaluation=SchaffersEvaluation -seed=1 -nosec
		python saveplot.py $outputDirName $runInd
		echo "saved plot in python"
	done

	echo "done with runset, saving results from set..."

	python saveRunsetResult.py $runsPerSet $outputDirName

done
done
done

