package com.Cocfish.www;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class MotherBoard {
	
	public static void main (String[]args) {
		System.out.println(plugins);
	}
	
	public static void install(IUSB im) {
//		System.out.println("安装："+ im.getClass().getSimpleName());
//		im.swapData();
	}
	
	private static Properties properties = new Properties();
	private static Map<String, IUSB> plugins = new HashMap<>();
			
	static {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			// 从classPath的根路径去寻找Config.properties
			InputStream inputStream = loader.getResourceAsStream("Config.properties");
			
			properties.load(inputStream);
//			System.out.println(properties);
			
			init();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void init() throws Exception {
		Set<Object> keys = properties.keySet();
		for (Object item : keys) {
			// 获取key
			String name = (String)item;
			// 获取value
			String className = properties.getProperty(name);
			// 使用反射创建对象 ，需要对象有公共无参数构造器
			@SuppressWarnings("deprecation")
			Object object = Class.forName(className).newInstance();
			// 判断当前对象是否实现了IUSB规范
			if (!(object instanceof IUSB)) {
				throw new RuntimeException(name + "没有实现IUSB协议");
			} 
			plugins.put(name, (IUSB)object);
		}
	}
	public static void work() {
		for (IUSB item : plugins.values()) {
			item.swapData();
		}
	}

}
