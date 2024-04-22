package co.loyyee.Omi.Drafter.util;

public class OpenAiException extends RuntimeException {
	public OpenAiException(String finishReason) {
		switch(finishReason.toLowerCase()) {
			case "length" -> new LengthException();
			case "content_filter" -> new ContentFilterException();
			case "tool_call" -> new FunctionCallException();
			case "null" -> new NullException();
			default -> new NullException();

		}
	}

	protected  class OpenAiExceptionWorker extends Exception {
		public OpenAiExceptionWorker(String message ) {
			super(message);
		}
	}

	protected class LengthException extends OpenAiExceptionWorker{
		static private final String message = "Incomplete model output due to max_tokens parameter or token limit";
		public LengthException() {
			this(message);
		}
		public LengthException(String message) {
			super(message);
		}
	}
	protected class FunctionCallException extends OpenAiExceptionWorker {
		static private final String message = "The model decided to call a function";
		public FunctionCallException() {
			this(message);
		}
		public FunctionCallException(String message) {
			super(message);
		}
	}
	protected class ContentFilterException extends OpenAiExceptionWorker {
		static private final String message = "Omitted content due to a flag from our content filters";
		public ContentFilterException() {
			this(message);
		}
		public ContentFilterException(String message) {
			super(message);
		}
	}
	protected class NullException extends OpenAiExceptionWorker {
		static private final String message = "API response still in progress or incomplete";

		public NullException() {
			this(message);
		}
		public NullException(String message) {
			super(message);
		}
	}
}

