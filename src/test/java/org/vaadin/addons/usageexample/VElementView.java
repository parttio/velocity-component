package org.vaadin.addons.usageexample;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.velocitycomponent.VElement;

@Route
public class VElementView extends VerticalLayout {

        public record FooBar(String foo, String bar, String car) {}

        public VElementView() {
            add(new H1("Example to listen DOM events easily!"));

            VElement.of(getElement()).on(FooBar.class, event -> {
                String bar = event.bar();
                add(new Paragraph("Received foo-bar event with detail: " + event));
            });
            
            add(new Button("Click me", e -> {
                    getElement().executeJs("""
                        this.dispatchEvent(new CustomEvent('foo-bar', {
                            detail: {
                                foo: 'foo',
                                bar: 'bar',
                                car: 'car'
                            }
                        }));
                    """);
            }));

            add(new Button("Click me too", e -> {
                    // This is in theory bit more efficient for the server
                    // as it does not need to deserialize-serialize (with GWT library)
                    // and then desirialize with Jackson, but only once with Jackson
                    getElement().executeJs("""
                        this.dispatchEvent(new CustomEvent('foo-bar', {
                            detail: JSON.stringify({
                                foo: 'foo',
                                bar: 'bar',
                                car: 'car'
                            })
                        }));
                    """);
            }));

        }
}
