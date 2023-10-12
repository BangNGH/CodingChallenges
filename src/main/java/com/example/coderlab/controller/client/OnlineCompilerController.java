package com.example.coderlab.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OnlineCompilerController {
    @GetMapping("onlineCompiler")
    public String index(){
        return "client/online_compiler/index";
    }
}
