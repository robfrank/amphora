package it.jugtorino.chalk;

import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

public class Joiners {

	public static final Joiner onSpaceJoiner = Joiner.on(' ').skipNulls();

	public static final Joiner onUnderscoreJoiner = Joiner.on('_').skipNulls();

	public static final Joiner onAmpJoiner = Joiner.on('&').skipNulls();

	public static final Joiner onQuestionMarkJoiner = Joiner.on('?').skipNulls();

	public static final Joiner onSlashJoiner = Joiner.on('/').skipNulls();

	public static final Joiner onEmptyJoiner = Joiner.on("").skipNulls();

	public static final Joiner onMinusAndSpacesJoiner = Joiner.on(" - ").skipNulls();

	private static final Map<String, Joiner> joiners = ImmutableMap.<String, Joiner> builder()
			.put(" ", onSpaceJoiner)
			.put("_", onUnderscoreJoiner)
			.put("&", onAmpJoiner)
			.put("_", onUnderscoreJoiner)
			.put("/", onSlashJoiner)
			.put(" - ", onMinusAndSpacesJoiner)
			.build();

	public static Joiner onSeparatorJoiner(String separator) {
		if (joiners.containsKey(separator)) return joiners.get(separator);
		return Joiner.on(separator).skipNulls();
	}

}
