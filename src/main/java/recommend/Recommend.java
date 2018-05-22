package recommend;

public interface Recommend {

    // run the system recommendation
    void run();

    // initial the historic data
    void init(int numberOfRecommend);

    /**
     * Receive news items
     * @param article it is a line of features in the article.
     */
    void getNews(String article);

}
