# Amphora

[![Java CI](https://github.com/it-jugtorino/amphora/actions/workflows/ci.yml/badge.svg)](https://github.com/it-jugtorino/amphora/actions/workflows/ci.yml)
[![Java Version](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/technologies/javase/25-relnote-issues.html)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Amphora is a simple, multi-value, shapable data container for Java. It allows you to build complex data structures fluently and perform simple ETL (Extract, Transform, Load) operations like joining, splitting, renaming, and modifying fields.

## Why Amphora?

Amphora was designed to simplify data ingestion and transformation tasks. It's particularly useful for:
- Quick ETL operations during data ingestion.
- Building flexible data models that need to be "reshaped" frequently.
- Handling semi-structured data where fields might have multiple values.

## Features

- **Fluent API:** Build data containers with ease.
- **Multi-value Support:** Fields can store multiple values.
- **ETL Operations:** Built-in support for `merge`, `split`, `rename`, and `modify`.
- **Immutability:** Take immutable snapshots of your data at any time.

## Installation

To use Amphora in your Maven project, add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>it.robfrank</groupId>
    <artifactId>amphora</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Example Usage

### 1. Create an Amphora

```java
var amphora = new Amphora()
                .add("incrementMe", 1)
                .add("txt", "a very long text")
                .add("joinMe", "multi1")
                .add("joinMe", "multi2")
                .add("joinMe", "multi3")
                .add("joinMe", "multi4")
                .add("joinMe", "multi5")
                .add("splitMe", "one two three four")
                .add("renameME", new Amphora().add("name", "inner"));
```

### 2. Transform Data

```java
amphora.mergeValuesOf("joinMe", Joiners.onSpaceJoiner)
       .split("splitMe")
       .rename("renameME", "renamed");
```

### 3. Modify Values

```java
Function<Integer, Integer> incrementer = input -> ++input;
amphora.modify("incrementMe", incrementer);
```

### 4. Take a Snapshot

```java
var snapshot = amphora.snapshot();
System.out.println(snapshot.data());
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Releasing

To release a new version of Amphora:

1. Go to the **Actions** tab in GitHub.
2. Select the **Release** workflow.
3. Click **Run workflow**.
4. Enter the new version (e.g., `1.0.0`) and choose whether to do a dry run.
5. Click **Run workflow**.

The workflow will:
- Update the version in `pom.xml`.
- Generate `CHANGELOG.md` based on Conventional Commits.
- Create a Git tag and GitHub Release.
- Sign and publish artifacts to Maven Central.

### Prerequisites

The following GitHub Secrets must be configured for the release workflow:

- `GPG_PRIVATE_KEY`: Your ASCII-armored GPG private key.
- `GPG_PASSPHRASE`: The passphrase for your GPG key.
- `MAVEN_CENTRAL_USERNAME`: Your Central Portal username/token ID.
- `MAVEN_CENTRAL_PASSWORD`: Your Central Portal password/token secret.

### Conventional Commits

Amphora uses [Conventional Commits](https://www.conventionalcommits.org/) for automated changelog generation. Ensure your commit messages follow the format (e.g., `feat: ...`, `fix: ...`).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
