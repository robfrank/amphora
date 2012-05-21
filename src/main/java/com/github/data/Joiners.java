package com.github.data;

import com.google.common.base.Joiner;

public class Joiners {

	public static final Joiner onSpaceJoiner = Joiner.on(' ').skipNulls();

	public static final Joiner onUnderscoreJoiner = Joiner.on('_').skipNulls();

	public static final Joiner onAmpJoiner = Joiner.on('&').skipNulls();

	public static final Joiner onQuestionMarkJoiner = Joiner.on('?').skipNulls();

	public static final Joiner onSlashJoiner = Joiner.on('/').skipNulls();

	public static final Joiner onEmptyJoiner = Joiner.on("").skipNulls();

	public static final Joiner onMinusAndSpacesJoiner = Joiner.on(" - ").skipNulls();

}
