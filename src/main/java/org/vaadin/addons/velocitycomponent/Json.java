package org.vaadin.addons.velocitycomponent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A trivial helper class for JS components to render
 * Java DTOs as JSON. Exposed to Velocity templates via
 * VelocityComponentDecorator.
 */
public class Json {

    static ObjectMapper mapper = new ObjectMapper();

    /**
     * @param o the object to render as JSON
     * @return the JSON representation of the object
     */
    static public String of(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{'value': 'JSON ERROR'}";
        }
    }
}
