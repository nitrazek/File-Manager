package com.example.FileManager.helpers;

import com.example.FileManager.models.entities.Folder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class FolderCycle {
    private final Folder mainFolder;

    @Getter
    private boolean isCycleDetected;

    @Getter
    private List<String> cycleList;
    public FolderCycle(Folder folder) {
        mainFolder = folder;
        cycleList = new ArrayList<>();
        isCycleDetected = detectCycle(folder.getParentFolder());
    }

    private boolean detectCycle(Folder folder) {
        if(folder == null)
            return false;

        if(folder.equals(mainFolder) || detectCycle(folder.getParentFolder())) {
            cycleList.add(folder.getName());
            return true;
        }

        return false;
    }
}
