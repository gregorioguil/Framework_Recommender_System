package main.java.runner;


import test.java.TestRec;

import java.io.File;

public class Main {

    public static void main(String [] args){
        Runner runner = new Runner(null,4);
        File data = new File("/home/gregorio/Documentos/Guilherme IC/cabeçalho");
        File logs = new File("/home/gregorio/Dropbox/Ufop/Iniciação Cientifica 2/DadosUnificados/dados.csv");
        runner.definedBase(3600,data,logs);
        runner.run();
        TestRec t = new TestRec();
        t.run();
    }
}

