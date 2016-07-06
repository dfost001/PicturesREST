package test_model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestName {
	
	String first = "";
	String last = "";
	String address = "";
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	

}
