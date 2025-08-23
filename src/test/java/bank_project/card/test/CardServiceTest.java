package bank_project.card.test;

import bank_project.dto.request.CardRequest;
import bank_project.entity.Cards;
import bank_project.entity.User;
import bank_project.entity.UserCard;
import bank_project.exception.custom.CardsNotFoundException;
import bank_project.exception.custom.UserCardNotFoundException;
import bank_project.repository.jpa.CardRepository;
import bank_project.repository.jpa.UserCardRepository;
import bank_project.repository.jpa.UserRepository;
import bank_project.service.CardService;
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
public class CardServiceTest {

    @Mock private CardRepository cardRepository;
    @Mock private UserCardRepository userCardRepository;
    @Mock private UserRepository userRepository;
    @Mock private CipherService cipher;
    @Mock private RedisService redis;
    @Mock private SessionTokenService sessionTokenService;
    @Mock private EntityManager entityManager;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(cardService, "entityManager", entityManager);
    }

    @Test
    public void testOpenNewCard_successfulCompletion(){
        String username = "test";
        CardRequest request = new CardRequest(
                username,
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test"
        );

        User fakeUser = new User();
        fakeUser.setId(1L);
        UserCard fakeUserCard = new UserCard();
        Cards fakeCards = new Cards();
        fakeCards.setCardName("test");

        doNothing().when(sessionTokenService).checkToken(username);
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(fakeUser));
        when(userCardRepository.findByUserId(1L)).thenReturn(Optional.of(fakeUserCard));
        when(cardRepository.findCardIdByCardName("test")).thenReturn(Optional.of(fakeCards));

        UserCard result = cardService.openNewCard(username, request);

        assertNotNull(result);

        verify(sessionTokenService).checkToken(username);
        verify(redis).deleteUserCache(username);
        verify(redis).addUserCache(username);
    }

    @Test
    public void testOpenNewCard_failedCompletion_incorrectCardType(){
        String username = "test";
        CardRequest request = new CardRequest(
                username,
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test"
        );

        User fakeUser = new User();
        fakeUser.setId(1L);
        UserCard fakeUserCard = new UserCard();
        Cards fakeCards = new Cards();
        fakeCards.setCardName("test");

        doNothing().when(sessionTokenService).checkToken(username);
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(fakeUser));
        when(userCardRepository.findByUserId(1L)).thenReturn(Optional.of(fakeUserCard));
        when(cardRepository.findCardIdByCardName("test")).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                CardsNotFoundException.class,
                () -> cardService.openNewCard(username, request)
        );

        assertEquals("Card not found", exception.getMessage());

        verify(sessionTokenService).checkToken(username);
        verify(redis).deleteUserCache(username);
    }

    @Test
    public void testDeleteCard_successfulCompletion(){
        String username = "test";

        User fakeUser = new User();
        fakeUser.setId(1L);
        UserCard fakeUserCard = new UserCard();

        doNothing().when(sessionTokenService).checkToken(username);
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(fakeUser));
        when(userCardRepository.findByUserId(1L)).thenReturn(Optional.of(fakeUserCard));

        UserCard result = cardService.deleteCard(username);

        assertNull(result.getCardId());
        assertNull(result.getBalance());
        assertNull(result.getCashback());
        assertNull(result.getCipherNumber());
        assertNull(result.getIsActive());
        assertNull(result.getCipherExpirationDate());
        assertNull(result.getCipherThreeNumbers());

        verify(sessionTokenService).checkToken(username);
        verify(redis).deleteUserCache(username);
        verify(redis).addUserCache(username);
    }

    @Test
    public void testDeleteCard_failedCompletion_cardNotFound(){
        String username = "test";

        User fakeUser = new User();
        fakeUser.setId(1L);
        UserCard fakeUserCard = new UserCard();

        doNothing().when(sessionTokenService).checkToken(username);
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(fakeUser));
        when(userCardRepository.findByUserId(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                UserCardNotFoundException.class,
                () -> cardService.deleteCard(username)
        );

        assertEquals("Users card not found", exception.getMessage());

        verify(sessionTokenService).checkToken(username);
        verify(redis).deleteUserCache(username);
    }
}
