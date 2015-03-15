package com.shibatats.httpviewer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shibatats.httpviewer.app.HttpViewer;

public class Main {

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		HttpViewer app = ctx.getBean(HttpViewer.class);
		app.doService();
		((ClassPathXmlApplicationContext)ctx).close();
	}

}
