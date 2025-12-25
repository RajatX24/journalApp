package com.example.journalApp.service;

import com.example.journalApp.repository.UserEntryRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserEntryServiceTest {
    @Autowired
    UserEntryRepository userEntryRepository;

    @Disabled
    @Test
    public void testAdd(){
        assertEquals(4,21+2);
    }

    @Disabled
    @Test
    public void testFindByUsername(){
        assertNotNull(userEntryRepository.findByuserName("shyam"));
    }
}
