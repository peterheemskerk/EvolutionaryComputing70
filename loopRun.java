

public class loopRun {

    public static final int RUNS_PER_SETTING = 100;
    public static final int[] POPULATION_SIZES = {20,50,100,200,400};

    public static void main(String[] var0) {

    ConsertTestBox testbox;

    double currentResult;

    for (int i=0;i<RUNS_PER_SETTING;i++){

        testbox = new ConsertTestBox();

        currentResult = testbox.run(var0);
        System.out.println(i + "," + currentResult);

    }

    }
}
