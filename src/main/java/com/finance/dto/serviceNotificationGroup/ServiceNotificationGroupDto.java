package com.finance.dto.serviceNotificationGroup;

import com.finance.dto.account.AccountDto;
import com.finance.dto.notificationGroup.NotificationGroupDto;
import com.finance.dto.service.ServiceDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ServiceNotificationGroupDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "service")
    private ServiceDto service;
    @ApiModelProperty(name = "notificationGroup")
    private NotificationGroupDto notificationGroup;
}
