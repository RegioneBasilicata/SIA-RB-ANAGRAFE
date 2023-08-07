package it.csi.solmr.util;

import it.csi.solmr.etc.SolmrConstants;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;

public class PageRequest {

	// da verificare quale tempo di timeout impostare..
	private static final int CONNECTION_TIMEOUT = ((Long)SolmrConstants.get("CONNECT_ANDROMEDA_TIMEOUT")).intValue();

	public PageRequest() {
		
	}

	public String getHtmlCode(String url) {
		SolmrLogger.debug(this, "Invocating method getHtmlCode in PageRequest ");
		String htmlCode = "";

		HttpClient client = new HttpClient();
		HttpConnectionManager connManager = client.getHttpConnectionManager();
		HttpConnectionParams params = connManager.getParams();
		params.setConnectionTimeout(CONNECTION_TIMEOUT);
		//client.setConnectionTimeout(CONNECTION_TIMEOUT);

		GetMethod method = new GetMethod(url);

		try {
			int statusCode = client.executeMethod(method);
			if(statusCode != HttpStatus.SC_OK) {
				SolmrLogger.debug(this, "Method getHtmlCode failed in PageRequest: "+method.getStatusLine());
			}
			htmlCode = new String(method.getResponseBody());
		}
		catch(HttpException he) {
			SolmrLogger.error(this, "Fatal protocol violation in method getHtmlCode in PageRequest: intercepted HttpException with message: "+he.getMessage());
		}
		catch(IOException ioe) {
			SolmrLogger.error(this, "Fatal transport error in method getHtmlCode in PageRequest: intercepted IOException with message: "+ioe.getMessage());
		}
		catch(Exception e) {
			SolmrLogger.error(this, "Generic unknown error in method getHtmlCode in PageRequest: intercepted Exception with message: "+e.getMessage());
		}
		finally {
			method.releaseConnection();
		}
		SolmrLogger.debug(this, "Invocated method getHtmlCode in PageRequest ");
		return htmlCode;
	}
}
