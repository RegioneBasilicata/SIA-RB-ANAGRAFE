package com.lucana.smranag.rsdi;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

public class VerificaAccessoServlet extends HttpServlet {
	public static final String jndiName = "java:/smrgaaweb/jdbc/smrgaawebDS";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(ServletConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url ="";
		
		Long idConduzioneParticella = Long.decode(request.getParameter("idConduzione"));
		Long idAzienda = Long.decode(request.getParameter("idAzienda"));
		
		SolmrLogger.debug(this, "idConduzioneParticella :"+ idConduzioneParticella);
		SolmrLogger.debug(this, "idAzienda :"+ idAzienda);
		
		if (idConduzioneParticella!=null && idAzienda != null){
			try {																			
				String cf = request.getParameter("cf");
				VerificaAccesso verifica = new VerificaAccesso(jndiName);
				url = verifica.verificaDati(cf, idConduzioneParticella, idAzienda);
				
		        request.setAttribute("url", url);
		        response.addHeader("url", url);
		        response.flushBuffer();
			} 
			catch (Exception e) {
				request.setAttribute("url",null);
				SolmrLogger.error(e, "Exception in VerificaAccessoServlet ="+e.getMessage());
			}
		} 
		else{
			SolmrLogger.debug(this, "---- idConduzione o idAzienda non sono valorizzati");
			request.setAttribute("url",null);			
		}
		SolmrLogger.debug(this, "url=" + url);		
	}

}
