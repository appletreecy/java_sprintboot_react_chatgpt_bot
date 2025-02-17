package scaocoding.com.helloworld;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    private final ChatGPTService chatGPTService;

    // Constructor injection for ChatGPTService
    public HelloController(ChatGPTService chatGPTService){
        this.chatGPTService = chatGPTService;
    }

    @GetMapping("/form")
    public String showForm() {
        return "form"; // This is fine for serving the initial HTML
    }

    /**
     * Handles form submissions from the frontend
     * Sends the user's input to ChatGPT and returns the response.
     *
     * @param name The user's input (message) from the frontend.
     * @return ChatGPT's response as a plain text string.
     */

    @PostMapping("/submit")
    @ResponseBody // Add this annotation to return plain text/JSON
    public String handleFormSubmission(@RequestParam String name) {
        // Process the name (e.g., save to database, perform validation, etc.)
       if (name == null || name.trim().isEmpty()){
           return "Please enter a valid message";
       }

       // Create a prompt for ChatGPT
        String prompt = "The user said" + name + ". Respond in a friendly way.";

       // Get ChatGPT's response
        String chatGPTResponse = chatGPTService.getChatGPTResponse(prompt);


        // Return a plain text response
        return chatGPTResponse;
    }
}
