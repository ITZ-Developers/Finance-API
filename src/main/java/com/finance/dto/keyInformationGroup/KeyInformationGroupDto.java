package com.finance.dto.keyInformationGroup;

import com.finance.dto.keyInformation.KeyInformationDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class KeyInformationGroupDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "keyInformations")
    private List<KeyInformationDto> keyInformations;
}
