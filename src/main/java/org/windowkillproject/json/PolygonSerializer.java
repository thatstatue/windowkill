package org.windowkillproject.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Field;

public class PolygonSerializer extends JsonSerializer<Polygon> {
    @Override
    public void serialize(Polygon polygon, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("xpoints");
        gen.writeArray(polygon.xpoints, 0, polygon.npoints);
        gen.writeFieldName("ypoints");
        gen.writeArray(polygon.ypoints, 0, polygon.npoints);
        gen.writeNumberField("npoints", polygon.npoints);

        try {
            Field boundsField = Polygon.class.getDeclaredField("bounds");
            boundsField.setAccessible(true);
            Rectangle bounds = (Rectangle) boundsField.get(polygon);
            gen.writeObjectField("bounds", bounds);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        gen.writeEndObject();
    }
}
