package block;

import java.io.IOException;

public abstract class DataSplit {
    public abstract void run(Long unitTime,Long initTime);
    public abstract void insertData(String path);
    public abstract int getNumberPartitions();
}
