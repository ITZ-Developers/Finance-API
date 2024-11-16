package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.notification.MyNotificationDto;
import com.finance.dto.notification.NotificationAdminDto;
import com.finance.dto.notification.NotificationDto;
import com.finance.form.notification.ReadNotificationForm;
import com.finance.mapper.NotificationMapper;
import com.finance.model.Notification;
import com.finance.model.criteria.NotificationCriteria;
import com.finance.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/notification")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class NotificationController extends ABasicController {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationMapper notificationMapper;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NO_V')")
    public ApiMessageDto<NotificationDto> get(@PathVariable("id") Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification == null) {
            return makeErrorResponse(ErrorCode.NOTIFICATION_ERROR_NOT_FOUND, "Not found notification");
        }
        return makeSuccessResponse(notificationMapper.fromEntityToNotificationDto(notification), "Get notification success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NO_L')")
    public ApiMessageDto<ResponseListDto<List<NotificationAdminDto>>> list(NotificationCriteria notificationCriteria, Pageable pageable) {
        if (notificationCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_FALSE)){
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }
        Page<Notification> notifications = notificationRepository.findAll(notificationCriteria.getCriteria(), pageable);
        ResponseListDto<List<NotificationAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(notificationMapper.fromEntityListToNotificationAdminDtoList(notifications.getContent()));
        responseListObj.setTotalPages(notifications.getTotalPages());
        responseListObj.setTotalElements(notifications.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list notification success");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<NotificationDto>>> autoComplete(NotificationCriteria notificationCriteria) {
        Pageable pageable = notificationCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_TRUE) ? PageRequest.of(0, 10) : PageRequest.of(0, Integer.MAX_VALUE);
        notificationCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<Notification> notifications = notificationRepository.findAll(notificationCriteria.getCriteria(), pageable);
        ResponseListDto<List<NotificationDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(notificationMapper.fromEntityListToNotificationDtoList(notifications.getContent()));
        responseListObj.setTotalPages(notifications.getTotalPages());
        responseListObj.setTotalElements(notifications.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list notification success");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NO_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification == null) {
            return makeErrorResponse(ErrorCode.NOTIFICATION_ERROR_NOT_FOUND, "Not found notification");
        }
        notificationRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete notification success");
    }

    @GetMapping(value = "/my-notification", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<MyNotificationDto> getMyNotifications(NotificationCriteria notificationCriteria, Pageable pageable) {
        notificationCriteria.setAccountId(getCurrentUser());
        notificationCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<Notification> notifications = notificationRepository.findAll(notificationCriteria.getCriteria(), pageable);
        MyNotificationDto myNotificationDto = new MyNotificationDto();
        myNotificationDto.setContent(notificationMapper.fromEntityListToNotificationDtoList(notifications.getContent()));
        myNotificationDto.setTotalPages(notifications.getTotalPages());
        myNotificationDto.setTotalElements(notifications.getTotalElements());
        myNotificationDto.setTotalUnread(notificationRepository.countByAccountIdAndStateAndStatus(getCurrentUser(), FinanceConstant.NOTIFICATION_STATE_SENT, FinanceConstant.STATUS_ACTIVE));
        return makeSuccessResponse(myNotificationDto, "Get my notifications success");
    }

    @PutMapping(value = "/read", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> read(@Valid @RequestBody ReadNotificationForm readNotificationForm, BindingResult bindingResult) {
        Notification notification = notificationRepository.findById(readNotificationForm.getId()).orElse(null);
        if (notification == null) {
            return makeErrorResponse(ErrorCode.NOTIFICATION_ERROR_NOT_FOUND, "Not found notification");
        }
        if (Objects.equals(notification.getState(), FinanceConstant.NOTIFICATION_STATE_SENT)) {
            notification.setState(FinanceConstant.NOTIFICATION_STATE_READ);
            notificationRepository.save(notification);
        }
        return makeSuccessResponse(null, "Read notification success");
    }

    @PutMapping(value = "/read-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> readAll() {
        notificationRepository.changeStateAllByAccountId(getCurrentUser(), FinanceConstant.NOTIFICATION_STATE_READ);
        return makeSuccessResponse(null, "Read all notifications success");
    }

    @DeleteMapping(value = "/delete-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> deleteAll() {
        notificationRepository.deleteAllByAccountId(getCurrentUser());
        return makeSuccessResponse(null, "Delete all notifications success");
    }
}
