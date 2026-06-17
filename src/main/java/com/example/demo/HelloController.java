package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

    // 用一個簡單的 List 當作記憶體資料庫
    private List<String> messages = new ArrayList<>();

    @GetMapping("/add")
    public String addMessage(@RequestParam String msg) {
        messages.add(msg);
        return "成功儲存訊息: " + msg;
    }

    @GetMapping("/list")
    public String listMessages() {
        return "目前的留言板內容: " + messages.toString();
    }
}