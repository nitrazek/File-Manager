package com.example.FileManager.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class FolderDepthModel {
    private String folderName, subFolderName;
    private int numberOfFiles;
}
