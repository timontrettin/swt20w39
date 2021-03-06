package pharmacy.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Benutzer Klasse
 * @author Timon Trettin
 */
@Entity
public class User {

	private @Id @GeneratedValue long id;

	@OneToOne
	private UserAccount userAccount;
	
	private String picture;

	private PayDirekt payDirekt;
	private BankAccount bankAccount;
	private PaymentCard paymentCard;

	private Address address;

	private Insurance insurance;

	private Boolean ordered = false;

	private Money salary;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Vacation> vacations = new ArrayList<>();

	private Integer vacationRemaining = 30;
	
	@SuppressWarnings("unused")
	public User() {}

	public User(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public long getId() {
		return id;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void addRole(Role role) {
		userAccount.add(role);
	}

	public void removeRole(Role role) {
		userAccount.remove(role);
	}
	
	public PayDirekt getPayDirekt() {
		return payDirekt;
	}
	
	public void setPayDirekt(PayDirekt newPayDirekt) {
		payDirekt = newPayDirekt;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}
	
	public void setBankAccount(BankAccount newBankAccount) {
		bankAccount = newBankAccount;
	}
		
	public PaymentCard getPaymentCard() {
		return paymentCard;
	}
	
	public void setPaymentCard(PaymentCard newPaymentCard) {
		paymentCard = newPaymentCard;
	}

	public Address getAddress() {
		return address;
	}

	public void changeAddress(Address newAddress) {
		address = newAddress;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String newPicture) {
		picture = newPicture;
	}

	public Boolean getOrdered() {
		return ordered;
	}

	public void setOrdered(Boolean newOrdered) {
		ordered = newOrdered;
	}

	public Insurance getInsurance() {
		return insurance;
	}

	public void setInsurance(Insurance newInsurance) {
		insurance = newInsurance;
	}

	public Money getSalary() {
		return salary;
	}

	public void setSalary(Money newSalary) {
		salary = newSalary;
	}

	@Scheduled(cron = "@yearly")
	protected void setVacationRemaining(){
		
		vacationRemaining = 30;
	}

	public Integer getVacationRemaining() {
		return vacationRemaining;
	}

	public void setVacationRemaining(Integer newVacationRemaining) {
		vacationRemaining = newVacationRemaining;
	}

	public List<Vacation> getVacations() {
		return vacations;
	}

	public void addVacation(Vacation newVacation) {
		vacations.add(newVacation);
	}

	public void removeVacation(Integer vac) {
		vacations.remove(vacations.get(vac));
	}
}
