import recommend.Recommend;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestRec implements Recommend {

    public TestRec(){

    }

    @Override
    public List<String> run(String numberPartitions) {
        List<String> recommends = new ArrayList<String>();
        System.out.println("Sistema de recomendaçao");
        return recommends;
    }

    @Override
    public void init(int numberOfRecommend, File partition, String path) {

    }


    public void getNews(String article) {

    }
}
