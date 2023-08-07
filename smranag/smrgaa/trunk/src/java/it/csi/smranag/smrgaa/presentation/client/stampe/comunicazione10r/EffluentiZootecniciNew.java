package it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.comunicazione10R.Comunicazione10RVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.RefluoEffluenteVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class EffluentiZootecniciNew extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comunicazione10r/EffluentiZootecniciNew.srt";
  private final static String CODICE_SUB_REPORT = "EFFLUENTI_ZOOTECNICI_NEW";

  public EffluentiZootecniciNew() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    
    Comunicazione10RVO[] comunicazione10RVO = (Comunicazione10RVO[])parametri.get("comunicazione10RVO");    
    
    int size = 0;
    Vector<RefluoEffluenteVO> vReflui = null;
    Vector<String> note = new Vector<String>();
    
    if (comunicazione10RVO != null && comunicazione10RVO.length!=0)
    {
      long idComunicazione[]=new long[comunicazione10RVO.length];
      long idUte[]=new long[comunicazione10RVO.length];
      
      //Mi ricavo tutti gli di degli effluenti e anche le note
      for (int i=0;i<comunicazione10RVO.length;i++)
      {
        idComunicazione[i]=comunicazione10RVO[i].getIdComunicazione10R();
        if (Validator.isNotEmpty(comunicazione10RVO[i].getNote()))
          note.add("- "+comunicazione10RVO[i].getNote());
        idUte[i] = comunicazione10RVO[i].getIdUte();
      }
      
      vReflui = gaaFacadeClient.getListRefluiStampa(idComunicazione);
    }
    
    
    if (vReflui != null)
      size = vReflui.size();

    if (size > 0)
    {
      // Rimuovo il testo indicante l'assenza di consistenze zootecniche
      // associate all'azienda
      subReport.removeElement("txtNoEffluenti");
      subReport.removeElement("nlNoEffluenti");

      DefaultTableLens tblEffluenti = new DefaultTableLens(subReport.getTable("tblEffluenti"));

      // Imposto l'header
      int col = 0;
      tblEffluenti.setColWidth(col, 140);
      tblEffluenti.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblEffluenti.setColWidth(col, 120);
      tblEffluenti.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblEffluenti.setColWidth(col, 120);
      tblEffluenti.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblEffluenti.setColWidth(col, 70);
      tblEffluenti.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setFont(1, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setAlignment(0, col, StyleConstants.H_CENTER);
      tblEffluenti.setAlignment(1, col++, StyleConstants.H_CENTER);
      tblEffluenti.setColWidth(col, 50);
      tblEffluenti.setFont(1, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setAlignment(1, col++, StyleConstants.H_CENTER);

      BigDecimal totVolumeUtilAgro = new BigDecimal(0);
      BigDecimal totAzotoUtilAgro = new BigDecimal(0);
      
      int numRiga = 2;
      for (int i = 0; i < size; i++)
      {
        RefluoEffluenteVO refluo = (RefluoEffluenteVO) vReflui.get(i);
        col = 0;
        
        boolean flagValoreUnoZero = true;
        boolean flagValoreDueZero = true;
        BigDecimal volumeUtilizzoAgronomico = new BigDecimal(0);
        BigDecimal azotoUtilizzoAgronomico = new BigDecimal(0);
        if(refluo.getVolumeUtilizzoAgronomico().compareTo(new BigDecimal(0)) > 0)
        {
          volumeUtilizzoAgronomico = refluo.getVolumeUtilizzoAgronomico();
          flagValoreUnoZero = false;
        }
        
        if(refluo.getAzotoUtilizzoAgronomico().compareTo(new BigDecimal(0)) > 0)
        {
          azotoUtilizzoAgronomico = refluo.getAzotoUtilizzoAgronomico();
          flagValoreDueZero = false;
        }      
        
        if(!flagValoreUnoZero || !flagValoreDueZero)
        {
          tblEffluenti.addRow();
          String ute = refluo.getComuneUte()+" ("+refluo.getSglProvUte()+") - "+refluo.getIndirizzoUte();
          tblEffluenti.setObject(numRiga, col++, StampeGaaServlet.checkNull(ute));
          
          tblEffluenti.setObject(numRiga, col++, StampeGaaServlet.checkNull(refluo.getTipoEffluente()));
          tblEffluenti.setObject(numRiga, col++, StampeGaaServlet.checkNull(refluo.getDescrizione()));
          
         
          
          tblEffluenti.setObject(numRiga, col, StringUtils.parseDoubleFieldBigDecimal(volumeUtilizzoAgronomico));
          tblEffluenti.setAlignment(numRiga, col++, StyleConstants.H_CENTER);
          totVolumeUtilAgro = totVolumeUtilAgro.add(volumeUtilizzoAgronomico);
          
          tblEffluenti.setObject(numRiga, col, Formatter.formatAndRoundBigDecimal0(azotoUtilizzoAgronomico));
          tblEffluenti.setAlignment(numRiga, col++, StyleConstants.H_CENTER);
          totAzotoUtilAgro = totAzotoUtilAgro.add(azotoUtilizzoAgronomico); 
          numRiga++;
        }
        
        

        
      }
      

      //Aggiungo la riga dei totali
      tblEffluenti.addRow();
      tblEffluenti.setObject(numRiga, 2, "Totale");
        
      tblEffluenti.setObject(numRiga, 3, Formatter.formatDouble1(totVolumeUtilAgro));
      tblEffluenti.setAlignment(numRiga, 3, StyleConstants.H_CENTER);
      tblEffluenti.setObject(numRiga, 4, Formatter.formatAndRoundBigDecimal0(totAzotoUtilAgro));
      tblEffluenti.setAlignment(numRiga, 4, StyleConstants.H_CENTER);
        
      tblEffluenti.setFont(numRiga, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setFont(numRiga, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);
        
      tblEffluenti.setHeaderRowCount(2);
      setNoBorderTotaliEffNew(tblEffluenti);
        
      
      subReport.setElement("tblEffluenti", tblEffluenti);
      
      DefaultTableLens tblNoteEffluenti = new DefaultTableLens(subReport.getTable("tblNoteEffluenti"));
      
      //tblNoteEffluenti.setAlignment(0,0,StyleConstants.V_TOP);
      tblNoteEffluenti.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      
      for (int i=0;i<note.size();i++)
      {
        //Se non sono al primo step devo aggiungere una riga alla tabella
        if (i!=0) 
          tblNoteEffluenti.addRow();
        tblNoteEffluenti.setObject(i, 0, StampeGaaServlet.checkNewLine((String)note.get(i)));
          
      }
      
      StampeGaaServlet.setNoBorder(tblNoteEffluenti);
      
      subReport.setElement("tblNoteEffluenti", tblNoteEffluenti);
      
    }
    else
    {
      // Rimuovo la tabella delle consistenze
      subReport.removeElement("tblEffluenti");
      // Rimuovo le note ed il relativo new line
      subReport.removeElement("tblNoteEffluenti");
      subReport.removeElement("spNoteEffluenti");
      subReport.removeElement("nvlNoteEffluenti");
    }    
    
  }
  
  
  
  private final void setNoBorderTotaliEffNew(DefaultTableLens tblLens)
  {
    int row=tblLens.getRowCount();
    tblLens.setColBorder(row-1, -1, StyleConstants.NO_BORDER);
    tblLens.setColBorder(row-1, 0, StyleConstants.NO_BORDER);
    tblLens.setColBorder(row-1, 1, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 0, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 1, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 2, StyleConstants.NO_BORDER);
  }
}