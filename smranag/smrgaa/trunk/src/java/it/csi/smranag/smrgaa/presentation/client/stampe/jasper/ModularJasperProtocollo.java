package it.csi.smranag.smrgaa.presentation.client.stampe.jasper;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.Protocollo;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.StampeModolGaaServlet;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

public abstract class ModularJasperProtocollo extends StampeModolGaaServlet
{
  /**
   * 
   */
  private static final long serialVersionUID = -2821978490279522457L;
  public HashMap<String, SubReportJasper> mapSubReports = new HashMap<String, SubReportJasper>();

  @Override
  public void init(ServletConfig config) throws ServletException
  {
    super.init(config);
    try
    {
      preloadSubReports();
    }
    catch (Exception e)
    {
      SolmrLogger.dumpStackTrace(this, "[ModularReport::init] ", e);
      throw new ServletException(e.getMessage());
    }
  }

  protected void addSubReport(SubReportJasper subReport) throws SolmrException,
      IOException
  {
    mapSubReports.put(subReport.getCodiceSubReport(), subReport);
  }

  /**
   * Carica da db i subreport per la tipologia di domanda e li inserisce nel
   * report, 1 per riga restituendo alla fine il numero dell'ultima riga che è
   * stata inserita
   * 
   * @param report
   * @param riga
   * @param idTipoRichiesta
   * @param codice
   * @param avivFacadeClient
   * @param request
   * @param parametri
   * @return numero dell'ultima riga che è stata inserita
   * @throws AtrvwebException
   * @throws Exception
   */
  protected void processSubReports(Protocollo protocollo,
      String codiceStampa,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HttpServletRequest request,
      HashMap<String, Object> parametri) throws Exception
  {
    
    Date dataRiferimento = (Date)parametri.get("dataRiferimento");
    RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
    
    RichiestaTipoReportVO subReportElements[] = gaaFacadeClient
        .getElencoSubReportRichiesta(codiceStampa, dataRiferimento);
    if (subReportElements != null)
    {
      
      //Stampa protocollo
      //Arrivo da una stampa in cui seleziono i quadri
      String idTipoReport = (String)parametri.get("idTipoReport");
      //perchè potrebbero esserci diversi documenti con diversi subreport
      String[] arrCodiceStampa = request.getParameterValues("codiceStampa_"+idTipoReport);
      //Controllo eventualmente per le altre stampe
      if(Validator.isEmpty(arrCodiceStampa))
      {
        arrCodiceStampa = request.getParameterValues("codiceStampa");
      }
      if(arrCodiceStampa != null)
      {
        Vector<Long> vQuadriSelezionati = new Vector<Long>();
        for(int i=0;i<arrCodiceStampa.length;i++)
        {
          Long idReportSubReport = new Long(arrCodiceStampa[i]);
          if(!vQuadriSelezionati.contains(idReportSubReport))
          {
            vQuadriSelezionati.add(idReportSubReport);
          }
        }
        
        Vector<Long> vIdQuadriSelezionati = new Vector<Long>();
        for (RichiestaTipoReportVO subElement : subReportElements)
        {
          if(vQuadriSelezionati.contains(subElement.getIdReportSubReport()))
          {
            vIdQuadriSelezionati.add(subElement.getIdReportSubReport());
          }
        }
        
        parametri.put("vIdQuadriSelezionati", vIdQuadriSelezionati);
      }
      
      
      //for (RichiestaTipoReportVO subElement : subReportElements)
      for (int i=0;i<subReportElements.length;i++)
      {
        
        RichiestaTipoReportVO subElement = subReportElements[i];
        
        //Se PA Locale non devo fare vedere alcuni quadri
        if(ruoloUtenza.isUtentePALocale() || ruoloUtenza.isUtentePALocaleSuper())
        {
          String parametroSpal = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_SPAL);
          
          HashMap<String,String> hMapQuadriPA = new HashMap<String,String>();  
          StringTokenizer strTokenQuadriPA = new StringTokenizer(parametroSpal,",");
          while(strTokenQuadriPA.hasMoreTokens())
          {
            String strTemp = strTokenQuadriPA.nextToken();
            hMapQuadriPA.put(strTemp,strTemp);
          }
          
          if(hMapQuadriPA.get(subElement.getIdReportSubReport().toString()) != null)
          {
            subElement.setVisibile(false);
          }
        }
        
        //Se nuova iscrizione e privato nn devo fare vedere il quadro
        //rappresentante legale
        AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)parametri.get("aziendaNuovaVO");
        if(Validator.isNotEmpty(aziendaNuovaVO))
        {
          if(aziendaNuovaVO.getCuaa().length() == 16)
          {
            if(subElement.getIdTipoSubReport().compareTo(
                SolmrConstants.COD_TIPO_SUB_REPORT_RAPP_LEGALE) == 0)
            {
              subElement.setVisibile(false);
            }
          }
          
        }  
        
        
        SubReportJasper subReport = mapSubReports.get(subElement.getCodice());
        
        if (subReport == null)
        {
          throw new SolmrException(
              AnagErrors.MSG_ERRORE_CONTATTARE_ASSISTENZA
                  + "Non è stato trovato il subreport "
                  + subElement.getCodice());
        }
        if (subReport.checkIfRequired(subElement, parametri))
        {
          
          subReport.processSubReport(protocollo, subElement, request,
              anagFacadeClient, gaaFacadeClient, parametri);
        }
      }
    }
    else
    {
      throw new SolmrException(AnagErrors.MSG_ERRORE_CONTATTARE_ASSISTENZA
          + " Non sono presenti quadri per questa tipologia di stampa");
    }
    
  }

  protected abstract void preloadSubReports() throws SolmrException,
      IOException;

}
