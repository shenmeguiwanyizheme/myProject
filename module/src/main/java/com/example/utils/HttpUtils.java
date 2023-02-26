package com.example.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

//万一不用protobuf，就用http呢
public class HttpUtils {
    public String doGet(String httpUrl) {
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(60000);
            urlConnection.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (urlConnection.getResponseCode() == 200) {
                is = urlConnection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder builder = new StringBuilder();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    builder.append(temp);
                    builder.append("\r\n");
                }
                result = builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            urlConnection.disconnect();
        }//有异常和没异常都可以正常关闭的
        return result;
    }

    public String doPost(String httpUrl, String param) {//返回值为响应数据的字节流（可以是一开始是json，xml，protobuf，然后被转换成字节流）
        InputStream is = null;
        BufferedReader br = null;
        HttpURLConnection urlConnection = null;
        URL url;
        String result = null;
        OutputStream os = null;
        try {
            url = new URL(httpUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(60000);
            urlConnection.setDoOutput(true);
//值得注意的是，这是在设置参数格式，这种content-type是form表单提交的时候的默认格式，参数是name1=value1&name2=value2的形式

            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            os = urlConnection.getOutputStream();
            os.write(param.getBytes(StandardCharsets.UTF_8));
            urlConnection.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (urlConnection.getResponseCode() == 200) {

                is = urlConnection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String temp;
                StringBuilder builder = new StringBuilder();
                while ((temp = br.readLine()) != null) {
                    builder.append(temp).append("\r\n");
                }
                result = br.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            urlConnection.disconnect();
        }
        if (null != is) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (null != os) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    //反正按照和资源创建相反的顺序来就行
}
