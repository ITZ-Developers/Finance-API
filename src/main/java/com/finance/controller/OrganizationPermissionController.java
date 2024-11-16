package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.organizationPermission.OrganizationPermissionAdminDto;
import com.finance.dto.organizationPermission.OrganizationPermissionDto;
import com.finance.form.organizationPermission.CreateOrganizationPermissionForm;
import com.finance.form.organizationPermission.UpdateOrganizationPermissionForm;
import com.finance.mapper.OrganizationPermissionMapper;
import com.finance.model.*;
import com.finance.model.criteria.OrganizationPermissionCriteria;
import com.finance.repository.*;
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
@RequestMapping("/v1/organization-permission")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class OrganizationPermissionController extends ABasicController {
    @Autowired
    private OrganizationPermissionRepository organizationPermissionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private OrganizationPermissionMapper organizationPermissionMapper;
    @Autowired
    private KeyService keyService;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('OR_P_V')")
    public ApiMessageDto<OrganizationPermissionAdminDto> get(@PathVariable("id") Long id) {
        OrganizationPermission organizationPermission = organizationPermissionRepository.findById(id).orElse(null);
        if (organizationPermission == null) {
            return makeErrorResponse(ErrorCode.ORGANIZATION_PERMISSION_ERROR_NOT_FOUND, "Not found organization permission");
        }
        return makeSuccessResponse(organizationPermissionMapper.fromEncryptEntityToEncryptOrganizationPermissionAdminDto(organizationPermission, keyService.getFinanceKeyWrapper()), "Get organization permission success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('OR_P_L')")
    public ApiMessageDto<ResponseListDto<List<OrganizationPermissionAdminDto>>> list(OrganizationPermissionCriteria organizationPermissionCriteria, Pageable pageable) {
        if (organizationPermissionCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_FALSE)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }
        Page<OrganizationPermission> organizationPermissions = organizationPermissionRepository.findAll(organizationPermissionCriteria.getCriteria(), pageable);
        ResponseListDto<List<OrganizationPermissionAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(organizationPermissionMapper.fromEncryptEntityListToEncryptOrganizationPermissionAdminDtoList(organizationPermissions.getContent(), keyService.getFinanceKeyWrapper()));
        responseListObj.setTotalPages(organizationPermissions.getTotalPages());
        responseListObj.setTotalElements(organizationPermissions.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list organization permission success");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<OrganizationPermissionDto>>> autoComplete(OrganizationPermissionCriteria organizationPermissionCriteria) {
        Pageable pageable = organizationPermissionCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_TRUE) ? PageRequest.of(0, 10) : PageRequest.of(0, Integer.MAX_VALUE);
        organizationPermissionCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<OrganizationPermission> organizationPermissions = organizationPermissionRepository.findAll(organizationPermissionCriteria.getCriteria(), pageable);
        ResponseListDto<List<OrganizationPermissionDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(organizationPermissionMapper.fromEncryptEntityListToEncryptOrganizationPermissionDtoList(organizationPermissions.getContent(), keyService.getFinanceKeyWrapper()));
        responseListObj.setTotalPages(organizationPermissions.getTotalPages());
        responseListObj.setTotalElements(organizationPermissions.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list organization permission success");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('OR_P_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateOrganizationPermissionForm createOrganizationPermissionForm, BindingResult bindingResult) {
        OrganizationPermission organizationPermission = new OrganizationPermission();
        Account account = accountRepository.findById(createOrganizationPermissionForm.getAccountId()).orElse(null);
        if (account == null) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NOT_FOUND, "Not found account");
        }
        organizationPermission.setAccount(account);
        Organization organization = organizationRepository.findById(createOrganizationPermissionForm.getOrganizationId()).orElse(null);
        if (organization == null) {
            return makeErrorResponse(ErrorCode.ORGANIZATION_ERROR_NOT_FOUND, "Not found organization");
        }
        organizationPermission.setOrganization(organization);
        if (organizationPermissionRepository.findFirstByAccountIdAndOrganizationId(account.getId(), organization.getId()).isPresent()) {
            return makeErrorResponse(ErrorCode.ORGANIZATION_PERMISSION_ERROR_ACCOUNT_AND_ORGANIZATION_EXISTED, "Organization permission exists in this account");
        }
        organizationPermissionRepository.save(organizationPermission);
        return makeSuccessResponse(null, "Create organization permission success");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('OR_P_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateOrganizationPermissionForm updateOrganizationPermissionForm, BindingResult bindingResult) {
        OrganizationPermission organizationPermission = organizationPermissionRepository.findById(updateOrganizationPermissionForm.getId()).orElse(null);
        if (organizationPermission == null) {
            return makeErrorResponse(ErrorCode.ORGANIZATION_PERMISSION_ERROR_NOT_FOUND, "Not found organization permission");
        }
        Account account = accountRepository.findById(updateOrganizationPermissionForm.getAccountId()).orElse(null);
        if (account == null) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NOT_FOUND, "Not found account");
        }
        organizationPermission.setAccount(account);
        Organization organization = organizationRepository.findById(updateOrganizationPermissionForm.getOrganizationId()).orElse(null);
        if (organization == null) {
            return makeErrorResponse(ErrorCode.ORGANIZATION_ERROR_NOT_FOUND, "Not found organization");
        }
        organizationPermission.setOrganization(organization);
        if (organizationPermissionRepository.findFirstByAccountIdAndOrganizationId(account.getId(), organization.getId()).isPresent()) {
            return makeErrorResponse(ErrorCode.ORGANIZATION_PERMISSION_ERROR_ACCOUNT_AND_ORGANIZATION_EXISTED, "Organization permission exists in this account");
        }
        organizationPermissionRepository.save(organizationPermission);
        return makeSuccessResponse(null, "Update organization permission success");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('OR_P_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        OrganizationPermission organizationPermission = organizationPermissionRepository.findById(id).orElse(null);
        if (organizationPermission == null) {
            return makeErrorResponse(ErrorCode.ORGANIZATION_PERMISSION_ERROR_NOT_FOUND, "Not found organization permission");
        }
        organizationPermissionRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete organization permission success");
    }
}