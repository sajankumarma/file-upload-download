package com.sajan.fileuploaddownload.controller;

import com.sajan.fileuploaddownload.exception.MyFileNotFoundException;
import com.sajan.fileuploaddownload.payload.UploadFileResponse;
import com.sajan.fileuploaddownload.properties.FileStorageProperties;
import com.sajan.fileuploaddownload.services.FileStorageService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class FileUploadDownloadController
{
    private static final Logger logger = LoggerFactory.getLogger(FileUploadDownloadController.class);
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    FileStorageProperties fileStorageProperties;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file)
    {
        try
        {
            String fileName = fileStorageService.storeFile(file);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(fileName)
                    .toUriString();
            return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
        }
        catch (Exception e)
        {
            logger.debug("error", e);
            return null;
        }

    }

    @PostMapping("/uploadFiles")
    public List<UploadFileResponse> uploadFiles(@RequestParam("files") MultipartFile[] files)
    {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws MyFileNotFoundException, FileNotFoundException
    {
        File file = new File(fileStorageProperties.getUploadDir() + File.separator + fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                //.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"", resource.getFilename() + "\"")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/all-files")
    public JSONArray getAllFiles() throws IOException
    {
        List<File> files = Files.list(Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize()).map(Path::toFile)
                .collect(Collectors.toList());
        JSONArray array = new JSONArray();

        for (File file : files)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("FileName", file.getName());
            jsonObject.put("Link", "/downloadFile/" + file.getName());
            array.add(jsonObject);
        }
        return array;

    }

}
