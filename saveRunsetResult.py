import pandas as pd
import sys
import os
import csv
import numpy as np


def calc_rc_log_distX(data):
    ylogdistX = data
    dif = []
    for y in range(1, len(ylogdistX)):
        dif.append(ylogdistX[y] - ylogdistX[y-1])
    # return sum(dif) / float(len(dif))
    return min(dif)

def main():
    runsPerSet = sys.argv[1]
    outputDirName = sys.argv[2]
    # tournamentSize = sys.argv[2]
    # childrenPerParent = sys.argv[3]
    dir_path = os.path.dirname(os.path.realpath(__file__))
    
    with open(dir_path + '/results/' + outputDirName + '/runset_' + outputDirName + '.csv','w+',newline='') as csvfile:
        
        datawriter = csv.writer(csvfile,delimiter=';')
        datawriter.writerow(['runIndex','gen_nine','maxFit','divX_nine','slopeLogDiv','divSig_nine','totDistSig_nine'])
        
        for runInd in range(1,int(runsPerSet)+1):
            data = pd.read_csv(dir_path + '/results/' + outputDirName + '/run_' + str(runInd) + '_' + outputDirName + '.csv',delimiter=';')
            ymax=np.array(data.Max)
            genList = np.array(data.Generation)
            logdivXList = np.array(data.LogDivX)
            divXList = np.array(data.DiversityX)
            divSigList = np.array(data.DiversitySigma)
            totDistSigList = np.array(data.TotalDistSigma)
            
            slopeLogDiv = calc_rc_log_distX(logdivXList)
            
            indList = np.where(ymax > 9.9)
            if indList[0].size == 0:
                sys.stdout.write("Fitness doesnt reach 9.9, inputting -1 for generation number and values at max fitness for rest")
                ind_g_nine = np.argmax(ymax) # Find the index where the max fitness is bigger than 9.9
                g_nine = -1
                maxFit = np.max(ymax)
                divX_nine = divXList[ind_g_nine]
                divSig_nine = divSigList[ind_g_nine]
                totDistSig_nine = totDistSigList[ind_g_nine]                                
            else:
                ind_g_nine = np.min(np.where(ymax > 9.9)) # Find the index where the max fitness is bigger than 9.9
                g_nine = genList[ind_g_nine]
                maxFit = np.max(ymax)
                divX_nine = divXList[ind_g_nine]
                divSig_nine = divSigList[ind_g_nine]
                totDistSig_nine = totDistSigList[ind_g_nine]
                
            datawriter.writerow([str(runInd),str(g_nine),str(maxFit),str(divX_nine),str(slopeLogDiv),str(divSig_nine),str(totDistSig_nine)])
            
            
            
            
if __name__ == "__main__":
	main()