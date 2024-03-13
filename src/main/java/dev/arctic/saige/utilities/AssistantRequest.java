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
import dev.arctic.saige.events.AiDataUpdateEvent;
import dev.arctic.saige.events.AiGoalUpdateEvent;
import dev.arctic.saige.listener.AiDataUpdateEventListener;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.logging.Level;

import static dev.arctic.saige.SaiGE.character;
import static dev.arctic.saige.SaiGE.plugin;

public class AssistantRequest {

    private final String token = SaiGE.API_KEY;
    private final String defaultID = "asst_vRIn1LqURjkzUSyjaB24TJPR";

    public void createRetrieveRunAsync(String assistantID, String threadID, String input) {
        new BukkitRunnable() {

            @Override
            public void run() {
                OpenAiService service = new OpenAiService(token, Duration.ofMinutes(1));
                String assID = assistantID == null ? defaultID : assistantID;
                Assistant assistant = service.retrieveAssistant(assID);


                Thread thread = threadID == null ? service.createThread(ThreadRequest.builder().build()) : service.retrieveThread(threadID);

                Message message = service.createMessage(thread.getId(), MessageRequest.builder().content(input + "| personality: " + SaiGE.character.getCharacterAsJSON()).build());

                RunCreateRequest runCreateRequest = RunCreateRequest.builder()
                        .assistantId(assistant.getId()).build();

                Run run = service.createRun(thread.getId(), runCreateRequest);

                waitForRunCompletionAsync(service, thread.getId(), run.getId());
                String intput = input;
                if (intput.equals("intialize")) {
                    SaiGE.commonThread = thread.getId();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    private void waitForRunCompletionAsync(OpenAiService service, String threadId, String runId) {

        new BukkitRunnable() {
            public String output;

            public void run() {
                Run retrievedRun = service.retrieveRun(threadId, runId);
                if (!"completed".equals(retrievedRun.getStatus()) && !"failed".equals(retrievedRun.getStatus()) && !"requires_action".equals(retrievedRun.getStatus())) {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> waitForRunCompletionAsync(service, threadId, runId), 10);
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

                processResponse(output);
            }
        }.runTaskAsynchronously(plugin);
    }

    public void processResponse(String output) {
        plugin.getLogger().log(Level.INFO, output);
        String[] parts = output.split(":");
        if (parts.length < 2) return;

        String key = parts[0].trim();
        String value = parts[1].trim();

        switch (key) {
            case "\"goal\"", "goal":
                AiGoalUpdateEvent goalEvent = new AiGoalUpdateEvent(value);
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(goalEvent));
                break;
            case "\"data\"","data":
                AiDataUpdateEvent dataEvent = new AiDataUpdateEvent(output);
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(dataEvent));
                break;
            default:
                Audience audience = Audience.audience(Bukkit.getOnlinePlayers());
                Component message = Component.text().content(SaiGE.getCharacter().getName() + " » " + value).color(TextColor.color(0xfcdb03)).build();
                audience.sendMessage(message);
                break;
        }
    }
}
