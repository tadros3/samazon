package samazon.models;

import java.util.Collection;
import java.util.Date;

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
	@JoinColumn(name = "orderId")
	private User user;
	
	@Column(name = "timestamp")
	private Date timestamp;
	
	@Column (name = "shipping_address")
	private String shippingAddress;
	
	@Column (name = "payment_method")
	private String paymentMethod;
	
	@Column (name = "open_order")
	private String openOrder;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(joinColumns = @JoinColumn(name = "order_id"),inverseJoinColumns = @JoinColumn(name = "item_id"))
	private Collection<LineItem> lineItems;
	
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

	public Collection<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(Collection<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

	public String getOpenOrder() {
		return openOrder;
	}

	public void setOpenOrder(String openOrder) {
		this.openOrder = openOrder;
	}
}
