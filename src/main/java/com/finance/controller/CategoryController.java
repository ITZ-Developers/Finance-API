package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.category.CategoryAdminDto;
import com.finance.dto.category.CategoryDto;
import com.finance.form.category.CreateCategoryForm;
import com.finance.form.category.UpdateCategoryForm;
import com.finance.mapper.CategoryMapper;
import com.finance.model.Category;
import com.finance.model.criteria.CategoryCriteria;
import com.finance.repository.CategoryRepository;
import com.finance.repository.DebitRepository;
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
@RequestMapping("/v1/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CategoryController extends ABasicController{
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private DebitRepository debitRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private KeyService keyService;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CA_V')")
    public ApiMessageDto<CategoryAdminDto> get(@PathVariable("id") Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            return makeErrorResponse(ErrorCode.CATEGORY_ERROR_NOT_FOUND, "Not found category");
        }
        return makeSuccessResponse(categoryMapper.fromEncryptEntityToEncryptCategoryAdminDto(category, keyService.getFinanceKeyWrapper()), "Get category success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CA_L')")
    public ApiMessageDto<ResponseListDto<List<CategoryAdminDto>>> list(CategoryCriteria categoryCriteria, Pageable pageable) {
        if (categoryCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_FALSE)){
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }
        Page<Category> categories = categoryRepository.findAll(categoryCriteria.getCriteria(), pageable);
        ResponseListDto<List<CategoryAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(categoryMapper.fromEncryptEntityListToEncryptCategoryAdminDtoList(categories.getContent(), keyService.getFinanceKeyWrapper()));
        responseListObj.setTotalPages(categories.getTotalPages());
        responseListObj.setTotalElements(categories.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list category success");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<CategoryDto>>> autoComplete(CategoryCriteria categoryCriteria) {
        Pageable pageable = categoryCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_TRUE) ? PageRequest.of(0, 10) : PageRequest.of(0, Integer.MAX_VALUE);
        categoryCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<Category> categories = categoryRepository.findAll(categoryCriteria.getCriteria(), pageable);
        ResponseListDto<List<CategoryDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(categoryMapper.fromEncryptEntityListToEncryptCategoryDtoList(categories.getContent(), keyService.getFinanceKeyWrapper()));
        responseListObj.setTotalPages(categories.getTotalPages());
        responseListObj.setTotalElements(categories.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list category success");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CA_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateCategoryForm createCategoryForm, BindingResult bindingResult) {
        Category category = categoryMapper.fromCreateCategoryFormToEncryptEntity(createCategoryForm, keyService.getFinanceSecretKey());
        Category categoryByName = categoryRepository.findFirstByNameAndKind(category.getName(), category.getKind()).orElse(null);
        if(categoryByName != null){
            return makeErrorResponse(ErrorCode.CATEGORY_ERROR_NAME_EXISTED, "Name existed in this kind");
        }
        categoryRepository.save(category);
        return makeSuccessResponse(null, "Create category success");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CA_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCategoryForm updateCategoryForm, BindingResult bindingResult) {
        Category category = categoryRepository.findById(updateCategoryForm.getId()).orElse(null);
        if (category == null){
            return makeErrorResponse(ErrorCode.CATEGORY_ERROR_NOT_FOUND, "Not found category");
        }
        String encryptName = AESUtils.encrypt(keyService.getFinanceSecretKey(), updateCategoryForm.getName(), FinanceConstant.AES_ZIP_ENABLE);
        if(!category.getName().equals(encryptName)){
            Category categoryByName = categoryRepository.findFirstByNameAndKind(encryptName, category.getKind()).orElse(null);
            if(categoryByName != null){
                return makeErrorResponse(ErrorCode.CATEGORY_ERROR_NAME_EXISTED, "Name existed in this kind");
            }
        }
        categoryMapper.fromUpdateCategoryFormToEncryptEntity(updateCategoryForm, category, keyService.getFinanceSecretKey());
        categoryRepository.save(category);
        return makeSuccessResponse(null, "Update category success");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CA_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            return makeErrorResponse(ErrorCode.CATEGORY_ERROR_NOT_FOUND, "Not found category");
        }
        transactionRepository.updateAllByCategoryId(id);
        debitRepository.updateAllByCategoryId(id);
        categoryRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete category success");
    }
}
