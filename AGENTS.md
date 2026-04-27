# Amphora Agent Guide

## Project Overview
Amphora is a modern, lightweight Java 25 library designed for building flexible, multi-value, shapable data containers. It simplifies data ingestion and transformation (ETL) tasks by providing a fluent API for joining, splitting, renaming, and modifying fields.

- **Technology Stack:** Java 25, Maven, JUnit 5, AssertJ.
- **Key Concepts:** Fluent API, Multi-value fields, Snapshots, ETL operations.

## Development Standards

### Code Style
- **Java 25:** Use modern Java features like `var`, Patterns in `instanceof`, Switch Expressions, and Records.
- **Formatting:** Managed by [Spotless](https://github.com/diffplug/spotless) with Google Java Format. Run `mvn spotless:apply` before committing.
- **License Headers:** All source files MUST have the MIT license header. Run `mvn license:format` to apply.

### Testing
- Use **JUnit 5** for test structure and **AssertJ** for assertions.
- Test files are located in `src/test/java/it/robfrank/`.
- Ensure high test coverage for any new ETL operation or core logic change.

### Git Conventions
- Follow **Conventional Commits** (e.g., `feat:`, `fix:`, `docs:`, `build:`, `ci:`).
- These are used by `release-it` to generate the changelog.

## Key Workflows

### 1. Adding a New ETL Operation
- Implement the method in [Amphora.java](air-file://v52uvmbjj2h788k4qkq1/Users/frank/projects/mine/amphora/src/main/java/it/robfrank/Amphora.java?type=file&root=%252F).
- Ensure it follows the fluent API pattern (returns `this`).
- Use JDK built-ins (e.g., `String.join`, `String.split`) instead of external libraries.
- Add comprehensive tests in [AmphoraTest.java](air-file://v52uvmbjj2h788k4qkq1/Users/frank/projects/mine/amphora/src/test/java/it/robfrank/AmphoraTest.java?type=file&root=%252F).

### 2. Build & Verification
- `mvn clean verify`: Runs all tests and quality checks (license, spotless).
- `mvn spotless:check`: Verifies code formatting.
- `mvn license:check`: Verifies license headers.

### 3. Release Process
- The release is automated via GitHub Actions using the `release` profile in Maven.
- Triggered manually from the GitHub UI or via `release-it` (if configured locally).
- Requires GPG signing and Maven Central credentials (stored as GitHub Secrets).

## Architecture Insights
- **Data Structure:** Uses `Map<String, List<Object>>` to support multi-value fields.
- **Snapshots:** [AmphoraSnapshot](air-file://v52uvmbjj2h788k4qkq1/Users/frank/projects/mine/amphora/src/main/java/it/robfrank/Amphora.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A5818%2C%22second%22%3A6054%7D%2C%22lines%22%3A%7B%22first%22%3A220%2C%22second%22%3A225%7D%7D&root=%252F) is a record providing an immutable view of the data.
- **Utility Classes:** [Joiners.java](air-file://v52uvmbjj2h788k4qkq1/Users/frank/projects/mine/amphora/src/main/java/it/robfrank/Joiners.java?type=file&root=%252F) and [Splitters.java](air-file://v52uvmbjj2h788k4qkq1/Users/frank/projects/mine/amphora/src/main/java/it/robfrank/Splitters.java?type=file&root=%252F) provide common delimiters for ETL operations.
