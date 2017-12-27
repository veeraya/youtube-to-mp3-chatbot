package com.veerayaaa.youtubetomp3bot.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;

@Service
@ConfigurationProperties(prefix="s3")
public class AmazonS3Service {
    private static final Logger logger = LoggerFactory.getLogger(AmazonS3Service.class);
    private AmazonS3 s3;
    private String mp3BucketName;

    public AmazonS3Service() {
        this.s3 = new AmazonS3Client();
        Region region = Region.getRegion(Regions.AP_SOUTHEAST_1);
        this.s3.setRegion(region);
    }

    public String uploadMp3(String uploadedFileName, InputStream inputStream) {
        return uploadFile(this.mp3BucketName, uploadedFileName, inputStream);
    }

    private String uploadFile(String bucketName, String uploadedFileName, InputStream inputStream) {
        logger.info("Uploading [{}] to S3 bucket [{}]", uploadedFileName, bucketName);
        ObjectMetadata metadata = new ObjectMetadata();
        PutObjectRequest request = new PutObjectRequest(bucketName, uploadedFileName, inputStream, metadata);
        PutObjectResult putObjectResult = this.s3.putObject(request);
        return getPresignedUrl(bucketName, uploadedFileName);
    }

    private String getPresignedUrl(String bucketName, String objectKey){
        logger.info("Generating pre-signed URL for key {} and bucketname {}", objectKey, bucketName);
        java.util.Date expiration = new java.util.Date();
        long milliSeconds = expiration.getTime();
        milliSeconds += (1000 * 60 * 60) * (24 * 2); // 2 days
        expiration.setTime(milliSeconds);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        generatePresignedUrlRequest.setExpiration(expiration);

        URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
        logger.info("Generated presigned url: {}", url.toString());
        return url.toString();
    }

    public String getMp3BucketName() {
        return mp3BucketName;
    }

    public void setMp3BucketName(String mp3BucketName) {
        this.mp3BucketName = mp3BucketName;
    }
}
