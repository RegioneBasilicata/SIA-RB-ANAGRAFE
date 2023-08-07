package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO;
import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AllevamentoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AzAssAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.CCAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.FabbricatoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.IterRichiestaAziendaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.MacchinaAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.MotivoRichiestaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.ParticellaAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.RichiestaAziendaDocumentoVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.RichiestaAziendaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.SoggettoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.StatoRichiestaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.UnitaMisuraVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.UteAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.uma.MacchinaGaaVO;
import it.csi.smranag.smrgaa.dto.uma.PossessoMacchinaVO;
import it.csi.smranag.smrgaa.integration.DocumentoGaaDAO;
import it.csi.smranag.smrgaa.integration.MacchineAgricoleGaaDAO;
import it.csi.smranag.smrgaa.integration.NuovaIscrizioneDAO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.anag.AnagrafeDAO;
import it.csi.solmr.integration.anag.DelegaDAO;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name= NuovaIscrizioneGaaBean.jndiName,mappedName= NuovaIscrizioneGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED )
public class NuovaIscrizioneGaaBean implements NuovaIscrizioneGaaLocal {

	/**
   * 
   */
  private static final long serialVersionUID = 3703093722323238683L;
  public final static String jndiName="comp/env/solmr/gaa/NuovaIscrizione";
  
  SessionContext sessionContext;
	private NuovaIscrizioneDAO nuovaIscrizioneDAO = null;
	private DocumentoGaaDAO documentoGaaDAO = null;
	private AnagrafeDAO aDAO = null;
	private DelegaDAO delegaDAO = null;
	private MacchineAgricoleGaaDAO macchinaDAO = null;
	

	
	@Resource  
	public void setSessionContext(SessionContext sessionContext) throws EJBException
	{
	    SolmrLogger.debug(this, "[NuovaIscrizioneGaaBean::setSessionContext] BEGIN.");
	    this.sessionContext = sessionContext;
	    initializeDAO();
	    SolmrLogger.debug(this, "[NuovaIscrizioneGaaBean::setSessionContext] END.");
	}


	public void initializeDAO() throws EJBException
	{
		SolmrLogger.debug(this, "[NuovaIscrizioneGaaBean::initializeDAO] BEGIN.");
		try
		{
		  nuovaIscrizioneDAO = new NuovaIscrizioneDAO();
		  documentoGaaDAO = new DocumentoGaaDAO();
		  aDAO = new AnagrafeDAO();
		  delegaDAO = new DelegaDAO();
		  macchinaDAO = new MacchineAgricoleGaaDAO();
		}
		catch (Exception e)
		{
		  SolmrLogger.fatal(this, "[NuovaIscrizioneGaaBean::initializeDAO] Eccezione nella creazione del DAO AbioDAO. Eccezione: "
		      + LoggerUtils.getStackTrace(e));
		}
	
		SolmrLogger.debug(this, "[NuovaIscrizioneGaaBean::initializeDAO] END.");
	}
	  
