package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

    private List<String> messages = new ArrayList<>();

    @GetMapping("/msg")
    public String home() {
        return "歡迎！請使用 /add?msg=內容 留言，或使用 /list 查看。";
    }

    @GetMapping("/add")
    public String addMessage(@RequestParam String msg) {
        messages.add(msg);
        return "成功儲存訊息：" + msg;
    }

    @GetMapping("/list")
    public String listMessages() {
        return "目前的留言內容：" + messages.toString();
    }
}