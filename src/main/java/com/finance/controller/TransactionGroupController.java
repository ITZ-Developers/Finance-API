package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.transactionGroup.TransactionGroupAdminDto;
import com.finance.dto.transactionGroup.TransactionGroupDto;
import com.finance.form.transactionGroup.CreateTransactionGroupForm;
import com.finance.form.transactionGroup.UpdateTransactionGroupForm;
import com.finance.mapper.TransactionGroupMapper;
import com.finance.model.*;
import com.finance.model.criteria.TransactionGroupCriteria;
import com.finance.repository.DebitRepository;
import com.finance.repository.TransactionGroupRepository;
import com.finance.repository.TransactionRepository;
import com.finance.service.KeyService;
import com.finance.utils.AESUtils;
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
@RequestMapping("/v1/transaction-group")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class TransactionGroupController extends ABasicController{
    @Autowired
    private TransactionGroupRepository transactionGroupRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionGroupMapper transactionGroupMapper;
    @Autowired
    private DebitRepository debitRepository;
    @Autowired
    private KeyService keyService;

    @GetMapping(value = "/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_G_V')")
    public ApiMessageDto<TransactionGroupAdminDto> get(@PathVariable("id")  Long id) {
        TransactionGroup transactionGroup = transactionGroupRepository.findById(id).orElse(null);
        if (transactionGroup == null){
            return makeErrorResponse(ErrorCode.TRANSACTION_GROUP_ERROR_NOT_FOUND, "Not found transaction group");
        }
        TransactionGroupAdminDto transactionGroupAdminDto = transactionGroupMapper.fromEncryptEntityToEncryptTransactionGroupAdminDto(transactionGroup, keyService.getFinanceKeyWrapper());
        return makeSuccessResponse(transactionGroupAdminDto, "Get transaction group success");
    }

    @GetMapping(value = "/list", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_G_L')")
    public ApiMessageDto<ResponseListDto<List<TransactionGroupAdminDto>>> list(TransactionGroupCriteria transactionGroupCriteria, Pageable pageable) {
        if (transactionGroupCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_FALSE)){
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }
        if (!isSuperAdmin()) {
            transactionGroupCriteria.setPermissionAccountId(getCurrentUser());
        }
        Page<TransactionGroup> transactionGroups = transactionGroupRepository.findAll(transactionGroupCriteria.getCriteria(), pageable);
        ResponseListDto<List<TransactionGroupAdminDto>> responseListObj = new ResponseListDto<>();
        List<TransactionGroupAdminDto> transactionGroupAdminDtos = transactionGroupMapper.fromEncryptEntityListToEncryptTransactionGroupAdminDtoList(transactionGroups.getContent(), keyService.getFinanceKeyWrapper());
        responseListObj.setContent(transactionGroupAdminDtos);
        responseListObj.setTotalPages(transactionGroups.getTotalPages());
        responseListObj.setTotalElements(transactionGroups.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list transaction group success");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<TransactionGroupDto>>> autoComplete(TransactionGroupCriteria transactionGroupCriteria) {
        Pageable pageable = transactionGroupCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_TRUE) ? PageRequest.of(0, 10) : PageRequest.of(0, Integer.MAX_VALUE);
        transactionGroupCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        if (!isSuperAdmin()) {
            transactionGroupCriteria.setPermissionAccountId(getCurrentUser());
        }
        Page<TransactionGroup> transactionGroups = transactionGroupRepository.findAll(transactionGroupCriteria.getCriteria(), pageable);
        ResponseListDto<List<TransactionGroupDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(transactionGroupMapper.fromEncryptEntityListToEncryptTransactionGroupDtoList(transactionGroups.getContent(), keyService.getFinanceKeyWrapper()));
        responseListObj.setTotalPages(transactionGroups.getTotalPages());
        responseListObj.setTotalElements(transactionGroups.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list transaction group success");
    }

    @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_G_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateTransactionGroupForm createTransactionGroupForm, BindingResult bindingResult) {
        TransactionGroup transactionGroup = transactionGroupMapper.fromCreateTransactionGroupFormToEncryptEntity(createTransactionGroupForm, keyService.getFinanceSecretKey());
        TransactionGroup transactionGroupByName = transactionGroupRepository.findFirstByName(transactionGroup.getName()).orElse(null);
        if (transactionGroupByName != null){
            return makeErrorResponse(ErrorCode.TRANSACTION_GROUP_ERROR_NAME_EXISTED, "Name transaction group existed");
        }
        transactionGroupRepository.save(transactionGroup);
        return makeSuccessResponse(null, "Create transaction group success");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_G_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateTransactionGroupForm updateTransactionGroupForm, BindingResult bindingResult) {
        TransactionGroup transactionGroup = transactionGroupRepository.findById(updateTransactionGroupForm.getId()).orElse(null);
        if (transactionGroup == null){
            return makeErrorResponse(ErrorCode.TRANSACTION_GROUP_ERROR_NOT_FOUND, "Not found transaction group");
        }
        String ecryptName = AESUtils.encrypt(keyService.getFinanceSecretKey(), updateTransactionGroupForm.getName(), FinanceConstant.AES_ZIP_ENABLE);
        if(!transactionGroup.getName().equals(ecryptName)){
            TransactionGroup transactionGroupByName = transactionGroupRepository.findFirstByName(ecryptName).orElse(null);
            if (transactionGroupByName != null){
                return makeErrorResponse(ErrorCode.TRANSACTION_GROUP_ERROR_NAME_EXISTED, "Name transaction group existed");
            }
        }
        transactionGroupMapper.fromUpdateTransactionGroupFormToEncryptEntity(updateTransactionGroupForm, transactionGroup, keyService.getFinanceSecretKey());
        transactionGroupRepository.save(transactionGroup);
        return makeSuccessResponse(null, "Update transaction group success");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_G_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        TransactionGroup transactionGroup = transactionGroupRepository.findById(id).orElse(null);
        if (transactionGroup == null){
            return makeErrorResponse(ErrorCode.TRANSACTION_GROUP_ERROR_NOT_FOUND, "Not found transaction group");
        }
        transactionRepository.updateAllByTransactionGroupId(id);
        debitRepository.updateAllByTransactionGroupId(id);
        transactionGroupRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete transaction group success");
    }
}