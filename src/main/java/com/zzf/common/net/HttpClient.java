package com.zzf.common.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpClient {

	// static String USER_AGENT =
	// "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)";
	// //ie
	static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:20.0) Gecko/20100101 Firefox/20.0"; // firefox
	static String ACCEPT = "text/html, application/xhtml+xml, */*";
	//static String ACCEPT = "text/xml";
	static String ACCEPT_LANGUAGE = "zh-CN";
	static String Accept_Encoding = "gzip,deflate";
	static Log log = LogFactory.getLog(HttpClient.class);

	private HttpClient() {

	}

	public static byte[] GetDataAuto(String url) throws IOException {
		byte[] b = null;
		log.debug(url);
		HttpClientParams params = new HttpClientParams();
		params.setParameter(HttpClientParams.USER_AGENT, USER_AGENT);
		org.apache.commons.httpclient.HttpClient http = new org.apache.commons.httpclient.HttpClient(
				params);
		GetMethod getm = new GetMethod(url);
		http.executeMethod(getm);
		b = getm.getResponseBody();
		return b;
	}

	public static byte[] GetImageData(URL url, String ref, StringBuilder type)
			throws IOException {
		log.debug(url);
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		byte[] b = GetData(url, map, ref);
		if (type != null) {
			type.delete(0,type.length());
			List<String> str = map.get("Content-Type");
			String typex = "";
			if (str.size() > 0) {
				typex = str.get(0);
			}
			
			if(typex.startsWith("image/"))
			{
				type.append(typex);
			}
			
//			if (typex.equalsIgnoreCase("image/fax")) {
//				type.append("fax");
//			}else if (typex.equalsIgnoreCase("image/gif")) {
//				type.append("gif");
//			}else if (typex.equalsIgnoreCase("image/x-icon")) {
//				type.append("ico");
//			}else if (typex.equalsIgnoreCase("image/jpeg")) {
//				type.append("jpg");
//			}else if (typex.equalsIgnoreCase("image/jpeg")) {
//				type.append("jpeg");
//			}else if (typex.equalsIgnoreCase("image/pnetvue")) {
//				type.append("net");
//			}else if (typex.equalsIgnoreCase("image/tiff")) {
//				type.append("tif");
//			}else if (typex.equalsIgnoreCase("image/png")) {
//				type.append("png");
//			}
		}

		return b;
	}

	private static byte[] GetData(URL url, Map<String, List<String>> rethead,
			String ref) throws IOException {
		byte[] b = null;
		log.debug(url);
		URLConnection connect = url.openConnection();
		if (connect instanceof HttpURLConnection) {
			HttpURLConnection hconn = (HttpURLConnection) connect;
			hconn.setRequestProperty("User-Agent", USER_AGENT);
			hconn.setRequestProperty("Accept", ACCEPT);
			hconn.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);
			hconn.setRequestProperty("Accept-Encoding", Accept_Encoding);
			if (ref != null) {
				hconn.setRequestProperty("Referer", ref);
			}
			hconn.setDoInput(true);
			hconn.connect();
			InputStream is = null;
			try {
				int cl = hconn.getContentLength();
				if (cl == 0) {
					return new byte[0];
				}

				ByteArrayOutputStream out = new ByteArrayOutputStream();

				is = hconn.getInputStream();

				String ced = hconn.getHeaderField("Content-Encoding");
				byte[] tmpb = new byte[1024];
				int k = is.read(tmpb);
				while (k > 0) {
					out.write(tmpb, 0, k);
					if (cl < 0) {
						cl = hconn.getContentLength();
					}

					if (cl >= 0 && cl <= out.size()) {
						break;
					}

					k = is.read(tmpb);
				}

				b = out.toByteArray();
				if ("gzip".equalsIgnoreCase(ced)) {
					GZIPInputStream gzip = new GZIPInputStream(
							new ByteArrayInputStream(b));
					out.reset();
					k = gzip.read(tmpb);
					while (k > 0) {
						out.write(tmpb, 0, k);
						k = gzip.read(tmpb);
					}
					b = out.toByteArray();
				}

				if (rethead != null) {
					rethead.putAll(hconn.getHeaderFields());
				}

			}catch(IOException ex)
			{
				if(hconn.getResponseCode() == 500)
				{
					String str = null;
				//ex.printStackTrace();
				try
				{
					InputStream isz =hconn.getErrorStream();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					byte[] tmpb = new byte[1024];
					int k = isz.read(tmpb);
					while (k > 0) {
						out.write(tmpb, 0, k);
						k = isz.read(tmpb);
					}
					isz.close();
					str = new String(out.toByteArray(),"utf-8");
					System.out.println( str );
					
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				if(str != null)
				{
					throw new IOException(str);
				}
				
				}
				throw ex;
			} finally {
				if (is != null) {
					is.close();
				}
				hconn.disconnect();
			}

		} else {
			log.warn("connect is not a HttpURLConnection");
		}

		return b;
	}

	public static byte[] GetData(URL url) throws IOException {
		return GetData(url, null, null);
	}

	/**
	 * https get
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 * @author liangyh
	 */
	public static byte[] GetSslData(URL url) throws Exception {
		log.debug(url);
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");// SSL TSL
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		byte[] b = null;
		URLConnection connect = url.openConnection();
		if (connect instanceof HttpURLConnection) {
			HttpURLConnection hconn = (HttpURLConnection) connect;
			hconn.setDoInput(true);
			hconn.connect();
			InputStream is = null;
			try {
				int cl = hconn.getContentLength();
				if (cl == 0) {
					return new byte[0];
				}
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				is = hconn.getInputStream();
				byte[] tmpb = new byte[1024];
				int k = is.read(tmpb);
				while (k > 0) {
					out.write(tmpb, 0, k);
					if (cl < 0) {
						cl = hconn.getContentLength();
					}

					if (cl >= 0 && cl <= out.size()) {
						break;
					}

					k = is.read(tmpb);
				}

				b = out.toByteArray();

			} finally {
				if (is != null) {
					is.close();
				}
				hconn.disconnect();
			}
		} else {
			log.warn("connect is not a HttpURLConnection");
		}
		return b;
	}

	public static byte[] PostData(URL url, byte[] postData) throws IOException {
		byte[] b = null;
		log.debug(url);
		URLConnection connect = url.openConnection();
		if (connect instanceof HttpURLConnection) {
			HttpURLConnection hconn = (HttpURLConnection) connect;
			hconn.setDoInput(true);
			hconn.setDoOutput(true);
			hconn.setRequestMethod("POST");
			if (postData != null) {
				String ct = hconn.getRequestProperty("Content-Type");
				if (ct != null) {
					int k = ct.indexOf("charset");
					if (k > 0) {
						int end = ct.indexOf(";", k + 1);
						if (end < 0) {
							end = ct.length();
							ct = ct.substring(0, k);
						} else {
							ct = ct.substring(0, k)
									+ ct.substring(end + 1, ct.length());
							;
						}
					}
					ct += "; charset=utf8";
				} else {
					ct = "text/html; charset=utf8";
				}

				// charset=UTF-8
				hconn.setRequestProperty("Content-Type", ct);
				hconn.getOutputStream().write(postData);
				hconn.getOutputStream().close();
			}

			hconn.connect();
			InputStream is = null;
			try {
				int cl = hconn.getContentLength();
				if (cl == 0) {
					return new byte[0];
				}
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				is = hconn.getInputStream();
				byte[] tmpb = new byte[1024];
				int k = is.read(tmpb);
				while (k > 0) {
					out.write(tmpb, 0, k);
					if (cl < 0) {
						cl = hconn.getContentLength();
					}

					if (cl >= 0 && cl <= out.size()) {
						break;
					}

					k = is.read(tmpb);
				}

				b = out.toByteArray();

			} 
			catch(IOException ex)
			{
				//ex.printStackTrace();
				if(hconn.getResponseCode() == 500)
				{
					String str = null;
				//ex.printStackTrace();
				try
				{
					InputStream isz =hconn.getErrorStream();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					byte[] tmpb = new byte[1024];
					int k = isz.read(tmpb);
					while (k > 0) {
						out.write(tmpb, 0, k);
						k = isz.read(tmpb);
					}
					isz.close();
					str = new String(out.toByteArray(),"utf-8");
					System.out.println( str );
					
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				if(str != null)
				{
					throw new IOException(str);
				}
				
				}
				throw ex;
			}
			finally {
				if (is != null) {
					is.close();
				}
				hconn.disconnect();
			}

		} else {
			log.warn("connect is not a HttpURLConnection");
		}

		return b;

	}
}
