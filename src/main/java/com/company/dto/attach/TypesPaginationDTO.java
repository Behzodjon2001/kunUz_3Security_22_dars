package com.company.dto.attach;

import com.company.dto.TypesDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TypesPaginationDTO {
    private long totalAmount;
    private List<TypesDTO> list;
}
