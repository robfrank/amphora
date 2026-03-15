# Amphora

[![Java CI](https://github.com/it-jugtorino/amphora/actions/workflows/ci.yml/badge.svg)](https://github.com/it-jugtorino/amphora/actions/workflows/ci.yml)
[![Java Version](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/technologies/javase/25-relnote-issues.html)

Amphora is a single, simple, multi-value, untyped data container.

## Why Amphora?

A presentation of [Amphora](http://presentz.org/jugtorino/201205_01_killbean_2) (in Italian).
We need to do some simple ETL during ingestion of data.

## Example

This simple example shows how to use an Amphora object to do a simple ETL.

First: create an Amphora with mixed content in a fluent way:

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

Now do some simple ETL:

```java
amphora.mergeValuesOf("joinMe", Joiners.onSpaceJoiner)
       .split("splitMe")
       .rename("renameME", "renamed");
```

And now pass a `Function` that Amphora uses to modify field's values:

```java
Function<Integer, Integer> incrementer = input -> ++input;

amphora.modify("incrementMe", incrementer);
```

You can also take an immutable snapshot of the data:

```java
var snapshot = amphora.snapshot();
System.out.println(snapshot.data());
```
