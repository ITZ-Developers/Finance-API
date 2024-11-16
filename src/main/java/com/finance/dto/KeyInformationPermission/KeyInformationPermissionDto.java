package com.finance.dto.KeyInformationPermission;

import com.finance.dto.account.AccountDto;
import com.finance.dto.keyInformation.KeyInformationDto;
import com.finance.dto.keyInformationGroup.KeyInformationGroupDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class KeyInformationPermissionDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "permissionKind")
    private Integer permissionKind;
    @ApiModelProperty(name = "account")
    private AccountDto account;
    @ApiModelProperty(name = "keyInformation")
    private KeyInformationDto keyInformation;
    @ApiModelProperty(name = "keyInformationGroup")
    private KeyInformationGroupDto keyInformationGroup;
}