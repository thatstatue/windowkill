package org.windowkillproject.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageSerializer extends JsonSerializer<BufferedImage> {
    @Override
    public void serialize(BufferedImage img, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("width", img.getWidth());
        gen.writeNumberField("height", img.getHeight());
        gen.writeStringField("type", getImageType(img.getType()));
        // Serialize other properties if needed
        gen.writeEndObject();
    }

    private String getImageType(int type) {
        switch (type) {
            case BufferedImage.TYPE_INT_RGB:
                return "TYPE_INT_RGB";
            case BufferedImage.TYPE_INT_ARGB:
                return "TYPE_INT_ARGB";
            default:
                return "UNKNOWN";
        }
    }
}
