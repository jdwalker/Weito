package drools;

import org.drools.builder.ResourceType;
import org.drools.io.Resource;

public class AlgorithmContents {
	private Resource resource;
	private ResourceType resourcetype;	
	/**
	 * @param resource
	 * @param resourcetype
	 */
	public AlgorithmContents(Resource resource, ResourceType resourcetype) {
		this.resource = resource;
		this.resourcetype = resourcetype;
	}
	
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public ResourceType getResourcetype() {
		return resourcetype;
	}
	public void setResourcetype(ResourceType resourcetype) {
		this.resourcetype = resourcetype;
	}	
	

}
