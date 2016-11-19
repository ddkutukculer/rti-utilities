/**
 * 
 */
package com.dellemc.awg.batch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.ArrayFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;

import com.dellemc.awg.model.CheckResult;
import com.dellemc.awg.model.DrainerOutput;
import com.dellemc.awg.model.OCSCCSTrigger;

/**
 * @author Doruk Kutukculer
 *
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Value("${output.dir}")
	private String outputDir;
	
	@Value("${ocs.ccs.log.file}")
	private String ocsCcsLogFile;

	@Value("${drainer.log.file}")
	private String drainerLogFile;

	@Value("${report}")
	private boolean report;
	
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;


	@Bean
	public FlatFileItemReader<String[]> reader() {
		FlatFileItemReader<String[]> reader = new FlatFileItemReader<String[]>();
		reader.setResource(new PathResource(drainerLogFile));
		reader.setLineMapper(new DefaultLineMapper<String[]>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer());
				
				setFieldSetMapper(new ArrayFieldSetMapper());
			}
		});
		return reader;
	}

	@Bean
	public FlatFileItemReader<OCSCCSTrigger> triggerReader() {
		FlatFileItemReader<OCSCCSTrigger> reader = new FlatFileItemReader<OCSCCSTrigger>();
		reader.setResource(new PathResource(ocsCcsLogFile));
		reader.setLineMapper(new CCSLogLineMapper());
		return reader;
	}

	@Bean
	public DrainLogItemProcessor processor() {
		return new DrainLogItemProcessor();
	}

	@Bean
	public Map<String,DrainerOutput> messageMap() {
		return new HashMap<>();
	}
	
	@Bean
	public DrainItemWriter writer() {
		DrainItemWriter writer = new DrainItemWriter();
		writer.setOutput(messageMap());
		return writer;
	}

	@Bean
	public TriggerOutputItemWriter triggerWriter() {
		TriggerOutputItemWriter writer = new TriggerOutputItemWriter();
		writer.setOutput(messageMap());
		writer.setMissingTriggers(missingTriggers());
		writer.setValidatedTriggers(validatedTriggers());
		return writer;
	}

	@Bean
	public Map<String, CheckResult> validatedTriggers() {
		return new HashMap<>();
	}
	
	@Bean
	public Map<String, OCSCCSTrigger> missingTriggers() {
		return new HashMap<>();
	}

	@Bean
	public JobExecutionListener listener() {
		return new JobCompletionNotificationListener(messageMap(), missingTriggers(), validatedTriggers(), outputDir, report);
	}

	@Bean
	public Job importUserJob() {
		return jobBuilderFactory.get("importUserJob")
				.incrementer(new RunIdIncrementer()).listener(listener())
				.flow(step1()).next(step2()).end().build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<String[], DrainerOutput> chunk(100)
				.reader(reader()).processor(processor()).writer(writer())
				.build();
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2").<OCSCCSTrigger, OCSCCSTrigger> chunk(100)
				.reader(triggerReader()).writer(triggerWriter())
				.build();
	}
}
