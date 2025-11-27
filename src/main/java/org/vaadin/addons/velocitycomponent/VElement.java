package org.vaadin.addons.velocitycomponent;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.dom.DomListenerRegistration;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;
import org.apache.commons.lang3.StringUtils;
import tools.jackson.databind.ObjectMapper;

import static java.util.Arrays.stream;

public class VElement {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private final Element element;

    private VElement(Element element) {
        this.element = element;
    }

    public static VElement body() {
        return new VElement(UI.getCurrent().getElement());
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
    public <T> DomListenerRegistration on(Class<T> eventType, SerializableConsumer<T> listener) {
        String simpleName = eventType.getSimpleName();
        if(simpleName.endsWith("Event")) {
            simpleName = simpleName.substring(0, simpleName.length() - 5);
        }
        String eventName = stream(StringUtils.splitByCharacterTypeCamelCase(simpleName))
                .map(s -> s.toLowerCase())
                .reduce((a, b) -> a + "-" + b).get();

        return element.addEventListener(eventName, event -> {
            T value = event.getEventDetail(eventType);
            listener.accept(value);
        }).addEventDetail();
    }

    /**
     * Listen to a custom client side event and receive the payload in "event.detail".
     * If the event type/payload is not String, Integer, Double or Boolean. byte[] can be used
     * if the client side sends the data as a base64 encoded string.
     *<p>
     * Anything else is expected to be a JSON and deserialized to the event type using
     * Jackson ObjectMapper.
     *</p>
     *
     * <p>
     * On the client side, the event should be dispatched with a CustomEvent with the detail property.
     * </p>
     *
     * @param eventName the name of the event
     * @param eventType the DTO of the "event.detail"
     * @param listener the listener to be called when the event is fired
     * @return a registration that can be used to remove the listener
     * @param <T> the type of the event
     */
    public <T> DomListenerRegistration on(String eventName, Class<T> eventType, SerializableConsumer<T> listener) {
        return element.addEventListener(eventName, event -> {
            T value = event.getEventDetail(eventType);
            listener.accept(value);
        }).addEventDetail();
    }

}
