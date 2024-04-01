package com.minis.context;

//发布事件
public interface ApplicationEventPublisher {
	void publishEvent(ApplicationEvent event);
}