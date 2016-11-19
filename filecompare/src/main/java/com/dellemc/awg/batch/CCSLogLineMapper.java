/**
 * 
 */
package com.dellemc.awg.batch;

import java.util.Map;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;

import com.dellemc.awg.model.OCSCCSTrigger;

/**
 * @author Doruk Kutukculer
 *
 */
public class CCSLogLineMapper implements LineMapper<OCSCCSTrigger> {

	@Override
	public OCSCCSTrigger mapLine(String line, int lineNumber) throws Exception {

		if(line.isEmpty() || line == null) {
			return null;
		}
		
		int indexOf = line.indexOf('{');
		String substring = line.substring(indexOf);
		
		JsonParser parser = new BasicJsonParser();
		Map<String, Object> fields = parser.parseMap(substring);
		return new OCSCCSTrigger(fields);
	}
}
