package com.example.FileManager.controllers;

import com.example.FileManager.models.File;
import com.example.FileManager.models.Folder;
import com.example.FileManager.repositories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/folder")
public class FolderController {

    @Autowired
    private FolderRepository folderRepository;

    @GetMapping("/getAllFolders")
    public ResponseEntity<List<Folder>> getAllFolders() {
        List<Folder> folderList = folderRepository.findAll();

        if(folderList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(folderList, HttpStatus.OK);
    }

    @GetMapping("/getFolderByName/{name}")
    public ResponseEntity<Folder> getFolderByName(@PathVariable String name) {
        Optional<Folder> folderData = folderRepository.findByName(name);

        if(folderData.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(folderData.get(), HttpStatus.OK);
    }

    @PostMapping("/addFolder")
    public ResponseEntity<Folder> addFolder(@RequestParam(value = "parentFolderName", required = false) String parentFolderName, @RequestBody Folder folder) {
        Optional<Folder> existingFolder = folderRepository.findByName(folder.getName());

        if(existingFolder.isPresent())
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        if(parentFolderName != null) {
            Optional<Folder> folderData = folderRepository.findByName(parentFolderName);

            if(folderData.isEmpty())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            folder.setParentFolder(folderData.get());
        }

        Folder folderData = folderRepository.save(folder);
        return new ResponseEntity<>(folderData, HttpStatus.OK);
    }

    @PostMapping("/updateFolderByName/{name}")
    public ResponseEntity<Folder> updateFolderByName(@PathVariable String name, @RequestParam(value = "parentFolderName", required = false) String parentFolderName, @RequestBody Folder newFolderData) {
        Optional<Folder> oldFolderData = folderRepository.findByName(name);

        if(oldFolderData.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Optional<Folder> conflictFolderData = folderRepository.findByName(newFolderData.getName());

        if(conflictFolderData.isPresent())
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Folder updatedFolderData = oldFolderData.get();
        updatedFolderData.setName(newFolderData.getName());

        if(parentFolderName != null) {
            Optional<Folder> folderData = folderRepository.findByName(parentFolderName);

            if(folderData.isEmpty())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            updatedFolderData.setParentFolder(folderData.get());
        }

        Folder folderData = folderRepository.save(updatedFolderData);
        return new ResponseEntity<>(folderData, HttpStatus.OK);
    }

    @DeleteMapping("/deleteFolderByName/{name}")
    public ResponseEntity<HttpStatus> deleteFolderByName(@PathVariable String name) {
        Optional<Folder> folderData = folderRepository.findByName(name);

        if(folderData.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        folderRepository.deleteById(folderData.get().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
