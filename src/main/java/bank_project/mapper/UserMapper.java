package bank_project.mapper;

import bank_project.dto.cache.AllUserCacheDto;
import bank_project.dto.cache.UserAccountCacheDto;
import bank_project.dto.cache.UserCacheDto;
import bank_project.dto.cache.UserCardCacheDto;
import bank_project.entity.UserAccountEntity;
import bank_project.entity.UserCardEntity;
import bank_project.entity.UserEntity;

public class UserMapper {
    public static UserCacheDto toCacheDto(UserEntity user) {
        return new UserCacheDto(
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
    public static AllUserCacheDto toAllCacheDto(UserEntity user,
                                                UserCardEntity userCard,
                                                UserAccountEntity userAccount
    ) {
        UserCacheDto userDto = toCacheDto(user);
        UserCardCacheDto userCardCacheDto =  null;
        if (userCard != null) {
            userCardCacheDto = UserCardMapper.toUserCardCacheDto(userCard);
        }
        UserAccountCacheDto userAccountCacheDto =  null;
        if (userAccount != null) {
            userAccountCacheDto = UserAccountMapper.toUserAccountCacheDto(userAccount);
        }
        return new AllUserCacheDto(userDto, userCardCacheDto, userAccountCacheDto);
    }
}
