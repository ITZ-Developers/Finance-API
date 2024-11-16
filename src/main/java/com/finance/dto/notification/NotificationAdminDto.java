package com.finance.dto.notification;

import com.finance.dto.ABasicAdminDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NotificationAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "message")
    private String message;
    @ApiModelProperty(name = "state")
    private Integer state;
    @ApiModelProperty(name = "serviceId")
    private Long serviceId;
    @ApiModelProperty(name = "accountId")
    private Long accountId;
}
