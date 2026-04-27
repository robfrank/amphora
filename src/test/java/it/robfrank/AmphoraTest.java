/*
 * MIT License
 *
 * Copyright (c) 2026 JUG Torino
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package it.robfrank;

import static it.robfrank.Joiners.onMinusAndSpacesJoiner;
import static it.robfrank.Joiners.onSpaceJoiner;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class AmphoraTest {

  @Test
  void shouldBeAnErrorAmphora() {
    assertThat(Amphora.ERROR.isError()).isTrue();
    assertThat(Amphora.ERROR.isNotError()).isFalse();
  }

  @Test
  void shouldRenameMultiValuesFields() {
    var amphora = new Amphora().add("name", "first").add("name", "second").add("name", "third");

    amphora.rename("name", "newName");
    assertThat(amphora.hasField("newName")).isTrue();
    assertThat(amphora.hasNotField("name")).isTrue();

    assertThat(String.join(onSpaceJoiner, amphora.valuesOf("newName")))
        .isEqualTo("first second third");
  }

  @Test
  void shouldRenameMultipleFields() {
    var amphora =
        new Amphora()
            .add("name", "first")
            .add("name", "second")
            .add("name", "third")
            .add("name2", "four");

    amphora.rename(Map.of("name", "newName", "name2", "newName2"));

    assertThat(amphora.hasField("newName")).isTrue();
    assertThat(amphora.hasNotField("name")).isTrue();

    assertThat(String.join(" ", amphora.valuesOf("newName"))).isEqualTo("first second third");

    assertThat(amphora.hasField("newName2")).isTrue();
    assertThat(amphora.hasNotField("name2")).isTrue();

    assertThat(String.join(" ", amphora.valuesOf("newName2"))).isEqualTo("four");
  }

  @Test
  void shouldMergeMultiValuesFields() {
    var amphora = new Amphora().add("name", "first").add("name", "second").add("name", "third");

    amphora.mergeValuesOf("name");

    assertThat(amphora.hasField("name")).isTrue();
    assertThat(amphora.valueOf("name")).isEqualTo("first second third");
  }

  @Test
  void shouldMergeMultiValuesFieldsWithGivenJoiner() {
    var amphora = new Amphora().add("name", "first").add("name", "second").add("name", "third");

    amphora.mergeValuesOf("name", onMinusAndSpacesJoiner);

    assertThat(amphora.hasField("name")).isTrue();
    assertThat(amphora.valueOf("name")).isEqualTo("first - second - third");
  }

  @Test
  void amphorasWithSameValuesAreEquals() {
    var first = new Amphora().add("name", "value").add("name", "value");
    var second = new Amphora().add("name", "value").add("name", "value");

    assertThat(second).isEqualTo(first);

    second.add("difference", "different");
    assertThat(second).isNotEqualTo(first);
  }

  @Test
  void amphoraWithRawValues() {
    var amphora2 = new Amphora();
    var amphora =
        new Amphora().add("stringVal", "this is a string").add("anotheramphora", amphora2);

    assertThat(amphora.valueOf("stringVal")).isEqualTo("this is a string");

    String rawValueOf = amphora.rawValueOf("stringVal");
    assertThat(rawValueOf).isEqualTo("this is a string");

    assertThat(amphora.valueOf("anotheramphora")).isEqualTo(amphora2.toString());
    Amphora anotheramphora = amphora.rawValueOf("anotheramphora");
    assertThat(anotheramphora).isEqualTo(amphora2);
  }

  @Test
  void shouldFilterFieldsKeyByRegExp() {
    var amphora = new Amphora();

    amphora.add("prefix_me", "value");
    amphora.add("prefix_metoo", "value");
    amphora.add("meno_suffix", "value");

    var fields = amphora.fields(Pattern.compile("prefix_.*"));

    assertThat(fields).hasSize(2).containsExactlyInAnyOrder("prefix_me", "prefix_metoo");
  }

  @Test
  void shouldMergeAmphoras() {
    var amphora = new Amphora();
    amphora.add("name", "value1");
    amphora.add("name", "value2");

    var toMerge = new Amphora();
    toMerge.add("name2", "value21");
    toMerge.add("name2", "value22");

    amphora.merge(toMerge);

    assertThat(amphora.hasField("name2")).isTrue();
    assertThat(amphora.valuesOf("name2")).hasSize(2);
  }

  @Test
  void shouldReplaceFieldValue() {
    var amphora = new Amphora().add("name", "value1").add("name", "value2");

    amphora.replace("name", "newValue");

    assertThat(amphora.valueOf("name")).isEqualTo("newValue");
    assertThat(amphora.valuesOf("name")).hasSize(1);

    amphora = new Amphora().add("name", "value1");

    amphora.replace("name", Boolean.valueOf(amphora.hasField("name")));

    assertThat(amphora.valueOf("name")).isEqualTo("true");
    assertThat(amphora.valuesOf("name")).hasSize(1);
  }

  @Test
  void shouldApplyFunctionToStringField() {
    var amphora = new Amphora();

    Function<String, String> fieldModifier = input -> "MODIFIED";

    amphora.add("name", "value1");

    amphora.modify("name", fieldModifier);

    assertThat(amphora.valueOf("name")).isEqualTo("MODIFIED");
  }

  @Test
  void shouldApplyFunctionToObjectField() {
    var amphora = new Amphora();

    Function<Integer, Integer> fieldModifier = input -> 2;

    amphora.add("name", 1);

    amphora.modify("name", fieldModifier);

    assertThat(amphora.valueOf("name")).isEqualTo("2");
    assertThat((Integer) amphora.rawValueOf("name")).isEqualTo(2);
  }

  @Test
  void etlExample() {
    Function<Integer, Integer> incrementer = input -> ++input;

    var amphora =
        new Amphora()
            .add("incrementMe", 1)
            .add("txt", "a very long text")
            .add("joinMe", "multi1")
            .add("joinMe", "multi2")
            .add("joinMe", "multi3")
            .add("joinMe", "multi4")
            .add("joinMe", "multi5")
            .add("splitMe", "one two three four")
            .add("renameME", new Amphora().add("name", "inner"));

    amphora
        .modify("incrementMe", incrementer)
        .mergeValuesOf("joinMe", onSpaceJoiner)
        .split("splitMe")
        .rename("renameME", "renamed");

    Amphora littleAmphora = amphora.rawValueOf("renamed");
    assertThat(littleAmphora.valueOf("name")).isEqualTo("inner");

    Integer incremented = amphora.rawValueOf("incrementMe");
    assertThat(incremented).isEqualTo(2);
  }

  @Test
  void shouldReturnSnapshot() {
    var amphora = new Amphora().add("key1", "val1").add("key1", "val2").add("key2", "val3");
    var snapshot = amphora.snapshot();

    assertThat(snapshot.data()).isEqualTo(amphora.asRawMap());
    assertThat(snapshot.data()).containsEntry("key1", List.of("val1", "val2"));

    // Snapshot should be immutable view or copy
    amphora.add("key3", "val4");
    assertThat(snapshot.data()).doesNotContainKey("key3");
  }

  @Test
  void shouldReturnEntries() {
    var amphora = new Amphora().add("k1", "v1").add("k1", "v2").add("k2", "v3");
    var entries = amphora.entries();

    assertThat(entries)
        .hasSize(3)
        .contains(Map.entry("k1", "v1"), Map.entry("k1", "v2"), Map.entry("k2", "v3"));
  }

  @Test
  void shouldHandleNullValuesAndMissingFields() {
    var amphora = new Amphora();
    amphora.add("nullKey", null);
    assertThat(amphora.hasField("nullKey")).isFalse();
    assertThat(amphora.valueOf("missing")).isEmpty();
    Object missingRawValue = amphora.rawValueOf("missing");
    assertThat(missingRawValue).isNull();
    assertThat(amphora.valuesOf("missing")).isEmpty();
    assertThat(amphora.rawValuesOf("missing")).isEmpty();
  }

  @Test
  void shouldLoadDataFromMap() {
    var data = Map.of("key1", "val1", "key2", 2);
    var amphora = new Amphora().load(data);
    assertThat(amphora.fields()).containsExactlyInAnyOrder("key1", "key2");
    assertThat(amphora.valueOf("key2")).isEqualTo("2");
  }

  @Test
  void shouldAddAllValues() {
    var amphora = new Amphora().addAll("key", List.of("v1", "v2", "v3"));
    assertThat(amphora.valuesOf("key")).hasSize(3).containsExactly("v1", "v2", "v3");
  }

  @Test
  void shouldRemoveSpecificValue() {
    var amphora = new Amphora().add("key", "v1").add("key", "v2");
    amphora.remove("key", "v1");
    assertThat(amphora.valuesOf("key")).containsExactly("v2");

    amphora.remove("key", "v2");
    assertThat(amphora.hasField("key")).isFalse();
  }

  @Test
  void shouldRemoveEntireField() {
    var amphora = new Amphora().add("key", "v1").add("key", "v2");
    amphora.remove("key");
    assertThat(amphora.hasField("key")).isFalse();
  }

  @Test
  void shouldCheckFieldExistence() {
    var amphora = new Amphora().add("key", "val");
    assertThat(amphora.hasField("key")).isTrue();
    assertThat(amphora.hasNotField("key")).isFalse();
    assertThat(amphora.hasNotField("missing")).isTrue();
  }

  @Test
  void shouldCheckValueExistence() {
    var amphora = new Amphora().add("k1", "v1").add("k2", "v2");

    assertThat(amphora.hasValue("v1")).isTrue();
    assertThat(amphora.hasValue("k1", "v1")).isTrue();
    assertThat(amphora.hasValue("k1", "v2")).isFalse();

    assertThat(amphora.hasNotValue("missing")).isTrue();
    assertThat(amphora.hasNotValue("k1", "v2")).isTrue();
  }

  @Test
  void shouldCopyFields() {
    var amphora = new Amphora().add("k1", "v1").add("k1", "v2");
    amphora.copy("k1", "k2");

    assertThat(amphora.valuesOf("k1")).containsExactly("v1", "v2");
    assertThat(amphora.valuesOf("k2")).containsExactly("v1", "v2");
  }

  @Test
  void shouldHandleSelfRename() {
    var amphora = new Amphora().add("key", "val");
    amphora.rename("key", "key");
    assertThat(amphora.valuesOf("key")).containsExactly("val");
  }

  @Test
  void shouldClearAllData() {
    var amphora = new Amphora().add("k1", "v1").add("k2", "v2");
    amphora.clear();
    assertThat(amphora.fields()).isEmpty();
  }

  @Test
  void shouldApplyTransformationToNewField() {
    var amphora = new Amphora().add("age", 25);
    amphora.apply("age", (Integer val) -> val + 1, "nextAge");
    Integer nextAge = amphora.rawValueOf("nextAge");
    assertThat(nextAge).isEqualTo(26);
  }

  @Test
  void shouldVerifyObjectMethods() {
    var amphora = new Amphora().add("key", "val");
    assertThat(amphora.toString()).contains("key=[val]");
    assertThat(amphora.hashCode()).isNotZero();
  }
}
