package com.finance.dto.transactionHistory;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.account.AccountDto;
import com.finance.dto.transaction.TransactionDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TransactionHistoryAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "note")
    private String note;
    @ApiModelProperty(name = "state")
    private Integer state;
    @ApiModelProperty(name = "account")
    private AccountDto account;
    @ApiModelProperty(name = "transaction")
    private TransactionDto transaction;
}
