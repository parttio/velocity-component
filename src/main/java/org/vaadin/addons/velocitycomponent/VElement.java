package org.vaadin.addons.velocitycomponent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonType;
import elemental.json.JsonValue;
import org.apache.commons.lang3.StringUtils;

import static java.util.Arrays.stream;

public class VElement {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private final Element element;

    private VElement(Element element) {
        this.element = element;
    }

    public static VElement of(Element element) {
        return new VElement(element);
    }

    /**
     * Listen to an event of a specific type. The event type is determined by the class of the event,
     * the name can contain optional "Event" postfix which is ignored in DOM event name.
     * The event payload is expected to be a JSON object or a string and serialized to the event type
     * using Jackson ObjectMapper.
     *
     * On the client side, the event should be dispatched with a CustomEvent with the detail property.
     *
     * @param eventType the class of the event
     * @param listener the listener to be called when the event is fired
     * @return a registration that can be used to remove the listener
     * @param <T> the type of the event
     */
    public <T> Registration on(Class<T> eventType, SerializableConsumer<T> listener) {
        String simpleName = eventType.getSimpleName();
        if(simpleName.endsWith("Event")) {
            simpleName = simpleName.substring(0, simpleName.length() - 5);
        }
        String kebabCased = stream(StringUtils.splitByCharacterTypeCamelCase(simpleName))
                .map(s -> s.toLowerCase())
                .reduce((a, b) -> a + "-" + b).get();

        return element.addEventListener(kebabCased, event -> {
            JsonValue jsonValue = event.getEventData().get("event.detail");
            T value;
            if(jsonValue.getType() == JsonType.OBJECT) {
                try {
                    value = objectMapper.readValue(jsonValue.toJson(), eventType);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    value = objectMapper.readValue(jsonValue.asString().toString(), eventType);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            listener.accept(value);
        }).addEventData("event.detail");
    }
}
