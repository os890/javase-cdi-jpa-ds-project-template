# Java SE CDI + JPA + DeltaSpike Project Template

A Java SE project template demonstrating CDI (Jakarta Contexts and Dependency Injection) with JPA (Jakarta Persistence) and Apache DeltaSpike Data repositories, running in a standalone Java SE environment.

## Overview

This template shows how to:

- Bootstrap a CDI container in Java SE using OpenWebBeans
- Use DeltaSpike Data module for repository-style JPA access
- Manage JPA `EntityManager` lifecycle with CDI producers
- Write CDI-integrated tests using the [Dynamic CDI Test Bean Addon](https://github.com/os890/dynamic-cdi-test-bean-addon)

## Requirements

- Java 25+
- Maven 3.6.3+
- CDI 4.1 container (OpenWebBeans 4.0.3, activated via `owb` profile)

## Project Structure

```
src/
  main/
    java/
      org/os890/cdi/template/
        ConfigEntry.java           # JPA entity
        ConfigRepository.java      # DeltaSpike Data repository
        DemoApp.java               # Entry point (CDI SE bootstrap)
        EntityManagerProducer.java # CDI EntityManager producer
    resources/
      META-INF/
        beans.xml                  # CDI 4.1 bean archive descriptor
        entities.xml               # JPA ORM mapping (Jakarta 3.2)
        persistence.xml            # JPA persistence unit (Jakarta 3.2)
        LICENSE.txt                # Apache License 2.0
  test/
    java/
      org/os890/cdi/test/
        SimpleTest.java                  # CDI integration test
        TestEntityManagerProducer.java   # Test EntityManager producer (HSQLDB)
        TestPersistenceUnitInfo.java     # In-memory HSQLDB persistence unit
    resources/
      META-INF/
        beans.xml                        # CDI 4.1 bean archive for tests
```

## Build

```bash
mvn clean verify
```

## Testing

Tests use the [Dynamic CDI Test Bean Addon](https://github.com/os890/dynamic-cdi-test-bean-addon) (`@EnableTestBeans`) for CDI SE integration testing with full classpath scanning.

`TestEntityManagerProducer` uses `@Specializes` to replace the production `EntityManagerProducer` with an HSQLDB in-memory database during tests.

## Quality

The build enforces:
- Java 25, Maven 3.6.3+
- Checkstyle (0 violations)
- Apache RAT license headers (0 unapproved)
- Compilation (0 warnings with -Xlint:all)
- Dependency convergence
- JaCoCo coverage reporting

## License

Apache License 2.0 — see [LICENSE](LICENSE)
