package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.project.ProjectAdminDto;
import com.finance.dto.project.ProjectDto;
import com.finance.form.project.CreateProjectForm;
import com.finance.form.project.UpdateProjectForm;
import com.finance.mapper.ProjectMapper;
import com.finance.model.Organization;
import com.finance.model.Project;
import com.finance.model.Tag;
import com.finance.model.criteria.ProjectCriteria;
import com.finance.repository.OrganizationRepository;
import com.finance.repository.ProjectRepository;
import com.finance.repository.TagRepository;
import com.finance.repository.TaskRepository;
import com.finance.service.FinanceApiService;
import com.finance.service.KeyService;
import com.finance.utils.AESUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/v1/project")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ProjectController extends ABasicController {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private KeyService keyService;
    @Autowired
    private FinanceApiService financeApiService;
    @Autowired
    private TagRepository tagRepository;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PR_V')")
    public ApiMessageDto<ProjectAdminDto> get(@PathVariable("id") Long id) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            return makeErrorResponse(ErrorCode.PROJECT_ERROR_NOT_FOUND, "Not found project");
        }
        ProjectAdminDto projectAdminDto = projectMapper.fromEncryptEntityToEncryptProjectAdminDto(project, keyService.getFinanceKeyWrapper(), taskRepository);
        return makeSuccessResponse(projectAdminDto, "Get project success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PR_L')")
    public ApiMessageDto<ResponseListDto<List<ProjectAdminDto>>> list(ProjectCriteria projectCriteria, Pageable pageable) {
        if (projectCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_FALSE)){
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }
        if (!isSuperAdmin()) {
            projectCriteria.setPermissionAccountId(getCurrentUser());
        }
        Page<Project> projects = projectRepository.findAll(projectCriteria.getCriteria(), pageable);
        List<ProjectAdminDto> projectAdminDtos = projectMapper.fromEncryptEntityListToEncryptProjectAdminDtoList(projects.getContent(), keyService.getFinanceKeyWrapper(), taskRepository);
        ResponseListDto<List<ProjectAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(projectAdminDtos);
        responseListObj.setTotalPages(projects.getTotalPages());
        responseListObj.setTotalElements(projects.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list project success");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<ProjectDto>>> autoComplete(ProjectCriteria projectCriteria) {
        Pageable pageable = projectCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_TRUE) ? PageRequest.of(0, 10) : PageRequest.of(0, Integer.MAX_VALUE);
        projectCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        if (!isSuperAdmin()) {
            projectCriteria.setPermissionAccountId(getCurrentUser());
        }
        Page<Project> projects = projectRepository.findAll(projectCriteria.getCriteria(), pageable);
        ResponseListDto<List<ProjectDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(projectMapper.fromEncryptEntityListToEncryptProjectDtoAutoCompleteList(projects.getContent(), keyService.getFinanceKeyWrapper()));
        responseListObj.setTotalPages(projects.getTotalPages());
        responseListObj.setTotalElements(projects.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list project success");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PR_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateProjectForm createProjectForm, BindingResult bindingResult) {
        String nameProject = AESUtils.encrypt(keyService.getFinanceSecretKey(), createProjectForm.getName(), FinanceConstant.AES_ZIP_ENABLE);
        Project projectByName = projectRepository.findFirstByName(nameProject).orElse(null);
        if(projectByName != null){
            return makeErrorResponse(ErrorCode.PROJECT_ERROR_NAME_EXISTED, "Name existed");
        }
        Project project = projectMapper.fromCreateProjectFormToEntity(createProjectForm, keyService.getFinanceSecretKey());
        if (createProjectForm.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(createProjectForm.getOrganizationId()).orElse(null);
            if (organization == null) {
                return makeErrorResponse(ErrorCode.ORGANIZATION_ERROR_NOT_FOUND, "Not found organization");
            }
            project.setOrganization(organization);
        }
        if (createProjectForm.getTagId() != null) {
            Tag tag = tagRepository.findById(createProjectForm.getTagId()).orElse(null);
            if (tag == null) {
                return makeErrorResponse(ErrorCode.TAG_ERROR_NOT_FOUND, "Not found tag");
            }
            if (!FinanceConstant.TAG_KIND_PROJECT.equals(tag.getKind())) {
                return makeErrorResponse(ErrorCode.PROJECT_ERROR_TAG_INVALID, "Tag kind does not match");
            }
            project.setTag(tag);
        }
        projectRepository.save(project);
        return makeSuccessResponse(null, "Create project success");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PR_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateProjectForm updateProjectForm, BindingResult bindingResult) {
        Project project = projectRepository.findById(updateProjectForm.getId()).orElse(null);
        if (project == null) {
            return makeErrorResponse(ErrorCode.PROJECT_ERROR_NOT_FOUND, "Not found project");
        }
        String nameProject = AESUtils.encrypt(keyService.getFinanceSecretKey(), updateProjectForm.getName(), FinanceConstant.AES_ZIP_ENABLE);
        if (!project.getName().equals(nameProject) && projectRepository.findFirstByName(nameProject).isPresent()) {
            return makeErrorResponse(ErrorCode.PROJECT_ERROR_NAME_EXISTED, "Name existed");
        }
        String decryptOldLogo = AESUtils.decrypt(keyService.getFinanceSecretKey(), project.getLogo(), FinanceConstant.AES_ZIP_ENABLE);
        if (StringUtils.isNoneBlank(updateProjectForm.getLogo()) && !updateProjectForm.getLogo().equals(decryptOldLogo)) {
            financeApiService.deleteFile(decryptOldLogo);
        }
        if (updateProjectForm.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(updateProjectForm.getOrganizationId()).orElse(null);
            if (organization == null) {
                return makeErrorResponse(ErrorCode.ORGANIZATION_ERROR_NOT_FOUND, "Not found organization");
            }
            project.setOrganization(organization);
        } else {
            project.setOrganization(null);
        }
        if (updateProjectForm.getTagId() != null) {
            Tag tag = tagRepository.findById(updateProjectForm.getTagId()).orElse(null);
            if (tag == null) {
                return makeErrorResponse(ErrorCode.TAG_ERROR_NOT_FOUND, "Not found tag");
            }
            if (!FinanceConstant.TAG_KIND_PROJECT.equals(tag.getKind())) {
                return makeErrorResponse(ErrorCode.PROJECT_ERROR_TAG_INVALID, "Tag kind does not match");
            }
            project.setTag(tag);
        } else {
            project.setTag(null);
        }
        projectMapper.fromUpdateProjectFormToEntity(updateProjectForm, project, keyService.getFinanceSecretKey());
        projectRepository.save(project);
        return makeSuccessResponse(null, "Update project success");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PR_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            return makeErrorResponse(ErrorCode.PROJECT_ERROR_NOT_FOUND, "Not found project");
        }
        String decryptOldLogo = AESUtils.decrypt(keyService.getFinanceSecretKey(), project.getLogo(), FinanceConstant.AES_ZIP_ENABLE);
        financeApiService.deleteFile(decryptOldLogo);
        taskRepository.updateAllByProjectId(id);
        projectRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete project success");
    }
}
