package com.example.FileManager.models;

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

    private String folder;
}
