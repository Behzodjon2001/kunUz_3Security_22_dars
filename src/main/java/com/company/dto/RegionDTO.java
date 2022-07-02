package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionDTO {
    private Integer id;
    private LocalDateTime createdDate;
    @NotNull(message = "Key qani ?")
    private String key;
    @NotNull(message = "Name uz qani")
    private String nameUz;
    @NotNull(message = "Name ru qani")
    private String nameRu;
    @NotNull(message = "Name en qani")
    private String nameEn;
    private String name;
}
