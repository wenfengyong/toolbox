package com.wenfengyong.toolbox.server.interfaces;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wenfengyong.toolbox.server.common.advice.ResponseResultBody;

import lombok.Data;

/**
 * 测试的controller
 * @author fywen
 * @date 2021/2/1 11:12
 */
@RestController
@ResponseResultBody
@RequestMapping("/test")
public class TestController {
    

    // todo 直接返回string 类型会报ClassCastException，需要处理
    @GetMapping("/hello")
    public Object hello(String name) {
        if (StringUtils.isBlank(name)) {
            name = "stranger";
        }
        return String.format("Hello, %s!", name);
    }
    
    @GetMapping("/helloError")
    public HashMap<String, Object> helloError() throws Exception {
        throw new Exception("helloError");
    }
    
    @GetMapping("/helloMyError")
    public HashMap<String, Object> helloMyError() {
        throw new RuntimeException("test");
    }
    
    @GetMapping("/helloObject")
    public InnerClassTest helloObject() {
        return new InnerClassTest();
    }
    
    @Data
    private static class InnerClassTest {
        private String name = "123";
    }
}
