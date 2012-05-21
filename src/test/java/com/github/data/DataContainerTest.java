package com.github.data;

import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static com.github.data.Joiners.onMinusAndSpacesJoiner;
import static com.github.data.Joiners.onSpaceJoiner;
import static com.google.common.base.Joiner.on;

public class DataContainerTest {

	@Test
	public void shouldRenameMultiValuesFields() throws Exception {

		DataContainer container = new DataContainer()
				.add("name", "first")
				.add("name", "second")
				.add("name", "third");

		container.renameField("name", "newName");
		assertTrue(container.hasField("newName"));
		assertTrue(container.hasNotField("name"));

		assertThat(onSpaceJoiner.join(container.valuesOf("newName")), equalTo("first second third"));
	}

	@Test
	public void shouldRenameMultipleFields() throws Exception {
		DataContainer container = new DataContainer();

		container.add("name", "first");
		container.add("name", "second");
		container.add("name", "third");

		container.add("name2", "four");

		container.renameFields(ImmutableMap.of("name", "newName", "name2", "newName2"));
		assertTrue(container.hasField("newName"));
		assertTrue(container.hasNotField("name"));

		assertThat(on(" ").join(container.valuesOf("newName")), equalTo("first second third"));

		assertTrue(container.hasField("newName2"));
		assertTrue(container.hasNotField("name2"));

		assertThat(on(" ").join(container.valuesOf("newName2")), equalTo("four"));

	}

	@Test
	public void shouldMergeMultiValuesFields() throws Exception {
		DataContainer container = new DataContainer()
				.add("name", "first")
				.add("name", "second")
				.add("name", "third");

		container.mergeValuesOf("name");

		assertTrue(container.hasField("name"));

		assertThat(container.valueOf("name"), equalTo("first second third"));
	}

	@Test
	public void shouldMergeMultiValuesFieldsWithGivenJoiner() throws Exception {
		DataContainer container = new DataContainer()
				.add("name", "first")
				.add("name", "second")
				.add("name", "third");

		container.mergeValuesOf("name", onMinusAndSpacesJoiner);

		assertTrue(container.hasField("name"));

		assertThat(container.valueOf("name"), equalTo("first - second - third"));
	}

	@Test
	public void containersWithSameValuesAreEquals() {

		DataContainer first = new DataContainer()
				.add("name", "value")
				.add("name", "value");

		DataContainer second = new DataContainer()
				.add("name", "value")
				.add("name", "value");

		assertThat(second, equalTo(first));

		second.add("difference", "different");
		assertThat(second, not(equalTo(first)));

	}

	@Test
	public void containerWithRawValues() throws Exception {

		DataContainer container = new DataContainer();
		container.add("stringVal", "this is a string");
		DataContainer container2 = new DataContainer();
		container.add("anothercontainer", container2);

		assertThat(container.valueOf("stringVal"), equalTo("this is a string"));
		String rawValueOf = container.rawValueOf("stringVal");
		assertThat(rawValueOf, equalTo("this is a string"));
		assertThat(container.valueOf("anothercontainer"), equalTo(container2.toString()));
		assertThat((DataContainer) container.rawValueOf("anothercontainer"), equalTo(container2));
	}

	@Test
	public void shouldFilterFiledsKeyByRegExp() throws Exception {
		DataContainer container = new DataContainer();

		container.add("prefix_me", "value");
		container.add("prefix_metoo", "value");
		container.add("meno_suffix", "value");

		Set<String> fields = container.fields(Pattern.compile("prefix_.*"));

		assertEquals(2, fields.size());
		assertTrue(fields.contains("prefix_me"));
		assertTrue(fields.contains("prefix_metoo"));

	}

	@Test
	public void shouldMergecontainers() throws Exception {
		DataContainer container = new DataContainer();
		container.add("name", "value1");
		container.add("name", "value2");

		DataContainer toMerge = new DataContainer();
		toMerge.add("name2", "value21");
		toMerge.add("name2", "value22");

		container.merge(toMerge);

		assertTrue(container.hasField("name2"));
		assertEquals(2, container.valuesOf("name2").size());

	}

	@Test
	public void shouldReplaceFieldValue() throws Exception {
		DataContainer container = new DataContainer()
				.add("name", "value1")
				.add("name", "value2");

		container.replace("name", "newValue");

		assertEquals("newValue", container.valueOf("name"));
		assertEquals(1, container.valuesOf("name").size());

		container = new DataContainer()
				.add("name", "value1");

		container.replace("name", Boolean.valueOf(container.hasField("name")));

		assertThat(container.valueOf("name"), equalTo("true"));
		assertEquals(1, container.valuesOf("name").size());

	}

	@Test
	public void shouldApplyFunctionToStringField() throws Exception {
		DataContainer container = new DataContainer();

		Function<String, String> fieldModifier = new Function<String, String>() {

			@Override
			public String apply(String input) {

				return "MODIFIED";
			}
		};

		container.add("name", "value1");

		container.apply("name", fieldModifier);

		assertEquals("MODIFIED", container.valueOf("name"));

	}

	@Test
	public void shouldApplyFunctionToObjectField() throws Exception {
		DataContainer container = new DataContainer();

		Function<Integer, Integer> fieldModifier = new Function<Integer, Integer>() {

			@Override
			public Integer apply(Integer input) {
				return Integer.valueOf(2);
			}
		};

		container.add("name", Integer.valueOf(1));

		container.apply("name", fieldModifier);

		assertEquals("2", container.valueOf("name"));

	}
}
