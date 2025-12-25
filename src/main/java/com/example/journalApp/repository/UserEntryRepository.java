package com.example.journalApp.repository;

import com.example.journalApp.entity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserEntryRepository extends MongoRepository<UserEntity, ObjectId>{
    UserEntity findByuserName(String userName);
    void deleteByuserName(String userName);
}
