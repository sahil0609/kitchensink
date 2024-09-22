package com.sahil.kitchensink.validations;

import com.sahil.kitchensink.model.User;
import com.sahil.kitchensink.model.UserDTO;
import com.sahil.kitchensink.repository.UserRespository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, UserDTO> {


    private final UserRespository userRespository;

    @Override
    public boolean isValid(UserDTO userDTO, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> user =  userRespository.findById(userDTO.getEmail());
        return user.isEmpty();
    }
}
