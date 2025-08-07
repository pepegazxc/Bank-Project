package bank_project.mapper;

import bank_project.dto.cache.CachedUserOperationHistoryDto;
import bank_project.entity.UserOperationHistory;

public class UserHistoryMapper {
    public static CachedUserOperationHistoryDto toHistoryDto(UserOperationHistory user) {
        return new CachedUserOperationHistoryDto(
                user.getContactId(),
                user.getOperationType(),
                user.getAmount(),
                user.getTime()
        );
    }
}
