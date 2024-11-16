package com.finance.dto.serviceNotificationGroup;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.notificationGroup.NotificationGroupDto;
import com.finance.dto.service.ServiceDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ServiceNotificationGroupAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "service")
    private ServiceDto service;
    @ApiModelProperty(name = "notificationGroup")
    private NotificationGroupDto notificationGroup;
}
