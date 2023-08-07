package it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AzAssAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.smrcomms.reportdin.util.DateUtils;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class ElAssRichAz extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/nuovaiscrizione/ElAssRichAz.srt";
  private final static String CODICE_SUB_REPORT = "EL_ASS_RICH_AZ";

  public ElAssRichAz() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    Long idAziendaNuova = (Long)parametri.get("idAziendaNuova");
    
    AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
    
    Vector<AzAssAziendaNuovaVO> vAssAzienda = null;
    //Caso del'inserimento caa
    if(Validator.isNotEmpty(aziendaNuovaVO.getCodEnte()))
    {
      vAssAzienda = gaaFacadeClient.getAziendeAssociateCaaStampaAziendaNuovaIscrizione(idAziendaNuova.longValue());
    }
    else
    {    
      vAssAzienda = gaaFacadeClient.getAziendeAssociateAziendaNuovaIscrizione(idAziendaNuova.longValue());
    }
      
    if ((vAssAzienda != null) && (vAssAzienda.size() > 0))
    {
      // Rimuovo il testo indicante l'assenza di UTE associate all'azienda
      subReport.removeElement("txtNessunAssociato");

      DefaultTableLens tblElencoAssociati = null;

      // Recupero le coordinate della cella in cui mi trovo
      tblElencoAssociati = new DefaultTableLens(subReport.getTable("tblElencoAssociati"));

      tblElencoAssociati.setColWidth(0, 100);
      tblElencoAssociati.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblElencoAssociati.setColWidth(1, 80);
      tblElencoAssociati.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblElencoAssociati.setColWidth(2, 320);
      tblElencoAssociati.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblElencoAssociati.setColWidth(3, 70);
      tblElencoAssociati.setFont(0, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);

      tblElencoAssociati.setAlignment(0, 0, StyleConstants.H_CENTER);
      tblElencoAssociati.setAlignment(0, 1, StyleConstants.H_CENTER);
      tblElencoAssociati.setAlignment(0, 2, StyleConstants.H_CENTER);
      tblElencoAssociati.setAlignment(0, 3, StyleConstants.H_CENTER);

      // this.setBorderRiquadri(tblTemp);
      for (int i = 0; i < vAssAzienda.size(); i++)
      {
        tblElencoAssociati.addRow();
        AzAssAziendaNuovaVO azAssAziendaNuovaVO = vAssAzienda.get(i);
        tblElencoAssociati.setObject(i + 1, 0, StampeGaaServlet.checkNull(azAssAziendaNuovaVO.getCuaa()));
        tblElencoAssociati.setObject(i + 1, 1, StampeGaaServlet.checkNull(azAssAziendaNuovaVO.getPartitaIva()));
        tblElencoAssociati.setObject(i + 1, 2, StampeGaaServlet.checkNull(azAssAziendaNuovaVO.getDenominazione()));
        if(Validator.isNotEmpty(azAssAziendaNuovaVO.getDataIngresso()))
          tblElencoAssociati.setObject(i + 1, 3, DateUtils.formatDate(azAssAziendaNuovaVO.getDataIngresso()));
        else
          tblElencoAssociati.setObject(i + 1, 3, "");
        tblElencoAssociati.setAlignment(i + 1, 3, StyleConstants.H_CENTER);
      }
      
      subReport.setElement("tblElencoAssociati", tblElencoAssociati);

    }
    else
    {
      // Rimuovo gli elementi non più necessari
      subReport.removeElement("tblElencoAssociati");
    }
    
    
    
  }
}