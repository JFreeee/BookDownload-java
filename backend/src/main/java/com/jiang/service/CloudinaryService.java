package com.jiang.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

	@Autowired
    private Cloudinary cloudinary;

    // ===== 上传图片 =====
    public String upload(MultipartFile file, String folder) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", folder)
        );
        return uploadResult.get("secure_url").toString();
    }

    // ===== 上传PDF / 任意文件 =====
    public String uploadRaw(MultipartFile file, String folder) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "raw",
                        "folder", folder
                )
        );
        return uploadResult.get("secure_url").toString();
    }

    // ===== 删除文件=====
    public void delete(String url) throws Exception {

        // 解析 public_id
        String publicId = url
                .substring(url.indexOf("/upload/") + 8)   // 去掉前面
                .replaceAll("\\.[^\\.]+$", "");           // 去掉扩展名

        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}