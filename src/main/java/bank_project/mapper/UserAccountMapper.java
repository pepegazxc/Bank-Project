package bank_project.mapper;

import bank_project.dto.cache.CachedUserAccountDto;
import bank_project.entity.UserAccount;

public class UserAccountMapper {
    public static CachedUserAccountDto toUserAccountCacheDto(UserAccount account){
        return new CachedUserAccountDto(
                account.getBalance(),
                account.getGoalTempId(),
                account.getCustomGoal(),
                account.getNumber()
        );
    }
}
