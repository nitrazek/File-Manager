package com.example.FileManager.controllers;

import com.example.FileManager.helpers.FolderDepth;
import com.example.FileManager.helpers.FolderDepthModel;
import com.example.FileManager.helpers.FolderStat;
import com.example.FileManager.helpers.FolderStatModel;
import com.example.FileManager.models.entities.Folder;
import com.example.FileManager.repositories.FileRepository;
import com.example.FileManager.repositories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FolderRepository folderRepository;

    @GetMapping("/files")
    public ResponseEntity<Map<String, Object>> filesReport() {
        AtomicLong avg = new AtomicLong(0L);
        AtomicLong max = new AtomicLong(0L);
        AtomicLong min = new AtomicLong(Long.MAX_VALUE);
        AtomicLong count = new AtomicLong(0L);
        fileRepository.findAll().forEach(file -> {
            Long fileSize = file.getSizeInBytes();
            count.incrementAndGet();
            max.updateAndGet(currentMax -> Math.max(currentMax, fileSize));
            min.updateAndGet(currentMin -> Math.min(currentMin, fileSize));
            avg.updateAndGet(currentAvg -> currentAvg + (fileSize - currentAvg)/count.get());
        });
        
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("Number of files", count.get());
        resultMap.put("Lowest file size", min.get());
        resultMap.put("Highest file size", max.get());
        resultMap.put("Average file size", avg.get());

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @GetMapping("/folders/{amount}")
    public Map<String, Object> foldersReport(@PathVariable int amount) {
        List<Folder> folderList = folderRepository.findByParentFolderNull();

        FolderStat folderStat = new FolderStat(folderList, amount);
        Map<String, Object> result = new HashMap<>();
        result.put("Top " + amount + " by file size", folderStat.getBySize());
        result.put("Top " + amount + " by file count", folderStat.getByCount());
        result.put("Top " + amount + " by average file size", folderStat.getByAverageSize());
        return result;
    }

    @GetMapping("/hierarchy")
    public List<FolderDepthModel> hierarchyReport() {
        List<Folder> folderList = folderRepository.findByParentFolderNull();

        FolderDepth folderDepth = new FolderDepth(folderList);
        return folderDepth.getDeepestPath();
    }
}
