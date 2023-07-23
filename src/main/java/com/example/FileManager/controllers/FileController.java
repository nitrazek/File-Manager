package com.example.FileManager.controllers;

import com.example.FileManager.models.dtos.FileDto;
import com.example.FileManager.models.entities.File;
import com.example.FileManager.models.entities.Folder;
import com.example.FileManager.repositories.FileRepository;
import com.example.FileManager.repositories.FolderRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FolderRepository folderRepository;

    @GetMapping("/getAllFiles")
    public ResponseEntity<List<FileDto>> getAllFiles() {
        List<FileDto> fileList = fileRepository.findAll().stream().map(FileDto::fromEntity).toList();

        if(fileList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }

    @GetMapping("/getFileByName/{name}")
    public ResponseEntity<FileDto> getFileByName(@PathVariable String name) {
        Optional<File> fileData = fileRepository.findByName(name);

        if(fileData.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(FileDto.fromEntity(fileData.get()), HttpStatus.OK);
    }

    @PostMapping("/addFile")
    public ResponseEntity<FileDto> addFile(@RequestBody @Valid FileDto fileDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<File> existingFile = fileRepository.findByName(fileDto.getName());

        if(existingFile.isPresent())
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        File file = File.fromDto(fileDto);

        if(fileDto.getFolderName() != null) {
            Optional<Folder> folderData = folderRepository.findByName(fileDto.getFolderName());

            if(folderData.isEmpty())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            file.setFolder(folderData.get());
        }

        File fileData = fileRepository.save(file);
        return new ResponseEntity<>(FileDto.fromEntity(fileData), HttpStatus.OK);
    }

    @PostMapping("/updateFileByName/{name}")
    public ResponseEntity<FileDto> updateFileByName(@PathVariable String name, @RequestBody @Valid FileDto newFileDtoData, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<File> oldFileData = fileRepository.findByName(name);

        if(oldFileData.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Optional<File> conflictFileData = fileRepository.findByName(newFileDtoData.getName());

        if(conflictFileData.isPresent())
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        File updatedFileData = oldFileData.get();
        updatedFileData.setName(newFileDtoData.getName());
        updatedFileData.setSizeInBytes(newFileDtoData.getSizeInBytes());

        if(newFileDtoData.getFolderName() != null) {
            Optional<Folder> folderData = folderRepository.findByName(newFileDtoData.getFolderName());

            if(folderData.isEmpty())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            updatedFileData.setFolder(folderData.get());
        }

        File fileData = fileRepository.save(updatedFileData);
        return new ResponseEntity<>(FileDto.fromEntity(fileData), HttpStatus.OK);
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
