package com.example.user.config;

import com.example.utils.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalErrorController {
    @RequestMapping("/error")
    public Response error() {
        return new Response(4004);
    }
}
