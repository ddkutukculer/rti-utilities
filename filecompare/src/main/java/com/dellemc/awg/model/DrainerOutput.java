/**
 * 
 */
package com.dellemc.awg.model;

import java.util.Arrays;

/**
 * @author Doruk Kutukculer
 *
 */
public class DrainerOutput {

	private String primaryID;
	private String eventTime;
	
	private String[] line;

	public String getPrimaryID() {
		return primaryID;
	}

	public void setPrimaryID(String primaryID) {
		this.primaryID = primaryID;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String[] getLine() {
		return line;
	}

	public void setLine(String[] line) {
		this.line = line;
	}

	public DrainerOutput(String primaryID, String eventTime, String[] line) {
		super();
		this.primaryID = primaryID;
		this.eventTime = eventTime;
		this.line = line;
	}

	public String getKey() {
		return primaryID+'@'+eventTime;
	}

	@Override
	public String toString() {
		return "DrainerOutput [primaryID=" + primaryID + ", eventTime="
				+ eventTime + ", output=" + Arrays.toString(line) + "]";
	}
	
}
