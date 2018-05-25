package runner;

import recommend.Recommend;

import java.util.ArrayList;

public abstract class RunnerFactory {
    public abstract  void createRunner(ArrayList<Recommend> recommends, int numberOfrecommend);
    public abstract void run();
}
