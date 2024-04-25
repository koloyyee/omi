package co.loyyee.Omi.Drafter.util.exception;

public class ContentFilterException extends OpenAiExceptionWorker {
	static private final String message = "Omitted content due to a flag from our content filters";
	public ContentFilterException() {
		this(message);
	}
	public ContentFilterException(String message) {
		super(message);
	}
}
