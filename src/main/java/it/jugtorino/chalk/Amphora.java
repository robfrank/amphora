package it.jugtorino.chalk;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import lombok.ToString;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static it.jugtorino.chalk.Joiners.onSpaceJoiner;
import static it.jugtorino.chalk.Matchers.checkThat;
import static it.jugtorino.chalk.Objects.isNotNull;
import static it.jugtorino.chalk.Objects.isNull;
import static it.jugtorino.chalk.Objects.not;
import static com.google.common.base.Functions.toStringFunction;
import static com.google.common.base.Predicates.contains;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

@ToString
public class Amphora {

	public static final Amphora ERROR = new Amphora();

	private final ListMultimap<String, Object> data;

	public Amphora() {
		super();
		this.data = ArrayListMultimap.create();
	}

	public Collection<Entry<String, Object>> entries() {
		return data.entries();
	}

	public String valueOf(String fieldName) {
		Object rawValue = rawValueOf(fieldName);

		if (Objects.isNull(rawValue)) return EMPTY;
		return rawValue.toString();
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> T rawValueOf(String fieldName) {
		if (data.get(fieldName).isEmpty()) return null;
		return (T) data.get(fieldName).get(0);
	}

	public Collection<String> valuesOf(String fieldName) {
		return transform(data.get(fieldName), toStringFunction());
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> Collection<T> rawValuesOf(String fieldName) {
		return (Collection<T>) data.get(fieldName);
	}

	public Set<String> fields() {
		return newHashSet(data.keySet());
	}

	public Set<String> fields(Pattern pattern) {
		return newHashSet(filter(data.keySet(), contains(pattern)));
	}

	public Amphora addAll(String fieldName, Iterable<? extends Object> values) {
		for (Object value : values) {
			add(fieldName, value);
		}
		return this;
	}

	public Amphora load(Map<String, ? extends Object> data) {
		for (Entry<String, ?> entry : data.entrySet()) {
			Object value = isNull(entry.getValue()) ? EMPTY : entry.getValue();
			add(entry.getKey(), value);
		}
		return this;
	}

	public Amphora add(String fieldName, Object fieldValue) {
		if (isNotNull(fieldValue)) data.put(fieldName, fieldValue);

		return this;
	}

	public Amphora replace(String field, Object newValue) {
		remove(field);
		add(field, newValue);
		return this;
	}

	public Amphora remove(String fieldName, Object fieldValue) {
		data.remove(fieldName, fieldValue);
		return this;
	}

	public Amphora remove(String fieldName) {
		data.removeAll(fieldName);
		return this;
	}

	public boolean isError() {
		return checkThat(this, sameInstance(ERROR));
	}

	public boolean isNotError() {
		return checkThat(this, not(sameInstance(ERROR)));
	}

	public boolean hasField(String fieldName) {
		return data.containsKey(fieldName);
	}

	public boolean hasNotField(String fieldName) {
		return not(data.containsKey(fieldName));
	}

	public boolean hasValue(Object value) {
		return data.containsValue(value);
	}

	public boolean hasValue(String fieldName, Object value) {
		return data.containsEntry(fieldName, value);
	}

	public boolean hasNotValue(Object value) {
		return not(data.containsValue(value));
	}

	public boolean hasNotValue(String fieldName, Object value) {
		return not(data.containsEntry(fieldName, value));
	}

	public Amphora copy(String fieldName, String newFieldName) {
		Collection<Object> values = newArrayList(data.get(fieldName));
		addAll(newFieldName, values);
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
		mergeValuesOf(fieldName, onSpaceJoiner);
		return this;
	}

	public Amphora mergeValuesOf(String fieldName, Joiner joiner) {
		String merged = joiner.join(valuesOf(fieldName));

		remove(fieldName);

		add(fieldName, merged);
		return this;

	}

	public Amphora split(String fieldName) {
		split(fieldName, Splitter.on(' '));

		return this;

	}

	public Amphora split(String fieldName, Splitter splitter) {
		Iterable<String> splitted = splitter.split(valueOf(fieldName));

		remove(fieldName);

		addAll(fieldName, splitted);
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

	public <F, T> Amphora modify(String fieldName, Function<F, T> fieldModifier) {
		replace(fieldName, fieldModifier.apply((F) rawValueOf(fieldName)));
		return this;
	}

	public <F, T> Amphora apply(String from, Function<F, T> transformer, String to) {
		add(to, transformer.apply((F) rawValueOf(from)));
		return this;
	}

	public Map<String, Collection<Object>> asRawMap() {
		return data.asMap();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;

		Amphora other = (Amphora) obj;

		if (data == null) {
			if (other.data != null) return false;
		} else if (!data.equals(other.data)) return false;

		return true;
	}

}
