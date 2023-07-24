package com.example.FileManager.repositories;

import com.example.FileManager.models.entities.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Optional<Folder> findByName(String name);
    List<Folder> findByParentFolderNull();
}
