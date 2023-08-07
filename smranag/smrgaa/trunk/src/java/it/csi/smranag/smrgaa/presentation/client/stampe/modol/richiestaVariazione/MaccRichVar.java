package it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.MacchinaVar;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.MacchineVar;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroVariazioneMotori;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.MacchinaAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class MaccRichVar extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "MACC_RICH_VAR";

  public MaccRichVar() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    QuadroVariazioneMotori quadroVariazioniMotori = new QuadroVariazioneMotori();
    fascicoDigitale.setQuadroVariazioneMotori(quadroVariazioniMotori);
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)parametri.get("aziendaNuovaVO");
    Vector<MacchinaAziendaNuovaVO> vMacchine = gaaFacadeClient.getMacchineAzNuova(aziendaNuovaVO.getIdRichiestaAzienda());
    if(aziendaNuovaVO.getIdTipoRichiesta().compareTo(new Long(SolmrConstants.RICHIESTA_VAR_IRRORATRICI)) != 0)
    {   
      quadroVariazioniMotori.setVisibility(false);
    }
    else
    {
      quadroVariazioniMotori.setVisibility(true);
      quadroVariazioniMotori.setTitoloMacchine(richiestaTipoReportVO.getQuadro());
    }
    
    int num = 0;
    if(vMacchine != null)
      num = vMacchine.size();
    
    if (num > 0)
    {
      quadroVariazioniMotori.setMacchineVar(new MacchineVar());
      for (int i = 0; i < num; i++)
      {
        MacchinaAziendaNuovaVO macchina = vMacchine.get(i);
        MacchinaVar macchinaModol = new MacchinaVar();
        macchinaModol.setGenere(macchina.getDescGenereMacchina());
        macchinaModol.setCategoria(macchina.getDescCategoria());
        if(Validator.isNotEmpty(macchina.getAnnoCostruzione()))
          macchinaModol.setAnno(""+macchina.getAnnoCostruzione());
        macchinaModol.setMarca(macchina.getDescMarca());
        macchinaModol.setTipo(macchina.getTipoMacchina());
        macchinaModol.setTelaio(macchina.getMatricolaTelaio());
        macchinaModol.setFormaPossesso(macchina.getDescTipoFormaPossesso());
        macchinaModol.setPercPossessoMacch(Formatter.formatDouble(macchina.getPercentualePossesso()));
        String variazione = "";
        if(macchina.getIdMacchina() == null)
          variazione = "aggiunta";
        else if((macchina.getIdMacchina() != null)
            && (macchina.getDataScarico() == null))
          variazione = "confermata";
        else if((macchina.getIdMacchina() != null)
            && (macchina.getDataScarico() != null))
          variazione = "scaricata";
        macchinaModol.setVariazione(variazione);
        macchinaModol.setDataCarico(DateUtils.formatDateNotNull(macchina.getDataCarico()));
        
        quadroVariazioniMotori.getMacchineVar().getMacchinaVar().add(macchinaModol);
      }


    }
    else
    {
      quadroVariazioniMotori.setSezioneVuota(true);
    }
    
    
    
  }
}