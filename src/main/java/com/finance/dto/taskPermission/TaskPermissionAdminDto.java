package com.finance.dto.taskPermission;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.account.AccountDto;
import com.finance.dto.task.TaskDto;
import com.finance.dto.project.ProjectDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskPermissionAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "permissionKind")
    private Integer permissionKind;
    @ApiModelProperty(name = "account")
    private AccountDto account;
    @ApiModelProperty(name = "task")
    private TaskDto task;
    @ApiModelProperty(name = "project")
    private ProjectDto project;
}
