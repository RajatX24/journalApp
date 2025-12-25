package com.example.journalApp.service;

import com.example.journalApp.entity.UserEntity;
import com.example.journalApp.repository.UserEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserEntryService {
    @Autowired
    UserEntryRepository userEntryRepository;
    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();


    public UserEntity saveEntryWithPasswordHashing(UserEntity userEntity){
        try{
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setRoles(Arrays.asList("USER"));
            return userEntryRepository.save(userEntity);
        }
        catch (Exception e){
            log.error("Could not save user to DB");
            log.error(String.valueOf(e));
            return null;
        }
    }

    public UserEntity saveAdmin(UserEntity userEntity){
        UserEntity userEntity1=userEntryRepository.findByuserName(userEntity.getUserName());
        userEntity1.setRoles(Arrays.asList("USER","ADMIN"));
        return userEntryRepository.save(userEntity1);
    }

    public UserEntity saveEntry(UserEntity userEntity){
        userEntity.setRoles(Arrays.asList("USER"));
        return userEntryRepository.save(userEntity);
    }

    public List<UserEntity> getAll(){
        return userEntryRepository.findAll();
    }

    public Optional<UserEntity> findByID(ObjectId id){
        return userEntryRepository.findById(id);
    }

    public void deleteByID(ObjectId id){
        userEntryRepository.deleteById(id);
    }

    public void deleteByUsername(String username){
        userEntryRepository.deleteByuserName(username);
    }

    public UserEntity findByUsername(String userName){
        return userEntryRepository.findByuserName(userName);
    }
}
