package org.ibboost.orqa.maven.services;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ibboost.orqa.maven.domain.OrqaExecution;
import org.ibboost.orqa.maven.utils.OutputHandler;

/**
 * Service responsible for executing an ORQA task
 * @author andrew.cowlin
 *
 */
public class OrqaExecutionService {

	private OutputHandler outputHandler;
	private OutputHandler errorHandler;

	public OrqaExecutionService(OutputHandler outputHandler, OutputHandler errorHandler) {
		this.outputHandler = outputHandler;
		this.errorHandler = errorHandler;
	}

	public OrqaExecutionResult execute(OrqaExecution execution) {

		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(execution.getOrqaHome());
		List<String> command = new ArrayList<>();
		command.add(execution.getExecutablePath());
		command.add("run");
		command.add(execution.getTaskPath());
		Map<String, String> taskParams = execution.getTaskParams();
		if (taskParams != null && taskParams.size() > 0) {
			for (Entry<String, String> entry : taskParams.entrySet()) {
				StringBuilder sb = new StringBuilder();
				sb.append(entry.getKey()).append("=").append(entry.getValue());
				command.add(sb.toString());
			}
		}

		command.add("--verbose");

		pb.command(command);
		pb.redirectOutput(Redirect.PIPE);
		pb.redirectError(Redirect.PIPE);
		try {
			Process p = pb.start();

			outputHandler.setInputStream(p.getInputStream());
			errorHandler.setInputStream(p.getErrorStream());
			Thread outputThread = new Thread(outputHandler);
			Thread errorThread = new Thread(errorHandler);
			outputThread.start();
			errorThread.start();
			p.waitFor();
			int exitCode = p.exitValue();
			if (exitCode == 0) {
				return OrqaExecutionResult.success("ORQA task ran successfully");
			} else {
				return OrqaExecutionResult.failure("ORQA task failed");
			}
		} catch (IOException | InterruptedException e) {
			return OrqaExecutionResult.failure("Technical error occurred running the ORQA script", e);
		}
	}
}
