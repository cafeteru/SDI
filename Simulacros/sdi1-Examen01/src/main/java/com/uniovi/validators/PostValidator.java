package com.uniovi.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.uniovi.entities.Post;
import com.uniovi.entities.User;

@Component
public class PostValidator implements Validator {

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Post post = (Post) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title",
				"Error.empty");
		if (post.getTitle().length() < 2) {
			errors.rejectValue("title", "Error.post.title.length");
		}

	}
}