package it.jugtorino.chalk;

import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

public class Joiners {

	private Joiners(){
		// Private constructor for utility class    
	}
        
	public static final Joiner onSpaceJoiner = Joiner.on(' ').skipNulls();

	public static final Joiner onUnderscoreJoiner = Joiner.on('_').skipNulls();

	public static final Joiner onAmpJoiner = Joiner.on('&').skipNulls();

	public static final Joiner onQuestionMarkJoiner = Joiner.on('?').skipNulls();

	public static final Joiner onSlashJoiner = Joiner.on('/').skipNulls();

	public static final Joiner onEmptyJoiner = Joiner.on("").skipNulls();

	public static final Joiner onMinusAndSpacesJoiner = Joiner.on(" - ").skipNulls();

	private static final Map<String, Joiner> separatorJoiners = ImmutableMap.<String, Joiner> builder()
			.put(" ", onSpaceJoiner)
			.put("_", onUnderscoreJoiner)
			.put("&", onAmpJoiner)
			.put("/", onSlashJoiner)
			.put(" - ", onMinusAndSpacesJoiner)
			.build();

	public static Joiner onSeparatorJoiner(String separator) {
		if (separatorJoiners.containsKey(separator)) return separatorJoiners.get(separator);
		return Joiner.on(separator).skipNulls();
	}

}
