package block;

import java.io.IOException;

public abstract class DataSplit {
    public abstract void run(Double unitTime,Double initTime);
    public abstract void insertData(String path);
}
