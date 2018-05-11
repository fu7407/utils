package com.zzf.common.googlepr;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import sun.misc.BASE64Encoder;
/**
 * Copyright 2008
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class SimpleWebClient {
    /**
     * 向指定url发�?请求并获得响应数�?
     * 
     * @param urlString
     * @return
     * @throws IOException
     */
    public static String getRequestHttp(String urlString) throws IOException {
        return getRequestHttp(urlString, "utf-8");
    }
    /**
     * 向指定url发�?请求并获得响应数�?
     * 
     * @param urlString
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String getRequestHttp(String urlString, String encoding)
            throws IOException {
        return getRequestHttp(urlString, encoding, null, 5000);
    }
    /**
     * 向指定url发�?请求并获得响应数�?
     * 
     * @param urlString
     * @param encoding
     * @param parameter
     * @return
     * @throws IOException
     */
    public static String getRequestHttp(final String urlString,
            final String encoding, final Map parameter, final int timeout)
            throws IOException {
        String nURL = (urlString.startsWith("http://") || urlString
                .startsWith("https://")) ? urlString : ("http:" + urlString)
                .intern();
        String user = null;
        String password = null;
        String method = "GET";
        String post = null;
        String digest = null;
        String responseContent = "ERROR";
        boolean foundRedirect = false;
        Map headers = new HashMap();
        if (parameter != null) {
            Set entrySet = parameter.entrySet();
            for (Iterator it = entrySet.iterator(); it.hasNext();) {
                Entry header = (Entry) it.next();
                String key = (String) header.getKey();
                String value = (String) header.getValue();
                if ("user".equals(key)) {
                    user = value;
                } else if ("pass".equals(key)) {
                    password = value;
                } else if ("method".equals(key)) {
                    method = value;
                } else if ("post".equals(key)) {
                    post = value;
                } else {
                    headers.put(key, value);
                }
            }
        }
        URL url = new URL(nURL);
        if (user != null && password != null) {
            BASE64Encoder base64 = new BASE64Encoder();
            digest = "Basic "
                    + base64.encode((user + ":" + password).getBytes());
        }
        do {
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            // 添加访问授权
            if (digest != null) {
                urlConnection.setRequestProperty("Authorization", digest);
            }
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod(method);
            if (timeout > 0) {
                urlConnection.setConnectTimeout(timeout);
            }
            //模拟http头文�?
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0;)");
            urlConnection.setRequestProperty("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, */*");
            //追加http头文�?
            Set headersSet = headers.entrySet();
            for (Iterator it = headersSet.iterator(); it.hasNext();) {
                Entry entry = (Entry) it.next();
                urlConnection.setRequestProperty((String) entry.getKey(),
                        (String) entry.getValue());
            }
            if (post != null) {
                OutputStreamWriter outRemote = new OutputStreamWriter(
                        urlConnection.getOutputStream());
                outRemote.write(post);
                outRemote.flush();
            }
            // 获得响应状�?
            int responseCode = urlConnection.getResponseCode();
            // 获得返回的数据长�?
            int responseLength = urlConnection.getContentLength();
            if (responseCode == 302) {
                // 重定�?
                String location = urlConnection.getHeaderField("Location");
                url = new URL(location);
                foundRedirect = true;
            } else {
                BufferedInputStream in;
                if (responseCode == 200 || responseCode == 201) {
                    in = new BufferedInputStream(urlConnection.getInputStream());
                } else {
                    in = new BufferedInputStream(urlConnection.getErrorStream());
                }
                int size = responseLength == -1 ? 4096 : responseLength;
                if (encoding != null) {
                    responseContent = SimpleWebClient.read(in, size, encoding);
                } else {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] bytes = new byte[size];
                    int read;
                    while ((read = in.read(bytes)) >= 0) {
                        out.write(bytes, 0, read);
                    }
                    responseContent = new String(out.toByteArray());
                    in.close();
                    out.close();
                }
                foundRedirect = false;
            }
            // 如果重定向则继续
        } while (foundRedirect);
        return responseContent;
    }
    /**
     * 转化InputStream为String
     * 
     * @param in
     * @param size
     * @return
     * @throws IOException
     */
    private static String read(final InputStream in, final int size,
            final String encoding) throws IOException {
        StringBuilder sbr = new StringBuilder();
        int nSize = size;
        if (nSize == 0) {
            nSize = 1;
        }
        char[] buffer = new char[nSize];
        int offset = 0;
        InputStreamReader isr = new InputStreamReader(in, encoding);
        while ((offset = isr.read(buffer)) != -1) {
            sbr.append(buffer, 0, offset);
        }
        in.close();
        isr.close();
        return sbr.toString();
    }
}
