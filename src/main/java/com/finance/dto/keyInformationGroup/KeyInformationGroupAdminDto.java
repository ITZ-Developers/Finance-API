package com.finance.dto.keyInformationGroup;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.keyInformation.KeyInformationDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class KeyInformationGroupAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
    @ApiModelProperty(name = "keyInformations")
    private List<KeyInformationDto> keyInformations;
}