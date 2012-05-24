package it.jugtorino.chalk;

import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static it.jugtorino.chalk.Joiners.onMinusAndSpacesJoiner;
import static it.jugtorino.chalk.Joiners.onSpaceJoiner;
import static com.google.common.base.Joiner.on;

public class AmphoraTest {

	@Test
	public void shoudBeAnErrorAmphora() {
		assertTrue(Amphora.ERROR.isError());
		assertFalse(Amphora.ERROR.isNotError());

	}

	@Test
	public void shouldRenameMultiValuesFields() throws Exception {

		Amphora amphora = new Amphora()
				.add("name", "first")
				.add("name", "second")
				.add("name", "third");

		amphora.rename("name", "newName");
		assertTrue(amphora.hasField("newName"));
		assertTrue(amphora.hasNotField("name"));

		assertThat(onSpaceJoiner.join(amphora.valuesOf("newName")), equalTo("first second third"));
	}

	@Test
	public void shouldRenameMultipleFields() throws Exception {
		Amphora amphora = new Amphora()
				.add("name", "first")
				.add("name", "second")
				.add("name", "third")
				.add("name2", "four");

		amphora.rename(ImmutableMap.of("name", "newName", "name2", "newName2"));

		assertTrue(amphora.hasField("newName"));
		assertTrue(amphora.hasNotField("name"));

		assertThat(on(" ").join(amphora.valuesOf("newName")), equalTo("first second third"));

		assertTrue(amphora.hasField("newName2"));
		assertTrue(amphora.hasNotField("name2"));

		assertThat(on(" ").join(amphora.valuesOf("newName2")), equalTo("four"));

	}

	@Test
	public void shouldMergeMultiValuesFields() throws Exception {
		Amphora amphora = new Amphora()
				.add("name", "first")
				.add("name", "second")
				.add("name", "third");

		amphora.mergeValuesOf("name");

		assertTrue(amphora.hasField("name"));

		assertThat(amphora.valueOf("name"), equalTo("first second third"));
	}

	@Test
	public void shouldMergeMultiValuesFieldsWithGivenJoiner() throws Exception {
		Amphora amphora = new Amphora()
				.add("name", "first")
				.add("name", "second")
				.add("name", "third");

		amphora.mergeValuesOf("name", onMinusAndSpacesJoiner);

		assertTrue(amphora.hasField("name"));

		assertThat(amphora.valueOf("name"), equalTo("first - second - third"));
	}

	@Test
	public void amphorasWithSameValuesAreEquals() {

		Amphora first = new Amphora()
				.add("name", "value")
				.add("name", "value");

		Amphora second = new Amphora()
				.add("name", "value")
				.add("name", "value");

		assertThat(second, equalTo(first));

		second.add("difference", "different");
		assertThat(second, not(equalTo(first)));

	}

	@Test
	public void amphoraWithRawValues() throws Exception {

		Amphora amphora2 = new Amphora();

		Amphora amphora = new Amphora()
				.add("stringVal", "this is a string")
				.add("anotheramphora", amphora2);

		assertThat(amphora.valueOf("stringVal"), equalTo("this is a string"));

		String rawValueOf = amphora.rawValueOf("stringVal");
		assertThat(rawValueOf, equalTo("this is a string"));

		assertThat(amphora.valueOf("anotheramphora"), equalTo(amphora2.toString()));
		Amphora anotheramphora = amphora.rawValueOf("anotheramphora");
		assertThat(anotheramphora, equalTo(amphora2));
	}

	@Test
	public void shouldFilterFiledsKeyByRegExp() throws Exception {
		Amphora amphora = new Amphora();

		amphora.add("prefix_me", "value");
		amphora.add("prefix_metoo", "value");
		amphora.add("meno_suffix", "value");

		Set<String> fields = amphora.fields(Pattern.compile("prefix_.*"));

		assertEquals(2, fields.size());
		assertTrue(fields.contains("prefix_me"));
		assertTrue(fields.contains("prefix_metoo"));

	}

	@Test
	public void shouldMergeamphoras() throws Exception {
		Amphora amphora = new Amphora();
		amphora.add("name", "value1");
		amphora.add("name", "value2");

		Amphora toMerge = new Amphora();
		toMerge.add("name2", "value21");
		toMerge.add("name2", "value22");

		amphora.merge(toMerge);

		assertTrue(amphora.hasField("name2"));
		assertEquals(2, amphora.valuesOf("name2").size());

	}

	@Test
	public void shouldReplaceFieldValue() throws Exception {
		Amphora amphora = new Amphora()
				.add("name", "value1")
				.add("name", "value2");

		amphora.replace("name", "newValue");

		assertEquals("newValue", amphora.valueOf("name"));
		assertEquals(1, amphora.valuesOf("name").size());

		amphora = new Amphora()
				.add("name", "value1");

		amphora.replace("name", Boolean.valueOf(amphora.hasField("name")));

		assertThat(amphora.valueOf("name"), equalTo("true"));
		assertEquals(1, amphora.valuesOf("name").size());

	}

	@Test
	public void shouldApplyFunctionToStringField() throws Exception {
		Amphora amphora = new Amphora();

		Function<String, String> fieldModifier = new Function<String, String>() {

			@Override
			public String apply(String input) {

				return "MODIFIED";
			}
		};

		amphora.add("name", "value1");

		amphora.modify("name", fieldModifier);

		assertThat(amphora.valueOf("name"), equalTo("MODIFIED"));

	}

	@Test
	public void shouldApplyFunctionToObjectField() throws Exception {
		Amphora amphora = new Amphora();

		Function<Integer, Integer> fieldModifier = new Function<Integer, Integer>() {

			@Override
			public Integer apply(Integer input) {
				return Integer.valueOf(2);
			}
		};

		amphora.add("name", Integer.valueOf(1));

		amphora.modify("name", fieldModifier);

		assertThat(amphora.valueOf("name"), equalTo("2"));
		assertThat((Integer) amphora.rawValueOf("name"), equalTo(2));
		assertEquals("2", amphora.valueOf("name"));
		assertEquals(2, amphora.rawValueOf("name"));

	}

	@Test
	public void etlExample() throws Exception {
		Function<Integer, Integer> incrementer = new Function<Integer, Integer>() {

			@Override
			public Integer apply(Integer input) {
				return ++input;
			}
		};

		Amphora amphora = new Amphora()
				.add("incrementMe", Integer.valueOf(1))
				.add("txt", "a very long text")
				.add("joinMe", "multi1")
				.add("joinMe", "multi2")
				.add("joinMe", "multi3")
				.add("joinMe", "multi4")
				.add("joinMe", "multi5")
				.add("splitMe", "one two three four")
				.add("renameME", new Amphora().add("name", "inner"));

		amphora.modify("incrementMe", incrementer)
				.mergeValuesOf("joinMe", Joiners.onSpaceJoiner)
				.split("splitMe")
				.rename("renameME", "renamed");

		Amphora littleAmphora = amphora.rawValueOf("renamed");
		Integer incremented = amphora.rawValueOf("incrementMe");

	}
}
