package com.ctrip.framework.apollo.portal.listener;

import com.google.common.base.Preconditions;

import com.ctrip.framework.apollo.common.entity.App;

import org.springframework.context.ApplicationEvent;

//创建项目的事件
public class AppCreationEvent extends ApplicationEvent {

  public AppCreationEvent(Object source) {
    super(source);
  }

  //获得事件对应的 App 对象
  public App getApp() {
    Preconditions.checkState(source != null);
    return (App) this.source;
  }
}
