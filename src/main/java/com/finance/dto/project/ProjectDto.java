package com.finance.dto.project;

import com.finance.dto.organization.OrganizationDto;
import com.finance.dto.tag.TagDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "organization")
    private OrganizationDto organization;
    @ApiModelProperty(name = "note")
    private String note;
    @ApiModelProperty(name = "tag")
    private TagDto tag;
}
