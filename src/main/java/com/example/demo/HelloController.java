package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

    private List<String> messages = new ArrayList<>();

    // 當你直接打開網址時，會顯示這個首頁
    @GetMapping("/")
    public String home() {
        return "歡迎！請使用 /add?msg=內容 留言，或使用 /list 查看內容。";
    }

    @GetMapping("/add")
    public String addMessage(@RequestParam String msg) {
        messages.add(msg);
        return "成功儲存訊息: " + msg;
    }

    @GetMapping("/list")
    public String listMessages() {
        return "目前的留言板內容: " + messages.toString();
    }
    @GetMapping("/")
    public String home() {
        return "這是首頁！請使用 /add?msg=你的訊息 來留言，或使用 /list 查看。";
    }
}