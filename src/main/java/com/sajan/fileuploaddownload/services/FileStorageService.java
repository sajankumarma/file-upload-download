package com.sajan.fileuploaddownload.services;

import com.sajan.fileuploaddownload.exception.FileStorageException;
import com.sajan.fileuploaddownload.exception.MyFileNotFoundException;
import com.sajan.fileuploaddownload.properties.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService
{
    private final Path fileStoragePath;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) throws FileStorageException
    {
        this.fileStoragePath = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try
        {
            Files.createDirectories(this.fileStoragePath);
        }
        catch (Exception e)
        {
            throw new FileStorageException("couldn't create directory where file needs to be uploaded",e);
        }

    }

    public String storeFile(MultipartFile file) throws FileStorageException
    {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try
        {
            if (fileName.contains(".."))
            {
                throw new FileStorageException("Filename contains invalid path, couldn't upload " + fileName);
            }
            Path targetLocation = this.fileStoragePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }
        catch(Exception e)
        {
            throw new FileStorageException("Error wile storing file " + fileName +" please try again",e);
        }
    }

    public Resource loadFileAsResource(String fileName) throws MyFileNotFoundException
    {
        try
        {
            Path filePath = this.fileStoragePath.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists())
            {
                return resource;
            }
            else
            {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        }
        catch (MalformedURLException e)
        {
            throw new MyFileNotFoundException("File not found " + fileName);
        }
    }
}
