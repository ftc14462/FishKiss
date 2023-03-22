package com.ctecltd;

public class BadItemNameException extends Exception {

	private String name;
	private Exception e;

	public BadItemNameException(Exception e, String name) {
		this.name = name;
		this.e = e;
		// TODO Auto-generated constructor stub
	}

	public String getMessage() {
		Throwable cause = e.getCause();
		String causeString = "Unknown Cause";
		if (cause != null) {
			causeString = cause.toString();
		}
		Class<? extends Exception> type = e.getClass();

		String message = "Failed to read Item Name: \"" + name + "\"\n" + e.getMessage() + "\n" + causeString + "\n"
				+ type.toString() + "\n";
		StackTraceElement[] st = e.getStackTrace();
		for (StackTraceElement ste : st) {
			message += ste.getFileName() + ": " + ste.getMethodName() + ": " + ste.getLineNumber() + "\n";
		}
		return message;
	}

}
