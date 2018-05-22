import block.DataSplit;


import java.io.File;
import java.io.IOException;

public class DataSplitTest {

    //@Test
    public void read() throws IOException {
        String path = "/home/gregorio/Documentos/Guilherme IC/sessoesJOAcomRecOrdenado.csv";
        File logs = new File(path);
        path = "/home/gregorio/Dropbox/Ufop/Iniciação Cientifica 2/DadosUnificados/dados.csv";
        File dados = new File(path);
        DataSplit b = new DataSplit(0, 2, dados, logs);
        b.run();
    }
}
