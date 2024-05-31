package com.livecard.front.common.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3 {

    private static final String USER_PROFILE = "user_profile";
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 파일 업로드
     * s3 key = dir/key.{multipartFile.extension}
     *
     * @return uploaded s3 url
     */
    @Nullable
    public String upload(MultipartFile multipartFile, String key) {
        return upload(
                multipartFile,
                key,
                StringUtils.getFilenameExtension(multipartFile.getOriginalFilename())
        );
    }

    /**
     * 파일 업로드
     * s3 key = dir/key.extension
     *
     * @return uploaded s3 url
     */
    @Nullable
    public String upload(MultipartFile multipartFile, String key, String extension) {
        return uploadToS3(multipartFile, key + "." + extension);
    }

    @Nullable
    private String uploadToS3(MultipartFile multipartFile, String key) {
        try (final InputStream stream = multipartFile.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(multipartFile.getContentType());
            metadata.setContentLength(multipartFile.getSize());

            PutObjectRequest request = new PutObjectRequest(
                    bucket,
                    key,
                    stream,
                    metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3Client.putObject(request);
            return amazonS3Client.getUrl(bucket, key).toString();
        } catch (IOException e) {
            log.error("updateToS3 = {}/{}, {}", bucket, key, multipartFile.getName());
            return null;
        }
    }

    public void delete(String path) {
        String key = path.substring(path.indexOf(USER_PROFILE));
        deleteFromS3(key);
    }

    private void deleteFromS3(String key) {
        amazonS3Client.deleteObject(bucket, key);
    }
}