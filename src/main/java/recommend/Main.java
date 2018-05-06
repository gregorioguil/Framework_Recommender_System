package main.java.recommend;

import main.java.runner.Runner;
import test.java.TestRec;

import java.io.File;

public class Main {

    public static void main(String [] args){
        TestRec t = new TestRec();
        Runner r = new Runner(null,4);
        File data = new File("/home/gregorio/Documentos/Guilherme IC/cabeçalho");
        File logs = new File("/home/gregorio/Dropbox/Ufop/Iniciação Cientifica 2/DadosUnificados/dados.csv");
        r.definedBase(3600,data,logs);
        t.run();
    }
}
