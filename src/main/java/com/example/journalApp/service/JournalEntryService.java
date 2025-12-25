package com.example.journalApp.service;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.UserEntity;
import com.example.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    JournalEntryRepository journalEntryRepository;

    @Autowired
    UserEntryService userEntryService;


    @Transactional
    public JournalEntry saveEntry(JournalEntry journalEntry, String userName){
        journalEntry.setDate(LocalDate.now());
        JournalEntry savedJournalEntry=journalEntryRepository.save(journalEntry);
        UserEntity user=userEntryService.findByUsername(userName);
        user.getJournalEntryList().add(savedJournalEntry);
        userEntryService.saveEntry(user);
        return savedJournalEntry;
    }

    public JournalEntry saveEntry(JournalEntry journalEntry){
        journalEntry.setDate(LocalDate.now());
        JournalEntry savedJournalEntry=journalEntryRepository.save(journalEntry);
        return savedJournalEntry;
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findByID(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    public void deleteByID(ObjectId id, String userName){
        UserEntity user=userEntryService.findByUsername(userName);
        user.getJournalEntryList().removeIf(x->x.getId().equals(id));
        userEntryService.saveEntry(user);

        journalEntryRepository.deleteById(id);
    }
}
