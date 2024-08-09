package org.windowkillproject.controller.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.windowkillproject.client.GameClient;
import org.windowkillproject.server.connections.online.PlayerState;

import java.io.IOException;

public class ClientDeserializer extends JsonDeserializer<GameClient> {

    @Override
    public GameClient deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        // Create a new GameClient instance
        GameClient gameClient = new GameClient(); // Assuming a default constructor is available

        // Parse JSON and set fields
        JsonNode node = p.getCodec().readTree(p);
       // String appString = node.get("app").asText();
        PlayerState playerState = PlayerState.valueOf(node.get("playerState").asText());
        String username = node.get("username").asText();

        // Set fields accordingly
        // You might need to handle additional logic for 'app' and other fields

       // gameClient.setApp(appString); // Adjust according to your setter
        gameClient.setPlayerState(playerState); // Adjust according to your setter
        gameClient.setUsername(username); // Adjust according to your setter

        return gameClient;
    }
}
