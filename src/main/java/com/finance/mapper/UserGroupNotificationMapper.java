package com.finance.mapper;

import com.finance.dto.account.KeyWrapperDto;
import com.finance.dto.userGroupNotification.UserGroupNotificationAdminDto;
import com.finance.dto.userGroupNotification.UserGroupNotificationDto;
import com.finance.model.UserGroupNotification;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AccountMapper.class, NotificationGroupMapper.class} )
public interface UserGroupNotificationMapper extends EncryptDecryptMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "account", target = "account", qualifiedByName = "fromEntityToAccountDtoForNotificationGroup")
    @Mapping(source = "notificationGroup", target = "notificationGroup", qualifiedByName = "fromEncryptEntityToEncryptNotificationGroupDto")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToUserGroupNotificationAdminDto")
    UserGroupNotificationAdminDto fromEntityToUserGroupNotificationAdminDto(UserGroupNotification userGroupNotification, @Context KeyWrapperDto keyWrapper);

    @IterableMapping(elementTargetType = UserGroupNotificationAdminDto.class, qualifiedByName = "fromEntityToUserGroupNotificationAdminDto")
    List<UserGroupNotificationAdminDto> fromEntityListToUserGroupNotificationAdminDtoList(List<UserGroupNotification> userGroupNotifications, @Context KeyWrapperDto keyWrapper);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "account", target = "account", qualifiedByName = "fromEntityToAccountDtoForNotificationGroup")
    @Mapping(source = "notificationGroup", target = "notificationGroup", qualifiedByName = "fromEncryptEntityToEncryptNotificationGroupDto")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEncryptEntityToEncryptUserGroupNotificationDto")
    UserGroupNotificationDto fromEncryptEntityToEncryptUserGroupNotificationDto(UserGroupNotification userGroupNotification, @Context KeyWrapperDto keyWrapper);

    @IterableMapping(elementTargetType = UserGroupNotificationDto.class, qualifiedByName = "fromEncryptEntityToEncryptUserGroupNotificationDto")
    List<UserGroupNotificationDto> fromEncryptEntityListToEncryptUserGroupNotificationDtoList(List<UserGroupNotification> userGroupNotifications, @Context KeyWrapperDto keyWrapper);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "account", target = "account", qualifiedByName = "fromEntityToAccountDtoForNotificationGroup")
    @Mapping(source = "notificationGroup", target = "notificationGroup", qualifiedByName = "fromEncryptEntityToEncryptNotificationGroupDto")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEncryptEntityToEncryptUserGroupNotificationAdminDto")
    UserGroupNotificationAdminDto fromEncryptEntityToEncryptUserGroupNotificationAdminDto(UserGroupNotification userGroupNotification, @Context KeyWrapperDto keyWrapper);

    @IterableMapping(elementTargetType = UserGroupNotificationAdminDto.class, qualifiedByName = "fromEncryptEntityToEncryptUserGroupNotificationAdminDto")
    List<UserGroupNotificationAdminDto> fromEncryptEntityListToEncryptUserGroupNotificationAdminDtoList(List<UserGroupNotification> userGroupNotifications, @Context KeyWrapperDto keyWrapper);
}