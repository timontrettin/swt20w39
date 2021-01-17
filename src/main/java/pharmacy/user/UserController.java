package pharmacy.user;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

import javax.mail.Address;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.salespointframework.useraccount.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Controller
class UserController {

	private final UserManagement userManagement;

	UserController(UserManagement userManagement) {
		this.userManagement = userManagement;
	}
/*
	@ControllerAdvice
	public class GlobalControllerAdvice {
		
		@ModelAttribute
		public void globalAttributes(Model model) {
			if(userManagement.currentUser().isPresent()) {
				model.addAttribute("picture", userManagement.currentUser().get().getPicture());
			}
		}
	} 
*/
	@GetMapping("/menu")
	String menu() {

		return "menu";
	}

	@GetMapping("/register")
	String register(Model model, UserForm userForm) {

		model.addAttribute("userForm", userForm);

		return "register";
	}

    @PostMapping("/register")
	String changeRegister(@Valid @ModelAttribute("userForm")UserForm userForm, Errors result) {

		if (result.hasErrors()) {
			return "register";
		}

		userManagement.addUser(userForm);

		return "redirect:/login";
	}

	@GetMapping("/users")
	@PreAuthorize("hasRole('BOSS')")
	String users(Model model, EmployeeForm employeeForm, InsuranceForm insuranceForm, AddressForm addressForm) {

		model.addAttribute("insuranceForm", insuranceForm);
		model.addAttribute("addressForm", addressForm);
		model.addAttribute("employeeForm", employeeForm);

		model.addAttribute("users", userManagement.findAll());
		model.addAttribute("customer", Role.of("CUSTOMER"));
		model.addAttribute("employee", Role.of("EMPLOYEE"));
		model.addAttribute("boss", Role.of("BOSS"));

		return "users";
	}
	
	@PostMapping("/user/{userId}/insurance")
	@PreAuthorize("hasRole('BOSS')")
	String changeInsurance(@PathVariable Long userId, @Valid @ModelAttribute("insuranceForm")InsuranceForm insuranceForm, Errors result) {
		
		if (result.hasErrors()) {
			return "redirect:/users";
		}
		
		userManagement.changeInsurance(userManagement.findUser(userId).get(), insuranceForm);

		return "redirect:/users";
	}
	
	@PostMapping("/user/{userId}/address")
	@PreAuthorize("hasRole('BOSS')")
	String addAddress(@PathVariable Long userId, @Valid @ModelAttribute("addressForm")AddressForm addressForm, Errors result) {
		
		if (result.hasErrors()) {
			return "redirect:/users";
		}
		
		userManagement.changeAddress(userManagement.findUser(userId).get(), addressForm);

		return "redirect:/users";
	}
	
	@GetMapping("/user/{userId}/hire")
	@PreAuthorize("hasRole('BOSS')")
	String hireEmployee(@PathVariable Long userId) {
		
		userManagement.hireEmployee(userManagement.findUser(userId).get());

		return "redirect:/users";
	}
	
	@GetMapping("/user/{userId}/dismiss")
	@PreAuthorize("hasRole('BOSS')")
	String dismissEmployee(@PathVariable Long userId) {
		
		userManagement.dismissEmployee(userManagement.findUser(userId).get());

		return "redirect:/users";
	}

	@PostMapping("/user/{userId}/salary")
	@PreAuthorize("hasRole('BOSS')")
	String changeEmployeeSalary(@PathVariable Long userId, @Valid @ModelAttribute("employeeForm")EmployeeForm employeeForm, Errors result) {

		if (result.hasErrors()) {
			return "redirect:/users";
		}
		
		userManagement.changeEmployee(userManagement.findUser(userId).get(), employeeForm);

		return "redirect:/users";
	}

	@GetMapping("/user/{userId}/remove")
	@PreAuthorize("hasRole('BOSS')")
	String removeUser(@PathVariable Long userId) {
		
		userManagement.removeUser(userManagement.findUser(userId).get());

		return "redirect:/users";
	}
	/*
	@GetMapping("/user/{userId}/vacation/{vacationId}/approve")
	@PreAuthorize("hasRole('BOSS')")
	String approveVacations(@PathVariable Long userId, @PathVariable Integer vacationId) {
		
		userManagement.approveVacation(userManagement.findUser(userId).get(), vacationId);

		return "redirect:/users";
	}
	
	@GetMapping("/user/{userId}/vacation/{vacationId}/reject")
	@PreAuthorize("hasRole('BOSS')")
	String rejectVacations(@PathVariable Long userId, @PathVariable Integer vacationId) {
		
		userManagement.removeVacation(userManagement.findUser(userId).get(), vacationId);

		return "redirect:/users";
	}

	@GetMapping("/vacation")
	@PreAuthorize("hasRole('EMPLOYEE')")
	String vacation(Model model, VacationForm vacationForm) {

		model.addAttribute("vacationForm", vacationForm);

		model.addAttribute("user", userManagement.currentUser().get());

		return "vacation";
	}
	
	@PostMapping("/vacation")
	@PreAuthorize("hasRole('EMPLOYEE')")
	String addVacation(@Valid @ModelAttribute("vacationForm")VacationForm vacationForm, Errors result) {
		
		if (result.hasErrors()) {
			return "vacation";
		}
		
		userManagement.addVacation(userManagement.currentUser().get(), vacationForm);

		return "account";
	}
*/
	@GetMapping("/account")
	@PreAuthorize("isAuthenticated()")
	String account(Model model) {

		model.addAttribute("user", userManagement.currentUser().get());
		model.addAttribute("customer", Role.of("CUSTOMER"));
		model.addAttribute("employee", Role.of("EMPLOYEE"));
		model.addAttribute("boss", Role.of("BOSS"));

		return "account";
	}

