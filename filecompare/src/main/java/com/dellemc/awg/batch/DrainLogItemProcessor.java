/**
 * 
 */
package com.dellemc.awg.batch;

import org.springframework.batch.item.ItemProcessor;

import com.dellemc.awg.model.DrainerOutput;

/**
 * @author Doruk Kutukculer
 *
 */
public class DrainLogItemProcessor implements ItemProcessor<String[], DrainerOutput> {

	@Override
	public DrainerOutput process(String[] item) throws Exception {
		if(item.length > 0) {
			String primaryId = item[1];
			String eventTime = item[4];
			
			
			return new DrainerOutput(primaryId, eventTime, item);
		}
		
		return null;
	}

	
}
