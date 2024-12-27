package uz.java.calculatefeebot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.java.calculatefeebot.repository.UserProfessionRepo;

@Service
@RequiredArgsConstructor
public class UserProfessionService {

    private final UserProfessionRepo userProfessionRepo ;

}
