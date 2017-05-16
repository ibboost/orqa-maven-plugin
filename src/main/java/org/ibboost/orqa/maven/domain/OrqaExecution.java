package org.ibboost.orqa.maven.domain;

import java.io.File;
import java.util.Map;

/**
 * OrqaExecution represents all the information required to execute an ORQA task
 * @author andrew.cowlin
 *
 */
public class OrqaExecution {

	private static final String EXECUTABLE_NAME = "orqac";

	private File orqaHome;

	private String taskPath;

	private Map<String, String> taskParams;

	public OrqaExecution(File orqaHome, String taskPath, Map<String, String> taskParams) {
		this.orqaHome = orqaHome;
		this.taskPath = taskPath;
		this.taskParams = taskParams;
	}

	public File getOrqaHome() {
		return orqaHome;
	}

	public void setOrqaHome(File orqaHome) {
		this.orqaHome = orqaHome;
	}

	public String getTaskPath() {
		return taskPath;
	}

	public void setTaskPath(String taskPath) {
		this.taskPath = taskPath;
	}

	public Map<String, String> getTaskParams() {
		return taskParams;
	}

	public void setTaskParams(Map<String, String> taskParams) {
		this.taskParams = taskParams;
	}

	public String getExecutableName() {
		return EXECUTABLE_NAME;
	}

	public String getExecutablePath() {
		return orqaHome.getAbsolutePath() + File.separator + EXECUTABLE_NAME;
	}
}
