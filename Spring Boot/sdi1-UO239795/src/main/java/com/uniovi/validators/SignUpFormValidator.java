package com.uniovi.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.uniovi.entities.User;
import com.uniovi.services.UsersService;

@Component
public class SignUpFormValidator implements Validator {
	@Autowired
	private UsersService usersService;

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email",
				"Error.empty");
		if (usersService.getUserByEmail(user.getEmail()) != null) {
			errors.rejectValue("email", "Error.signup.email.duplicate");
		}
		if (user.getName().length() < 2) {
			errors.rejectValue("name", "Error.signup.name.length");
		}
		if (user.getSurName().length() < 2) {
			errors.rejectValue("surName", "Error.signup.lastName.length");
		}
		if (user.getPassword().length() < 5) {
			errors.rejectValue("password", "Error.signup.password.length");
		}
		if (user.getPasswordConfirm().length() < 5) {
			errors.rejectValue("passwordConfirm",
					"Error.signup.password.length");
		}
		if (!user.getPasswordConfirm().equals(user.getPassword())) {
			errors.rejectValue("passwordConfirm",
					"Error.signup.passwordConfirm.coincidence");
		}
	}
}