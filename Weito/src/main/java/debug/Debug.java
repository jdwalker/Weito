package debug;

import java.util.EnumSet;

public class Debug {
	public enum DebugMode {
		FORMAT,FEATURE,FEATURESPEC,KEYWORD,DROOLSLOG,DROOLSSTAGEENTER
	}
	
	private static EnumSet<DebugMode> mode = EnumSet.noneOf(DebugMode.class);

	public static EnumSet<DebugMode> getMode() {
		return mode;
	}

	public static void setMode(EnumSet<DebugMode> mode) {
		Debug.mode = mode;
	}
	
}
