package com.example.FileManager.helpers;

import com.example.FileManager.models.entities.File;
import com.example.FileManager.models.entities.Folder;
import lombok.Getter;

import java.util.*;

@Getter
public class FolderStat {
    private final Comparator<FolderStatModel> folderStatModelComparator = (o1, o2) -> {
        int resultComparison = Long.compare(o2.getResult(), o1.getResult());
        if (resultComparison != 0) {
            return resultComparison;
        }
        return o1.getName().compareTo(o2.getName());
    };
    private TreeSet<FolderStatModel> bySize;
    private TreeSet<FolderStatModel> byCount;
    private TreeSet<FolderStatModel> byAverageSize;

    public FolderStat(List<Folder> parentFolderList, int amount) {
        bySize = new TreeSet<>(folderStatModelComparator);
        byCount = new TreeSet<>(folderStatModelComparator);
        byAverageSize = new TreeSet<>(folderStatModelComparator);
        for(Folder parentFolder: parentFolderList)
            CalculateStats(parentFolder);

        trimSet(bySize, amount);
        trimSet(byCount, amount);
        trimSet(byAverageSize, amount);
    }

    private HashMap<String, Long> CalculateStats(Folder folder) {
        Long size = 0L, count = 0L, avg = 0L;

        List<File> fileList = folder.getFiles();
        if(fileList != null)
            for(File file: fileList) {
                size += file.getSizeInBytes();
                count++;
            }

        List<Folder> subfolderList = folder.getSubfolders();
        if(subfolderList != null)
            for(Folder subfolder: subfolderList) {
                HashMap<String, Long> subfolderResults = CalculateStats(subfolder);
                size += subfolderResults.get("size");
                count += subfolderResults.get("count");
            }

        if(count > 0)
            avg = size / count;

        bySize.add(new FolderStatModel(folder.getName(), size));
        byCount.add(new FolderStatModel(folder.getName(), count));
        byAverageSize.add(new FolderStatModel(folder.getName(), avg));

        HashMap<String, Long> results = new HashMap<>();
        results.put("size", size);
        results.put("count", count);
        return results;
    }

    private void trimSet(TreeSet<FolderStatModel> set, int amount) {
        while (set.size() > amount) {
            set.pollLast();
        }
    }
}
