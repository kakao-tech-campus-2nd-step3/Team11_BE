package boomerang;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class UiController {

    @GetMapping
    public String getHome(Model model) {
        return "home";
    }

    @GetMapping("/mentor")
    public String getMentor(Model model) {
        return "mentor";
    }

    @GetMapping("/room/{roomId}")
    public String getChatRoomPage(@PathVariable Long roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "chat_room";
    }
}
