package org.vaadin.addons.usageexample;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.velocitycomponent.VElement;

@Route
public class VElementView extends VerticalLayout {

    public VElementView() {
        add(new H1("Example to listen DOM events easily!"));

        VElement.of(getElement()).on(FooBar.class, event -> {
            String bar = event.bar();
            add(new Paragraph("Received foo-bar event with detail: " + event));
        });
        // Event name can also be specified explicitly
        VElement.of(getElement()).on("foo-car", FooBar.class, event -> {
            String bar = event.bar();
            add(new Paragraph("Received foo-car event with detail: " + event));
        });
        VElement.of(getElement()).on("string-msg", String.class, event -> {
            add(new Paragraph("Received string-msg event with detail: " + event));
        });
        VElement.of(getElement()).on("boolean-msg", String.class, event -> {
            add(new Paragraph("Received boolean-msg event with detail: " + event));
        });
        VElement.of(getElement()).on("int-msg", Integer.class, event -> {
            add(new Paragraph("Received int-msg event with detail: " + event));
        });
        VElement.of(getElement()).on("double-msg", Double.class, event -> {
            add(new Paragraph("Received double-msg event with detail: " + event));
        });

        add(new HorizontalLayout(
                new Button("Click me", e -> {
                    getElement().executeJs("""
                                this.dispatchEvent(new CustomEvent('foo-bar', {
                                    detail: {
                                        foo: 'foo',
                                        bar: 'bar',
                                        car: 'car'
                                    }
                                }));
                            """);
                }),
                new Button("Fire string", e -> {
                    getElement().executeJs("""
                                this.dispatchEvent(new CustomEvent('string-msg', {
                                    detail: 'foo'
                                }));
                            """);
                }),
                new Button("Fire boolean", e -> {
                    getElement().executeJs("""
                                this.dispatchEvent(new CustomEvent('boolean-msg', {
                                    detail: true
                                }));
                            """);
                }),
                new Button("Fire int", e -> {
                    getElement().executeJs("""
                                this.dispatchEvent(new CustomEvent('int-msg', {
                                    detail: 1
                                }));
                            """);
                }),
                new Button("Fire double", e -> {
                    getElement().executeJs("""
                                this.dispatchEvent(new CustomEvent('double-msg', {
                                    detail: 1.2345
                                }));
                            """);
                })
        ));


    }

    public record FooBar(String foo, String bar, String car) {
    }
}
