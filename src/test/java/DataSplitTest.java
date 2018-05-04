package test.java;



import main.java.block.DataSplit;
import org.junit.jupiter.api.Test;

import java.io.File;

public class DataSplitTest {

    @Test
    public void read() {
        String path = "/home/gregorio/Documentos/Guilherme IC/sessoesJOAcomRecOrdenado.csv";
        File dados = new File(path);
        DataSplit b = new DataSplit(2, dados);
        b.run();
    }
}