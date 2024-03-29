package com.cg.oms.main;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cg.oms.beans.Admin;
import com.cg.oms.beans.AdminMessage;
import com.cg.oms.beans.Card;
import com.cg.oms.beans.Cart;
import com.cg.oms.beans.Customer;
import com.cg.oms.beans.CustomerAddress;
import com.cg.oms.beans.CustomerMessage;
import com.cg.oms.beans.Order;
import com.cg.oms.beans.Product;
import com.cg.oms.services.OnlineMedicalStoreServices;
import com.cg.oms.services.OnlineMedicalStoreServicesImpl;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OMSApp {

	// db name : onlinemedicalstore_db
	// change hbm2ddl.auto value in persistence.xml file only
	static Integer loginCustomerId = 0;
	// @Autowired
	OnlineMedicalStoreServices service = new OnlineMedicalStoreServicesImpl();
	AnnotationConfigApplicationContext userctx = new AnnotationConfigApplicationContext(Customer.class);

	// Customer API
	// ************************************************************************
	@RequestMapping(value = "/customer/registerCustomer", method = RequestMethod.POST)
	public Boolean registerCustomer(@RequestBody Customer customer) {
		System.out.println(customer);
		if (service.registerCustomer(customer)) {
			return true;
		} else {
			return false;
		}
	}

	// cancel order
	@RequestMapping(value = "/order/cancelOrder/{orderId}/{customerId}", method = RequestMethod.DELETE)
	public Boolean cancelOrder(@PathVariable("orderId") int orderId, @PathVariable("customerId") int customerId) {
		return service.cancelOrder(orderId, customerId);
	}

	// display order
	@RequestMapping(value = "/order/displayOrder/{customerId}", method = RequestMethod.DELETE)
	public List<Order> displayOrder(@PathVariable("customerId") int customerId) {
		return service.displayOrder(customerId);
	}

	@RequestMapping(value = "/cart/displayCart/{customerId}", method = RequestMethod.GET)
	public Cart displayCart(@PathVariable("customerId") int customerId) {
		return service.displayCart(customerId);
	}

	@RequestMapping(value = "/customer/placeOrder", method = RequestMethod.POST)
	public Order placeOrder(@RequestBody Cart cart) {
		Order order = service.placeOrder(cart);
		return order;
	}

	@RequestMapping(value = "/customer/loginCustomer", method = RequestMethod.POST)
	public Customer loginCustomer(@RequestBody Customer customer) {
		Customer loginCustomer = service.loginCustomer(customer.getCustomerId(), customer.getPassword());
		System.out.println(customer);
		if (loginCustomer != null) {
			return loginCustomer;
		} else {
			return null;
		}
	}

	// delete customer
	@RequestMapping(value = "/customer/deleteCustomer/{customerId}", method = RequestMethod.DELETE)
	public Boolean deleteCustomer(@PathVariable("customerId") int customerId) {
		return service.deleteProduct(customerId);

	}

	@RequestMapping(value = "/customer/updateCustomer", method = RequestMethod.PUT)
	public Boolean updateCustomer(@RequestBody Customer customer) {
		customer.setCustomerId(customer.getCustomerId());
		if (service.updateCustomer(customer)) {
			return true;
		} else {
			return false;
		}

	}

	@RequestMapping(value = "/customer/updatePassword", method = RequestMethod.PUT)

	public Boolean updatePassword(@RequestBody Customer customer) {

		if (service.updatePassword(customer.getCustomerId(), customer.getPassword(), customer.getNewPassword())) {
			return true;
		} else {
			return false;
		}

	}

	@RequestMapping(value = "/customer/addAddress", method = RequestMethod.POST)
	public Boolean addAddress(@RequestBody CustomerAddress customerAddress) {
		customerAddress.setCustomerId(loginCustomerId);

		if (service.addAddress(customerAddress)) {
			return true;
		} else {
			return false;
		}
	}

	// update address of Customer
	@RequestMapping(value = "/customer/updateAddress", method = RequestMethod.PUT)

	public Boolean updateAddress(@RequestBody CustomerAddress customerAddress) {

		customerAddress.setCustomerId(loginCustomerId);
		System.out.println(customerAddress);
		if (service.updateAddress(customerAddress)) {
			return true;
		} else {
			return false;
		}
	}

	// Add to cart
	@RequestMapping(value = "/customer/addToCart/{productId}/{customerId}", method = RequestMethod.GET)
	public Cart addToCart(@PathVariable("productId") int productId, @PathVariable("customerId") int customerId) {
		Cart cart = service.addToCart(productId, customerId);
		return cart;
	}

	// update cart
	@RequestMapping(value = "/customer/updateCart", method = RequestMethod.GET)
	public Cart updateCart(@RequestBody Cart cart) {
		Cart tempcart = service.updateCart(cart);
		return tempcart;
	}

	// Remove from cart
	@RequestMapping(value = "/customer/removeFromCart/{productId}/{customerId}", method = RequestMethod.GET)
	public Cart removeFromCart(@PathVariable("productId") int productId, @PathVariable("customerId") int customerId) {
		Cart cart = service.removeFromCart(productId, customerId);
		return cart;
	}

	// search product by search_Keyword
	@RequestMapping(value = "/admin/searchproductbykeyword", method = RequestMethod.POST)
	public List<Product> searchProduct(@RequestBody Product product) {

		return service.searchProduct(product.getSearchKeyword());
	}

	// validate Card
	@RequestMapping(value = "/card/validate", method = RequestMethod.POST)
	public Boolean validateCard(@RequestBody Card card) {

		return service.validateCard(card);
	}

	// send msg to admin
	@RequestMapping(value = "/customer/sendMessage", method = RequestMethod.POST)
	public Boolean sendMessageToAdmin(@RequestBody CustomerMessage message) {
		message.setMessageDate(new java.util.Date());
		if (service.sendMessageToAdmin(message)) {
			return true;
		} else {
			return false;
		}
	}
	// get msg from Admin

	@RequestMapping(value = "/customer/getMessage", method = RequestMethod.POST)
	public List<AdminMessage> getMessage(@RequestBody CustomerMessage message) {
		return service.getAdminMSG(message.getCustomerId());
	}

	// Admin API
	// ****************************************************************************************************************************
	// get all customer List
	@RequestMapping(value = "/admin/getAllCustomers", method = RequestMethod.GET)
	public List<Customer> getCustomers() {
		return service.getAllCustomer();
	}

	// admin login
	@RequestMapping(value = "/admin/adminLogin", method = RequestMethod.POST)
	public Admin adminLogin(@RequestBody Admin admin) {

		admin = service.adminLogin(admin.getAdminId(), admin.getPassword());
		System.out.println(admin);
		if (admin != null) {

			return admin;

		}
		return null;
	}

	// search customer by id
	@RequestMapping(value = "/customer/searchCustomer", method = RequestMethod.GET)
	public Customer searchCustomer(@RequestBody Customer customer) {
		Customer searchCustomer = service.searchCustomer(customer.getCustomerId());
		if (searchCustomer != null) {
			return searchCustomer;
		} else {

		}
		return null;
	}

	// delete customer by Admin
	@RequestMapping(value = "/admin/deleteCustomer/{customerId}", method = RequestMethod.DELETE)
	public Boolean deleteCustomerAdmin(@PathVariable("customerId") int customerId) {
		return service.deleteProduct(customerId);

	}

	// Create Product
	@RequestMapping(value = "/admin/addproduct", method = RequestMethod.POST)
	public Product createProduct(@RequestBody Product inputproduct) {
		Product product = service.createProduct(inputproduct);
		return product;
	}

	// Search Product
	@RequestMapping(value = "/admin/searchProductById", method = RequestMethod.POST)
	public Product searchProductById(@RequestBody Product inputProduct) {
		Product product = service.searchProduct(inputProduct.getProductId());
		return product;

	}

	// delete product
	@RequestMapping(value = "/admin/deleteProduct/{productId}", method = RequestMethod.DELETE)
	public Boolean deleteProduct(@PathVariable("productId") int productId) {
		return service.deleteProduct(productId);

	}

	// getAllProduct
	@RequestMapping(value = "/admin/getAllProduct", method = RequestMethod.GET)
	public List<Product> getAllProduct() {
		List<Product> productList = new ArrayList<Product>();
		productList = service.getAllProduct();
		return productList;
	}

	// update product
	@RequestMapping(value = "/admin/updateProduct", method = RequestMethod.PUT)
	public Product updateProduct(@RequestBody Product inputproduct) {
		Product product = service.updateProduct(inputproduct);
		return product;
	}

	// get Customer message
	@RequestMapping(value = "/admin/getCustomerMSG", method = RequestMethod.GET)
	public List<CustomerMessage> getCustomerMSG() {
		return service.getCustomerMSG();

	}

	// send msg to user
	@RequestMapping(value = "/admin/sendMessage", method = RequestMethod.POST)
	public Boolean sendMessageToCustomer(@RequestBody AdminMessage message) {
		message.setMessageDate(new java.util.Date());
		if (service.sendMessageToCustomer(message)) {
			return true;
		} else {
			return false;
		}
	}

}
