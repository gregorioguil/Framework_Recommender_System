package framework.evaluate;

import java.io.File;

public interface Metrics {
    //public void run(File framework.block.recommend,File partition,File score);

    void run(File recommend, File partition);
}
