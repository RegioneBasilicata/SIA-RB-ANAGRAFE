package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.ElencoUTE;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroUte;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.UTE;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class Ute extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "UTE";

  public Ute() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    QuadroUte quadroUte = new QuadroUte();
    fascicoDigitale.setQuadroUte(quadroUte);
    quadroUte.setVisibility(true);
    
    quadroUte.setTitoloUte(richiestaTipoReportVO.getQuadro());
    
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    Vector<UteVO> utes = anagFacadeClient.getUteQuadroB(anagAziendaVO
        .getIdAzienda(), dataInserimentoDichiarazione);
    int num = utes.size();
    if (num > 0)
    {
      quadroUte.setElencoUTE(new ElencoUTE());
      for (int i = 0; i < num; i++)
      {
        UteVO ute = (UteVO) utes.get(i);
        UTE uteModol = new UTE();
        uteModol.setDenominazione(ute.getDenominazione());
        String indirizzo = ute.getIndirizzo();
        indirizzo += " - " +ute.getCap();
        indirizzo += " "+ute.getComune();
        indirizzo += " ("+ute.getProvincia()+")";
        uteModol.setIndirizzo(indirizzo);
        uteModol.setTel(ute.getTelefono());
        
        
        if(Validator.isNotEmpty(ute.getDescAteco()))
        {
          String ateco = "";
          ateco = ute.getDescAteco();
          ateco += " ("+ute.getCodeAteco()+")";
          uteModol.setAteco(ateco);
        }
        
        
        quadroUte.getElencoUTE().getUTE().add(uteModol);
      }

    }
    else
    {
      quadroUte.setSezioneVuota(true);
    }
    
    
    
  }
}