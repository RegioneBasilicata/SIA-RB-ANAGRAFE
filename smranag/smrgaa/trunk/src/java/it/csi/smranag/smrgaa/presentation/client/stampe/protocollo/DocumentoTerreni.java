package it.csi.smranag.smrgaa.presentation.client.stampe.protocollo;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;

import java.awt.Dimension;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class DocumentoTerreni extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/protocollo/DocumentoTerreni.srt";
  private final static String CODICE_SUB_REPORT = "DOCUMENTO_TERRENI";

  public DocumentoTerreni() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    DocumentoVO documentoVO = (DocumentoVO)parametri.get("documentoVO"); 
    
    
    
    //Controllo se sono nel nuovo o nel vecchio stile
    boolean flagOldStyle = false;
    Date dataConfronto = null;
    try
    {
      String parametroFAPP = anagFacadeClient
        .getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_FAPP);
      dataConfronto = DateUtils.parseDate(parametroFAPP);
      if(documentoVO.getDataInizioValidita() != null)
      {
        if(documentoVO.getDataInizioValidita().before(dataConfronto))
        {
          flagOldStyle = true;
        }
      }
    }
    catch(Exception ex)
    {}
    
    
    
    
    int size=0;
    Vector<?> elencoParticelle = documentoVO.getElencoParticelle();
    
    if(elencoParticelle!=null)
    {
      size=elencoParticelle.size();
    }
    
    if (size>0)
    {
      subReport.removeElement("txtNoTerreni");     
      
      DefaultTableLens tblTerreni = new DefaultTableLens(subReport.getTable("tblTerreni"));
      
      for (int i=0;i<11;i++)
      {
        tblTerreni.setFont(0, i, StampeGaaServlet.FONT_SERIF_BOLD_8);
        tblTerreni.setAlignment(0, i, StyleConstants.H_CENTER);
        tblTerreni.setFont(1, i, StampeGaaServlet.FONT_SERIF_BOLD_8);
        tblTerreni.setAlignment(1, i, StyleConstants.H_CENTER);
      }      
        
      // Imposto l'header
      int col = 0;
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 130);//comune
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 25);//sezione
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 25);//foglio
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 25);//particella
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 25);//subalterno
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 50);//superficie catastale
      tblTerreni.setSpan(0, col , new Dimension(2, 1));
      tblTerreni.setObject(0, col, StampeGaaServlet.checkNull("Conduzione"));
      tblTerreni.setColWidth(col++, 25);//codice conduzione
      if(flagOldStyle)
      {
        tblTerreni.setObject(1, col, StampeGaaServlet.checkNull("Sup."));
      }
      else
      {
        tblTerreni.setObject(1, col, StampeGaaServlet.checkNull("%"));
      }
      tblTerreni.setColWidth(col++, 50);//superficie/percentuale candotta
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 25);//C.P.
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 73);//Data Scadenza
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 100);//Note
    
      for (int i = 0; i < size; i++)
      {
        BigDecimal temp=new BigDecimal(0);
        tblTerreni.addRow();
        StoricoParticellaVO particella= (StoricoParticellaVO) elencoParticelle.get(i);
        col = 0;
        String descComune = particella.getComuneParticellaVO().getDescom()+" ("
          +particella.getComuneParticellaVO().getSiglaProv()+")";
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(descComune));
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getSezione()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getFoglio()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getParticella()));
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getSubalterno()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        try
        {
          temp = new BigDecimal(particella.getSupCatastale());
        }
        catch(Exception e)
        {
          temp=new BigDecimal(0);
        }
        tblTerreni.setObject(i + 2, col++, StringUtils.parseSuperficieFieldBigDecimal(temp));
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(
            particella.getElencoConduzioni()[0].getIdTitoloPossesso()));
        
        if(flagOldStyle)
        {
          try
          {
            temp = new BigDecimal(particella.getElencoConduzioni()[0].getSuperficieCondotta());
          }
          catch(Exception e)
          {
            temp=new BigDecimal(0);
          }
          tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
          tblTerreni.setObject(i + 2, col++, StringUtils.parseSuperficieFieldBigDecimal(temp));
        }
        else
        {
          tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
          tblTerreni.setObject(i + 2, col++, ""+particella.getElencoConduzioni()[0].getPercentualePossesso());
        }        
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdCasoParticolare()));
        
        
        
        if(particella.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0] != null)
        {
          tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
          tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(
            DateUtils.formatDateNotNull(particella.getElencoConduzioni()[0]
            .getElencoDocumentoConduzione()[0].getMaxDataFineValidita())));
          
          tblTerreni.setAlignment(i + 2, col, StyleConstants.H_LEFT);
          tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(
            particella.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getNote()));
        }
      }
      
      subReport.setElement("tblTerreni", tblTerreni);
    }
    else
    {
      subReport.removeElement("tblTerreni");
    }
    
    
    
    
    
    
    
    
    
  }
}