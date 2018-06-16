package block;

public abstract class DataSplitFactory {
    public abstract void createDataSplit(String logs, String data);

    public abstract void run(Long unitTime, Long initTime);

    public abstract int getNumberPartitions();
}
