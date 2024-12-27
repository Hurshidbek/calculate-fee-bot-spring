package uz.java.calculatefeebot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.java.calculatefeebot.constants.enums.Lang;
import uz.java.calculatefeebot.model.enums.EducationLevel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String tgUsername;

    private String name;

    private int age;

    private boolean genderIsMan;

    private String phoneNumber;

    private boolean isBlocked;

    private EducationLevel educationLevel;

    private Lang lang;

    private String step;

    private Long chatId;


}
