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

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
