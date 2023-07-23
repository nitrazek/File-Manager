package com.example.FileManager.models.dtos;

import com.example.FileManager.models.entities.File;
import com.example.FileManager.models.entities.Folder;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class FolderDto {
    @NotBlank(message = "Nazwa folderu nie może być pusta")
    private String name;

    private List<String> fileNames;

    private String parentFolderName;

    public static FolderDto fromEntity(Folder folder) {
        FolderDto folderDto = new FolderDto();

        folderDto.setName(folder.getName());
        if (folder.getFiles() != null)
            folderDto.fileNames = folder.getFiles().stream().map(File::getName).toList();
        if(folder.getParentFolder() != null)
            folderDto.parentFolderName = folder.getParentFolder().getName();

        return folderDto;
    }
}
