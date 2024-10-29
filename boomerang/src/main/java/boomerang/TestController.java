package boomerang;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/home")
    public String getHome() {
        return "home";
    }

    @GetMapping("/welcome")
    public String getWelcome() {
        return "welcome";
    }

}
