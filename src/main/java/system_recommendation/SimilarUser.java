package system_recommendation;

import recommend.Recommend;

import java.io.File;
import java.util.List;

public class SimilarUser implements Recommend {
    /**
     * Receive news items
     *
     * @param task it is a line of partition in the <id_user,id_session,timeStamp,id_article>.
     */
    @Override
    public List<String> run(String task) {
        return null;
    }

    /**
     * initial the historic data
     *
     * @param numberOfRecommend it  a number of partitions
     * @param partition         it a file of partition
     * @param path
     */
    @Override
    public void init(int numberOfRecommend, File partition, String path) {

    }

    @Override
    public void getNews(String article) {

    }

    @Override
    public void clean() {

    }
}
