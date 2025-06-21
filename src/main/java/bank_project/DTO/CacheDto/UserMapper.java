package bank_project.DTO.CacheDto;

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
}
