package com.example.FileManager.controllers;

import com.example.FileManager.helpers.FolderCycle;
import com.example.FileManager.models.dtos.FolderDto;
import com.example.FileManager.models.entities.Folder;
import com.example.FileManager.repositories.FolderRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/folder")
public class FolderController {

    @Autowired
    private FolderRepository folderRepository;

    @GetMapping("/getAllFolders")
    public ResponseEntity<List<FolderDto>> getAllFolders() {
        List<FolderDto> folderList = folderRepository.findAll().stream().map(FolderDto::fromEntity).toList();

        if(folderList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(folderList, HttpStatus.OK);
    }

    @GetMapping("/getFolderByName/{name}")
    public ResponseEntity<FolderDto> getFolderByName(@PathVariable String name) {
        Optional<Folder> folderData = folderRepository.findByName(name);

        if(folderData.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(FolderDto.fromEntity(folderData.get()), HttpStatus.OK);
    }

    @PostMapping("/addFolder")
    public ResponseEntity<FolderDto> addFolder(@RequestBody @Valid FolderDto folderDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Folder> existingFolder = folderRepository.findByName(folderDto.getName());

        if(existingFolder.isPresent())
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Folder folder = Folder.fromDto(folderDto);

        if(folderDto.getParentFolderName() != null) {
            Optional<Folder> folderData = folderRepository.findByName(folderDto.getParentFolderName());

            if(folderData.isEmpty())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            folder.setParentFolder(folderData.get());
        }

        Folder folderData = folderRepository.save(folder);
        return new ResponseEntity<>(FolderDto.fromEntity(folderData), HttpStatus.OK);
    }

    @PostMapping("/updateFolderByName/{name}")
    public ResponseEntity<FolderDto> updateFolderByName(@PathVariable String name, @RequestBody @Valid FolderDto newFolderDtoData, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Folder> oldFolderData = folderRepository.findByName(name);

        if(oldFolderData.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Folder updatedFolderData = oldFolderData.get();

        if(!updatedFolderData.getName().equals(newFolderDtoData.getName())) {
            Optional<Folder> conflictFolderData = folderRepository.findByName(newFolderDtoData.getName());

            if(conflictFolderData.isPresent())
                return new ResponseEntity<>(HttpStatus.CONFLICT);

            updatedFolderData.setName(newFolderDtoData.getName());
        }


        if(newFolderDtoData.getParentFolderName() != null) {
            Optional<Folder> folderData = folderRepository.findByName(newFolderDtoData.getParentFolderName());

            if(folderData.isEmpty())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            updatedFolderData.setParentFolder(folderData.get());

            FolderCycle folderCycle = new FolderCycle(updatedFolderData);
            if(folderCycle.isCycleDetected())
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Folder folderData = folderRepository.save(updatedFolderData);
        return new ResponseEntity<>(FolderDto.fromEntity(folderData), HttpStatus.OK);
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
