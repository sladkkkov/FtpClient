package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FtpFile {
    private String name;
    private Long size;
    private FtpFile parent;
    private boolean isDirectory;
}