package uz.java.calculatefeebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.calculatefeebot.model.entity.Admin;

public interface AdminRepo extends JpaRepository<Admin, Long> {
}
