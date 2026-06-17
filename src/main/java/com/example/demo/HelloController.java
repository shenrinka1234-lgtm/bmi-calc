@RestController
public class HelloController {

    private List<String> messages = new ArrayList<>();

    // 確保這裡只有這一個 home() 方法
    @GetMapping("/")
    public String home() {
        return "歡迎！請使用 /add?msg=內容 留言，或使用 /list 查看。";
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
}