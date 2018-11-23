package com.mailian.firecontrol.dto.web.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.mailian.core.bean.BaseInfo;
import com.mailian.core.bean.TreeEntity;
import com.mailian.core.manager.ViewManager;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/29
 * @Description:
 */
@ApiModel(description = "菜单信息")
public class MenuResp extends BaseInfo implements TreeEntity<MenuResp> {

    @ApiModelProperty(value = "菜单ID")
    private Integer id;

    @ApiModelProperty(value = "父菜单ID")
    private Integer parentId;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;

    @ApiModelProperty(value = "菜单url")
    private String url;

    @ApiModelProperty(value = "路由地址")
    private String routePath;

    @ApiModelProperty(value = "菜单类型",notes = "0目录 1菜单 2按钮")
    private Integer menuType;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "子集菜单")
    private List<MenuResp> childMenu;

    /*详细信息*/
    @ApiModelProperty(value = "是否显示",notes = "0显示 1隐藏")
    private Integer visible;

    @ApiModelProperty(value = "权限标识")
    private String perms;

    @ApiModelProperty(value = "父菜单信息")
    private MenuResp parentMenu;


    @JsonView(value = ViewManager.SimpleView.class)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public List<MenuResp> getChildMenu() {
        return childMenu;
    }

    public void setChildMenu(List<MenuResp> childMenu) {
        this.childMenu = childMenu;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    @JsonView(value = ViewManager.SimpleView.class)
    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public MenuResp getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(MenuResp parentMenu) {
        this.parentMenu = parentMenu;
    }

    @JsonIgnore
    @Override
    public String getIdStr() {
        return id.toString();
    }

    @JsonIgnore
    @Override
    public String getParentIdStr() {
        return parentId.toString();
    }

    @JsonIgnore
    @Override
    public Integer getSortNo() {
        return orderNum;
    }

    @JsonIgnore
    @Override
    public String getNameStr() {
        return menuName;
    }

    @JsonIgnore
    @Override
    public void setChildList(List<MenuResp> childList) {
        childMenu = childList;
    }

    @JsonIgnore
    @Override
    public boolean filterByParam(Object... params) {
        return false;
    }

    @JsonIgnore
    @Override
    public void setChildBizCount(Integer bizCount) {

    }

    @JsonIgnore
    @Override
    public Integer getBizCount() {
        return null;
    }
}
