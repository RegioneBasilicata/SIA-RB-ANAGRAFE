package it.csi.solmr.business.anag;

import it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAllevamento;
import it.csi.smranag.smrgaa.dto.allevamenti.StabulazioneTrattamento;
import it.csi.smranag.smrgaa.integration.AllevamentoDAO;
import it.csi.smranag.smrgaa.util.comparator.SottoCategoriaAllComparator;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.AllevamentoAnagVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.CategorieAllevamentoAnagVO;
import it.csi.solmr.dto.anag.TipoASLAnagVO;
import it.csi.solmr.dto.anag.TipoCategoriaAnimaleAnagVO;
import it.csi.solmr.dto.anag.TipoSpecieAnimaleAnagVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.dto.anag.sian.SianAllevamentiVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.integration.anag.AllevamentoAnagDAO;
import it.csi.solmr.integration.anag.FascicoloDAO;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.UteComparator;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * Classe per la gestione degli allevamenti anagrafe
 */

@Stateless(name="comp/env/solmr/anag/AllevamentoAnag",mappedName="comp/env/solmr/anag/AllevamentoAnag")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class AllevamentoAnagBean implements AllevamentoAnagLocal
{
  /**
   * 
   */
  private static final long            serialVersionUID = 1759439055310467153L;

  SessionContext                       sessionContext;

  private transient AllevamentoAnagDAO aaDAO;
  private transient FascicoloDAO       fDAO;
  private transient CommonDAO          cDAO             = null;
  private transient AllevamentoDAO     allevamentoDAO;

  private void initializeDAO() throws EJBException
  {
    try
    {
      aaDAO = new AllevamentoAnagDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      fDAO = new FascicoloDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      cDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      allevamentoDAO = new AllevamentoDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);

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

  public Vector<AllevamentoAnagVO> getAllevamentiByIdUTE(Long idUTE, int anno) throws SolmrException, Exception
  {
    try
    {
      Vector<AllevamentoAnagVO> allevamenti = aaDAO.getAllevamentiByIdUTE(idUTE, anno);

      for (int i = 0; i < allevamenti.size(); i++)
      {
        AllevamentoAnagVO allevamentoVO = (AllevamentoAnagVO) allevamenti.get(i);

        allevamentoVO.setCategorieAllevamentoVector(aaDAO.getCategorieAllevamento(allevamentoVO.getIdAllevamentoLong()));
        allevamentoVO.setTipoSpecieAnimaleAnagVO(aaDAO.getTipoSpecieAnimale(allevamentoVO.getIdSpecieAnimaleLong()));

        if (allevamentoVO.getIdASLLong() != null)
        {
          allevamentoVO.setTipoASLAnagVO(aaDAO.getTipoASL(allevamentoVO.getIdASLLong()));
        }

        allevamentoVO.setUtenteAggiornamentoVO(cDAO.getUtenteIrideById(allevamentoVO.getIdUtenteAggiornamentoLong()));
      }

      return allevamenti;
    }
    catch (NotFoundException ex)
    {
      throw new Exception("Nessun allevamento per l'UTE selezionato");
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  public Vector<Vector<AllevamentoAnagVO>> getAllevamentiByIdAzienda(Long idAzienda, int anno) throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "inside AllevamentoAnagBean : getAllevamentiByIdAzienda : idAzienda = " + idAzienda);
    try
    {
      Vector<Vector<AllevamentoAnagVO>> elencoAllevamenti = new Vector<Vector<AllevamentoAnagVO>>();

      Vector<AllevamentoAnagVO> allevamenti = aaDAO.getAllevamentiByIdAziendaOrdinati(idAzienda, anno);
      for (int i = 0; i < allevamenti.size(); i++)
      {
        AllevamentoAnagVO allevamentoVO = (AllevamentoAnagVO) allevamenti.get(i);
        allevamentoVO.setCategorieAllevamentoVector(aaDAO.getCategorieAllevamento(allevamentoVO.getIdAllevamentoLong()));
        allevamentoVO.setTipoSpecieAnimaleAnagVO(aaDAO.getTipoSpecieAnimale(allevamentoVO.getIdSpecieAnimaleLong()));
        if (allevamentoVO.getIdASLLong() != null)
        {
          allevamentoVO.setTipoASLAnagVO(aaDAO.getTipoASL(allevamentoVO.getIdASLLong()));
        }
        allevamentoVO.setUtenteAggiornamentoVO(cDAO.getUtenteIrideById(allevamentoVO.getIdUtenteAggiornamentoLong()));
      }
      elencoAllevamenti.add(allevamenti);
      // }
      return elencoAllevamenti;
    }
    catch (NotFoundException ex)
    {
      throw new SolmrException("Nessun allevamento per l'idAzienda selezionato");
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  public AllevamentoAnagVO[] getAllevamentiByIdUTE(Long idUTE, Date dataAl) throws SolmrException, Exception
  {
    try
    {
      AllevamentoAnagVO[] allevamenti = aaDAO.getAllevamentiByIdUTE(idUTE, dataAl);

      if (allevamenti != null)
        for (int i = 0; i < allevamenti.length; i++)
        {
          allevamenti[i].setCategorieAllevamentoVector(aaDAO.getCategorieAllevamento(allevamenti[i].getIdAllevamentoLong()));
        }
      return allevamenti;
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  public AllevamentoAnagVO[] getAllevamentiByIdAzienda(Long idAzienda, Date dataAl) throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "inside AllevamentoAnagBean : getAllevamentiByIdAzienda : idAzienda = " + idAzienda);
    try
    {
      AllevamentoAnagVO[] allevamenti = aaDAO.getAllevamentiByIdAziendaOrdinati(idAzienda, dataAl);
      if (allevamenti != null)
        for (int i = 0; i < allevamenti.length; i++)
        {
          allevamenti[i].setCategorieAllevamentoVector(aaDAO.getCategorieAllevamento(allevamenti[i].getIdAllevamentoLong()));
        }
      return allevamenti;
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  public Integer[] getAnniByIdAzienda(Long idAzienda) throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "inside AllevamentoAnagBean : getAnniByIdAzienda : idAzienda = " + idAzienda);
    try
    {
      return aaDAO.getAnniByIdAzienda(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  public Vector<CodeDescription> getTipoTipoProduzione(long idSpecie) throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "inside AllevamentoAnagBean : getTipoTipoProduzione");
    try
    {
      return aaDAO.getTipoTipoProduzione(idSpecie);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  public Vector<CodeDescription> getOrientamentoProduttivo(long idSpecie, long idTipoProduzione) throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "inside AllevamentoAnagBean : getOrientamentoProduttivo");
    try
    {
      return aaDAO.getOrientamentoProduttivo(idSpecie, idTipoProduzione);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }
  
  public Vector<CodeDescription> getTipoProduzioneCosman(long idSpecie, long idTipoProduzione, long idOrientamentoProduttivo) 
      throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "inside AllevamentoAnagBean : getOrientamentoProduttivo");
    try
    {
      return aaDAO.getTipoProduzioneCosman(idSpecie, idTipoProduzione, idOrientamentoProduttivo);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }
  
  public Vector<CodeDescription> getSottocategorieCosman(long idSpecie, 
      long idTipoProduzione, long idOrientamentoProduttivo, long idTipoProduzioneCosman, String flagEsiste)
      throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "inside AllevamentoAnagBean : getSottocategorieCosman");
    try
    {
      return aaDAO.getSottocategorieCosman(idSpecie, idTipoProduzione, idOrientamentoProduttivo, idTipoProduzioneCosman, flagEsiste);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  public AllevamentoAnagVO getAllevamento(Long idAllevamento) throws SolmrException, Exception
  {
    try
    {
      AllevamentoAnagVO allevamentoVO = aaDAO.getAllevamento(idAllevamento);
      allevamentoVO.setCategorieAllevamentoVector(aaDAO.getCategorieAllevamento(allevamentoVO.getIdAllevamentoLong()));
      allevamentoVO.setTipoSpecieAnimaleAnagVO(aaDAO.getTipoSpecieAnimale(allevamentoVO.getIdSpecieAnimaleLong()));

      if (allevamentoVO.getIdASLLong() != null)
      {
        allevamentoVO.setTipoASLAnagVO(aaDAO.getTipoASL(allevamentoVO.getIdASLLong()));
      }

      allevamentoVO.setUtenteAggiornamentoVO(cDAO.getUtenteIrideById(allevamentoVO.getIdUtenteAggiornamentoLong()));
      return allevamentoVO;
    }
    catch (NotFoundException ex)
    {
      throw new SolmrException("Nessun allevamento per l'idAllevamento selezionato");
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  public Vector<CategorieAllevamentoAnagVO> getCategorieAllevamento(Long idAllevamento) throws SolmrException, Exception
  {
    try
    {
      return aaDAO.getCategorieAllevamento(idAllevamento);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  public Long insertAllevamento(AllevamentoAnagVO allevamentoVO, long idUtenteAggiornamento, boolean inserisciAllev) throws SolmrException, Exception
  {
    Long pk = null;
    try
    {
      //Inserisco il record su DB_ALLEVAMENTO solo se inserisciAllev è true
      //altrimenti significa che arrivo qua dalla modifica e devo inserire solo i 
      //record collegati alle altre tabelle
      if (inserisciAllev)
        pk = aaDAO.insertAllevamento(allevamentoVO, idUtenteAggiornamento);
      else
        pk=allevamentoVO.getIdAllevamentoLong();
      
      SolmrLogger.debug(this, "Value of parameter [PK] in method insertAllevamento in AllevamentoAnagBean: " + pk + "\n");
      Vector<?> cat = allevamentoVO.getCategorieAllevamentoVector();
      SolmrLogger.debug(this, "Value of Vector cat in method insertAllevamento in AllevamentoAnagBean: " + cat);
      // Il vettore cat potrebbe essere null se chiamo questo metodo da importa
      // dati BDN quindi faccio il controllo...
      if (cat != null && cat.size() != 0)
      {
        // .Il sistema per ogni categoria animale presente nell’insieme
        // raggruppato per categoria animale dichiarata nella sezione
        // “Consistenza zootecnica”, inserisce
        // un record su db_categoria_allevamento.
        // Per far questo devo ordinare i record per poter distinguere
        // le varie categorie animali
        SottoCategoriaAllevamento sottoCategorie[] = cat.size() == 0 ? null : (SottoCategoriaAllevamento[]) cat.toArray(new SottoCategoriaAllevamento[0]);

        Arrays.sort(sottoCategorie, new SottoCategoriaAllComparator());

        long idCategoriaAnimale = sottoCategorie[0].getIdCategoriaAnimale();
        Vector<SottoCategoriaAllevamento> sottoCategorieVet = new Vector<SottoCategoriaAllevamento>();
        long quantitaTot = 0; // Valore calcolato come è la somma delle quantità
        // legate alle sottocategorie allevamento dichiarate
        // nella sezione “Consistenza zootecnica” che
        // insistono sulla categoria allevamento che si stà
        // inserendo.
        long quantitaProprietaTot = 0;
        double pesoVivoUnitarioTot = 0; // Valore calcolato come è la media
        // ponderata del peso vivo unitario “pesato”
        // per la relativa quantità delle
        // sottocategorie allevamento dichiarate
        // nella sezione “Consistenza zootecnica”
        // che insistono sulla categoria allevamento
        // che si stà inserendo.
        for (int i = 0; i < sottoCategorie.length; i++)
        {
          long quantita = 0;
          long quantitaProprieta = 0;
          double pesoVivoUnitario = 0;
          try
          {
            if(Validator.isNotEmpty(sottoCategorie[i].getQuantitaProprieta()))
              quantitaProprieta = Long.parseLong(sottoCategorie[i].getQuantitaProprieta().replace(',', '.'));
            if(Validator.isNotEmpty(sottoCategorie[i].getQuantita()))
              quantita = Long.parseLong(sottoCategorie[i].getQuantita().replace(',', '.'));
            if(Validator.isNotEmpty(sottoCategorie[i].getPesoVivo()))
              pesoVivoUnitario = Double.parseDouble(sottoCategorie[i].getPesoVivo().replace(',', '.'));
          }
          catch (Exception e)
          {
          }
          if (sottoCategorie[i].getIdCategoriaAnimale() == idCategoriaAnimale)
          {
            try
            {
              quantitaTot += quantita;
              quantitaProprietaTot += quantitaProprieta;
              if(quantita != 0)
              {
                pesoVivoUnitarioTot += pesoVivoUnitario * quantita;
              }
              else if(quantitaProprieta != 0)
              {
                pesoVivoUnitarioTot += pesoVivoUnitario * quantitaProprieta;
              }
            }
            catch (Exception e)
            {
            }
          }
          else
          {
            // Devo inserire la categoria su db
            CategorieAllevamentoAnagVO temp = new CategorieAllevamentoAnagVO();
            temp.setIdCategoriaAnimale(idCategoriaAnimale + "");
            temp.setQuantita("" + quantitaTot);
            temp.setQuantitaProprieta("" + quantitaProprietaTot);
            if(quantitaTot != 0)
            {
              temp.setPesoVivoUnitario("" + (pesoVivoUnitarioTot / (double) quantitaTot));
            }
            else if(quantitaProprietaTot != 0)
            {
              temp.setPesoVivoUnitario("" + (pesoVivoUnitarioTot / (double) quantitaProprietaTot));
            }
            else
            {
              temp.setPesoVivoUnitario("0");
            }
            Long pkCategorie = aaDAO.insertCategorieAllevamento(temp, pk);

            // Devo inserire su db tutte le sottocategorie
            for (int j = 0; j < sottoCategorieVet.size(); j++)
            {
              Long idSottocategoriaAllevamento=aaDAO.insertSottoCategorieAllevamento((SottoCategoriaAllevamento) sottoCategorieVet.get(j), pkCategorie);
              //Nel caso in cui sono state inserite le informazioni nella sezione “Stabulazione trattamenti” 
              //procedere nell’ inserimento di un record, per ogni stabulazione trattamento, sulla tavola db_stabulazione_trattamento 
              long idSottoCategoria=((SottoCategoriaAllevamento) sottoCategorieVet.get(j)).getIdSottoCategoriaAnimale();
              insertStabulazioneTrattamento(allevamentoVO,idSottocategoriaAllevamento,idSottoCategoria);
            }

            sottoCategorieVet = new Vector<SottoCategoriaAllevamento>();
            quantitaTot = quantita;
            quantitaProprietaTot = quantitaProprieta;
            if(quantita != 0)
            {
              pesoVivoUnitarioTot = pesoVivoUnitario * quantita;
            }
            else if(quantitaProprieta !=0)
            {
              pesoVivoUnitarioTot = pesoVivoUnitario * quantitaProprieta;
            }
            idCategoriaAnimale = sottoCategorie[i].getIdCategoriaAnimale();
          }
          sottoCategorieVet.add(sottoCategorie[i]);
        }
        // Devo inserire la categoria su db
        CategorieAllevamentoAnagVO temp = new CategorieAllevamentoAnagVO();
        temp.setIdCategoriaAnimale(idCategoriaAnimale + "");
        temp.setQuantita("" + quantitaTot);
        temp.setQuantitaProprieta("" + quantitaProprietaTot);
        if(quantitaTot != 0)
        {          
          temp.setPesoVivoUnitario("" + (pesoVivoUnitarioTot / (double) quantitaTot));
        }
        else if(quantitaProprietaTot != 0)
        {          
          temp.setPesoVivoUnitario("" + (pesoVivoUnitarioTot / (double) quantitaProprietaTot));
        }
        else
        {
          temp.setPesoVivoUnitario("0");
        }
        Long pkCategorie = aaDAO.insertCategorieAllevamento(temp, pk);

        // Devo inserire su db tutte le sottocategorie
        for (int j = 0; j < sottoCategorieVet.size(); j++)
        {
          Long idSottocategoriaAllevamento=aaDAO.insertSottoCategorieAllevamento((SottoCategoriaAllevamento) sottoCategorieVet.get(j), pkCategorie);
          //Nel caso in cui sono state inserite le informazioni nella sezione “Stabulazione trattamenti” 
          //procedere nell’ inserimento di un record, per ogni stabulazione trattamento, sulla tavola db_stabulazione_trattamento 
          long idSottoCategoria=((SottoCategoriaAllevamento) sottoCategorieVet.get(j)).getIdSottoCategoriaAnimale();
          insertStabulazioneTrattamento(allevamentoVO,idSottocategoriaAllevamento,idSottoCategoria);
          
        }
      }
      
      allevamentoDAO.deleteAcquaLavaggio(pk);
      if(Validator.isNotEmpty(allevamentoVO.getvAllevamentoAcquaLavaggio()))
      {
        for(int j=0;j<allevamentoVO.getvAllevamentoAcquaLavaggio().size();j++)
        {
          allevamentoVO.getvAllevamentoAcquaLavaggio().get(j).setIdAllevamento(pk);
          allevamentoDAO.insertAllevamentoAcquaLavaggio(
              allevamentoVO.getvAllevamentoAcquaLavaggio().get(j));          
        }        
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(ex.getMessage());
    }
    return pk;
  }
  
  private void insertStabulazioneTrattamento(AllevamentoAnagVO allevamentoVO,Long idSottocategoriaAllevamento,long idSottoCategoria)
    throws DataAccessException
  {
    if (allevamentoVO.getStabulazioniTrattamenti()!=null)
    {
      //scorro le stabulazioni
      int sizeStab = allevamentoVO.getStabulazioniTrattamenti().size();
      for (int k = 0; k < sizeStab; k++)
      {
        StabulazioneTrattamento stab = (StabulazioneTrattamento) allevamentoVO.getStabulazioniTrattamenti().get(k);
        if ((idSottoCategoria+"").equals(stab.getIdSottoCategoriaAnimale()))
        {
          //Devo inserire la stabulazione
          Long idStabulazioneTrattamento=aaDAO.insertStabulazioneTrattamento(stab, idSottocategoriaAllevamento);
          
          //Per ogni stabulazione trattamenti, inserita al punto precedente, 
          //procedere nell’inserimento dei relativi effluenti prodotti 
          //su db_effluente_prodotto.
          
          //double azotoProdottoPalabile=0;
          
          //Prima di procedere è necessario cercare per la sottocategoria/stabulazione la tipologia di effluente non palabile.
          if (stab.getStabNonPal()!=null)
          {
            //Se è stata trovata, e ne esiste una sola:
            //Inserisco effluenti non palabili
            String flagTrattamento=SolmrConstants.FLAG_N;
            double volumeProdotto=0, azotoProdotto=0, volumeProdottoAziendale=0, azotoProdottoAziendale=0;
            
            volumeProdotto=Double.parseDouble(stab.getNonPalabile().replace(',', '.'));
            azotoProdotto=stab.azotoNonPalabilePreTratt(); //Valore calcolato (vedi elenco formule rif.I)
            volumeProdottoAziendale=stab.volumeNonPalabileAziendale(); //Valore calcolato (vedi elenco formule rif.F)
            azotoProdottoAziendale=stab.azotoNonPalabilePreTrattAz(); //Valore calcolato (vedi elenco formule rif.L)
            
            //Inserisco prima il pretrattamento
            aaDAO.insertEffluenteProdotto(stab,idStabulazioneTrattamento, false, flagTrattamento,
                volumeProdotto, azotoProdotto, volumeProdottoAziendale, azotoProdottoAziendale,false); 
            
            //Poi inserisco il postTrattamento
            /*flagTrattamento=SolmrConstants.FLAG_S;
            
            //azoto_prodotto  Se non è selezionato il trattamento:Inserire il relativo valore. (Vedi Tab.A)
            //Se è stato indicato il trattamento con db_tipo_trattamento.flag_calcolo=’S’:Valore calcolato =(vedi elenco formule rif.O)
            //Se è stato indicato il trattamento e il relativo db_tipo_trattamento.flag_calcolo=’N’: Valore digitato dall’utente 
            //riproporzionato rispetto al volume non palabile post trattamento inserito dall’utente.Arrotondare il dato alle unità.
            
            azotoProdotto=stab.azotoProdNonPalabilePostTratt(); 
            azotoProdottoAziendale=stab.azotoProdPostTrattNonPalAz();      
            volumeProdotto=Double.parseDouble(stab.getNonPalabileTrat().replace(',', '.'));
           
            if (stab.getTipoTrattamento()!=null)
            {
              if (stab.isFlagCalcolo())           
                azotoProdotto=stab.azotoProdNonPalabilePostTratt(); 
              else
              {
                //valore riproporzionato
                double volumeProdottoNonPalTrat=Double.parseDouble(stab.getNonPalabileTrat().replace(',', '.'));
                double volumeProdottoPalTrat=Double.parseDouble(stab.getPalabileTrat().replace(',', '.'));
                azotoProdotto=Double.parseDouble(stab.getTotaleAzoto().replace(',', '.'));
                if (volumeProdottoNonPalTrat==0)
                  azotoProdotto=0;
                else azotoProdotto=azotoProdotto*volumeProdottoNonPalTrat/(volumeProdottoNonPalTrat+volumeProdottoPalTrat);
                azotoProdottoAziendale=azotoProdotto;
                if (volumeProdottoPalTrat!=0)
                  azotoProdottoPalabile=Double.parseDouble(stab.getTotaleAzoto().replace(',', '.'))-azotoProdotto;                
              }
            }
            
            
            
            
            //indica se è stato selezionato un trattamento
            boolean trattamento=false;
            
            //Se non è selezionato il trattamento: Inserire il relativo volume (Vedi Tab.A)
            //Se è stato indicato il trattamento con db_tipo_trattamento.flag_calcolo=’S’:Valore calcolato= (vedi elenco formule rif.Q)
            //[3. Memorizzare in una variabile Valore calcolato (vedi elenco formule rif.R)].
            //Se è stato indicato il trattamento con db_tipo_trattamento.flag_calcolo=’N’:Inserire lo stesso valore del campo volume_prodotto .
            
            if (stab.getTipoTrattamento()!=null)
            {
              trattamento=true;
              if (stab.isFlagCalcolo())           
                volumeProdottoAziendale=stab.volNonPalPostTrattAziend(); //Valore calcolato (vedi elenco formule rif.Q)
              else
                volumeProdottoAziendale=volumeProdotto;
            }
              
            
            
            aaDAO.insertEffluenteProdotto(stab,idStabulazioneTrattamento, false, flagTrattamento,
                volumeProdotto, azotoProdotto, volumeProdottoAziendale, azotoProdottoAziendale,trattamento); */
          }
          
          
          
          
          
          
          
          
          //Prima di procedere è necessario cercare per la sottocategoria/stabulazione la tipologia di effluente non palabile.
          if (stab.getStabPal()!=null)
          {
            //Se è stata trovata, e ne esiste una sola:
            //Inserisco effluenti palabili
            String flagTrattamento=SolmrConstants.FLAG_N;
            double volumeProdotto=0, azotoProdotto=0, volumeProdottoAziendale=0, azotoProdottoAziendale=0;
            
            volumeProdotto=Double.parseDouble(stab.getPalabile().replace(',', '.'));
            azotoProdotto= stab.azotoPalabilePreTratt(); //Valore calcolato (vedi elenco formule rif.G)
            volumeProdottoAziendale=stab.volumePalabileAziendale(); //Valore calcolato (vedi elenco formule rif.D)
            azotoProdottoAziendale=stab.azotoPalabilePreTrattAz(); //Valore calcolato (vedi elenco formule rif.H)
            
            //Inserisco prima il pretrattamento
            aaDAO.insertEffluenteProdotto(stab,idStabulazioneTrattamento, true, flagTrattamento,
                volumeProdotto, azotoProdotto, volumeProdottoAziendale, azotoProdottoAziendale, false); 
            
            /*double volumeProdottoC=volumeProdotto;
            double azotoProdottoC=azotoProdotto;
            double volumeProdottoAziendaleC=volumeProdottoAziendale;
            double azotoProdottoAziendaleC=azotoProdottoAziendale;
            
            //Poi inserisco il postTrattamento
            flagTrattamento=SolmrConstants.FLAG_S;
            
            
            volumeProdotto=Double.parseDouble(stab.getPalabileTrat().replace(',', '.'));
            
           
            //Se non è selezionato il trattamento:Inserire il relativo volume (Vedi Tab.C) 
            //Se è stato indicato il trattamento con db_tipo_trattamento.flag_calcolo=’S’:Valore calcolato =
            //(vedi elenco formule rif.Q)Valore calcolato =TabC.volume_prodotto_aziendale +Rif 3. (se presente)  
            //Se è stato indicato il trattamento con db_tipo_trattamento.flag_calcolo=’N’:Inserire lo stesso valore del campo volume_prodotto .
            
            //indica se è stato selezionato un trattamento
            boolean trattamento=false;
            
            if (stab.getTipoTrattamento()!=null)  
            {
              trattamento=true;
              if (stab.isFlagCalcolo())
              {
                volumeProdotto=volumeProdotto-volumeProdottoC;
                volumeProdottoAziendale=stab.volPalPostTrattAziend();
                azotoProdotto=stab.azotoProdPalabilePostTratt();
              }
              else  
                volumeProdottoAziendale=volumeProdotto;
            }
            if (stab.getTipoTrattamento()!=null) 
            {
              if (stab.isFlagCalcolo())  
                azotoProdottoAziendale=stab.azotoProdPostTrattPalAz();
              else
              {
                azotoProdottoAziendale=azotoProdotto=azotoProdottoPalabile;
              }
            }
            
                     
              
            aaDAO.insertEffluenteProdotto(stab,idStabulazioneTrattamento, true, flagTrattamento,
                volumeProdotto, azotoProdotto, volumeProdottoAziendale, azotoProdottoAziendale,trattamento); 
            
            
            
          
            //E)Inserimento Effluente palabile post trattamento
            //Se è stato indicato il trattamento con 
            //db_tipo_trattamento.flag_calcolo=’S’ allora inserire un record sulla 
            //tavola db_effluente_prodotto cosi’ valorizzato:
            //id_effluente_prodotto Sequence
            //id_stabulazione_trattamento il relativo db_stabulazione_trattamento.id_stabulazione_trattamento 
            //id_effluente  lo stesso precedente (Vedi Tab C)
            //flag_trattamento  ‘S’ (ovvero post trattamento)
            //volume_prodotto TabC.volume_prodotto 
            //azoto_prodotto  TabC.azoto_prodotto
            //volume_prodotto_aziendale TabC.volume_prodotto_aziendale
            //azoto_prodotto_aziendale  TabC. azoto_prodotto_aziendale

            if (trattamento && stab.isFlagCalcolo())
              aaDAO.insertEffluenteProdotto(stab,idStabulazioneTrattamento, true, flagTrattamento,
                  volumeProdottoC, azotoProdottoC, volumeProdottoAziendaleC, azotoProdottoAziendaleC,false); */
          }
          
        }
      }
    }
  }

  public Vector<UteVO> getElencoIdUTEByIdAzienda(Long idAzienda) throws SolmrException, Exception
  {
    Vector<UteVO> results = new Vector<UteVO>();
    try
    {
      Vector<Long> vect = aaDAO.getElencoIdUTEByIdAzienda(idAzienda);
      UteVO UTE = null;
      for (int i = 0; i < vect.size(); i++)
      {
        UTE = new UteVO();
        UTE = fDAO.getUteById((Long) vect.get(i));
        results.add(UTE);
      }

      // ordine il risultato
      UteVO[] temp = (UteVO[]) results.toArray(new UteVO[0]);
      if (temp != null)
      {
        Arrays.sort(temp, new UteComparator());
        results = new Vector<UteVO>();
        for (int i = 0; i < temp.length; i++)
          results.add(temp[i]);
      }
    }
    catch (NotFoundException ex)
    {
      throw new SolmrException("Nessun UTE trovato per l'azienda selezionata.");
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
    return results;
  }

  public Vector<TipoASLAnagVO> getTipiASL() throws SolmrException, Exception
  {
    try
    {
      return aaDAO.getTipiASL();
    }
    catch (NotFoundException ex)
    {
      throw new SolmrException("Non è stata trovata alcuna ASL");
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  public Vector<TipoSpecieAnimaleAnagVO> getTipiSpecieAnimale() throws SolmrException, Exception
  {
    try
    {
      Vector<TipoSpecieAnimaleAnagVO> tipiSpecieAnimale = aaDAO.getTipiSpecieAnimale();
      Vector<TipoSpecieAnimaleAnagVO> results = new Vector<TipoSpecieAnimaleAnagVO>();

      for (int i = 0; i < tipiSpecieAnimale.size(); i++)
      {
        TipoSpecieAnimaleAnagVO specie = (TipoSpecieAnimaleAnagVO) tipiSpecieAnimale.get(i);
        specie.setCategorie(aaDAO.getTipiCategoriaAnimaleBySpecie(specie.getIdSpecieAnimaleLong()));
        results.add(specie);
        SolmrLogger.debug(this, "AllevamentoAnagBean -- getTipiSpecieAnimale -- specie " + i + " " + specie);
        SolmrLogger.debug(this, "AllevamentoAnagBean -- getTipiSpecieAnimale -- unità  di misura " + specie.getUnitaDiMisura());
      }

      return results;
    }
    catch (NotFoundException ex)
    {
      throw new SolmrException("Non è stata trovata alcuna Specie Animale");
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }
  
  public Vector<TipoSpecieAnimaleAnagVO> getTipiSpecieAnimaleAzProv() throws SolmrException, Exception
  {
    try
    {
      Vector<TipoSpecieAnimaleAnagVO> tipiSpecieAnimale = aaDAO.getTipiSpecieAnimaleAzProv();
      Vector<TipoSpecieAnimaleAnagVO> results = new Vector<TipoSpecieAnimaleAnagVO>();

      for (int i = 0; i < tipiSpecieAnimale.size(); i++)
      {
        TipoSpecieAnimaleAnagVO specie = (TipoSpecieAnimaleAnagVO) tipiSpecieAnimale.get(i);
        specie.setCategorie(aaDAO.getTipiCategoriaAnimaleBySpecie(specie.getIdSpecieAnimaleLong()));
        results.add(specie);
        SolmrLogger.debug(this, "AllevamentoAnagBean -- getTipiSpecieAnimaleAzProv -- specie " + i + " " + specie);
        SolmrLogger.debug(this, "AllevamentoAnagBean -- getTipiSpecieAnimaleAzProv -- unità  di misura " + specie.getUnitaDiMisura());
      }

      return results;
    }
    catch (NotFoundException ex)
    {
      throw new SolmrException("Non è stata trovata alcuna Specie Animale");
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }
  
  

  /**
   * restituisce un vettore contenente le categorie relative ad una specie
   * 
   * @param idSpecie
   *          Long
   * @return TipoCategoriaAnimaleAnagVO
   * @throws SolmrException
   * @throws Exception
   */
  public Vector<TipoCategoriaAnimaleAnagVO> getCategorieByIdSpecie(Long idSpecie) throws SolmrException, Exception
  {
    try
    {
      return aaDAO.getTipiCategoriaAnimaleBySpecie(idSpecie);
    }
    catch (NotFoundException ex)
    {
      throw new SolmrException("Non è stata trovata alcuna Categoria Animale con il parametro indicato");
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  /**
   * Elimina un allevamento e tutte le categorieAllevamento collegate
   * 
   * @param idAllevamento
   *          Long
   * @throws SolmrException
   * @throws Exception
   */
  public void deleteAllevamentoAll(Long idAllevamento,boolean cancAllevamenti) throws SolmrException, Exception
  {
    try
    {
      if (cancAllevamenti && aaDAO.isAllevamentoStoricizzato(idAllevamento))
      {
        throw new SolmrException("Non è possibile cancellare un allevamento storicizzato");
      }
      else
      {
        if (cancAllevamenti && aaDAO.isAllevamentoDichiarato(idAllevamento))
        {
          throw new SolmrException(AnagErrors.ERRORE_NO_DELETE_ALLEVAMENTO);
        }
        else
        {         
          // Se cancAllevamenti è true cancello l'allevamento, altrimenti significa che
          //arrivo qui dalla modifica e devo cancellare solo le altre tabelle
          if (cancAllevamenti)
            aaDAO.deleteAllevamento(idAllevamento);
          else
          {
            // Cancello tutte le categorie associate all'allevamento ed i record 
            // collegati, faccio questo quando devo fare un update dell'allevamento
            aaDAO.deleteCategorieAllevamento(idAllevamento);  
          }
        }
      }
    }
    catch (SolmrException ex)
    {
      this.sessionContext.setRollbackOnly();
      throw ex;
    }
    catch (NotFoundException ex)
    {
      throw new SolmrException("Non è stata trovata alcuna Specie Animale");
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  public void updateAllevamento(AllevamentoAnagVO all, long idUtenteAggiornamento) throws SolmrException, Exception
  {
    try
    {
      if (aaDAO.isAllevamentoStoricizzato(all.getIdAllevamentoLong()))
      {
        throw new SolmrException("Non è possibile modificare un allevamento storicizzato");
      }
      else
      {
        if (aaDAO.isAllevamentoDichiarato(all.getIdAllevamentoLong()))
        {
          //Storicizzo il vecchio allevamento
          aaDAO.storicizzaAllevamento(all, idUtenteAggiornamento);
          
          //Inserisco l'allevamento modificato
          insertAllevamento(all, idUtenteAggiornamento,true);
          
          return;
        }
        
        //Cancello tutte le tabelle legate agli allevamenti
        deleteAllevamentoAll(all.getIdAllevamentoLong(),false);
        //Modifica la tabella degli allevamenti
        aaDAO.updateAllevamento(all, idUtenteAggiornamento);
        //Inserisco i nuovi dati sulle altre tabelle
        insertAllevamento(all, idUtenteAggiornamento,false);
      }
    }
    catch (NotFoundException ex)
    {
      throw new SolmrException("Non è stato trovato alcun allevamento");
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
    catch (SolmrException ex)
    {
      this.sessionContext.setRollbackOnly();
      throw ex;
    }
  }

  // Metodo per effettuare la cessazione di un allevamento
  public void storicizzaAllevamento(AllevamentoAnagVO all, long idUtenteAggiornamento) throws SolmrException, Exception
  {
    try
    {
      aaDAO.storicizzaAllevamento(all, idUtenteAggiornamento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception();
    }
  }

  // Metodo che mi restituisce un elenco di allevamenti secondo un ordine
  // stabilito
  // (Metodo introdotto per correggere baco di ordinamento non risolvibile con
  // la struttura creata
  // in partenza) Mauro Vocale 24/01/2005
  public Vector<AllevamentoAnagVO> getAllevamentiByIdAziendaOrdinati(Long idAzienda, int anno) throws SolmrException, Exception
  {
    Vector<AllevamentoAnagVO> elencoAllevamenti = null;
    try
    {
      elencoAllevamenti = aaDAO.getAllevamentiByIdAziendaOrdinati(idAzienda, anno);
    }
    catch (NotFoundException nfe)
    {
      throw new SolmrException(nfe.getMessage());
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoAllevamenti;
  }

  // Metodo che mi restituisce i dati relativi al tipo specie animale a partire
  // dall'id
  public TipoSpecieAnimaleAnagVO getTipoSpecieAnimale(Long idSpecieAnimale) throws Exception
  {
    TipoSpecieAnimaleAnagVO tipoSpecieAnimaleAnagVO = null;
    try
    {
      tipoSpecieAnimaleAnagVO = aaDAO.getTipoSpecieAnimale(idSpecieAnimale);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (NotFoundException nfe)
    {
      throw new Exception(nfe.getMessage());
    }
    return tipoSpecieAnimaleAnagVO;
  }

  /**
   * Metodo per verificare se il record allevamento che restituisce il SIAN è
   * già stato censito in anagrafe
   * 
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @param sianAllevamentiVO
   *          SianAllevamentiVO
   * @return boolean
   * @throws Exception
   */
  public boolean isRecordSianInAnagrafe(AnagAziendaVO anagAziendaVO, SianAllevamentiVO sianAllevamentiVO) throws Exception
  {
    try
    {
      return aaDAO.isRecordSianInAnagrafe(anagAziendaVO, sianAllevamentiVO);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per recuperare il tipo asl a partire dall'id_amm_competenza
   * 
   * @param idAmmCompetenza
   *          Long
   * @param isActive
   *          boolean
   * @return TipoASLAnagVO
   * @throws Exception
   * @throws SolmrException
   */
  public TipoASLAnagVO getTipoASLAnagVOByExtIdAmmCompetenza(Long idAmmCompetenza, boolean isActive) throws Exception, SolmrException
  {
    try
    {
      return aaDAO.getTipoASLAnagVOByExtIdAmmCompetenza(idAmmCompetenza, isActive);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di recuperare l'elenco degli allevamenti di
   * un'azienda agricola relativi ad un determinato piano di riferimento
   * 
   * @param idAzienda
   * @param idPianoRiferimento
   * @param orderBy
   * @return it.csi.solmr.dto.anag.AllevamentoAnagVO[]
   * @throws Exception
   */
  public AllevamentoAnagVO[] getListAllevamentiAziendaByPianoRifererimento(
      Long idAzienda, Long idPianoRiferimento, Long idUte, String[] orderBy) throws Exception
  {
    AllevamentoAnagVO[] elencoAllevamenti = null;
    try
    {
      elencoAllevamenti = aaDAO.getListAllevamentiAziendaByPianoRifererimento(idAzienda, idPianoRiferimento, idUte, orderBy);
      if (elencoAllevamenti != null)
      {
        for (int i = 0; i < elencoAllevamenti.length; i++)
        {
          elencoAllevamenti[i].setCategorieAllevamentoVector(aaDAO.getCategorieAllevamento(elencoAllevamenti[i].getIdAllevamentoLong()));
        }
      }
      return elencoAllevamenti;
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public TipoCategoriaAnimaleAnagVO getTipoCategoriaAnimale(Long idCategoriaAnimale) 
      throws Exception
  {
    try
    {
      return aaDAO.getTipoCategoriaAnimale(idCategoriaAnimale);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
}
