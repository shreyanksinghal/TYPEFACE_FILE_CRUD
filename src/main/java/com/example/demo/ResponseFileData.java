package com.example.demo;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFileData {
    private String fileId;
    private String name;
    private String type;
    private Long size;
    private Date createdOn;
    private Date updatedOn;
}
