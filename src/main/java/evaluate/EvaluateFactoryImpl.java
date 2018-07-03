package evaluate;

import metrics.Metrics;

import java.util.ArrayList;

public class EvaluateFactoryImpl extends EvaluateFactory{
    Evaluate evaluate = null;
    @Override
    public void createEvaluate(ArrayList<Metrics> metrics, String path,String pathLog, int numberSystens, int numberPartitions) {
        evaluate = new EvaluateImpl(metrics,path,pathLog,numberSystens,numberPartitions);
    }


    @Override
    public void run() {
        evaluate.run();
    }
}
