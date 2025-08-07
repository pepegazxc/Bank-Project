package bank_project.mapper;

import bank_project.dto.cache.UserCardCacheDto;
import bank_project.entity.UserCardEntity;

public class UserCardMapper {
    public static UserCardCacheDto toUserCardCacheDto(UserCardEntity card){
        return new UserCardCacheDto(
                card.getCipherNumber(),
                card.getCipherThreeNumbers(),
                card.getCipherExpirationDate(),
                card.getBalance(),
                card.getCashback(),
                card.getCardId(),
                card.getIsActive()
        );
    }
}
