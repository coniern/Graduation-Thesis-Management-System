package com.hfuu.auth.controller;

import com.hfuu.auth.entity.ThesisDocument;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.IOException;

/**
 * 文件上传和管理控制器
 * 
 * @author hfuu
 */
@RestController
@RequestMapping("/api/file")
public class FileController {
    
    // 文件存储路径
    private final String uploadDir = "uploads/";
    
    /**
     * 上传文件
     * 
     * @param file 上传的文件
     * @param documentType 文档类型
     * @param studentId 学生ID
     * @return 上传结果
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") Integer documentType,
            @RequestParam("studentId") Long studentId) {
        try {
            // 创建上传目录
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String uniqueFilename = UUID.randomUUID().toString() + "." + fileExtension;
            
            // 保存文件
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // 保存文件信息到数据库（这里只是模拟，实际应该调用服务层）
            ThesisDocument document = new ThesisDocument();
            document.setStudentId(studentId);
            document.setDocumentType(documentType);
            document.setDocumentName(originalFilename);
            document.setFilePath(filePath.toString());
            document.setFileSize(file.getSize());
            document.setFileFormat(fileExtension);
            document.setUploadTime(new java.util.Date().toString());
            
            // 返回上传结果
            return ResponseEntity.ok("文件上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("文件上传失败");
        }
    }
    
    /**
     * 下载文件
     * 
     * @param fileId 文件ID
     * @return 文件资源
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        try {
            // 这里应该根据fileId从数据库查询文件信息
            // 为了简化，这里直接使用一个示例文件路径
            Path filePath = Paths.get(uploadDir + "example.pdf");
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 获取学生的文件列表
     * 
     * @param studentId 学生ID
     * @return 文件列表
     */
    @GetMapping("/student/{studentId}")
    public List<ThesisDocument> getStudentFiles(@PathVariable Long studentId) {
        // 模拟获取学生文件列表
        List<ThesisDocument> files = new ArrayList<>();
        return files;
    }
    
    /**
     * 获取文件版本列表
     * 
     * @param fileId 文件ID
     * @return 版本列表
     */
    @GetMapping("/versions/{fileId}")
    public List<ThesisDocument> getFileVersions(@PathVariable Long fileId) {
        // 模拟获取文件版本列表
        List<ThesisDocument> versions = new ArrayList<>();
        return versions;
    }
}
