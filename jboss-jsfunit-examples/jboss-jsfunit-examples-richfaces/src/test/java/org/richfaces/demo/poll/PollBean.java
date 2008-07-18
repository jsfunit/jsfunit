/**
 * 
 */
package org.richfaces.demo.poll;

import java.util.Date;

/**
 * @author Ilya Shaikovsky
 *
 */
public class PollBean {
	
	private Date pollStartTime;
	private Date lastTime;
	private boolean pollEnabled;

	
	public PollBean() {
		pollEnabled=true;
	}
	public Date getDate() {
		lastTime = new Date();
		if (null==pollStartTime){
			pollStartTime = lastTime;
//			return lastTime;
		}
//		if ((lastTime.getTime()-pollStartTime.getTime())>=60000) setPollEnabled(false);
		return lastTime;
	}
	// Get last poll time (without modification)
	public Date getLasttime() {
		return lastTime;
	}

	public boolean getPollEnabled() {
		return pollEnabled;
	}

	public void setPollEnabled(boolean pollEnabled) {
		if (pollEnabled) setPollStartTime(null);
		this.pollEnabled = pollEnabled;
	}

	public Date getPollStartTime() {
		return pollStartTime;
	}

	public void setPollStartTime(Date pollStartTime) {
		this.pollStartTime = pollStartTime;
	}
	
}
