package com.dellemc.awg.batch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.dellemc.awg.model.CheckResult;
import com.dellemc.awg.model.DrainerOutput;
import com.dellemc.awg.model.OCSCCSTrigger;

public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private final Map<String,DrainerOutput> messageMap;

	private final Map<String, OCSCCSTrigger> missingTriggers;

	private final Map<String, CheckResult> validatedTriggers;
	
	private String outputDir;
	private boolean report;

	@Autowired
	public JobCompletionNotificationListener(Map<String,DrainerOutput> map, Map<String, OCSCCSTrigger> missingTriggers, Map<String, CheckResult> validatedTriggers, String outputDir, boolean report) {
		this.messageMap = map;
		this.missingTriggers = missingTriggers;
		this.validatedTriggers = validatedTriggers;
		this.outputDir = outputDir;
		this.report=report;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");

		//	log.info(messageMap.toString());

			log.info("DRAINED_MESSAGES: " + messageMap.size());
			log.info("MISSING_TRIGGERS: " + missingTriggers.size());
			log.info("VALIDATED_TRIGGERS: " + validatedTriggers.size());

			
			if(report) {
				SimpleDateFormat format = new SimpleDateFormat("YYYYMMDD");
				Date date = new Date();
				String curDate = format.format(date);
				
				String missingFilePath= outputDir+"/missingTriggers_" + curDate +".log";
				String validatedFilePath= outputDir+"/validatedTriggers_" + curDate +".log";
				Path missingPath = Paths.get(missingFilePath);
				Path validatedPath = Paths.get(validatedFilePath);
				
				String missing = prettyPrintMap(missingTriggers);
				String validated = prettyPrintMap(validatedTriggers);
				try {
					Files.write(missingPath, missing.getBytes());
					Files.write(validatedPath, validated.getBytes());
				} catch (IOException e) {
					log.error("Exception occurred writing report", e);
				}
			}
		}
	}

	private String prettyPrintMap(Map<String,? extends Object> map) {

		StringBuilder result = new StringBuilder("");
		for (String key : map.keySet()) {
			Object value = map.get(key);
			result.append(key).append("_").append(value).append("\n");
		}
		return result.toString();
	}
}