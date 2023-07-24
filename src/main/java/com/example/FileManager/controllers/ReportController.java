package com.example.FileManager.controllers;

import com.example.FileManager.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private FileRepository fileRepository;

    @GetMapping("/1")
    public ResponseEntity<Map<String, Object>> report1() {
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

    @GetMapping("/2")
    public void report2() {

    }

    @GetMapping("/3")
    public void report3() {

    }
}
