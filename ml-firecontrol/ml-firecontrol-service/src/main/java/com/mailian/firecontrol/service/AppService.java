package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.ResponseResult;
import com.mailian.firecontrol.dao.auto.model.App;
import com.mailian.firecontrol.dto.app.AppInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface AppService extends BaseService<App> {

    AppInfo selectLatest(Integer type);

    ResponseResult downLoadApp(String filePath, HttpServletResponse response) throws Exception;

    /**
     * 上传app更新包以及更新app版本信息
     * @param appInfo,appFile
     * @return
     */
    ResponseResult upLoadApp(AppInfo appInfo, MultipartFile appFile) throws Exception;
}
