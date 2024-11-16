package com.finance.dto.notificationGroup;

import com.finance.dto.account.AccountDto;
import com.finance.dto.service.ServiceDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class NotificationGroupDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
}
