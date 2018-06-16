package evaluate;

public class EvaluateFactoryImpl extends EvaluateFactory{
    Evaluate evaluate = null;
    @Override
    public void createEvaluate(String path, int numberPartitions) {
        evaluate = new EvaluateImpl(path,numberPartitions);
    }

    @Override
    public void run() {

    }
}
