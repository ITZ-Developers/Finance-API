package com.finance.dto.debit;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.account.AccountDto;
import com.finance.dto.category.CategoryDto;
import com.finance.dto.tag.TagDto;
import com.finance.dto.transaction.TransactionDto;
import com.finance.dto.transactionGroup.TransactionGroupDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DebitAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "kind")
    private Integer kind;
    @ApiModelProperty(name = "state")
    private Integer state;
    @ApiModelProperty(name = "money")
    private String money;
    @ApiModelProperty(name = "note")
    private String note;
    @ApiModelProperty(name = "document")
    private String document;
    @ApiModelProperty(name = "transaction")
    private TransactionDto transaction;
    @ApiModelProperty(name = "category")
    private CategoryDto category;
    @ApiModelProperty(name = "transactionGroup")
    private TransactionGroupDto transactionGroup;
    @ApiModelProperty(name = "transactionDate")
    private Date transactionDate;
    @ApiModelProperty(name = "addedBy")
    private AccountDto addedBy;
    @ApiModelProperty(name = "tag")
    private TagDto tag;
}
