package com.example.administrator.config;

import com.example.utils.Response;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalErrorController implements ErrorController {//虽然没什么鸟用，但是你还是标志一下比较好

    @RequestMapping("/error")
    public Response error() {
        return new Response(4004);
    }
}
