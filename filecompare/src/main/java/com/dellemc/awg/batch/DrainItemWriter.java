/**
 * 
 */
package com.dellemc.awg.batch;

import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemWriter;

import com.dellemc.awg.model.DrainerOutput;

/**
 * @author Doruk Kutukculer
 *
 */
public class DrainItemWriter implements ItemWriter<DrainerOutput> {

	private Map<String,DrainerOutput> output;
	
	@Override
	public void write(List<? extends DrainerOutput> items) throws Exception {
		for (DrainerOutput drainerOutput : items) {
			output.put(drainerOutput.getKey(), drainerOutput);
		}
		
	}

	public Map<String, DrainerOutput> getOutput() {
		return output;
	}

	public void setOutput(Map<String, DrainerOutput> output) {
		this.output = output;
	}

}
