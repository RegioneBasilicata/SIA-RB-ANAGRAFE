package it.csi.smranag.smrgaa.presentation.client.stampe.modol;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttDichiarataVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;

import java.awt.Font;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

/**
 * Classe base per la creazione di subreport da utilizzare come Quadri/Sezioni
 * nelle stampe. Contiene la logica di base per la gestione del subreport,
 * compresa la gesteione della cache del template.
 * 
 * @author TOBECONFIG (71646)
 */
public abstract class SubReportModol
{
  public final static Font             SERIF_BOLD                = new Font(
                                                                     "Serif",
                                                                     Font.BOLD,
                                                                     10);
  public final static Font             SERIF_9                   = new Font(
                                                                     "Serif",
                                                                     Font.PLAIN,
                                                                     9);

  private String                       codiceSubReport           = null;

  public SubReportModol(String codiceSubReport)
      throws IOException, SolmrException
  {
    this.codiceSubReport = codiceSubReport;
  }

  

  public abstract void processSubReport(
      RichiestaTipoReportVO richiestaTipoReportVO, Fascicolo fascicoDigitale,
      HttpServletRequest request, AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient,
      HashMap<String, Object> parametri) throws Exception;

  @SuppressWarnings("unchecked")
  public boolean checkIfRequired(RichiestaTipoReportVO richiestaTipoReportVO,
      HashMap<String, Object> parametri) throws Exception
  {
    boolean visualizzo = false;
    Vector<Long> vIdQuadriSelezionati = (Vector<Long>)parametri.get("vIdQuadriSelezionati");
    //Se valorizzato arrivo da una stampa in cui selezioni i quadri
    if(vIdQuadriSelezionati != null)
    {
      if(richiestaTipoReportVO.isVisibile())
      {
        //Quadro principale senza figli
        if(richiestaTipoReportVO.getIdReportSubReportPadre() == null)
        {
          //Quadro obbligatorio non selezionabile
          if(!richiestaTipoReportVO.isSelezionabile())
          {
            visualizzo = true;
          }
          else
          {
            //Quadro non obbligatorio selezionabile
            if(vIdQuadriSelezionati.contains(richiestaTipoReportVO.getIdReportSubReport()))
            {
              visualizzo = true;
            }
          }
        }
        else
        {
          //Non è selezionabile ma è stato selezionato il padre
          if(!richiestaTipoReportVO.isSelezionabile()
              && vIdQuadriSelezionati.contains(richiestaTipoReportVO.getIdReportSubReportPadre()))
          {
            visualizzo = true;
          }
        }
      }
    }
    //Stampa senza quadri selezionabili
    else
    {
      visualizzo = richiestaTipoReportVO.isVisibile();
    }
    
    return visualizzo;
  }

  public String getCodiceSubReport()
  {
    return codiceSubReport;
  }
  
  
  public String creaLayoutForAttestazioni(ParametriAttAziendaVO parametriAttAziendaVO, 
      ParametriAttDichiarataVO parametriAttDichiarataVO, Long idPianoRiferimento, 
      TipoAttestazioneVO tipoAttestazioneVO) 
  {
    try 
    { 
      StringBuffer sb = new StringBuffer(tipoAttestazioneVO.getDescrizione());
      while(sb.toString().toUpperCase().lastIndexOf("##") != -1) 
      {
        String nomeParametro = "";
        int start = sb.toString().indexOf("##");
        int end = sb.toString().indexOf(" ", start);
        if(end != -1) 
        {
          nomeParametro = sb.substring(start, end);
        }
        else 
        {
          nomeParametro = sb.substring(start);
        }
        nomeParametro = StringUtils.replace(nomeParametro, "##", "");
        Object valore = null;
        // Piano di riferimento "in lavorazione"
        if(idPianoRiferimento == null) 
        {
          if(parametriAttAziendaVO != null) 
          {
            valore = parametriAttAziendaVO.getClass().getMethod("get" +org.apache.commons.lang.StringUtils.capitalize(nomeParametro), (Class[])null).invoke(parametriAttAziendaVO, (Object[])null);
          }
          else 
          {
            valore = null;
          }
        }
        else 
        {
          if(parametriAttDichiarataVO != null) 
          {
            valore = parametriAttDichiarataVO.getClass().getMethod("get" +org.apache.commons.lang.StringUtils.capitalize(nomeParametro), (Class[])null).invoke(parametriAttDichiarataVO, (Object[])null);
          }
          else 
          {
            valore = null;
          }
        }
        
        if(valore != null) 
        {
          if(end != -1) 
          {
            sb.replace(start, end, (String)valore);
          }
          else {
            sb.replace(start, sb.length(), (String)valore);
          }
        }
        else 
        {
          if(end != -1) 
          {
            sb.replace(start, end, "......");
          }
          else 
          {
            sb.replace(start, sb.length(), "......");
          }
        }
      }
      return sb.toString();
    }
    catch(NoSuchMethodException nme) {
      return "";
    }
    catch(IllegalAccessException iae) {
      return "";
    }
    catch(InvocationTargetException ite) {
      return "";
    }
  }

}
