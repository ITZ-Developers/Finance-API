package com.finance.dto.keyInformation;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.account.AccountDto;
import com.finance.dto.keyInformationGroup.KeyInformationGroupDto;
import com.finance.dto.organization.OrganizationDto;
import com.finance.dto.tag.TagDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class KeyInformationAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "kind")
    private Integer kind;
    @ApiModelProperty(name = "description")
    private String description;
    @ApiModelProperty(name = "document")
    private String document;
    @ApiModelProperty(name = "account")
    private AccountDto account;
    @ApiModelProperty(name = "keyInformationGroup")
    private KeyInformationGroupDto keyInformationGroup;
    @ApiModelProperty(name = "organization")
    private OrganizationDto organization;
    @ApiModelProperty(name = "additionalInformation")
    private String additionalInformation;
    @ApiModelProperty(name = "tag")
    private TagDto tag;
}
