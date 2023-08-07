package org.vaadin.addons.usageexample;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import org.apache.velocity.app.Velocity;
import org.vaadin.addons.velocitycomponent.AbstractVelocityJsComponent;

import java.util.Map;
import java.util.Properties;

@Route
@Tag("div") // this can be pretty much anything, simple div is fine for this example
public class UsageExample extends AbstractVelocityJsComponent {

    private MyDto myDto = new MyDto();

    public MyDto getMyDto() {
        return myDto;
    }

    public int getInteger() {
        return 3;
    }

    public String getNameOfTheGame() {
        return getClass().getSimpleName();
    }

    public UsageExample() {
        Element paragraphElement = new Element("p");
        paragraphElement.getStyle().setColor("green");
        paragraphElement.setText("I should be red (or purple)!");
        getElement().appendChild(paragraphElement);

        getElement().executeJs("""
                """);

        velocityJs("""
                    var p = document.createElement("p");
                    p.textContent = "Hello from $this.nameOfTheGame";
                    this.appendChild(p);

                    // this would be the same as the shorthand above
                    var extendedform = "Hello from ${this.getNameOfTheGame()}";
                    
                    p = document.createElement("p");
                    // using the Json helper class, obj will be a JSON object
                    // serialized from the Java DTO
                    var obj = $Json.of($this.myDto);
                    p.textContent =  JSON.stringify(obj);
                    this.appendChild(p);
                    
                    // Some other tests and examples:
                    var b = document.createElement("button");
                    b.textContent = "Click me" + ${this.getInteger()};
                    b.onclick = function() {
                        var object = ${this.getMyDto()};
                        alert("Clicked! object (JSON.stringify):" + JSON.stringify(object)
                         + " \\n this.myDto.name: " + "${this.myDto.name}"
                        );
                    };
                    this.appendChild(b);
                """);

        // a call with special context paremeters
        velocityJs("""
                // $serverElement refers to the paragraphElement
                // instantiated above. Using {} syntax below so that 
                // Velocity don't try to parse ".style.color"
                ${serverElement}.style.color = "red";
                
                setTimeout(() => {${serverElement}.style.color = "purple";}, 2000);
                            
                var p = document.createElement("p");
                p.innerHTML = "specialparameter1: $specialparameter1 " + 
                "<br/> specialparameter2: $specialparameter2";
                this.appendChild(p);
                """, Map.of(
                "specialparameter1", "Special Parameter Value1",
                "serverElement", paragraphElement,
                "specialparameter2", Integer.valueOf(69)
        ));

        jsTemplate("js.js", Map.of(
                "specialparameter1", "Special Parameter Value1",
                "serverElement", paragraphElement,
                "specialparameter2", Integer.valueOf(69)
        ));

    }
}
