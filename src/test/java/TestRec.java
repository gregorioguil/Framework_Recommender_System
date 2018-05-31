import recommend.Recommend;

import java.io.File;

public class TestRec implements Recommend {

    public TestRec(){

    }

    @Override
    public void run(int numberPartitions) {
        System.out.println("Sistema de recomendaçao");
    }

    @Override
    public void init(int numberOfRecommend, File partition, String path) {

    }


    public void getNews(String article) {

    }
}
