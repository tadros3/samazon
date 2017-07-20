package samazon.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product{
		
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private long id;
	 
	 @Column(name = "p_name")
	 private String p_name;
	 

	@Column(name = "inStock")
	 private long inStock;
	 
	 @Column(name = "s_image")
	 private String s_image;
	 
	 @Column(name = "l_image")
	 private String l_image;
	 
	 @Column(name = "price")
	 private double price;
	 
	 @Column(name = "s_desc")
	 private String s_desc;
	 
	 @Column(name = "l_desc")
	 private String l_desc;
	 
	 public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getP_name() {
			return p_name;
		}

	public void setP_name(String p_name) {
			this.p_name = p_name;
	}

	public long getInStock() {
		return inStock;
	}

	public void setInStock(long inStock) {
		this.inStock = inStock;
	}

	public String getS_image() {
		return s_image;
	}

	public void setS_image(String s_image) {
		this.s_image = s_image;
	}

	public String getL_image() {
		return l_image;
	}

	public void setL_image(String l_image) {
		this.l_image = l_image;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getS_desc() {
		return s_desc;
	}

	public void setS_desc(String s_desc) {
		this.s_desc = s_desc;
	}

	public String getL_desc() {
		return l_desc;
	}

	public void setL_desc(String l_desc) {
		this.l_desc = l_desc;
	}

	
}
