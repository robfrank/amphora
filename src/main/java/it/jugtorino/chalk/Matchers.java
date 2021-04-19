package it.jugtorino.chalk;

import org.hamcrest.Matcher;

public class Matchers {

	private Matchers(){
		// Private constructor for utility class    
	}

        public static <T> boolean checkThat(T actual, Matcher<? super T> matcher) {
		return matcher.matches(actual);
	}

}
