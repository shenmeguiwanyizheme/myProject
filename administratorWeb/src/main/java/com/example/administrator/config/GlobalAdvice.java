package com.example.administrator.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice(basePackages = "com.example")
@Slf4j
public class GlobalAdvice {
    @ExceptionHandler(value = RuntimeException.class)
    public Response runtimeExceptionGlobalAdvice(RuntimeException exception) {//由spring MVC自动填充参数
        log.error("出错啦");
        //利用exception  Message传递数据
        Response response = new Response();
        String jsonMsg = exception.getMessage();
        JSONObject jsonObject = JSON.parseObject(jsonMsg);//返回值一定是一个jsonObject，parse返回的是object
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            if (entry.getKey().equals("code")) {
                response.setCode((int) entry.getValue());
            } else {
                HashMap<String, Object> map = (HashMap<String, Object>) entry.getValue();
                if (map.size() != 0) {
                    response.setResult(entry.getValue());
                }
            }
        }
        return response;
    }

//    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
//    public ResponseEntity<String> mediaTypeNotAcceptable(HttpServletRequest request) {
//        return ResponseEntity.status(404).build();
//    }
//
//    @RequestMapping(value = "${server.error.path:${error.path:/error}}")
//
//    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
//
//        Map<String, Object> map = new HashMap<String, Object>(16);
//        String error = "路径有误，请操作后重试";
//        StringJoiner joiner = new StringJoiner(",", "[", "]");
//        joiner.add(error);
//        map.put("rtnCode", "9999");
//        map.put("rtnMsg", joiner.toString());
//        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @RequestMapping("/error")
//    public String error() {
//        return "error";
//    }
//
//    public String getErrorPath() {
//        return "/error";
//    }

}








