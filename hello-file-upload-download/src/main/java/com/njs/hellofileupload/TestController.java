package com.njs.hellofileupload;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
 

@Controller
public class TestController { 
	
	@Autowired
    private TestService testService ;
	
	@GetMapping("/")
	public String uploadForm() {
		return "file-upload.html";
	}			
	

	// 파일 업로드
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<?> uploadFile(
    		 @RequestParam("title") String title, 
    		 @RequestParam("price") String price, 
    		 @RequestParam("photo") MultipartFile photo) {
    	
   	    Map<String, String> responseMap = new HashMap<>();

        if (photo.isEmpty()) {
            responseMap.put("error", "파일을 선택하세요.");
            return ResponseEntity.badRequest().body(responseMap);
        }

        String uploadFileName = "";
        try {
            // 파일 저장 
            // uploadFileName =  testService.uploadFile(photo.getOriginalFilename(), photo);  // Files.copy 업로드
            // uploadFileName =  testService.uploadFile2(photo.getOriginalFilename(), photo); // MultipartFile.transferTo 업로드
            uploadFileName =  testService.uploadFile3(photo.getOriginalFilename(), photo); // 파일명을 UUID 로 변경하여 업로드
        } catch (IOException e) {
            System.out.println("파일 업로드 실패(controller): " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일을 업로드 할 수 없습니다.");
        }
 
        // 추가 작업 수행
        // TODO: 여기에 DB 저장하는 코드를 작성
                
        // JSON 응답 생성
        responseMap.put("message", "파일 업로드를 성공하였습니다.");
        responseMap.put("title", title);
        responseMap.put("price", price);
        responseMap.put("photo", uploadFileName);

        return ResponseEntity.ok().body(responseMap);
    }
         
    // 파일 다운로드
    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        Resource resource;
        try {
            resource = testService.downloadFile(fileName);
        } catch (IOException e) {
            System.out.println("파일 다운로드 실패(controller): " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일을 다운로드할 수 없습니다.");
        }

        if (resource == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("파일을 찾을 수 없습니다.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }     
    
}