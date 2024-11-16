package com.finance.dto.userGroupNotification;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.account.AccountDto;
import com.finance.dto.notificationGroup.NotificationGroupDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserGroupNotificationAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "account")
    private AccountDto account;
    @ApiModelProperty(name = "notificationGroup")
    private NotificationGroupDto notificationGroup;
}
