# Design Spec: Amphora Project Modernization (Java 25)

**Date:** 2026-03-15
**Topic:** Modernizing the Amphora library to Java 25, improving developer experience, and streamlining dependencies.

## 1. Objective
Transform the legacy Amphora project into a modern, idiomatic Java 25 library. This includes a full refactor of core logic, migration to modern testing frameworks, and the introduction of robust CI/CD and local quality gates.

## 2. Architecture & Core Refactoring

### 2.1 Java 25 Modernization
- **Source/Target Level:** Update Maven configuration to target **Java 25**.
- **Data Storage:** Replace Guava's `ListMultimap` with a standard `Map<String, List<Object>>` or a custom internal record-based structure.
- **Pattern Matching:** Use **Pattern Matching for `instanceof`** and **Switch Expressions** within `Amphora.java` to handle various value types (primitives, nested `Amphora` instances, Lists) with type safety and conciseness.
- **Records:** Explore replacing any internal data transfer objects or Lombok-based classes with **Java Records** where immutability is preferred.

### 2.2 Functional API Refactor
- **JDK Functions:** Migrate from Guava's `Function` and `Predicate` to `java.util.function.Function` and `java.util.function.Predicate`.
- **String Handling:** Replace Guava's `Joiner` and `Splitter` with modern JDK alternatives:
    - `String.join()` and `Stream.collect(Collectors.joining())` for joining.
    - `String.split()` or `Pattern.compile().asPredicate()` for splitting.
- **Utility Classes:** Refactor or remove `Joiners.java`, `Splitters.java`, `Matchers.java`, `Objects.java`, and `Predicates.java` in favor of standard JDK static methods or simple lambda expressions.

## 3. Dependencies & Build System

### 3.1 Pruning Legacy Dependencies
- **Remove:** `com.google.guava:guava`.
- **Remove:** `org.apache.commons:commons-lang3`.
- **Remove:** `maven-eclipse-plugin` (obsolete).
- **Update:** `org.projectlombok:lombok` to a version compatible with Java 25.

### 3.2 Testing Framework Migration
- **JUnit 5 (Jupiter):** Migrate all tests from JUnit 4 to JUnit 5.
- **AssertJ:** Replace Hamcrest assertions with AssertJ for a more fluent and readable testing experience.
- **Refactor `AmphoraTest.java`:** Update test cases to use modern Java syntax (`var`, `List.of`) and JUnit 5 annotations (`@Test`, `@BeforeEach`).

## 4. Infrastructure & Tooling

### 4.1 CI/CD (GitHub Actions)
- **Workflow:** Create `.github/workflows/ci.yml`.
- **Triggers:** Push to `main` and all Pull Requests.
- **Steps:** Checkout, setup JDK 25 (Temurin), run `mvn verify`.

### 4.2 Dependency Management (Dependabot)
- **Configuration:** Create `.github/dependabot.yml`.
- **Scope:** Monitor Maven dependencies and GitHub Actions versions weekly.

### 4.3 Local Quality Gates (pre-commit)
- **Framework:** Introduce `pre-commit`.
- **Configuration:** Create `.pre-commit-config.yaml`.
- **Hooks:**
    - Trailing whitespace and end-of-file fixer.
    - YAML linting.
    - Java formatting/linting via a modern tool (e.g., Spotless or Checkstyle).

## 5. Documentation

### 5.1 README Migration
- **Format:** Convert `README.textile` to `README.md`.
- **Content Update:** Update code examples to reflect the new Java 25 API and modern syntax.
- **Status Badges:** Add CI status and Java version badges.

## 6. Success Criteria
1.  Project compiles and tests pass on JDK 25.
2.  Zero dependencies on Guava and Apache Commons.
3.  100% migration to JUnit 5 and AssertJ.
4.  CI workflow successfully validates every PR.
5.  `README.md` provides clear, modern examples.
