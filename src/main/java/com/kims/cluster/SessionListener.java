package com.kims.cluster;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener{

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		//세션 타임 아웃 4시간 적용
        event.getSession().setMaxInactiveInterval(60 * 240);
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
	}
	
	public SessionListener() {
		System.out.println("SessionListener===========================");
	}
}
