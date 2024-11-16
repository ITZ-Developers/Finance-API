package com.finance.dto.serviceSchedule;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.service.ServiceDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ServiceScheduleAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "numberOfDueDays")
    private Integer numberOfDueDays;
    @ApiModelProperty(name = "ordering")
    private Integer ordering;
    @ApiModelProperty(name = "service")
    private ServiceDto service;
}
