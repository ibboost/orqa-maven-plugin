package org.ibboost.orqa.maven.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.maven.plugin.logging.Log;

public class OutputHandler implements Runnable {

	private Log log;

	private InputStream in;

	private boolean isErrorStream;

	public static OutputHandler createOutputHandler(Log log) {
		return new OutputHandler(log, false);
	}

	public static OutputHandler createErrorHandler(Log log) {
		return new OutputHandler(log, true);
	}

	private OutputHandler(Log log, boolean isErrorStream) {
		this.isErrorStream = isErrorStream;
		this.log = log;
	}

	public void setInputStream(InputStream in) {
		this.in = in;
	}

	@Override
	public void run() {
		try {
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (isErrorStream) {
					log.error(line);
				} else {
					log.info(line);
				}
			}
			reader.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
