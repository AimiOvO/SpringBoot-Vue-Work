package com.gyhr.config.file;

import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/img")
public class imageUpload {

    private String uploadPath = System.getProperty("user.dir") + "/images";

    /**
     * 上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResultVo<Object> upload(@RequestParam("file") MultipartFile file) {
        String url = null;
        // 获取文件名信息
        String filename = file.getOriginalFilename();
        // 获取拓展名
        String fileExtName = filename.substring(filename.lastIndexOf("."));
        // 生成新的文件名
        String newName = UUID.randomUUID().toString() + fileExtName;
        File fileDir = new File(uploadPath);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        File targetFile = new File(uploadPath, newName);
        try {
            file.transferTo(targetFile);
            url = "/" + targetFile.getName();
        } catch (IOException e) {
            return ResultUtils.success("上传失败");
        }
        return ResultUtils.success("上传成功","/images" + url);
    }

    /**
     * 删除
     * @param name
     * @return
     */
    public ResultVo<Object> delete(String name) {
        File file = new File(uploadPath+ "\\" + name);
        boolean delete = false;
        if (file.exists()) {
            delete = file.delete();
        }
        return  delete ? ResultUtils.success("删除成功") : ResultUtils.error("删除失败");    }

}
