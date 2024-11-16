package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.serviceNotificationGroup.ServiceNotificationGroupAdminDto;
import com.finance.dto.serviceNotificationGroup.ServiceNotificationGroupDto;
import com.finance.form.serviceNotificationGroup.CreateServiceNotificationGroupForm;
import com.finance.form.serviceNotificationGroup.UpdateServiceNotificationGroupForm;
import com.finance.mapper.ServiceNotificationGroupMapper;
import com.finance.model.NotificationGroup;
import com.finance.model.Service;
import com.finance.model.ServiceNotificationGroup;
import com.finance.model.criteria.ServiceNotificationGroupCriteria;
import com.finance.repository.NotificationGroupRepository;
import com.finance.repository.ServiceNotificationGroupRepository;
import com.finance.repository.ServiceRepository;
import com.finance.service.KeyService;
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

@RestController
@RequestMapping("/v1/service-notification-group")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ServiceNotificationGroupController extends ABasicController{
    @Autowired
    private ServiceNotificationGroupRepository serviceNotificationGroupRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private NotificationGroupRepository notificationGroupRepository;
    @Autowired
    private ServiceNotificationGroupMapper serviceNotificationGroupMapper;
    @Autowired
    private KeyService keyService;

    @GetMapping(value = "/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_N_G_V')")
    public ApiMessageDto<ServiceNotificationGroupAdminDto> get(@PathVariable("id")  Long id) {
        ServiceNotificationGroup serviceNotificationGroup = serviceNotificationGroupRepository.findById(id).orElse(null);
        if (serviceNotificationGroup == null){
            return makeErrorResponse(ErrorCode.SERVICE_NOTIFICATION_GROUP_ERROR_NOT_FOUND, "Not found service notification group");
        }
        return makeSuccessResponse(serviceNotificationGroupMapper.fromEncryptEntityToEncryptServiceNotificationGroupAdminDto(serviceNotificationGroup, keyService.getFinanceKeyWrapper()), "Get service notification group success");
    }

    @GetMapping(value = "/list", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_N_G_L')")
    public ApiMessageDto<ResponseListDto<List<ServiceNotificationGroupAdminDto>>> list(ServiceNotificationGroupCriteria serviceNotificationGroupCriteria, Pageable pageable) {
        if (serviceNotificationGroupCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_FALSE)){
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }
        Page<ServiceNotificationGroup> serviceNotificationGroups = serviceNotificationGroupRepository.findAll(serviceNotificationGroupCriteria.getCriteria(), pageable);
        ResponseListDto<List<ServiceNotificationGroupAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(serviceNotificationGroupMapper.fromEncryptEntityListToEncryptServiceNotificationGroupAdminDtoList(serviceNotificationGroups.getContent(), keyService.getFinanceKeyWrapper()));
        responseListObj.setTotalPages(serviceNotificationGroups.getTotalPages());
        responseListObj.setTotalElements(serviceNotificationGroups.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list service notification group success");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<ServiceNotificationGroupDto>>> autoComplete(ServiceNotificationGroupCriteria serviceNotificationGroupCriteria) {
        Pageable pageable = serviceNotificationGroupCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_TRUE) ? PageRequest.of(0, 10) : PageRequest.of(0, Integer.MAX_VALUE);
        serviceNotificationGroupCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<ServiceNotificationGroup> serviceNotificationGroups = serviceNotificationGroupRepository.findAll(serviceNotificationGroupCriteria.getCriteria(), pageable);
        ResponseListDto<List<ServiceNotificationGroupDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(serviceNotificationGroupMapper.fromEncryptEntityListToEncryptServiceNotificationGroupDtoList(serviceNotificationGroups.getContent(), keyService.getFinanceKeyWrapper()));
        responseListObj.setTotalPages(serviceNotificationGroups.getTotalPages());
        responseListObj.setTotalElements(serviceNotificationGroups.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list service notification group success");
    }

    @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_N_G_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateServiceNotificationGroupForm createServiceNotificationGroupForm, BindingResult bindingResult) {
        Service service = serviceRepository.findById(createServiceNotificationGroupForm.getServiceId()).orElse(null);
        if (service == null){
            return makeErrorResponse(ErrorCode.SERVICE_ERROR_NOT_FOUND, "Not found service");
        }
        NotificationGroup notificationGroup = notificationGroupRepository.findById(createServiceNotificationGroupForm.getNotificationGroupId()).orElse(null);
        if (notificationGroup == null){
            return makeErrorResponse(ErrorCode.NOTIFICATION_GROUP_ERROR_NOT_FOUND, "Not found notification group");
        }
        ServiceNotificationGroup existedByServiceIdAndNotificationGroupId = serviceNotificationGroupRepository.findFirstByServiceIdAndNotificationGroupId(service.getId(), notificationGroup.getId()).orElse(null);
        if (existedByServiceIdAndNotificationGroupId != null){
            return makeErrorResponse(ErrorCode.SERVICE_NOTIFICATION_GROUP_ERROR_SERVICE_AND_NOTIFICATION_GROUP_EXISTED, "Notification group existed this service");
        }
        ServiceNotificationGroup serviceNotificationGroup = new ServiceNotificationGroup();
        serviceNotificationGroup.setService(service);
        serviceNotificationGroup.setNotificationGroup(notificationGroup);
        serviceNotificationGroupRepository.save(serviceNotificationGroup);
        return makeSuccessResponse(null, "Create service notification group success");
    }

    @PutMapping(value = "/update", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_N_G_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateServiceNotificationGroupForm updateServiceNotificationGroupForm, BindingResult bindingResult) {
        ServiceNotificationGroup serviceNotificationGroup = serviceNotificationGroupRepository.findById(updateServiceNotificationGroupForm.getId()).orElse(null);
        if (serviceNotificationGroup == null){
            return makeErrorResponse(ErrorCode.SERVICE_NOTIFICATION_GROUP_ERROR_NOT_FOUND, "Not found service notification group");
        }
        Service service = serviceRepository.findById(updateServiceNotificationGroupForm.getServiceId()).orElse(null);
        if (service == null){
            return makeErrorResponse(ErrorCode.SERVICE_ERROR_NOT_FOUND, "Not found service");
        }
        NotificationGroup notificationGroup = notificationGroupRepository.findById(updateServiceNotificationGroupForm.getNotificationGroupId()).orElse(null);
        if (notificationGroup == null){
            return makeErrorResponse(ErrorCode.NOTIFICATION_GROUP_ERROR_NOT_FOUND, "Not found notification group");
        }
        if (!serviceNotificationGroup.getService().getId().equals(updateServiceNotificationGroupForm.getServiceId()) || !serviceNotificationGroup.getNotificationGroup().getId().equals(updateServiceNotificationGroupForm.getNotificationGroupId())){
            ServiceNotificationGroup existedByServiceIdAndNotificationGroupId = serviceNotificationGroupRepository.findFirstByServiceIdAndNotificationGroupId(service.getId(), notificationGroup.getId()).orElse(null);
            if (existedByServiceIdAndNotificationGroupId != null){
                return makeErrorResponse(ErrorCode.SERVICE_NOTIFICATION_GROUP_ERROR_SERVICE_AND_NOTIFICATION_GROUP_EXISTED, "Notification group existed this service");
            }
        }
        serviceNotificationGroup.setService(service);
        serviceNotificationGroup.setNotificationGroup(notificationGroup);
        serviceNotificationGroupRepository.save(serviceNotificationGroup);
        return makeSuccessResponse(null, "Update service notification group success");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_N_G_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        ServiceNotificationGroup serviceNotificationGroup = serviceNotificationGroupRepository.findById(id).orElse(null);
        if (serviceNotificationGroup == null){
            return makeErrorResponse(ErrorCode.SERVICE_NOTIFICATION_GROUP_ERROR_NOT_FOUND, "Not found service notification group");
        }
        serviceNotificationGroupRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete service notification group success");
    }
}
