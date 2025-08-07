package bank_project.mapper;

import bank_project.dto.cache.UserAccountCacheDto;
import bank_project.entity.UserAccountEntity;

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
