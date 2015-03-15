package com.shibatats.httpviewer.config;

public interface ConfigGetter {

	String getTargetURL();

	String getProxyHost();

	String getProxyPort();

	String getEncoding();

	boolean isPrintToFile();

}
