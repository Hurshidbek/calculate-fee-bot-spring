package uz.java.calculatefeebot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.java.calculatefeebot.repository.ProfessionRepo;

@Service
@RequiredArgsConstructor
public class ProfessionService {

    private final ProfessionRepo professionRepo ;

}
