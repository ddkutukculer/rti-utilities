/**
 * 
 */
package com.dellemc.awg.model;

import java.util.Map;

/**
 * @author Doruk Kutukculer
 *
 */
public class OCSCCSTrigger {

	private Map<String, Object> fields;

	public Map<String, Object> getFields() {
		return fields;
	}

	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}

	public OCSCCSTrigger(Map<String, Object> fields2) {
		super();
		this.fields = fields2;
	}

	@Override
	public String toString() {
		return "OCSCCSTrigger [fields=" + fields + "]";
	}

	public String getKey() {
		String primaryId = ""+fields.get("primaryId");
		String eventStartTime = ""+fields.get("eventStartTime");
		
		return primaryId + "@" + eventStartTime + "000";
	}
	
	
}
