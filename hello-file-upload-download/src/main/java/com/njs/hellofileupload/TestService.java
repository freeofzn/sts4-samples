package com.njs.hellofileupload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TestService {
	
    @Value("${file.upload-directory}")
    private String uploadDirectory;

	/* 1. Files.copy 사용하여 업로드 하기 */
    public String uploadFile(String fileName, MultipartFile multipartFile) throws IOException {
        String filePath = uploadDirectory + "/" + fileName;

        File file = new File(filePath);
        if (file.exists()) {
        	throw new RuntimeException("파일 업로드에 실패했습니다.(파일중복)");
        }
        
        try {
            // 디렉토리가 존재하지 않으면 생성
            Path directory = Paths.get(uploadDirectory);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // 파일 복사
            Path targetPath = Paths.get(filePath);
            Files.copy(multipartFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e) {
            // 예외 던지기
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }    
  	 	
    }
    
    /* 2. MultipartFile 의 transferTo 사용하여 업로드 하기 */
    public String uploadFile2(String fileName, MultipartFile multipartFile) throws IOException {
        String filePath = uploadDirectory + "/" + fileName;

        File file = new File(filePath);
        if (file.exists()) {
            throw new RuntimeException("파일 업로드에 실패했습니다.(파일중복)");
        }

        try {
            // 디렉토리가 존재하지 않으면 생성
            Path directory = Paths.get(uploadDirectory);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // transferTo() 메서드를 사용하여 파일 저장
            File targetFile = new File(filePath);
            multipartFile.transferTo(targetFile);

            return fileName;
        } catch (Exception e) {
            // 예외 던지기
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
       
    }   	
    

	/* 3. 원본파일 대신 UUID 저장하여 중복피하기  */
    public String uploadFile3(String fileName, MultipartFile multipartFile) throws IOException {
        try {
            // 디렉토리가 존재하지 않으면 생성
            Path directory = Paths.get(uploadDirectory);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // 파일명을 UUID로 변경하여 중복 방지
            String originalFilename = multipartFile.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uuidFileName = UUID.randomUUID().toString() + fileExtension;

            // 저장할 파일 경로 설정
            String filePath = uploadDirectory + "/" + uuidFileName;

            // transferTo() 메서드를 사용하여 파일 저장
            File targetFile = new File(filePath);
            multipartFile.transferTo(targetFile);
            
            return uuidFileName;
        } catch (Exception e) {
            // 예외 던지기
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }        
        
    }   	
     
 
    // 파일 다운로드 메서드
    public Resource downloadFile(String fileName) throws IOException {
    	Path filePath = Paths.get(uploadDirectory).resolve(fileName).normalize();
        Resource resource;

        try {
            resource = new InputStreamResource(Files.newInputStream(filePath));
        } catch (IOException e) {
            System.out.println("파일 다운로드 오류(service): " + e.getMessage());
            throw new IOException("파일을 읽을 수 없습니다.", e);
        }
        
        return resource;
    }
    
	
}