package com.ctrip.framework.apollo.spring.util;

import com.ctrip.framework.apollo.exceptions.ApolloConfigException;
import com.ctrip.framework.apollo.spring.config.ConfigPropertySourceFactory;
import com.ctrip.framework.apollo.spring.property.PlaceholderHelper;
import com.ctrip.framework.apollo.spring.property.SpringValueRegistry;
import com.ctrip.framework.apollo.tracer.Tracer;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

//基于Guice的Ioc容器
public class SpringInjector {
  private static volatile Injector s_injector;
  private static final Object lock = new Object();

  private static Injector getInjector() {
    if (s_injector == null) {
      synchronized (lock) {
        if (s_injector == null) {
          try {
              //创建一个容器
            s_injector = Guice.createInjector(new SpringModule());
          } catch (Throwable ex) {
            ApolloConfigException exception = new ApolloConfigException("Unable to initialize Apollo Spring Injector!", ex);
            Tracer.logError(exception);
            throw exception;
          }
        }
      }
    }

    return s_injector;
  }

  public static <T> T getInstance(Class<T> clazz) {
    try {
      return getInjector().getInstance(clazz);
    } catch (Throwable ex) {
      Tracer.logError(ex);
      throw new ApolloConfigException(
          String.format("Unable to load instance for %s!", clazz.getName()), ex);
    }
  }

  private static class SpringModule extends AbstractModule {
      //现在注入到里面，这三个类需要提前加载到Guice容器内，且只能引入这三个依赖
    @Override
    protected void configure() {
      bind(PlaceholderHelper.class).in(Singleton.class);
      bind(ConfigPropertySourceFactory.class).in(Singleton.class);
      bind(SpringValueRegistry.class).in(Singleton.class);
    }
  }
}
