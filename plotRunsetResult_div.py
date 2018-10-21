import pandas as pd

import csv
import numpy as np
from mpl_toolkits import mplot3d
import matplotlib.pyplot as plt
import matplotlib as mpl

# Dit bestand hoort in de results folder

def nan_if(arr, value):
    return np.where(arr == value, np.nan, arr)

def main():
    
    numChildrenList = [10,30,50,70,90]
    tournamentSizeList = [5,10,15,20,40]
    childrenPerParent = 2
    
    '''
    font = {'family' : 'normal',
        'weight' : 'bold',
        'size'   : 16}

    mpl.rc('font', **font)
    '''
    
    # tournamentSize = sys.argv[2]
    # childrenPerParent = sys.argv[3]
    
    grid_numChildren = np.zeros((5,5))
    grid_tournamentSize = np.zeros((5,5))
    grid_g9_mean = np.zeros((5,5))
    grid_meandiv = np.zeros((5,5))
    
    for ncInd in range(0,5):
        for tsInd in range(0,5):
            
            numChildren = numChildrenList[ncInd]
            tournamentSize = tournamentSizeList[tsInd]
            
            runsetName = 'chNum_' + str(numChildren) + '_ts_' + str(tournamentSize) + '_chPar_' + str(childrenPerParent)
            currentRunset = pd.read_csv('./' + runsetName + '/runset_' + runsetName + '.csv',delimiter=';')
            currentExampleRun = pd.read_csv('./' + runsetName + '/run_1_' + runsetName + '.csv',delimiter=';')
            genList = currentExampleRun.Generation
            maxgen = np.max(genList)
            
            grid_numChildren[ncInd][tsInd] = numChildren
            grid_tournamentSize[ncInd][tsInd] = tournamentSize
            
            divArray = np.array(currentRunset.slopeLogDiv.astype(float))
            
            g9array = np.array(currentRunset.gen_nine.astype(float))
            print('g9array:')
            print(g9array)
            # g9array_nan[g9array_nan < 0] = np.nan
            
            grid_meandiv[ncInd][tsInd] = np.mean(divArray)/maxgen            
            # grid_g9_raw[ncInd][tsInd] = g9array
            
            
    
    print(grid_numChildren)
    print(grid_tournamentSize)
    fig = plt.figure()
    ax = plt.axes(projection='3d')
    ax.plot_surface(grid_numChildren,grid_tournamentSize,grid_meandiv, rstride=1, cstride=1,cmap='summer', edgecolor='none',shade=True)
    
    # if (childrenPerParent==1):
    #     ax.set_title('one child per parent');
    # else:
    #     ax.set_title('two children per parent');
    
    ax.set_xlabel('number of children')
    ax.set_ylabel('tournament size')
    # ax.set_zlabel('mean slope of log of diversity')
    
    plt.show()
    
            
            
if __name__ == "__main__":
	main()
