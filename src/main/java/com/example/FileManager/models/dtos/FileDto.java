package com.example.FileManager.models.dtos;

import com.example.FileManager.models.entities.File;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class FileDto {
    @NotBlank(message = "Nazwa pliku nie może być pusta")
    private String name;

    private String folderName;

    @Positive(message = "Rozmiar pliku musi być większy od zera.")
    private Long sizeInBytes;

    public static FileDto fromEntity(File file) {
        FileDto fileDto = new FileDto();

        fileDto.setName(file.getName());
        fileDto.setSizeInBytes(file.getSizeInBytes());
        if(file.getFolder() != null)
            fileDto.setFolderName(file.getFolder().getName());

        return fileDto;
    }
}
