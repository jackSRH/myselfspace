package com.mailian.firecontrol.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.util.StringUtil;
import com.mailian.firecontrol.dao.auto.mapper.AppMapper;
import com.mailian.firecontrol.dao.auto.model.App;
import com.mailian.firecontrol.dto.app.AppInfo;
import com.mailian.firecontrol.service.AppService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppServiceImpl extends BaseServiceImpl<App, AppMapper> implements AppService {

    @Value("${app.dir}")
    private String APP_PATH;
    @Value("${system.domain}")
    private String SYSTEM_DOMAIN;
    @Value("${appdownloadpath.uri}")
    private String DOWNLOADPATH_URI;
    @Value("YunAPP.apk")
    private String APP_LOAD;

    @Override
    public AppInfo selectLatest(Integer type){
        Page page = PageHelper.startPage(1,1);
        page.setOrderBy("update_time desc");
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("type",type);
        List<App> apps = this.selectByMap(queryMap);

        if(StringUtils.isEmpty(apps)){
            return null;
        }

        AppInfo appInfo = new AppInfo();
        BeanUtils.copyProperties(apps.get(0),appInfo);
        String path = SYSTEM_DOMAIN + DOWNLOADPATH_URI + appInfo.getPath();
        appInfo.setPath(path);
        return appInfo;
    }

    @Override
    public ResponseResult downLoadApp(String filePath, HttpServletResponse response) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseResult.buildFailResult();
        }
        String[] str = filePath.split("\\/");
        String fileName = str[str.length -1];
        response.setHeader("content-disposition", "attachment;filename="
                + URLEncoder.encode(fileName, "UTF-8"));
        // 读取要下载的文件，保存到文件输入流
        FileInputStream in = new FileInputStream(file);
        response.setHeader("Content-Length", ""+file.length());

        // 创建输出流
        OutputStream out = response.getOutputStream();
        // 创建缓冲区
        byte buffer[] = new byte[1024];
        int len = 0;

        // 循环将输入流中的内容读取到缓冲区当中
        while ((len = in.read(buffer)) > 0) {
            // 输出缓冲区的内容到浏览器，实现文件下载
            out.write(buffer, 0, len);
        }

        // 关闭文件输入流
        in.close();
        // 关闭输出流
        out.close();
        return ResponseResult.buildOkResult();
    }

    @Override
    public ResponseResult upLoadApp(AppInfo appInfo, MultipartFile appFile) throws Exception{
        App app = new App();
        BeanUtils.copyProperties(appInfo,app);
        String md5 = StringUtil.getMd5InputStream(appFile.getInputStream());
        app.setMd5(md5);
        String filename = appFile.getOriginalFilename();
        app.setName(filename);

        String path;
        if(0 == app.getType()){
            path = APP_PATH  + "config"+ File.separator;
        }else{
            path = APP_PATH  + "user" + File.separator;
        }

        //新增或者更新固定路径的文件
        String basePath = path + APP_LOAD;
        File baseFile = new File(basePath);
        File dir = baseFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }else{
            baseFile.delete();
        }
        FileUtils.copyInputStreamToFile(appFile.getInputStream(),baseFile);
        app.setPath(basePath);
        return this.insert(app)> 0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }
}
