package com.example.journalApp.controller;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.UserEntity;
import com.example.journalApp.service.JournalEntryService;
import com.example.journalApp.service.UserEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("journal")
public class JournalEntryController {

    @Autowired
    JournalEntryService journalEntryService;
    @Autowired
    UserEntryService userEntryService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllEnteriesOfAUser(){
        System.out.println("i am here");
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
//        System.out.println(userName);
        UserEntity user=userEntryService.findByUsername(userName);
        List<JournalEntry> journalEntries=user.getJournalEntryList();
        if(journalEntries!=null && !journalEntries.isEmpty())
            return new ResponseEntity<>(journalEntries,HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("id/{myID}")
    public ResponseEntity<JournalEntry> getJournalEntryByID(@PathVariable ObjectId myID){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        UserEntity user=userEntryService.findByUsername(userName);

        Optional<JournalEntry> optionalJournalEntry=journalEntryService.findByID(myID);

        if(optionalJournalEntry.isPresent()){
            JournalEntry journalEntry=optionalJournalEntry.get();
            List<JournalEntry> collect=user.getJournalEntryList().stream().filter(x->x.getId().equals(myID)).collect(Collectors.toList());
            if(!collect.isEmpty())
                return new ResponseEntity<JournalEntry>(optionalJournalEntry.get(), HttpStatus.OK);
            else
                return new ResponseEntity<JournalEntry>(HttpStatus.FORBIDDEN);
        }
        else{
            return new ResponseEntity<JournalEntry>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        try {
            return new ResponseEntity<>(journalEntryService.saveEntry(journalEntry,userName),HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("id/{myID}")
    public ResponseEntity<JournalEntry> updateJournalEntryByID(@PathVariable ObjectId myID,@RequestBody JournalEntry updatedJournalEntry){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        UserEntity user=userEntryService.findByUsername(userName);
        List<JournalEntry> collect=user.getJournalEntryList().stream().filter(x->x.getId().equals(myID)).collect(Collectors.toList());
        if(collect.isEmpty())
            return new ResponseEntity<JournalEntry>(HttpStatus.NOT_FOUND);

        Optional<JournalEntry> old=journalEntryService.findByID(myID);
        if(old.isPresent()){
            JournalEntry oldEntry=old.get();
            oldEntry.setTitle(updatedJournalEntry.getTitle()!=null && updatedJournalEntry.getTitle()!=""?updatedJournalEntry.getTitle(): oldEntry.getTitle());
            oldEntry.setContent(updatedJournalEntry.getContent()!=null && updatedJournalEntry.getContent()!=""?updatedJournalEntry.getContent(): oldEntry.getContent());
            journalEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry,HttpStatus.NO_CONTENT);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("id/{myID}")
    public ResponseEntity<?> deleteJournalEntryByID(@PathVariable ObjectId myID){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        UserEntity user=userEntryService.findByUsername(userName);
        List<JournalEntry> collect=user.getJournalEntryList().stream().filter(x->x.getId().equals(myID)).collect(Collectors.toList());

        if(collect.isEmpty())
            return new ResponseEntity<JournalEntry>(HttpStatus.NOT_FOUND);

        journalEntryService.deleteByID(myID,userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
