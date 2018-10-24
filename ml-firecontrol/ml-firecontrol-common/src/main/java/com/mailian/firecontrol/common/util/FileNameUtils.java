package com.mailian.firecontrol.common.util;

import com.mailian.core.constants.CommonConstant;

import java.util.UUID;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/20
 * @Description:
 */
public class FileNameUtils {
    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName(String fileOriginName){
        return UUID.randomUUID() + getSuffix(fileOriginName);
    }

    /**
     * 判断文件是否图片格式
     * @param fileOriginName
     * @return
     */
    public static boolean isImg(String fileOriginName){
        return CommonConstant.IMAGE_TYPE.contains(getSuffix(fileOriginName).trim().toLowerCase());
    }
}
