package com.casic.cloud.hyperloop.common.utils;

import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: LDC
 * @Date: 2019/12/27 15:54
 * @version: V1.0
 */
public class FileUploadUtil {
    private static String TOWHYPHENS = "---";
    private static String BOUNDARY = "####################";
    private static String END = "\r\n";

    public static void Upload2RemoteServer(String server, List<String> pathList) {
        try {
            URL url = new URL(server);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);//设置允许输出
            connection.setDoInput(true);//设置允许输入
            connection.setUseCaches(false);//不使用缓存
            connection.setRequestMethod(RequestMethod.POST.name());
            connection.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARY);
            //将所有文件写入输入流
            setFile2Stream(pathList, connection);

            //获取响应
            callBack(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void callBack(HttpURLConnection connection) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = "";
        StringBuffer sb = new StringBuffer();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        System.out.println("响应----------" + sb.toString());
        br.close();
    }

    private static void setFile2Stream(List<String> pathList, HttpURLConnection connection) throws Exception {
        OutputStream outputStream = connection.getOutputStream();
        DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(outputStream));

        for (String e : pathList) {
            String fileName = e.substring(e.lastIndexOf("/") + 1);
            //写入分隔符
            writer.writeBytes(TOWHYPHENS + BOUNDARY + END);
            //写入文件名
            writer.writeBytes("Content-Disposition:form-data;name=file;filename=" + fileName + END);
            //写出结束标志
            writer.writeBytes(END);
            //获取文件流
            File file = new File(e);
            FileInputStream in = new FileInputStream(file);
            int contentLength = ((Long)file.length()).intValue();
            byte[] bytes = new byte[1024 * 10];
            int len;
            BigDecimal size = new BigDecimal("0");
            while ((len = in.read(bytes)) != -1) {
                writer.write(bytes, 0, len);
                //进度条，精度3
                size = printProgressBar(contentLength, len, size, 3);
            }
            in.close();
            //每完成一个文件，写出一次结束标志
            writer.writeBytes(END);
        }
        //写入分隔符
        writer.writeBytes(TOWHYPHENS + BOUNDARY + END);
        writer.flush();
        int responseCode = connection.getResponseCode();//调用此方法才会将缓存中数据真正上传
        System.out.println("responseCode----------" + responseCode);
        System.out.println("responseMessage----------" + connection.getResponseMessage());
        writer.close();
    }


    private static void downloadFromServer(String server, String path) {
        try {
            URL url = new URL(server);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);//设置允许输出
            connection.setDoInput(true);//设置允许输入
            connection.setUseCaches(false);//不使用缓存

            download(path, connection);//下载

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void download(String path, HttpURLConnection connection) throws IOException {
        //获取内容长度
        int contentLength = connection.getContentLength();
        //获取文件路径
        String filePath = connection.getURL().getFile();
        String fileName = filePath.substring(filePath.lastIndexOf("/"));
        String downLoad = path + fileName;//文件下载路径

        File file = new File(downLoad);
        //创建父目录
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        byte[] bytes = new byte[1024 * 10];
        int len;
        BigDecimal size = new BigDecimal("0");
        while ((len = bis.read(bytes)) != -1) {
            bos.write(bytes, 0, len);
            //进度条，精度3
            size = printProgressBar(contentLength, len, size, 3);
        }

        bis.close();
        bos.close();
    }

    /**
     *
     * @param contentLength 文件总长度
     * @param len
     * @param size
     * @param precision 精度
     * @return
     */
    public static BigDecimal printProgressBar(int contentLength, int len, BigDecimal size ,int precision) {
        size = size.add(BigDecimal.valueOf(len));
        //乘100
        BigDecimal multiply = size.multiply(new BigDecimal("100"));
        //除总长度，精度取三，策略四舍五入
        BigDecimal divide = multiply.divide(BigDecimal.valueOf(contentLength),precision, BigDecimal.ROUND_HALF_UP);
        System.out.println("传输进度: " + divide + "%\n");
        return size;
    }


    public static void main(String[] args) {
        String server = "http://192.168.3.214:80/tools";
        String filePath = "C:/Users/AnglIng/Desktop/dubbo/gateway/gateway-0.0.1-SNAPSHOT.jar";
        List pathList = new ArrayList();
        pathList.add(filePath);
        Upload2RemoteServer(server, pathList);

        String path = "F:\\";
        server = "http://192.168.3.214/tools/jdk-8u144-linux-arm64-vfp-hflt.tar.gz";
        downloadFromServer(server, path);
    }

}
