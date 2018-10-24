package com.mailian.core.bean;

import com.fasterxml.jackson.annotation.JsonView;
import com.mailian.core.manager.ViewManager;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/27
 * @Description: 分页bean
 */
@ApiModel(description = "分页实体")
public class PageBean<T> {
    @ApiModelProperty(value = "当前页")
    private Integer currentPage = 1;

    @ApiModelProperty(value = "每页显示的总条数")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "总条数")
    private Integer totalNum;

    @ApiModelProperty(value = "是否有下一页")
    private Integer isMore;

    @ApiModelProperty(value = "总页数")
    private Integer totalPage;

    @ApiModelProperty(value = "开始索引")
    private Integer startIndex;

    @ApiModelProperty(value = "分页结果")
    private List<T> items;

    public PageBean() {
        super();
    }

    public PageBean(Integer currentPage, Integer pageSize, Integer totalNum, List<T> items) {
        super();
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalNum = totalNum;
        this.items = items;
        this.totalPage = (this.totalNum+this.pageSize-1)/this.pageSize;
        this.startIndex = (this.currentPage-1)*this.pageSize;
        this.isMore = this.currentPage >= this.totalPage?0:1;
    }

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

    @JsonView(value = ViewManager.SimpleView.class)
    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public Integer getIsMore() {
        return isMore;
    }

    public void setIsMore(Integer isMore) {
        this.isMore = isMore;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
