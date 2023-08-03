package org.vaadin.addons.usageexample;

import org.vaadin.addons.velocitycomponent.Json;

public class MyDto {
    public String name = "Velocity";
    public String project = "Jakarta";

    public String getName() {
        return name;
    }

    public String getProject() {
        return project;
    }

    @Override
    public String toString() {
        return Json.of(this);
    }
}
