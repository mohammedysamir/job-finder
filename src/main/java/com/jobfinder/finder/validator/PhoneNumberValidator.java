package com.jobfinder.finder.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
  @Override
  public void initialize(ValidPhoneNumber constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    String countryCodeRegex = "^[+][0-9]{1,3}$";
    String phoneNumberRegex = "^[0-9]{10,15}$";
    String countryCode = s.split(" ")[0];
    String phoneNumber = s.split(" ")[1];
    if (s.isBlank()) {
      return false;
    }
    return countryCode.matches(countryCodeRegex) && phoneNumber.matches(phoneNumberRegex);
  }
}
