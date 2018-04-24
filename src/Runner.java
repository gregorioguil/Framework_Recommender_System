import java.io.File;
import java.util.ArrayList;

public class Runner {

    private ArrayList<Recommend> recommends;
    private int numRecomendacao;
    private ArrayList<File> partition;
    public Runner(ArrayList<Recommend> recommends,int numRecomendacao,ArrayList<File> partition){
        this.recommends = recommends;
        this.numRecomendacao = numRecomendacao;
        this.partition = partition;
    }
    public int run(){

        return 0;
    }
}
