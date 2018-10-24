package com.mailian.firecontrol.service.component;

import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.util.FileNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/30
 * @Description: 上传组件
 */
@Component
public class UploadComponent {
    private static final Logger log = LoggerFactory.getLogger(UploadComponent.class);

    @Value("${web.upload-path}")
    private String DEFAULT_PATH;

    /**
     * 公共上传
     * @param dirName
     * @param useRandomFileName
     * @param file
     * @return
     * @throws IOException
     */
    public String upload(String dirName,boolean useRandomFileName,MultipartFile file,String defaultPrefix) throws IOException {
        String filename = file.getOriginalFilename();
        if(useRandomFileName){
            filename = FileNameUtils.getFileName(filename);
        }else{
            filename = StringUtils.nvl(defaultPrefix,"") + filename;
        }
        String relativePath = dirName + File.separator + filename;
        File uploaderFile = new File(DEFAULT_PATH +File.separator+ relativePath);
        File dir = uploaderFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file.transferTo(uploaderFile);
        return relativePath;
    }

    /**
     * 获取默认路径
     * @return
     */
    public String getDefaultPath(){
        return DEFAULT_PATH;
    }
}
