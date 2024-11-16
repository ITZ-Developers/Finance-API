package com.finance.dto.service;

public interface ServiceReminderDto {
    Long getServiceId();
    String getServiceName();
    Long getAccountId();
    Integer getNumberOfDueDays();
}
