package com.finance.dto.transactionGroup;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.transaction.TransactionDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TransactionGroupAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
    @ApiModelProperty(name = "transactions")
    private List<TransactionDto> transactions;
}
