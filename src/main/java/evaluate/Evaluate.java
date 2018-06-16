package evaluate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Evaluate {
    private ArrayList<Metrics> metrics;
    public Evaluate (ArrayList<Metrics> metrics){
        this.metrics = metrics;
    }

    public void run(File recommend,File partition,File score) throws IOException {
        FileWriter writeScore = new FileWriter(score);
        for(int i = 0;i < this.metrics.size(); i ++) {
            this.metrics.get(i).run(recommend, partition);
            // Escrever o retorno da função eval no arquivo
        }
    }
}