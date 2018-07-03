package evaluate;

import metrics.Metrics;

import java.util.ArrayList;

public abstract class EvaluateFactory {
    public abstract void createEvaluate(ArrayList<Metrics> metrics, String path,String pathLog, int numberSystens, int numberPartitions);
    public abstract void run();
}
