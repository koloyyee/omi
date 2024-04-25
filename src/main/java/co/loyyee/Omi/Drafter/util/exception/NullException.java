package co.loyyee.Omi.Drafter.util.exception;

public class NullException extends OpenAiExceptionWorker {
	static private final String message = "API response still in progress or incomplete";

	public NullException() {
		this(message);
	}
	public NullException(String message) {
		super(message);
	}
}
