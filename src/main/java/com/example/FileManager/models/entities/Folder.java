package com.example.FileManager.models.entities;

import com.example.FileManager.models.dtos.FileDto;
import com.example.FileManager.models.dtos.FolderDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "folders")
@Data
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<File> files;

    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    private Folder parentFolder;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL)
    private List<Folder> subfolders;

    public static Folder fromDto(FolderDto folderDto) {
        Folder folder = new Folder();

        folder.setName(folderDto.getName());

        return folder;
    }
}