	public AziendaNuovaVO getAziendaNuovaIscrizione(String cuaa, long[] arrTipoRichiesta)	    
	    throws SolmrException
	{
	  try
    {
      return nuovaIscrizioneDAO.getAziendaNuovaIscrizione(cuaa, arrTipoRichiesta);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
	}
	
	public AziendaNuovaVO getAziendaNuovaIscrizioneEnte(String codEnte, long[] arrTipoRichiesta)      
      throws SolmrException
  {
    try
    {
      return nuovaIscrizioneDAO.getAziendaNuovaIscrizioneEnte(codEnte, arrTipoRichiesta);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public AziendaNuovaVO getAziendaNuovaIscrizioneByPrimaryKey(Long idAziendaNuova)     
      throws SolmrException
  {
    try
    {
      return nuovaIscrizioneDAO.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Long insertAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento)     
      throws SolmrException
  {
    try
    {
      Long idAziendaNuova = nuovaIscrizioneDAO.insertAziendaNuova(aziendaNuovaVO);
      if("S".equals(aziendaNuovaVO.getSedeLegUte()))
      {
        UteAziendaNuovaVO uteAziendaNuovaVO = new UteAziendaNuovaVO();
        uteAziendaNuovaVO.setDenominazione("Sede legale");
        uteAziendaNuovaVO.setCap(aziendaNuovaVO.getSedelegCap());
        uteAziendaNuovaVO.setIstatComune(aziendaNuovaVO.getSedelegComune());
        uteAziendaNuovaVO.setIdAziendaNuova(idAziendaNuova);
        uteAziendaNuovaVO.setIndirizzo(aziendaNuovaVO.getSedelegIndirizzo());
        uteAziendaNuovaVO.setTelefono(aziendaNuovaVO.getTelefono());
        uteAziendaNuovaVO.setFax(aziendaNuovaVO.getFax());
        
        nuovaIscrizioneDAO.insertUteAziendaNuova(uteAziendaNuovaVO);
      }
      else
      {
        //Sono privato
        if(aziendaNuovaVO.getCuaa().length() == 16)
        {
          UteAziendaNuovaVO uteAziendaNuovaVO = new UteAziendaNuovaVO();
          uteAziendaNuovaVO.setDenominazione("Sede legale");
          uteAziendaNuovaVO.setCap(aziendaNuovaVO.getResCap());
          uteAziendaNuovaVO.setIstatComune(aziendaNuovaVO.getResComune());
          uteAziendaNuovaVO.setIdAziendaNuova(idAziendaNuova);
          uteAziendaNuovaVO.setIndirizzo(aziendaNuovaVO.getResIndirizzo());
          
          nuovaIscrizioneDAO.insertUteAziendaNuova(uteAziendaNuovaVO);         
        }
        
      }
      
      SoggettoAziendaNuovaVO soggettoAziendaNuovaVO = new SoggettoAziendaNuovaVO();
      soggettoAziendaNuovaVO.setIdAziendaNuova(idAziendaNuova);
      soggettoAziendaNuovaVO.setCognome(aziendaNuovaVO.getCognome());
      soggettoAziendaNuovaVO.setNome(aziendaNuovaVO.getNome());
      soggettoAziendaNuovaVO.setIstatComune(aziendaNuovaVO.getResComune());
      soggettoAziendaNuovaVO.setCap(aziendaNuovaVO.getResCap());
      soggettoAziendaNuovaVO.setIndirizzo(aziendaNuovaVO.getResIndirizzo());
      soggettoAziendaNuovaVO.setTelefono(aziendaNuovaVO.getTelefonoSoggetto());
      soggettoAziendaNuovaVO.setEmail(aziendaNuovaVO.getMailSoggetto());
      soggettoAziendaNuovaVO.setIdRuolo(new Integer(1));
      soggettoAziendaNuovaVO.setDataInizioRuolo(new Date());
      //Sono privato
      if(aziendaNuovaVO.getCuaa().length() == 16)
      {
        soggettoAziendaNuovaVO.setCodiceFiscale(aziendaNuovaVO.getCuaa());
      }
      else
      {
        soggettoAziendaNuovaVO.setCodiceFiscale(aziendaNuovaVO.getCodiceFiscale());
      }
      nuovaIscrizioneDAO.insertSoggettoAziendaNuova(soggettoAziendaNuovaVO);
      
      RichiestaAziendaVO richiestaAziendaVO = new RichiestaAziendaVO();
      richiestaAziendaVO.setIdTipoRichiesta(aziendaNuovaVO.getIdTipoRichiesta());
      richiestaAziendaVO.setIdMotivoRichiesta(aziendaNuovaVO.getIdMotivoRichiesta());
      richiestaAziendaVO.setIdAziendaNuova(idAziendaNuova);
      //fisso regione
      richiestaAziendaVO.setCodiceEnte("17");
      richiestaAziendaVO.setDataAggiornamento(new Date());
      richiestaAziendaVO.setIdUtenteAggiornamento(new Long(idUtenteAggiornamento));      
      Long idRichiestaAzienda = nuovaIscrizioneDAO.insertRichiestaAzienda(richiestaAziendaVO);
      
      IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
      iterRichiestaAziendaVO.setIdRichiestaAzienda(idRichiestaAzienda);
      iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
      iterRichiestaAziendaVO.setDataInizioValidita(new Date());
      iterRichiestaAziendaVO.setDataAggiornamento(new Date());
      iterRichiestaAziendaVO.setIdUtenteAggiornamento(new Long(idUtenteAggiornamento));
      String note = null;
      if(SolmrConstants.RICHIESTA_NI_AZIENDA_OBSOLETA == aziendaNuovaVO.getIdTipoRichiesta().longValue())
      {
        AnagAziendaVO anagAziendaVO = new AnagAziendaVO();
        anagAziendaVO.setCUAA(aziendaNuovaVO.getCuaa());
        try
        {
          Vector<Long> vectIdAnagAzienda = aDAO.getListIdAziende(anagAziendaVO, null, false, false, false);
          if(Validator.isNotEmpty(vectIdAnagAzienda) && (vectIdAnagAzienda.size() > 0))
          {
            if(vectIdAnagAzienda.size() == 1)
            {
              AnagAziendaVO anagAziendaVODelega = aDAO.findByPrimaryKey(vectIdAnagAzienda.get(0), false, null);
              if(aDAO.controllaPresenzaDelega(anagAziendaVODelega))
              {
                note = "Attenzione: azienda con mandato attivo, alla validazione della richiesta il sistema provvederà ad effettuare la revoca del mandato";
              }
            }
          }
        }
        //se va in eccezione vuol dire ch enn ha trovato nulla oppure troppi record quindi giusto nn fare nulla
        catch(Exception ex)
        {}
      }
      iterRichiestaAziendaVO.setNote(note);
      nuovaIscrizioneDAO.insertIterRichiestaAzienda(iterRichiestaAziendaVO);
      
      return idAziendaNuova;
      
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	
	public Long insertAziendaNuovaRichiestaValCess(RichiestaAziendaVO richiestaAziendaVO)     
      throws SolmrException
  {
    try
    {
      
      //fisso regione
      richiestaAziendaVO.setCodiceEnte("17");
      richiestaAziendaVO.setDataAggiornamento(new Date());
      Long idRichiestaAzienda = nuovaIscrizioneDAO.insertRichiestaAzienda(richiestaAziendaVO);
      
      IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
      iterRichiestaAziendaVO.setIdRichiestaAzienda(idRichiestaAzienda);
      iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
      iterRichiestaAziendaVO.setDataInizioValidita(new Date());
      iterRichiestaAziendaVO.setDataAggiornamento(new Date());
      iterRichiestaAziendaVO.setIdUtenteAggiornamento(richiestaAziendaVO.getIdUtenteAggiornamento());
      nuovaIscrizioneDAO.insertIterRichiestaAzienda(iterRichiestaAziendaVO);
      
      return idRichiestaAzienda;
      
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Long insertAziendaNuovaRichiestaVariazione(RichiestaAziendaVO richiestaAziendaVO)     
      throws SolmrException
  {
    try
    {
      
     
      DelegaVO delegaVO =  delegaDAO.getDelegaByAziendaAndIdProcedimento(richiestaAziendaVO.getIdAzienda(),
          new Long(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE));
      if(delegaVO != null)
      {
        richiestaAziendaVO.setCodiceEnte(delegaVO.getCodiceFiscaleIntermediario());
      }
      else
      {     
        richiestaAziendaVO.setCodiceEnte("17");
      }
      richiestaAziendaVO.setDataAggiornamento(new Date());
      Long idRichiestaAzienda = nuovaIscrizioneDAO.insertRichiestaAzienda(richiestaAziendaVO);
      
      IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
      iterRichiestaAziendaVO.setIdRichiestaAzienda(idRichiestaAzienda);
      iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
      iterRichiestaAziendaVO.setDataInizioValidita(new Date());
      iterRichiestaAziendaVO.setDataAggiornamento(new Date());
      iterRichiestaAziendaVO.setIdUtenteAggiornamento(richiestaAziendaVO.getIdUtenteAggiornamento());
      nuovaIscrizioneDAO.insertIterRichiestaAzienda(iterRichiestaAziendaVO);
      
      return idRichiestaAzienda;
      
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void updateAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento)     
      throws SolmrException
  {
    try
    {
      nuovaIscrizioneDAO.updateAziendaNuova(aziendaNuovaVO);
      nuovaIscrizioneDAO.updateIdMotivoRichiesta(aziendaNuovaVO.getIdRichiestaAzienda(), 
          aziendaNuovaVO.getIdMotivoRichiesta());
      Vector<UteAziendaNuovaVO> vUteAziendaNuova = getUteAziendaNuovaIscrizione(aziendaNuovaVO.getIdAziendaNuova());
      if("S".equals(aziendaNuovaVO.getSedeLegUte())
          && Validator.isEmpty(vUteAziendaNuova))
      {
        UteAziendaNuovaVO uteAziendaNuovaVO = new UteAziendaNuovaVO();
        uteAziendaNuovaVO.setDenominazione("Sede legale");
        uteAziendaNuovaVO.setCap(aziendaNuovaVO.getSedelegCap());
        uteAziendaNuovaVO.setIstatComune(aziendaNuovaVO.getSedelegComune());
        uteAziendaNuovaVO.setIdAziendaNuova(aziendaNuovaVO.getIdAziendaNuova());
        uteAziendaNuovaVO.setIndirizzo(aziendaNuovaVO.getSedelegIndirizzo());
        uteAziendaNuovaVO.setTelefono(aziendaNuovaVO.getTelefono());
        uteAziendaNuovaVO.setFax(aziendaNuovaVO.getFax());
        
        nuovaIscrizioneDAO.insertUteAziendaNuova(uteAziendaNuovaVO);
      }
      else
      {
        //Sono privato
        if((aziendaNuovaVO.getCuaa().length() == 16)
            && Validator.isEmpty(vUteAziendaNuova))
        {          
          UteAziendaNuovaVO uteAziendaNuovaVO = new UteAziendaNuovaVO();
          uteAziendaNuovaVO.setDenominazione("Sede legale");
          uteAziendaNuovaVO.setCap(aziendaNuovaVO.getResCap());
          uteAziendaNuovaVO.setIstatComune(aziendaNuovaVO.getResComune());
          uteAziendaNuovaVO.setIdAziendaNuova(aziendaNuovaVO.getIdAziendaNuova());
          uteAziendaNuovaVO.setIndirizzo(aziendaNuovaVO.getResIndirizzo());
          
          nuovaIscrizioneDAO.insertUteAziendaNuova(uteAziendaNuovaVO);
        }       
        
      }
      
      //controllo soggetti..
      SoggettoAziendaNuovaVO soggettoAziendaNuova = nuovaIscrizioneDAO
          .getRappLegaleNuovaIscrizione(aziendaNuovaVO.getIdAziendaNuova());
      if(Validator.isNotEmpty(soggettoAziendaNuova))
      {
        nuovaIscrizioneDAO.deleteSoggettoFromId(soggettoAziendaNuova.getIdSoggettiAziendaNuova());
      }
      //inserisco il rapplegale
      SoggettoAziendaNuovaVO soggettoAziendaNuovaVO = new SoggettoAziendaNuovaVO();
      soggettoAziendaNuovaVO.setIdAziendaNuova(aziendaNuovaVO.getIdAziendaNuova());
      soggettoAziendaNuovaVO.setCognome(aziendaNuovaVO.getCognome());
      soggettoAziendaNuovaVO.setNome(aziendaNuovaVO.getNome());
      soggettoAziendaNuovaVO.setIstatComune(aziendaNuovaVO.getResComune());
      soggettoAziendaNuovaVO.setCap(aziendaNuovaVO.getResCap());
      soggettoAziendaNuovaVO.setIndirizzo(aziendaNuovaVO.getResIndirizzo());
      soggettoAziendaNuovaVO.setTelefono(aziendaNuovaVO.getTelefonoSoggetto());
      soggettoAziendaNuovaVO.setEmail(aziendaNuovaVO.getMailSoggetto());
      soggettoAziendaNuovaVO.setIdRuolo(new Integer(1));
      soggettoAziendaNuovaVO.setDataInizioRuolo(new Date());
      //Sono privato
      if(aziendaNuovaVO.getCuaa().length() == 16)
      {
        soggettoAziendaNuovaVO.setCodiceFiscale(aziendaNuovaVO.getCuaa());
      }
      else
      {
        soggettoAziendaNuovaVO.setCodiceFiscale(aziendaNuovaVO.getCodiceFiscale());
      }
      nuovaIscrizioneDAO.insertSoggettoAziendaNuova(soggettoAziendaNuovaVO);
      
      if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
      {
        IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
        iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
        aggiornaStatoNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, iterRichiestaAziendaVO);       
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<UteAziendaNuovaVO> getUteAziendaNuovaIscrizione(
      long idAziendaNuova)  throws SolmrException
  {
    try
    {
      return nuovaIscrizioneDAO.getUteAziendaNuovaIscrizione(idAziendaNuova);  
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	
	public void aggiornaUteAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento, Vector<UteAziendaNuovaVO> vUteAziendaNuova)     
      throws SolmrException
  {
    try
    {
      //cancello tutte quelle senza legami!!
      nuovaIscrizioneDAO.deleteUteAziendaNuova(vUteAziendaNuova.get(0).getIdAziendaNuova().longValue());
      for(int i=0;i<vUteAziendaNuova.size();i++)
      {
        if(Validator.isNotEmpty(vUteAziendaNuova.get(i).getIdUteAziendaNuova()) 
          && (nuovaIscrizioneDAO.getUteAziendaNuovaByPrimariKey(
            vUteAziendaNuova.get(i).getIdUteAziendaNuova()) != null))
        {
          nuovaIscrizioneDAO.updateUteAziendaNuova(vUteAziendaNuova.get(i));
        }
        else
        {
          nuovaIscrizioneDAO.insertUteAziendaNuova(vUteAziendaNuova.get(i));          
        }
        
      }
      
      if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
      {
        IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
        iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
        aggiornaStatoNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, iterRichiestaAziendaVO);       
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<FabbricatoAziendaNuovaVO> getFabbrAziendaNuovaIscrizione(long idAziendaNuova)     
      throws SolmrException
  {
    try
    {      
      return nuovaIscrizioneDAO.getFabbrAziendaNuovaIscrizione(idAziendaNuova);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void aggiornaFabbrAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
	  Vector<FabbricatoAziendaNuovaVO> vFabbrAziendaNuova)     
      throws SolmrException
  {
    try
    {
      //delete prendo il primo tantoil valore è per tutti uguale
      nuovaIscrizioneDAO.deletePartFabbrAziendaNuova(aziendaNuovaVO.getIdAziendaNuova());
      nuovaIscrizioneDAO.deleteFabbrAziendaNuova(aziendaNuovaVO.getIdAziendaNuova());
      
      if(Validator.isNotEmpty(vFabbrAziendaNuova))
      {
        for(int i=0;i<vFabbrAziendaNuova.size();i++)
        {
          Long idFabbrAziendaNuova = nuovaIscrizioneDAO.insertFabbrAziendaNuova(vFabbrAziendaNuova.get(i));
          if(vFabbrAziendaNuova.get(i).getvPartFabbrAziendaNuova() != null)
          {
            for(int j=0;j<vFabbrAziendaNuova.get(i).getvPartFabbrAziendaNuova().size();j++)
            {
              vFabbrAziendaNuova.get(i).getvPartFabbrAziendaNuova().get(j).setIdFabbricatoAziendaNuova(idFabbrAziendaNuova);
              nuovaIscrizioneDAO.insertPartFabbrAziendaNuova(vFabbrAziendaNuova.get(i).getvPartFabbrAziendaNuova().get(j));
            }
          }
        }
      }
      
      if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
      {
        IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
        iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
        aggiornaStatoNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, iterRichiestaAziendaVO);       
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public boolean existsDependenciesUte(long idUteAziendaNuova)      
      throws SolmrException
  {
    try
    {      
      return nuovaIscrizioneDAO.existsDependenciesUte(idUteAziendaNuova);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<ParticellaAziendaNuovaVO> getParticelleAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException
  {
    try
    {      
      return nuovaIscrizioneDAO.getParticelleAziendaNuovaIscrizione(idAziendaNuova);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<UnitaMisuraVO> getListUnitaMisura()
	   throws SolmrException
  {
    try
    {      
      return nuovaIscrizioneDAO.getListUnitaMisura();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void aggiornaParticelleAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento, 
	  Vector<ParticellaAziendaNuovaVO> vParticelleAziendaNuova)     
      throws SolmrException
  {
    try
    {
      //cancello tutte quelle senza legami!!
      nuovaIscrizioneDAO.deleteParticelleAziendaNuova(aziendaNuovaVO.getIdAziendaNuova());
      if(Validator.isNotEmpty(vParticelleAziendaNuova))
      {
        for(int i=0;i<vParticelleAziendaNuova.size();i++)
        {
          nuovaIscrizioneDAO.insertParticellaAziendaNuova(vParticelleAziendaNuova.get(i));                  
        }
      }
      
      if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
      {
        IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
        iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
        aggiornaStatoNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, iterRichiestaAziendaVO);       
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<AllevamentoAziendaNuovaVO> getAllevamentiAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException
  {
    try
    {      
      return nuovaIscrizioneDAO.getAllevamentiAziendaNuovaIscrizione(idAziendaNuova);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void aggiornaAllevamentiAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
	    Vector<AllevamentoAziendaNuovaVO> vAllevamentiAziendaNuova)     
	      throws SolmrException
  {
    try
    {
      nuovaIscrizioneDAO.deleteAllevamentiAziendaNuova(aziendaNuovaVO.getIdAziendaNuova());
      
      if(Validator.isNotEmpty(vAllevamentiAziendaNuova))
      {
        for(int i=0;i<vAllevamentiAziendaNuova.size();i++)
        {
          nuovaIscrizioneDAO.insertAllevamentoAziendaNuova(vAllevamentiAziendaNuova.get(i));               
        }
      }
      
      
      if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
      {
        IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
        iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
        aggiornaStatoNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, iterRichiestaAziendaVO);       
      }
      
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<CCAziendaNuovaVO> getCCAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException
  {
    try
    {      
      return nuovaIscrizioneDAO.getCCAziendaNuovaIscrizione(idAziendaNuova);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void aggiornaCCAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<CCAziendaNuovaVO> vCCAziendaNuova)     
        throws SolmrException
  {
    try
    {
      nuovaIscrizioneDAO.deleteCCAziendaNuova(aziendaNuovaVO.getIdAziendaNuova());
      
      if(Validator.isNotEmpty(vCCAziendaNuova))
      {
        for(int i=0;i<vCCAziendaNuova.size();i++)
        {
          nuovaIscrizioneDAO.insertCCAziendaNuova(vCCAziendaNuova.get(i));               
        }
      }
      
      if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
      {
        IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
        iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
        aggiornaStatoNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, iterRichiestaAziendaVO);       
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<RichiestaAziendaDocumentoVO> getAllegatiAziendaNuovaIscrizione(
      long idAziendaNuova, long idTipoRichiesta) throws SolmrException
  {
    try
    { 
      Vector<RichiestaAziendaDocumentoVO> vRichiestaziendaDocumento = nuovaIscrizioneDAO.getAllegatiAziendaNuovaIscrizione(idAziendaNuova, idTipoRichiesta);
      
      if(vRichiestaziendaDocumento != null)
      {
        for(int i=0;i<vRichiestaziendaDocumento.size();i++)
        {
          vRichiestaziendaDocumento.get(i).setvAllegatoDocumento(
            documentoGaaDAO.getElencoFileAllegatiRichiesta(vRichiestaziendaDocumento.get(i)
                .getIdRichiestaAziendaDocumento().longValue()));
        }
      }
      
      return vRichiestaziendaDocumento;
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Long insertRichAzDocAziendaNuova(RichiestaAziendaDocumentoVO richiestaAziendaDocumentoVO)
	    throws SolmrException
  {
    try
    {      
      return nuovaIscrizioneDAO.insertRichAzDocAziendaNuova(richiestaAziendaDocumentoVO);
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void deleteDocumentoRichiesta(long idRichiestaDocumento) 
      throws SolmrException
  {
    try
    {
      Vector<AllegatoDocumentoVO> vAllegati = documentoGaaDAO
          .getElencoFileAllegatiRichiesta(idRichiestaDocumento);
      if(Validator.isNotEmpty(vAllegati))
      {
        documentoGaaDAO.deleteAllegatiDocumentoRichiesta(idRichiestaDocumento);     
      }      
      nuovaIscrizioneDAO.deleteRichAzDocAziendaNuova(idRichiestaDocumento);
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }    
  }
	
	public void insertFileStampa(long idRichiestaAzienda,  byte ba[])
	    throws SolmrException
  {
    try
    {      
      nuovaIscrizioneDAO.updateFileStampa(idRichiestaAzienda);
      nuovaIscrizioneDAO.insertFileStampa(idRichiestaAzienda, ba);
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  } 
	
	
	public void aggiornaStatoNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, 
	    long idUtenteAggiornamento, IterRichiestaAziendaVO iterRichiestaAziendaVO)     
      throws SolmrException
  {
    try
    {      
      
      IterRichiestaAziendaVO iterRichiestaAziendaVODB = nuovaIscrizioneDAO
          .getIterRichiestaAziendaByPrimaryKey(aziendaNuovaVO.getIdIterRichiestaAzienda().longValue());
      if(Validator.isEmpty(iterRichiestaAziendaVO.getNote()))
      {
        iterRichiestaAziendaVO.setNote(iterRichiestaAziendaVODB.getNote());
      }
      
      nuovaIscrizioneDAO.storicizzaIterRichiestaAzienda(
          aziendaNuovaVO.getIdIterRichiestaAzienda().longValue());
      
      iterRichiestaAziendaVO.setIdRichiestaAzienda(aziendaNuovaVO.getIdRichiestaAzienda());
      //iterRichiestaAziendaVO.setIdStatoRichiesta(idStatoRichiesta);
      iterRichiestaAziendaVO.setDataInizioValidita(new Date());
      iterRichiestaAziendaVO.setDataAggiornamento(new Date());
      iterRichiestaAziendaVO.setIdUtenteAggiornamento(new Long(idUtenteAggiornamento));
      nuovaIscrizioneDAO.insertIterRichiestaAzienda(iterRichiestaAziendaVO);
      
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	
	public void aggiornaStatoRichiestaValCess(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, IterRichiestaAziendaVO iterRichiestaAziendaVO)     
      throws SolmrException
  {
    try
    {      
      nuovaIscrizioneDAO.storicizzaIterRichiestaAzienda(
          aziendaNuovaVO.getIdIterRichiestaAzienda().longValue());
      
      iterRichiestaAziendaVO.setIdRichiestaAzienda(aziendaNuovaVO.getIdRichiestaAzienda());
      //iterRichiestaAziendaVO.setIdStatoRichiesta(idStatoRichiesta);
      iterRichiestaAziendaVO.setDataInizioValidita(new Date());
      iterRichiestaAziendaVO.setDataAggiornamento(new Date());
      iterRichiestaAziendaVO.setIdUtenteAggiornamento(new Long(idUtenteAggiornamento));
      nuovaIscrizioneDAO.insertIterRichiestaAzienda(iterRichiestaAziendaVO);      
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<Long> getElencoIdRichiestaAzienda(
      Long idTipoRichiesta, Long idStatoRichiesta, String cuaa, String partitaIva, String denominazione,
      Long idAzienda, RuoloUtenza ruoloUtenza) throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getElencoIdRichiestaAzienda(idTipoRichiesta, idStatoRichiesta, cuaa,
          partitaIva, denominazione, idAzienda, ruoloUtenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<Long> getElencoIdRichiestaAziendaGestCaa(
      Long idTipoRichiesta, Long idStatoRichiesta, String cuaa, String partitaIva, String denominazione, 
      RuoloUtenza ruoloUtenza) throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getElencoIdRichiestaAziendaGestCaa(idTipoRichiesta, 
          idStatoRichiesta, cuaa, partitaIva, denominazione, ruoloUtenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<Long> getElencoRichieseteAziendaByIdRichiestaAzienda(long idAzienda, String codiceRuolo) 
	    throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getElencoRichieseteAziendaByIdRichiestaAzienda(idAzienda, codiceRuolo);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<AziendaNuovaVO> getElencoAziendaNuovaByIdRichiestaAzienda(
      Vector<Long> vIdRichiestaAzienda) throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getElencoAziendaNuovaByIdRichiestaAzienda(vIdRichiestaAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<CodeDescription> getListTipoRichiesta() throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getListTipoRichiesta();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<CodeDescription> getListTipoRichiestaVariazione(String codiceRuolo) throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getListTipoRichiestaVariazione(codiceRuolo);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<StatoRichiestaVO> getListStatoRichiesta() throws SolmrException
	{
    try
    {       
      return nuovaIscrizioneDAO.getListStatoRichiesta();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public RichiestaAziendaVO getPdfAziendaNuova(
      long idRichiestaAzienda) throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getPdfAziendaNuova(idRichiestaAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public PlSqlCodeDescription ribaltaAziendaPlSql(long idRichiestaAzienda) 
    throws SolmrException
  {
    
    PlSqlCodeDescription plSqlObl = null;    
    try
    {
      plSqlObl = nuovaIscrizioneDAO.ribaltaAziendaPlSql(idRichiestaAzienda);
      //Ce stato un errore nel plsql senza generare eccezioni
      if(Validator.isNotEmpty(plSqlObl.getDescription())
          && !plSqlObl.getDescription().equalsIgnoreCase("0"))
      {
        sessionContext.setRollbackOnly();
      }
      
      return plSqlObl;
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
    
  }
	
	public boolean isPartitaIvaPresente(
      String partitaIva, long[] arrTipoRichiesta) throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.isPartitaIvaPresente(partitaIva, arrTipoRichiesta);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void updateFlagDichiarazioneAllegati(long idRichiestaAzienda, 
	    String flagDichiarazioneAllegati) throws SolmrException
  {
    try
    {       
      nuovaIscrizioneDAO.updateFlagDichiarazioneAllegati(idRichiestaAzienda, 
          flagDichiarazioneAllegati);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<MotivoRichiestaVO> getListMotivoRichiesta(int idTipoRichiesta)
	  throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getListMotivoRichiesta(idTipoRichiesta);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void updateRichiestaAziendaIndex(long idRichiestaAzienda, long idDocumentoIndex)
	    throws SolmrException
  {
    try
    {       
      nuovaIscrizioneDAO.updateRichiestaAziendaIndex(idRichiestaAzienda, idDocumentoIndex);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public AziendaNuovaVO getRichAzByIdAzienda(long idAzienda, long idTipoRichiesta)
     throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getRichAzByIdAzienda(idAzienda, idTipoRichiesta);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public AziendaNuovaVO getRichAzByIdAziendaConValida(
      long idAzienda, long idTipoRichiesta) throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getRichAzByIdAziendaConValida(idAzienda, idTipoRichiesta);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void updateRichiestaAzienda(RichiestaAziendaVO richiestaAziendaVO) 
	    throws SolmrException
  {
    try
    {       
      nuovaIscrizioneDAO.updateRichiestaAzienda(richiestaAziendaVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<SoggettoAziendaNuovaVO> getSoggettiAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getSoggettiAziendaNuovaIscrizione(idAziendaNuova);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public SoggettoAziendaNuovaVO getRappLegaleNuovaIscrizione(
      long idAziendaNuova) throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getRappLegaleNuovaIscrizione(idAziendaNuova);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void aggiornaSoggettiAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<SoggettoAziendaNuovaVO> vSoggettoAziendaNuova)     
        throws SolmrException
  {
    try
    {
      nuovaIscrizioneDAO.deleteSoggettoAziendaNuova(aziendaNuovaVO.getIdAziendaNuova());
      
      if(Validator.isNotEmpty(vSoggettoAziendaNuova))
      {
        for(int i=0;i<vSoggettoAziendaNuova.size();i++)
        {
          nuovaIscrizioneDAO.insertSoggettoAziendaNuova(vSoggettoAziendaNuova.get(i));               
        }
      }
      
      if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
      {
        IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
        iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
        aggiornaStatoNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, iterRichiestaAziendaVO);       
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void aggiornaMacchineIrrAziendaNuova(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<MacchinaAziendaNuovaVO> vMacchineNuovaRichiesta)     
        throws SolmrException
  {
    try
    {
      nuovaIscrizioneDAO.deleteMacchineInsert(aziendaNuovaVO.getIdRichiestaAzienda());
      
      if(Validator.isNotEmpty(vMacchineNuovaRichiesta))
      {
        for(int i=0;i<vMacchineNuovaRichiesta.size();i++)
        {
          if(Validator.isEmpty(vMacchineNuovaRichiesta.get(i).getIdMacchina()))
          {
            nuovaIscrizioneDAO.insertMacchinaAziendaNuova(vMacchineNuovaRichiesta.get(i));
          }
          else
          {
            //aggiorno solo quelle con data scarico valorizzata
            if(Validator.isNotEmpty(vMacchineNuovaRichiesta.get(i).getDataScarico()))
            {
              nuovaIscrizioneDAO.updateMacchinaAziendaNuova(vMacchineNuovaRichiesta.get(i));
            }
          }
            
        }
      }
      
      if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
      {
        IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
        iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
        aggiornaStatoNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, iterRichiestaAziendaVO);       
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void aggiornaAzAssociateCaaAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, 
	    long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException
  {
    try
    {
      nuovaIscrizioneDAO.deleteSoggAssAziendaNuova(aziendaNuovaVO.getIdAziendaNuova());
      nuovaIscrizioneDAO.deleteAzAssAziendaNuova(aziendaNuovaVO.getIdAziendaNuova());
      
      if(Validator.isNotEmpty(vAssAziendaNuova))
      {
        for(int i=0;i<vAssAziendaNuova.size();i++)
        {
          nuovaIscrizioneDAO.insertAzzAssAziendaNuova(vAssAziendaNuova.get(i));               
        }
      }
      
      if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
      {
        IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
        iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
        aggiornaStatoNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, iterRichiestaAziendaVO);       
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	
	
	public void aggiornaAzAssociateCaaRichiestaVariazione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException
  {
    try
    {
      nuovaIscrizioneDAO.deleteAzzAssInsert(aziendaNuovaVO.getIdRichiestaAzienda());
      
      for(int i=0;i<vAssAziendaNuova.size();i++)
      {
        if(Validator.isEmpty(vAssAziendaNuova.get(i).getIdAziendaCollegata()))
        {
          nuovaIscrizioneDAO.insertAzzAssAziendaNuova(vAssAziendaNuova.get(i));
        }
        else
        {
          if("S".equalsIgnoreCase(vAssAziendaNuova.get(i).getFlagEliminato()))
          {
            nuovaIscrizioneDAO.updateEliminaAzAssAzNuova(vAssAziendaNuova.get(i));
          }
          else
          {
            nuovaIscrizioneDAO.updateAzAssAzNuova(vAssAziendaNuova.get(i));
          }
        }
            
      }
      
      
      if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
      {
        IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
        iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
        aggiornaStatoNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, iterRichiestaAziendaVO);       
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	
	public void aggiornaAzAssociateRichiestaVariazione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException
  {
    try
    {
      nuovaIscrizioneDAO.deleteSoggAzzAssInsert(aziendaNuovaVO.getIdRichiestaAzienda());
      nuovaIscrizioneDAO.deleteAzzAssInsert(aziendaNuovaVO.getIdRichiestaAzienda());
      
      for(int i=0;i<vAssAziendaNuova.size();i++)
      {
        if(Validator.isEmpty(vAssAziendaNuova.get(i).getIdAziendaCollegata()))
        {          
          Long idSoggAssAzNuova = null;
          if(Validator.isEmpty(vAssAziendaNuova.get(i).getIdAziendaAssociata()))
          {
            idSoggAssAzNuova = nuovaIscrizioneDAO.insertAzzAssSoggAziendaNuova(vAssAziendaNuova.get(i));
          }
          vAssAziendaNuova.get(i).setIdSoggAssAzNuova(idSoggAssAzNuova);
          nuovaIscrizioneDAO.insertAzzAssAziendaNuova(vAssAziendaNuova.get(i)); 
        }
        else
        {
          nuovaIscrizioneDAO.updateAzAssAzNuova(vAssAziendaNuova.get(i));
        }            
      }
      
      //cesso quelle importate ma non presenti a video importate
      Vector<AzAssAziendaNuovaVO> vAssAziendaDb = nuovaIscrizioneDAO.getAziendeAssociateAziendaRichVariazione(aziendaNuovaVO.getIdRichiestaAzienda());
      for(int i=0;i<vAssAziendaDb.size();i++)
      {
        if(vAssAziendaDb.get(i).getIdAziendaCollegata() != null)
        {
          boolean flagTrovato = false;
          for(int j=0;j<vAssAziendaNuova.size();j++)
          { 
            if(vAssAziendaNuova.get(j).getIdAssociateAzNuove() != null)
            {
              if(vAssAziendaDb.get(i).getIdAssociateAzNuove().compareTo(vAssAziendaNuova.get(j).getIdAssociateAzNuove()) == 0)
              {
                flagTrovato = true;
                break;
              }
            }
          }
          
          if(!flagTrovato)
          {
            nuovaIscrizioneDAO.updateEliminaAzAssAzNuova(vAssAziendaDb.get(i));
          }
        }
      }
      
      
      
      if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
      {
        IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
        iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
        aggiornaStatoNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, iterRichiestaAziendaVO);       
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public HashMap<String, AzAssAziendaNuovaVO> getAziendeAssociateCaaAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getAziendeAssociateCaaAziendaNuovaIscrizione(idAziendaNuova);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public HashMap<String, AzAssAziendaNuovaVO> getAziendeAssociateCaaAziendaRichVariazione(
      long idRichiestaAzienda) throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getAziendeAssociateCaaAziendaRichVariazione(idRichiestaAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<AzAssAziendaNuovaVO> getAziendeAssociateAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getAziendeAssociateAziendaNuovaIscrizione(idAziendaNuova);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<AzAssAziendaNuovaVO> getAziendeAssociateAziendaRichVariazione(
      long idRichiestaAzienda) throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getAziendeAssociateAziendaRichVariazione(idRichiestaAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<AzAssAziendaNuovaVO> getAziendeAssociateCaaStampaAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.getAziendeAssociateCaaStampaAziendaNuovaIscrizione(idAziendaNuova);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void aggiornaAzAssociateAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException
  {
    try
    {
      nuovaIscrizioneDAO.deleteSoggAssAziendaNuova(aziendaNuovaVO.getIdAziendaNuova());
      nuovaIscrizioneDAO.deleteAzAssAziendaNuova(aziendaNuovaVO.getIdAziendaNuova());
      
      if(Validator.isNotEmpty(vAssAziendaNuova))
      {
        for(int i=0;i<vAssAziendaNuova.size();i++)
        {
          Long idSoggAssAzNuova = null;
          if(Validator.isEmpty(vAssAziendaNuova.get(i).getIdAziendaAssociata()))
          {
            idSoggAssAzNuova = nuovaIscrizioneDAO.insertAzzAssSoggAziendaNuova(vAssAziendaNuova.get(i));
          }
          vAssAziendaNuova.get(i).setIdSoggAssAzNuova(idSoggAssAzNuova);
          nuovaIscrizioneDAO.insertAzzAssAziendaNuova(vAssAziendaNuova.get(i)); 
          
        }
      }
      
      if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
      {
        IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
        iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
        aggiornaStatoNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, iterRichiestaAziendaVO);       
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	
	
	public void caricaMacchineNuovaRichiesta(long idAzienda, long idRichiestaAzienda)     
     throws SolmrException
  {
    try
    {
      nuovaIscrizioneDAO.deleteMacchineImport(idRichiestaAzienda);
      
      Vector<MacchinaAziendaNuovaVO> vMacchine = nuovaIscrizioneDAO.getMacchineForImportAzNuova(idAzienda);
      
      if(Validator.isNotEmpty(vMacchine))
      {
        for(int i=0;i<vMacchine.size();i++)
        {
          vMacchine.get(i).setIdRichiestaAzienda(idRichiestaAzienda);
          nuovaIscrizioneDAO.insertMacchinaAziendaNuova(vMacchine.get(i));         
        }
      }
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	
	public void caricaAziendeAssociateRichiesta(long idAzienda, long idRichiestaAzienda, String flagSoloAggiunta)     
     throws SolmrException
  {
    try
    {
      nuovaIscrizioneDAO.deleteSoggAzzAssImport(idRichiestaAzienda);
      nuovaIscrizioneDAO.deleteAzzAssImport(idRichiestaAzienda);
      
      
      if(!"S".equalsIgnoreCase(flagSoloAggiunta))
      {
        Vector<AzAssAziendaNuovaVO> vAzzAss = nuovaIscrizioneDAO.getAzzAssForImportAzNuova(idAzienda);
        
        if(Validator.isNotEmpty(vAzzAss))
        {
          for(int i=0;i<vAzzAss.size();i++)
          {
            vAzzAss.get(i).setIdRichiestaAzienda(idRichiestaAzienda);
            if(Validator.isNotEmpty(vAzzAss.get(i).getIdSoggettoAssociato()))
            {
              vAzzAss.get(i).setIdSoggAssAzNuova(
                  nuovaIscrizioneDAO.insertAzzAssSoggAziendaNuova(vAzzAss.get(i)));
            }
            nuovaIscrizioneDAO.insertAzzAssAziendaNuova(vAzzAss.get(i));         
          }
        }
      }
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	
	public void caricaAziendeAssociateCaaRichiesta(long idAzienda, long idRichiestaAzienda)     
     throws SolmrException
  {
    try
    {
      //nuovaIscrizioneDAO.deleteSoggAzzAssImport(idRichiestaAzienda);
      nuovaIscrizioneDAO.deleteAzzAssImport(idRichiestaAzienda);
      
      Vector<AzAssAziendaNuovaVO> vAzzAss = nuovaIscrizioneDAO.getAzzAssCaaForImportAzNuova(idAzienda);
      
      if(Validator.isNotEmpty(vAzzAss))
      {
        for(int i=0;i<vAzzAss.size();i++)
        {
          vAzzAss.get(i).setIdRichiestaAzienda(idRichiestaAzienda);
          /*if(Validator.isNotEmpty(vAzzAss.get(i).getIdSoggettoAssociato()))
          {
            vAzzAss.get(i).setIdSoggAssAzNuova(
                nuovaIscrizioneDAO.insertAzzAssSoggAziendaNuova(vAzzAss.get(i)));
          }*/
          nuovaIscrizioneDAO.insertAzzAssAziendaNuova(vAzzAss.get(i));         
        }
      }
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	
	public void ribaltaMacchineNuovaRichiesta(long idRichiestaAzienda, long idUtenteAggiornamento)     
	     throws SolmrException
  {
    try
    {
      //nuovaIscrizioneDAO.deleteMacchineImport(idRichiestaAzienda);
      
      Vector<MacchinaAziendaNuovaVO> vMacchine = nuovaIscrizioneDAO.getMacchineAzNuova(idRichiestaAzienda);
      if(Validator.isNotEmpty(vMacchine))
      {
        for(int i=0;i<vMacchine.size();i++)
        {
          MacchinaAziendaNuovaVO macchinaNuovaVO = vMacchine.get(i);
 
          //non ancora presente su anagrafe
          if(Validator.isEmpty(macchinaNuovaVO.getIdMacchina()))
          {
            Long idMacchina = nuovaIscrizioneDAO.getIdMacchinaSuDb(macchinaNuovaVO);
            if(Validator.isEmpty(idMacchina))
            {
              MacchinaGaaVO macchinaVO = new MacchinaGaaVO();
              macchinaVO.setIdGenereMacchina(macchinaNuovaVO.getIdGenereMacchina());
              macchinaVO.setIdCategoria(macchinaNuovaVO.getIdCategoria());
              macchinaVO.setModello(macchinaNuovaVO.getTipoMacchina());
              macchinaVO.setMatricolaTelaio(macchinaNuovaVO.getMatricolaTelaio());
              macchinaVO.setIdMarca(macchinaNuovaVO.getIdMarca());
              macchinaVO.setExtIdUtenteAggiornamento(idUtenteAggiornamento);
              macchinaVO.setAnnoCostruzione(macchinaNuovaVO.getAnnoCostruzione());
              
              idMacchina = macchinaDAO.insertMacchinaGaa(macchinaVO);              
            }
            
            nuovaIscrizioneDAO.updateIdMacchinaNuovaMacchinaAziendaNuova(macchinaNuovaVO.getIdMacchineAzNuova(), idMacchina);
            
            PossessoMacchinaVO possessoVO = new PossessoMacchinaVO();
            possessoVO.setIdUte(macchinaNuovaVO.getIdUte());
            possessoVO.setIdTipoFormaPossesso(macchinaNuovaVO.getIdTipoFormaPossesso());
            possessoVO.setPercentualePossesso(macchinaNuovaVO.getPercentualePossesso());
            possessoVO.setDataCarico(macchinaNuovaVO.getDataCarico());
            possessoVO.setExtIdUtenteAggiornamento(idUtenteAggiornamento);
            possessoVO.setFlagValida("N");
            
            macchinaDAO.insertPossessoMacchinaGaa(possessoVO, idMacchina);
          
          }
        }
      }
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Vector<MacchinaAziendaNuovaVO> getMacchineAzNuova(long idRichiestaAzienda)
	  throws SolmrException
	{
    try
    {       
      return nuovaIscrizioneDAO.getMacchineAzNuova(idRichiestaAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public boolean isUtenteAbilitatoPresaInCarico(long idTipoRichiesta, String codiceRuolo) 
	    throws SolmrException
  {
    try
    {       
      return nuovaIscrizioneDAO.isUtenteAbilitatoPresaInCarico(idTipoRichiesta, codiceRuolo);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	
	
	
}
