import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Evaluate {
    private ArrayList<Metrics> metrics;
    public Evaluate (ArrayList<Metrics> metrics){
        this.metrics = metrics;
    }

    public void run(File recommend,File partition,File score){
        FileWriter writeScore = new FileWriter(score);
        for(int i = 0;i < this.metrics.size(); i ++){
            this.metrics[i].run(recommend,partition); // Escrever o retorno da função eval no arquivo
        }
    }
}
