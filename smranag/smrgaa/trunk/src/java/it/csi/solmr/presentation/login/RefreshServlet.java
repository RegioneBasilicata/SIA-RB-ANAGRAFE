package it.csi.solmr.presentation.login;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import it.csi.solmr.util.SolmrLogger;


public class RefreshServlet extends HttpServlet {

	private static final long serialVersionUID = 401831840129850983L;

	public  void service(HttpServletRequest request, HttpServletResponse  response)
    throws IOException, ServletException 
    {
        SolmrLogger.debug(this, "RefreshServlet :: service() - sessione rinnovata");
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        
        response.getWriter().write("<valid>Funziona Refresh x get</valid>");
    }
}
