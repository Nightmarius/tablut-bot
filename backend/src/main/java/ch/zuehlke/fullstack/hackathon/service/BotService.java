package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.fullstack.hackathon.database.BotData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BotService {
    private JpaRepository<BotData, Long> botRepo;

    public Optional<BotData> getBotById(Long id){
        return botRepo.findById(id);
    }
    public List<BotData> getBots(){
        return botRepo.findAll();
    }
}
