package bank_project.Service;

import bank_project.DTO.RequestDto.CardRequest;
import bank_project.DTO.ViewDto.ViewCardDto;
import bank_project.Entity.CardsEntity;
import bank_project.Entity.UserCardEntity;
import bank_project.Entity.UserEntity;
import bank_project.Repository.JpaRepository.CardRepository;
import bank_project.Repository.JpaRepository.UserCardRepository;
import bank_project.Repository.JpaRepository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CardService {
    private final CardRepository cardRepository;
    private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;
    private final CipherService cipher;
    private final RedisService redisService;
    private final SessionTokenService sessionTokenService;

    @Autowired
    private EntityManager entityManager;

    public CardService(CardRepository cardRepository, UserCardRepository userCardRepository, UserRepository userRepository, CipherService cipher, RedisService redisService, SessionTokenService sessionTokenService) {
        this.cardRepository = cardRepository;
        this.userCardRepository = userCardRepository;
        this.userRepository = userRepository;
        this.cipher = cipher;
        this.redisService = redisService;
        this.sessionTokenService = sessionTokenService;
    }

    public List<ViewCardDto> getAllCards(){
        List<CardsEntity> cards = cardRepository.findAll();
        return cards.stream()
                .map(card -> new ViewCardDto(
                        card.getCardName(),
                        card.getLawPerMonth(),
                        card.getCashBackPercent(),
                        card.getCardInfo()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserCardEntity openNewCard(String username, CardRequest cardRequest){
        sessionTokenService.checkToken(username);

        Random rand = new Random();

        redisService.deleteUserCache(username);

        UserEntity savedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = savedUser.getId();

        UserCardEntity savedUserCard = userCardRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CardsEntity savedCards = cardRepository.findCardIdByCardName(cardRequest.getCardType())
                .orElseThrow(() -> new RuntimeException("Card not found"));

        while (true) {
            String cardNumber = "42" + String.format("%014d", Math.abs(rand.nextLong()) % 1_000_000_000_000_00L );
            String cardThreeNumber = String.format("%03d", Math.abs(rand.nextInt()) % 1000);

            int month = rand.nextInt(12) + 1;
            int year = LocalDate.now().getYear() + rand.nextInt(5) + 6;
            int day = rand.nextInt(28) + 1;
            String cardExpirationDate = String.format("%02d/%02d/%04d", day, month, year);

            if (userCardRepository.findByCipherNumber(cipher.encrypt(cardNumber)).isEmpty() &&
                    userCardRepository.findByCipherThreeNumbers(cipher.encrypt(cardThreeNumber)).isEmpty() &&
            userCardRepository.findByCipherExpirationDate(cipher.encrypt(cardExpirationDate)).isEmpty()) {

                if (savedUserCard.getCardId() == null) {
                    savedUserCard.setCardId(savedCards);
                }
                if (savedUserCard.getCipherNumber() == null) {
                    savedUserCard.setCipherNumber(cipher.encrypt(cardNumber));
                }
                if(savedUserCard.getCipherThreeNumbers() == null) {
                    savedUserCard.setCipherThreeNumbers(cipher.encrypt(cardThreeNumber));
                }
                if (savedUserCard.getCipherExpirationDate() == null) {
                    savedUserCard.setCipherExpirationDate(cipher.encrypt(cardExpirationDate));
                }
                if (savedUserCard.getBalance() == null) {
                    savedUserCard.setBalance(BigDecimal.valueOf(0.0));
                }
                if (savedUserCard.getCashback() == null) {
                    savedUserCard.setCashback(BigDecimal.valueOf(0.0));
                }
                if (savedUserCard.getIsActive() == null) {
                    savedUserCard.setIsActive(true);
                }

                entityManager.flush();
                log.info("User {} has opened new card", username);

                redisService.addUserCache(username);

                return savedUserCard;
            }

        }
    }

    @Transactional
    public UserCardEntity deleteCard(String username){
        sessionTokenService.checkToken(username);

        redisService.deleteUserCache(username);

        UserEntity savedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = savedUser.getId();

        UserCardEntity savedCard = userCardRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        savedCard.setIsActive(false);

        if (!savedCard.getIsActive()) {
            savedCard.setCipherThreeNumbers(null);
            savedCard.setCipherExpirationDate(null);
            savedCard.setCardId(null);
            savedCard.setCashback(null);
            savedCard.setCipherNumber(null);
            savedCard.setCipherExpirationDate(null);
            savedCard.setBalance(null);
            savedCard.setIsActive(null);
        }

        entityManager.flush();
        log.info("User {} has deleted card", username);

        redisService.addUserCache(username);

        return savedCard;
    }
}
