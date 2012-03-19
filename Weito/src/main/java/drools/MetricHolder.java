package drools;

public class MetricHolder implements Comparable<MetricHolder> {
	double metric;

	/**
	 * @param metric
	 */
	public MetricHolder(double metric) {
		this.metric = metric;
	}

	@Override
	public int compareTo(MetricHolder o) {
		if( this.getMetric() > o.getMetric() ) return 1;
		if( this.getMetric() < o.getMetric() ) return -1;
		return 0;
	}

	public double getMetric() {
		return metric;
	}

	public void setMetric(double metric) {
		this.metric = metric;
	}

}
