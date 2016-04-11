package com.mickwheelz.dhdyndns;

import java.util.Timer;

public class Main {
	
	private static Utility util = new Utility();
	public static PreferenceStore prefs = util.parsePrefs();
	private static Timer timer = new Timer();

	public static void main(String[] args) {
		
		Integer timeInMs =  prefs.getTimeout()*6000;
		timer.schedule(new RefreshDNS(), 0, timeInMs);

	}
}




