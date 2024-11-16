package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.transactionHistory.TransactionHistoryAdminDto;
import com.finance.dto.transactionHistory.TransactionHistoryDto;
import com.finance.form.transactionHistory.UpdateTransactionHistoryForm;
import com.finance.mapper.TransactionHistoryMapper;
import com.finance.model.TransactionHistory;
import com.finance.model.criteria.TransactionHistoryCriteria;
import com.finance.repository.TransactionHistoryRepository;
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
@RequestMapping("/v1/transaction-history")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class TransactionHistoryController extends ABasicController {
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    TransactionHistoryMapper transactionHistoryMapper;
    @Autowired
    private KeyService keyService;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_H_V')")
    public ApiMessageDto<TransactionHistoryAdminDto> get(@PathVariable("id") Long id) {
        TransactionHistory transactionHistory = transactionHistoryRepository.findById(id).orElse(null);
        if (transactionHistory == null) {
            return makeErrorResponse(ErrorCode.TRANSACTION_HISTORY_ERROR_NOT_FOUND, "Not found transaction history");
        }
        TransactionHistoryAdminDto transactionHistoryAdminDto = transactionHistoryMapper.fromEncryptEntityToEncryptTransactionHistoryAdminDto(transactionHistory, keyService.getFinanceKeyWrapper());
        return makeSuccessResponse(transactionHistoryAdminDto, "Get transaction success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_H_L')")
    public ApiMessageDto<ResponseListDto<List<TransactionHistoryAdminDto>>> list(TransactionHistoryCriteria transactionHistoryCriteria, Pageable pageable) {
        if (transactionHistoryCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_FALSE)){
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }
        Page<TransactionHistory> transactionHistories = transactionHistoryRepository.findAll(transactionHistoryCriteria.getCriteria(), pageable);
        ResponseListDto<List<TransactionHistoryAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(transactionHistoryMapper.fromEncryptEntityListToEncryptTransactionHistoryAdminDtoList(transactionHistories.getContent(), keyService.getFinanceKeyWrapper()));
        responseListObj.setTotalPages(transactionHistories.getTotalPages());
        responseListObj.setTotalElements(transactionHistories.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list transaction history success");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<TransactionHistoryDto>>> autoComplete(TransactionHistoryCriteria transactionHistoryCriteria) {
        Pageable pageable = transactionHistoryCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_TRUE) ? PageRequest.of(0, 10) : PageRequest.of(0, Integer.MAX_VALUE);
        transactionHistoryCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<TransactionHistory> transactionHistories = transactionHistoryRepository.findAll(transactionHistoryCriteria.getCriteria(), pageable);
        ResponseListDto<List<TransactionHistoryDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(transactionHistoryMapper.fromEntityListToTransactionHistoryDtoList(transactionHistories.getContent()));
        responseListObj.setTotalPages(transactionHistories.getTotalPages());
        responseListObj.setTotalElements(transactionHistories.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list transaction history success");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_H_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateTransactionHistoryForm updateTransactionHistoryForm, BindingResult bindingResult) {
        TransactionHistory transactionHistory = transactionHistoryRepository.findById(updateTransactionHistoryForm.getId()).orElse(null);
        if (transactionHistory == null) {
            return makeErrorResponse(ErrorCode.TRANSACTION_HISTORY_ERROR_NOT_FOUND, "Not found transaction history");
        }
        transactionHistoryMapper.fromUpdateTransactionHistoryFormToEntity(updateTransactionHistoryForm, transactionHistory);
        transactionHistoryRepository.save(transactionHistory);
        return makeSuccessResponse(null, "Update transaction history success");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_H_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        TransactionHistory transactionHistory = transactionHistoryRepository.findById(id).orElse(null);
        if (transactionHistory == null) {
            return makeErrorResponse(ErrorCode.TRANSACTION_HISTORY_ERROR_NOT_FOUND, "Not found transaction history");
        }
        transactionHistoryRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete transaction history success");
    }
}
