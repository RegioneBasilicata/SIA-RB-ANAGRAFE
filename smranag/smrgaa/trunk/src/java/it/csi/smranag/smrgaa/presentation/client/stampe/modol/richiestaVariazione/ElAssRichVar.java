package it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Associata;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Associate;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroAssociazioni;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AzAssAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.IntermediarioAnagVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class ElAssRichVar extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "EL_ASS_RICH_VAR";

  public ElAssRichVar() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    QuadroAssociazioni quadroAssociazioni = new QuadroAssociazioni();
    fascicoDigitale.setQuadroAssociazioni(quadroAssociazioni);
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)parametri.get("aziendaNuovaVO");
    if(aziendaNuovaVO.getIdTipoRichiesta().compareTo(new Long(SolmrConstants.RICHIESTA_VAR_SOCI)) != 0)
    {   
      quadroAssociazioni.setVisibility(false);
    }
    else
    {
      quadroAssociazioni.setVisibility(true);
      quadroAssociazioni.setTitoloAssociazioni(richiestaTipoReportVO.getQuadro());
    }
    
    int num = 0;
    Vector<AzAssAziendaNuovaVO> vAssAzienda = gaaFacadeClient.getAziendeAssociateAziendaRichVariazione(aziendaNuovaVO.getIdRichiestaAzienda());
    if(vAssAzienda != null)
      num = vAssAzienda.size();    
    
    if (num > 0)
    {
      quadroAssociazioni.setAssociate(new Associate());
      for (int i = 0; i < num; i++)
      {
        AzAssAziendaNuovaVO azAssAziendaNuovaVO = vAssAzienda.get(i);
        Associata associata = new Associata();
        
        //caso intermediario
        if(azAssAziendaNuovaVO.getCodEnte() != null)
        {
          IntermediarioAnagVO intermediario = anagFacadeClient.findIntermediarioVOByCodiceEnte(azAssAziendaNuovaVO.getCodEnte());
          azAssAziendaNuovaVO.setCuaa(intermediario.getExtCuaa());
          azAssAziendaNuovaVO.setPartitaIva(intermediario.getPartitaIva());
          azAssAziendaNuovaVO.setDenominazione(intermediario.getDenominazione());
          azAssAziendaNuovaVO.setDesCom(intermediario.getDesCom());
          azAssAziendaNuovaVO.setSglProv(intermediario.getSglProv());
          azAssAziendaNuovaVO.setIndirizzo(intermediario.getIndirizzo());
          azAssAziendaNuovaVO.setCap(intermediario.getCap());
        }
        
        associata.setCuaa(azAssAziendaNuovaVO.getCuaa());
        associata.setPartitaIva(azAssAziendaNuovaVO.getPartitaIva());
        associata.setDenominazione(azAssAziendaNuovaVO.getDenominazione());
        String comune = azAssAziendaNuovaVO.getDesCom()+" ("+azAssAziendaNuovaVO.getSglProv()+")";
        associata.setComune(comune);
        associata.setIndirizzo(azAssAziendaNuovaVO.getIndirizzo());
        associata.setCAP(azAssAziendaNuovaVO.getCap());
        
        
        associata.setDataIngresso(DateUtils.formatDateNotNull(azAssAziendaNuovaVO.getDataIngresso()));
        associata.setDataUscita(DateUtils.formatDateNotNull(azAssAziendaNuovaVO.getDataUscita()));
        
        String variazione = "";
        if(azAssAziendaNuovaVO.getIdAziendaCollegata() == null)
        {
          variazione = "aggiunta";
        }
        else
        {
          if("S".equalsIgnoreCase(azAssAziendaNuovaVO.getFlagEliminato()))
          {
            variazione = "cancellata";
          }
          else if(azAssAziendaNuovaVO.getDataUscita() != null)
          {
            variazione = "uscita";
          }
          else if(azAssAziendaNuovaVO.getDataUscita() == null)
          {
            variazione = "confermato";
          }
        }
        
        
        associata.setVariazione(variazione);
        
        quadroAssociazioni.getAssociate().getAssociata().add(associata);
      }
    }
    else
    {
      quadroAssociazioni.setSezioneVuota(true);
    }
    
    
    
  }
}