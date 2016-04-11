package com.mickwheelz.dhdyndns;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

public class RefreshDNS extends TimerTask {

	private static Utility util = new Utility();
	private static String extIp;

	public static List<NameValuePair> params = util.setupAPICall(Main.prefs.getApiKey(), Constants.FORMAT_JSON);

	public void run() {		

		try {
			extIp = util.getExternalIp();
		}
		catch (Exception e) {
			System.out.println(Constants.MSG_NO_IP);
		}

		try {
			if(extIp != null) {
				DreamhostDNS dnsRecord = getRecord();

				if(dnsRecord == null) {
					//no existing record, create new
					addRecord(extIp, Main.prefs.getDomain());
				}
				else {
					//if ip hasn't changed, do nothing
					if(extIp.equalsIgnoreCase(dnsRecord.getValue())) {
						System.out.println(Constants.MSG_SAME_IP);
					}
					else {
						removeRecord(dnsRecord);
						addRecord(extIp, Main.prefs.getDomain());
						System.out.println(Constants.MSG_IP_CHANGE + extIp);

					}
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addRecord(String extIp, String domain) throws ParseException, IOException {

		DreamhostDNS newDnsRecord = new DreamhostDNS();

		newDnsRecord.setRecord(domain);
		newDnsRecord.setValue(extIp);
		newDnsRecord.setType(Constants.DNS_REC_TYPE);

		params = null;
		params = util.setupAPICall(Main.prefs.getApiKey(), Constants.FORMAT_JSON);


		HttpPost postAddRecord = util.addRemoveRecord(params, Constants.DNS_ADD_CMD, newDnsRecord);
		HttpResponse responseAddRecord = util.callAPI(postAddRecord);

		String respString = EntityUtils.toString(responseAddRecord.getEntity(), "UTF-8");
		System.out.println(respString);


	}

	private void removeRecord(DreamhostDNS dnsRecord) throws ParseException, IOException {

		params = null;
		params = util.setupAPICall(Main.prefs.getApiKey(), Constants.FORMAT_JSON);

		HttpPost postRemoveRecord = util.addRemoveRecord(params, Constants.DNS_RMV_CMD, dnsRecord);
		HttpResponse responseRemoveDNS = util.callAPI(postRemoveRecord);

		String stringDNSrmv = EntityUtils.toString(responseRemoveDNS.getEntity(), "UTF-8");
		System.out.println(stringDNSrmv);


	}

	private DreamhostDNS getRecord() throws ParseException, IOException {

		HttpPost postListRecords = util.listDNSRecord(params);			
		HttpResponse responseListRecords = util.callAPI(postListRecords);

		DreamhostDNSList listDNSRecords = util.parseDNSList(responseListRecords);

		DreamhostDNS dnsRecord = null;

		for(DreamhostDNS dns :listDNSRecords.getData() ) {

			if(dns.getRecord().equalsIgnoreCase(Main.prefs.getDomain())) {
				dnsRecord = dns;
			}

		}

		return dnsRecord;

	}
}