# Framework Recommender System

Framework para análise e avaliação de sistemas de recomendação 
de jornais online. Este trabalho tem como objetivo a criação de um framework para teste e avaliação de sistemas de recomendação de jornal online. A ferramenta recebe os dados e trata para entrada nos algoritmos de recomendação, estes algoritmos
retornam uma lista de recomendação a qual é avalida pelas 
métricas que o proprio usuário pode implementar. As informações 
contidas abaixo podem ser vistas mais afundo no arquivo `Monografia.pdf`,
que se encontra neste repositório.


A criação deste framework focou em deixar o mesmo o mais fácil 
possível de ser usado por outros pesquisadores que desejam 
testar e avaliar sistemas de recomendação para jornais. Com 
isso segue uma exemplificação de caso de uso, mostrando como 
ficou relativamente fácil o uso dessa ferramenta. 
Seque uma exemplificação do funcionamento do framework.

```java
class Main {
    public static void main(String[] args) {
        // Iniciando o framework e rodando o modulo de dataSplit
        Framework.insertData( arqOfLlogs, arqOfData);
        Framework.runDataSplit(unitTime, iniTime);
        // Adicionando os sistemas de recomendacao criados pelos usuario do framework, e rodando-os
        Framework.insertRecSys( aRecSysMakeByUser() );
        Framework.insertRecSys( otherRecSysMakeByUser() );
        ...
        Framework.runRunner(numberRec);
        // Adicionando as metricas implementadas pelos usuario do framework, e rodando-os
        Framework.insertMetrics( aMetricMakeByUser() );
        Framework.insertMetrics( anotherMetricMakeByUser() );
        ...
        Framework.runEvaluator();
    }
}
```

O algoritmo acima é um exemplo de implementação da classe 
`Main` que o usuário do framework deve implementar. É mostrado 
a inicialização dos dados, a inserção de sistemas de recomendação 
e métricas.

### Módulo  Tratamento dos Dados (DataSplit)

Inicialmente a base é composta por 2 arquivos no formato csv. 
Um contendo informações sobre os  artigos e outro com 
informações sobre os usuários, tais como o tempo de acesso e 
artigos lidos. A estrutura desses arquivos é descrita na seção 
refetente aos dados na monografia. O módulo tratamento dos dados 
é responsável por criar as partições e diretórios onde os dados 
irão ser manipulados. Vale ressaltar que os dados já devem estar 
limpos e normalizados de acordo com as exigências deste para 
funcionamento desta ferramenta. 
     
A primeira coisa que é realizada é a leitura desse dois 
arquivos, isso é feito através do método `Framework.insertData(logs,data)`, onde é 
passado o arquivo de `logs`, que é referente à iteração dos 
usuários do jornal e `data` que é informações relevantes dos 
artigos. Esses métodos são responsáveis pela indexação da base 
de dados e podem serem omitidos se uma vez executados.

```java
class Main {
    public static void main(String[] args) {
        long iniTime = 0, unitTime = 604800000; // tempo em milissegundos
        Framework.insertData("logs.csv","data.csv"); // insere dados
        Framework.runDataSplit(unitTime,iniTime);
    }
}
```

### Módulo de Recomendação (Runner)

O módulo  `Runner` é responsável por executar os algoritmos de 
recomendação. Este é também um objeto interno, que é construído 
usando dos padrões de projeto `Factory` e `Singleton`. Ele deve 
ser executado posterior ao módulo `DataSplit`, garantindo a 
existência do conjunto de dados e partições para iniciar a 
simulação. Para inserir um algoritmo de recomendação é usado o 
método `Framework.insertRecSys(Recommed Obj)`. Como argumento 
esse método recebe um objeto que implementa uma interface 
`Recommend` do framework.

```java
public interface Recommend {
    /**
     * Receive news items
     * @param task it is a line of partition in the <id_user,id_session,timeStamp,id_article>.
     */
    List<String> run(String task);

    /**
     * initial the historic data
     * @param numberOfRecommend it  a number of partitions
     * @param partition it a file of partition
     */
    void init(int numberOfRecommend, File partition, String path);

    /**
     * clean everything
     */
    void clean();

}
```


### Módulo de Avaliação (Evaluate)

É o módulo dedicado às métricas de avaliação. Onde, o módulo de 
recomendação faz uma predição usando algum algoritmo de 
recomendação e essa predição é avaliada de acordo com as 
métricas.

Para a execução desse módulo é necessário a implementação da 
classe `Metrics`.