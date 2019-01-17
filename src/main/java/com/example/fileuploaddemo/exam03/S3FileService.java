package com.example.fileuploaddemo.exam03;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3FileService {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    public String saveCommentThumb(InputStream in, String contentType, long size, String key) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(size);
        amazonS3Client.putObject(bucket, key, in, objectMetadata);
        return key;
    }
    public String saveThumb(InputStream in, String contentType, long size, String dirName) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(size);
        String fileKey = dirName + "/t/" + UUID.randomUUID();
        amazonS3Client.putObject(bucket, fileKey, in, objectMetadata);
        return amazonS3Client.getUrl(bucket, fileKey).toString();
    }
//    public ImageExif save(MultipartFile multipartFile, String dirName) throws IOException {
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentType(multipartFile.getContentType());
//        objectMetadata.setContentLength(multipartFile.getSize());
//        String fileKey = dirName + "/" + UUID.randomUUID();
//        amazonS3Client.putObject(bucket, fileKey, multipartFile.getInputStream(), objectMetadata);
//        amazonS3Client.getUrl(bucket, fileKey).toString();
//        ImageExif imageExif = getImageExif(fileKey, multipartFile.getSize());
//        imageExif.setFileKey(fileKey);
//        return imageExif;
//    }
//    public ImageExif getImageExif(String fileKey, long size) throws RuntimeException{
//        ImageExif imageExif = new ImageExif();
//        S3Object s3Object = amazonS3Client.getObject(bucket, fileKey);
//        String contentType = s3Object.getObjectMetadata().getContentType();
//        InputStream in = null;
//        try{
//            in = s3Object.getObjectContent();
//            Metadata metadata = null;
//            try{
//                metadata = ImageMetadataReader.readMetadata(in);
//            }catch (Exception ex){
//                throw new RuntimeException(fileKey + " : " + ex.getMessage());
//            }
//            Directory gifDirectory = metadata.getFirstDirectoryOfType(GifHeaderDirectory.class);
//            Directory pngDirectory = metadata.getFirstDirectoryOfType(PngDirectory.class);
//            Directory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
//            Directory exifDirecoty = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
//            int width = 0;
//            int height = 0;
//            int orientation = 1;
//            boolean jpegType = false;
//            boolean notImage = false;
//            if(gifDirectory != null){
//                imageExif.setImageType("gif");
//                width = gifDirectory.getInt(GifHeaderDirectory.TAG_IMAGE_WIDTH);
//                height = gifDirectory.getInt(GifHeaderDirectory.TAG_IMAGE_HEIGHT);
//            }else if(pngDirectory != null){
//                imageExif.setImageType("png");
//                width = pngDirectory.getInt(PngDirectory.TAG_IMAGE_WIDTH);
//                height = pngDirectory.getInt(PngDirectory.TAG_IMAGE_HEIGHT);
//            }else if(jpegDirectory != null){
//                imageExif.setImageType("jpeg");
//                jpegType = true;
//                width = jpegDirectory.getInt(JpegDirectory.TAG_IMAGE_WIDTH);
//                height = jpegDirectory.getInt(JpegDirectory.TAG_IMAGE_HEIGHT);
//                if(exifDirecoty != null && exifDirecoty.containsTag(ExifIFD0Directory.TAG_ORIENTATION))
//                    orientation = exifDirecoty.getInt(ExifIFD0Directory.TAG_ORIENTATION);
//            }else{
//                notImage = true;
//            }
//            if(!notImage) {
//                imageExif.setHeight(height);
//                imageExif.setWidth(width);
//                imageExif.setOrientation(orientation);
//                imageExif.setLength(size);
//            }else{
//                return null; // 이미지가 아닐 경우 null을 리턴함.
//            }
//        }catch(Exception ex){
//            throw new RuntimeException(fileKey + " : " + ex.getMessage());
//        }finally {
//            try {
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return imageExif;
//    }
//    public S3File get(String filekey) throws Exception{
//        S3Object s3Object = amazonS3Client.getObject(bucket, filekey);
//        String contentType = s3Object.getObjectMetadata().getContentType();
//        InputStream in = s3Object.getObjectContent();
//        return new S3File(filekey, contentType, in);
//    }
    public void remove(String fileKey) throws Exception{
        amazonS3Client.deleteObject(bucket, fileKey);
    }
    public boolean existsImage(String filekey) {
        try {
            ObjectMetadata objectMetadata = amazonS3Client.getObjectMetadata(bucket, filekey);
            if (objectMetadata == null)
                return false;
            else
                return true;
        }catch(Exception ex){
            return false;
        }
    }
}