package org.windowkillproject.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Field;

public class PolygonDeserializer extends JsonDeserializer<Polygon> {
    @Override
    public Polygon deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        int[] xpoints = getArrayFromJsonNode(node.get("xpoints"));
        int[] ypoints = getArrayFromJsonNode(node.get("ypoints"));
        int npoints = node.get("npoints").asInt();

        Polygon polygon = new Polygon(xpoints, ypoints, npoints);

        JsonNode boundsNode = node.get("bounds");
        if (boundsNode != null) {
            Rectangle bounds = p.getCodec().treeToValue(boundsNode, Rectangle.class);
            try {
                Field boundsField = Polygon.class.getDeclaredField("bounds");
                boundsField.setAccessible(true);
                boundsField.set(polygon, bounds);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return polygon;
    }

    private int[] getArrayFromJsonNode(JsonNode node) {
        int[] array = new int[node.size()];
        for (int i = 0; i < node.size(); i++) {
            array[i] = node.get(i).asInt();
        }
        return array;
    }
}
