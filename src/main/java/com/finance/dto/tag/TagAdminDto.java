package com.finance.dto.tag;

import com.finance.dto.ABasicAdminDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TagAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "kind")
    private Integer kind;
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "colorCode")
    private String colorCode;
}
