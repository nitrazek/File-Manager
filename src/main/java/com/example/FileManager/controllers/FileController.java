package com.example.FileManager.controllers;

import com.example.FileManager.models.File;
import com.example.FileManager.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @GetMapping("/getAllFiles")
    public ResponseEntity<List<File>> getAllFiles() {
        List<File> fileList = fileRepository.findAll();

        if(fileList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }

    @GetMapping("/getFileByName/{name}")
    public ResponseEntity<File> getFileById(@PathVariable String name) {
        Optional<File> fileData = fileRepository.findByName(name);

        if(fileData.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(fileData.get(), HttpStatus.OK);
    }

    @PostMapping("/addFile")
    public ResponseEntity<File> addFile(@RequestBody File file) {
        Optional<File> existingFile = fileRepository.findByName(file.getName());

        if(existingFile.isPresent())
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        File fileData = fileRepository.save(file);
        return new ResponseEntity<>(fileData, HttpStatus.OK);
    }

    @PostMapping("/updateFileByName/{name}")
    public ResponseEntity<File> updateFileByName(@PathVariable String name, @RequestBody File newFileData) {
        Optional<File> oldFileData = fileRepository.findByName(name);

        if(oldFileData.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        File updatedFileData = oldFileData.get();
        updatedFileData.setName(newFileData.getName());
        updatedFileData.setSizeInBytes(newFileData.getSizeInBytes());
        updatedFileData.setFolder(newFileData.getFolder());

        File fileData = fileRepository.save(updatedFileData);
        return new ResponseEntity<>(fileData, HttpStatus.OK);
    }

    @DeleteMapping("/deleteFileByName/{name}")
    public ResponseEntity<HttpStatus> deleteFileByName(@PathVariable String name) {
        Optional<File> fileData = fileRepository.findByName(name);

        if(fileData.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        fileRepository.deleteById(fileData.get().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
