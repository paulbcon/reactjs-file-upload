package com.example.fileuploaddemo.service;

import java.util.List;
import com.example.fileuploaddemo.entity.FileDB;
public interface FileService {
    List<FileDB> getAllFiles();
    void saveAllFilesList(List<FileDB> fileList);
}
