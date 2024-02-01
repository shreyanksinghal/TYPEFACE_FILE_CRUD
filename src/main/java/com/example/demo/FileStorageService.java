package com.example.demo;

import java.io.IOException;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    @Autowired
    private FileDetailsRepository fileDBRepository;

    public FileDetails store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDetails fileDetails = new FileDetails(fileName, file.getContentType(), file.getBytes(), file.getSize());
        return fileDBRepository.save(fileDetails);
    }

    public FileDetails update(MultipartFile file, String fileId) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDetails fileDetails = new FileDetails(fileName, file.getContentType(), file.getBytes(), file.getSize());

        if(fileDBRepository.existsById(fileId)) {
            fileDetails.setId(fileId);
        }
        return fileDBRepository.save(fileDetails);
    }

    public FileDetails getFile(String id) {
        return fileDBRepository.findById(id).get();
    }

    public Stream<FileDetails> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }

    public void delete(String fileId) {
        fileDBRepository.deleteById(fileId);
    }
}
