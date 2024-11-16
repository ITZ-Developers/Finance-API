package com.finance.dto.transactionPermission;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.account.AccountDto;
import com.finance.dto.transaction.TransactionDto;
import com.finance.dto.transactionGroup.TransactionGroupDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TransactionPermissionAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "permissionKind")
    private Integer permissionKind;
    @ApiModelProperty(name = "account")
    private AccountDto account;
    @ApiModelProperty(name = "transaction")
    private TransactionDto transaction;
    @ApiModelProperty(name = "transactionGroup")
    private TransactionGroupDto transactionGroup;
}
