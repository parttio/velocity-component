package org.vaadin.addons.usageexample;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.velocitycomponent.AbstractVelocityJsComponent;

import java.util.Map;

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
        paragraphElement.setText("I should be red!");
        getElement().appendChild(paragraphElement);

        velocityJs("""
                    var p = document.createElement("p");
                    p.innerHTML = "Hello from $this.nameOfTheGame";
                    this.appendChild(p);

                    // this would be the same as the shorthand above
                    var extendedform = "Hello from ${this.getNameOfTheGame()}";
                    
                    p = document.createElement("p");
                    // using the Json helper class, obj will be a JSON object
                    // serialized from the Java DTO
                    var obj = $Json.of($this.myDto);
                    p.innerHTML =  JSON.stringify(obj);
                    this.appendChild(p);
                    
                    // Some other tests and examples:
                    var b = document.createElement("button");
                    b.innerHTML = "Click me" + ${this.getInteger()};
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
                // instantiated above, using {} form so that 
                // Velocity don't try to parse ".style.color"
                ${serverElement}.style.color = "red";
                            
                var p = document.createElement("p");
                p.innerHTML = "specialparameter1: $specialparameter1 " + 
                "<br/> specialparameter2: $specialparameter2";
                this.appendChild(p);
                """, Map.of(
                "specialparameter1", "Special Parameter Value1",
                "serverElement", paragraphElement,
                "specialparameter2", Integer.valueOf(69)
        ));

    }
}
