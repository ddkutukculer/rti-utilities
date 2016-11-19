/**
 * 
 */
package com.dellemc.awg.batch;

import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemWriter;

import com.dellemc.awg.model.CheckResult;
import com.dellemc.awg.model.DrainerOutput;
import com.dellemc.awg.model.OCSCCSTrigger;

/**
 * @author Doruk Kutukculer
 *
 */
public class TriggerOutputItemWriter implements ItemWriter<OCSCCSTrigger> {

	private Map<String,DrainerOutput> output;
	
	private Map<String,OCSCCSTrigger> missingTriggers;

	private Map<String,CheckResult> validatedTriggers;
	
	@Override
	public void write(List<? extends OCSCCSTrigger> items) throws Exception {

		for (OCSCCSTrigger ocsccsTrigger : items) {
			String key = ocsccsTrigger.getKey();
			DrainerOutput drainerOutput = output.get(key);
			if(drainerOutput == null) {
				//report missing
				//add trigger to missing list
				missingTriggers.put(key, ocsccsTrigger);
				
			} else {
				//report success
				//RESULT(trigger, DrainerLog)
				validatedTriggers.put(key, new CheckResult(true, ocsccsTrigger, drainerOutput));
			}
		}
	}

	public Map<String, DrainerOutput> getOutput() {
		return output;
	}

	public void setOutput(Map<String, DrainerOutput> output) {
		this.output = output;
	}

	public Map<String, OCSCCSTrigger> getMissingTriggers() {
		return missingTriggers;
	}

	public void setMissingTriggers(Map<String, OCSCCSTrigger> missingTriggers) {
		this.missingTriggers = missingTriggers;
	}

	public Map<String, CheckResult> getValidatedTriggers() {
		return validatedTriggers;
	}

	public void setValidatedTriggers(Map<String, CheckResult> validatedTriggers) {
		this.validatedTriggers = validatedTriggers;
	}

}

