package com.charmsoffice.mobilestudio.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class CommunicationLayer {

	public static String getConfirmation(String userName, String sid, String action) throws Exception 
	{
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("username", userName));
		postData.add((NameValuePair) new BasicNameValuePair("sid", sid));
		postData.add((NameValuePair) new BasicNameValuePair("action", action));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer("https://www.charmsoffice" +
				".com/charms/mobileStudio/mobileStudioExchange.asp?username=" + userName.replace(" ", "%20") + "&sid=" + sid
				+ "&action=" + action, postData, true);
		return serverResponse;
	}

	public static String getAssignmentList(String userName, String sid,
			String action) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("username",
				userName));
		postData.add((NameValuePair) new BasicNameValuePair("sid", sid));
		postData.add((NameValuePair) new BasicNameValuePair("action", action));

		String serverResponse = WebServerOperation
				.sendHTTPPostRequestToServer(
						"https://www.charmsoffice.com/charms/mobileStudio/mobileStudioExchange.asp?username="
								+ userName
								+ "&sid="
								+ sid
								+ "&action="
								+ action, postData, true);
		return serverResponse;
	}

	public static String getUploadResponse(String userName, String sid,
			String action, String fileName) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("username",
				userName));
		postData.add((NameValuePair) new BasicNameValuePair("sid", sid));
		postData.add((NameValuePair) new BasicNameValuePair("action", action));
		postData.add((NameValuePair) new BasicNameValuePair("filename",
				fileName));

		String serverResponse = WebServerOperation
				.sendHTTPPostRequestToServer(
						"https://www.charmsoffice.com/charms/mobileStudio/mobileStudioUpload.asp?username="
								+ userName
								+ "&sid="
								+ sid
								+ "&action="
								+ action + "&filename=" + fileName, postData,
						true);
		return serverResponse;
	}

	public static String getRecordMeList(String userName, String sid,
			String action) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("username",
				userName));
		postData.add((NameValuePair) new BasicNameValuePair("sid", sid));
		postData.add((NameValuePair) new BasicNameValuePair("action", action));

		String serverResponse = WebServerOperation
				.sendHTTPPostRequestToServer(
						"https://www.charmsoffice.com/charms/mobileStudio/mobileStudioExchange.asp?username="
								+ userName
								+ "&sid="
								+ sid
								+ "&action="
								+ action, postData, true);
		return serverResponse;
	}

	public static String getMyRecordingList(String userName, String sid,
			String action) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("username",
				userName));
		postData.add((NameValuePair) new BasicNameValuePair("sid", sid));
		postData.add((NameValuePair) new BasicNameValuePair("action", action));

		String serverResponse = WebServerOperation
				.sendHTTPPostRequestToServer(
						"https://www.charmsoffice.com/charms/mobileStudio/mobileStudioExchange.asp?username="
								+ userName
								+ "&sid="
								+ sid
								+ "&action="
								+ action, postData, true);
		return serverResponse;
	}
	
	public static String getFileNameCheck(String userName, String sid, String action, String fileName) throws Exception 
	{
		List<NameValuePair> postData = new ArrayList<NameValuePair>(4);
		postData.add((NameValuePair) new BasicNameValuePair("username", userName));
		postData.add((NameValuePair) new BasicNameValuePair("sid", sid));
		postData.add((NameValuePair) new BasicNameValuePair("action", action));
		postData.add((NameValuePair) new BasicNameValuePair("filename", fileName));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer("https://www.charmsoffice.com/charms/mobileStudio/mobileStudioUpload.asp?" +
				"username=" + userName + "&sid=" + sid+ "&action=" + action + "&filename=" + fileName, postData, true);
		return serverResponse;
	}

}
