package it.csi.smranag.smrgaa.presentation.client.stampe.documento;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.anag.sian.SianAnagTributariaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class DocAltroParticelle extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/documento/DocAltroParticelle.srt";
  private final static String CODICE_SUB_REPORT = "DOC_ALTRO_PARTICELLE";

  public DocAltroParticelle() throws IOException, SolmrException
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
    long[] idDocumento = new long[1];
    idDocumento[0] = documentoVO.getIdDocumento().longValue();
    String cuaa = (String)parametri.get("cuaa");
    PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)parametri.get("personaFisicaVO");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    
    TipoReportVO tipReportVO = gaaFacadeClient.getTipoReport(new Long(richiestaTipoReportVO.getIdTipoReport()).longValue());
    String denominazione = "";
    String dataNascita = null;
    String luogoNascita = "";
    String indirizzo = "";
    String comune = "";
    String prov = "";
    String cap = "";
    String codiceFiscale = "";
    String partitaIva = "";
    if("N".equalsIgnoreCase(tipReportVO.getFlagSceltaPropietari()))
    {
      SianAnagTributariaVO sianAnagTributariaVO = anagFacadeClient.selectDatiAziendaTributaria(cuaa);
      
      if(cuaa.length() == 11)
      {
        denominazione = sianAnagTributariaVO.getDenominazione();
        indirizzo = sianAnagTributariaVO.getIndirizzoSedeLegale();
        comune = sianAnagTributariaVO.getComuneSedeLegale();
        prov = sianAnagTributariaVO.getProvinciaSedeLegale();
        cap = sianAnagTributariaVO.getCapSedeLegale();
        codiceFiscale = sianAnagTributariaVO.getCodiceFiscale();
        partitaIva = sianAnagTributariaVO.getPartitaIva();
      }
      else
      {
        denominazione = sianAnagTributariaVO.getCognome()+" "+sianAnagTributariaVO.getNome();
        dataNascita = sianAnagTributariaVO.getDataNascita();
        luogoNascita = sianAnagTributariaVO.getComuneNascita();
        indirizzo = sianAnagTributariaVO.getIndirizzoResidenza();
        comune = sianAnagTributariaVO.getComuneResidenza();
        prov = sianAnagTributariaVO.getProvinciaResidenza();
        cap = sianAnagTributariaVO.getCapResidenza();
        codiceFiscale = sianAnagTributariaVO.getCodiceFiscale();
        partitaIva = sianAnagTributariaVO.getPartitaIva();
      }
    }
    else
    {
      denominazione = anagAziendaVO.getDenominazione();
      
      if(anagAziendaVO.getCUAA().length() == 16)
      {
        if(personaFisicaVO.getNascitaData() != null)
        {
          dataNascita = DateUtils.formatDate(personaFisicaVO.getNascitaData());
        }
        
        String txtLuogoDiNascitaSezIIStatoEstero = StampeGaaServlet.checkNull(personaFisicaVO
            .getNascitaStatoEstero());
        String txtLuogoDiNascitaSezIICittaEstero = StampeGaaServlet.checkNull(personaFisicaVO
            .getNascitaCittaEstero());
        if (!"".equals(txtLuogoDiNascitaSezIIStatoEstero)
            || !"".equals(txtLuogoDiNascitaSezIICittaEstero))
        {
          luogoNascita = StampeGaaServlet.checkNull(txtLuogoDiNascitaSezIIStatoEstero
                  + ("".equals(txtLuogoDiNascitaSezIICittaEstero) ? "" : " - "
                      + txtLuogoDiNascitaSezIICittaEstero));
        }
        else
        {
          luogoNascita = StampeGaaServlet.checkNull(personaFisicaVO
              .getDescNascitaComune());
        }
      }
      
      indirizzo =  StampeGaaServlet.checkNull(anagAziendaVO.getSedelegIndirizzo());
      
      
      String txtComuneSezIIStatoEstero = StampeGaaServlet.checkNull(anagAziendaVO
          .getSedelegEstero());
      String txtComuneSezIICittaEstero = StampeGaaServlet.checkNull(anagAziendaVO
          .getSedelegCittaEstero());
      if (!"".equals(txtComuneSezIIStatoEstero)
          || !"".equals(txtComuneSezIICittaEstero))
      {
        comune = StampeGaaServlet.checkNull(txtComuneSezIIStatoEstero
                + ("".equals(txtComuneSezIIStatoEstero) ? "" : " - "
                    + txtComuneSezIICittaEstero));
      }
      else
      {
        comune = StampeGaaServlet.checkNull(anagAziendaVO
            .getDescComune());
        prov = StampeGaaServlet.checkNull(anagAziendaVO
            .getSedelegProv());
        cap = StampeGaaServlet.checkNull(anagAziendaVO
            .getSedelegProv());
      }
      codiceFiscale = anagAziendaVO.getCUAA();
    }
    
    subReport.setElement("txtDenominazione", StampeGaaServlet.checkNull(denominazione)+"  ");
    subReport.setElement("txtDataNascita", dataNascita);
    subReport.setElement("txtLuogoNascita", StampeGaaServlet.checkNull(luogoNascita));
    subReport.setElement("txtIndirizzo", StampeGaaServlet.checkNull(indirizzo));
    subReport.setElement("txtComune", StampeGaaServlet.checkNull(comune));
    subReport.setElement("txtProv", StampeGaaServlet.checkNull(prov));
    subReport.setElement("txtCap", StampeGaaServlet.checkNull(cap));
    subReport.setElement("txtCodiceFiscale", StampeGaaServlet.checkNull(codiceFiscale));
    subReport.setElement("txtPartitaIva", StampeGaaServlet.checkNull(partitaIva));
    
    if(Validator.isNotEmpty(documentoVO.getDataInizioValidita()))
    {
      subReport.setElement("txtDataInizioValidita", DateUtils.formatDate(documentoVO.getDataInizioValidita()));
    }
    
    if(Validator.isNotEmpty(documentoVO.getDataFineValidita()))
    {
      subReport.setElement("txtDataFineValidita", DateUtils.formatDate(documentoVO.getDataFineValidita()));
    }
    
    
    Vector<TipoAttestazioneVO> vAttestazioneVO = gaaFacadeClient.getAttestStampaProtoc(
        richiestaTipoReportVO.getIdReportSubReport().longValue());
    
    if(Validator.isNotEmpty(vAttestazioneVO))
    {
      DefaultTableLens tblAttestazioni = new DefaultTableLens(subReport.getTable("tblAttestazioni"));
      tblAttestazioni.setFont(StampeGaaServlet.FONT_SERIF_10);
      tblAttestazioni.setColWidth(0, 600);
      
      StampeGaaServlet.setNoBorder(tblAttestazioni);     
      
      for(int i=0;i<vAttestazioneVO.size();i++)
      {        
        tblAttestazioni.addRow();
        
        tblAttestazioni.setAlignment(i, 0, StyleConstants.H_LEFT);
        tblAttestazioni.setObject(i, 0, StampeGaaServlet.checkNull(vAttestazioneVO.get(i).getDescrizione()));
      }
      
      
      subReport.setElement("tblAttestazioni", tblAttestazioni);
      
      
    }
    else
    {
      subReport.removeElement("tblAttestazioni");
    }
    
    
    
    
    int size=0;
    Vector<?> elencoParticelle = documentoVO.getElencoParticelle();
    
    if(elencoParticelle!=null)
    {
      size=elencoParticelle.size();
    }
    
    if (size>0)
    {
      DefaultTableLens tblTitoloParticelle = new DefaultTableLens(subReport.getTable("tblTitoloParticelle"));
      tblTitoloParticelle.setFont(0, 0, StampeGaaServlet.FONT_SERIF_10);
      tblTitoloParticelle.setAlignment(0, 0, StyleConstants.H_LEFT);
      tblTitoloParticelle.setColWidth(0, 600);
      tblTitoloParticelle.setObject(0, 0, "le seguenti particelle:");
      tblTitoloParticelle.setAlignment(0, 0, StyleConstants.H_LEFT);
      
      StampeGaaServlet.setNoBorder(tblTitoloParticelle);
      
      subReport.setElement("tblTitoloParticelle", tblTitoloParticelle);
      
      
      
      DefaultTableLens tblTerreni = new DefaultTableLens(subReport.getTable("tblTerreni"));
      
      tblTerreni.setFont(StampeGaaServlet.FONT_SERIF_10);
      
      for (int i=0;i<8;i++)
      {
        tblTerreni.setFont(0, i, StampeGaaServlet.FONT_SERIF_BOLD_8);
        tblTerreni.setAlignment(0, i, StyleConstants.H_CENTER);
        tblTerreni.setFont(1, i, StampeGaaServlet.FONT_SERIF_BOLD_8);
        tblTerreni.setAlignment(1, i, StyleConstants.H_CENTER);
      }      
        
      // Imposto l'header
      int col = 0;
      tblTerreni.setColWidth(col++, 170);//comune
      tblTerreni.setColWidth(col++, 30);//sezione
      tblTerreni.setColWidth(col++, 40);//foglio
      tblTerreni.setColWidth(col++, 40);//particella
      tblTerreni.setColWidth(col++, 30);//subalterno
      tblTerreni.setColWidth(col++, 50);//superficie catastale
      tblTerreni.setColWidth(col++, 40);//% Condotta
      tblTerreni.setColWidth(col++, 160);//documento giustificativo
      
    
      for (int i = 0; i < size; i++)
      {
        BigDecimal temp=new BigDecimal(0);
        tblTerreni.addRow();
        StoricoParticellaVO particella= (StoricoParticellaVO) elencoParticelle.get(i);
        col = 0;
        String descComune = particella.getComuneParticellaVO().getDescom()+" ("
          +particella.getComuneParticellaVO().getSiglaProv()+")";
        tblTerreni.setObject(i + 1, col++, StampeGaaServlet.checkNull(descComune));
        tblTerreni.setObject(i + 1, col++, StampeGaaServlet.checkNull(particella.getSezione()));
        tblTerreni.setAlignment(i + 1, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 1, col++, StampeGaaServlet.checkNull(particella.getFoglio()));
        tblTerreni.setAlignment(i + 1, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 1, col++, StampeGaaServlet.checkNull(particella.getParticella()));
        tblTerreni.setObject(i + 1, col++, StampeGaaServlet.checkNull(particella.getSubalterno()));
        tblTerreni.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
        try
        {
          temp = new BigDecimal(particella.getSupCatastale());
        }
        catch(Exception e)
        {
          temp=new BigDecimal(0);
        }
        tblTerreni.setObject(i + 1, col++, StringUtils.parseSuperficieFieldBigDecimal(temp));
        
        tblTerreni.setAlignment(i + 1, col, StyleConstants.H_CENTER);
        BigDecimal percentualePossessoTmp = particella.getElencoConduzioni()[0].getPercentualePossesso();
        String percentualePossesso = "";
        if(percentualePossessoTmp != null)
        {
          if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
          {
            percentualePossessoTmp = new BigDecimal(1);
          }
          percentualePossesso = Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp);
        }
        tblTerreni.setObject(i + 1, col++, percentualePossesso);
        
        String protocolloRic = "";
        if("S".equalsIgnoreCase(tipReportVO.getFlagSceltaPropietari()))
        {
          if(Validator.isNotEmpty(particella.getElencoConduzioni()[0]
              .getElencoDocumentoConduzione()[0].getNote()))
          {
            protocolloRic = StringUtils.trasfToDBNumeroProtocolloField(particella.getElencoConduzioni()[0]
              .getElencoDocumentoConduzione()[0].getNote().trim());
          }
          
          //Se nn lo trovo nn metto niente!!!
          if(Validator.isNotEmpty(protocolloRic))
          {
            if(!gaaFacadeClient.isNumeroProtocolloValido(protocolloRic))
            {
              protocolloRic = "";
            }
          }
          
          
        }
        
        if(Validator.isNotEmpty(protocolloRic))
        {
          protocolloRic = StringUtils.parseNumeroProtocolloField(protocolloRic);
        }
        
        tblTerreni.setAlignment(i + 1, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 1, col++, protocolloRic);
        
        
      }
      
      if("N".equalsIgnoreCase(tipReportVO.getFlagSceltaPropietari()))
      {
        tblTerreni.removeColumn(7);
      }      
      subReport.setElement("tblTerreni", tblTerreni);
      
      
      if("N".equalsIgnoreCase(tipReportVO.getFlagSceltaPropietari()))
      {
        subReport.removeElement("nlNoteTerreni1");
        subReport.removeElement("txtNoteTerreni");
        subReport.removeElement("nlNoteTerreni2");
      }
      else
      {
        String parametroNoteStampaDoc = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_NOTA_STAMPA_DOC)+"  ";
        
        subReport.setElement("txtNoteTerreni", parametroNoteStampaDoc);
        subReport.getElement("txtNoteTerreni").setAlignment(StyleConstants.H_LEFT);
      }
      
      
      
    }
    else
    {
      subReport.removeElement("nlParticelle");
      subReport.removeElement("tblTitoloParticelle");
      subReport.removeElement("nlParticelle2");
      subReport.removeElement("nlParticelle3");
      subReport.removeElement("txtParticelle");
      subReport.removeElement("tblTerreni");
      subReport.removeElement("nlNoteTerreni1");
      subReport.removeElement("txtNoteTerreni");
      subReport.removeElement("nlNoteTerreni2");
    }
    
    
    
    
    
  }

  
}