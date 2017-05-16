package org.ibboost.orqa.maven;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.ibboost.orqa.maven.domain.OrqaExecution;
import org.ibboost.orqa.maven.services.OrqaExecutionResult;
import org.ibboost.orqa.maven.services.OrqaExecutionService;
import org.ibboost.orqa.maven.utils.OutputHandler;

/**
 * Executes an ORQA task as part of a maven build cycle.
 *
 * @goal execute - Executes an ORQA task
 * @author andrew.cowlin
 *
 */
@Mojo(name = "execute", defaultPhase = LifecyclePhase.INTEGRATION_TEST, inheritByDefault = false)
public class ExecuteMojo extends AbstractMojo {

	private static final String ORQA_HOME_PROPERTY = "ORQA_HOME";

	@Parameter(property = "execute.orqaHome")
	private File orqaHome;

	@Parameter(property = "execute.task", required = true)
	private String task;

	@Parameter
	private Map<String, String> taskParams;

	public void execute() throws MojoExecutionException, MojoFailureException {

		if (orqaHome == null) {
			orqaHome = getDefaultOrqaHome();
		}

		if (orqaHome == null) {
			reportError("ORQA Home is not set. Either set the orqaHome plugin parameter or the ORQA_HOME environment variable");
		}

		if (!(orqaHome.exists() && orqaHome.isDirectory())) {
			reportError("The configured ORQA location is not a valid directory. [" + orqaHome + "]");
		}

		OrqaExecution execution = new OrqaExecution(orqaHome, task, taskParams);

		getLog().info("Using ORQA distribution in " + orqaHome.getAbsolutePath());
		getLog().info("Executing task " + task);

		if (taskParams != null && taskParams.size() > 0) {
			getLog().info("Task parameters are:");

			for (Entry<String, String> param : taskParams.entrySet()) {
				getLog().info(param.getKey() + ": " + param.getValue());
			}
		} else {
			getLog().info("No task parameters specified");
		}
		getLog().info("");
		getLog().info("Launching ORQA...");
		getLog().info("");

		OutputHandler outputHandler = OutputHandler.createOutputHandler(getLog());
		OutputHandler errorHandler = OutputHandler.createErrorHandler(getLog());
		OrqaExecutionService orqaService = new OrqaExecutionService(outputHandler, errorHandler);
		OrqaExecutionResult result = orqaService.execute(execution);

		if (!result.getSuccess()) {
			if (result.getException() != null) {
				throw new MojoFailureException(result.getMessage(), result.getException());
			} else {
				throw new MojoFailureException(result.getMessage());
			}
		}
	}

	private void reportError(String msg) throws MojoFailureException {
		getLog().error(msg);
		throw new MojoFailureException(msg);
	}

	private File getDefaultOrqaHome() {
		String orqaHomeEnv = System.getenv(ORQA_HOME_PROPERTY);
		if (orqaHomeEnv != null) {
			getLog().info("Using ORQA_HOME environment variable");
			getLog().info("ORQA_HOME: " + orqaHomeEnv);
			return new File(orqaHomeEnv);
		}

		return null;
	}
}
