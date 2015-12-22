package com.shibatats.httpviewer.app.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shibatats.httpviewer.app.HttpViewer;
import com.shibatats.httpviewer.config.ConfigGetter;
import com.shibatats.httpviewer.printer.Printer;

@Component
public class HttpViewerImpl implements HttpViewer {

    @Autowired
    private ConfigGetter config;

    @Autowired
    private Printer printer;

    private static final String LS = System.getProperty("line.separator");

    // Public Method ///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void doService() {
        HttpURLConnection urlConn = null;
        try {
            urlConn = getHttpURLConnection();
            urlConn.connect();

            String headerStr = getHeaderStr(urlConn);
            String bodyStr = printBody(urlConn);
            String content = headerStr + bodyStr;

            printer.printToConsole(content);
            if (config.isPrintToFile()) {
                printer.printToFile(content);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
    }

    // Private Method //////////////////////////////////////////////////////////////////////////////////////////////////
    private HttpURLConnection getHttpURLConnection() throws MalformedURLException, IOException {
        String targetURL = config.getTargetURL();
        String proxyHost = config.getProxyHost();
        String proxyPort = config.getProxyPort();

        URL url = new URL(targetURL);
        if (!StringUtils.isEmpty(proxyHost) && !StringUtils.isEmpty(proxyPort) && StringUtils.isNumeric(proxyPort)) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort)));
            return (HttpURLConnection)url.openConnection(proxy);
        }
        return (HttpURLConnection)url.openConnection();
    }

    private String getHeaderStr(HttpURLConnection urlConn) {
        StringBuilder sb = new StringBuilder();
        sb.append(LS + "----------- Header -----------" + LS);
        Map<String, List<String>> headerList = urlConn.getHeaderFields();
        for (Entry<String, List<String>> entry : headerList.entrySet()) {
            String key = entry.getKey();
            List<String> valList = entry.getValue();
            sb.append(key + " : " + valList + LS);
        }
        return sb.toString();
    }

    private String printBody(HttpURLConnection urlConn) throws IOException, UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append(LS + "----------- Body -----------" + LS);

        InputStream resStream = getResultStream(urlConn);
        if (resStream == null)
            return sb.append("This response has no HttpBody.").toString();

        String encoding = config.getEncoding();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resStream, encoding))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + LS);
            }
        }
        return sb.toString();
    }

    private InputStream getResultStream(HttpURLConnection urlConn) throws IOException {
        switch (urlConn.getResponseCode()) {
        case HttpURLConnection.HTTP_OK:
            return urlConn.getInputStream();
        case HttpURLConnection.HTTP_NOT_FOUND:
            return null;
        }

        return urlConn.getErrorStream();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
