package org.vaadin.addons.velocitycomponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.dom.Element;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This is a base class for components that use Velocity to
 * generate JavaScript for their front-end integration.
 * <p>
 * The component can use the {@link #velocityJs(String, VelocityContext, List)} method
 * or its overloads to execute JavaScript in the context of the
 * components base element. The passed JavaScript is first processed
 * using Velocity templating engine, so you can use named parameters
 * from the context. The context by default contains the component
 * itself (referred as $this) and the {@link Json} helper class
 * (referred as $Json).
 * </p>
 */
public abstract class AbstractVelocityJsComponent extends Component {

    static VelocityEngine ve;
    static {
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader.class.getName());
        ve = new VelocityEngine();
        ve.init(p);
    }

    /**
     * Get the VelocityContext for this component.
     * <p>
     * The component can override to add more context or
     * to cache the context.
     * </p>
     *
     * @return the VelocityContext used to execute JS with special methods.
     */
    protected VelocityContext getVelocityContext() {
        VelocityContext velocityContext = new VelocityContext();
        // Add the component itself to the context
        velocityContext.put("this", this);
        // Add the Json helper class to the context
        velocityContext.put("Json", Json.class);
        return velocityContext;
    }

    protected PendingJavaScriptResult velocityJs(String jsVelocityTemplate) {
        return velocityJs(jsVelocityTemplate, getVelocityContext(), null);
    }

    protected PendingJavaScriptResult velocityJs(String jsVelocityTemplate, VelocityContext ctx, List<Element> specialParameters) {
        if (ctx == null) {
            ctx = getVelocityContext();
        }
        if(specialParameters == null) {
            specialParameters = Collections.emptyList();
        }
        StringWriter script = new StringWriter();
        ve.evaluate(ctx, script, "velocityJsComponent", jsVelocityTemplate);
        Serializable[] parameters = specialParameters.toArray(new Serializable[0]);
        return getElement().executeJs(script.toString(), parameters);
    }

    protected PendingJavaScriptResult velocityJs(String jsVelocityTemplate, Map<String, Object> additionalContext) {
        VelocityContext ctx = new VelocityContext(getVelocityContext());
        List<Element> specialParameters = new ArrayList<>();
        additionalContext.forEach((key, val) -> {
            if (val instanceof Element) {
                // Special handling for Vaadin Elements
                Element element = (Element) val;
                ctx.put(key, "$"+specialParameters.size());
                specialParameters.add(element);
            } else {
                ctx.put(key, val);
            }
        });
        return velocityJs(jsVelocityTemplate, ctx, specialParameters);
    }

    public PendingJavaScriptResult jsTemplate(String templateName, Map<String, Object> additionalContext) {
        VelocityContext ctx = new VelocityContext(getVelocityContext());
        List<Element> specialParameters = new ArrayList<>();
        additionalContext.forEach((key, val) -> {
            if (val instanceof Element) {
                // Special handling for Vaadin Elements
                Element element = (Element) val;
                ctx.put(key, "$"+specialParameters.size());
                specialParameters.add(element);
            } else {
                ctx.put(key, val);
            }
        });

        Template template = ve.getTemplate(templateName);
        StringWriter sw = new StringWriter();
        template.merge(ctx,
                sw
        );
        Serializable[] parameters = specialParameters.toArray(new Serializable[0]);
        return getElement().executeJs(sw.toString(), parameters);
    }

}
