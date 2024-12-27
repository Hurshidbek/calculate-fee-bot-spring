package uz.java.calculatefeebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.calculatefeebot.model.entity.UserProfession;

public interface UserProfessionRepo extends JpaRepository<UserProfession, Long> {


}
