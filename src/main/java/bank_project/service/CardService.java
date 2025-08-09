package bank_project.service;

import bank_project.dto.request.CardRequest;
import bank_project.dto.view.ViewCardDto;
import bank_project.entity.Cards;
import bank_project.entity.UserCard;
import bank_project.entity.User;
import bank_project.repository.jpa.CardRepository;
import bank_project.repository.jpa.UserCardRepository;
import bank_project.repository.jpa.UserRepository;
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
        List<Cards> cards = cardRepository.findAll();
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
    public UserCard openNewCard(String username, CardRequest cardRequest){
        sessionTokenService.checkToken(username);
        redisService.deleteUserCache(username);

        User savedUser = findUser(username);

        Long userId = savedUser.getId();

        UserCard savedUserCard = findUserCard(userId);
        Cards savedCards = findCards(cardRequest);

        String cardNumber = generateUniqueCardNumber();
        String cardCvv = generateCardCvv();
        String cardExpirationDate = generateCardExpirationDate();
        fillNewCardFields(savedUserCard, savedCards, cardNumber, cardExpirationDate, cardCvv);


        entityManager.flush();
        log.info("User {} has opened new card", username);

        redisService.addUserCache(username);
        return savedUserCard;
    }

    @Transactional
    public UserCard deleteCard(String username){
        sessionTokenService.checkToken(username);
        redisService.deleteUserCache(username);

        User savedUser = findUser(username);
        Long userId = savedUser.getId();
        UserCard savedCard = findUserCard(userId);

        setFieldsToExistingCard(savedCard);

        entityManager.flush();
        log.info("User {} has deleted card", username);

        redisService.addUserCache(username);

        return savedCard;
    }

    private User findUser(String username){
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    private UserCard findUserCard(Long userId){
        return userCardRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Cards findCards(CardRequest cardRequest){
        return cardRepository.findCardIdByCardName(cardRequest.getCardType())
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }

    private String generateCardNumber(){
        Random rand = new Random();
        String cardNumber = "42" + String.format("%014d", Math.abs(rand.nextLong()) % 1_000_000_000_000_00L );
        return cardNumber;
    }

    private String generateCardExpirationDate(){
        Random rand = new Random();

        int month = rand.nextInt(12) + 1;
        int year = LocalDate.now().getYear() + rand.nextInt(5) + 6;
        int day = rand.nextInt(28) + 1;
        String cardExpirationDate = String.format("%02d/%02d/%04d", day, month, year);

        return cardExpirationDate;
    }

    private String generateCardCvv(){
        Random rand = new Random();
        String cardThreeNumber = String.format("%03d", Math.abs(rand.nextInt()) % 1000);
        return cardThreeNumber;
    }

    private Boolean checkingTheUniquenessOfCardNumber(String cardNumber) {
        return userCardRepository.findByCipherNumber(cipher.encrypt(cardNumber)).isEmpty();
    }

    private Boolean checkingTheUniquenessOfCardExpirationDate(String cardExpirationDate) {
        return userCardRepository.findByCipherExpirationDate(cipher.encrypt(cardExpirationDate)).isEmpty();
    }

    private Boolean checkingTheUniquenessOfCardCvv(String cardCvv) {
        return userCardRepository.findByCipherThreeNumbers(cipher.encrypt(cardCvv)).isEmpty();
    }

    private String generateUniqueCardNumber(){
        String cardNumber;
        do{
            cardNumber = generateCardNumber();
        }while (!checkingTheUniquenessOfCardNumber(cardNumber));
        return cardNumber;
    }

    private String generateUniqueCardExpirationDate(){
        String cardExpirationDate;
        do{
            cardExpirationDate = generateCardExpirationDate();
        }while (!checkingTheUniquenessOfCardExpirationDate(cardExpirationDate));
        return cardExpirationDate;
    }
    private String generateUniqueCardCvv(){
        String cardCvv;
        do{
            cardCvv = generateCardCvv();
        }while(!checkingTheUniquenessOfCardCvv(cardCvv));
        return cardCvv;
    }
    private UserCard fillNewCardFields(UserCard savedUserCard, Cards savedCards, String cardNumber, String cardExpirationDate, String cardCvv){
        if (savedUserCard.getCardId() == null) {
            savedUserCard.setCardId(savedCards);
        }
        if (savedUserCard.getCipherNumber() == null) {
            savedUserCard.setCipherNumber(cipher.encrypt(cardNumber));
        }
        if(savedUserCard.getCipherThreeNumbers() == null) {
            savedUserCard.setCipherThreeNumbers(cipher.encrypt(cardCvv));
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
        return savedUserCard;
    }
    private UserCard setFieldsToExistingCard(UserCard savedCard){
        savedCard.setCipherThreeNumbers(null);
        savedCard.setCipherExpirationDate(null);
        savedCard.setCardId(null);
        savedCard.setCashback(null);
        savedCard.setCipherNumber(null);
        savedCard.setBalance(null);
        savedCard.setIsActive(null);
        return savedCard;
    }
}
