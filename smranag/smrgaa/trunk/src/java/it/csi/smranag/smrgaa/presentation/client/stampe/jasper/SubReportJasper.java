package it.csi.smranag.smrgaa.presentation.client.stampe.jasper;

import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.Protocollo;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.exception.SolmrException;

/**
 * Classe base per la creazione di subreport da utilizzare come Quadri/Sezioni
 * nelle stampe. Contiene la logica di base per la gestione del subreport,
 * compresa la gesteione della cache del template.
 * 
 * @author Massimo Durando
 */
public abstract class SubReportJasper
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

  public SubReportJasper(String codiceSubReport)
      throws IOException, SolmrException
  {
    this.codiceSubReport = codiceSubReport;
  }

  

  public abstract void processSubReport(Protocollo protocollo, RichiestaTipoReportVO richiestaTipoReportVO, HttpServletRequest request,
	      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
	    	      throws Exception;

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
  
  

}
