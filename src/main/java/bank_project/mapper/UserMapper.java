package bank_project.mapper;

import bank_project.dto.cache.CachedAllUserDto;
import bank_project.dto.cache.CachedUserAccountDto;
import bank_project.dto.cache.CachedUserDto;
import bank_project.dto.cache.CachedUserCardDto;
import bank_project.entity.UserAccount;
import bank_project.entity.UserCard;
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
                                                 UserCard userCard,
                                                 UserAccount userAccount
    ) {
        CachedUserDto userDto = toCacheDto(user);
        CachedUserCardDto cachedUserCardDto =  null;
        if (userCard != null) {
            cachedUserCardDto = UserCardMapper.toUserCardCacheDto(userCard);
        }
        CachedUserAccountDto cachedUserAccountDto =  null;
        if (userAccount != null) {
            cachedUserAccountDto = UserAccountMapper.toUserAccountCacheDto(userAccount);
        }
        return new CachedAllUserDto(userDto, cachedUserCardDto, cachedUserAccountDto);
    }
}
