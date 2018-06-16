package evaluate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EvaluateImpl extends Evaluate {
    private ArrayList<Metrics> metrics;
    private File score;
    private File partition;
    private File recommend;
    private String pathDataBase;
    private int numberPartitions;
    public EvaluateImpl(String path, int numberPartitions){
        this.metrics = new ArrayList<Metrics>();
        this.pathDataBase = path;
        this.numberPartitions = numberPartitions;
    }




    @Override
    public void run() {
        try {
            FileWriter writeScore = new FileWriter(score);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0;i < this.metrics.size(); i ++) {
            this.metrics.get(i).run(recommend, partition);
            // Escrever o retorno da função eval no arquivo
        }
    }

    @Override
    public void insertMetrics(Metrics metrics) {

    }

}