package bank_project.mapper;

import bank_project.dto.cache.UserOperationHistoryCacheDto;
import bank_project.entity.UserOperationHistoryEntity;

public class UserHistoryMapper {
    public static UserOperationHistoryCacheDto toHistoryDto(UserOperationHistoryEntity user) {
        return new UserOperationHistoryCacheDto(
                user.getContactId(),
                user.getOperationType(),
                user.getAmount(),
                user.getTime()
        );
    }
}
