package recommend;

import java.io.File;
import java.util.List;

public interface Recommend {


    /**
     * Receive news items
     * @param task it is a line of partition in the <id_user,id_session,timeStamp,id_article>.
     */
    List<String> run(String task);


    /**
     * initial the historic data
     * @param numberOfRecommend it  a number of partitions
     * @param partition it a file of partition
     */
    void init(int numberOfRecommend, File partition, String path);


    void getNews(String article);
    
    void clean();

}
