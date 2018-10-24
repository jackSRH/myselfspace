package com.mailian.core.manager;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/28
 * @Description:视图管理器
 */
public class ViewManager {
    public interface SimpleView { }

    public interface WebSimpleView extends SimpleView{ }

    public interface WebDetailView extends WebSimpleView{ }

    public interface AppSimpleView extends SimpleView{ }

    public interface AppDetailView extends SimpleView{ }

    public interface LoginView extends SimpleView{ }

    public interface OtherView extends SimpleView{ }
}
