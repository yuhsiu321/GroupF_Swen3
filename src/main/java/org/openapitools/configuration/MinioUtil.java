package org.openapitools.configuration;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * 云存储工具类
 *
 * @author lxj
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Component
@Slf4j

public class MinioUtil {


    final static MinioClientConfig prop = new MinioClientConfig();

    final MinioClient minioClient;

    public MinioUtil(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * 查看存储bucketName是否存在
     *
     * @return boolean
     */
    public Boolean bucketExists(String bucketName) {
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {

            return false;
        }
        return found;
    }

    /**
     * 创建存储bucketName
     *
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets() {
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            return buckets;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 文件上传
     *
     * @return Boolean
     * @paramfile 文件
     */
    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new RuntimeException();
        }
        String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String objectName = sdf.format(new Date()) + "/" + fileName;
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(prop.getBucketName()).object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build();
            //文件名称相同覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return objectName;
    }

    /**
     * 预览
     *
     * @return
     * @paramfileName
     */
    public String preview(String fileName) {
        // 查看文件地址
        GetPresignedObjectUrlArgs build = new GetPresignedObjectUrlArgs().builder().bucket(prop.getBucketName()).object(fileName).method(Method.GET).build();
        try {
            String url = minioClient.getPresignedObjectUrl(build);
            return url;
        } catch (Exception e) {

        }
        return null;
    }


    /**
     * 下载云存储文件到本地，默认存储项目根目录。可指定fileName 路径：D:\\file\\ceshi.txt
     *
     * @paramfilePath
     */
    public void downloadLocal(String filePath) {
        try {
            String decode = URLDecoder.decode(filePath, "utf-8");
            String fileName = decode.substring(decode.lastIndexOf("/") + 1);
            StatObjectResponse response = minioClient.statObject(
                    StatObjectArgs.builder().bucket(prop.getBucketName()).object(filePath).build()
            );
            if (response != null) {
                minioClient.downloadObject(DownloadObjectArgs.builder()
                        .bucket(prop.getBucketName())
                        .object(filePath)
                        .filename(fileName)
                        .build());
            }
        } catch (Exception e) {

        }
    }

    /**
     * 查看文件对象
     *
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(prop.getBucketName()).build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {

            return null;
        }
        return items;
    }

    /**
     * 删除
     *
     * @return
     * @throws Exception
     * @paramfileName
     */
    public boolean remove(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(prop.getBucketName()).object(fileName).build());
        } catch (Exception e) {

            return false;
        }
        return true;
    }
}