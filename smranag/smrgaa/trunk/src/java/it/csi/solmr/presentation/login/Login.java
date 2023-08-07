package it.csi.solmr.presentation.login;

import it.csi.iride2.policy.entity.Identita;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.PageCache;
import it.csi.solmr.util.SolmrLogger;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>Title: SMRGAA</p>
 * <p>Description: SMRGAA</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */

public class Login extends HttpServlet
{
	private static final long serialVersionUID = 4171425417702739674L;


  // nuova gestione login con Shibboleth
  private final static String VIEW_URL="layout/sceltaRuolo.htm";

	public void init() throws ServletException
	{
		super.init();

		if (SolmrLogger.isDebugEnabled(this))
			SolmrLogger.debug(this, "Initializing Login...");
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		ServletContext sContext = this.getServletContext();
		
		//String activeSit = null;
		String portalName = null;
		//String CERT_AUTHENTICATION_TARGET = null;

		SolmrLogger.debug(this,"[Login::service] BEGIN.");
		
		SolmrLogger.debug(this," ---- Charset.defaultCharset() = "+Charset.defaultCharset());
		
		//java.io.ByteArrayInputStream bais;
		//it.csi.jsf.htmpl.Htmpl htmplLayout = null;
		//String pageLogin = null;
		String messaggioErrore = request.getParameter("messaggioErrore");
		SolmrLogger.debug(this,"[Login::service] messaggioErrore = "+messaggioErrore);
		String URL_ACCESS_POINT = null;
		URL_ACCESS_POINT = (String)request.getAttribute("originalURI");

		HttpSession session = request.getSession(true);
		SolmrLogger.debug(this, "--- (String)request.getAttribute(originalURI) =" +URL_ACCESS_POINT); 
		session.setAttribute("URL_ACCESS_POINT", URL_ACCESS_POINT);

		URL_ACCESS_POINT = eliminaParameterMessaggioErrore(URL_ACCESS_POINT);
		portalName=request.getParameter("portalName");
		SolmrLogger.debug(this, "--- portalName request.getParameter ="+portalName);
		request.getSession().setAttribute("PORTAL_NAME",portalName);
		SolmrLogger.debug(this, "--- metto in sessione PORTAL_NAME ="+portalName);

		//scrivo il cookie per mantenere il portale da cui si e' effettuato l'accesso
		javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie("URLAccessPoint", URL_ACCESS_POINT);
		response.addCookie(cookie);
		session.setAttribute("URL_ACCESS_POINT", URL_ACCESS_POINT);
		/**
		 * Recupero della pagina di Login tramite connessione http.
		 */
		//LoaderInclude loaderInclude = LoaderInclude.getInstance();

		
		//non più usato con papua!!!
		/*it.csi.iride2.policy.entity.Application myApp = new Application(it.csi.solmr.etc.SolmrConstants.APP_NAME_IRIDE2_SMRGAA);
		session.setAttribute("myApp", myApp);*/
		
		/* leggo l'oggetto PageCache presente in scope application */
		PageCache cp = (PageCache)sContext.getAttribute("cp");
		/* se l'oggetto non esiste, allora lo creo nuovo e lo piazzo subito in scope application*/
		if (cp == null)
		{
			cp = new PageCache();
			sContext.setAttribute("cp", cp);
			cp = (PageCache)sContext.getAttribute("cp");
		}
		
		
		/* piazzo in scope application l'oggetto cp appena utilizzato */
		sContext.setAttribute("cp", cp);

		if (portalName.equalsIgnoreCase(SolmrConstants.NOME_PORTALE_RUPAR))
		{
			// pageLogin = loaderInclude.getInclude(activeSit + SolmrConstants.URL_LOGIN_RUPAR, session);
			//pageLogin = pageLoginRupar; // inutile per Shibboleth
			SolmrLogger.debug(this, "-- setto in sessione pathToFollow = rupar");
			session.setAttribute("pathToFollow", "rupar");
		}
		else if (portalName.equalsIgnoreCase(SolmrConstants.NOME_PORTALE_SISPIE))
		{
			SolmrLogger.debug(this, "-- setto in sessione pathToFollow = sispie");	
		  session.setAttribute("pathToFollow", "sispie");
		    //pageLogin = pageLoginSispie; // inutile per Shibboleth
		}
		else if(portalName.equalsIgnoreCase(SolmrConstants.NOME_PORTALE_TOBECONFIG)){
			SolmrLogger.debug(this, "-- setto in sessione pathToFollow = TOBECONFIG");	
			session.setAttribute("pathToFollow", "TOBECONFIG");
		}
		
		

		// nuova gestione login con Shibboleth
		// se siamo giunti a questo punto ed esiste un oggetto identita allora
    // andiamo avanti direttamente alla scelta del ruolo
		if (session.getAttribute(SolmrConstants.IDENTITA) != null)
		{
			SolmrLogger.debug(this," ----- session.getAttribute(SolmrConstants.IDENTITA) != null");
			Identita identita = (Identita)session.getAttribute(SolmrConstants.IDENTITA);
			SolmrLogger.debug(this," - codiceFiscale ="+identita.getCodFiscale());
			SolmrLogger.debug(this," - nome ="+identita.getNome());
			SolmrLogger.debug(this," - cognome ="+identita.getCognome());
			
			SolmrLogger.debug(this," --- sendRedirect to :"+VIEW_URL);
			
			SolmrLogger.debug(this,"[Login::service] END (Shibboleth).");
			response.sendRedirect(VIEW_URL);
      return;
		};

		// altrimenti siamo ancora con la vecchia gestione di login "nativo"
		// e quindi dobbiamo riproporre la pagina di autenticazione 
		/*bais = new java.io.ByteArrayInputStream(pageLogin.getBytes());
		htmplLayout = new it.csi.jsf.htmpl.Htmpl(bais);
		htmplLayout.set("ACTION_POINT", ACTION_POINT);
		htmplLayout.set("ACTIV_SITE", activeSit);
		htmplLayout.set("portalName", portalName);
		//htmplLayout.set("CERT_AUTHENTICATION_TARGET", CERT_AUTHENTICATION_TARGET);

		if (messaggioErrore != null)
		{
			htmplLayout.newBlock("MsgErrore");
			htmplLayout.set("MsgErrore.messaggioErrore", messaggioErrore);
		}

		String certAccessPoint = (String)session.getAttribute("certAccessPoint");
		SolmrLogger.debug(this,"[Login:service]\n\n\n\n\ncertAccessPoint Prima di forward = "+certAccessPoint);
		X509Certificate[] cert = null;
		cert = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
		SolmrLogger.debug(this,"[Login:service]\n\n\n\n\ncert Prima di forward = "+cert);

		if ((cert != null) && (request.getParameter("messaggioErrore") == null) && (certAccessPoint == null))
		{
			SolmrLogger.debug(this,"[Login:service] Prima del forward");
			forward(ACTION_POINT_FORWARD, request, response);
		}
		else
		{
			try
			{
				htmplLayout.newBlock("accessUP");
				htmplLayout.newBlock("accessUPP");
				response.getOutputStream().print(htmplLayout.text());
			}
			catch (IOException ex)
			{
				SolmrLogger.dumpStackTrace(this,"[Login:service] Eccezione #1",ex);
			}
		}

		SolmrLogger.debug(this,"[Login::service] END (login nativo).");*/
	}

	public static String getUrl(HttpServletRequest req)
	{
		String reqUri = (String)req.getAttribute("originalURI");
		String queryString = req.getQueryString();
		if (queryString != null)
		{
			reqUri += ("?" + queryString);
		}

		return reqUri;
	}

	/**
	 * Esegue il forward della pagina all'url indicato come parametro.
	 *
	 * @param url
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	/*private void forward(String url, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException
	{
		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.forward(request, response);
	}*/

	private String eliminaParameterMessaggioErrore(String queryString)
	{
		String token = "&messaggioErrore=";
		String subUrl = null;

		if (queryString != null)
		{

			int indexMessaggioErrore = queryString.indexOf(token);

			if (indexMessaggioErrore != -1)
			{
				subUrl = queryString.substring(0, indexMessaggioErrore - 1);
			}
			else
			{
				subUrl = queryString;
			}
		}

		return subUrl;
	}
}
