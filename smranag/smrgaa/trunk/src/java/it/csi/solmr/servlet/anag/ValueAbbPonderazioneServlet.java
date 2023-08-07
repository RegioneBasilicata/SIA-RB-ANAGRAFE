package it.csi.solmr.servlet.anag;

import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ValueAbbPonderazioneServlet extends HttpServlet 
{
   
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -8520056562616748713L;
  
  
  protected ServletContext context = null;


  public void init(ServletConfig config) throws ServletException {
     super.init(config);
     context = config.getServletContext();
  }

  public void destroy() {
    super.destroy();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     SolmrLogger.error(this, "Generazione PDF passando per doGet()");
     doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    SolmrLogger.debug(this,"Passato da doPost(): " + request.getRequestURI());

    // Controllo validità della sessione
    //HttpSession session = request.getSession(false);
    PrintWriter out = response.getWriter();
    response.setContentType("text/plain");
    
    String idTipoEfa = request.getParameter("idTipoEfa");
    String idUtilizzo = request.getParameter("idUtilizzo");
    String idTipoDestinazione = request.getParameter("idTipoDestinazione");
    String idTipoDettaglioUso = request.getParameter("idTipoDettaglioUso");
    String idTipoQualitaUso = request.getParameter("idTipoQualitaUso");
    String idVarieta = request.getParameter("idVarieta");
    
    
    
    String returVal = "1";    
    try
    {
      GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
      if(Validator.isNotEmpty(idTipoEfa) && Validator.isNotEmpty(idUtilizzo)
        && Validator.isNotEmpty(idTipoDestinazione) && Validator.isNotEmpty(idTipoDettaglioUso)
        && Validator.isNotEmpty(idTipoQualitaUso) && Validator.isNotEmpty(idVarieta))
      {
      
        Integer val = gaaFacadeClient.getAbbPonderazioneByMatrice(new Long(idTipoEfa), 
            new Long(idUtilizzo), new Long(idTipoDestinazione), new Long(idTipoDettaglioUso), new Long(idTipoQualitaUso), new Long(idVarieta));
      
        returVal = ""+val.intValue();
      }
      
    }
    catch(Exception ex)
    {
      returVal = "failed";
    }
  
    
    
    out.print(returVal);
    
    
    
  }

  
  
  
  
  

  

  
  
  
  

  

  
  
  
  
  
  
  
  
  
  
  
  
  
  
}
