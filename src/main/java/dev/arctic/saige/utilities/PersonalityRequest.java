package dev.arctic.saige.utilities;

import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.assistants.Assistant;
import com.theokanning.openai.messages.Message;
import com.theokanning.openai.messages.MessageRequest;
import com.theokanning.openai.runs.Run;
import com.theokanning.openai.runs.RunCreateRequest;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.threads.Thread;
import com.theokanning.openai.threads.ThreadRequest;
import dev.arctic.saige.SaiGE;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.logging.Level;

import static dev.arctic.saige.SaiGE.plugin;

public class PersonalityRequest {

    private final String token = SaiGE.API_KEY;
    private final String defaultID = "asst_KmCegqPU9Uu3V18cfa7wsLEm";

    public void createRetrieveRunAsync(String input) {
        new BukkitRunnable() {

            @Override
            public void run() {
                OpenAiService service = new OpenAiService(token, Duration.ofMinutes(1));
                Assistant assistant = service.retrieveAssistant(defaultID);


                Thread thread =  service.createThread(ThreadRequest.builder().build());

                Message message = service.createMessage(thread.getId(), MessageRequest.builder().content(SaiGE.character.getCharacterAsJSON() + ":" + input).build());

                RunCreateRequest runCreateRequest = RunCreateRequest.builder()
                        .assistantId(assistant.getId()).build();

                Run run = service.createRun(thread.getId(), runCreateRequest);

                waitForRunCompletionAsync(service, thread.getId(), run.getId());
            }
        }.runTaskAsynchronously(plugin);
    }

    private void waitForRunCompletionAsync(OpenAiService service, String threadId, String runId) {

        new BukkitRunnable() {
            public String output;

            public void run() {
                Run retrievedRun = service.retrieveRun(threadId, runId);
                if (!"completed".equals(retrievedRun.getStatus()) && !"failed".equals(retrievedRun.getStatus()) && !"requires_action".equals(retrievedRun.getStatus())) {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> waitForRunCompletionAsync(service, threadId, runId), 20);
                    return;
                }

                OpenAiResponse<Message> response = service.listMessages(threadId);
                Message latestAssistantMessage = response.getData().stream()
                        .filter(message -> "assistant".equals(message.getRole()))
                        .findFirst()
                        .orElse(null);

                if (latestAssistantMessage != null) {
                    latestAssistantMessage.getContent().forEach(content -> {
                        this.output = content.getText().getValue();
                    });
                }
                try{
                    SaiGE.character.setCharacterFromJSON(output);
                    plugin.getLogger().log(Level.INFO, "Character updated from Request");
                    plugin.getLogger().log(Level.INFO, "Character: " + SaiGE.character.getCharacterAsJSON());
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Error updating character from Request");
                }
            }
        }.runTaskAsynchronously(plugin);

    }
}
