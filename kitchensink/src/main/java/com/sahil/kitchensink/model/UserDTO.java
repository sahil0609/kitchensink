package com.sahil.kitchensink.model;

import com.sahil.kitchensink.validations.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@UniqueEmail
public class UserDTO {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 100)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$", message = "Password must contain at least one uppercase letter, one lowercase letter and one number")
    private String password;
}
