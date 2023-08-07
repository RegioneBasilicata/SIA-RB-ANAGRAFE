package it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.BloccoFinale;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Dichiarazione;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Dichiarazioni;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.ElencoDichiarazioni;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class TrattamentoDatiRV extends SubReportModol
{
 
  private final static String CODICE_SUB_REPORT = "TRATTAMENTO_DATI";

  public TrattamentoDatiRV() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
        
    

    Vector<TipoAttestazioneVO> vAttestazioni = null;

    vAttestazioni = gaaFacadeClient.getAttestazioniFromSubReport(richiestaTipoReportVO.getIdReportSubReport(),
       null);

    if (vAttestazioni != null)
    {
      if(fascicoDigitale.getDichiarazioni() == null)
      {
        Dichiarazioni dichiarazioni = new Dichiarazioni();
        fascicoDigitale.setDichiarazioni(dichiarazioni);
        dichiarazioni.setVisibility(true);
      }
      
      fascicoDigitale.getDichiarazioni().setBloccoFinale(new BloccoFinale());
      fascicoDigitale.getDichiarazioni().getBloccoFinale().setVisibility(true);
      
      fascicoDigitale.getDichiarazioni().getBloccoFinale().setElencoDichiarazioni(new ElencoDichiarazioni());
      for (int i = 0; i < vAttestazioni.size(); i++)
      {
        TipoAttestazioneVO tipoAttestazioneVO = vAttestazioni.get(i);
        Dichiarazione dichiarazione = new Dichiarazione();
        dichiarazione.setValoreDich(tipoAttestazioneVO.getDescrizione());
        
        fascicoDigitale.getDichiarazioni().getBloccoFinale()
          .getElencoDichiarazioni().getDichiarazione().add(dichiarazione);
      }
      
    }
    
    
    
    
    
  }
  
}