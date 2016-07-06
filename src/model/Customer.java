package model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the customer database table.
 * 
 */
@Entity
@Table(name="customer")
@NamedQuery(name="Customer.findAll", query="SELECT c FROM Customer c")
@XmlRootElement(name="customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(length=25)
	@Size(max=25)
	private String firstName;

	@Column(length=25)
	@Size(max=25)
	private String lastName;

	//bi-directional many-to-one association to PicSample
	
	@XmlTransient
	@OneToMany(cascade=CascadeType.ALL, mappedBy="customer", fetch=FetchType.EAGER)
	private List<PicSample> picSamples;

	public Customer() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
   
	
	public List<PicSample> getPicSamples() {
    	if(this.picSamples == null)
    		picSamples = new ArrayList<PicSample>();
		return this.picSamples;
	}

	public void setPicSamples(List<PicSample> picSamples) {
		this.picSamples = picSamples;
	}

	public PicSample addPicSample(PicSample picSample) {
		getPicSamples().add(picSample);
		picSample.setCustomer(this);

		return picSample;
	}

	public PicSample removePicSample(PicSample picSample) {
		getPicSamples().remove(picSample);
		picSample.setCustomer(null);

		return picSample;
	}

}