package bank_project.redis.test;

import bank_project.dto.cache.CachedAllUserDto;
import bank_project.entity.User;
import bank_project.entity.UserAccount;
import bank_project.entity.UserCard;
import bank_project.exception.custom.UserAccountNotFoundException;
import bank_project.mapper.UserMapper;
import bank_project.repository.jpa.UserAccountRepository;
import bank_project.repository.jpa.UserCardRepository;
import bank_project.repository.jpa.UserHistoryRepository;
import bank_project.repository.jpa.UserRepository;
import bank_project.repository.redis.CachedUserHistoryRepository;
import bank_project.repository.redis.UserInfoRepository;
import bank_project.service.CipherService;
import bank_project.service.RedisService;
import bank_project.service.SessionTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RedisServiceTest {
    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private UserRepository userRepository;
    @Mock private UserInfoRepository userInfoRepository;
    @Mock private CipherService cipherService;
    @Mock private UserCardRepository userCardRepository;
    @Mock private UserAccountRepository userAccountRepository;
    @Mock private UserHistoryRepository userHistoryRepository;
    @Mock private SessionTokenService sessionTokenService;
    @Mock private CachedUserHistoryRepository cachedUserHistoryRepository;
    @Mock private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private RedisService redisService;

    @Test
    public void testAddUSerCache_successfulCompletion(){
        String username = "test";

        User fakeUser = new User();
        fakeUser.setId(1L);
        UserCard fakeCard = new UserCard();
        UserAccount fakeAccount = new UserAccount();

        CachedAllUserDto data = UserMapper.toAllCacheDto(fakeUser, fakeCard, fakeAccount);

        when(userRepository.findByUserName(username)).thenReturn(Optional.of(fakeUser));
        when(userCardRepository.findByUserId(1L)).thenReturn(Optional.of(fakeCard));
        when(userAccountRepository.findByUserId(1L)).thenReturn(Optional.of(fakeAccount));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        redisService.addUserCache(username);

        verify(redisTemplate.opsForValue())
                .set("user:" + username, data);

    }

    @Test
    public void testAddUSerCache_failedCompletion_UserAccountNotFound(){
        String username = "test";

        User fakeUser = new User();
        fakeUser.setId(1L);
        UserCard fakeCard = new UserCard();
        UserAccount fakeAccount = new UserAccount();

        CachedAllUserDto data = UserMapper.toAllCacheDto(fakeUser, fakeCard, fakeAccount);

        when(userRepository.findByUserName(username)).thenReturn(Optional.of(fakeUser));
        when(userCardRepository.findByUserId(1L)).thenReturn(Optional.of(fakeCard));
        when(userAccountRepository.findByUserId(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                UserAccountNotFoundException.class,
                () ->redisService.addUserCache(username)
        );

        assertEquals("Account not found after save", exception.getMessage());
    }

    @Test
    public void testDeleteUSerCache_successfulCompletion(){
        String username = "test";

        redisService.deleteUserCache(username);

        verify(redisTemplate).delete("user:" + username);
    }

}
