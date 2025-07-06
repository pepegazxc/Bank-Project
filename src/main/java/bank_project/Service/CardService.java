package bank_project.Service;

import bank_project.DTO.ViewDto.ViewCardDto;
import bank_project.Entity.CardsEntity;
import bank_project.Repository.JpaRepository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
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
}
