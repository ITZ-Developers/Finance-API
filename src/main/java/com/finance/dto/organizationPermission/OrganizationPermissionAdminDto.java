package com.finance.dto.organizationPermission;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.account.AccountDto;
import com.finance.dto.organization.OrganizationDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrganizationPermissionAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "account")
    private AccountDto account;
    @ApiModelProperty(name = "organization")
    private OrganizationDto organization;
}
