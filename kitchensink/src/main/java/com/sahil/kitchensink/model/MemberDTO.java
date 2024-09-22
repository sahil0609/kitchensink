package com.sahil.kitchensink.model;

import com.sahil.kitchensink.model.markers.CreateMemberCheck;
import com.sahil.kitchensink.model.markers.UpdateMemberCheck;
import com.sahil.kitchensink.model.markers.generalMemberCheck;
import com.sahil.kitchensink.validations.UniqueMemberEmail;
import com.sahil.kitchensink.validations.UpdateMemberUniqunessConstraint;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@UniqueMemberEmail(groups = CreateMemberCheck.class)
@UpdateMemberUniqunessConstraint(groups = UpdateMemberCheck.class)
public class MemberDTO {


    private String id;

    @NotNull(groups = generalMemberCheck.class, message = "name cannot be null")
    @NotBlank(groups = generalMemberCheck.class, message = "name cannot be blank")
    @Size(min = 1, max = 25, message = "name must be between 1 and 25 characters", groups = generalMemberCheck.class)
    @Pattern(regexp = "\\D*", message =  "name Must not contain numbers", groups = generalMemberCheck.class)
    private String name;

    @NotNull(groups = generalMemberCheck.class, message = "email cannot be null")
    @NotBlank(groups = generalMemberCheck.class, message = "email cannot be blank")
    @Email(groups = generalMemberCheck.class, message = "email is invalid")
    private String email;

    @NotNull(groups = generalMemberCheck.class, message = "phone number cannot be null")
    @NotBlank(groups = generalMemberCheck.class, message = "phone number cannot be blank")
    @Size(min = 10, max = 12, message = "phone number must be between 10 and 12 digits", groups = generalMemberCheck.class)
    @Pattern(regexp = "\\d*", message = "phone number Must contain only numbers", groups = generalMemberCheck.class)
    @Digits(fraction = 0, integer = 12, message = "phone number is invalid", groups = generalMemberCheck.class)
    private String phoneNumber;
}
