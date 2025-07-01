package bank_project.Mappers;

import bank_project.DTO.CacheDto.UserCardCacheDto;
import bank_project.Entity.UserCardEntity;

public class UserCardMapper {
    public static UserCardCacheDto toUserCardCacheDto(UserCardEntity card){
        return new UserCardCacheDto(
                card.getCipherNumber(),
                card.getCipherThreeNumbers(),
                card.getCipherExpirationDate(),
                card.getBalance(),
                card.getCashback(),
                card.getIsActive()
        );
    }
}
