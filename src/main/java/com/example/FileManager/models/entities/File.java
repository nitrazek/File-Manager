package com.example.FileManager.models.entities;

import com.example.FileManager.models.dtos.FileDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Files")
@Data
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private Long sizeInBytes;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    public static File fromDto(FileDto fileDto) {
        File file = new File();

        file.setName(fileDto.getName());
        file.setSizeInBytes(fileDto.getSizeInBytes());

        return file;
    }
}
