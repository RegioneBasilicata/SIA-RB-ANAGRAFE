package it.csi.smranag.smrgaa.business;

import it.csi.sigmater.sigtersrv.dto.daticatastali.SoggettoCatastale;
import it.csi.sigmater.sigtersrv.dto.daticatastali.Titolarita;
import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.sigmater.ParticellaSigmaterVO;
import it.csi.smranag.smrgaa.dto.sigmater.RichiestaSigmaterVO;
import it.csi.smranag.smrgaa.dto.sigmater.SoggettoSigmaterVO;
import it.csi.smranag.smrgaa.dto.sigmater.TitolaritaParticellaSigVO;
import it.csi.smranag.smrgaa.dto.sigmater.TitolaritaSigmaterVO;
import it.csi.smranag.smrgaa.integration.SigmaterGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.ParticellaGaaDAO;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.anag.ParticellaCertificataVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.PersonaGiuridicaVO;
import it.csi.solmr.dto.anag.ProprietaCertificataVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.integration.anag.AnagWriteDAO;
import it.csi.solmr.integration.anag.ParticellaCertificataDAO;
import it.csi.solmr.integration.anag.SoggettiDAO;
import it.csi.solmr.util.SolmrLogger;

import java.math.BigDecimal;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * EJB utilizzato per accedere ai servizi PA/PD esposti da SigMater
 * @author 70525
 *
 */

