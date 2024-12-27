package uz.java.calculatefeebot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.java.calculatefeebot.repository.AdminRepo;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepo adminRepo ;
}
