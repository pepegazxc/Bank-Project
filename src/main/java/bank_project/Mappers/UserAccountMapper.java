package bank_project.Mappers;

import bank_project.DTO.CacheDto.UserAccountCacheDto;
import bank_project.Entity.UserAccountEntity;

public class UserAccountMapper {
    public static UserAccountCacheDto toUserAccountCacheDto(UserAccountEntity account){
        return new UserAccountCacheDto(
                account.getBalance(),
                account.getGoalTempId(),
                account.getCustomGoal(),
                account.getNumber()
        );
    }
}
