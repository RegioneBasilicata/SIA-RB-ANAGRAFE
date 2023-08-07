package it.csi.smranag.smrgaa.presentation.client.stampe.documento;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.anag.sian.SianAnagTributariaVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.Validator;
import it.csi.solmr.util.services.DateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class DocSottoscritto extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/documento/DocSottoscritto.srt";
  private final static String CODICE_SUB_REPORT = "DOC_SOTTOSCRITTO";

  public DocSottoscritto() throws IOException, SolmrException
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
    if("S".equalsIgnoreCase(tipReportVO.getFlagSceltaPropietari()))
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
            .getSedelegCAP());
      }
      codiceFiscale = anagAziendaVO.getCUAA();
      partitaIva = anagAziendaVO.getPartitaIVA();
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
    
    Vector<TipoAttestazioneVO> vAttestazioneVO = gaaFacadeClient.getAttestStampaProtoc(
        richiestaTipoReportVO.getIdReportSubReport().longValue());
    
    if(Validator.isNotEmpty(vAttestazioneVO))
    {
      DefaultTableLens tblAttestazioni = new DefaultTableLens(subReport.getTable("tblAttestazioni"));
      tblAttestazioni.setFont(StampeGaaServlet.FONT_SERIF_10);
      tblAttestazioni.setColWidth(0,600);
      
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
    
    
    
    
    
  }

  
}