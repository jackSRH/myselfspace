package com.mailian.core.bean;

import com.fasterxml.jackson.annotation.JsonView;
import com.mailian.core.manager.ViewManager;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/28
 * @Description:
 */
@ApiModel(value = "分页公共参数")
public class BasePage implements Serializable {
    private static final long serialVersionUID = -5167988575222182272L;

    @ApiModelProperty(value = "当前页码",notes = "默认1")
    private Integer currentPage = 1;
    @ApiModelProperty(value = "每页显示数量",notes = "默认10")
    private Integer pageSize = 10;

    @JsonView(value = ViewManager.SimpleView.class)
    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
