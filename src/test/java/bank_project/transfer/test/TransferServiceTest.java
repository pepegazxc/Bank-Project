package bank_project.transfer.test;

import bank_project.dto.request.request.transfer.BetweenAccountsCashRequest;
import bank_project.entity.User;
import bank_project.entity.UserAccount;
import bank_project.entity.UserCard;
import bank_project.repository.jpa.UserAccountRepository;
import bank_project.repository.jpa.UserCardRepository;
import bank_project.repository.jpa.UserRepository;
import bank_project.service.OperationHistoryService;
import bank_project.service.RedisService;
import bank_project.service.SessionTokenService;
import bank_project.service.TransferService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock  private UserRepository userRepository;
    @Mock private  UserCardRepository userCardRepository;
    @Mock private UserAccountRepository userAccountRepository;
    @Mock private  SessionTokenService sessionTokenService;
    @Mock private  RedisService redisService;
    @Mock private EntityManager entityManager;
    @Mock private OperationHistoryService operation;

    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(transferService, "entityManager", entityManager);
    }

    @Test
    public void testBetweenAccountAndCard_successfulTransfer() {
        String username = "test";
        BetweenAccountsCashRequest request = new BetweenAccountsCashRequest(
                BigDecimal.valueOf(100),
                null
        );

        User fakeUser = new User();
        fakeUser.setId(1L);
        UserCard fakeCard = new UserCard();
        UserAccount fakeAccount = new UserAccount();

        fakeCard.setBalance(BigDecimal.ZERO);
        fakeAccount.setBalance(BigDecimal.valueOf(200));

        when(userAccountRepository.findByUserId(1L)).thenReturn(Optional.of(fakeAccount));
        when(userCardRepository.findByUserId(1L)).thenReturn(Optional.of(fakeCard));
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(fakeUser));
        doNothing().when(operation).saveUserOperation(any(), any(), any());
        doNothing().when(sessionTokenService).checkToken(username);

        transferService.betweenAccountAndCard(request, username);

        assert (fakeAccount.getBalance().compareTo(BigDecimal.valueOf(100)) == 0);
        assert (fakeCard.getBalance().compareTo(BigDecimal.valueOf(100)) == 0);

        verify(redisService, times(1)).deleteUserCache(username);
        verify(sessionTokenService, times(1)).checkToken(username);
        verify(redisService, times(1)).addUserCache(username);
        verify(redisService, times(1)).addUserHistory(username);
    }
}
