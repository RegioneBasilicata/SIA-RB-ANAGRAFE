package it.csi.smranag.smrgaa.presentation.client.stampe.jasper.documento;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.documento.DocAltroParticelle;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.Protocollo;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.SubReportJasper;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.anag.sian.SianAnagTributariaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.Validator;

public class DocAltroParticelleJasper extends SubReportJasper
{
  private final static String CODICE_SUB_REPORT = "DOC_ALTRO_PARTICELLE";

  public DocAltroParticelleJasper() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  
  public void processSubReport(Protocollo protocollo, RichiestaTipoReportVO richiestaTipoReportVO,
      HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    DocumentoVO documentoVO = (DocumentoVO)parametri.get("documentoVO");
    
    DocAltroParticelle docAltroParticelle = new DocAltroParticelle(); 
    
    
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
    
    docAltroParticelle.setCap(StampeGaaServlet.checkNull(cap));
    docAltroParticelle.setCodiceFiscale(StampeGaaServlet.checkNull(codiceFiscale));
    docAltroParticelle.setComune(StampeGaaServlet.checkNull(comune));
    docAltroParticelle.setDataNascita(dataNascita);
    docAltroParticelle.setDenominazione(StampeGaaServlet.checkNull(denominazione)+"  ");
    docAltroParticelle.setIndirizzo(StampeGaaServlet.checkNull(indirizzo));
    docAltroParticelle.setLuogoNascita(StampeGaaServlet.checkNull(luogoNascita));
    docAltroParticelle.setPartitaIva(StampeGaaServlet.checkNull(partitaIva));
    docAltroParticelle.setProv(StampeGaaServlet.checkNull(prov));
    
    if(Validator.isNotEmpty(documentoVO.getDataInizioValidita()))
    {
      docAltroParticelle.setDataInizioValidita(DateUtils.formatDate(documentoVO.getDataInizioValidita()));
    }
    
    if(Validator.isNotEmpty(documentoVO.getDataFineValidita()))
    {
    	docAltroParticelle.setDataFineValidita(DateUtils.formatDate(documentoVO.getDataFineValidita()));
    }
    
    Vector<TipoAttestazioneVO> vAttestazioneVO = gaaFacadeClient.getAttestStampaProtoc(
        richiestaTipoReportVO.getIdReportSubReport().longValue());
    
    if(Validator.isNotEmpty(vAttestazioneVO))
    {
      for(int i=0;i<vAttestazioneVO.size();i++)
      {   
    	  docAltroParticelle.addAttestazione(StampeGaaServlet.checkNull(vAttestazioneVO.get(i).getDescrizione()));
      }
    }
    
    
    int size=0;
    Vector<?> elencoParticelle = documentoVO.getElencoParticelle();
    
    if(elencoParticelle!=null)
    {
      size=elencoParticelle.size();
    }
    
    if (size>0)
    {
      for (int i = 0; i < size; i++)
      {
        StoricoParticellaVO particella= (StoricoParticellaVO) elencoParticelle.get(i);
        docAltroParticelle.addParticella(particella);
      }
    
    }
    protocollo.setDocAltroParticelle(docAltroParticelle);
    
  }

  
}