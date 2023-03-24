package com.example.fileuploaddemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fileuploaddemo.entity.FileDB;
import com.example.fileuploaddemo.repository.FileDBRepository;
import com.example.fileuploaddemo.util.ImageUtil;

@Service
public class FileServiceImplementation implements FileService{

    @Autowired 
    FileDBRepository fileRepository;

    @Override
    public List<FileDB> getAllFiles() {
        
          // fetch all the files form database
          return fileRepository.findAll();
    }

    @Override
    public void saveAllFilesList(List<FileDB> fileList) {
        // Save all the files into database
        for (FileDB fileModal : fileList)
           // fileRepository.save(fileModal);
           fileRepository.save(FileDB.builder()
           .fileName(fileModal.getFileName())
           .fileType(fileModal.getFileType())
           .content(ImageUtil.compressImage(fileModal.getContent())).build());
           
    }

    
}
