package drools;


import java.util.HashMap;
import java.util.List;


public class Metrics extends HashMap<String,Double> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8380133156832162350L;
	
	static List<String> metricnames;

	
	public Metrics() {
		super();
		for(String s : metricnames) {
			this.put(s, 0d);
		}
	}
	
	public void increaseBy(String s, double v) {
		this.put(s, v + this.get(s));
	}
	
	/**
	 * @param v
	 * @return a new Metric with the result
	 */
	public Metrics multiplyBy(double v) {
		Metrics result = new Metrics();
		
		for(String s : metricnames) {
			result.put(s, this.get(s) * v); 
		}
		return result;
	}
	
	public Metrics round() {
		Metrics result = new Metrics();
		
		for(String s : metricnames) {
			result.put( s, new Double( Math.round( this.get( s ) ) ) ); 
		}
		return result;
	}
	
	/**
	 * @param m - the Metric to be added
	 * @return a new Metrics with all the values added together
	 */
	public Metrics addMetrics(Metrics m) {
		Metrics result = new Metrics();
		
		for(String s : metricnames) {
			result.put(s, this.get(s) + m.get(s)); 
		}
		return result;
	}

	/**
	 * @return the metricNames
	 */
	public static List<String> getMetricnames() {
		return metricnames;
	}

	/**
	 * @param metricNames the metricNames to set
	 */
	public static void setMetricnames(List<String> metricnames) {
		Metrics.metricnames = metricnames;
	}
	
}
