package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroTerreni;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Riepiloghi;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Riepilogo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RigaRiepilogo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RigheRiepilogo;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class TerreniRiepilogoComune extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "TERRENI_RIEPILOGO_COMUNE";

  public TerreniRiepilogoComune() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    if(fascicoDigitale.getQuadroTerreni() == null)
      fascicoDigitale.setQuadroTerreni(new QuadroTerreni());
    QuadroTerreni quadroTerreni = fascicoDigitale.getQuadroTerreni();
    
    if(quadroTerreni.getRiepiloghi() == null)
    {
      quadroTerreni.setRiepiloghi(new Riepiloghi());    
      quadroTerreni.getRiepiloghi().setVisibility(true);
    }
    
    Riepilogo riepilogo = new Riepilogo();
    quadroTerreni.getRiepiloghi().getRiepilogo().add(riepilogo); 
    riepilogo.setNomeRiepilogo(richiestaTipoReportVO.getQuadro());
    riepilogo.setIntestazione1(richiestaTipoReportVO.getSezione());
    riepilogo.setIntestazione2("SAU");
    riepilogo.setIntestazione3("Sup. totale");
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Long codiceFotografia = (Long)parametri.get("codiceFotografia");
    
    Vector<StoricoParticellaVO> terreni = gaaFacadeClient
      .getRiepilogoComuneStampa(anagAziendaVO.getIdAzienda(), codiceFotografia);
    
    if(terreni != null)
    {   
      riepilogo.setRigheRiepilogo(new RigheRiepilogo());
      

    
      for(int i=0;i<terreni.size();i++)
      {
        RigaRiepilogo rigaRiepilogo = new RigaRiepilogo();
        StoricoParticellaVO storicoParticellaVO = terreni.get(i);       
        ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO.getElencoConduzioni()[0];
        rigaRiepilogo.setValore1(storicoParticellaVO.getComuneParticellaVO().getDescom());
        rigaRiepilogo.setValore2(StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieAgronomica()));
        rigaRiepilogo.setValore3(StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
        
        riepilogo.getRigheRiepilogo().getRigaRiepilogo().add(rigaRiepilogo);
      }     
    }
    else
    {
      riepilogo.setSezioneVuota(true);
    }    
    
  }
  
  
  
}