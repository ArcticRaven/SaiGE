package dev.arctic.saige.character;

import com.google.gson.Gson;
import dev.arctic.saige.SaiGE;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.logging.Level;

import static dev.arctic.saige.SaiGE.plugin;

@Data
@Getter
@Setter
public class Character {
    public String name;
    public String personality;
    public String language;
    public String[] memories;

    public Character(String name, String personality, String language, String[] memories) {
        this.name = name;
        this.personality = personality;
        this.language = language;
        this.memories = memories;
    }

    public static Character getDefaultCharacter() {

        return new Character("SaiGE", "neutral and supportive", "Modern english, professional", new String[0]);
    }

    public String getCharacterAsJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void setCharacterFromJSON(String json) {
        plugin.getLogger().info("Attempting to parse JSON: " + json);
        Gson gson = new Gson();
        try {
            Character character = gson.fromJson(json, Character.class);
            SaiGE.setCharacter(character);
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.WARNING, "Error updating character data from JSON");
        }
    }

}
