package block;

import java.io.*;

import java.util.*;






class DataSplitImpl extends DataSplit {
    private Long initTime;
    private Long unitTime;
    private File data;
    private File logs;
    private File articles;
    private Long currentTime;
    private String lastArticle;
    private Long secondsDay;
    static  String pathDataBase;
    private TreeMap<Long, Integer> indices;
    private int numberPartitions;
    private HashMap<Integer,Set<Integer>> sessions;

    public DataSplitImpl( String logs, String data){
        this.data = new File(data);

        this.logs = new File(logs);
        this.secondsDay = Long.valueOf(0);
        this.indices = new TreeMap<Long, Integer>();
        String[] path = data.split("/");
        this.pathDataBase = "";
        for(int i = 0; i < path.length -1; i++)
            this.pathDataBase += "/"+path[i];
        this.pathDataBase += "/DataBase/";
    }

    public int getNumberPartitions(){
        return this.numberPartitions;
    }

    public void setNumberPartitions(int numberPartitions){
        this.numberPartitions = numberPartitions;
    }

    public void run(Long unitTime, Long initTime) {
        this.unitTime = unitTime;
        this.initTime = initTime;
        this.currentTime = unitTime;
        FileReader fileReaderLogs;
        FileReader fileReaderData;
        BufferedReader bufferedReaderLogs;
        BufferedReader bufferedReaderData;


        int part = 0, part2 = 1;
        Map<String, Long> task = new HashMap<String, Long>();
        try {

            if (new File(this.pathDataBase+"templates").mkdirs())
                System.out.println("Created dir DataBase/templates/");
            if (new File(this.pathDataBase + "partition" + part).mkdirs())
                System.out.println("Created dir DataBase/partition/" + part);
            //Partition partition = new Partition(part,this.pathDataBase);
            FileWriter fileWriter = new FileWriter(new File(this.pathDataBase+"partition"+part+"/tasks.txt"),true);
            //BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            FileWriter fileWriterTemplate;
            PrintWriter printWriter1 = null;


            fileReaderLogs = new FileReader(this.logs);
            fileReaderData = new FileReader(this.data);
            bufferedReaderData = new BufferedReader(fileReaderData);
            bufferedReaderLogs = new BufferedReader(fileReaderLogs);
            while(true) {
	            String lineData;
	            int cont = 0;
	            
	            while(true) {
	            	lineData = bufferedReaderData.readLine();
	            	if(lineData == null)
	            		break;
	            	String[] arg2 = lineData.split(";");
	            	Long timePublication = Long.parseLong(arg2[2]);
	            	task.put(arg2[0]+";"+arg2[2], timePublication);
	                //System.out.println(timePublication+" "+this.getCurrentTime());
	                //System.out.println(lineData);
	                if(timePublication >= this.getCurrentTime()) {
	                	//System.in.read();
	                	break;
	                }

	            }
	            
	            String lineLogs;
	            while (true) {
	            	lineLogs = bufferedReaderLogs.readLine();
	            	if(lineLogs == null)
	            		break;
	            	String[] arg = lineLogs.split(";");
	            	Long timeStamp = Long.parseLong(arg[3]);
	                task = mountTask(arg, task);
	                if(part > 0)
                        montSession(arg,printWriter1);
	                //System.out.println(timeStamp+" "+this.getCurrentTime());
	                if(timeStamp >= this.getCurrentTime()) {
	                	//System.in.read();
	                	break;
	                }
	            }
	            
	            if (lineLogs == null && lineData != null) {
	            	task = MapSort.sortByValue(task);
	            	writePartition(task, part, printWriter);
	                break;
	            }
	            //System.out.println(cont);
	            cont++;
	
	         
	            task = MapSort.sortByValue(task);
	            //System.out.println("Ordenado!");
	            task = writePartition(task, part, printWriter);
	            //System.out.println("Escrito! "+task.size());
	            //task.clear();
	            
	            this.setCurrentTime();
	            part++;
	            setNumberPartitions(part);
	            if (new File(pathDataBase + "partition" + part).mkdirs())
	                System.out.println("Created dir DataBase/partition" + part);
	            printWriter.close();
	            fileWriter = new FileWriter(new File(this.pathDataBase+"partition"+part+"/tasks.txt"),true);
	            //bufferedWriter = new BufferedWriter(fileWriter);
	            printWriter = new PrintWriter(fileWriter);
	            if(printWriter1 != null)
	                printWriter1.close();
                fileWriterTemplate = new FileWriter(new File(this.pathDataBase + "templates/template" + part + ".txt"));
                printWriter1 = new PrintWriter(fileWriterTemplate);
	
	            //task = mountTask(arg, task);
	                //task = MapSort.sortByValue(task);
         	}
            printWriter1.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long getTimePublicationArticle(Long s) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Integer day  = this.indices.get(s);

        if(day == null) {
            return null;
        }
        
        Long timePublication = null;
        try {
            fileReader = new FileReader(this.pathDataBase+"articles/"+day+"/"+s+".txt");
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            String[] arg = line.split(";");
            timePublication = Long.parseLong(arg[2]);
            bufferedReader.close();
        } catch (IOException e) {
            return timePublication;
        }

        return timePublication;
    }

    public TreeMap<Long, Integer> getIndices() {
        return indices;
    }

    public void setIndices(TreeMap<Long, Integer> indices) {
        this.indices = indices;
    }

    private static boolean noExists(String s) {
        return false;
    }

    public void insertData(String path) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        int day = 0;
        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            if(new File(pathDataBase+"articles/"+day+"/").mkdirs())
                System.out.println("Created dir DataBase/articles/"+day+"/");
            //System.in.read();
            int cont = 0;
            while (line != null) {
                String[] arg = line.split(";");
                Long timeStamp = Long.parseLong(arg[2]);
                if (timeStamp > this.getSecondsDay()) {
                    day++;
                    if (new File(pathDataBase+"articles/" + day + "/").mkdirs())
                        System.out.println("Created dir DataBase/articles/" + day + "/");
                    this.setSecondsDay();
                }
                writeArticleFile(arg[0], line, day);
                this.indices.put(Long.parseLong(arg[0]), day);

                //System.out.println(this.getCurrentTime()+" "+Long.parseDouble(arg[2]));
                //dataBase.insertArticle(line);

                line = bufferedReader.readLine();
                cont++;
                System.out.println(cont);
                System.out.println("Mapsize " + this.indices.size());
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeArticleFile(String s, String line, int day) {
        try {
            File file = new File(this.pathDataBase+"articles/"+day+"/"+s+".txt");
            FileWriter fileWriter = new FileWriter(this.pathDataBase+"articles/"+day+"/"+s+".txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(line);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Long getSecondsDay(){
        return this.secondsDay;
    }

    public void setSecondsDay(){
        this.secondsDay += 86400000;
    }

    private Map<String, Long> mountTask(String[] arg, Map<String, Long> task) throws IOException {
        // <u1,s1,time1,a1>
    	
        String flagTaks = "";

        for(int i = 3; i < arg.length; i += 4) {
            flagTaks += ";" + arg[i];
            flagTaks += ";" + arg[i+1];
            flagTaks += ";" + this.indices.get(Long.parseLong(arg[i+1]));
            getTimePublicationArticle(Long.parseLong(arg[i+1]));
            task.put(arg[0]+";"+arg[1]+flagTaks,Long.parseLong(arg[i]));
            flagTaks = "";
        }
        return task;
    }


    public void montSession(String[] arg, PrintWriter fileWriter){

        List<Long> list = new ArrayList<Long>();

        String out = arg[1];
        for(int i = 4; i < arg.length; i += 4){
            //System.out.println(arg[0]);
            Long idAux = Long.parseLong(arg[i]);
            Long time = getTimePublicationArticle(idAux);
            //System.out.println(idAux);
            if(time == null){
                list.add((long) -1);
            }else{

                list.add(idAux);

            }

        }

        fileWriter.write(out+";"+list+"\n");
        //fileWriter.close();

    }

    public Map<String,Long> writePartition(Map<String, Long> task, int part, PrintWriter bufferedWriter) throws IOException {
        System.out.println("Escrita na partição "+part+" "+task.size());
        Set<String> set = task.keySet();
        Iterator<String> iterator = set.iterator();
        int cont = 0;
        ArrayList<String> array = new ArrayList<String>();
//        while(iterator.hasNext()){
//        	System.out.println(task.get(iterator.next()));
//        }
//        System.in.read();
        
        while(iterator.hasNext()){
            //System.out.println("write "+cont);
            cont++;
            String id = iterator.next();
            array.add(id);
            String[] key = id.split(";");
            //String[] arg = key[0].split(";");
            //System.out.println(arg[3]);

            Long timePublication;
            String line = null;
            Long idArticle;
            Long timeStamp;
            if(key.length > 3) {
            	
                idArticle = Long.parseLong(key[3]);
                timeStamp = Long.parseLong(key[2]);
                timePublication = getTimePublicationArticle(idArticle);
                if(timePublication == null) {
                    line = idArticle+";"+0+";"+false;
                }else {
                	if(timeStamp >= this.getCurrentTime()) {
//                		System.out.println("Entrou no break. "+timeStamp+" "+this.getCurrentTime());
//                		System.in.read();
                		break;
                	}
                	Integer day = this.indices.get(idArticle);
                	line = key[0]+";"+key[1]+";"+key[2]+";"+key[3]+";"+day+";"+true;
                }
            }else {
                timePublication = Long.parseLong(key[1]);
                idArticle = Long.parseLong(key[0]);
                Integer day = this.indices.get(idArticle);
//                if(day == null) {
//                	System.out.println(idArticle+";"+timePublication+";"+day+";"+true);
//                	System.in.read();
//                }
                line = idArticle+";"+timePublication+";"+day+";"+true;
            }
            bufferedWriter.println(line);
            //task.remove(id);
        }
        
        for(int i = 0; i < array.size(); i ++) {
        	//System.out.println(array.get(i));
        	task.remove(array.get(i));
        }
        //System.in.read();
        bufferedWriter.close();
        //System.out.println("Acabou escrita.");
        //bufferedWriter.close();
        return task;
    }

//    private boolean verifyExists(Integer s) {
//        String[] dado = this.indices.get(s).split(";");
//        if(dado[1].equals("true")){
//            return true;
//        }
//        this.indices.remove(s);
//        this.indices.put(s,dado[0]+";true");
//        return false;
//    }

    public void setCurrentTime(){
        this.currentTime += this.unitTime;
    }

    public void setUnitTime(Long unitTime) {
        this.unitTime = unitTime;
    }

    public void setdata(File data) {
        this.data = data;
    }

    public void setLogs(File logs) {
        this.logs = logs;
    }

    public Long getUnitTime() {

        return unitTime;
    }

    public Long getCurrentTime(){
        return currentTime;
    }

    public File getdata() {
        return data;
    }

    public File getLogs() {
        return logs;
    }

    public void setArticles(String line) {
        try {
            FileWriter fileWriter = new FileWriter(this.articles,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(line+"\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lastArticle(String id){
        this.lastArticle = id;
    }

    public String getlastArticle() {
        return this.lastArticle;
    }

}

