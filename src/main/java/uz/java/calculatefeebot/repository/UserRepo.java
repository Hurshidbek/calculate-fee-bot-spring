package uz.java.calculatefeebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.calculatefeebot.model.entity.Users;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Long> {

    Optional<Users> findByPhoneNumber(String phoneNumber);

    Optional<Users> findByChatId(Long chatID);

}
