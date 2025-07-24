package com.jobfinder.finder.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, List<String>> {
  @Override
  public void initialize(ValidPhoneNumber constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(List<String> phoneNumbers, ConstraintValidatorContext constraintValidatorContext) {
    String countryCodeRegex = "^[+][0-9]{1,3}$";
    String phoneNumberRegex = "^[0-9]{10,15}$";

    if (phoneNumbers.isEmpty()) {
      return false;
    }
    for (String p : phoneNumbers) {
      if (p.isBlank()) {
        log.error("Phone number is blank");
        return false;
      }
      String countryCode = p.split(" ")[0];
      String phoneNumber = p.split(" ")[1];
      boolean isValid = countryCode.matches(countryCodeRegex) && phoneNumber.matches(phoneNumberRegex);
      if (!isValid) {
        log.error("Invalid phone number format: {}", p);
        return false;
      }
    }
    return true;
  }
}
