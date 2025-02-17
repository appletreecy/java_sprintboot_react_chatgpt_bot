package scaocoding.com.helloworld;

import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGPTService {
    private final OpenAiService openAiService;

    /**
     * Constructor for ChatGPTService.
     * Initializes the OpenAiService with the provided API key.
     */
    public ChatGPTService() {

        // Load environment variables from .env
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("OPENAI_API_KEY");

        // Creating service with increased timeout
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(30));
    }

    /**
     * Sends a prompt to ChatGPT and returns the response.
     *
     * @param prompt The input prompt for ChatGPT.
     * @return ChatGPT's response as a string.
     * @throws RuntimeException If there's an error calling the OpenAI API.
     */
    public String getChatGPTResponse(String prompt) {
        // Log the prompt being sent to OpenAI
        System.out.println("Sending prompt to OpenAI: " + prompt);

        // Create a list of messages
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("user", prompt));

        // Create a chat completion request
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(messages)
                .model("gpt-4o-mini") // Use the current chat model
                .maxTokens(1000) // Limit response length
                .temperature(0.7) // Controls randomness (0 = deterministic, 1 = creative)
                .build();

        try {
            // Send the request to OpenAI and get the response
            ChatCompletionResult result = openAiService.createChatCompletion(chatCompletionRequest);
            String response = result.getChoices().get(0).getMessage().getContent();

            // Log the response received from OpenAI
            System.out.println("Received response from OpenAI: " + response);

            // Return the response
            return response;
        } catch (Exception e) {
            // Log the error and throw a runtime exception
            System.err.println("Error calling OpenAI API: " + e.getMessage());
            throw new RuntimeException("Failed to get response from OpenAI", e);
        }
    }
}