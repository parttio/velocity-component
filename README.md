# Velocity JS Component - Velocity template powered JS for Vaadin component development

This library contains an enhanced JS API that can be used to build Java wrappers of client side components for Vaadin.

A real world usage example is available at [MapLibre GL integration](https://github.com/parttio/maplibre).

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
