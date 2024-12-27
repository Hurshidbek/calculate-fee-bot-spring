package uz.java.calculatefeebot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.java.calculatefeebot.model.entity.Users;
import uz.java.calculatefeebot.repository.UserRepo;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo ;

    public boolean getAll(){
        userRepo.findAll();
        return true;
    }

    public boolean getById(Long id){
        userRepo.findById(id);
        return true;
    }


    public Users getByChatId(Long id){
        return userRepo.findByChatId(id).get();
    }

    public boolean getByPhone(String phone){
        userRepo.findByPhoneNumber(phone);
        return true;
    }

    public boolean add(Users user){
        userRepo.save(user);
        return true;
    }

    public boolean update(Users user){
        userRepo.save(user);
        return true;
    }

    public boolean delete(Users user){
        userRepo.delete(user);
        return true;
    }

    public boolean userExists(Long chatId){
        return  userRepo.findByChatId(chatId).isPresent() ;
    }

}
