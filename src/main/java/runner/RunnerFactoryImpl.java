package runner;

import recommend.Recommend;

import java.util.ArrayList;

public class RunnerFactoryImpl extends RunnerFactory {
    Runner runner = null;


    @Override
    public void createRunner(ArrayList<Recommend> recommends,int numberOfrecommend) {
        runner = new RunnerImpl(recommends,numberOfrecommend);
    }

    @Override
    public void run() {
        runner.run();
    }
}
