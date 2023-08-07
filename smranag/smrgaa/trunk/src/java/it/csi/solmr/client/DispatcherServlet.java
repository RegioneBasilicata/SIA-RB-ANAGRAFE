package it.csi.solmr.client;

import it.csi.papua.papuaserv.exception.messaggistica.LogoutException;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.smranag.smrgaa.util.MessaggisticaUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.ConfigFileParser;
import it.csi.solmr.util.IrideFileParser;
import it.csi.solmr.util.SolmrLogger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca Romanello
 * @version 1.0
 */

public class DispatcherServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5511567263222834035L;
	private String defaultController = null;
	private HashMap<Object,Object> mapping = null;
	private String errorPage = null;
	private Vector<Object> getAccepted = null;
	private String theContext = null;
	private HashMap<Object,Object> noLogin = null;

	public void init() throws ServletException {
		super.init();
		theContext = this.getServletContext().toString();
		if (SolmrLogger.isDebugEnabled(this))
			SolmrLogger.debug(this, "Initializing DispatcherServlet for context "+theContext+"...");
		ConfigFileParser cfp = new ConfigFileParser(SolmrConstants.FILE_CONF_CLIENT);
		String pointTo = cfp.getAttributePointTo();
		mapping = cfp.getMapping();
		defaultController = cfp.getDefaultController();
		errorPage = cfp.getErrorPage();
		System.out.println("DispatcherServlet - init() - errorPage ="+errorPage);		
		getAccepted = cfp.getGetAccepted();
		noLogin=cfp.getNoLogin();
		this.getServletContext().setAttribute("pointTo", pointTo);
		String anagAccess = (String)this.getServletContext().getInitParameter("anagAccess");
		String umaAccess = (String)this.getServletContext().getInitParameter("umaAccess");
		if (SolmrLogger.isDebugEnabled(this))
			SolmrLogger.debug(this, theContext+" - Accessing Anag and Profile via: "+anagAccess);
		if (SolmrLogger.isDebugEnabled(this))
			SolmrLogger.debug(this, theContext+" - Accessing Uma via: "+umaAccess);
		this.getServletContext().setAttribute("anagAccess", anagAccess);
		this.getServletContext().setAttribute("umaAccess", umaAccess);
		IrideFileParser irideFileParser=new IrideFileParser();
		this.getServletContext().setAttribute("iride2mappings", irideFileParser.getMapping());
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURI();

		request.setAttribute("originalURI",url);

		if (request.getQueryString()!=null) {
			if (SolmrLogger.isDebugEnabled(this))
				SolmrLogger.debug(this, theContext+" - Query string: "+request.getQueryString());
			url = url.substring(url.substring(url.indexOf("/")+1).indexOf("/")+1);
			if (!getAccepted.contains(url)) {
				System.out.print(" --- CASO : !getAccepted.contains(url)");
				System.out.println("-- url ="+url);
				
				SolmrLogger.error(this, theContext+" - Method GET not accepted on this page");
				request.getSession().setAttribute("exception", new SolmrException("Method GET not accepted on this page"));
				System.out.println("DispatcherServlet - doGet() - forward(errorPage) ="+errorPage);	
				forward(errorPage, request, response);
			} else
				process(request, response);
		} else
			process(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	public void destroy() {
		SolmrLogger.error(this,"destroy BEGIN");
		defaultController = null;
		mapping = null;
		errorPage = null;
		getAccepted = null;
		theContext = null;
		super.destroy();
		SolmrLogger.error(this,"destroy END");
	}

	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURI();		
		String urlRemapped = null;
		url = url.substring(url.substring(url.indexOf("/") + 1).indexOf("/") + 1);
		//modifica fatta perchè su wild-fly rimane appesa la parte relativa a ;jsessionid=
		int posSessions=url.indexOf(";");
		if (posSessions>0)
			url = url.substring(0,posSessions);
		urlRemapped = (String) mapping.get(url);
		if (urlRemapped == null)
			urlRemapped = defaultController;
		if (SolmrLogger.isDebugEnabled(this))
			SolmrLogger.debug(this, theContext+" - Processing Request ["+url+"] - "+" Remapped to: ["+urlRemapped+"]");
		request.setAttribute("layout", url);
		if (noLogin.get(url) != null || isAuthenticated(request))
		{
			if (SolmrLogger.isDebugEnabled(this))
				SolmrLogger.debug(this, theContext+" - User authenticated");

			response.setHeader("Cache-Control", "private");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", -1);


			//request.setAttribute("layout", url);
		} 
		else {
			SolmrLogger.fatal(this, theContext+" - User NOT authenticated - "+request.getSession().getAttribute("exception"));
			SolmrLogger.debug(this," -- url ="+url);
			if(!isAuthenticated(request)) {
				System.out.print("-- CASO : !isAuthenticated(request)");
				SolmrLogger.error(this, "Session is non valid or is timeout: go to: "+errorPage);
				response.sendRedirect(errorPage);
				return;
			}
			else {
				if(request.getSession().getAttribute("exception")==null) {
					request.getSession().setAttribute("exception",new SolmrException(AnagErrors.EXC_AUTHENTICATION_FAILED));
				}
			}
			response.sendRedirect(urlRemapped);
			return;
		}
		
		
		
		  // caricamento periodico dati messaggistica per testatina
    if(request.getSession().getAttribute("LogoutException")==null)
    {
      try
      {
        MessaggisticaUtils.caricaMessaggiTestata(request.getSession());
      }
      catch (LogoutException e) 
      {
        SolmrLogger.error(this, "Ricevuta richiesta di logout forzato: "+e.getMessage());
        request.getSession().setAttribute("LogoutException", e);
        // redirect su pagina di logout custom
        forward("/error/forceLogoutPage.jsp", request, response);
        return;
      }
    }
		
		
		
		
		
		forward(urlRemapped, request, response);
	}

	private boolean isAuthenticated(HttpServletRequest request) {
		boolean isAuthenticated = false;
		SolmrLogger.debug(this, "isAuthenticated - BEGIN");
		System.out.println("isAuthenticated - BEGIN");
		
		SolmrLogger.debug(this, "Valore di request in isAuthenticated: "+request);
		HttpSession session = request.getSession(false);
		SolmrLogger.debug(this, "Valore di session in isAuthenticated: "+session);
		if(session != null) {
			SolmrLogger.debug(this, "---- session != null");
			LoggerUtils loggerUtils = new LoggerUtils();
			loggerUtils.dumpHttpSessionAttributes(session);
		}
		// --- CASO di sessione null
		else {
			System.out.println("La sessione è null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			SolmrLogger.debug(this, "La sessione è null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
		if (session != null && session.getAttribute("ruoloUtenza") != null) {
			isAuthenticated = true;
			System.out.println("RuoloUtenza in sessione != null");
		}
		// --- CASO di sessione not null e oggetto RuoloUtenza in sessione null
		if(session != null && session.getAttribute("ruoloUtenza") == null){	
		  SolmrLogger.debug(this, "RuoloUtenza in sessione è null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		  System.out.println("RuoloUtenza in sessione è null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		  // invalido la sessione
		  SolmrLogger.debug(this, " -- forzo l'invalidazione della sessione : session.invalidate()");
		  session.invalidate();
		}
		SolmrLogger.debug(this, "isAuthenticated - END");
		System.out.println("isAuthenticated - END");
		return isAuthenticated;
	}

	private void forward(String url,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.forward(request, response);
	}
}
