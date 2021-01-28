package pharmacy.order;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;

import pharmacy.user.UserManagement;

public class InsuranceValidator implements ConstraintValidator<ValidInsurance, String> {
	@Autowired
	private OrderController controller;
	@Autowired
	private UserManagement management;
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(this.controller.haspresonly(this.controller.getCart())) {
			if (this.management.currentUser().get().getInsurance().getCompany().isEmpty()) {
				return false;
			}
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate("Versicherung fehlt").addConstraintViolation();
		return true;
	}
}