package com.mickwheelz.dhdyndns;

public class Constants {

	public static final String IP_CHECK_URL = "http://checkip.amazonaws.com";
	public static final String API_URL = "https://api.dreamhost.com";
	public static final String FORMAT_PARAM = "format";
	public static final String FORMAT_JSON = "JSON";
	public static final String KEY_PARAM = "key";
	public static final String CMD_PARAM = "cmd";
	public static final String UID_PARAM = "unique_id";
	
	public static final String DNS_RECORD = "record";
	public static final String DNS_VALUE = "value";
	public static final String DNS_TYPE = "type";
	
	public static final String DNS_ADD_CMD = "dns-add_record";
	public static final String DNS_RMV_CMD = "dns-remove_record";
	public static final String DNS_LST_CMD = "dns-list_records";
	
	public static final String DNS_REC_TYPE = "A";
	
	public static final String PREFS_FILENAME = "preferences.json";
	
	public static final String MSG_NO_IP = "Unable to get IP, will try again in " + Main.prefs.getTimeout() + "m";
	public static final String MSG_SAME_IP = "IP has not changed";
	public static final String MSG_IP_CHANGE = "IP is now set to ";
	
	
}
