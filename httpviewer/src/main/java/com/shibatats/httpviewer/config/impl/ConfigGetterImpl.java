package com.shibatats.httpviewer.config.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.shibatats.httpviewer.config.ConfigGetter;

@Component
public class ConfigGetterImpl implements ConfigGetter {

	@Value("${targetURL}")
	private String targetURL;

	@Value("${proxyHost}")
	private String proxyHost;

	@Value("${proxyPort}")
	private String proxyPort;

	@Value("${encoding}")
	private String encoding;

	@Value("${printToFile}")
	private boolean printToFile;

	@Override
	public String getTargetURL() {
		return targetURL;
	}

	@Override
	public String getProxyHost() {
		return proxyHost;
	}

	@Override
	public String getProxyPort() {
		return proxyPort;
	}

	@Override
	public String getEncoding() {
		return encoding;
	}

	@Override
	public boolean isPrintToFile() {
		return printToFile;
	}

}
