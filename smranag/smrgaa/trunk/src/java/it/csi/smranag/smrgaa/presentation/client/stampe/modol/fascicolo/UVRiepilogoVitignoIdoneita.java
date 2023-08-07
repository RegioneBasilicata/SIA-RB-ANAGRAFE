package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroUnitaVitate;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RiepiloghiUv;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RiepilogoUv;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RigaRiepilogoUv;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RigheRiepilogoUv;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.terreni.RiepiloghiUnitaArboreaVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class UVRiepilogoVitignoIdoneita extends SubReportModol
{
  
  private final static String CODICE_SUB_REPORT = "UV_RIEP_VITIGNO_IDONEITA";

  public UVRiepilogoVitignoIdoneita() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {    
    if(fascicoDigitale.getQuadroUnitaVitate() == null)
      fascicoDigitale.setQuadroUnitaVitate(new QuadroUnitaVitate());    
    QuadroUnitaVitate quadroUnitaViatate = fascicoDigitale.getQuadroUnitaVitate();
    
    
    if(quadroUnitaViatate.getRiepiloghiUv() == null)
    {
      quadroUnitaViatate.setRiepiloghiUv(new RiepiloghiUv());
      quadroUnitaViatate.getRiepiloghiUv().setVisibility(true);
    }    
    RiepilogoUv riepilogoUv = new RiepilogoUv();
    quadroUnitaViatate.getRiepiloghiUv().getRiepilogoUv().add(riepilogoUv); 
    riepilogoUv.setNomeRiepilogo(richiestaTipoReportVO.getQuadro());
    riepilogoUv.setIntestazione1(richiestaTipoReportVO.getSezione());
    riepilogoUv.setIntestazione2("Idoneità");
    riepilogoUv.setIntestazione3("n. UNAR");
    riepilogoUv.setIntestazione4("Superficie");
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    
    Vector<RiepiloghiUnitaArboreaVO> vRiepiloghi = gaaFacadeClient.getStampaUVRiepilogoVitignoIdoneita(
        anagAziendaVO.getIdAzienda(), idDichiarazioneConsistenza);
    
    if (vRiepiloghi != null)
    {
      riepilogoUv.setRigheRiepilogoUv(new RigheRiepilogoUv());      
    
      // this.setBorderRiquadri(tblTemp);
      for (int i = 0; i < vRiepiloghi.size(); i++)
      {
        RigaRiepilogoUv rigaRiepilogoUv = new RigaRiepilogoUv();
        
        RiepiloghiUnitaArboreaVO riepiloghiUnitaArboreaVO = vRiepiloghi.get(i);
        rigaRiepilogoUv.setValore1(riepiloghiUnitaArboreaVO.getDescVarieta());
        rigaRiepilogoUv.setValore2(riepiloghiUnitaArboreaVO.getTipoTipolgiaVino());
        rigaRiepilogoUv.setValore3(riepiloghiUnitaArboreaVO.getNumElementi().toString());
        rigaRiepilogoUv.setValore4(Formatter.formatDouble4(riepiloghiUnitaArboreaVO.getSupVitata()));
        
        riepilogoUv.getRigheRiepilogoUv().getRigaRiepilogoUv().add(rigaRiepilogoUv);
      }
    }
    else
    {
      riepilogoUv.setSezioneVuota(true);
    }
    
    
    
  }
  
}