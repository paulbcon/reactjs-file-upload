package com.example.fileuploaddemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.fileuploaddemo.entity.FileDB;
import com.example.fileuploaddemo.service.FileServiceImplementation;
import com.example.fileuploaddemo.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
// @CrossOrigin(origins = "http://localhost:8082") //open for specific port
@CrossOrigin() // open for all ports
public class MultipleFilesUploadController {

    @Autowired
    FileServiceImplementation fileServiceImplementation;

    /**
     * Method to upload multiple files
     * 
     * @param files
     * @return FileResponse
     */
    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFiles(@RequestParam("files") MultipartFile[] files) {

        try {

            createDirIfNotExist();

            List<String> fileNames = new ArrayList<>();

            // read and write the file to the local folder
            Arrays.asList(files).stream().forEach(file -> {
                byte[] bytes = new byte[0];
                try {
                    bytes = file.getBytes();
                    Files.write(Paths.get(FileUtil.folderPath + file.getOriginalFilename()), bytes);
                    fileNames.add(file.getOriginalFilename());
                } catch (IOException e) {

                }
            });

            // Declare empty list for collect the files data
            // which will come from UI
            List<FileDB> fileList = new ArrayList<FileDB>();
            for (MultipartFile file : files) {
                String fileContentType = file.getContentType();
                byte[] sourceFileContent = file.getBytes();
                String fileName = file.getOriginalFilename();
                FileDB fileModal = new FileDB(fileName, sourceFileContent, fileContentType);

                // Adding file into fileList
                fileList.add(fileModal);
            }

            // Saving all the list item into database
            fileServiceImplementation.saveAllFilesList(fileList);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new FileUploadResponse("Files uploaded successfully: " + fileNames));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new FileUploadResponse("Exception to upload files!"));
        }
    }

    /**
     * Create directory to save files, if not exist
     */
    private void createDirIfNotExist() {
        // create directory to save the files
        File directory = new File(FileUtil.folderPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    /**
     * Method to get the list of files from the file storage folder.
     * 
     * @return file
     */
    @GetMapping("/files")
    public ResponseEntity<String[]> getListFiles() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new File(FileUtil.folderPath).list());
    }

}