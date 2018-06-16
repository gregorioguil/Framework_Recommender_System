package system_recommendation;

import java.io.*;
import java.util.*;

import org.apache.lucene.search.similarities.DFISimilarity;
import org.apache.lucene.search.similarities.DFRSimilarity;

import recommend.Recommend;

public class SystemTFIDF implements Recommend {
	private TreeMap<Integer, Set<Integer>> users;
	private String path;
	//<day,(id,timePublication)>
	private HashMap<Integer, TreeMap<Integer, Double>> article;
	private HashMap<String,Float> termFrequency;
	private HashMap<String,Float> tf;
	private HashMap<String,Float> idf;
	private int numRecommend;
	private int numberDocuments;


	public SystemTFIDF(){
		this.termFrequency = new HashMap<String,Float>();
		this.tf = new HashMap<String,Float>();
		this.idf = new HashMap<String,Float>();
		this.numberDocuments = 0;
	}
	
	

	@Override
	public List<String> run(String task) {
		// TODO Auto-generated method stub
		List<String> recommends = new ArrayList<String>();
		System.out.println("Sistema de Recomendação TF-IDF.");
		
		String[] arg = task.split(";");

		if(arg.length < 5) {
			if(this.article.containsKey(Integer.parseInt(arg[2]))){
				this.article.get(Integer.parseInt(arg[2])).put(Integer.parseInt(arg[0]),Double.parseDouble(arg[1]));
			}else{
				this.article.put(Integer.parseInt(arg[2]),new TreeMap<Integer,Double>());
				this.article.get(Integer.parseInt(arg[2])).put(Integer.parseInt(arg[0]),Double.parseDouble(arg[1]));
			}

			return recommends;
		}
		System.out.println("User:"+arg[0]+", Lendo:"+arg[3]);
		return recommends;
	}

	// Inicializa base de dados do sistema de recomendação
	@Override
	public void init(int numberOfRecommend, File partition, String path) {
		// TODO Auto-generated method stub
		int part = 0;
		this.path = path;
		this.users = new TreeMap<Integer, Set<Integer>>();

		this.article = new HashMap<Integer, TreeMap<Integer, Double>>();
		this.numRecommend = numberOfRecommend;
		try {
			//FileWriter fileWriter = new FileWriter(path+"partition"+part+"/recommend.txt");
			FileReader fileReader = new FileReader(partition);
			FileReader fileOut = null;
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			BufferedReader bufOut = null;
			String line = bufferedReader.readLine();

			int cont = 0;
			while (line != null) {
				System.out.println(cont);
				cont++;


				String[] arg = line.split(";");
				if(arg.length < 4){
					line = bufferedReader.readLine();

					continue;
				}

				Integer idUser = Integer.parseInt(arg[0]);
				Integer idArticle = Integer.parseInt(arg[3]);
				Integer day = Integer.parseInt(arg[4]);
				if (this.users.containsKey(idUser)){
					this.users.get(idUser).add(idArticle);
					//dataBase.updateUser(line);
				}else{
					this.users.put(idUser, new TreeSet<Integer>());
					this.users.get(idUser).add(idArticle);

					//dataBase.insertUser(line);
				}
				//System.out.println(aux.get(idUser));

//                if(arg.length > 3 && !articles.containsKey(Integer.parseInt(arg[3])))
				FileReader fileReaderArticle =  new FileReader(this.path+"articles/"+day+"/"+idArticle+".txt");
				BufferedReader bufferedReaderArticle = new BufferedReader(fileReaderArticle);
				String art = bufferedReaderArticle.readLine();
				bufferedReaderArticle.close();
				String[] arg2 = art.split(";");
				Double timePublication = Double.parseDouble(arg2[2]);
				if(!this.article.containsKey(day)){
					this.article.put(day, new TreeMap<Integer, Double>());
					this.article.get(day).put(idArticle, timePublication);
					this.numberDocuments ++;
				}
				line = bufferedReader.readLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getNews(String article) {
		// TODO Auto-generated method stub
		
	}

	public HashMap<String,Float> freqTerm(HashMap<String, Float> freq, String article){
		String[] args = article.split(";");
		for(int i = 20; i < args.length; i++) {
			String[] word = args[i].split(":");
			float freqT = Float.parseFloat(word[1]);
			if(this.termFrequency.containsKey(word[0])){
				float freqOld = freq.get(word[0]);
				this.termFrequency.remove(word[0]);
				this.termFrequency.put(word[0],freqOld+freqT);
			}else{
				this.termFrequency.put(word[0],freqT);
			}
		}
		return this.termFrequency;
	}

	public static void calculateTF_IDF(){

	}

	public HashMap<String,Float> calculateIDF(int numberDocuments, String article, HashMap<String, Float> freqT) {
		//HashMap<String,Float> terms = new HashMap<String,Float>();
		String[] args = article.split(";");
		for(int i = 20; i < args.length; i++) {
			String[] word = args[i].split(":");
			float freq = Float.parseFloat(word[1]);
			float freqTotal = (float) Math.log(numberDocuments/freqT.get(word[0]));
			this.idf.put(word[0],freqTotal);
		}
		return this.idf;
	}
	
	// Calculate term frequency
	public HashMap<String,Float> calculateTF(String article) {
		//HashMap<String,Float> terms = new HashMap<String,Float>();
		String[] args = article.split(";");
		float qtWords = Integer.parseInt(args[19]);
		for(int i = 20; i < args.length; i++) {
			String[] word = args[i].split(":");
			float freq = Float.parseFloat(word[1]);
			this.tf.put(word[0],(freq/qtWords));
		}
		return this.tf;
	}

}
