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
import java.util.Base64;
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
public class HttpViewerImplWithBasicTemp implements HttpViewer {

    @Autowired
    private ConfigGetter config;

    @Autowired
    private Printer printer;

    private static final String LS = System.getProperty("line.separator");

    // Public Method ///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void doService() {

        final String USERNAME   = "hoge";
        final String PASSWORD   = "hoge";
        // Authenticator.setDefault(new Authenticator() {
        // @Override
        // protected PasswordAuthentication getPasswordAuthentication() {
        // return new PasswordAuthentication(USERNAME, PASSWORD.toCharArray());
        // }
        // });

        HttpURLConnection urlConn = null;
        try {
            urlConn = getHttpURLConnection();

            final String userPassword = USERNAME + ":" + PASSWORD;
            final String encodeAuthorization = Base64.getEncoder().encodeToString(userPassword.getBytes());
            urlConn.setRequestProperty("Authorization", "Basic " + encodeAuthorization);

            urlConn.addRequestProperty("X-mazda-common-header", "{\"RequesterApplicationMessageID\":\"2802568c-2e5d-494b-a628-d1492bf7ff27\",\"RequesterApplicationID\":\"factgroup\",\"CallerApplicationID\":\"factgroup\",\"CallerIP\":\"133.189.219.60\",\"RequesterParty\":\"MC\",\"RequesterUserID\":\"WSS-m901245\"}");
            // urlConn.setRequestProperty("Authorization", "Basic eTA1MzI4NDpKdTVxbXZCOQ==");
            urlConn.setRequestProperty("User-Agent", "Jersey/2.3.1 (HttpUrlConnection 1.6.0_24)");
            // urlConn.setRequestProperty("Host", "api.wf.mazda.co.jp");
            // urlConn.setRequestProperty("Accept", "text/html, image/gif, image/jpeg, */*; q=.2");
            // urlConn.setRequestProperty("VIPRIONLOOPCOUNT", "1");
            // urlConn.setRequestProperty("ECID-Context", "1.005IY1CYB77Fk3ti4dv1CU0002G0000EbU;kXjE9ZDLIPHCrF9OkPRBqHFBhLPRYLQSnVAPr6QCl9CPaTAPr3CEh4DOq6DBs7EPYT9Dl9CPs4CE^KFOXKRB^LRSoPQRXKTQjUPJ_TQO_IVS");
            // urlConn.setRequestProperty("Connection", "Keep-Alive");
            // urlConn.setRequestProperty("Proxy-Client-IP", "133.189.219.60");
            // urlConn.setRequestProperty("X-Forwarded-For", "133.189.219.60");
            // urlConn.setRequestProperty("X-WebLogic-KeepAliveSecs", "30");
            // urlConn.setRequestProperty("X-WebLogic-Force-JVMID", "-248232668");
            urlConn.setDoOutput(true);

            String url = urlConn.getURL().toString();
            printer.printToConsole(url);
            Map<String, List<String>> requestHeaders = urlConn.getRequestProperties();
            StringBuilder sb = new StringBuilder();
            for (Entry<String, List<String>> entry : requestHeaders.entrySet()) {
                sb.append(entry.getKey() + " : " + entry.getValue() + LS);
            }
            printer.printToConsole(sb.toString());
            if (config.isPrintToFile()) {
                printer.printToFile(url);
                printer.printToFile(sb.toString());
            }

            urlConn.connect();

            String headerStr = getHeaderStr(urlConn);
            String bodyStr = printBody(urlConn);
            String border = printBorder();
            String content = headerStr + bodyStr + border;

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

    private String printBorder() {
        return LS + "----------------------------" + LS;
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
