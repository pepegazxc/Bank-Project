package bank_project.account.test;

import bank_project.dto.request.AccountRequest;
import bank_project.entity.Accounts;
import bank_project.entity.User;
import bank_project.entity.UserAccount;
import bank_project.exception.custom.AccountsNotFoundException;
import bank_project.exception.custom.UserAccountNotFoundException;
import bank_project.repository.jpa.AccountRepository;
import bank_project.repository.jpa.GoalTemplateRepository;
import bank_project.repository.jpa.UserAccountRepository;
import bank_project.repository.jpa.UserRepository;
import bank_project.service.AccountService;
import bank_project.service.CipherService;
import bank_project.service.RedisService;
import bank_project.service.SessionTokenService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock private  AccountRepository accountRepository;
    @Mock private  UserAccountRepository userAccountRepository;
    @Mock private  UserRepository userRepository;
    @Mock private  RedisService redisService;
    @Mock private  CipherService cipher;
    @Mock private  GoalTemplateRepository goalTemplateRepository;
    @Mock private  SessionTokenService sessionTokenService;
    @Mock private EntityManager entityManager;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp(){
        ReflectionTestUtils.setField(accountService, "entityManager", entityManager);
    }

    @Test
    public void testOpenNewAccount_successfulCompletion(){
        String username = "test";
        AccountRequest request = new AccountRequest(
          null,
          "test",
                "testGoal"
        );

        User fakeUser = new User();
        fakeUser.setId(1L);
        Accounts fakeAccounts = new Accounts();
        fakeAccounts.setAccount("test");
        UserAccount fakeUserAccount = new UserAccount();

        doNothing().when(sessionTokenService).checkToken(username);
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(fakeUser));
        when(accountRepository.findAccountIdByAccount("test")).thenReturn(Optional.of(fakeAccounts));
        when(userAccountRepository.findByUserId(1L)).thenReturn(Optional.of(fakeUserAccount));

        UserAccount result = accountService.openNewAccount(request, username);

        assertNotNull(result);

        verify(sessionTokenService).checkToken(username);
        verify(redisService).deleteUserCache(username);
        verify(redisService).addUserCache(username);
    }

    @Test
    public void testOpenNewAccount_failedCompletion_incorrectAccountType(){
        String username = "test";
        AccountRequest request = new AccountRequest(
                null,
                "test",
                "testGoal"
        );

        User fakeUser = new User();
        fakeUser.setId(1L);
        Accounts fakeAccounts = new Accounts();
        fakeAccounts.setAccount("test");
        UserAccount fakeUserAccount = new UserAccount();

        doNothing().when(sessionTokenService).checkToken(username);
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(fakeUser));
        when(accountRepository.findAccountIdByAccount("test")).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                AccountsNotFoundException.class,
                () -> accountService.openNewAccount(request, username)
        );

        assertEquals("Account not found", exception.getMessage());

        verify(sessionTokenService).checkToken(username);
        verify(redisService).deleteUserCache(username);
    }

    @Test
    public void testDeleteAccount_successfulCompletion(){
        String username = "test";

        User fakeUser = new User();
        fakeUser.setId(1L);
        UserAccount fakeUserAccount = new UserAccount();

        doNothing().when(sessionTokenService).checkToken(username);
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(fakeUser));
        when(userAccountRepository.findByUserId(1L)).thenReturn(Optional.of(fakeUserAccount));

        UserAccount result = accountService.deleteAccount(username);

        assertNull(result.getAccountId());
        assertNull(result.getUserId());
        assertNull(result.getBalance());
        assertNull(result.getNumber());
        assertNull(result.getCustomGoal());
        assertNull(result.getGoalTempId());

        verify(sessionTokenService).checkToken(username);
        verify(redisService).deleteUserCache(username);
    }

    @Test
    public void testDeleteAccount_failedCompletion_accountNotFound(){
        String username = "test";

        User fakeUser = new User();
        fakeUser.setId(1L);
        UserAccount fakeUserAccount = new UserAccount();

        doNothing().when(sessionTokenService).checkToken(username);
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(fakeUser));
        when(userAccountRepository.findByUserId(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                UserAccountNotFoundException.class,
                () -> accountService.deleteAccount(username)
        );

        assertEquals("Users account not found", exception.getMessage());

        verify(sessionTokenService).checkToken(username);
        verify(redisService).deleteUserCache(username);
    }
}
