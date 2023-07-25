package com.example.FileManager.helpers;

import com.example.FileManager.models.entities.Folder;
import lombok.Getter;

public class FolderCycle {
    private Folder mainFolder;

    @Getter
    private boolean isCycleDetected;
    public FolderCycle(Folder folder) {
        mainFolder = folder;

        isCycleDetected = detectCycle(folder.getParentFolder());
    }

    private boolean detectCycle(Folder folder) {
        if(folder == null)
            return false;

        if(folder.equals(mainFolder))
            return true;

        return detectCycle(folder.getParentFolder());
    }
}
