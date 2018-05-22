package system_recommendation;

import recommend.Recommend;



public class SystemRandom implements Recommend {


    public void run() {

        System.out.println("Sistema de Recomendação Randômico.");
    }

    public void init(int numRecommend) {

    }

    /**
     * receive news items
     *
     * @param article it is a line of features in the article.
     */
    @Override
    public void getNews(String article) {

    }


}
