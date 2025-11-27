package org.vaadin.addons.velocitycomponent;

import tools.jackson.databind.ObjectMapper;

/**
 * A trivial helper class for JS components to render
 * Java DTOs as JSON. Exposed to Velocity templates via
 * VelocityComponentDecorator.
 *
 * TODO figure out if this can now be removed altogether in V25
 */
public class Json {

    static ObjectMapper mapper = new ObjectMapper();

    /**
     * @param o the object to render as JSON
     * @return the JSON representation of the object
     */
    static public String of(Object o) {
        return mapper.writeValueAsString(o);
    }
}
