package com.finance.dto.servicePermission;

import com.finance.dto.account.AccountDto;
import com.finance.dto.service.ServiceDto;
import com.finance.dto.serviceGroup.ServiceGroupDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ServicePermissionDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "permissionKind")
    private Integer permissionKind;
    @ApiModelProperty(name = "account")
    private AccountDto account;
    @ApiModelProperty(name = "service")
    private ServiceDto service;
    @ApiModelProperty(name = "serviceGroup")
    private ServiceGroupDto serviceGroup;
}
