package com.usc.app.ims.config.action.group;

import com.usc.obj.api.USCObject;
import com.usc.obj.util.USCObjectQueryHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lwp
 * @DATE: 2019/11/18 15:12
 * @Description: 上传群组头像以及获取群组头像
 **/
@RestController
@RequestMapping(value = "/src/chatGroup")
public class GroupAvatar {

    @PostMapping(value = "/uploadAvatar")
    public Map<String, Object> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Map<String, Object> param = new HashMap<>();
        if (file != null) {
            try {
                // 获取系统的”/“（系统不同斜杠会不一致，比如部署在linux中会报错）
                String separator = File.separator;
                // 获取项目根目录路径
                String dir = System.getProperty("user.dir");
                // 存储路径
                File pFile = new File(dir + separator + "avatar");
                // 创建一个文件
                String fileName = file.getOriginalFilename();
                File fileDir = new File(pFile, fileName);
                // 判断是否有该文件夹路径，没有则新建
                if (!pFile.exists()) {
                    pFile.mkdirs();
                }
                // 把接收的文件放入到文件中
                file.transferTo(fileDir.toPath());
                param.put("code", 0);
                param.put("fileName", file.getOriginalFilename());
                param.put("msg", "上传成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            param.put("code", 1);
            param.put("msg", "上传失败");
        }
        return param;
    }

    @GetMapping(value = "/getAvatar/{groupId}")
    public byte[] uploadAvatar(@PathVariable("groupId") String groupId) throws IOException {
        File file;
        String separator = File.separator;
        String dir = System.getProperty("user.dir");

        USCObject user = USCObjectQueryHelper.getObjectByID("CHAT_GROUP", groupId);
        String fileName = user.getFieldValueToString("photo");
        if (fileName != null) {
            file = new File(dir + separator + "avatar" + separator + fileName);
        } else {
            file = new File(dir + separator + "avatar" + separator + "user.jpg");
        }

        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }

}
