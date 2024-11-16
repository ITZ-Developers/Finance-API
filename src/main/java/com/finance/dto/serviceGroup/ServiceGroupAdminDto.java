package com.finance.dto.serviceGroup;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.service.ServiceDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ServiceGroupAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
    @ApiModelProperty(name = "services")
    private List<ServiceDto> services;
}
