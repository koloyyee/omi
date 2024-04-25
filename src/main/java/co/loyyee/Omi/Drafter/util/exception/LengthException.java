package co.loyyee.Omi.Drafter.util.exception;

public class LengthException extends OpenAiExceptionWorker{
	static private final String message = "Incomplete model output due to max_tokens parameter or token limit";
	public LengthException() {
		this(message);
	}
	public LengthException(String message) {
		super(message);
	}
}
