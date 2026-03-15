package it.jugtorino.chalk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Amphora {

  public static final Amphora ERROR = new Amphora();
  private static final String EMPTY = "";

  private final Map<String, List<Object>> data;

  public Amphora() {
    this.data = new HashMap<>();
  }

  public Collection<Entry<String, Object>> entries() {
    return data.entrySet().stream()
        .flatMap(entry -> entry.getValue().stream().map(val -> Map.entry(entry.getKey(), val)))
        .collect(Collectors.toList());
  }

  public String valueOf(String fieldName) {
    Object rawValue = rawValueOf(fieldName);
    return rawValue == null ? EMPTY : rawValue.toString();
  }

  @SuppressWarnings("unchecked")
  public <T> T rawValueOf(String fieldName) {
    List<Object> values = data.get(fieldName);
    if (values == null || values.isEmpty()) return null;
    return (T) values.get(0);
  }

  public Collection<String> valuesOf(String fieldName) {
    List<Object> values = data.get(fieldName);
    if (values == null) return List.of();
    return values.stream().map(Objects::toString).collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  public <T> Collection<T> rawValuesOf(String fieldName) {
    List<Object> values = data.get(fieldName);
    return (Collection<T>) (values != null ? values : List.of());
  }

  public Set<String> fields() {
    return new HashSet<>(data.keySet());
  }

  public Set<String> fields(Pattern pattern) {
    return data.keySet().stream()
        .filter(key -> pattern.matcher(key).find())
        .collect(Collectors.toSet());
  }

  public Amphora addAll(String fieldName, Iterable<?> values) {
    for (Object value : values) {
      add(fieldName, value);
    }
    return this;
  }

  public Amphora load(Map<String, ?> data) {
    for (Entry<String, ?> entry : data.entrySet()) {
      Object value = entry.getValue() == null ? EMPTY : entry.getValue();
      add(entry.getKey(), value);
    }
    return this;
  }

  public Amphora add(String fieldName, Object fieldValue) {
    if (fieldValue != null) {
      data.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(fieldValue);
    }
    return this;
  }

  public Amphora replace(String field, Object newValue) {
    remove(field);
    add(field, newValue);
    return this;
  }

  public Amphora remove(String fieldName, Object fieldValue) {
    List<Object> values = data.get(fieldName);
    if (values != null) {
      values.remove(fieldValue);
      if (values.isEmpty()) {
        data.remove(fieldName);
      }
    }
    return this;
  }

  public Amphora remove(String fieldName) {
    data.remove(fieldName);
    return this;
  }

  public boolean isError() {
    return this == ERROR;
  }

  public boolean isNotError() {
    return this != ERROR;
  }

  public boolean hasField(String fieldName) {
    return data.containsKey(fieldName);
  }

  public boolean hasNotField(String fieldName) {
    return !data.containsKey(fieldName);
  }

  public boolean hasValue(Object value) {
    return data.values().stream().anyMatch(list -> list.contains(value));
  }

  public boolean hasValue(String fieldName, Object value) {
    List<Object> values = data.get(fieldName);
    return values != null && values.contains(value);
  }

  public boolean hasNotValue(Object value) {
    return !hasValue(value);
  }

  public boolean hasNotValue(String fieldName, Object value) {
    return !hasValue(fieldName, value);
  }

  public Amphora copy(String fieldName, String newFieldName) {
    List<Object> values = data.get(fieldName);
    if (values != null) {
      addAll(newFieldName, new ArrayList<>(values));
    }
    return this;
  }

  public Amphora rename(String oldFieldName, String newFieldName) {
    if (oldFieldName.equals(newFieldName)) return this;
    copy(oldFieldName, newFieldName);
    remove(oldFieldName);
    return this;
  }

  public Amphora rename(Map<String, String> old2new) {
    for (Entry<String, String> mapping : old2new.entrySet()) {
      rename(mapping.getKey(), mapping.getValue());
    }
    return this;
  }

  public Amphora mergeValuesOf(String fieldName) {
    return mergeValuesOf(fieldName, " ");
  }

  public Amphora mergeValuesOf(String fieldName, String delimiter) {
    String merged = String.join(delimiter, valuesOf(fieldName));
    remove(fieldName);
    add(fieldName, merged);
    return this;
  }

  public Amphora split(String fieldName) {
    return split(fieldName, " ");
  }

  public Amphora split(String fieldName, String delimiter) {
    String value = valueOf(fieldName);
    remove(fieldName);
    if (!value.isEmpty()) {
      addAll(fieldName, List.of(value.split(delimiter)));
    }
    return this;
  }

  public Amphora merge(Amphora toMerge) {
    for (String field : toMerge.fields()) {
      addAll(field, toMerge.rawValuesOf(field));
    }
    return this;
  }

  public Amphora clear() {
    data.clear();
    return this;
  }

  @SuppressWarnings("unchecked")
  public <F, T> Amphora modify(String fieldName, Function<F, T> fieldModifier) {
    replace(fieldName, fieldModifier.apply((F) rawValueOf(fieldName)));
    return this;
  }

  @SuppressWarnings("unchecked")
  public <F, T> Amphora apply(String from, Function<F, T> transformer, String to) {
    add(to, transformer.apply((F) rawValueOf(from)));
    return this;
  }

  public Map<String, List<Object>> asRawMap() {
    return new HashMap<>(data);
  }

  public AmphoraSnapshot snapshot() {
    Map<String, List<Object>> snapshotData = new HashMap<>();
    data.forEach((key, value) -> snapshotData.put(key, List.copyOf(value)));
    return new AmphoraSnapshot(Map.copyOf(snapshotData));
  }

  public record AmphoraSnapshot(Map<String, List<Object>> data) {}

  @Override
  public String toString() {
    return "Amphora(data=" + data + ")";
  }

  @Override
  public int hashCode() {
    return Objects.hash(data);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Amphora other = (Amphora) obj;
    return Objects.equals(data, other.data);
  }
}