	@GetMapping("/account/settings")
	@PreAuthorize("isAuthenticated()")
	String settings(Model model, PictureForm pictureForm) {

		model.addAttribute("pictureForm", pictureForm);

		model.addAttribute("user", userManagement.currentUser().get());
		model.addAttribute("customer", Role.of("CUSTOMER"));
		model.addAttribute("employee", Role.of("EMPLOYEE"));
		model.addAttribute("boss", Role.of("BOSS"));

		return "settings";
	}
	
	@PostMapping("/account/settings/picture")
	@PreAuthorize("isAuthenticated()")
	String changePicture(Model model, @Valid @ModelAttribute("pictureForm")PictureForm pictureForm, Errors result) {
		model.addAttribute("user", userManagement.currentUser().get());
		
		if (result.hasErrors()) {
			return "settings";
		}
		
		userManagement.changePicture(userManagement.currentUser().get(), pictureForm);

		return "redirect:/account/settings";
	}

	@GetMapping("/account/settings/password")
	@PreAuthorize("isAuthenticated()")
	String password(Model model, PasswordForm passwordForm) {

		model.addAttribute("passwordForm", passwordForm);

		model.addAttribute("user", userManagement.currentUser().get());
		model.addAttribute("customer", Role.of("CUSTOMER"));
		model.addAttribute("employee", Role.of("EMPLOYEE"));
		model.addAttribute("boss", Role.of("BOSS"));

		return "password";
	}

	@GetMapping("/account/settings/insurance")
	@PreAuthorize("isAuthenticated()")
	String insurance(Model model, InsuranceForm insuranceForm) {

		model.addAttribute("insuranceForm", insuranceForm);

		model.addAttribute("user", userManagement.currentUser().get());
		model.addAttribute("customer", Role.of("CUSTOMER"));
		model.addAttribute("employee", Role.of("EMPLOYEE"));
		model.addAttribute("boss", Role.of("BOSS"));

		return "insurance";
	}

	@GetMapping("/account/settings/address")
	@PreAuthorize("isAuthenticated()")
	String account(Model model, AddressForm addressForm) {

		model.addAttribute("addressForm", addressForm);

		model.addAttribute("user", userManagement.currentUser().get());
		model.addAttribute("customer", Role.of("CUSTOMER"));
		model.addAttribute("employee", Role.of("EMPLOYEE"));
		model.addAttribute("boss", Role.of("BOSS"));

		return "address";
	}
	
	@PostMapping("/account/settings/password")
	@PreAuthorize("isAuthenticated()")
	String changeAccountPassword(Model model, @Valid @ModelAttribute("passwordForm")PasswordForm passwordForm, Errors result) {
		model.addAttribute("user", userManagement.currentUser().get());
		
		if (result.hasErrors()) {
			return "password";
		}
		
		userManagement.changePassword(passwordForm);

		return "redirect:/account/settings";
	}
	
	@PostMapping("/account/settings/insurance")
	@PreAuthorize("isAuthenticated()")
	String changeAccountInsurance(Model model, @Valid @ModelAttribute("insuranceForm")InsuranceForm insuranceForm, Errors result) {
		model.addAttribute("user", userManagement.currentUser().get());
		
		if (result.hasErrors()) {
			return "insurance";
		}
		
		userManagement.changeInsurance(userManagement.currentUser().get(), insuranceForm);

		return "redirect:/account/settings";
	}
	
	@PostMapping("/account/settings/address")
	@PreAuthorize("isAuthenticated()")
	String addAccountAddress(Model model, @Valid @ModelAttribute("addressForm")AddressForm addressForm, Errors result) {
		model.addAttribute("user", userManagement.currentUser().get());
		
		if (result.hasErrors()) {
			return "address";
		}
		
		userManagement.changeAddress(userManagement.currentUser().get(), addressForm);

		return "redirect:/account/settings";
	}

	@GetMapping("/account/settings/remove")	
	@PreAuthorize("hasRole('CUSTOMER')")
	String removeAccountUser() {
		
		userManagement.removeUser(userManagement.currentUser().get());

		return "redirect:/logout";
	}
/*
	String example(@LoggedIn Optional<UserAccount> userAccount) {
        // functional style using map and lambda expression:
        return userAccount.map(account -> {
            // things to be done if the user account is present
            return "...";
        }).orElse("redirect:/");  // if the user account is *not* present
	}
*/
}
