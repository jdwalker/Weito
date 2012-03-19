package featurealgorithm;

import java.util.List;

public class DocumentFeatureResult {
String uniquename;
List<String> features;


/**
 * @param uniquename
 * @param features
 */
public DocumentFeatureResult(String uniquename, List<String> features) {
	this.uniquename = uniquename;
	this.features = features;
}

public String getUniquename() {
	return uniquename;
}

public List<String> getFeatures() {
	return features;
}
public void setFeatures(List<String> features) {
	this.features = features;
}
}
