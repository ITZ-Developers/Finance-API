package com.finance.dto.organization;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.keyInformation.KeyInformationDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrganizationAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "logo")
    private String logo;
    @ApiModelProperty(name = "keyInformations")
    private List<KeyInformationDto> keyInformations;
}