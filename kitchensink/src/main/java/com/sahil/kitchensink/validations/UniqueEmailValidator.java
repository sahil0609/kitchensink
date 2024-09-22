package com.sahil.kitchensink.validations;

import com.sahil.kitchensink.model.User;
import com.sahil.kitchensink.model.UserDTO;
import com.sahil.kitchensink.repository.UserRespository;
import com.sahil.kitchensink.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, UserDTO> {

    @Autowired
    private UserRespository userRespository;

    @Override
    public boolean isValid(UserDTO userDTO, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> user =  userRespository.findById(userDTO.getEmail());
        return user.isEmpty();
    }
}
