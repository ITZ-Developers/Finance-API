package com.finance.dto.task;

import com.finance.dto.organization.OrganizationDto;
import com.finance.dto.project.ProjectDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "state")
    private Integer state;
    @ApiModelProperty(name = "note")
    private String note;
    @ApiModelProperty(name = "document")
    private String document;
    @ApiModelProperty(name = "project")
    private ProjectDto project;
    @ApiModelProperty(name = "parent")
    private TaskDto parent;
}
