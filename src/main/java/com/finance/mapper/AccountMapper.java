package com.finance.mapper;

import com.finance.form.account.CreateAccountAdminForm;
import com.finance.form.account.UpdateAccountAdminForm;
import com.finance.form.account.UpdateProfileAdminForm;
import com.finance.model.Account;
import com.finance.dto.account.AccountAdminDto;
import com.finance.dto.account.AccountDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {GroupMapper.class, DepartmentMapper.class})
public interface AccountMapper {

    @Mapping(source = "username", target = "username")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    Account fromCreateAccountAdminFormToEntity(CreateAccountAdminForm createAccountAdminForm);

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateAccountAdminFormToEntity(UpdateAccountAdminForm updateAccountAdminForm, @MappingTarget Account account);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "group", target = "group", qualifiedByName = "fromEntityToGroupDtoForAccount")
    @Mapping(source = "department", target = "department", qualifiedByName = "fromEntityToDepartmentDto")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "isSuperAdmin", target = "isSuperAdmin")
    @Mapping(source = "publicKey", target = "publicKey")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToAccountAdminDto")
    AccountAdminDto fromEntityToAccountAdminDto(Account account);

    @IterableMapping(elementTargetType = AccountAdminDto.class, qualifiedByName = "fromEntityToAccountAdminDto")
    List<AccountAdminDto> fromEntityListToAccountAdminDtoList(List<Account> accounts);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "group", target = "group", qualifiedByName = "fromEntityToGroupDto")
    @Mapping(source = "isSuperAdmin", target = "isSuperAdmin")
    @Mapping(source = "publicKey", target = "publicKey")
    @Mapping(source = "department", target = "department", qualifiedByName = "fromEntityToDepartmentDto")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToAccountDto")
    AccountDto fromEntityToAccountDto(Account account);

    @IterableMapping(elementTargetType = AccountDto.class, qualifiedByName = "fromEntityToAccountDto")
    List<AccountDto> fromEntityListToAccountDtoList(List<Account> accounts);

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "birthDate", target = "birthDate")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateProfileAdminFormToEntity(UpdateProfileAdminForm updateProfileAdminForm, @MappingTarget Account account);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "department", target = "department", qualifiedByName = "fromEntityToDepartmentDto")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToAccountDtoForNotificationGroup")
    AccountDto fromEntityToAccountDtoForNotificationGroup(Account account);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "fullName", target = "fullName")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToAccountDtoAutoComplete")
    AccountDto fromEntityToAccountDtoAutoComplete(Account account);

    @IterableMapping(elementTargetType = AccountDto.class, qualifiedByName = "fromEntityToAccountDtoAutoComplete")
    List<AccountDto> fromEntityListToAccountDtoListAutoComplete(List<Account> accounts);
}