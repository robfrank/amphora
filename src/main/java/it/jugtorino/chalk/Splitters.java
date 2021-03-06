package it.jugtorino.chalk;

import java.util.Map;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

public class Splitters {

	private Splitters(){
		// Private constructor for utility class    
	}
    
	public static final Splitter onSpaceSplitter = Splitter.on(' ').omitEmptyStrings().trimResults();

	public static final Splitter onCommaSplitter = Splitter.on(',').omitEmptyStrings().trimResults();

	public static final Splitter onSemicolonSplitter = Splitter.on(';').omitEmptyStrings().trimResults();

	public static final Splitter onUnderscoreSplitter = Splitter.on('_').omitEmptyStrings().trimResults();

	public static final Splitter onAmpSplitter = Splitter.on('&').omitEmptyStrings().trimResults();

	public static final Splitter onQuestionMarkSplitter = Splitter.on('?').omitEmptyStrings().trimResults();

	public static final Splitter onSlashSplitter = Splitter.on('/').omitEmptyStrings().trimResults();

	public static final Splitter onMinusAndSpacesSplitter = Splitter.on(" - ").omitEmptyStrings().trimResults();

	private static final Map<String, Splitter> separatorSplitters = ImmutableMap.<String, Splitter> builder()
			.put(" ", onSpaceSplitter)
			.put(",", onCommaSplitter)
			.put(";", onSemicolonSplitter)
			.put("_", onUnderscoreSplitter)
			.put("/", onSlashSplitter)
			.put(" - ", onMinusAndSpacesSplitter).build();

	public static Splitter onSeparatorSplitter(String separator) {
		if (separatorSplitters.containsKey(separator)) return separatorSplitters.get(separator);
		return Splitter.on(separator).omitEmptyStrings().trimResults();
	}

}
