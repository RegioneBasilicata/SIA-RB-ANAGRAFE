package it.csi.smranag.smrgaa.presentation.client.stampe.modol;

import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.util.SolmrLogger;

import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class StampeModolGaaServlet extends HttpServlet {
  
  
  private static final long serialVersionUID = -3923808641674677141L;
  
  
  protected String nomeFilePdf = null;
  protected ServletContext context = null;

  private static final String CONTENT_TYPE_PDF = "application/pdf";
  private static final String CONTENT_TYPE_DOWNLOAD = "application/x-download";
  private static final String ERROR_PAGE = "/view/erroreView.jsp";
  final String errMsg = "Impossibile procedere nella stampa."+
      "Contattare l'assistenza comunicando il seguente messaggio: ";

  public static final Font FONT_SERIF_BOLD_18 = new Font("Serif",Font.BOLD,18);
  public static final Font FONT_SERIF_BOLD_17 = new Font("Serif",Font.BOLD,17);
  public static final Font FONT_SERIF_BOLD_10 = new Font("Serif",Font.BOLD,10);
  public static final Font FONT_SERIF_BOLD_9 = new Font("Serif", Font.BOLD, 9);
  public static final Font FONT_SERIF_BOLD_8 = new Font("Serif", Font.BOLD, 8);
  public static final Font FONT_SERIF_8 = new Font("Serif", Font.PLAIN, 8);
  public static final Font FONT_SERIF_10 = new Font("Serif", Font.PLAIN, 10);
  
  public static final Font FONT_SERIF_BOLD_ITALIC_7 = new Font("Dialog", Font.ITALIC
      + Font.BOLD, 7);

  public void init(ServletConfig config) throws ServletException {
     super.init(config);
     context = config.getServletContext();
  }

  public void destroy() {
    nomeFilePdf = null;
    super.destroy();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     SolmrLogger.error(this, "Generazione PDF passando per doGet()");
     doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    SolmrLogger.debug(this,"Passato da doPost(): " + request.getRequestURI());

    // Controllo validità della sessione
    HttpSession session = request.getSession(false);
    if (! (session != null &&
        session.getAttribute("ruoloUtenza") != null))
      throw new ServletException("StampaServlet - Sessione non valida");

    // Controllo esistenza degli attributi della sottoclasse
    if (null==this.nomeFilePdf)
      throw new ServletException("\nStampaServlet - Tentativo fallito a causa di nomeFilePdf non definito");

    try 
    {
      String action=request.getParameter("action");
      byte ba[] = stampaPdf(request, response);
      
      //salvaPdf
      salvaPdf(request, ba);
      
      //invia mail
      inviaPdf(request, ba);
      
      response.setContentLength(ba.length);
      if ("download".equals(action))
      {
        response.setContentType(CONTENT_TYPE_DOWNLOAD);
        response.addHeader("Content-Disposition",
                           "attachment;filename = " + this.nomeFilePdf);
      }
      else 
        response.setContentType(CONTENT_TYPE_PDF);
      
     
      OutputStream out = response.getOutputStream();
      out.write(ba);
      
    }
    catch (Exception e) 
    {
      
      SolmrLogger.error(this, "\nStampeServlet.doPost()\n"+e.getMessage()+"\n");
      String messaggio = errMsg+": \n"+e.toString();
      request.setAttribute("messaggioErrore",messaggio);
      session.setAttribute("chiudi", "chiudi");
      request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
    }
  }

 

  protected byte[] stampaPdf(HttpServletRequest request,
                             HttpServletResponse response) throws Exception  
  {
    try 
    {
      

      AnagFacadeClient anagFacadeclient = new AnagFacadeClient();
      GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
      RuoloUtenza ruoloUtenza = (RuoloUtenza) request.getSession().getAttribute("ruoloUtenza");
      return popolaPdf(request, anagFacadeclient, gaaFacadeClient, ruoloUtenza);
     
      
    }
    catch (Exception e) 
    {
      e.printStackTrace();
      throw e;
    }
  }
  
  
  protected abstract byte[] popolaPdf(HttpServletRequest request,
      AnagFacadeClient anagFacadeclient,
      GaaFacadeClient gaaFacadeClient,
      RuoloUtenza ruoloUtenza) throws Exception;
  
  protected void salvaPdf(HttpServletRequest request,
      byte[] arrayPdf) throws Exception
  {
    try 
    {
      

      AnagFacadeClient anagFacadeclient = new AnagFacadeClient();
      GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
      RuoloUtenza ruoloUtenza = (RuoloUtenza) request.getSession().getAttribute("ruoloUtenza");
      scriviPdf(request, anagFacadeclient, gaaFacadeClient, arrayPdf, ruoloUtenza);
    }
    catch (Exception e) 
    {
      e.printStackTrace();
      throw e;
    }
  }
  
  protected abstract void scriviPdf(HttpServletRequest request,
      AnagFacadeClient anagFacadeclient,
      GaaFacadeClient gaaFacadeClient,
      byte[] arrayPdf, 
      RuoloUtenza ruoloUtenza) throws Exception;
  
  
  protected void inviaPdf(HttpServletRequest request,
      byte[] arrayPdf) throws Exception
  {
    try 
    {
      AnagFacadeClient anagFacadeclient = new AnagFacadeClient();
      GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
      RuoloUtenza ruoloUtenza = (RuoloUtenza) request.getSession().getAttribute("ruoloUtenza");
      inviaMailPdf(request, anagFacadeclient, gaaFacadeClient, arrayPdf, ruoloUtenza);
    }
    catch (Exception e) 
    {
      e.printStackTrace();
      throw e;
    }
  }
  
  protected abstract void inviaMailPdf(HttpServletRequest request,
      AnagFacadeClient anagFacadeclient,
      GaaFacadeClient gaaFacadeClient,
      byte[] arrayPdf, 
      RuoloUtenza ruoloUtenza) throws Exception;
  
  
  
  
  public static String checkNull(String stringa) {
    if(stringa==null)
      stringa="";
    
    return stringa;
  }
  
  


  

  
  
  
  
  
}
