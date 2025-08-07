package bank_project.mapper;

import bank_project.dto.cache.CachedAllUserDto;
import bank_project.dto.cache.CachedUserAccountDto;
import bank_project.dto.cache.CachedUserDto;
import bank_project.dto.cache.UserCardCacheDto;
import bank_project.entity.UserAccountEntity;
import bank_project.entity.UserCardEntity;
import bank_project.entity.User;

public class UserMapper {
    public static CachedUserDto toCacheDto(User user) {
        return new CachedUserDto(
                user.getName(),
                user.getSurname(),
                user.getPatronymic(),
                user.getUsername(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getPassport(),
                user.getToken(),
                user.getPostalCode()
        );
    }
    public static CachedAllUserDto toAllCacheDto(User user,
                                                 UserCardEntity userCard,
                                                 UserAccountEntity userAccount
    ) {
        CachedUserDto userDto = toCacheDto(user);
        UserCardCacheDto userCardCacheDto =  null;
        if (userCard != null) {
            userCardCacheDto = UserCardMapper.toUserCardCacheDto(userCard);
        }
        CachedUserAccountDto cachedUserAccountDto =  null;
        if (userAccount != null) {
            cachedUserAccountDto = UserAccountMapper.toUserAccountCacheDto(userAccount);
        }
        return new CachedAllUserDto(userDto, userCardCacheDto, cachedUserAccountDto);
    }
}
