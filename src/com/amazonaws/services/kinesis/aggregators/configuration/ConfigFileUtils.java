package com.amazonaws.services.kinesis.aggregators.configuration;

import java.net.URL;
import java.util.Date;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

public class ConfigFileUtils {
    public static final String makeConfigFileURL(String configUrl) {
        String url = null;

        if (configUrl.startsWith("http")) {
            url = configUrl;
        } else {
            AmazonS3 s3Client = new AmazonS3Client();
            String bucket = configUrl.split("/")[2];
            String prefix = configUrl.substring(configUrl.indexOf(bucket) + bucket.length() + 1);

            // generate a presigned url for X hours
            Date expiration = new Date();
            long msec = expiration.getTime();
            msec += 1000 * 60 * 60; // 1 hour.
            expiration.setTime(msec);

            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
                    bucket, prefix);
            generatePresignedUrlRequest.setMethod(HttpMethod.GET);
            generatePresignedUrlRequest.setExpiration(expiration);

            URL s3url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            url = s3url.toString();
        }

        return url;
    }
}
