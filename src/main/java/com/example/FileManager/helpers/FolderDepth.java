package com.example.FileManager.helpers;

import com.example.FileManager.models.entities.Folder;

import java.util.ArrayList;
import java.util.List;

public class FolderDepth {
    private Folder deepestFolder;
    private Long depth;
    public FolderDepth(List<Folder> parentFolderList) {
        depth = -1L;
        for(Folder folder: parentFolderList) {
            CalculateDepth(folder, 0L);
        }
    }

    public List<FolderDepthModel> getDeepestPath() {
        List<FolderDepthModel> deepestPath = new ArrayList<>();
        addFolderToList(deepestPath, deepestFolder, null);
        return deepestPath;
    }

    private void CalculateDepth(Folder folder, Long currentDepth) {
        if(folder.getSubfolders() != null)
            for(Folder subfolder: folder.getSubfolders())
                CalculateDepth(subfolder, currentDepth + 1);

        if(depth < currentDepth) {
            depth = currentDepth;
            deepestFolder = folder;
        }
    }

    private void addFolderToList(List<FolderDepthModel> depthList, Folder folder, String subFolderName) {
        if(folder.getParentFolder() != null)
            addFolderToList(depthList, folder.getParentFolder(), folder.getName());

        int filesCount = 0;
        if(folder.getFiles() != null)
            filesCount = folder.getFiles().size();

        depthList.add(new FolderDepthModel(folder.getName(), subFolderName, filesCount));
    }
}
