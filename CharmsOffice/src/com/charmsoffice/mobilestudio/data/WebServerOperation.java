package com.charmsoffice.mobilestudio.data;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class WebServerOperation {
	
	private static CookieStore cookieStore;

	public static String sendHTTPPostRequestToServer(String urlPath, List<NameValuePair> postData,  boolean readfromServer) throws Exception
	{
		String data="";		

		HttpClient httpclient  = new DefaultHttpClient();
		HttpPost httppost  = new HttpPost(urlPath);

		HttpResponse response = null;
		HttpEntity entity = null;
		InputStream is = null;
		BufferedReader reader = null;

		if(postData !=null)
			httppost.setEntity(new UrlEncodedFormEntity(postData, "UTF-8"));
		
		if(cookieStore == null)
			cookieStore = ((DefaultHttpClient) httpclient).getCookieStore();
		else 
			((DefaultHttpClient) httpclient).setCookieStore(cookieStore);

		response = httpclient.execute(httppost);

		if(readfromServer)
		{
			entity = response.getEntity();
			is = entity.getContent();
			reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"), 8);

			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) 
			{
				sb.append(line);
			}

			is.close();
			data = sb.toString();			
		}
		Log.e("Inside POST", "#######");

		return data.trim();
	}
	
	public static String sendHTTPGetRequestToServer(String urlPath, boolean readfromServer) throws Exception
	{
		String data="";		

		HttpClient httpclient  = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(urlPath);

		HttpResponse response = null;
		HttpEntity entity = null;
		InputStream is = null;
		BufferedReader reader = null;
		
		if(cookieStore == null)
			cookieStore = ((DefaultHttpClient) httpclient).getCookieStore();
		else 
			((DefaultHttpClient) httpclient).setCookieStore(cookieStore);

		response = httpclient.execute(httpget);

		if(readfromServer)
		{
			entity = response.getEntity();
			is = entity.getContent();
			reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"), 8);

			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) 
			{
				sb.append(line);
			}

			is.close();
			data = sb.toString();			
		}
		Log.e("Inside POST", "#######");

		return data.trim();
	}


	public static String downLoadFile(String fileUrl, String savePath) throws Exception
	{
		//String status;
		try
		{
		    URL u = new URL(fileUrl);
		    HttpURLConnection c = (HttpURLConnection) u.openConnection();
		    c.setRequestMethod("GET");
		    c.setDoOutput(true);
		    c.connect();
		    FileOutputStream f = new FileOutputStream(new File(savePath, getImageFileName(fileUrl)));
	
	
		    InputStream in = c.getInputStream();
	
		    byte[] buffer = new byte[1024];
		    int len1 = 0;
		    while ( (len1 = in.read(buffer)) > 0 ) 
		    {
		        f.write(buffer,0, len1);
		    }
		    f.close();
		    return "Downlaoded";
		}
		catch(IOException e)
		{
			return e.getMessage();
		}
	}	
	
	private static String getImageFileName(String userImage) 
	{
		int index = userImage.lastIndexOf("/");
		String fileName = userImage.substring(index + 1);
		return fileName;
	}

	public static String sendJSONDataToWebServer(String path, String jString)
	{

		String a = null; 

		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		try 
		{
			HttpPost post = new HttpPost(path);
			Log.i("jason Object", jString);
			post.setHeader("json", jString);
			StringEntity se = new StringEntity(jString);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
			post.setEntity(se);

			if(cookieStore == null)
				cookieStore = ((DefaultHttpClient) client).getCookieStore();
			else 
				((DefaultHttpClient) client).setCookieStore(cookieStore);

			response = client.execute(post);
			if (response != null) 
			{
				InputStream in = response.getEntity().getContent();
				a = convertStreamToString(in);
				Log.i("Read from Server", a);
			}
		} 
		catch (Exception e) 
		{
			return e.toString();
		}
		return a;

	}
	private static String convertStreamToString(InputStream is) 
	{

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try 
		{
			while ((line = reader.readLine()) != null) 
			{
				sb.append(line + "\n");
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				is.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public static String uploadFile(String uploadUrl, String localFilePath) throws Exception
	 {
	  String lineEnd = "\r\n";
	  String twoHyphens = "--";
	  String boundary = "*****";
	  String Tag="3rd";
	  URL connectURL;
	  connectURL = new URL(uploadUrl);
	  FileInputStream fileInputStream = new FileInputStream(localFilePath);
	  
	  try
	  {
	   Log.e(Tag,"Starting to bad things");

	   HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
	   
	   conn.setDoInput(true);
	   
	   conn.setDoOutput(true);
	   
	   conn.setUseCaches(false);
	   
	   conn.setRequestMethod("POST");

	   conn.setRequestProperty("Connection", "Keep-Alive");

	   conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

	   DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );

	   dos.writeBytes(twoHyphens + boundary + lineEnd);
	   dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + localFilePath +"\"" + lineEnd);
	   dos.writeBytes(lineEnd);


	   Log.e(Tag,"Headers are written");

	   int bytesAvailable = fileInputStream.available();
	   int maxBufferSize = 1024;
	   int bufferSize = Math.min(bytesAvailable, maxBufferSize);
	   byte[] buffer = new byte[bufferSize];

	   int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

	   while (bytesRead > 0)
	   {
			dos.write(buffer, 0, bufferSize);
		    bytesAvailable = fileInputStream.available();
		    bufferSize = Math.min(bytesAvailable, maxBufferSize);
		    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	   }

	   dos.writeBytes(lineEnd);
	   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	   Log.e(Tag,"File is written");
	   fileInputStream.close();
	   dos.flush();

	   InputStream is = conn.getInputStream();
	   int ch;

	   StringBuffer b =new StringBuffer();
	   while( ( ch = is.read() ) != -1 )
	   {
		   b.append( (char)ch );
	   }
	   
	   String s=b.toString();
	   Log.i("Response",s);
	   dos.close();
	   return s.trim();


	  }
	  catch (MalformedURLException ex)
	  {
		   Log.e(Tag, "error: " + ex.getMessage(), ex);
		   return "Error MalformedURL";
	  }

	  catch (IOException ioe)
	  {
		   Log.e(Tag, "error: " + ioe.getMessage(), ioe);
		   return "File Not Found";
	  }
	 }

	
	public static Bitmap getBitmapFromURL(String thisURL) 
	{
        try 
        {
            URL url = new URL(thisURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap mybitmap = BitmapFactory.decodeStream(input);

            return mybitmap;

        }
        catch (Exception ex) 
        {
            return null;
        }
	}
}

