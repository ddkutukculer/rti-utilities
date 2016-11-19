/**
 * 
 */
package com.dellemc.awg.model;


/**
 * @author Doruk Kutukculer
 *
 */
public class CheckResult {

	boolean result;
	private OCSCCSTrigger trigger;
	private DrainerOutput drainerLog;
	
	public OCSCCSTrigger getTrigger() {
		return trigger;
	}
	public void setTrigger(OCSCCSTrigger trigger) {
		this.trigger = trigger;
	}
	public DrainerOutput getDrainerLog() {
		return drainerLog;
	}
	public void setDrainerLog(DrainerOutput drainerLog) {
		this.drainerLog = drainerLog;
	}
	public CheckResult(boolean result, OCSCCSTrigger trigger,
			DrainerOutput drainerLog) {
		super();
		this.result = result;
		this.trigger = trigger;
		this.drainerLog = drainerLog;
	}
	@Override
	public String toString() {
		return "CheckResult [result=" + result + ", TRIGGER_REQUEST=" + trigger
				+ ", RTI_OUTPUT=" + drainerLog + "]";
	}
	
	
}
