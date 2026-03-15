# Amphora Project Modernization (Java 25) Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

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

## Chunk 1: Build System & Infrastructure

### Task 1: Update Maven Configuration for Java 25
- [ ] **Step 1: Modify `pom.xml` to target Java 25 and update plugins**
- [ ] **Step 2: Run `mvn clean compile` to verify build setup**
- [ ] **Step 3: Commit `pom.xml`**

### Task 2: Configure CI/CD and Dependency Management
- [ ] **Step 1: Create `.github/workflows/ci.yml`**
- [ ] **Step 2: Create `.github/dependabot.yml`**
- [ ] **Step 3: Commit GitHub configurations**

### Task 3: Setup pre-commit and Spotless
- [ ] **Step 1: Add Spotless plugin to `pom.xml`**
- [ ] **Step 2: Create `.pre-commit-config.yaml`**
- [ ] **Step 3: Run `pre-commit install` and `mvn spotless:apply`**
- [ ] **Step 4: Commit formatting and hook configs**

---

## Chunk 2: Test Migration (JUnit 5 & AssertJ)

### Task 4: Migrate `AmphoraTest.java` to JUnit 5
- [ ] **Step 1: Update imports from JUnit 4 (`org.junit.*`) to JUnit 5 (`org.junit.jupiter.api.*`)**
- [ ] **Step 2: Replace `@Before` with `@BeforeEach`, `@Test` with JUnit 5's `@Test`**
- [ ] **Step 3: Replace Hamcrest assertions with AssertJ (`assertThat(...)`)**
- [ ] **Step 4: Run tests and ensure they fail/pass as expected**
- [ ] **Step 5: Commit test migration**

---

## Chunk 3: Core Refactoring & Dependency Removal

### Task 5: Refactor `Amphora.java` (Part 1: Data Storage)
- [ ] **Step 1: Replace `ListMultimap` with `Map<String, List<Object>>`**
- [ ] **Step 2: Fix compilation errors in `add`, `get`, and other methods**
- [ ] **Step 3: Verify with existing tests**
- [ ] **Step 4: Commit storage refactor**

### Task 6: Refactor Utilities (`Joiners`, `Splitters`, etc.)
- [ ] **Step 1: Refactor `Joiners.java` to use JDK `Collectors.joining()`**
- [ ] **Step 2: Refactor `Splitters.java` to use `String.split()`**
- [ ] **Step 3: Migrate `Predicates.java` and `Matchers.java` to JDK functional interfaces**
- [ ] **Step 4: Commit utility modernization**

### Task 7: Final Dependency Removal
- [ ] **Step 1: Remove Guava and Commons-Lang from `pom.xml`**
- [ ] **Step 2: Remove any remaining imports in Java files**
- [ ] **Step 3: Run full `mvn verify`**
- [ ] **Step 4: Commit dependency pruning**

---

## Chunk 4: Documentation

### Task 8: Migrate README to Markdown
- [ ] **Step 1: Create `README.md` from `README.textile` content**
- [ ] **Step 2: Update code examples to modern Java 25 syntax**
- [ ] **Step 3: Remove `README.textile`**
- [ ] **Step 4: Commit documentation changes**
