package bank_project.mapper;

import bank_project.dto.cache.CachedUserCardDto;
import bank_project.entity.UserCard;

public class UserCardMapper {
    public static CachedUserCardDto toUserCardCacheDto(UserCard card){
        return new CachedUserCardDto(
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
