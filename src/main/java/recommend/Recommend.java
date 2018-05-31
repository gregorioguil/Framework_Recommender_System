package recommend;

import java.io.File;

public interface Recommend {

    // run the system recommendation
    void run(int numberPartitions);

    // initial the historic data
    void init(int numberOfRecommend, File partition, String path);

    /**
     * Receive news items
     * @param article it is a line of features in the article.
     */
    void getNews(String article);

}
