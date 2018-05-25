package block;

public abstract class DataSplitFactory {
    public abstract void createDataSplit(String logs, String data);

    public abstract void run(Double unitTime, Double initTime);
}
