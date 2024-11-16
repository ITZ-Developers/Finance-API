package com.finance.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.constant.FinanceConstant;
import com.finance.dto.DocumentDto;
import com.finance.utils.AESUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class DocumentService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FinanceApiService financeApiService;

    public List<DocumentDto> parseJSONDocumentToDocumentDtoList(String document, String secretKey) {
        try {
            String parsedDocument = (secretKey != null) ? AESUtils.decrypt(secretKey, document, FinanceConstant.AES_ZIP_ENABLE) : document;
            return objectMapper.readValue(parsedDocument, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON document to object list", e);
        }
    }

    private boolean validateDocumentString(List<DocumentDto> documentDtos) {
        for (DocumentDto documentDto : documentDtos) {
            if (StringUtils.isBlank(documentDto.getName()) || StringUtils.isBlank(documentDto.getUrl())) {
                return true;
            }
        }
        return false;
    }

    public boolean isNotValidCreateDocumentString(String documentString) {
        return validateDocumentString(parseJSONDocumentToDocumentDtoList(documentString, null));
    }

    public boolean isNotValidUpdateDocumentString(String updateDocumentString, String entityDocumentString, String secretKey) {
        List<DocumentDto> entityDocuments = parseJSONDocumentToDocumentDtoList(entityDocumentString, secretKey);
        List<DocumentDto> updateDocuments = updateDocumentString != null ?
                parseJSONDocumentToDocumentDtoList(updateDocumentString, null) :
                Collections.emptyList();
        if (validateDocumentString(updateDocuments)) {
            return true;
        }
        List<DocumentDto> documentsNotInUpdate = new ArrayList<>(entityDocuments);
        documentsNotInUpdate.removeAll(updateDocuments);
        deleteDocuments(documentsNotInUpdate);
        return false;
    }

    private void deleteDocuments(List<DocumentDto> documents) {
        for (DocumentDto document : documents) {
            financeApiService.deleteFile(document.getUrl());
        }
    }

    public void deleteDocument(String documentString, String secretKey) {
        if (documentString != null) {
            List<DocumentDto> documents = parseJSONDocumentToDocumentDtoList(documentString, secretKey);
            deleteDocuments(documents);
        }
    }
}
