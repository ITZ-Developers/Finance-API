package com.finance.dto.transactionHistory;

import com.finance.dto.account.AccountDto;
import com.finance.dto.transaction.TransactionDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TransactionHistoryDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "note")
    private String note;
    @ApiModelProperty(name = "state")
    private Integer state;
    @ApiModelProperty(name = "status")
    private Integer status;
    @ApiModelProperty(name = "account")
    private AccountDto account;
    @ApiModelProperty(name = "transaction")
    private TransactionDto transaction;
}
