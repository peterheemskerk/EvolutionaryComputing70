import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.text as txt
import sys
import os

def plot_algorithm_save(data,filename,dir_path,runIndex):
    fig, ax = plt.subplots()
    x = data.Generation
    ymax = data.Max
    ymin = data.Min
    ymean = data.Mean
        
    ax.plot(x, ymax, label = "Maximum score")
    ax.plot(x, ymin, label = "Minimum score")
    ax.plot(x, ymean, label = "Average score")
    
    indList = np.where(ymax > 9.9)
    if indList[0].size == 0:
        ax.set_title('Run ' + str(runIndex) + ', 9.9 NOT REACHED')
        
    else:
        ind_g_nine = np.min(indList)
        ax.plot(ind_g_nine,9.9,'r*',label = "9.9 point")
        ax.set_title('Run ' + str(runIndex) + ', 9.9 reached at generation ' + str(ind_g_nine))
        
    
    ax.legend(fontsize = "medium")
    ax.set_xlabel("Generation")
    ax.set_ylabel("Score")
    plt.grid(b=True,which='minor',axis='both')
    plt.ylim((0,10))
        #plt.show()
    fig.savefig(dir_path + '/results/' + filename + '/run_' + runIndex + '_' + filename + '.png',bbox_inches='tight',dpi=300)
    plt.close(fig)


def main():
    filename = sys.argv[1]
    runIndex = sys.argv[2]
    dir_path = os.path.dirname(os.path.realpath(__file__))
    
    data = pd.read_csv(dir_path + '/results/' + filename + '/run_' + runIndex + '_' + filename + '.csv',delimiter=';')
    plot_algorithm_save(data,filename,dir_path,runIndex)

if __name__ == "__main__":
    main()

