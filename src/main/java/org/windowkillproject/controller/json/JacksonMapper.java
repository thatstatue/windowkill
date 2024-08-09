package org.windowkillproject.controller.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.windowkillproject.controller.json.deserializers.BufferedImageDeserializer;
import org.windowkillproject.controller.json.deserializers.PolygonDeserializer;
import org.windowkillproject.controller.json.deserializers.RectangleDeserializer;
import org.windowkillproject.controller.json.serializers.BufferedImageSerializer;
import org.windowkillproject.controller.json.serializers.PolygonSerializer;
import org.windowkillproject.controller.json.serializers.RectangleSerializer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class JacksonMapper extends ObjectMapper{
    private static JacksonMapper mapper;



    public static JacksonMapper getInstance(){
        if (mapper==null) {
            mapper = new JacksonMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            SimpleModule module = new SimpleModule();

            module.addSerializer(Polygon.class, new PolygonSerializer());
            module.addDeserializer(Polygon.class, new PolygonDeserializer());

            module.addSerializer(BufferedImage.class, new BufferedImageSerializer());
            module.addDeserializer(BufferedImage.class, new BufferedImageDeserializer());

            module.addSerializer(Rectangle.class, new RectangleSerializer());
            module.addDeserializer(Rectangle.class, new RectangleDeserializer());

//            module.addSerializer(GameClient.class, new ClientSerializer());
//            module.addDeserializer(GameClient.class, new ClientDeserializer());

            mapper.registerModule(module);
            mapper.addMixIn(Component.class, ComponentMixin.class);
        }
        return mapper;
    }
}
