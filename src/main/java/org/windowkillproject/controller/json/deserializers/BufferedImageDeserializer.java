package org.windowkillproject.controller.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.io.IOException;

public class BufferedImageDeserializer extends JsonDeserializer<BufferedImage> {
    @Override
    public BufferedImage deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        int width = node.get("width").asInt();
        int height = node.get("height").asInt();
        String type = node.get("type").asText();

        int imageType = getImageType(type);
        BufferedImage img = new BufferedImage(width, height, imageType);

        // Handle image data if necessary
        // For example, if you need to handle pixel data, you can include that in the JSON
        // and reconstruct the BufferedImage using that data.

        return img;
    }

    private int getImageType(String type) {
        switch (type) {
            case "TYPE_INT_RGB":
                return BufferedImage.TYPE_INT_RGB;
            case "TYPE_INT_ARGB":
                return BufferedImage.TYPE_INT_ARGB;
            default:
                return BufferedImage.TYPE_INT_ARGB;
        }
    }
}
