package bank_project.Mappers;

import bank_project.DTO.CacheDto.UserOperationHistoryCacheDto;
import bank_project.Entity.UserOperationHistoryEntity;

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
