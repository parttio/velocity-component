# Velocity JS Component - Velocity template powered JS for Vaadin component development

This library contains an enhanced JS API that can be used to build Java wrappers of client side components for Vaadin.

This helper library is intended to be used only temporarily, until StringTemplate based API is ready in both Java itself and Vaadin.

Check the usage from `src/test`. A real world usage example is available at [MapLibre GL integration](https://github.com/parttio/maplibre).

Compared to standard `getElement().executeJs()` style API, this library provides the following benefits:

 * Parameter values are more readable in the JS snippets, e.g. '$foo' instead of '$0'
 * Possibility to pass JS objects and/or JSON to JS snippets, without transferring as string (less code, better performance). 
 * Velocity template based JS code generation in general, e.g. generate JS code using loops and conditionals

### Development

Starting the test/demo server:
```
mvn jetty:run
```

This deploys demo at http://localhost:8080

### Integration test

TODO add integration tests :-)

## Publishing to Vaadin Directory

With commit rights to the repository, issue:

    mvn release:prepare release:cleanup

Configured GH action will build a release and push to Maven Central.
