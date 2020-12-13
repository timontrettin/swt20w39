package pharmacy.finances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.accountancy.ProductPaymentEntry;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.payment.Cash;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class FinanceController {
	@Autowired
	private final Accountancy acc;
	@Autowired
	private final OrderManagement<Order> orderManagement;
	@Autowired
	private final UserAccountManagement um;
	@Autowired
	private final BusinessTime time;
	
	
	FinanceController(Accountancy acc, OrderManagement<Order> orderManagement, UserAccountManagement um, BusinessTime time) {
		this.time = time;
		Assert.notNull(um, "OrderManagement must not be null!");
		Assert.notNull(orderManagement, "OrderManagement must not be null!");
		Assert.notNull(acc, "OrderManagement must not be null!");
		this.orderManagement = orderManagement;
		this.acc=acc;
		this.um=um;
		
	}
	 private List<AccountancyEntry> getEntriesOfRole(String role){
		 List<AccountancyEntry> working=this.acc.findAll().toList();
		 List<AccountancyEntry> ret =new ArrayList<AccountancyEntry>();
		 for(Object o: this.orderManagement.findBy(OrderStatus.PAID).get().toArray()) {
			 for(AccountancyEntry a: working) {
				 if(a.getDescription().contains( ((Order) o).getId().toString())) {
					 if(((Order) o).getUserAccount().hasRole(Role.of(role))) {
					 ret.add(a);
					 }
					 
				 }
			 }
		 }
		 return ret;
	 }
	 private List<AccountancyEntry> getEntriesOfRoleAndUser (String role, String user){
		 List<AccountancyEntry> working=this.acc.findAll().toList();
		 List<AccountancyEntry> ret =new ArrayList<AccountancyEntry>();
		 for(Object o: this.orderManagement.findBy(OrderStatus.PAID).get().toArray()) {
			 for(AccountancyEntry a: working) {
				 if(a.getDescription().contains( ((Order) o).getId().toString())) {
					 if(((Order) o).getUserAccount().hasRole(Role.of(role))&& ((Order) o).getUserAccount().getLastname().equals(user)) {
					 ret.add(a);
					 }
					 
				 }
			 }
		 }
		 return ret;
	 }
	 private List<AccountancyEntry> createGehalt(){
		 List<AccountancyEntry> ret =new ArrayList<AccountancyEntry>();
		 List<UserAccount> working=this.um.findAll().toList();
		 if(this.time.getTime().getDayOfMonth()>29) {
		 for(UserAccount u:working) {
			 if(u.hasRole(Role.of("EMPLOYEE"))) {
				 AccountancyEntry sal= new AccountancyEntry(u.getSalary(), "Gehalt von "+u.getLastname());
				 ret.add(sal);
				 this.acc.add(sal);
			 }
		 }}
		 return ret;
	 }
	 private List<AccountancyEntry> createKosten(String bez, double betr){
		 List<AccountancyEntry> ret =new ArrayList<AccountancyEntry>();
		 if(this.time.getTime().getDayOfMonth()>29) {
			 List<UserAccount> working=this.um.findAll().toList();
			 for(UserAccount u:working) {
				 if(u.hasRole(Role.of("BOSS"))) {
					 AccountancyEntry sal= new AccountancyEntry(Money.of(betr, "EUR"), bez);
					 ret.add(sal);
					 this.acc.add(sal);
				 }
			 }
		 }
		 return ret;
	 }

	@GetMapping("/finances")
	public String finances(Model model) {
		List<AccountancyEntry> ret=this.acc.findAll().toList();
		model.addAttribute("filterB",new FilterBase());
		model.addAttribute("tab", ret);
		return "finances";
	}
	
	
	//diese Methode wird nie Aufgerufen!!
	@PostMapping("/filtern")
	 public String filterN(@ModelAttribute FilterBase filterB, Model model) {
		 model.addAttribute("filterB", filterB);
		 Filter filter1=filterB.getFilter();
		 System.out.println(filter1.toString());
		 List<AccountancyEntry> ret=new ArrayList<AccountancyEntry>();
		Filter f=Filter.ALLE;
		
		 switch(filter1){
		 case OBEST			: ret = this.getEntriesOfRole("CUSTOMER");			break;
		 case VERK			: ret = this.getEntriesOfRole("EMPLOYEE");			break;
		 case PRAXA			: ret = this.getEntriesOfRoleAndUser("DOCTOR", "A");break;
		 case PRAXB			: ret = this.getEntriesOfRoleAndUser("DOCTOR", "B");break;
		 case PRAXC			: ret = this.getEntriesOfRoleAndUser("DOCTOR", "C");break;
		 case GEHÄLTER		: ret = this.createGehalt();						break;
		 case STROM			: ret = this.createKosten("Strom", 50);				break;
		 case MIETE			: ret = this.createKosten("Miete",500);				break;
		 case WASSER		: ret = this.createKosten("Wasser", 20);			break;
		 case HEIZ			: ret = this.createKosten("Heizkosten", 20);		break;
		 default			: ret = this.acc.findAll().toList();				break;
		 }
		 model.addAttribute("tab", ret);
		 return "finances";
	 }

	 
	
}
