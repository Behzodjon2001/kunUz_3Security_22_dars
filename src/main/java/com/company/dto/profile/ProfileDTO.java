package com.company.dto.profile;

import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;
    @NotNull(message = "Name qani ?")
    private String name;
    @NotNull(message = "Surname qani ?")
    private String surname;
    @NotNull(message = "Email qani ?")
    private String email;
    @NotNull(message = "Password qani ?")
    private String password;
    private String attachId;
    private LocalDateTime createdDate;
    private Boolean visible;
    private ProfileStatus status;
    private ProfileRole role;

    private String jwt;
}