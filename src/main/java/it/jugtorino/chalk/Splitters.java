package it.jugtorino.chalk;

import com.google.common.base.Splitter;

public class Splitters {

	public static final Splitter onSpaceSplitter = Splitter.on(' ').omitEmptyStrings().trimResults();

	public static final Splitter onUnderscoreSplitter = Splitter.on('_').omitEmptyStrings().trimResults();

	public static final Splitter onAmpSplitter = Splitter.on('&').omitEmptyStrings().trimResults();

	public static final Splitter onQuestionMarkSplitter = Splitter.on('?').omitEmptyStrings().trimResults();

	public static final Splitter onSlashSplitter = Splitter.on('/').omitEmptyStrings().trimResults();

	public static final Splitter onEmptySplitter = Splitter.on("").omitEmptyStrings().trimResults();

	public static final Splitter onMinusAndSpacesSplitter = Splitter.on(" - ").omitEmptyStrings().trimResults();

}
