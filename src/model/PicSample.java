package model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;

import java.util.Date;


/**
 * The persistent class for the pic_sample database table.
 * 
 */
@Entity
@Table(name="pic_sample")
@NamedQuery(name="PicSample.findAll", query="SELECT p FROM PicSample p")
@XmlRootElement(name="picSample")
@XmlAccessorType(XmlAccessType.FIELD)
public class PicSample implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@XmlElement(required=false)
	private int photoId;

	@Column(length=100)
	@Size(min=0, max=100)
	private String comment;

	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	@XmlSchemaType(name="dateTime", type=java.util.Date.class)
	@XmlElement(required=false, nillable=true)
	private Date dtUpdated;

	@XmlElement(nillable=true)
	@XmlSchemaType(name="base64Binary")
	@Lob
	private byte[] photo;

	@Column(length=40)
	@Size(min=0, max=40)
	private String picName;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="CustomerId")
	@XmlTransient
	private Customer customer;
	
	
	@Column(length=40)
	private String originalPicName;

	public PicSample() {
	}

	public int getPhotoId() {
		return this.photoId;
	}

	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDtUpdated() {
		return this.dtUpdated;
	}

	public void setDtUpdated(Date dtUpdated) {
		this.dtUpdated = dtUpdated;
	}

	public byte[] getPhoto() {
		return this.photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getPicName() {
		return this.picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}
   
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getOriginalPicName() {
		return originalPicName;
	}

	public void setOriginalPicName(String originalPicName) {
		this.originalPicName = originalPicName;
	}
	
	

}