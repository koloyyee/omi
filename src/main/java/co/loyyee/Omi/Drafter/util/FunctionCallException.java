package co.loyyee.Omi.Drafter.util;

public class FunctionCallException extends OpenAiExceptionWorker {
	static private final String message = "The model decided to call a function";
	public FunctionCallException() {
		this(message);
	}
	public FunctionCallException(String message) {
		super(message);
	}
}
