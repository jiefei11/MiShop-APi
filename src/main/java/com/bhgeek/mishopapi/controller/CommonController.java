package com.bhgeek.mishopapi.controller;

import com.bhgeek.mishopapi.common.RespBean;
import com.bhgeek.mishopapi.common.RespBeanEnum;
import com.bhgeek.mishopapi.utils.CosUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping()
public class CommonController {



    @PostMapping("/uploadImage")
    public RespBean upload(@RequestParam("file") MultipartFile file) throws Exception {

        //上传文件
        CosUtil client = new CosUtil();
        String url = client.uploadFile(file);
        return new RespBean(RespBeanEnum.SUCCESS,(Object) url);
    }

}
