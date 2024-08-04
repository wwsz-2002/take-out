package com.sky.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
public class UuidUtil {

    /**
     * 根据文件名生成UUID
     * @param file
     * @return
     */
    public String getUuid(MultipartFile file){
        //此处设定保存的路径名字
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt，即你最后保存在Bucket中的路径和名字。
        //进行UUid起名
        String originalFilename = file.getOriginalFilename();
        //获取最后一个点的位置index
        int index = originalFilename.lastIndexOf(".");
        //根据最后一个点的位置获取原始文件的拓展名字extname
        String extname = originalFilename.substring(index);
        //构造唯一的文件名以防止同名文件覆盖--使用uuid（通用唯一识别码）92c57c26-8bda-4d5e-b7a3-4f6eb634d130
        String objectName = UUID.randomUUID().toString()+extname;
        return objectName;
    }
}
