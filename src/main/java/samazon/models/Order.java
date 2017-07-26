package samazon.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table (name = "orders", schema = "Samazon")
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	
	@Column(name = "timestamp")
	private Date timestamp;
	
	@Column (name = "shipping_address")
	private String shippingAddress;
	
	@Column (name = "payment_method")
	private String paymentMethod;
	
	@Column (name = "open_order")
	private String openOrder;
	
	@OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST})
	//@OneToMany(cascade = {CascadeType.ALL},orphanRemoval=true)
	//@OneToMany(fetch = FetchType.EAGER,mappedBy="order",cascade = {CascadeType.ALL})
	@JoinTable(joinColumns = @JoinColumn(name = "order_id"),inverseJoinColumns = @JoinColumn(name = "item_id"))
	//@JoinColumn(name = "orderId")
	private List<LineItem> lineItems = new ArrayList<LineItem>();
	
	public Order() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

	public String getOpenOrder() {
		return openOrder;
	}

	public void setOpenOrder(String openOrder) {
		this.openOrder = openOrder;
	}
	
	public void addLineItem(LineItem litem)
	{
		lineItems.add(litem);
	}
	
	public void removeLineItem(LineItem litem) {
		lineItems.remove(litem);
	}
}
