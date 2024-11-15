package boomerang;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/home")
    public String getHome() {
        return "home";
    }

    @GetMapping("/welcome")
    public String getWelcome() {
        return "welcome";
    }


    @GetMapping("/file-page")
    public String getFilePage() {
        return "file.html";
    }
}
