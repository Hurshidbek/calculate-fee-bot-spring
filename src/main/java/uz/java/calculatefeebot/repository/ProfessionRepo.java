package uz.java.calculatefeebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.calculatefeebot.model.entity.Profession;

public interface ProfessionRepo extends JpaRepository<Profession, Long> {
}
