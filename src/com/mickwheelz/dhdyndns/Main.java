package com.mickwheelz.dhdyndns;

import java.util.Timer;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

public class Main implements Daemon {
	
	private static Utility util = new Utility();
	public static PreferenceStore prefs = util.parsePrefs();
	private static Timer timer = new Timer();

    public static void main(String[] args) {
		Integer timeInMs =  prefs.getTimeout()*6000;
		timer.schedule(new RefreshDNS(), 0, timeInMs);
    }

    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        System.out.println(Constants.MSG_INIT);
    }

    @Override
    public void start() throws Exception {
        System.out.println(Constants.MSG_START);
        main(null);
    }

    @Override
    public void stop() throws Exception {
        System.out.println(Constants.MSG_STOP);
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void destroy() {
        System.out.println(Constants.MSG_DONE);
    }

 }




