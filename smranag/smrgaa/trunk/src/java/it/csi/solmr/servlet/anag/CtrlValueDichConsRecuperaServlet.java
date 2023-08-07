package it.csi.solmr.servlet.anag;

import it.csi.smranag.smrgaa.dto.AllegatoDichiarazioneVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoAllegatoVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.pdf.modol.PdfFascicoloAllegatoInseribileModol;
import it.csi.smranag.smrgaa.presentation.pdf.modol.PdfFascicoloModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CtrlValueDichConsRecuperaServlet extends HttpServlet 
{
   
 
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 8008127164960210643L;
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
    //Long idDichiarazioneConsistenzaDaValid = (Long)request.getSession().getAttribute("idDichiarazioneConsistenzaDaValid");
    Long idDichiarazioneConsistenzaStampa = (Long)request.getSession().getAttribute("idDichiarazioneConsistenzaStampa");
    String returVal = "";    
    if(Validator.isNotEmpty(idDichiarazioneConsistenzaStampa))
    {
      returVal = "inCorso";
    }
    else
    {    
      String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenzaStampa");
      request.getSession().setAttribute("idDichiarazioneConsistenzaStampa", new Long(idDichiarazioneConsistenza));
      //request.getSession().setAttribute("idDichiarazioneConsistenzaDaValid", new Long(idDichiarazioneConsistenza));
      Long idDichiarazioneConsistenzaLg = new Long(idDichiarazioneConsistenza);
      
      
      Vector<Long> vIdTipoAllegato = new Vector<Long>();
      vIdTipoAllegato.add(new Long(SolmrConstants.VALIDAZIONE_ALLEGATO));     
      
      try
      {
    	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
        AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
        anagFacadeClient.updateDichiarazioneConsistenzaRichiestaStampa(idDichiarazioneConsistenzaLg);
        ConsistenzaVO consistenzaVO = anagFacadeClient.findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenzaLg);
        Vector<AllegatoDichiarazioneVO> vAllAllegatiAttivi = gaaFacadeClient
            .getElencoAllegatiAttiviDichiarazione(new Long(consistenzaVO.getIdDichiarazioneConsistenza()));
        
        if(Validator.isNotEmpty(vAllAllegatiAttivi))
        {
          for(int i=0;i<vAllAllegatiAttivi.size();i++)
          {
            AllegatoDichiarazioneVO allegatoDichiarazioneVO = vAllAllegatiAttivi.get(i);
            //Controllo se esite un record attivo tra gli allegati inseribili
            //senza aver valorizzato ext_id_documento 
            vIdTipoAllegato.add(allegatoDichiarazioneVO.getIdTipoAllegato());
          }
        }
        
        
        for(int i=0;i<vIdTipoAllegato.size();i++)
        {
          Long idTipoAllegato = vIdTipoAllegato.get(i);
          AllegatoDichiarazioneVO allegatoDichiarazioneAttivo = gaaFacadeClient
            .getAllegatoDichiarazioneFromIdDichiarazione(idDichiarazioneConsistenzaLg, idTipoAllegato);
          TipoAllegatoVO tipoAllegatoVO = gaaFacadeClient.getTipoAllegatoById(idTipoAllegato.intValue());
          request.setAttribute("tipoAllegatoVO", tipoAllegatoVO);
          //Verifico se è attivo ed ha ext_id_documento_index valorizzato
          //va bene e nn devo fare nulla
          if (Validator.isNotEmpty(allegatoDichiarazioneAttivo)
              && Validator.isEmpty(allegatoDichiarazioneAttivo.getExtIdDocumentoIndex()))
          {
            //Validazione
            if(idTipoAllegato.compareTo(new Long(SolmrConstants.VALIDAZIONE_ALLEGATO)) == 0)
            {
              //TipoAllegatoVO tipoAllegatoVO = gaaFacadeClient.getTipoAllegatoById(idTipoAllegato.intValue());
              //request.setAttribute("tipoAllegatoVO", tipoAllegatoVO);
              PdfFascicoloModol generaPdf = new PdfFascicoloModol();
              generaPdf.generaDocumento(request, response);       
            }
            //Allegato
            else
            {           
              //genero l'allegato...
              PdfFascicoloAllegatoInseribileModol generaPdf = new PdfFascicoloAllegatoInseribileModol();
              generaPdf.generaDocumento(request, response);
            }
          }
          
        }
        
        
        
        
        
        
        
        
        //Carico tutti gli allegati obbligatori automatici
        /*Vector<AllegatoDichiarazioneVO> vDocAllDefault = gaaFacadeClient
            .getElencoAllegatiDichiarazioneDefault(consistenzaVO.getDataInserimentoDichiarazione(), 
              new Integer(consistenzaVO.getIdMotivo()));
        for(int i=0;i<vDocAllDefault.size();i++)
        {
          vIdTipoAllegato.add(vDocAllDefault.get(i).getIdTipoAllegato());
        }
        //carico allegati presenti inseribili senza ext_id_documento_index valorizzato..
        //e sono presenti solo col protocollo valorizzato...
        if(Validator.isNotEmpty(consistenzaVO.getNumeroProtocollo()))
        {
          Vector<AllegatoDichiarazioneVO> vAllAllegatiAttivi = gaaFacadeClient
            .getElencoAllegatiAttiviDichiarazione(new Long(consistenzaVO.getIdDichiarazioneConsistenza()));
          //prendo solo quelli inseribili e non presenti su index...
          if(Validator.isNotEmpty(vAllAllegatiAttivi))
          {
            for(int i=0;i<vAllAllegatiAttivi.size();i++)
            {
              AllegatoDichiarazioneVO allegatoDichiarazioneVO = vAllAllegatiAttivi.get(i);
              //Controllo se esite un record attivo tra gli allegati inseribili
              //senza aver valorizzato ext_id_documento 
              if("S".equalsIgnoreCase(allegatoDichiarazioneVO.getFlagInseribile())
                  && Validator.isEmpty(allegatoDichiarazioneVO.getExtIdDocumentoIndex()))
              {
                vIdTipoAllegato.add(allegatoDichiarazioneVO.getIdTipoAllegato());                
              }
            }
          }
        }
        
        
        
        //Cliccando su qualunque icona di recupero viene sempre rigenerato tutto
        //quello andato male...
        for(int i=0;i<vIdTipoAllegato.size();i++)
        {
          Long idTipoAllegato = vIdTipoAllegato.get(i);
          AllegatoDichiarazioneVO allegatoDichiarazioneAttivo = gaaFacadeClient
            .getAllegatoDichiarazioneFromIdDichiarazione(idDichiarazioneConsistenzaLg, idTipoAllegato);
          TipoAllegatoVO tipoAllegatoVO = gaaFacadeClient.getTipoAllegatoById(idTipoAllegato.intValue());
          request.setAttribute("tipoAllegatoVO", tipoAllegatoVO);
          //Verifico se è attivo...in questo è già presente 
          //va bene e nn devo fare nulla
          if(Validator.isEmpty(allegatoDichiarazioneAttivo))
          {          
            AllegatoDichiarazioneVO allegatoDichiarazioneStor = 
                gaaFacadeClient.getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric(
                    idDichiarazioneConsistenzaLg, vIdTipoAllegato.get(i));
            
            // è già presente per la stessa validazione l'allegato su db
            // ma storicizzato quindi devo ricrearlo ma sovrascrivendo...
            // oppure è stata selezionato il protocolla...
            if(allegatoDichiarazioneStor != null)
            {
              //request.getSession().removeAttribute("idDichiarazioneConsistenzaDaValid");
              PdfFascicoloRigeneraModol generaPdf = new PdfFascicoloRigeneraModol();
              generaPdf.generaDocumento(request, response);          
            }
            //caso di fallimento prima creazione file!!!!!
            else
            {
              //request.getSession().removeAttribute("idDichiarazioneConsistenzaProtocolla");
              //Creo Allegato
              if(idTipoAllegato.compareTo(new Long(SolmrConstants.VALIDAZIONE_ALLEGATO)) != 0)
              {
                //TipoAllegatoVO tipoAllegatoVO = gaaFacadeClient.getTipoAllegatoById(idTipoAllegato.intValue());
                //request.setAttribute("tipoAllegatoVO", tipoAllegatoVO);
                PdfFascicoloAllegatoModol generaAllegatoPdf = new PdfFascicoloAllegatoModol();
                generaAllegatoPdf.generaDocumento(request, response);            
              }
              //Creo Validazione
              else
              {
                PdfFascicoloModol generaPdf = new PdfFascicoloModol();
                generaPdf.generaDocumento(request, response);
              }
            }
          }
          //Caso allegati dalla sezione allegati....
          else if (Validator.isNotEmpty(allegatoDichiarazioneAttivo)
            && Validator.isEmpty(allegatoDichiarazioneAttivo.getExtIdDocumentoIndex()))
          {           
            //genero l'allegato...
            PdfFascicoloAllegatoInseribileModol generaPdf = new PdfFascicoloAllegatoInseribileModol();
            generaPdf.generaDocumento(request, response);
          }
        }*/
        
        
        
        returVal = "success";
      }
      catch(Exception ex)
      {
        returVal = "failed";
      }
      
      //request.getSession().removeAttribute("idDichiarazioneConsistenzaDaValid");
      request.getSession().removeAttribute("idDichiarazioneConsistenzaStampa");
    }
    
    
    
    out.print(returVal);
    
    
    
  }

  
  
  
  
  

  

  
  
  
  

  

  
  
  
  
  
  
  
  
  
  
  
  
  
  
}
