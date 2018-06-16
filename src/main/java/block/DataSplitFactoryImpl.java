package block;

public class DataSplitFactoryImpl extends DataSplitFactory{
    DataSplit dataSplit = null;
    @Override
    public void createDataSplit(String logs, String data) {
        dataSplit = new DataSplitImpl(logs,data);
        dataSplit.insertData(data);
    }

    @Override
    public void run(Long unitTime, Long initTime) {
        dataSplit.run(unitTime,initTime);
    }

    @Override
    public int getNumberPartitions() {
        return dataSplit.getNumberPartitions();
    }
}
