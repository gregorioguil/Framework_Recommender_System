package system_recommendation;

import java.io.File;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import recommend.Recommend;

public class PersonCorrelation implements Recommend {

	@Override
	public List<String> run(String task) {
		// TODO Auto-generated method stub
		RealMatrix data = null;
		new PearsonsCorrelation().computeCorrelationMatrix(data);
		return null;
	}

	@Override
	public void init(int numberOfRecommend, File partition, String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getNews(String article) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		
	}

}
