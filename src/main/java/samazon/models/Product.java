package samazon.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Product{
		
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private long id;
	 
	 @Column(name = "p_name")
	 private String pName;
	 

	 @Column(name = "inStock")
	 private long inStock;
	 
	 @Column(name = "s_image")
	 private String sImage;
	 
	 @Column(name = "l_image")
	 private String lImage;
	 
	 @Column(name = "price")
	 private double price;
	 
	 @Column(name = "s_desc")
	 private String sDesc;
	 
	 @Column(name = "l_desc")
	 private String lDesc;
	 
	 @Column(name = "active")
	 private String active;
	 
	 @OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST})
	 //@OneToMany(cascade = {CascadeType.ALL},orphanRemoval=true)
	// @OneToMany(fetch = FetchType.EAGER,mappedBy="product",cascade = {CascadeType.ALL})
	 @JoinTable(joinColumns = @JoinColumn(name = "product_id"),inverseJoinColumns = @JoinColumn(name = "item_id"))
	 //@JoinColumn(name = "productid")
	 private List<LineItem> litem = new ArrayList<LineItem>();
	 
	 @OneToMany(cascade = {CascadeType.PERSIST})
	 @JoinTable(joinColumns = @JoinColumn(name = "product_id"),inverseJoinColumns = @JoinColumn(name = "review_id"))
	 private List<Review> reviews = new ArrayList<Review>();
	 
	 public Product() {
		 active = "active";
	 }
	 
	 public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPName() {
			return pName;
		}

	public void setPName(String pName) {
			this.pName = pName;
	}

	public long getInStock() {
		return inStock;
	}

	public void setInStock(long inStock) {
		this.inStock = inStock;
	}

	public String getSImage() {
		return sImage;
	}

	public void setSImage(String sImage) {
		this.sImage = sImage;
	}

	public String getLImage() {
		return lImage;
	}

	public void setLImage(String lImage) {
		this.lImage = lImage;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getSDesc() {
		return sDesc;
	}

	public void setSDesc(String sDesc) {
		this.sDesc = sDesc;
	}

	public String getLDesc() {
		return lDesc;
	}

	public void setLDesc(String lDesc) {
		this.lDesc = lDesc;
	}

	public List<LineItem> getLitem() {
		return litem;
	}

	public void setLitem(List<LineItem> litem) {
		this.litem = litem;
	}
	
	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public void addLineItem(LineItem lineitem) {
		litem.add(lineitem);
	}
	
	public void removeLineItem(LineItem lineitem) {
		litem.remove(lineitem);
	}

}
