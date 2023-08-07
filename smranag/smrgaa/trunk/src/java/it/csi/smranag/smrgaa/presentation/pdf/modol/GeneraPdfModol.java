package it.csi.smranag.smrgaa.presentation.pdf.modol;

import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.util.SolmrLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class GeneraPdfModol 
{
  
  
  
  //private static final String ERROR_PAGE = "/view/erroreView.jsp";
  final String errMsg = "Impossibile procedere nella genrazione del pdf modol."+
      "Contattare l'assistenza comunicando il seguente messaggio: ";


  public void generaDocumento(HttpServletRequest request, HttpServletResponse response) 
    throws Exception 
  {
    SolmrLogger.debug(this,"Passato da inizio genera Documento");

    // Controllo validità della sessione
    HttpSession session = request.getSession(false);
    if (! (session != null &&
        session.getAttribute("ruoloUtenza") != null) )
      throw new Exception("StampaServlet - Sessione non valida");

    try 
    {
      byte ba[] = stampaPdf(request, response);
      
      //salvaPdf
      salvaPdf(request, ba);
      
      //invia mail
      inviaPdf(request, ba);
       
    }
    catch (Exception e) 
    {      
      SolmrLogger.error(this, "\nGeneraPdfModol\n"+e.getMessage()+"\n");
      throw e;
    }
  }

 

  protected byte[] stampaPdf(HttpServletRequest request,
                             HttpServletResponse response) throws Exception  
  {
    try 
    {
      

      AnagFacadeClient anagFacadeclient = new AnagFacadeClient();
      GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
      RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
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
      RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
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
      RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
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
