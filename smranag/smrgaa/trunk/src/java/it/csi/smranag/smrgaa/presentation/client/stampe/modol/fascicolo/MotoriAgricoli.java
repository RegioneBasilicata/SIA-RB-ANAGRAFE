package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Macchina;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Motori;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroMotori;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.uma.PossessoMacchinaVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class MotoriAgricoli extends SubReportModol
{
  
  private final static String CODICE_SUB_REPORT = "MOTORI_AGRICOLI";

  public MotoriAgricoli() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    QuadroMotori quadroMotori = new QuadroMotori();
    fascicoDigitale.setQuadroMotori(quadroMotori);
    quadroMotori.setTitoloMotori(richiestaTipoReportVO.getQuadro());
    quadroMotori.setVisibility(true);
    
    
   
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    
    Vector<PossessoMacchinaVO> vPossessoMacchina = gaaFacadeClient
      .getElencoMacchineAgricoleForStampa(anagAziendaVO.getIdAzienda(), dataInserimentoDichiarazione);
    
    if (Validator.isNotEmpty(vPossessoMacchina))
    {     
      quadroMotori.setMotori(new Motori());

      for (int i=0;i<vPossessoMacchina.size();i++)
      {
        PossessoMacchinaVO possessoMacchina = vPossessoMacchina.get(i);

        Macchina macchina = new Macchina();

        macchina.setGenere(possessoMacchina.getMacchinaVO().getGenereMacchinaVO().getDescrizione());
        if(Validator.isNotEmpty(possessoMacchina.getMacchinaVO().getTipoCategoriaVO()))
          macchina.setCategoria(possessoMacchina.getMacchinaVO().getTipoCategoriaVO().getDescrizione());
        macchina.setMarca(possessoMacchina.getMacchinaVO().getDescMarca());
        macchina.setTipo(possessoMacchina.getMacchinaVO().getModello());         
        if(Validator.isNotEmpty(possessoMacchina.getMacchinaVO().getLastNumeroTargaVO()))
          macchina.setTarga(possessoMacchina.getMacchinaVO().getLastNumeroTargaVO().getNumeroTarga());
                
        quadroMotori.getMotori().getMacchina().add(macchina);
      
      }
      
    }
    else
    {
      quadroMotori.setMotori(null);
      quadroMotori.setSezioneVuota(true);
    }
    
    
    
  }
}