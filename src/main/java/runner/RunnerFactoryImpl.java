package runner;

import recommend.Recommend;

import java.util.ArrayList;

public class RunnerFactoryImpl extends RunnerFactory {
    Runner runner = null;


    @Override
    public void createRunner(ArrayList<Recommend> recommends, int numberOfrecommend, String path, int numberPartitions) {
        runner = new RunnerImpl(recommends,numberOfrecommend,path,numberPartitions);
    }

    @Override
    public void run() {
        runner.run();
    }
}
