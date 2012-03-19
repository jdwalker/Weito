package debug;

public interface IDebuggable {
	public enum DebugFlag {
		ON,OFF
	}

	public void setDebugFlags(DebugFlag flags);
}
