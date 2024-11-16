package com.finance.mapper;

import com.finance.dto.department.DepartmentAdminDto;
import com.finance.dto.department.DepartmentDto;
import com.finance.form.department.CreateDepartmentForm;
import com.finance.form.department.UpdateDepartmentForm;
import com.finance.model.Department;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepartmentMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    Department fromCreateDepartmentFormToEntity(CreateDepartmentForm createDepartmentForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateDepartmentFormToEntity(UpdateDepartmentForm updateDepartmentForm, @MappingTarget Department department);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToDepartmentAdminDto")
    DepartmentAdminDto fromEntityToDepartmentAdminDto(Department department);

    @IterableMapping(elementTargetType = DepartmentAdminDto.class, qualifiedByName = "fromEntityToDepartmentAdminDto")
    List<DepartmentAdminDto> fromEntityListToDepartmentAdminDtoList(List<Department> departments);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToDepartmentDto")
    DepartmentDto fromEntityToDepartmentDto(Department department);

    @IterableMapping(elementTargetType = DepartmentDto.class, qualifiedByName = "fromEntityToDepartmentDto")
    List<DepartmentDto> fromEntityListToDepartmentDtoList(List<Department> departments);
}