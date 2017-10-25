package com.edusys.manager.controller;

import com.alibaba.fastjson.JSON;
import com.edu.common.util.PropertiesFileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Gary on 2017/8/31.
 */
@Controller
@RequestMapping("/manage")
public class FileUploadController {
    private static final String FILE_PATH = "C:/uploaddir";

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(HttpServletRequest request, HttpServletResponse
            response) throws IOException {
        //String fileName = (String)request.getAttribute("filename");
        MultipartHttpServletRequest multipartRequest =
                (MultipartHttpServletRequest) request;
        Iterator<String> fileNames = multipartRequest.getFileNames();
        MultipartFile multipartFile = multipartRequest.getFile(fileNames.next());
        //如果使用firebug，或者chrome的开发者工具，可以看到，这个文件上传工具发送了两个文件名
        //分别是：name="Filedata"; filename="AVScanner.ini"
        //用这两个文件名获得文件内容都可以，只不过第一个没有后缀，需要自己处理
        //第二个是原始的文件名，但是这个文件名有可能是上传文件时的全路径
        //例如  C:/testssh/a.log，如果是全路径的话，也需要处理
        String fileAlias = multipartFile.getName();
        //获得文件原始名称
        String name = multipartFile.getOriginalFilename();
        //新的文件名
        String newName = new Date().getTime() + name.substring(name.indexOf("."), name.length());
        PropertiesFileUtil propertiesFileUtil = PropertiesFileUtil.getInstance("config");
        String realPath = propertiesFileUtil.get("uploadDir");
        String filePath = realPath + "/" + newName;
        saveFile(filePath, multipartFile.getBytes());
        Map<String, String> resultMap = new HashMap<String, String>(5);
        resultMap.put("result", "success");
        resultMap.put("newName", newName);
        return JSON.toJSONString(resultMap);
    }

    //保存文件的方法
    public void saveFile(String filePath, byte[] content) throws IOException {
        BufferedOutputStream bos = null;
        try {
            File file = new File(filePath);
            //判断文件路径是否存在
            if (!file.getParentFile().exists()) {
                //文件路径不存在时，创建保存文件所需要的路径
                file.getParentFile().mkdirs();
            }
            //创建文件（这是个空文件，用来写入上传过来的文件的内容）
            file.createNewFile();
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(content);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("文件不存在。");
        } finally {
            if (null != bos) {
                bos.close();
            }
        }
    }

    @RequestMapping(value = "/del-file", method = RequestMethod.POST)
    @ResponseBody
    public String fileDel(HttpServletRequest request, HttpServletResponse
            response, String filename) throws IOException {
        System.out.println(filename);
        PropertiesFileUtil propertiesFileUtil = PropertiesFileUtil.getInstance("config");
        String realPath = propertiesFileUtil.get("uploadDir");
        String filePath = realPath + "/" + filename;
        String result = "success";
        if(!delFile(filePath)) result = "fail";
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("result", result);
        return JSON.toJSONString(resultMap);
    }

    //删除文件
    public boolean delFile(String filePath) throws IOException {
        try{
            File file = new File(filePath);
            if(file.exists()){
                return file.delete();
            }
        }catch (Exception ex){
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        FileUploadController fu = new FileUploadController();
        try {
            fu.delFile("C:\\uploaddir\\1.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
