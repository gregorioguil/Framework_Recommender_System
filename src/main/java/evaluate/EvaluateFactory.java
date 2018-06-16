package evaluate;

public abstract class EvaluateFactory {
    public abstract void createEvaluate(String path, int numberPartitions);
    public abstract void run();
}
