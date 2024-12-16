package com.backend.immilog.image.infrastructure.gateway;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.backend.immilog.global.exception.CustomException;
import com.backend.immilog.notification.applicaiton.service.DiscordSendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.backend.immilog.global.exception.CommonErrorCode.IMAGE_UPLOAD_FAILED;

@RequiredArgsConstructor
@Service
public class S3FileStorageHandler implements FileStorageHandler {
    private final AmazonS3 amazonS3;
    private final DiscordSendingService discordSendingService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String uploadFile(
            MultipartFile multipartFile,
            String imagePath
    ) {
        String originalFileName = generateFileName(multipartFile, imagePath);
        uploadFileToS3(multipartFile, originalFileName);
        return generateFileUrl(originalFileName);
    }

    @Override
    public void deleteFile(String imagePath) {
        amazonS3.deleteObject(bucket, imagePath);
    }

    private String generateFileName(
            MultipartFile multipartFile,
            String imagePath
    ) {
        return imagePath + "/" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 16)
                + "." + getFileExtension(multipartFile);
    }

    private void uploadFileToS3(
            MultipartFile multipartFile,
            String originalFileName
    ) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, originalFileName, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            discordSendingService.send("S3 파일 업로드", e);
            throw new CustomException(IMAGE_UPLOAD_FAILED);
        }
    }

    private String generateFileUrl(String originalFileName) {
        return amazonS3.getUrl(bucket, originalFileName).toString();
    }

    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            int lastDotIndex = originalFilename.lastIndexOf(".");
            if (lastDotIndex != -1) {
                return originalFilename.substring(lastDotIndex + 1);
            }
        }
        return "png";
    }
}
