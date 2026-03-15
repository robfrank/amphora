# Amphora Project Modernization (Java 25) Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:executing-plans to implement this plan. Steps use checkbox (`- [x]`) syntax for tracking.

**Goal:** Modernize the Amphora library to Java 25, remove legacy dependencies (Guava, Commons-Lang), and migrate to JUnit 5/AssertJ.

**Architecture:** Refactor core logic to use modern JDK features (Records, Pattern Matching, Switch Expressions), replace Guava multimaps with standard Java Collections, and implement a robust CI/CD pipeline with GitHub Actions and pre-commit hooks.

**Tech Stack:** Java 25, Maven, JUnit 5, AssertJ, GitHub Actions, pre-commit, Spotless (google-java-format).

---

## File Structure & Responsibilities

### Core Library
- `src/main/java/it/jugtorino/chalk/Amphora.java`: Main data container. Will be refactored to use `Map<String, List<Object>>` and modern Java syntax.
- `src/main/java/it/jugtorino/chalk/Joiners.java`: Utility for joining. Will be refactored to use `Collectors.joining()`.
- `src/main/java/it/jugtorino/chalk/Splitters.java`: Utility for splitting. Will be refactored to use `String.split()`.
- `src/main/java/it/jugtorino/chalk/Matchers.java`: Utility for matching. Will be refactored to use JDK `Predicate`.
- `src/main/java/it/jugtorino/chalk/Predicates.java`: Legacy predicate definitions. Will be migrated to `java.util.function.Predicate`.
- `src/main/java/it/jugtorino/chalk/Objects.java`: (To be evaluated for removal if redundant with JDK 25 `Objects` or Records).

### Testing
- `src/test/java/it/jugtorino/chalk/AmphoraTest.java`: Main test suite. Will be migrated to JUnit 5 and AssertJ.

### Infrastructure
- `pom.xml`: Maven configuration. Target Java 25, update/remove dependencies and plugins.
- `.github/workflows/ci.yml`: GitHub Actions CI pipeline.
- `.github/dependabot.yml`: Dependabot configuration.
- `.pre-commit-config.yaml`: pre-commit hooks for formatting and linting.
- `README.md`: Migrated from `README.textile`.

---

## Chunk 1: Build System & Infrastructure (Approved)

### Task 1: Update Maven Configuration for Java 25
- [x] **Step 1: Modify `pom.xml` to target Java 25 and update plugins**
    - Update `<maven.compiler.release>` to `25`.
    - Update `maven-compiler-plugin` to `3.13.0+`.
    - Update `maven-surefire-plugin` to `3.2.0+` (required for JUnit 5).
    - Remove the obsolete `maven-eclipse-plugin`.
    - Update `org.projectlombok:lombok` to `1.18.36+`.
- [x] **Step 2: Run `mvn clean compile` to verify build setup**
    - Run: `mvn clean compile`
    - Expected: BUILD SUCCESS
- [x] **Step 3: Commit `pom.xml`**

### Task 2: Configure CI/CD and Dependency Management
- [x] **Step 1: Create `.github/workflows/ci.yml`**
    - Set up JDK 25 (Temurin), run `mvn verify`. Use `actions/cache` for Maven.
- [x] **Step 2: Create `.github/dependabot.yml`**
    - Configure for Maven and GitHub Actions updates.
- [x] **Step 3: Verify GitHub configurations**
    - Run: `action-lint .github/workflows/ci.yml` (if available) or check YAML syntax manually.
- [x] **Step 4: Commit GitHub configurations**

### Task 3: Setup pre-commit and Spotless
- [x] **Step 1: Add Spotless plugin to `pom.xml`**
    - Configure `google-java-format` (version `1.22.0+`).
- [x] **Step 2: Create `.pre-commit-config.yaml`**
    - Add hooks: `trailing-whitespace`, `end-of-file-fixer`, `check-yaml`.
    - Add a `local` hook to run `mvn spotless:check`.
- [x] **Step 3: Run hooks and Spotless**
    - Run: `pre-commit install && pre-commit run --all-files`
    - Run: `mvn spotless:apply`
- [x] **Step 4: Commit formatting and hook configs**

---

## Chunk 2: Test Migration (JUnit 5 & AssertJ)

### Task 4: Migrate `AmphoraTest.java` to JUnit 5
- [x] **Step 1: Add JUnit 5 (Jupiter) and AssertJ to `pom.xml`**
- [x] **Step 2: Update imports from JUnit 4 (`org.junit.*`) to JUnit 5 (`org.junit.jupiter.api.*`)**
- [x] **Step 3: Replace `@Before` with `@BeforeEach`, `@Test` with JUnit 5's `@Test`**
- [x] **Step 4: Replace Hamcrest assertions with AssertJ (`assertThat(...)`)**
- [x] **Step 5: Modernize test syntax using `var` and `List.of`**
- [x] **Step 6: Run tests and ensure they pass**
    - Run: \`mvn test\`
    - Expected: All tests pass.
- [x] **Step 7: Commit test migration**

---

## Chunk 3: Core Refactoring & Dependency Removal

### Task 5: Refactor `Amphora.java` (Part 1: Data Storage)
- [x] **Step 1: Replace `ListMultimap` with `Map<String, List<Object>>` using `HashMap`**
- [x] **Step 2: Fix compilation errors using Pattern Matching for `instanceof` and Switch Expressions for value processing**
- [x] **Step 3: Verify with existing tests**
- [x] **Step 4: Commit storage refactor**

### Task 5.1: Refactor `Amphora.java` (Part 2: API Modernization)
- [x] **Step 1: Write a failing test for the new `snapshot()` method**
- [x] **Step 2: Run test to verify it fails**
- [x] **Step 3: Implement `snapshot()` as a Record-based immutable view**
- [x] **Step 4: Run test to verify it passes**
- [x] **Step 5: Commit API refactor**

### Task 6: Refactor/Remove Utilities (`Joiners`, `Splitters`, etc.)
- [x] **Step 1: Refactor `Joiners.java` to use JDK `Collectors.joining()` or replace with lambdas**
- [x] **Step 2: Refactor `Splitters.java` to use `String.split()` or replace with lambdas**
- [x] **Step 3: Migrate `Predicates.java` and `Matchers.java` to JDK functional interfaces (`java.util.function.Predicate`)**
- [x] **Step 4: Remove `Objects.java` and replace with JDK `java.util.Objects`**
- [x] **Step 5: Remove redundant utility classes where possible in favor of JDK static methods**
- [x] **Step 6: Commit utility modernization/removal**

### Task 7: Final Dependency Removal
- [x] **Step 1: Remove Guava and Commons-Lang from `pom.xml`**
- [x] **Step 2: Remove any remaining imports in Java files (use \`grep\` to verify)**
- [x] **Step 3: Run full `mvn verify`**
- [x] **Step 4: Commit dependency pruning**

---

## Chunk 4: Documentation

### Task 8: Migrate README to Markdown
- [x] **Step 1: Create `README.md` from `README.textile` content**
- [x] **Step 2: Add status badges (CI status, Java version) to the new \`README.md\`**
- [x] **Step 3: Update code examples to modern Java 25 syntax (`var`, `List.of`)**
- [x] **Step 4: Remove `README.textile`**
- [x] **Step 5: Commit documentation changes**
