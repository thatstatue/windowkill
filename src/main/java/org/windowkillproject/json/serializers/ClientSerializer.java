package org.windowkillproject.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.windowkillproject.client.GameClient;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ClientSerializer extends JsonSerializer<GameClient> {

    @Override
    public void serialize(GameClient gameClient, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        // Serialize fields as needed. Do not serialize transient fields.
       // gen.writeStringField("app", gameClient.getApp().toString()); // Adjust as necessary
        gen.writeStringField("playerState", gameClient.getPlayerState().name()); // Adjust as necessary
        gen.writeStringField("username", gameClient.getUsername());
        gen.writeEndObject();
    }
}

