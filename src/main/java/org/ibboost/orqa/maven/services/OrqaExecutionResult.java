package org.ibboost.orqa.maven.services;

/**
 * Class representing the result of an ORQA task execution
 * @author andrew.cowlin
 *
 */
public class OrqaExecutionResult {

	private String message;

	private Boolean success;

	private Throwable exception;

	public static OrqaExecutionResult success(String message) {
		return new OrqaExecutionResult(true, message, null);
	}

	public static OrqaExecutionResult failure(String message, Throwable exception) {
		return new OrqaExecutionResult(false, message, exception);
	}

	public static OrqaExecutionResult failure(String message) {
		return new OrqaExecutionResult(false, message, null);
	}

	private OrqaExecutionResult(Boolean success, String message, Throwable exception) {
		this.success = success;
		this.message = message;
		this.exception = exception;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
}
