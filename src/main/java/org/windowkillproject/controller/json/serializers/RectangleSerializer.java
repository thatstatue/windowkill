package org.windowkillproject.controller.json.serializers;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import java.awt.Rectangle;
import java.io.IOException;

public class RectangleSerializer extends JsonSerializer<Rectangle> {
    @Override
    public void serialize(Rectangle rect, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("x", rect.getX());
        gen.writeNumberField("y", rect.getY());
        gen.writeNumberField("width", rect.getWidth());
        gen.writeNumberField("height", rect.getHeight());
        // Ignore bounds2D to avoid recursion
        gen.writeEndObject();
    }
}

