package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroTerreni;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Riepiloghi;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Riepilogo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RigaRiepilogo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RigheRiepilogo;
import it.csi.smranag.smrgaa.dto.modol.RiepilogoStampaTerrVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
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

public class TerreniRiepilogoTipoArea extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "TERRENI_RIEP_TIPI_AREA";

  public TerreniRiepilogoTipoArea() throws IOException, SolmrException
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
    
    Vector<RiepilogoStampaTerrVO> vRighe = new Vector<RiepilogoStampaTerrVO>(); 
    
    addVectorRiepiloghi(vRighe, gaaFacadeClient
      .getStampaRiepiloghiStoricoPartTipoArea("A", anagAziendaVO.getIdAzienda().longValue(), 
          codiceFotografia));
    addVectorRiepiloghi(vRighe, gaaFacadeClient
        .getStampaRiepiloghiStoricoPartTipoArea("B", anagAziendaVO.getIdAzienda().longValue(), 
            codiceFotografia));
    addVectorRiepiloghi(vRighe, gaaFacadeClient
        .getStampaRiepiloghiStoricoPartTipoArea("C", anagAziendaVO.getIdAzienda().longValue(), 
            codiceFotografia));
    addVectorRiepiloghi(vRighe, gaaFacadeClient
        .getStampaRiepiloghiStoricoPartTipoArea("D", anagAziendaVO.getIdAzienda().longValue(), 
            codiceFotografia));
    addVectorRiepiloghi(vRighe, gaaFacadeClient
        .getStampaRiepiloghiFoglioTipoArea("E", anagAziendaVO.getIdAzienda().longValue(), 
            codiceFotografia));
    addVectorRiepiloghi(vRighe, gaaFacadeClient
        .getStampaRiepiloghiFoglioTipoArea("F", anagAziendaVO.getIdAzienda().longValue(), 
            codiceFotografia));
    addVectorRiepiloghi(vRighe, gaaFacadeClient
        .getStampaRiepiloghiFoglioTipoArea("PSN", anagAziendaVO.getIdAzienda().longValue(), 
            codiceFotografia));
    addVectorRiepiloghi(vRighe, gaaFacadeClient
        .getStampaRiepiloghiStoricoPartTipoArea("G", anagAziendaVO.getIdAzienda().longValue(), 
            codiceFotografia));
    addVectorRiepiloghi(vRighe, gaaFacadeClient
        .getStampaRiepiloghiStoricoPartTipoArea("H", anagAziendaVO.getIdAzienda().longValue(), 
            codiceFotografia));
    addVectorRiepiloghi(vRighe, gaaFacadeClient
        .getStampaRiepiloghiStoricoPartTipoArea("I", anagAziendaVO.getIdAzienda().longValue(), 
            codiceFotografia));
    addVectorRiepiloghi(vRighe, gaaFacadeClient
        .getStampaRiepiloghiStoricoPartTipoArea("L", anagAziendaVO.getIdAzienda().longValue(), 
            codiceFotografia));
    addVectorRiepiloghi(vRighe, gaaFacadeClient
        .getStampaRiepiloghiStoricoPartTipoArea("M", anagAziendaVO.getIdAzienda().longValue(), 
            codiceFotografia));
    
    
    if(vRighe.size() > 0)
    {   
      riepilogo.setRigheRiepilogo(new RigheRiepilogo());
      
      for(int i=0;i<vRighe.size();i++)
      {
        RigaRiepilogo rigaRiepilogo = new RigaRiepilogo();
        RiepilogoStampaTerrVO riepilogoStampaTerrVO = vRighe.get(i);        
        rigaRiepilogo.setValore1(riepilogoStampaTerrVO.getDescrizione());
        rigaRiepilogo.setValore2(Formatter.formatDouble4(riepilogoStampaTerrVO.getValore1()));
        rigaRiepilogo.setValore3(Formatter.formatDouble4(riepilogoStampaTerrVO.getValore2()));
        
        riepilogo.getRigheRiepilogo().getRigaRiepilogo().add(rigaRiepilogo);
      }     
    }
    else
    {
      riepilogo.setSezioneVuota(true);
    }    
    
  }
  
  
  private void addVectorRiepiloghi(Vector<RiepilogoStampaTerrVO> vRighe, Vector<RiepilogoStampaTerrVO> vRigheQuery)
  {
    if(vRigheQuery != null)
    {
      for(int i=0;i<vRigheQuery.size();i++)
        vRighe.add(vRigheQuery.get(i));
    }
  }
  
  
  
}