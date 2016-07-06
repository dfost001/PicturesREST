package client.model;

import model.PicSample;
import model.Customer;

import java.util.List;






import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
//import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="clientResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientResponse {
	
	@XmlElementWrapper(nillable=true, name="pictures")
	@XmlElement(nillable=true, required=false, name="picture")
	private List<PicSample> picture;
	private Customer customer;
	private Integer indexFrom;
	private Integer indexTo;
	private Integer total;
	
	public List<PicSample> getPicture() {
		return picture;
	}
	public void setPicture(List<PicSample> picture) {
		this.picture = picture;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Integer getIndexFrom() {
		return indexFrom;
	}
	public void setIndexFrom(Integer indexFrom) {
		this.indexFrom = indexFrom;
	}
	public Integer getIndexTo() {
		return indexTo;
	}
	public void setIndexTo(Integer indexTo) {
		this.indexTo = indexTo;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}

}
