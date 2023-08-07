package it.csi.smranag.smrgaa.presentation.client.stampe;

import inetsoft.report.ReportSheet;
import inetsoft.report.TabularSheet;
import inetsoft.report.io.Builder;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

/**
 * Classe base per la creazione di subreport da utilizzare come Quadri/Sezioni
 * nelle stampe. Contiene la logica di base per la gestione del subreport,
 * compresa la gesteione della cache del template.
 * 
 * @author TOBECONFIG (70399)
 */
public abstract class SubReport
{
  public final static Font             SERIF_BOLD                = new Font(
                                                                     "Serif",
                                                                     Font.BOLD,
                                                                     10);
  public final static Font             SERIF_9                   = new Font(
                                                                     "Serif",
                                                                     Font.PLAIN,
                                                                     9);

  private String                       templateName              = null;
  private String                       codiceSubReport           = null;
  private HashMap<String, ReportSheet> reportCache               = new HashMap<String, ReportSheet>();
  private boolean                      ignoraSaltoPaginaIniziale = false;

  public SubReport(String templateName, String codiceSubReport)
      throws IOException, SolmrException
  {
    this.templateName = templateName;
    this.codiceSubReport = codiceSubReport;
    loadAndCacheNewReport(templateName, codiceSubReport);
  }

  protected ReportSheet loadAndCacheNewReport(String newTemplateName,
      String codice) throws IOException, SolmrException
  {
    ReportSheet subReport = loadNewReport(newTemplateName);
    reportCache.put(codice, subReport);
    return subReport;
  }

  protected ReportSheet loadNewReport(String newTemplateName)
      throws IOException, SolmrException
  {
    InputStream is = this.getClass().getResourceAsStream(newTemplateName);
    if (is == null)
    {
      throw new SolmrException(
          "Impossibile trovare il template del subreport " + codiceSubReport);
    }
    ReportSheet subReport = Builder.getBuilder(Builder.TEMPLATE, is).read("./");
    return subReport;
  }

  public String getTemplateName()
  {
    return templateName;
  }

  public void setTemplateName(String templateName)
  {
    this.templateName = templateName;
  }

  protected ReportSheet newTemplateInstance(String name)
      throws SolmrException
  {
    ReportSheet reportFromCache = reportCache.get(name);
    if (reportFromCache == null)
    {
      throw new SolmrException(AnagErrors.MSG_ERRORE_CONTATTARE_ASSISTENZA
          + "Impossibile caricare il template del subreport " + codiceSubReport);
    }
    return (ReportSheet) reportFromCache.clone();
  }

  protected ReportSheet newTemplateInstance() throws SolmrException
  {
    return newTemplateInstance(codiceSubReport);
  }

  public abstract void processSubReport(
      RichiestaTipoReportVO richiestaTipoReportVO, ReportSheet subReport,
      HttpServletRequest request, AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient,
      HashMap<String, Object> parametri) throws Exception;

  public ReportSheet execute(RichiestaTipoReportVO richiestaTipoReportVO,
      HttpServletRequest request, AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient,
      HashMap<String, Object> parametri, TabularSheet report) throws Exception
  {
    ReportSheet subReport = newTemplateInstance();
    subReport.setBackground(report.getBackground());
    //subReport.setBackgroundImageLocation(report.getBackgroundImageLocation());
    subReport.setBackgroundLayout(report.getBackgroundLayout());
    processSubReport(richiestaTipoReportVO, subReport, request,
        anagFacadeClient, gaaFacadeClient, parametri);
    return subReport;

  }

  @SuppressWarnings("unchecked")
  public boolean checkIfRequired(RichiestaTipoReportVO richiestaTipoReportVO,
      HttpServletRequest request, AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient,
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

  public void setQuadroAndSezione(ReportSheet subReport,
      RichiestaTipoReportVO richiestaTipoReportVO)
  {
    setQuadroAndSezione(subReport, richiestaTipoReportVO.getQuadro(),
        richiestaTipoReportVO.getSezione());
  }

  public void setQuadroAndSezione(ReportSheet subReport, String descQuadro,
      String descSezione)
  {
    if (descQuadro != null)
    {
      subReport.setElement("lblQuadro", StampeGaaServlet.checkNull(descQuadro));
    }
    else
    {
      subReport.removeElement("lblQuadro");
      subReport.removeElement("nl1");
    }
    if (descSezione != null)
    {
      subReport.setElement("lblSezione", StampeGaaServlet.checkNull(descSezione));
    }
    else
    {
      subReport.removeElement("lblSezione");
      subReport.removeElement("nl1");
    }
  }

  public String getCodiceSubReport()
  {
    return codiceSubReport;
  }

  public boolean isIgnoraSaltoPaginaIniziale()
  {
    return ignoraSaltoPaginaIniziale;
  }

  public void setIgnoraSaltoPaginaIniziale(boolean ignoraSaltoPaginaIniziale)
  {
    this.ignoraSaltoPaginaIniziale = ignoraSaltoPaginaIniziale;
  }
}