@Stateless(name=SigmaterGaaBean.jndiName,mappedName=SigmaterGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SigmaterGaaBean implements SigmaterGaaLocal
{
	
  

	/**
   * 
   */
  private static final long serialVersionUID = 3207108271916771227L;
  public final static String jndiName="comp/env/solmr/gaa/SigmaterGaa";
  
  SessionContext sessionContext;
	
  private transient CommonDAO cDAO;
	private transient ParticellaGaaDAO pGaaDAO;
	private transient SigmaterGaaDAO sGaaDAO;
	private transient SoggettiDAO soggettiDAO;
	private transient AnagWriteDAO awDAO;
	private transient ParticellaCertificataDAO partCertDAO;
	
	private void initializeDAO() throws EJBException
	{
    try
    {
      cDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    	pGaaDAO = new ParticellaGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    	sGaaDAO = new SigmaterGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    	soggettiDAO = new SoggettiDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    	awDAO = new AnagWriteDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    	partCertDAO = new ParticellaCertificataDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.fatal(this, ex.getMessage());
      throw new EJBException(ex);
    }
	}

	@Resource
	public void setSessionContext(SessionContext sessionContext)
	{
	  this.sessionContext = sessionContext;
    initializeDAO();
	}
	
	
	public Vector<BaseCodeDescription> getComuneAndSezioneForSigmater(long idAzienda) 
	    throws SolmrException
  {    
    try
    {
      return pGaaDAO.getComuneAndSezioneForSigmater(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public RichiestaSigmaterVO getRichiestaSigmater(RichiestaSigmaterVO richSigVO)
      throws SolmrException
  {    
    try
    {
      return sGaaDAO.getRichiestaSigmater(richSigVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void updateRichiestaSigmater(RichiestaSigmaterVO richSigVO)
	    throws SolmrException
  {    
    try
    {
      sGaaDAO.updateRichiestaSigmater(richSigVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	
	public Long insertRichiestaSigmater(RichiestaSigmaterVO richSigVO)
	    throws SolmrException
  {    
    try
    {
      return sGaaDAO.insertRichiestaSigmater(richSigVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public SoggettoSigmaterVO getSoggettoSigmater(long idRichiestaSigmater)
	    throws SolmrException
  {    
    try
    {
      return sGaaDAO.getSoggettoSigmater(idRichiestaSigmater);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void updateSoggettoSigmater(SoggettoSigmaterVO soggSigVO)
	    throws SolmrException
  {    
    try
    {
      sGaaDAO.updateSoggettoSigmater(soggSigVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Long insertSoggettoSigmater(SoggettoSigmaterVO soggSigVO)
	    throws SolmrException
  {    
    try
    {
      return sGaaDAO.insertSoggettoSigmater(soggSigVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public boolean esisteTitolaritaSigmaterFromIdRichiesta(long idRichiestaSigmater)
	    throws SolmrException
  {    
    try
    {
      return sGaaDAO.esisteTitolaritaSigmaterFromIdRichiesta(idRichiestaSigmater);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void deleteTitolaritaParticellaSigFromPadre(long idRichiestaSigmater)
	    throws SolmrException
  {    
    try
    {
      sGaaDAO.deleteTitolaritaParticellaSigFromPadre(idRichiestaSigmater);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void deleteTitolaritaSigmater(long idRichiestaSigmater)
	    throws SolmrException
  {    
    try
    {
      sGaaDAO.deleteTitolaritaSigmater(idRichiestaSigmater);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Long getIdTipoDiritto(String codice)
	    throws SolmrException
  {    
    try
    {
      return sGaaDAO.getIdTipoDiritto(codice);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Long insertTitolaritaSigmater(TitolaritaSigmaterVO titSigVO)
	    throws SolmrException
  {    
    try
    {
      return sGaaDAO.insertTitolaritaSigmater(titSigVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public ParticellaSigmaterVO getParticellaSigmater(ParticellaSigmaterVO partSigVO)
	    throws SolmrException
  {    
    try
    {
      return sGaaDAO.getParticellaSigmater(partSigVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public void updateParticellaSigmater(ParticellaSigmaterVO partSigVO)
	    throws SolmrException
  {    
    try
    {
      sGaaDAO.updateParticellaSigmater(partSigVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public boolean esisteTitolaritaParticellaSig(TitolaritaParticellaSigVO titPartSigVO)
	    throws SolmrException
  {    
    try
    {
      return sGaaDAO.esisteTitolaritaParticellaSig(titPartSigVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Long insertTitolaritaParticellaSig(TitolaritaParticellaSigVO titPartSigVO)
	    throws SolmrException
  {    
    try
    {
      return sGaaDAO.insertTitolaritaParticellaSig(titPartSigVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public String getIstatComuneNonEstinfoFromCodFisc(String codFisc)
	    throws SolmrException
  {    
    try
    {
      return cDAO.getIstatComuneNonEstinfoFromCodFisc(codFisc);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	public Long insertParticellaSigmater(ParticellaSigmaterVO partSigVO)
	    throws SolmrException
  {    
    try
    {
      return sGaaDAO.insertParticellaSigmater(partSigVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
	
	
	public void importaTitolaritaSigmater(long idParticella, 
	    Titolarita[] titolarita, long idUtente) throws SolmrException
  {
    try
    {
      
      //Storicizzo
      Vector<ProprietaCertificataVO> vPropCert = partCertDAO.getListProprietaCertifByIdParticella(idParticella);
      ParticellaCertificataVO partCertVO = partCertDAO.findParticellaCertificataByIdParticella(idParticella, null);
      if(partCertVO == null)
      {
        throw new SolmrException("Non esiste un record su db_particella_certificata " +
        		"per la particella con id-"+idParticella);
      }
      Long idParticellaCertificata =  partCertVO.getIdParticellaCertificata();
      
      if(Validator.isNotEmpty(vPropCert))
      {
        for(int k=0;k<vPropCert.size();k++)
        {
          partCertDAO.storicizzaProprietaCertificata(
              vPropCert.get(k).getIdProprietaCertificata(), idUtente);
        }              
      }
      
      
      //per valorizzare 100 la percentuale possesso
      boolean unicaTitolarita = false;
      if(titolarita.length == 1)
      {
        unicaTitolarita = true;
      }
      
      //inserisco
      for(int i=0;i<titolarita.length;i++)
      {
        boolean unicoSoggetto = false;
        if(titolarita[0].getSoggettoDiRiferimento() == null)
        {
          unicoSoggetto = true;
        }
        
        for (int j=0;j<2;j++)
        {
          SoggettoCatastale soggetto=null;
          
          if (j==0) 
          {
            soggetto=titolarita[i].getSoggetto();
          }
          else 
          {
            soggetto=titolarita[i].getSoggettoDiRiferimento();
          }
          Long idSoggetto = null;
          if (soggetto!=null)
          {            
            if ("P".equals(soggetto.getTipoSoggetto())) 
            {
              String codiceFiscale = null;
              String  cognome= null;
              String  nome= null;
              String sesso = null;
              String dataNascita = null;
              String luogoNascita = null;
              String istatComune = null;
              if(soggetto.getSoggFisico()!=null)
              {
                codiceFiscale=soggetto.getSoggFisico().getCodFiscale();
                cognome=soggetto.getSoggFisico().getCognome();
                nome=soggetto.getSoggFisico().getNome();
                sesso=soggetto.getSoggFisico().getSesso();
                if ("1".equals(sesso)) sesso="M";
                if ("2".equals(sesso)) sesso="F";
                dataNascita=soggetto.getSoggFisico().getDataNascita();
                luogoNascita=soggetto.getSoggFisico().getLuogoNascita();
              }
              
              idSoggetto = soggettiDAO.getIdSoggettoFromPersonaFisica(codiceFiscale);
              //Inserisco un record su DB_SOGGETTO e DB_PERSONA_FISICA
              if(idSoggetto == null)
              {
                idSoggetto = awDAO.insertSoggetto(SolmrConstants.FLAG_S);
                
                
                if(Validator.isNotEmpty(luogoNascita))
                {
                  ComuneVO comuneVO = null;
                  try 
                  {
                    comuneVO = cDAO.getComuneByCUAA(luogoNascita);
                    if(Validator.isNotEmpty(comuneVO))
                      istatComune = comuneVO.getIstatComune(); 
                  }
                  catch(Exception e) 
                  {}
                }
                
                PersonaFisicaVO persVO = new PersonaFisicaVO();
                persVO.setIdSoggetto(idSoggetto);
                persVO.setCodiceFiscale(codiceFiscale);
                persVO.setCognome(cognome);
                persVO.setNome(nome);
                persVO.setSesso(sesso);
                persVO.setNascitaComune(istatComune);
                persVO.setNascitaData(DateUtils.parseDate(dataNascita));
                persVO.setResIndirizzo("-");
                awDAO.insertPersonaFisicaNew(persVO, idUtente);
              }
            }
            else
            {
              String codiceFiscale = null;
              String denominazione= null;
              String luogoNascita = null;
              String istatComune = null;
              if(soggetto.getSoggGiuridico()!=null)
              {
                codiceFiscale=soggetto.getSoggGiuridico().getCodFiscale();
                denominazione=soggetto.getSoggGiuridico().getDenominazione();
                luogoNascita=soggetto.getSoggGiuridico().getSede();
              }
              
              if(Validator.isNotEmpty(luogoNascita))
              {
                ComuneVO comuneVO = null;
                try 
                {
                  comuneVO = cDAO.getComuneByCUAA(luogoNascita);
                  if(Validator.isNotEmpty(comuneVO))
                    istatComune = comuneVO.getIstatComune(); 
                }
                catch(Exception e) 
                {}
              }
              
              idSoggetto = soggettiDAO.getIdSoggettoFromPersonaGiuridica(codiceFiscale);
              if(idSoggetto == null)
              {
                idSoggetto = awDAO.insertSoggetto(SolmrConstants.FLAG_N);
                
                
                PersonaGiuridicaVO persVO = new PersonaGiuridicaVO();
                persVO.setIdSoggetto(idSoggetto);
                persVO.setCodiceFiscale(codiceFiscale);
                persVO.setDenominazione(denominazione);
                persVO.setIstatComune(istatComune);
                persVO.setPartitaIva(codiceFiscale);
                
                awDAO.insertPersonaGiuridica(persVO, idUtente);                
              }             
            }
            
            
            
            //inserisco su Proprieta certificata
            ProprietaCertificataVO propCertVO = new ProprietaCertificataVO();
            propCertVO.setIdParticellaCertificata(idParticellaCertificata);
            propCertVO.setIdSoggetto(idSoggetto);
            propCertVO.setIdTipoDiritto(sGaaDAO.getIdTipoDiritto(titolarita[i].getCodiceDiritto()));
            
            BigDecimal percentualePossesso = null;
            if(Validator.isNotEmpty(titolarita[i].getQuotaDenominatore())
              && Validator.isNotEmpty(titolarita[i].getQuotaNumeratore()))
            {
              percentualePossesso = new BigDecimal(1);
              percentualePossesso = percentualePossesso.multiply(new BigDecimal(titolarita[i].getQuotaNumeratore()));
              percentualePossesso = percentualePossesso.divide(
                  new BigDecimal(titolarita[i].getQuotaDenominatore()),4,BigDecimal.ROUND_HALF_UP);
              percentualePossesso = percentualePossesso.multiply(new BigDecimal(100));
              if(!unicoSoggetto)
              {
                percentualePossesso = percentualePossesso.divide(
                    new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP);
              }
            }
            else
            {
              if(unicaTitolarita)
              {
                percentualePossesso = new BigDecimal(100);
                if(!unicoSoggetto)
                {
                  percentualePossesso = percentualePossesso.divide(
                      new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP);
                }
              }
              //non riesco a capire la percentuale
              else
              {
                percentualePossesso = null;
              }
            }
            propCertVO.setPercentualePossesso(percentualePossesso);
            try
            {
              propCertVO.setDataInizioTitolarita(DateUtils.parseDate(titolarita[i].getDataValidita()));
            }
            catch(Exception ex)
            {
              throw new SolmrException("Dato non importabile perchè non esiste la data inizio titolarità " +
                  "per la particella con id-"+idParticella);
            }
            
            partCertDAO.insertProprietaCertificata(propCertVO, idUtente);        
           
            
          } //if soggetto
        } //for soggetto
        
        
        
      }
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
    
    
  }
	
	
	
}
