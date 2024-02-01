package com.example.demo;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String message;
        try {
            FileDetails savedFileDetails = fileStorageService.store(file);
            message = "Uploaded the file successfully with fileId: " + savedFileDetails.getId();
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not upload the file!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileId) {
        FileDetails fileDetails = fileStorageService.getFile(fileId);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDetails.getName() + "\"")
            .body(fileDetails.getData());
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<String> updateFile(@PathVariable String fileId,
        @RequestParam("file") MultipartFile file) {
        String message;
        try {
            fileStorageService.update(file, fileId);
            message = "Updated the file successfully.";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not update the file!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) {
        String message;
        try {
            fileStorageService.delete(fileId);
            message = "Deleted the file successfully.";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not delete the file!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<ResponseFileData>> getListFiles() {
        List<ResponseFileData> files = fileStorageService.getAllFiles().map(dbFile ->
             new ResponseFileData(
                dbFile.getId(),
                dbFile.getName(),
                dbFile.getType(),
                dbFile.getFileSize(),
                dbFile.getCreatedOn(),
                dbFile.getUpdatedOn())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }
}
