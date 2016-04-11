package com.mickwheelz.dhdyndns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class Utility {

	public String getExternalIp() throws Exception {

		URL ipCheck = new URL(Constants.IP_CHECK_URL);
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					ipCheck.openStream()));
			String ip = in.readLine();
			return ip;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<NameValuePair> setupAPICall(String apiKey, String format) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Constants.KEY_PARAM, apiKey));
		params.add(new BasicNameValuePair(Constants.FORMAT_PARAM, format));

		return params;
	}

	public HttpPost listDNSRecord(List<NameValuePair> params) {

		HttpPost httppost = new HttpPost(Constants.API_URL);

		params.add(new BasicNameValuePair(Constants.CMD_PARAM, Constants.DNS_LST_CMD));

		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			return httppost;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}


	}

	public HttpPost addRemoveRecord(List<NameValuePair> params, String command, DreamhostDNS dnsRecord) { 

		HttpPost httppost = new HttpPost(Constants.API_URL);

		params.add(new BasicNameValuePair(Constants.CMD_PARAM, command));

		params.add(new BasicNameValuePair(Constants.DNS_RECORD, dnsRecord.getRecord()));
		params.add(new BasicNameValuePair(Constants.DNS_VALUE, dnsRecord.getValue()));
		params.add(new BasicNameValuePair(Constants.DNS_TYPE, dnsRecord.getType()));


		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			return httppost;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}

	}

	public HttpResponse callAPI(HttpPost post) {

		HttpClient httpclient = HttpClients.createDefault();
		HttpResponse response;

		try {
			response = httpclient.execute(post);
			return response;

		} 
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public DreamhostResult parseFinalResponse (HttpResponse response) throws ParseException, IOException {

		HttpEntity entity = response.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");

		Gson gson = new Gson();			
		DreamhostResult result = gson.fromJson(responseString, DreamhostResult.class);			

		System.out.println("Result: " + result.getResult());
		System.out.println("Data: " + result.getData());

		return result;

	}

	public DreamhostDNSList parseDNSList(HttpResponse response) throws ParseException, IOException {

		HttpEntity entity = response.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");

		Gson gson = new Gson();			

		DreamhostDNSList result = gson.fromJson(responseString, DreamhostDNSList.class);	

		return result;

	}

	public File preferencesToFile(PreferenceStore preferences, String fileName) {

		File prefsFile = new File(fileName);

		//Write default preferences to a file
		try {
			Gson gson = new Gson();
			String jsonPrefs = gson.toJson(preferences);

			prefsFile.createNewFile();

			FileWriter writer = new FileWriter(prefsFile); 
			writer.write(jsonPrefs); 
			writer.flush(); 
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return prefsFile;
	}

	public PreferenceStore readPreferences(String fileName) throws IOException{

		String json = "";
		Gson gson = new Gson();

		File prefsFile = new File(fileName);
		BufferedReader br = new BufferedReader(new FileReader(prefsFile));

		String sCurrentLine;

		while ((sCurrentLine = br.readLine()) != null) {
			json += sCurrentLine;
		}
		br.close();

		PreferenceStore prefs = gson.fromJson(json, PreferenceStore.class);

		return prefs;

	}

	public void createDefaultPrefs() {
		
		PreferenceStore preferences = new PreferenceStore();
		
		preferences.setApiKey("api-key-here");
		preferences.setDomain("domain-name-here");
		preferences.setTimeout(30);
		
		//Write to Preferences File
		preferencesToFile(preferences, Constants.PREFS_FILENAME);

	}

	public PreferenceStore parsePrefs() {
		
		PreferenceStore prefs = null;
		
		try {
			prefs = readPreferences(Constants.PREFS_FILENAME);
		} catch (IOException e) {
			createDefaultPrefs();
		}
		
		if(prefs == null) {
			createDefaultPrefs();
			try {
				prefs = readPreferences(Constants.PREFS_FILENAME);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return prefs;
		
	}
}

