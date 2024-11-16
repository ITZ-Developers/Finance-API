package com.finance.dto.organizationPermission;

import com.finance.dto.account.AccountDto;
import com.finance.dto.organization.OrganizationDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrganizationPermissionDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "account")
    private AccountDto account;
    @ApiModelProperty(name = "organization")
    private OrganizationDto organization;
}
