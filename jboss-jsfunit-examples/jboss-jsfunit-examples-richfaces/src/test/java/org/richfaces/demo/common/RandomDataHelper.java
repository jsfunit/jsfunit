package org.richfaces.demo.common;

public class RandomDataHelper {
	public static int random(int min, int max) {
		assert(min<=max);
		return min+(int)Math.round(Math.random()*(double)(max-min));
	}
	public static Object random(Object values[]) {
		assert(values!=null);
		return values[random(0,values.length-1)];
	}
	private static char randomChar() {
		if (Math.random()>0.5) {
			return (char)((int)'0'+random(0,9));
		} else {
			return (char)((int)'A'+random(0,25));
		}
	}
	public static String randomString(int length) {
		StringBuffer buf = new StringBuffer();
		for (int counter=0;counter<length;counter++) {
			buf.append(randomChar());
		}
		return buf.toString();
	}
}

