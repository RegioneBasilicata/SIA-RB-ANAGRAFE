package it.csi.smranag.smrgaa.presentation.pdf;

import inetsoft.report.ReportSheet;
import inetsoft.report.TabularSheet;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public abstract class ModularReportPdf extends GeneraPDF
{
  public HashMap<String, SubReport> mapSubReports = new HashMap<String, SubReport>();


  public ModularReportPdf() throws Exception
  {
    preloadSubReports();
  }

  protected void addSubReport(SubReport subReport) throws SolmrException,
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
   */
  protected int processSubReports(TabularSheet report, int riga,
      String codiceStampa,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HttpServletRequest request,
      HashMap<String, Object> parametri) throws Exception
  {
    
    Date dataRiferimento = (Date)parametri.get("dataRiferimento");
    
    RichiestaTipoReportVO subReportElements[] = gaaFacadeClient
        .getElencoSubReportRichiesta(codiceStampa, dataRiferimento);
    if (subReportElements != null)
    {
      
      //Arrivo da una stampa in cui seleziono i quadri
      String idTipoReport = (String)parametri.get("idTipoReport");
      //perchè potrebbero esserci diversi documenti con diversi subreport
      String[] arrCodiceStampa = request.getParameterValues("codiceStampa_"+idTipoReport);
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
      
      for (int i=0;i<subReportElements.length;i++)
      {        
        RichiestaTipoReportVO subElement = subReportElements[i];
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
          
          /*Long idAllegato = (Long)parametri.get("idAllegato");
          
          if(Validator.isEmpty(idAllegato))
          {
            if(subElement.getIdTipoSubReport().compareTo(
                SolmrConstants.COD_TIPO_SUB_REPORT_STAMPA_IMG) == 0)
            {
              subElement.setVisibile(false);
            }
          }*/
        }
      }
      
      
      //for (RichiestaTipoReportVO subElement : subReportElements)
      for (int i=0;i<subReportElements.length;i++)
      {
        
        RichiestaTipoReportVO subElement = subReportElements[i];        
        
        SubReport subReport = mapSubReports.get(subElement.getCodice());
        if (subReport == null)
        {
          throw new SolmrException(
              AnagErrors.MSG_ERRORE_CONTATTARE_ASSISTENZA
                  + "Non è stato trovato il subreport "
                  + subElement.getCodice());
        }
        if (subReport.checkIfRequired(subElement, request, anagFacadeClient, gaaFacadeClient,
            parametri))
        {
          
          boolean saltoPagina = !subReport.isIgnoraSaltoPaginaIniziale()
            && subElement.isSaltoPaginaIniziale();          
          
          ReportSheet vectSubReport = subReport.execute(subElement, request,
              anagFacadeClient, gaaFacadeClient, parametri, report);
          addSubReportToMainReport(report, vectSubReport, new int[]
          { riga++, 0 }, saltoPagina);
        }
      }
    }
    else
    {
      throw new SolmrException(AnagErrors.MSG_ERRORE_CONTATTARE_ASSISTENZA
          + " Non sono presenti quadri per questa tipologia di stampa");
    }
    return riga;
  }

  protected abstract void preloadSubReports() throws SolmrException,
      IOException;

}
