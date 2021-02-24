package com.sajan.fileuploaddownload;

import com.sajan.fileuploaddownload.properties.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class FileUploadDownloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploadDownloadApplication.class, args);
	}

}
