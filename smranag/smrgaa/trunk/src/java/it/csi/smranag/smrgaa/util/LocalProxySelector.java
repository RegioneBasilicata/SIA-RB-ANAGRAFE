package it.csi.smranag.smrgaa.util;

import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.SianConstants;
import it.csi.solmr.util.SolmrLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LocalProxySelector extends ProxySelector
{
	
  private List<URI> failed = new ArrayList<URI>( );
  
  public List<Proxy> select(URI uri) {
    
	// Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
	ResourceBundle res = ResourceBundle.getBundle("config");
	String ambienteDeploy = res.getString("ambienteDeploy");
	SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
	
	List<Proxy> result = new ArrayList<Proxy>( );
	if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI)){
		//if (failed.contains(uri) || "http".equals(uri.getScheme()))
	    if (!failed.contains(uri))
	    {
	        result.add(Proxy.NO_PROXY);
	    }
	    else
	    {
	        SocketAddress proxyAddress = new InetSocketAddress( SianConstants.PROXY_SERVER, SianConstants.PROXY_PORT );
	        Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
	        result.add(proxy);
	    }
	}	 	
	else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY)){
		result.add(Proxy.NO_PROXY);
	}
	
    
    
    
    return result;
    
  }
  
  public void connectFailed(URI uri, SocketAddress address, IOException ex) {
    failed.add(uri);
  }
  
}
