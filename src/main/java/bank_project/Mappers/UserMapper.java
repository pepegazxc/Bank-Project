package bank_project.Mappers;

import bank_project.DTO.CacheDto.AllUserCacheDto;
import bank_project.DTO.CacheDto.UserAccountCacheDto;
import bank_project.DTO.CacheDto.UserCacheDto;
import bank_project.DTO.CacheDto.UserCardCacheDto;
import bank_project.Entity.UserAccountEntity;
import bank_project.Entity.UserCardEntity;
import bank_project.Entity.UserEntity;

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
