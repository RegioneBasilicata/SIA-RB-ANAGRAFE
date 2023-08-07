package it.csi.smranag.smrgaa.integration;


import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.uma.TipoGenereMacchinaGaaVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaMacchineAgricoleVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaMacchineAgricoleVO;
import it.csi.smranag.smrgaa.dto.uma.MacchinaGaaVO;
import it.csi.smranag.smrgaa.dto.uma.NumeroTargaGaaVO;
import it.csi.smranag.smrgaa.dto.uma.PossessoMacchinaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoCategoriaGaaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoFormaPossessoGaaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoMacchinaGaaVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.uma.MacchinaVO;
import it.csi.solmr.dto.uma.MatriceVO;
import it.csi.solmr.dto.uma.PossessoVO;
import it.csi.solmr.dto.uma.TargaVO;
import it.csi.solmr.dto.uma.UtilizzoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;


public class MacchineAgricoleGaaDAO extends it.csi.solmr.integration.BaseDAO {
  
  
  public MacchineAgricoleGaaDAO() throws ResourceAccessException{
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public MacchineAgricoleGaaDAO(String refName) throws ResourceAccessException 
  {
    super(refName);
  }
  
  
 /**
  * restituisce l'elenco dei manuali visibili relativamente ad un ruolo
  * 
  * 
  * 
  * @param descRuolo
  * @return
  * @throws DataAccessException
  */
  public long[] ricercaIdPossessoMacchina(
      FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIds = null;
    
    long ids[] = null;
    try
    {
      SolmrLogger.debug(this, "[MacchineAgricoleGaaDAO::ricercaIdPossessoMacchina] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf = new StringBuffer(" " +
        "SELECT  PM.ID_POSSESSO_MACCHINA " +
        "FROM    DB_POSSESSO_MACCHINA PM, "+
        "        DB_UTE UT, " +
        "        DB_MACCHINA_GAA MG," +
        "        DB_TIPO_GENERE_MACCHINA_GAA GMG, " +
        "        DB_TIPO_MACCHINA TM," +
        "        DB_TIPO_CATEGORIA_GAA TCG " +
        "WHERE   UT.ID_AZIENDA = ? " +
        "AND     UT.ID_UTE = PM.ID_UTE " +
        "AND     PM.ID_MACCHINA = MG.ID_MACCHINA " +
        "AND     MG.ID_GENERE_MACCHINA = GMG.ID_GENERE_MACCHINA " +
        "AND     GMG.ID_TIPO_MACCHINA = TM.ID_TIPO_MACCHINA (+) " +
        "AND     MG.ID_CATEGORIA = TCG.ID_CATEGORIA (+) ");
      if(Validator.isNotEmpty(filtriRicercaMacchineAgricoleVO.getIdGenereMacchina()))
      {
        queryBuf.append(        
        "AND    MG.ID_GENERE_MACCHINA = ? ");
      }
      if(!filtriRicercaMacchineAgricoleVO.isStorico())
      {
        queryBuf.append(
        "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
        "AND    PM.DATA_FINE_VALIDITA IS NULL " +
        "AND    PM.DATA_SCARICO IS NULL ");
      }
      else
      {
        queryBuf.append(
        "AND    (PM.DATA_FINE_VALIDITA IS NULL " +
        "        OR (PM.DATA_FINE_VALIDITA IS NOT NULL " +
        "            AND    PM.DATA_SCARICO IS NOT NULL)) ");        
      }

      queryBuf.append(
        "ORDER BY TM.DESCRIZIONE, " +
        "         GMG.DESCRIZIONE, " +
        "         TCG.DESCRIZIONE, " +
        "         PM.DATA_CARICO  ");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[MacchineAgricoleGaaDAO::ricercaIdPossessoMacchina] Query="
            + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, filtriRicercaMacchineAgricoleVO
        .getIdAzienda().longValue());
      if(filtriRicercaMacchineAgricoleVO.getIdGenereMacchina() != null)
      {
        stmt.setLong(++indice, filtriRicercaMacchineAgricoleVO
          .getIdGenereMacchina().longValue());
      }
      
      

      ResultSet rs = stmt.executeQuery();
      vIds = new Vector<Long>();
      while (rs.next())
      {
        vIds.add(new Long(rs.getLong("ID_POSSESSO_MACCHINA")));
      }
      int size = vIds.size();
      if (size == 0)
      {
        return null;
      }
      else
      {
        ids = new long[size];
        for (int i = 0; i < size; ++i)
        {
          ids[i] = ((Long) vIds.get(i)).longValue();
        }
        return ids;
      }
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vIds", vIds), new Variabile("ids", ids) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("filtriRicercaMacchineAgricoleVO", filtriRicercaMacchineAgricoleVO) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[MacchineAgricoleGaaDAO::ricercaIdPossessoMacchina] ", t,
          query, variabili, parametri);

      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[MacchineAgricoleGaaDAO::ricercaIdPossessoMacchina] END.");
    }
  }
  
  
  
  public long[] ricercaIdPossessoMacchinaImport(
      FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIds = null;
    
    long ids[] = null;
    try
    {
      SolmrLogger.debug(this, "[MacchineAgricoleGaaDAO::ricercaIdPossessoMacchinaImport] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf = new StringBuffer(" " +
        "SELECT  PM.ID_POSSESSO_MACCHINA " +
        "FROM    DB_POSSESSO_MACCHINA PM, "+
        "        DB_UTE UT, " +
        "        DB_ANAGRAFICA_AZIENDA AA, " +
        "        DB_MACCHINA_GAA MG," +
        "        DB_TIPO_GENERE_MACCHINA_GAA GMG, " +
        "        DB_TIPO_MACCHINA TM, " +
        "        DB_R_GENERE_MACCHINA_RUOLO RMR, " +
        "        DB_TIPO_CATEGORIA_GAA TCG " +
        "WHERE   AA.CUAA = ? " +
        "AND     AA.DATA_FINE_VALIDITA IS NULL " +
        "AND     AA.ID_AZIENDA = UT.ID_AZIENDA " +
        "AND     UT.ID_UTE = PM.ID_UTE " +
        "AND     PM.ID_MACCHINA = MG.ID_MACCHINA " +
        "AND     MG.ID_GENERE_MACCHINA = GMG.ID_GENERE_MACCHINA " +
        "AND     MG.ID_GENERE_MACCHINA = RMR.ID_GENERE_MACCHINA " +
        "AND     MG.ID_CATEGORIA = TCG.ID_CATEGORIA (+) " +
        "AND     (TCG.FLAG_CONTROLLI_UNIVOCITA = 'S' " +
        "         OR  MG.ID_CATEGORIA IS NULL) " +
        "AND     RMR.CODICE_RUOLO = ? " +
        "AND     GMG.FLAG_IMPORTABILE = 'N' " +
        "AND     GMG.ID_TIPO_MACCHINA = TM.ID_TIPO_MACCHINA (+) " +
        "AND     UT.DATA_FINE_ATTIVITA IS NULL " +
        "AND     PM.DATA_FINE_VALIDITA IS NULL " +
        "AND     (PM.DATA_SCARICO IS NOT NULL  " +
        "        OR  (PM.DATA_SCARICO IS NULL " +
        "             AND  PM.PERCENTUALE_POSSESSO < 100" +
        "             AND  NOT EXISTS ( SELECT UT2.ID_AZIENDA" +
        "                               FROM   DB_UTE UT2," +
        "                                      DB_POSSESSO_MACCHINA PM2 " +
        "                               WHERE  UT2.ID_AZIENDA = ? " +
        "                               AND    UT2.DATA_FINE_ATTIVITA IS NULL " +
        "                               AND    PM2.ID_UTE = UT2.ID_UTE  " +
        "                               AND    PM2.DATA_FINE_VALIDITA IS NULL " +
        "                               AND    PM2.ID_MACCHINA = PM.ID_MACCHINA ))) " +
        "ORDER BY TM.DESCRIZIONE, " +
        "         GMG.DESCRIZIONE, " +
        "         TCG.DESCRIZIONE, " +
        "         PM.DATA_CARICO  ");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[MacchineAgricoleGaaDAO::ricercaIdPossessoMacchinaImport] Query="
            + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setString(++indice, filtriRicercaMacchineAgricoleVO
          .getCuaa());
      stmt.setString(++indice, filtriRicercaMacchineAgricoleVO
          .getCodiceRuolo());
      stmt.setLong(++indice, filtriRicercaMacchineAgricoleVO
        .getIdAzienda().longValue());
      
      
      

      ResultSet rs = stmt.executeQuery();
      vIds = new Vector<Long>();
      while (rs.next())
      {
        vIds.add(new Long(rs.getLong("ID_POSSESSO_MACCHINA")));
      }
      int size = vIds.size();
      if (size == 0)
      {
        return null;
      }
      else
      {
        ids = new long[size];
        for (int i = 0; i < size; ++i)
        {
          ids[i] = ((Long) vIds.get(i)).longValue();
        }
        return ids;
      }
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vIds", vIds), new Variabile("ids", ids) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("filtriRicercaMacchineAgricoleVO", filtriRicercaMacchineAgricoleVO) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[MacchineAgricoleGaaDAO::ricercaIdPossessoMacchinaImport] ", t,
          query, variabili, parametri);

      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[MacchineAgricoleGaaDAO::ricercaIdPossessoMacchinaImport] END.");
    }
  }
  
  
  public RigaRicercaMacchineAgricoleVO[] getRigheRicercaMacchineAgricoleById(
      long ids[]) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    RigaRicercaMacchineAgricoleVO rigaRicercaMacchineAgricoleVO = null;
    RigaRicercaMacchineAgricoleVO righe[] = null;
    HashMap<Long,RigaRicercaMacchineAgricoleVO> hmRighe = null;
    try
    {
      SolmrLogger.debug(this,
          "[MacchineAgricoleGaaDAO::getRigheRicercaMacchineAgricoleById] BEGIN.");      

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
     
      queryBuf.append("" +   
        "SELECT  PM.ID_POSSESSO_MACCHINA, " +
        "        MG.ID_MACCHINA, " +
        "        COM.DESCOM, " +
        "        PROV.SIGLA_PROVINCIA, " +
        "        UT.INDIRIZZO, " +
        "        TM.DESCRIZIONE AS DESC_TIPO_MACCHINA, " +
        "        GMG.DESCRIZIONE AS DESC_TIPO_GENERE, " +
        "        TC.DESCRIZIONE AS DESC_TIPO_CATEGORIA, " +
        "        TMG.DESCRIZIONE AS DESC_TIPO_MARCA, " +
        "        MG.TIPO_MACCHINA, " +
        "        MG.MATRICOLA_TELAIO, " +
        "        NTG.NUMERO_TARGA, " +
        "        PM.DATA_CARICO, " +
        "        PM.DATA_SCARICO, " +
        "        MG.ANNO_COSTRUZIONE, " +
        "        AA.CUAA, " +
        "        AA.DENOMINAZIONE, " +
        "        PM.FLAG_VALIDA " +
        "FROM    DB_POSSESSO_MACCHINA PM, "+
        "        DB_UTE UT, " +
        "        DB_MACCHINA_GAA MG," +
        "        DB_TIPO_GENERE_MACCHINA_GAA GMG, " +
        "        DB_TIPO_MACCHINA TM, " +
        "        COMUNE COM, " +
        "        PROVINCIA PROV, " +
        "        DB_TIPO_CATEGORIA_GAA TC, " +
        "        DB_TIPO_MARCA_GAA TMG, " +
        "        DB_NUMERO_TARGA_GAA NTG, " +
        "        DB_ANAGRAFICA_AZIENDA AA " +
        "WHERE   PM.ID_POSSESSO_MACCHINA IN (").append(
           this.getIdListFromArrayForSQL(ids)).append(") " +
        "AND     UT.ID_UTE = PM.ID_UTE " +
        "AND     UT.COMUNE = COM.ISTAT_COMUNE " +
        "AND     COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
        "AND     PM.ID_MACCHINA = MG.ID_MACCHINA " +
        "AND     MG.ID_GENERE_MACCHINA = GMG.ID_GENERE_MACCHINA " +
        "AND     GMG.ID_TIPO_MACCHINA = TM.ID_TIPO_MACCHINA " +
        "AND     MG.ID_CATEGORIA = TC.ID_CATEGORIA (+) " +
        "AND     MG.ID_MARCA = TMG.ID_MARCA (+) " +
        "AND     MG.ID_MACCHINA = NTG.ID_MACCHINA (+) " +
        "AND     UT.ID_AZIENDA = AA.ID_AZIENDA " +
        "AND     AA.DATA_FINE_VALIDITA IS NULL ");
         
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      //conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::getRigheRicercaMacchineAgricoleById] Query="
                + query);
      }
      conn = getDatasource().getConnection();
      
      stmt = conn.prepareStatement(query);
      
      
      ResultSet rs = stmt.executeQuery();
     
      
      hmRighe = new HashMap<Long,RigaRicercaMacchineAgricoleVO>();
      while (rs.next())
      {
        rigaRicercaMacchineAgricoleVO = new RigaRicercaMacchineAgricoleVO();
        Long idPossessoMacchina = new Long(rs.getLong("ID_POSSESSO_MACCHINA"));
        rigaRicercaMacchineAgricoleVO.setIdPossessoMacchina(idPossessoMacchina.longValue());
        rigaRicercaMacchineAgricoleVO.setIdMacchina(new Long(rs.getLong("ID_MACCHINA")));
        rigaRicercaMacchineAgricoleVO.setComune(rs.getString("DESCOM"));
        rigaRicercaMacchineAgricoleVO.setSglProvincia(rs.getString("SIGLA_PROVINCIA"));
        rigaRicercaMacchineAgricoleVO.setIndirizzo(rs.getString("INDIRIZZO"));
        rigaRicercaMacchineAgricoleVO.setDescTipoMacchina(rs.getString("DESC_TIPO_MACCHINA"));
        rigaRicercaMacchineAgricoleVO.setDescTipoGenereMacchina(rs.getString("DESC_TIPO_GENERE"));
        rigaRicercaMacchineAgricoleVO.setDescTipoCategoriaMacchina(rs.getString("DESC_TIPO_CATEGORIA"));
        rigaRicercaMacchineAgricoleVO.setDescTipoMarca(rs.getString("DESC_TIPO_MARCA"));
        rigaRicercaMacchineAgricoleVO.setDescModello(rs.getString("TIPO_MACCHINA"));
        rigaRicercaMacchineAgricoleVO.setNumeroTarga(rs.getString("NUMERO_TARGA"));
        rigaRicercaMacchineAgricoleVO.setMatricolaTelaio(rs.getString("MATRICOLA_TELAIO"));
        rigaRicercaMacchineAgricoleVO.setDataCarico(rs.getTimestamp("DATA_CARICO"));
        rigaRicercaMacchineAgricoleVO.setDataScarico(rs.getTimestamp("DATA_SCARICO"));
        rigaRicercaMacchineAgricoleVO.setAnnoCostruzione(checkIntegerNull(rs.getString("ANNO_COSTRUZIONE")));
        rigaRicercaMacchineAgricoleVO.setCuaa(rs.getString("CUAA"));
        rigaRicercaMacchineAgricoleVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        rigaRicercaMacchineAgricoleVO.setFlagValida(rs.getString("FLAG_VALIDA"));
        
        hmRighe.put(idPossessoMacchina, rigaRicercaMacchineAgricoleVO);
      }
      int size = hmRighe.size();
      if (size == 0)
      {
        return null;
      }
      else
      {
        righe = new RigaRicercaMacchineAgricoleVO[size];
        for (int i = 0; i < size; ++i)
        {
          righe[i] = (RigaRicercaMacchineAgricoleVO) hmRighe.get(new Long(ids[i]));
        }
        return righe;
      }
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("hmRighe", hmRighe),
          new Variabile("rigaRicercaMacchineAgricoleVO", rigaRicercaMacchineAgricoleVO),
          new Variabile("righe", righe) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("ids", ids) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[MacchineAgricoleGaaDAO::getRigheRicercaMacchineAgricoleById] ", t,
          query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[MacchineAgricoleGaaDAO::getRigheRicercaMacchineAgricoleById] END.");
    }
  }
  
  
  public Vector<PossessoMacchinaVO> getElencoMacchineAgricoleForStampa(
      long idAzienda, Date dataInserimentoValidazione) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<PossessoMacchinaVO> vPossessoMacchina = null;
    PossessoMacchinaVO possessoMacchinaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[MacchineAgricoleGaaDAO::getElencoMacchineAgricoleForStampa] BEGIN.");      

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
     
      queryBuf.append("" +   
        "SELECT  PM.ID_POSSESSO_MACCHINA, " +
        "        MG.ID_MACCHINA, " +
        "        GMG.DESCRIZIONE AS DESC_TIPO_GENERE, " +
        "        TC.DESCRIZIONE AS DESC_TIPO_CATEGORIA, " +
        "        TMG.DESCRIZIONE AS DESC_TIPO_MARCA, " +
        "        MG.TIPO_MACCHINA, " +
        "        NTG.NUMERO_TARGA " +
        "FROM    DB_POSSESSO_MACCHINA PM, "+
        "        DB_UTE UT, " +
        "        DB_MACCHINA_GAA MG," +
        "        DB_TIPO_GENERE_MACCHINA_GAA GMG, " +
        "        DB_TIPO_CATEGORIA_GAA TC, " +
        "        DB_TIPO_MARCA_GAA TMG, " +
        "        DB_NUMERO_TARGA_GAA NTG " +
        "WHERE   UT.ID_AZIENDA = ? " +
        "AND     UT.ID_UTE = PM.ID_UTE " +
        "AND     PM.ID_MACCHINA = MG.ID_MACCHINA " +
        "AND     MG.ID_GENERE_MACCHINA = GMG.ID_GENERE_MACCHINA " +
        "AND     MG.ID_CATEGORIA = TC.ID_CATEGORIA (+) " +
        "AND     MG.ID_MARCA = TMG.ID_MARCA (+) " +
        "AND     MG.ID_MACCHINA = NTG.ID_MACCHINA (+) ");
      if(Validator.isNotEmpty(dataInserimentoValidazione))
      {
        queryBuf.append("" +
        "AND     PM.DATA_CARICO <= ? " +
        "AND     NVL(PM.DATA_SCARICO, TO_DATE('31/12/9999','DD/MM/YYYY')) >= ? " +
        "AND     PM.DATA_INIZIO_VALIDITA <= ? " +
        "AND     NVL(PM.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','DD/MM/YYYY')) >= ? ");
      }
      else
      {
        queryBuf.append("" +
        "AND     PM.DATA_CARICO IS NULL " +
        "AND     PM.DATA_FINE_VALIDITA IS NULL ");
      }
      queryBuf.append("" +   
        "ORDER BY GMG.DESCRIZIONE," +
        "         TC.DESCRIZIONE, " +
        "         TMG.DESCRIZIONE, " +
        "         MG.TIPO_MACCHINA ");
         
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      //conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::getElencoMacchineAgricoleForStampa] Query="
                + query);
      }
      conn = getDatasource().getConnection();
      
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      if(Validator.isNotEmpty(dataInserimentoValidazione))
      {
        stmt.setTimestamp(++indice, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++indice, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++indice, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++indice, convertDateToTimestamp(dataInserimentoValidazione));
      }
      
      
      ResultSet rs = stmt.executeQuery();
     
      while (rs.next())
      {
        if(vPossessoMacchina == null)
          vPossessoMacchina = new Vector<PossessoMacchinaVO>();
        
        possessoMacchinaVO = new PossessoMacchinaVO();
        
        possessoMacchinaVO.setIdPossessoMacchina(rs.getLong("ID_POSSESSO_MACCHINA"));
        MacchinaGaaVO macchinaVO = new MacchinaGaaVO();
        macchinaVO.setIdMacchina(rs.getLong("ID_MACCHINA"));
        TipoGenereMacchinaGaaVO tipoGenere= new TipoGenereMacchinaGaaVO();
        tipoGenere.setDescrizione(rs.getString("DESC_TIPO_GENERE"));
        macchinaVO.setGenereMacchinaVO(tipoGenere);
        if(Validator.isNotEmpty(rs.getString("DESC_TIPO_CATEGORIA")))
        {
          TipoCategoriaGaaVO tipoCategoria = new TipoCategoriaGaaVO();
          tipoCategoria.setDescrizione(rs.getString("DESC_TIPO_CATEGORIA"));
          macchinaVO.setTipoCategoriaVO(tipoCategoria);
        }
        macchinaVO.setDescMarca(rs.getString("DESC_TIPO_MARCA"));
        macchinaVO.setModello(rs.getString("TIPO_MACCHINA"));
        if(Validator.isNotEmpty(rs.getString("NUMERO_TARGA")))
        {
          NumeroTargaGaaVO numeroTarga = new NumeroTargaGaaVO();
          numeroTarga.setNumeroTarga(rs.getString("NUMERO_TARGA"));
          macchinaVO.setLastNumeroTargaVO(numeroTarga);
        }
        possessoMacchinaVO.setMacchinaVO(macchinaVO);
        
        vPossessoMacchina.add(possessoMacchinaVO);
      }
      
      return vPossessoMacchina;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("possessoMacchinaVO", possessoMacchinaVO),
          new Variabile("vPossessoMacchina", vPossessoMacchina) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda), 
        new Parametro("dataInserimentoValidazione", dataInserimentoValidazione) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[MacchineAgricoleGaaDAO::getElencoMacchineAgricoleForStampa] ", t,
          query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[MacchineAgricoleGaaDAO::getElencoMacchineAgricoleForStampa] END.");
    }
  }
  
  
  public void deleteNumeroTarga(Vector<Long> vIdMacchine) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger.debug(this,
              "[MacchineAgricoleGaaDAO::deleteNumeroTarga] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      String parametri=" (";
      
      if (vIdMacchine !=null)
      {
        for(int i=0;i<vIdMacchine.size();i++)
        {
          parametri+="?";
          if (i<vIdMacchine.size()-1)
            parametri+=",";
        }
      }
      parametri+=") ";
      
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("DELETE DB_NUMERO_TARGA_GAA WHERE ID_MACCHINA IN ").append(parametri);
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::deleteNumeroTarga] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      if (vIdMacchine !=null)
      {
        for(int i=0;i<vIdMacchine.size();i++)
          stmt.setLong(++indice,vIdMacchine.get(i).longValue());
      }
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdMacchine", vIdMacchine)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaDAO::deleteNumeroTarga] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::deleteNumeroTarga] END.");
    }
  }
  
  public void deletePosessoMacchina(Vector<Long> vIdMacchine) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger.debug(this,
              "[MacchineAgricoleGaaDAO::deletePosessoMacchina] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      String parametri=" (";
      
      if (vIdMacchine !=null)
      {
        for(int i=0;i<vIdMacchine.size();i++)
        {
          parametri+="?";
          if (i<vIdMacchine.size()-1)
            parametri+=",";
        }
      }
      parametri+=") ";
      
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("DELETE DB_POSSESSO_MACCHINA WHERE ID_MACCHINA IN ").append(parametri);
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::deletePosessoMacchina] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      if (vIdMacchine !=null)
      {
        for(int i=0;i<vIdMacchine.size();i++)
          stmt.setLong(++indice,vIdMacchine.get(i).longValue());
      }
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdMacchine", vIdMacchine)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaDAO::deletePosessoMacchina] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::deletePosessoMacchina] END.");
    }
  }
  
  public void deleteMacchina(Vector<Long> vIdMacchine) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger.debug(this,
              "[MacchineAgricoleGaaDAO::deleteMacchina] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      String parametri=" (";
      
      if (vIdMacchine !=null)
      {
        for(int i=0;i<vIdMacchine.size();i++)
        {
          parametri+="?";
          if (i<vIdMacchine.size()-1)
            parametri+=",";
        }
      }
      parametri+=") ";
      
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("DELETE DB_MACCHINA_GAA WHERE ID_MACCHINA IN ").append(parametri);
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::deleteMacchina] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      if (vIdMacchine !=null)
      {
        for(int i=0;i<vIdMacchine.size();i++)
          stmt.setLong(++indice,vIdMacchine.get(i).longValue());
      }
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdMacchine", vIdMacchine)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaDAO::deleteMacchina] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::deleteMacchina] END.");
    }
  }
  
  
  public Vector<Long> getListUniqueIdMacchinaForDelete(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIdMacchina = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getListUniqueIdMacchinaForDelete] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      "SELECT PM.ID_MACCHINA " + 
      "FROM   DB_POSSESSO_MACCHINA PM, " +
      "       DB_UTE UT, " +
      "       DB_MACCHINA_GAA MG, " +
      "       DB_TIPO_GENERE_MACCHINA_GAA GMG " + 
      "WHERE  UT.ID_AZIENDA = ? " +
      "AND    UT.ID_UTE = PM.ID_UTE " +
      "AND    PM.ID_MACCHINA = MG.ID_MACCHINA " +
      "AND    MG.ID_GENERE_MACCHINA = GMG.ID_GENERE_MACCHINA " +
      "AND    GMG.FLAG_IMPORTABILE = 'S' ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::getListUniqueIdMacchinaForDelete] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vIdMacchina == null)
        {
          vIdMacchina = new Vector<Long>();
        }
        
        Long idMacchina = new Long(rs.getLong("ID_MACCHINA"));
        if(!vIdMacchina.contains(idMacchina))
          vIdMacchina.add(idMacchina);
        
       
      }
      return vIdMacchina;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vIdMacchina", vIdMacchina) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::getListUniqueIdMacchinaForDelete] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getListUniqueIdMacchinaForDelete] END.");
    }
  }
  
  public Long insertMacchina(MacchinaVO macchinaVO, Long idGenereMacchina, Long idCategoria) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idMacchina = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::insertMacchina] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idMacchina = getNextPrimaryKey(SolmrConstants.SEQ_DB_MACCHINA);
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "INSERT INTO DB_MACCHINA_GAA (" + 
        "     ID_MACCHINA, " +
        "     ID_GENERE_MACCHINA, " + 
        "     ID_CATEGORIA, " +
        "     ID_MARCA, " +
        "     TIPO_MACCHINA, " +
        "     TARA, " +
        "     LORDO," +
        "     NUMERO_ASSI," +
        "     ILLUMINAZIONE," +
        "     NUMERO_OMOLOGAZIONE, " +
        "     CONSUMO_ORARIO, " +
        "     CALORIE," +
        "     POTENZA_CV, " +
        "     POTENZA_KW, " +
        "     ID_ALIMENTAZIONE," +
        "     ID_NAZIONALITA," +
        "     ID_TRAZIONE," +
        "     MATRICOLA_TELAIO," +
        "     MATRICOLA_MOTORE," +
        "     NUMERO_MATRICE," +
        "     EXT_ID_UTENTE_AGGIORNAMENTO," +
        "     DATA_AGGIORNAMENTO," +
        "     EXT_ID_MACCHINA_UMA," +
        "     EXT_ID_MATRICE_UMA)  " +
        "   VALUES" +
        "   (?,?,?,?,?,?,?,?,?,?," +
        "    ?,?,?,?,?,?,?,?,?,?," +
        "    ?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::insertMacchina] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idMacchina.longValue());
      stmt.setLong(++indice, idGenereMacchina.longValue());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(idCategoria));
      
      Long idMarca = null;
      if(macchinaVO.getMatriceVO() != null
        && macchinaVO.getMatriceVO().getIdMarca() != null)
      {
        idMarca = macchinaVO.getMatriceVO().getIdMarcaLong();
      }
      else
      {
        if(macchinaVO.getMatriceVO() != null
            && macchinaVO.getMatriceVO().getDescMarca() != null)
        {
          idMarca = getIdMarca(macchinaVO.getMatriceVO().getDescMarca());
        }
      }
      if(Validator.isEmpty(idMarca))
      {
        if(Validator.isNotEmpty(macchinaVO.getDatiMacchinaVO())
          && Validator.isNotEmpty(macchinaVO.getDatiMacchinaVO().getMarca()))
        {
          idMarca = getIdMarca(macchinaVO.getDatiMacchinaVO().getMarca());
        }
      }      
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(idMarca));
      
      String tipoMacchina = null;
      if(Validator.isNotEmpty(macchinaVO.getMatriceVO())
        && Validator.isNotEmpty(macchinaVO.getMatriceVO().getTipoMacchina()))
      {
        tipoMacchina = macchinaVO.getMatriceVO().getTipoMacchina();
      }
      else if(Validator.isNotEmpty(macchinaVO.getDatiMacchinaVO())
          && Validator.isNotEmpty(macchinaVO.getDatiMacchinaVO().getTipoMacchina()))
      {
        tipoMacchina = macchinaVO.getDatiMacchinaVO().getTipoMacchina();
      }
      stmt.setString(++indice, tipoMacchina);
      
      if(macchinaVO.getDatiMacchinaVO() != null
        && macchinaVO.getDatiMacchinaVO().getTara() != null)
      {
        stmt.setBigDecimal(++indice, new BigDecimal(
            macchinaVO.getDatiMacchinaVO().getTara().replace(",", ".")));
      }
      else
      {
        stmt.setString(++indice, null);
      }
      if(macchinaVO.getDatiMacchinaVO() != null
        && macchinaVO.getDatiMacchinaVO().getLordo() != null)
      {
        stmt.setBigDecimal(++indice, new BigDecimal(
            macchinaVO.getDatiMacchinaVO().getLordo().replace(",", ".")));
      }
      else
      {
        stmt.setString(++indice, null);
      }
      
      if(macchinaVO.getDatiMacchinaVO() != null
        && macchinaVO.getDatiMacchinaVO().getNumeroAssiLong() != null)
      {
        stmt.setInt(++indice, macchinaVO.getDatiMacchinaVO().getNumeroAssiLong().intValue());
      }
      else
      {
        stmt.setString(++indice, null);
      }
      
      if(macchinaVO.getMatriceVO() != null
        && macchinaVO.getMatriceVO().getIlluminazione() != null)
      {
        stmt.setString(++indice,macchinaVO.getMatriceVO().getIlluminazione());
      }
      else
      {
        stmt.setString(++indice, null);
      }
      
      if(macchinaVO.getMatriceVO() != null
        && macchinaVO.getMatriceVO().getNumeroOmologazione() != null)
      {
        stmt.setString(++indice,macchinaVO.getMatriceVO().getNumeroOmologazione());
      }
      else
      {
        stmt.setString(++indice, null);
      }
      
      if(macchinaVO.getMatriceVO() != null
        && macchinaVO.getMatriceVO().getConsumoOrarioLong() != null)
      {
        stmt.setInt(++indice, macchinaVO.getMatriceVO().getConsumoOrarioLong().intValue());
      }
      else
      {
        stmt.setString(++indice, null);
      }
      
      if(macchinaVO.getDatiMacchinaVO() != null
        && macchinaVO.getDatiMacchinaVO().getCalorieLong() != null)
      {
        stmt.setInt(++indice, macchinaVO.getDatiMacchinaVO().getCalorieLong().intValue());
      }
      else
      {
        stmt.setString(++indice, null);
      }
      
      Long potenzaCV = null;
      if(macchinaVO.getMatriceVO() != null
        && macchinaVO.getMatriceVO().getPotenzaCVLong() != null)
      {
        potenzaCV = macchinaVO.getMatriceVO().getPotenzaCVLong();
      }
      else
      {
        if(macchinaVO.getDatiMacchinaVO() != null
            && macchinaVO.getDatiMacchinaVO().getPotenzaLong() != null)
        {
          potenzaCV = macchinaVO.getDatiMacchinaVO().getPotenzaLong();
        }
      }
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(potenzaCV));
      
      if(macchinaVO.getMatriceVO() != null
        && macchinaVO.getMatriceVO().getPotenzaKWLong() != null)
      {
        stmt.setInt(++indice, macchinaVO.getMatriceVO().getPotenzaKWLong().intValue());
      }
      else
      {
        stmt.setString(++indice, null);
      }
      
      Long idAlimentazione = null;
      if(Validator.isNotEmpty(macchinaVO.getMatriceVO())
        && Validator.isNotEmpty(macchinaVO.getMatriceVO().getIdAlimentazioneLong()))
      {
        idAlimentazione = macchinaVO.getMatriceVO().getIdAlimentazioneLong();
      }
      else if(Validator.isNotEmpty(macchinaVO.getDatiMacchinaVO())
        && Validator.isNotEmpty(macchinaVO.getDatiMacchinaVO().getIdAlimentazioneLong()))
      {
        idAlimentazione = macchinaVO.getDatiMacchinaVO().getIdAlimentazioneLong();
      }
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(idAlimentazione));
      
      Long idNazionalita = null;
      if(Validator.isNotEmpty(macchinaVO.getMatriceVO())
        && Validator.isNotEmpty(macchinaVO.getMatriceVO().getIdNazionalitaLong()))
      {
        idNazionalita = macchinaVO.getMatriceVO().getIdNazionalitaLong();
      }
      else if(Validator.isNotEmpty(macchinaVO.getDatiMacchinaVO())
        && Validator.isNotEmpty(macchinaVO.getDatiMacchinaVO().getIdNazionalitaLong()))
      {
        idNazionalita = macchinaVO.getDatiMacchinaVO().getIdNazionalitaLong();
      }
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(idNazionalita));      
      
      if(macchinaVO.getMatriceVO() != null
        && macchinaVO.getMatriceVO().getIdTrazioneLong() != null)
      {
        stmt.setInt(++indice, macchinaVO.getMatriceVO().getIdTrazioneLong().intValue());
      }
      else
      {
        stmt.setString(++indice, null);
      }      
      
      stmt.setString(++indice, macchinaVO.getMatricolaTelaio());
      stmt.setString(++indice, macchinaVO.getMatricolaMotore());
      
      if(macchinaVO.getMatriceVO() != null
        && macchinaVO.getMatriceVO().getNumeroMatrice() != null)
      {
        stmt.setString(++indice, macchinaVO.getMatriceVO().getNumeroMatrice());
      }
      else
      {
        stmt.setString(++indice, null);
      }
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(macchinaVO.getExtIdUtenteAggiornamentoLong()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(macchinaVO.getDataAggiornamentoDate()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(macchinaVO.getIdMacchinaLong()));
      if(macchinaVO.getMatriceVO() != null
          && macchinaVO.getMatriceVO().getIdMatriceLong() != null)
      {
        stmt.setLong(++indice, macchinaVO.getMatriceVO().getIdMatriceLong().longValue());
      }
      else
      {
        stmt.setString(++indice, null);
      }
      
      stmt.executeUpdate();
      
      return idMacchina;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idMacchina", idMacchina)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("macchinaVO", macchinaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaDAO::insertMacchina] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::insertMacchina] END.");
    }
  }
  
  public Long getIdMarca(String descrizione) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idMarca = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getIdMarca] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      "SELECT TM.ID_MARCA " + 
      "FROM   DB_TIPO_MARCA_GAA TM " +
      "WHERE  TM.DESCRIZIONE = ? ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::getIdMarca] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setString(++indice, descrizione);
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        idMarca = new Long(rs.getLong("ID_MARCA"));       
      }
      
      return idMarca;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("idMarca", idMarca) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("descrizione", descrizione)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::getIdMarca] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getIdMarca] END.");
    }
  }
  
  public boolean existMarcaById(long idMarca) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean trovato = false;
    
    try
    {
      SolmrLogger.debug(this,"[MacchineAgricoleGaaGaaDAO::existMarcaById] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      "SELECT TM.ID_MARCA " + 
      "FROM   DB_TIPO_MARCA_GAA TM " +
      "WHERE  TM.ID_MARCA = ? ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,"[MacchineAgricoleGaaGaaDAO::existMarcaById] Query="+ query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idMarca);
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        trovato = true;     
      }
      
      return trovato;
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("idMarca", idMarca) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("trovato", trovato)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[MacchineAgricoleGaaGaaDAO::existMarcaById] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger.debug(this, "[MacchineAgricoleGaaGaaDAO::existMarcaById] END.");
    }
  }
  
  
  public Long insertNumeroTarga(TargaVO targaVO, Long idMacchina) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idNumeroTarga = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::insertNumeroTarga] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idNumeroTarga = getNextPrimaryKey(SolmrConstants.SEQ_DB_NUMERO_TARGA);
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "INSERT INTO DB_NUMERO_TARGA_GAA (" + 
        "     ID_NUMERO_TARGA, " +
        "     ID_MACCHINA," +
        "     ID_TARGA, " +
        "     NUMERO_TARGA," +
        "     ID_PROVINCIA," +
        "     FLAG_TARGA_NUOVA," +
        "     MC_824," +
        "     EXT_ID_UTENTE_AGGIORNAMENTO," +
        "     DATA_AGGIORNAMENTO)  " +
        "   VALUES" +
        "   (?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::insertNumeroTarga] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idNumeroTarga.longValue());
      stmt.setLong(++indice, idMacchina.longValue());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(targaVO.getIdTargaLong()));
      stmt.setString(++indice, targaVO.getNumeroTarga());
      stmt.setString(++indice, targaVO.getIdProvincia());
      stmt.setString(++indice, targaVO.getFlagTargaNuova());
      stmt.setString(++indice, targaVO.getMc_824());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(targaVO.getExtIdUtenteAggiornamentoLong()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(targaVO.getDataAggiornamentoDate()));
      
      stmt.executeUpdate();
      
      return idNumeroTarga;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idMacchina", idMacchina)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("targaVO", targaVO),
        new Parametro("idMacchina", idMacchina) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaDAO::insertNumeroTarga] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::insertNumeroTarga] END.");
    }
  }
  
  
  public Long insertPossessoMacchina(UtilizzoVO utilizzoVO, PossessoVO possessoVO, 
      Long idMacchina, Long idUte) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idPossessoMacchina = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::insertPossessoMacchina] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idPossessoMacchina = getNextPrimaryKey(SolmrConstants.SEQ_DB_POSSESSO_MACCHINA);
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "INSERT INTO DB_POSSESSO_MACCHINA (" + 
        "     ID_POSSESSO_MACCHINA, " +
        "     ID_MACCHINA," +
        "     ID_UTE, " +
        "     ID_TIPO_FORMA_POSSESSO," +
        "     DATA_SCADENZA_LEASING," +
        "     PERCENTUALE_POSSESSO," +
        "     DATA_INIZIO_VALIDITA," +
        "     DATA_FINE_VALIDITA," +
        "     DATA_AGGIORNAMENTO," +
        "     EXT_ID_UTENTE_AGGIORNAMENTO," +
        "     DATA_CARICO," +
        "     DATA_SCARICO," +
        "     ID_SCARICO," +
        "     ID_AZIENDA_LEASING)  " +
        "   VALUES" +
        "   (?,?,?,?,?,?,?,?,?,?," +
        "    ?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::insertPossessoMacchina] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idPossessoMacchina.longValue());
      stmt.setLong(++indice, idMacchina.longValue());
      stmt.setLong(++indice, idUte.longValue());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(possessoVO.getIdFormaPossessoLong()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(possessoVO.getDataScadenzaLeasingDate()));
      stmt.setInt(++indice, 100);
      stmt.setTimestamp(++indice, convertDateToTimestamp(possessoVO.getDataInizioValiditaDate()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(possessoVO.getDataFineValiditaDate()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(possessoVO.getDataAggiornamentoDate()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(possessoVO.getExtIdUtenteAggiornamentoLong()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(utilizzoVO.getDataCaricoDate()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(utilizzoVO.getDataScaricoDate()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(utilizzoVO.getIdScaricoLong()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(possessoVO.getExtIdAziendaLong()));
      
      
      
      stmt.executeUpdate();
      
      return idPossessoMacchina;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idPossessoMacchina", idPossessoMacchina)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("utilizzoVO", utilizzoVO),
          new Parametro("possessoVO", possessoVO),
        new Parametro("idMacchina", idMacchina), 
        new Parametro("idUte", idUte) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaDAO::insertPossessoMacchina] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::insertPossessoMacchina] END.");
    }
  }
  
  public Long insertPossessoMacchinaGaa(PossessoMacchinaVO possessoVO, 
      Long idMacchina) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idPossessoMacchina = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::insertPossessoMacchinaGaa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idPossessoMacchina = getNextPrimaryKey(SolmrConstants.SEQ_DB_POSSESSO_MACCHINA);
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "INSERT INTO DB_POSSESSO_MACCHINA (" + 
        "     ID_POSSESSO_MACCHINA, " +
        "     ID_MACCHINA," +
        "     ID_UTE, " +
        "     ID_TIPO_FORMA_POSSESSO," +
        "     DATA_SCADENZA_LEASING," +
        "     PERCENTUALE_POSSESSO," +
        "     DATA_INIZIO_VALIDITA," +
        "     DATA_FINE_VALIDITA," +
        "     DATA_AGGIORNAMENTO," +
        "     EXT_ID_UTENTE_AGGIORNAMENTO," +
        "     DATA_CARICO," +
        "     DATA_SCARICO," +
        "     ID_SCARICO," +
        "     ID_AZIENDA_LEASING," +
        "     FLAG_VALIDA)  " +
        "   VALUES" +
        "   (?,?,?,?,?,?,?,?,?,?," +
        "    ?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::insertPossessoMacchinaGaa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idPossessoMacchina.longValue());
      stmt.setLong(++indice, idMacchina.longValue());
      stmt.setLong(++indice, possessoVO.getIdUte().longValue());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(possessoVO.getIdTipoFormaPossesso()));
      stmt.setTimestamp(++indice, null);
      stmt.setBigDecimal(++indice, possessoVO.getPercentualePossesso());
      Date dataSys = new Date();
      stmt.setTimestamp(++indice, convertDateToTimestamp(dataSys));
      stmt.setTimestamp(++indice, null);
      stmt.setTimestamp(++indice, convertDateToTimestamp(dataSys));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(possessoVO.getExtIdUtenteAggiornamento()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(possessoVO.getDataCarico()));
      stmt.setTimestamp(++indice, null);
      stmt.setBigDecimal(++indice, null);
      stmt.setBigDecimal(++indice, null);
      String flagValida = "S";
      if(Validator.isNotEmpty(possessoVO.getFlagValida()))
        flagValida = possessoVO.getFlagValida();
      stmt.setString(++indice, flagValida);
      
      
      
      stmt.executeUpdate();
      
      return idPossessoMacchina;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idPossessoMacchina", idPossessoMacchina)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("possessoVO", possessoVO),
        new Parametro("idMacchina", idMacchina) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaDAO::insertPossessoMacchinaGaa] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::insertPossessoMacchinaGaa] END.");
    }
  }
  
  
  public Vector<TipoGenereMacchinaGaaVO> getElencoTipoGenereMacchina() 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoGenereMacchinaGaaVO> vTipoGenere = null;
    TipoGenereMacchinaGaaVO tipoGenereMacchinaGaaVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoGenereMacchina] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      "SELECT TGM.ID_GENERE_MACCHINA, " +
      "       TGM.ID_TIPO_MACCHINA, " +
      "       TGM.CODICE_GENERE," +
      "       TGM.DESCRIZIONE, " +
      "       TGM.FLAG_MATRICE, " +
      "       TGM.FLAG_IMPORTABILE " + 
      "FROM   DB_TIPO_GENERE_MACCHINA_GAA TGM " +
      "WHERE  TGM.DATA_FINE_VALIDITA IS NULL " +
      "ORDER BY TGM.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::getElencoTipoGenereMacchina] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
         
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoGenere == null)
          vTipoGenere = new Vector<TipoGenereMacchinaGaaVO>();
        
        tipoGenereMacchinaGaaVO = new TipoGenereMacchinaGaaVO();
        tipoGenereMacchinaGaaVO.setIdGenereMacchina(rs.getLong("ID_GENERE_MACCHINA"));
        tipoGenereMacchinaGaaVO.setIdTipoMacchina(rs.getLong("ID_TIPO_MACCHINA"));
        tipoGenereMacchinaGaaVO.setCodiceGenere(rs.getString("CODICE_GENERE"));
        tipoGenereMacchinaGaaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoGenereMacchinaGaaVO.setFlagImportabile(rs.getString("FLAG_IMPORTABILE"));
        tipoGenereMacchinaGaaVO.setFlagMatrice(rs.getString("FLAG_MATRICE"));
        
        
        vTipoGenere.add(tipoGenereMacchinaGaaVO);
      }
      
      return vTipoGenere;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoGenere", vTipoGenere),
          new Variabile("tipoGenereMacchinaGaaVO", tipoGenereMacchinaGaaVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoGenereMacchina] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoGenereMacchina] END.");
    }
  }
  
  
  public TipoGenereMacchinaGaaVO getTipoGenereMacchinaByPrimaryKey(long idGenereMacchina) 
      throws DataAccessException
    {
      String query = null;
      Connection conn = null;
      PreparedStatement stmt = null;
      StringBuffer queryBuf = null;
      TipoGenereMacchinaGaaVO tipoGenereMacchinaGaaVO = null;
      
      try
      {
        SolmrLogger
            .debug(this,
                "[MacchineAgricoleGaaGaaDAO::getTipoGenereMacchinaByPrimaryKey] BEGIN.");
    
        /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
        
    
        queryBuf = new StringBuffer();
        queryBuf.append("" +
        "SELECT TGM.ID_GENERE_MACCHINA, " +
        "       TGM.ID_TIPO_MACCHINA, " +
        "       TGM.CODICE_GENERE," +
        "       TGM.DESCRIZIONE, " +
        "       TGM.FLAG_MATRICE, " +
        "       TGM.FLAG_IMPORTABILE " + 
        "FROM   DB_TIPO_GENERE_MACCHINA_GAA TGM " +
        "WHERE  TGM.ID_GENERE_MACCHINA = ? ");
        
        query = queryBuf.toString();
        /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
        conn = getDatasource().getConnection();
        if (SolmrLogger.isDebugEnabled(this))
        {
          // Dato che la query costruita dinamicamente è un dato importante la
          // registro sul file di log se il
          // debug è abilitato
    
          SolmrLogger.debug(this,
              "[MacchineAgricoleGaaGaaDAO::getTipoGenereMacchinaByPrimaryKey] Query="
                  + query);
        }
        stmt = conn.prepareStatement(query);
        
        int indice = 0;
        stmt.setLong(++indice, idGenereMacchina);
        
           
    
        // Setto i parametri della query
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next())
        {          
          tipoGenereMacchinaGaaVO = new TipoGenereMacchinaGaaVO();
          tipoGenereMacchinaGaaVO.setIdGenereMacchina(rs.getLong("ID_GENERE_MACCHINA"));
          tipoGenereMacchinaGaaVO.setIdTipoMacchina(rs.getLong("ID_TIPO_MACCHINA"));
          tipoGenereMacchinaGaaVO.setCodiceGenere(rs.getString("CODICE_GENERE"));
          tipoGenereMacchinaGaaVO.setDescrizione(rs.getString("DESCRIZIONE"));
          tipoGenereMacchinaGaaVO.setFlagImportabile(rs.getString("FLAG_IMPORTABILE"));
          tipoGenereMacchinaGaaVO.setFlagMatrice(rs.getString("FLAG_MATRICE"));
          
        }
        
        return tipoGenereMacchinaGaaVO;
      }
      catch (Exception t)
      {
        // Vettore di variabili interne del metodo
        Variabile variabili[] = new Variabile[]
        { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
            new Variabile("tipoGenereMacchinaGaaVO", tipoGenereMacchinaGaaVO) };
    
        // Vettore di parametri passati al metodo
        Parametro parametri[] = new Parametro[]
        { new Parametro("idGenereMacchina", idGenereMacchina) };
        // Logging dell'eccezione, query, variabili e parametri del metodo
        LoggerUtils
            .logDAOError(
                this,
                "[MacchineAgricoleGaaGaaDAO::getTipoGenereMacchinaByPrimaryKey] ",
                t, query, variabili, parametri);
        /*
         * Rimappo e rilancio l'eccezione come DataAccessException.
         */
        throw new DataAccessException(t.getMessage());
      }
      finally
      {
        /*
         * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
         * ignora ogni eventuale eccezione)
         */
        close(null, stmt, conn);
    
        // Fine metodo
        SolmrLogger
            .debug(this,
                "[MacchineAgricoleGaaGaaDAO::getTipoGenereMacchinaByPrimaryKey] END.");
      }
    }
  
  
  
  public PossessoMacchinaVO getPosessoMacchinaFromId(long idPossessoMacchina) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    PossessoMacchinaVO possessoMacchinaVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getPosessoMacchinaFromId] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      "SELECT PM.ID_POSSESSO_MACCHINA, " +
      "       PM.ID_MACCHINA, " +
      "       COM.DESCOM, " +
      "       PROV.SIGLA_PROVINCIA, " +
      "       UT.INDIRIZZO, " +
      "       TMM.DESCRIZIONE AS DESC_TIPO_MACCHINA, " +
      "       MG.ID_GENERE_MACCHINA, " +
      "       GMG.DESCRIZIONE AS DESC_TIPO_GENERE, " +
      "       TCG.DESCRIZIONE AS DESC_TIPO_CATEGORIA, " +
      "       TCG.FLAG_CONTROLLI_UNIVOCITA, " +
      "       TMG.DESCRIZIONE AS DESC_TIPO_MARCA, " +
      "       MG.TIPO_MACCHINA, " +
      "       MG.ANNO_COSTRUZIONE, " +
      "       MG.MATRICOLA_TELAIO, " +
      "       MG.MATRICOLA_MOTORE, " +
      "       NTG.NUMERO_TARGA, " +
      "       MG.CALORIE, " +
      "       MG.POTENZA_CV, " +
      "       MG.POTENZA_KW, " +
      "       TAG.DESCRIZIONE AS DESC_TIPO_ALIMENTAZIONE, " +
      "       TNG.DESCRIZIONE AS DESC_TIPO_NAZIONALITA, " +
      "       MG.LORDO, " +
      "       MG.TARA, " +
      "       MG.NOTE, " +
      "       MG.EXT_ID_UTENTE_AGGIORNAMENTO, " +
      "       MG.DATA_AGGIORNAMENTO, " +
      "       AA.CUAA, " +
      "       AA.PARTITA_IVA, " +
      "       AA.DENOMINAZIONE, " +
      "       AA.SEDELEG_CAP, " +
      "       AA.SEDELEG_INDIRIZZO, " +
      "       COM_LEG.DESCOM AS DESCOM_LEG, " +
      "       PROV_LEG.SIGLA_PROVINCIA AS SGLPROV_LEG, " +
      "       DU.DITTA_UMA, " +
      "       PROV.SIGLA_PROVINCIA SGLPROV_UMA, " +
      "       TS.DESCRIZIONE AS DESC_TIPO_SCARICO, " +
      "       TFP.ID_TIPO_FORMA_POSSESSO, " +
      "       TFP.DESCRIZIONE AS DESC_TIPO_FORMA_POSSESSO, " +
      "       PM.DATA_CARICO, " +
      "       PM.DATA_SCARICO, " +
      "       MG.ID_MARCA, " +
      "       UT.ID_UTE, " +
      "       PM.PERCENTUALE_POSSESSO, " +
      "       MG.NUMERO_OMOLOGAZIONE," +
      "       MG.NUMERO_ASSI, " +
      "       MG.ILLUMINAZIONE, " +
      "       TT.DESCRIZIONE AS DESC_TIPO_TRAZIONE " +
      "FROM   DB_POSSESSO_MACCHINA PM, " +
      "       DB_UTE UT, " +
      "       COMUNE COM, " +
      "       PROVINCIA PROV, " +
      "       COMUNE COM_LEG, " +
      "       PROVINCIA PROV_LEG, " +
      "       DB_MACCHINA_GAA MG, " +
      "       DB_TIPO_GENERE_MACCHINA_GAA GMG, " +
      "       DB_TIPO_NAZIONALITA_GAA TNG, " +
      "       DB_TIPO_MARCA_GAA TMG, " +
      "       DB_TIPO_MACCHINA TMM, " +
      "       DB_TIPO_CATEGORIA_GAA TCG, " +
      "       DB_NUMERO_TARGA_GAA NTG, " +
      "       DB_TIPO_ALIMENTAZIONE_GAA TAG, " +
      "       DB_ANAGRAFICA_AZIENDA AA, " +
      "       DB_DITTA_UMA DU, "+
      "       PROVINCIA PROV_DU, " +
      "       DB_TIPO_SCARICO TS, " +
      "       DB_TIPO_FORMA_POSSESSO_GAA TFP, " +
      "       DB_TIPO_TRAZIONE TT " +
      "WHERE  PM.ID_POSSESSO_MACCHINA = ? " +
      "AND    PM.ID_MACCHINA = MG.ID_MACCHINA " +
      "AND    PM.ID_UTE = UT.ID_UTE " +
      "AND    UT.ID_AZIENDA = AA.ID_AZIENDA " +
      "AND    AA.DATA_FINE_VALIDITA IS NULL " +
      "AND    UT.COMUNE = COM.ISTAT_COMUNE " +
      "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
      "AND    AA.SEDELEG_COMUNE = COM_LEG.ISTAT_COMUNE " +
      "AND    COM_LEG.ISTAT_PROVINCIA = PROV_LEG.ISTAT_PROVINCIA " +
      "AND    MG.ID_GENERE_MACCHINA = GMG.ID_GENERE_MACCHINA " +
      "AND    GMG.ID_TIPO_MACCHINA = TMM.ID_TIPO_MACCHINA " +
      "AND    MG.ID_MACCHINA = NTG.ID_MACCHINA (+) " +
      "AND    MG.ID_CATEGORIA = TCG.ID_CATEGORIA (+) " +
      "AND    MG.ID_MARCA = TMG.ID_MARCA (+) " +
      "AND    MG.ID_ALIMENTAZIONE = TAG.ID_ALIMENTAZIONE (+) " +
      "AND    MG.ID_NAZIONALITA = TNG.ID_NAZIONALITA (+) " +
      "AND    AA.ID_AZIENDA = DU.EXT_ID_AZIENDA (+) " +
      "AND    DU.EXT_PROVINCIA_UMA = PROV_DU.ISTAT_PROVINCIA (+) " +
      "AND    DU.DATA_ISCRIZIONE (+) <= SYSDATE " +
      "AND    NVL(DU.DATA_CESSAZIONE (+), TO_DATE('31/12/9999','DD/MM/YYYY')) >= SYSDATE " +
      "AND    PM.ID_SCARICO = TS.ID_SCARICO (+) " +
      "AND    PM.ID_TIPO_FORMA_POSSESSO = TFP.ID_TIPO_FORMA_POSSESSO " +
      "AND    MG.ID_TRAZIONE = TT.ID_TRAZIONE (+) ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::getPosessoMacchinaFromId] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idPossessoMacchina);
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        possessoMacchinaVO = new PossessoMacchinaVO();
        possessoMacchinaVO.setIdPossessoMacchina(rs.getLong("ID_POSSESSO_MACCHINA"));
        possessoMacchinaVO.setComunUte(rs.getString("DESCOM"));
        possessoMacchinaVO.setSglProvinciaUte(rs.getString("SIGLA_PROVINCIA"));
        possessoMacchinaVO.setIndirizzoUte(rs.getString("INDIRIZZO"));
        MacchinaGaaVO macchinaGaaVO = new MacchinaGaaVO();
        macchinaGaaVO.setIdMacchina(rs.getLong("ID_MACCHINA"));
        TipoMacchinaGaaVO tipoMacchinaGaaVO = new TipoMacchinaGaaVO();
        tipoMacchinaGaaVO.setDescrizione(rs.getString("DESC_TIPO_MACCHINA"));
        Long idGenereMacchina = new Long(rs.getLong("ID_GENERE_MACCHINA"));
        macchinaGaaVO.setIdGenereMacchina(idGenereMacchina);
        TipoGenereMacchinaGaaVO genereMacchinaGaaVO = new TipoGenereMacchinaGaaVO();
        genereMacchinaGaaVO.setIdGenereMacchina(idGenereMacchina);
        genereMacchinaGaaVO.setDescrizione(rs.getString("DESC_TIPO_GENERE"));
        genereMacchinaGaaVO.setTipoMacchinaVO(tipoMacchinaGaaVO);
        macchinaGaaVO.setGenereMacchinaVO(genereMacchinaGaaVO);
        TipoCategoriaGaaVO tipoCategoriaGaaVO = new TipoCategoriaGaaVO();
        tipoCategoriaGaaVO.setDescrizione(rs.getString("DESC_TIPO_CATEGORIA"));
        tipoCategoriaGaaVO.setFlagControlliUnivocita(rs.getString("FLAG_CONTROLLI_UNIVOCITA"));
        macchinaGaaVO.setTipoCategoriaVO(tipoCategoriaGaaVO);
        macchinaGaaVO.setDescMarca(rs.getString("DESC_TIPO_MARCA"));
        macchinaGaaVO.setModello(rs.getString("TIPO_MACCHINA"));
        macchinaGaaVO.setAnnoCostruzione(checkIntegerNull(rs.getString("ANNO_COSTRUZIONE")));
        macchinaGaaVO.setMatricolaTelaio(rs.getString("MATRICOLA_TELAIO"));
        macchinaGaaVO.setMatricolaMotore(rs.getString("MATRICOLA_MOTORE"));
        NumeroTargaGaaVO numeroTargaGaaVO = new NumeroTargaGaaVO();
        numeroTargaGaaVO.setNumeroTarga(rs.getString("NUMERO_TARGA"));
        macchinaGaaVO.setLastNumeroTargaVO(numeroTargaGaaVO);
        macchinaGaaVO.setCalorie(checkIntegerNull(rs.getString("CALORIE")));
        macchinaGaaVO.setPotenzaCv(checkIntegerNull(rs.getString("POTENZA_CV")));
        macchinaGaaVO.setPotenzaKw(checkIntegerNull(rs.getString("POTENZA_KW")));
        macchinaGaaVO.setDescTipoAlimentazione(rs.getString("DESC_TIPO_ALIMENTAZIONE"));
        macchinaGaaVO.setDescTipoNazionalita(rs.getString("DESC_TIPO_NAZIONALITA"));
        macchinaGaaVO.setLordo(rs.getBigDecimal("LORDO"));
        macchinaGaaVO.setTara(rs.getBigDecimal("TARA"));
        macchinaGaaVO.setNote(rs.getString("NOTE"));
        macchinaGaaVO.setExtIdUtenteAggiornamento(rs.getLong("EXT_ID_UTENTE_AGGIORNAMENTO"));
        macchinaGaaVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        macchinaGaaVO.setIdMarca(checkLongNull(rs.getString("ID_MARCA")));
        macchinaGaaVO.setNumeroOmologazione(rs.getString("NUMERO_OMOLOGAZIONE"));
        macchinaGaaVO.setNumeroAssi(rs.getString("NUMERO_ASSI"));
        macchinaGaaVO.setIlluminazione(rs.getString("ILLUMINAZIONE"));
        macchinaGaaVO.setDescTipoTrazione(rs.getString("DESC_TIPO_TRAZIONE"));
        possessoMacchinaVO.setMacchinaVO(macchinaGaaVO);
        possessoMacchinaVO.setCuaa(rs.getString("CUAA"));
        possessoMacchinaVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        possessoMacchinaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        possessoMacchinaVO.setComunSedeLeg(rs.getString("DESCOM_LEG"));
        possessoMacchinaVO.setIndirizzoSedeLeg(rs.getString("SEDELEG_INDIRIZZO"));
        possessoMacchinaVO.setCapSedeLeg(rs.getString("SEDELEG_CAP"));
        possessoMacchinaVO.setSglProvinciaSedeLeg(rs.getString("SGLPROV_LEG"));
        possessoMacchinaVO.setDittaUma(rs.getString("DITTA_UMA"));
        possessoMacchinaVO.setSiglaProvinciaUma(rs.getString("SGLPROV_UMA"));
        possessoMacchinaVO.setDescTipoScarico(rs.getString("DESC_TIPO_SCARICO"));
        String idTipoFormaPossesso = rs.getString("ID_TIPO_FORMA_POSSESSO");
        if(Validator.isNotEmpty(idTipoFormaPossesso))
        {
          possessoMacchinaVO.setIdTipoFormaPossesso(new Long(idTipoFormaPossesso));
          TipoFormaPossessoGaaVO tipoFormaPossessoGaaVO = new TipoFormaPossessoGaaVO();
          tipoFormaPossessoGaaVO.setIdTipoFormaPossesso(new Long(idTipoFormaPossesso).longValue());
          tipoFormaPossessoGaaVO.setDescrizione(rs.getString("DESC_TIPO_FORMA_POSSESSO"));
          
          possessoMacchinaVO.setTipoFormaPossessoGaaVO(tipoFormaPossessoGaaVO);
          
        }
        
        possessoMacchinaVO.setDataCarico(rs.getTimestamp("DATA_CARICO"));
        possessoMacchinaVO.setDataScarico(rs.getTimestamp("DATA_SCARICO"));
        possessoMacchinaVO.setIdUte(checkLongNull(rs.getString("ID_UTE")));
        possessoMacchinaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
      }
      
      return possessoMacchinaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("possessoMacchinaVO", possessoMacchinaVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idPossessoMacchina", idPossessoMacchina)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::getPosessoMacchinaFromId] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getPosessoMacchinaFromId] END.");
    }
  }
  
  
  
  public Vector<PossessoMacchinaVO> getElencoDitteUtilizzatrici(
      long idMacchina, Date dataScarico) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<PossessoMacchinaVO> vPossessoMacchina = null;
    PossessoMacchinaVO possessoMacchinaVO = null;
    
    try
    {
      SolmrLogger.debug(this,
          "[MacchineAgricoleGaaDAO::getElencoDitteUtilizzatrici] BEGIN.");      

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
     
      queryBuf.append("" +   
        "SELECT  PM.ID_POSSESSO_MACCHINA, " +
        "        PM.ID_MACCHINA, " +
        "        AA.CUAA, " +
        "        AA.PARTITA_IVA, " +
        "        AA.DENOMINAZIONE, " +
        "        DU.DITTA_UMA, " +
        "        PROV.SIGLA_PROVINCIA, " +
        "        FPG.DESCRIZIONE AS DESC_FORMA_POSSESSO, " +
        "        PM.PERCENTUALE_POSSESSO, "+
        "        PM.DATA_CARICO, " +
        "        PM.DATA_SCARICO, " +
        "        TS.DESCRIZIONE AS DESC_TIPO_SCARICO, " +
        "        PM.DATA_INIZIO_VALIDITA, " +
        "        PM.DATA_FINE_VALIDITA " +
        "FROM    DB_POSSESSO_MACCHINA PM, "+
        "        DB_UTE UT, " +
        "        DB_ANAGRAFICA_AZIENDA AA, " +
        "        DB_DITTA_UMA DU, "+
        "        PROVINCIA PROV, " +
        "        DB_TIPO_FORMA_POSSESSO_GAA FPG," +
        "        DB_TIPO_SCARICO TS "+
        "WHERE   PM.ID_MACCHINA = ? " +
        "AND     UT.ID_UTE = PM.ID_UTE " +
        "AND     UT.ID_AZIENDA = AA.ID_AZIENDA " +
        "AND     AA.ID_AZIENDA = DU.EXT_ID_AZIENDA (+) " +
        "AND     DU.EXT_PROVINCIA_UMA = PROV.ISTAT_PROVINCIA (+) " +
        "AND     PM.ID_TIPO_FORMA_POSSESSO = FPG.ID_TIPO_FORMA_POSSESSO " +
        "AND     PM.ID_SCARICO = TS.ID_SCARICO (+) " +
        "AND     PM.DATA_FINE_VALIDITA IS NULL ");
        
     if(Validator.isNotEmpty(dataScarico))
     {
       queryBuf.append("" +
       //"AND      PM.DATA_CARICO <= ? " +
       //"AND      NVL(PM.DATA_SCARICO, TO_DATE('31/12/9999','DD/MM/YYYY')) >= ? " +
       "AND      AA.DATA_FINE_VALIDITA IS NULL " +
       "AND      DU.DATA_ISCRIZIONE (+) <= ? " +
       "AND      NVL(DU.DATA_CESSAZIONE (+), TO_DATE('31/12/9999','DD/MM/YYYY')) >= ? ");    
     }
     else
     {
       queryBuf.append("" +
       //"AND      PM.DATA_FINE_VALIDITA IS NULL " +
       //"AND      PM.DATA_SCARICO IS NULL " +
       "AND      AA.DATA_FINE_VALIDITA IS NULL " +
       "AND      DU.DATA_ISCRIZIONE (+) <= SYSDATE " +
       "AND      NVL(DU.DATA_CESSAZIONE (+), TO_DATE('31/12/9999','DD/MM/YYYY')) >= SYSDATE ");
     }
     queryBuf.append("" +   
       "ORDER BY AA.DENOMINAZIONE ");
         
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      //conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::getElencoDitteUtilizzatrici] Query="
                + query);
      }
      conn = getDatasource().getConnection();
      
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idMacchina);
      if(Validator.isNotEmpty(dataScarico))
      {
        //stmt.setTimestamp(++indice, convertDateToTimestamp(dataScarico));
        //stmt.setTimestamp(++indice, convertDateToTimestamp(dataScarico));
        //stmt.setTimestamp(++indice, convertDateToTimestamp(dataScarico));
        //stmt.setTimestamp(++indice, convertDateToTimestamp(dataScarico));
        stmt.setTimestamp(++indice, convertDateToTimestamp(dataScarico));
        stmt.setTimestamp(++indice, convertDateToTimestamp(dataScarico));
      }
      
      
      ResultSet rs = stmt.executeQuery();
     
      
      while (rs.next())
      {
        if(vPossessoMacchina == null)
          vPossessoMacchina = new Vector<PossessoMacchinaVO>();
        
        possessoMacchinaVO = new PossessoMacchinaVO();
        possessoMacchinaVO.setIdPossessoMacchina(new Long(rs.getLong("ID_POSSESSO_MACCHINA")));
        MacchinaGaaVO macchinaGaaVO = new MacchinaGaaVO();
        macchinaGaaVO.setIdMacchina(rs.getLong("ID_MACCHINA"));        
        possessoMacchinaVO.setMacchinaVO(macchinaGaaVO);
        possessoMacchinaVO.setCuaa(rs.getString("CUAA"));
        possessoMacchinaVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        possessoMacchinaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        possessoMacchinaVO.setDittaUma(rs.getString("DITTA_UMA"));
        possessoMacchinaVO.setSiglaProvinciaUma(rs.getString("SIGLA_PROVINCIA"));
        possessoMacchinaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        TipoFormaPossessoGaaVO tipoFormaPossessoGaaVO = new TipoFormaPossessoGaaVO(); 
        tipoFormaPossessoGaaVO.setDescrizione(rs.getString("DESC_FORMA_POSSESSO"));
        possessoMacchinaVO.setTipoFormaPossessoGaaVO(tipoFormaPossessoGaaVO);
        possessoMacchinaVO.setDataCarico(rs.getTimestamp("DATA_CARICO"));
        possessoMacchinaVO.setDataScarico(rs.getTimestamp("DATA_SCARICO"));
        possessoMacchinaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        possessoMacchinaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        possessoMacchinaVO.setDescTipoScarico(rs.getString("DESC_TIPO_SCARICO"));
        
        
        vPossessoMacchina.add(possessoMacchinaVO);
      }
      
      return vPossessoMacchina;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vPossessoMacchina", vPossessoMacchina),
          new Variabile("possessoMacchinaVO", possessoMacchinaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idMacchina", idMacchina),
        new Parametro("dataScarico", dataScarico) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[MacchineAgricoleGaaDAO::getElencoDitteUtilizzatrici] ", t,
          query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[MacchineAgricoleGaaDAO::getElencoDitteUtilizzatrici] END.");
    }
  }
  
  
  
  
  public Vector<PossessoMacchinaVO> getElencoPossessoDitteUtilizzatrici(
      long idMacchina, long idAzienda) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<PossessoMacchinaVO> vPossessoMacchina = null;
    PossessoMacchinaVO possessoMacchinaVO = null;
    
    try
    {
      SolmrLogger.debug(this,
          "[MacchineAgricoleGaaDAO::getElencoPossessoDitteUtilizzatrici] BEGIN.");      

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
     
      queryBuf.append("" +   
        "SELECT  PM.ID_POSSESSO_MACCHINA, " +
        "        PM.ID_MACCHINA, " +
        "        AA.DENOMINAZIONE, " +
        "        FPG.DESCRIZIONE AS DESC_FORMA_POSSESSO, " +
        "        PM.PERCENTUALE_POSSESSO, "+
        "        PM.DATA_SCADENZA_LEASING, " +
        "        PM.DATA_INIZIO_VALIDITA, " +
        "        PM.DATA_FINE_VALIDITA " +
        "FROM    DB_POSSESSO_MACCHINA PM, "+
        "        DB_UTE UT, " +
        "        DB_ANAGRAFICA_AZIENDA AA, " +
        "        DB_TIPO_FORMA_POSSESSO_GAA FPG " +
        "WHERE   PM.ID_MACCHINA = ? " +
        "AND     UT.ID_AZIENDA = ? " +
        "AND     UT.ID_UTE = PM.ID_UTE " +
        "AND     PM.ID_AZIENDA_LEASING = AA.ID_AZIENDA (+) " +
        "AND     AA.DATA_FINE_VALIDITA (+) IS NULL " +
        "AND     PM.ID_TIPO_FORMA_POSSESSO = FPG.ID_TIPO_FORMA_POSSESSO " +
        "ORDER BY PM.DATA_INIZIO_VALIDITA ");
         
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      //conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::getElencoPossessoDitteUtilizzatrici] Query="
                + query);
      }
      conn = getDatasource().getConnection();
      
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idMacchina);
      stmt.setLong(++indice, idAzienda);
      
      ResultSet rs = stmt.executeQuery();
     
      
      while (rs.next())
      {
        if(vPossessoMacchina == null)
          vPossessoMacchina = new Vector<PossessoMacchinaVO>();
        
        possessoMacchinaVO = new PossessoMacchinaVO();
        possessoMacchinaVO.setIdPossessoMacchina(new Long(rs.getLong("ID_POSSESSO_MACCHINA")));
        MacchinaGaaVO macchinaGaaVO = new MacchinaGaaVO();
        macchinaGaaVO.setIdMacchina(rs.getLong("ID_MACCHINA"));        
        possessoMacchinaVO.setMacchinaVO(macchinaGaaVO);
        possessoMacchinaVO.setDenominazioneAzLeasing(rs.getString("DENOMINAZIONE"));
        possessoMacchinaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        TipoFormaPossessoGaaVO tipoFormaPossessoGaaVO = new TipoFormaPossessoGaaVO(); 
        tipoFormaPossessoGaaVO.setDescrizione(rs.getString("DESC_FORMA_POSSESSO"));
        possessoMacchinaVO.setTipoFormaPossessoGaaVO(tipoFormaPossessoGaaVO);
        possessoMacchinaVO.setDataScadenzaLeasing(rs.getTimestamp("DATA_SCADENZA_LEASING"));
        possessoMacchinaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        possessoMacchinaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        
        
        vPossessoMacchina.add(possessoMacchinaVO);
      }
      
      return vPossessoMacchina;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vPossessoMacchina", vPossessoMacchina),
          new Variabile("possessoMacchinaVO", possessoMacchinaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idMacchina", idMacchina),
        new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[MacchineAgricoleGaaDAO::getElencoPossessoDitteUtilizzatrici] ", t,
          query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[MacchineAgricoleGaaDAO::getElencoPossessoDitteUtilizzatrici] END.");
    }
  }
  
  
  
  public Vector<TipoMacchinaGaaVO> getElencoTipoMacchina(String flagIrroratrice) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoMacchinaGaaVO> vTipoMacchina = null;
    TipoMacchinaGaaVO tipoMacchinaGaaVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoMacchina] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT TM.ID_TIPO_MACCHINA, " +
        "       TM.CODICE_TIPO_MACCHINA," +
        "       TM.DESCRIZIONE, " +
        "       TM.FLAG_IRRORATRICE " +
        "FROM   DB_TIPO_MACCHINA TM " +
        "WHERE  TM.DATA_FINE_VALIDITA IS NULL ");
      if(Validator.isNotEmpty(flagIrroratrice))
      {
        queryBuf.append("" +
        "AND    TM.FLAG_IRRORATRICE = ? ");
        
      }
      queryBuf.append("" +
      "ORDER BY TM.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::getElencoTipoMacchina] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      if(Validator.isNotEmpty(flagIrroratrice))
      {
        stmt.setString(++indice, flagIrroratrice);
      }   
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoMacchina == null)
          vTipoMacchina = new Vector<TipoMacchinaGaaVO>();
        
        tipoMacchinaGaaVO = new TipoMacchinaGaaVO();
        tipoMacchinaGaaVO.setIdTipoMacchina(rs.getLong("ID_TIPO_MACCHINA"));
        tipoMacchinaGaaVO.setCodiceTipoMacchina(rs.getString("CODICE_TIPO_MACCHINA"));
        tipoMacchinaGaaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoMacchinaGaaVO.setFlagIrroratrice(rs.getString("FLAG_IRRORATRICE"));
        
        
        vTipoMacchina.add(tipoMacchinaGaaVO);
      }
      
      return vTipoMacchina;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoMacchina", vTipoMacchina),
          new Variabile("tipoMacchinaGaaVO", tipoMacchinaGaaVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("flagIrroratrice", flagIrroratrice)  };
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoMacchina] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoMacchina] END.");
    }
  }
  
  
  
  public Vector<TipoGenereMacchinaGaaVO> getElencoTipoGenereMacchinaFromRuolo(
      long idTipoMacchina, String codiceRuolo) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoGenereMacchinaGaaVO> vTipoGenere = null;
    TipoGenereMacchinaGaaVO tipoGenereMacchinaGaaVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoGenereMacchinaFromRuolo] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      "SELECT TGM.ID_GENERE_MACCHINA, " +
      "       TGM.ID_TIPO_MACCHINA, " +
      "       TGM.CODICE_GENERE," +
      "       TGM.DESCRIZIONE, " +
      "       TGM.FLAG_MATRICE, " +
      "       TGM.FLAG_IMPORTABILE " + 
      "FROM   DB_TIPO_GENERE_MACCHINA_GAA TGM, " +
      "       DB_R_GENERE_MACCHINA_RUOLO RGM " +
      "WHERE  TGM.ID_TIPO_MACCHINA = ? " +
      "AND    TGM.DATA_FINE_VALIDITA IS NULL " +
      "AND    TGM.ID_GENERE_MACCHINA = RGM.ID_GENERE_MACCHINA " +
      "AND    RGM.CODICE_RUOLO = ? " +
      "ORDER BY TGM.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::getElencoTipoGenereMacchinaFromRuolo] Query="
                + query);
      }
      
      
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idTipoMacchina);
      stmt.setString(++indice, codiceRuolo);
      
      
         
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoGenere == null)
          vTipoGenere = new Vector<TipoGenereMacchinaGaaVO>();
        
        tipoGenereMacchinaGaaVO = new TipoGenereMacchinaGaaVO();
        tipoGenereMacchinaGaaVO.setIdGenereMacchina(rs.getLong("ID_GENERE_MACCHINA"));
        tipoGenereMacchinaGaaVO.setIdTipoMacchina(rs.getLong("ID_TIPO_MACCHINA"));
        tipoGenereMacchinaGaaVO.setCodiceGenere(rs.getString("CODICE_GENERE"));
        tipoGenereMacchinaGaaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoGenereMacchinaGaaVO.setFlagImportabile(rs.getString("FLAG_IMPORTABILE"));
        tipoGenereMacchinaGaaVO.setFlagMatrice(rs.getString("FLAG_MATRICE"));
        
        
        vTipoGenere.add(tipoGenereMacchinaGaaVO);
      }
      
      return vTipoGenere;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoGenere", vTipoGenere),
          new Variabile("tipoGenereMacchinaGaaVO", tipoGenereMacchinaGaaVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoMacchina", idTipoMacchina), 
        new Parametro("codiceRuolo", codiceRuolo) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoGenereMacchinaFromRuolo] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoGenereMacchinaFromRuolo] END.");
    }
  }
  
  
  public Vector<TipoCategoriaGaaVO> getElencoTipoCategoria(long idGenereMacchina) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoCategoriaGaaVO> vTipoCategoria = null;
    TipoCategoriaGaaVO tipoCategoriaGaaVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoCategoria] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      "SELECT TC.ID_CATEGORIA, " +
      "       TC.ID_GENERE_MACCHINA, " +
      "       TC.CODICE_CATEGORIA," +
      "       TC.DESCRIZIONE " + 
      "FROM   DB_TIPO_CATEGORIA_GAA TC " +
      "WHERE  TC.ID_GENERE_MACCHINA = ? " +
      "AND    TC.DATA_FINE_VALIDITA IS NULL " +
      "AND    TC.FLAG_INSERIBILE_ANAG = 'S' " +
      "ORDER BY TC.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::getElencoTipoCategoria] Query="
                + query);
      }
      
      
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idGenereMacchina);     
      
         
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoCategoria == null)
          vTipoCategoria = new Vector<TipoCategoriaGaaVO>();
        
        tipoCategoriaGaaVO = new TipoCategoriaGaaVO();
        tipoCategoriaGaaVO.setIdCategoria(rs.getLong("ID_CATEGORIA"));
        tipoCategoriaGaaVO.setIdGenereMacchina(rs.getLong("ID_GENERE_MACCHINA"));
        tipoCategoriaGaaVO.setCodiceCategoria(rs.getString("CODICE_CATEGORIA"));
        tipoCategoriaGaaVO.setDescrizione(rs.getString("DESCRIZIONE"));
               
        vTipoCategoria.add(tipoCategoriaGaaVO);
      }
      
      return vTipoCategoria;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoCategoria", vTipoCategoria),
          new Variabile("tipoCategoriaGaaVO", tipoCategoriaGaaVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idGenereMacchina", idGenereMacchina) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoCategoria] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoCategoria] END.");
    }
  }
  
  
  public Vector<CodeDescription> getElencoTipoMarca() 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<CodeDescription> vTipoMarca = null;
    CodeDescription tipoMarca = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoMarca] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      "SELECT TM.ID_MARCA, " +
      "       TM.DESCRIZIONE " + 
      "FROM   DB_TIPO_MARCA_GAA TM " +
      "WHERE  TM.FLAG_PRINCIPALE = 'S' " +
      "ORDER BY TM.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::getElencoTipoMarca] Query="
                + query);
      }
      
      
      stmt = conn.prepareStatement(query);     
         
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoMarca == null)
          vTipoMarca = new Vector<CodeDescription>();
        
        tipoMarca = new CodeDescription();
        tipoMarca.setCode(new Integer(rs.getInt("ID_MARCA")));
        tipoMarca.setDescription(rs.getString("DESCRIZIONE"));
               
        vTipoMarca.add(tipoMarca);
      }
      
      return vTipoMarca;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoMarca", vTipoMarca),
          new Variabile("tipoMarca", tipoMarca) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoMarca] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoMarca] END.");
    }
  }
  
  public Vector<CodeDescription> getElencoTipoMarcaByIdGenere(long idGenereMacchina) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<CodeDescription> vTipoMarca = null;
    CodeDescription tipoMarca = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoMarcaByIdGenere] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      "SELECT TM.ID_MARCA, " +
      "       TM.DESCRIZIONE " + 
      "FROM   DB_TIPO_MARCA_GAA TM, " +
      "       DB_R_MARCA_GENERE_MACCHINA MGM " +
      "WHERE  TM.FLAG_PRINCIPALE = 'S' " +
      "AND    TM.ID_MARCA = MGM.ID_MARCA " +
      "AND    MGM.ID_GENERE_MACCHINA = ? " +
      "ORDER BY TM.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::getElencoTipoMarcaByIdGenere] Query="
                + query);
      }
      
      
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idGenereMacchina);
      
         
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoMarca == null)
          vTipoMarca = new Vector<CodeDescription>();
        
        tipoMarca = new CodeDescription();
        tipoMarca.setCode(new Integer(rs.getInt("ID_MARCA")));
        tipoMarca.setDescription(rs.getString("DESCRIZIONE"));
               
        vTipoMarca.add(tipoMarca);
      }
      
      return vTipoMarca;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoMarca", vTipoMarca),
          new Variabile("tipoMarca", tipoMarca) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  new Parametro("idGenereMacchina", idGenereMacchina)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoMarcaByIdGenere] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoMarcaByIdGenere] END.");
    }
  }
  
  public Vector<TipoFormaPossessoGaaVO> getElencoTipoFormaPossesso() 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoFormaPossessoGaaVO> vTipoFormaPossesso = null;
    TipoFormaPossessoGaaVO tipoFormaPossessoGaaVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoFormaPossesso] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      "SELECT TF.ID_TIPO_FORMA_POSSESSO, " +
      "       TF.DESCRIZIONE, " +
      "       TF.ABILITA_PERCENTUALE_POSSESSO " + 
      "FROM   DB_TIPO_FORMA_POSSESSO_GAA TF " +
      "WHERE  TF.DATA_FINE_VALIDITA IS NULL " +
      "ORDER BY TF.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::getElencoTipoFormaPossesso] Query="
                + query);
      }
      
      
      stmt = conn.prepareStatement(query);     
         
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoFormaPossesso == null)
          vTipoFormaPossesso = new Vector<TipoFormaPossessoGaaVO>();
        
        tipoFormaPossessoGaaVO = new TipoFormaPossessoGaaVO();
        tipoFormaPossessoGaaVO.setIdTipoFormaPossesso(rs.getLong("ID_TIPO_FORMA_POSSESSO"));
        tipoFormaPossessoGaaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoFormaPossessoGaaVO.setAbilitaPercentualePossesso(rs.getString("ABILITA_PERCENTUALE_POSSESSO"));       
        
        vTipoFormaPossesso.add(tipoFormaPossessoGaaVO);
      }
      
      return vTipoFormaPossesso;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoFormaPossesso", vTipoFormaPossesso),
          new Variabile("tipoFormaPossessoGaaVO", tipoFormaPossessoGaaVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoFormaPossesso] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoFormaPossesso] END.");
    }
  }
  
  /**
   * almneo uno tra idMarca, modello e annoCotruzione sarà sempre valorizzato!!!
   * 
   * 
   * @param idAzienda
   * @param idMarca
   * @param modello
   * @param annoCostruzione
   * @param matricolaTelaio
   * @return
   * @throws DataAccessException
   */
  public boolean existsMotoreAgricolo(Long idMarca, String modello,
      Integer annoCostruzione, String matricolaTelaio) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean trovato = false;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::existsMotoreAgricolo] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT PM.ID_POSSESSO_MACCHINA " +
        "FROM   DB_POSSESSO_MACCHINA PM, " +
        "       DB_MACCHINA_GAA MG " +
        "WHERE  PM.ID_MACCHINA = MG.ID_MACCHINA ");
      if(Validator.isNotEmpty(idMarca))
      {
        queryBuf.append("" +
        "AND    MG.ID_MARCA = ? ");
      }
      if(Validator.isNotEmpty(modello))
      {
        queryBuf.append("" +
        "AND    MG.TIPO_MACCHINA = ? ");
      }
      if(Validator.isNotEmpty(annoCostruzione))
      {
        queryBuf.append("" +
        "AND    MG.ANNO_COSTRUZIONE = ? ");
      }
      if(Validator.isNotEmpty(matricolaTelaio))
      {
        queryBuf.append("" +
        "AND    MG.MATRICOLA_TELAIO = ? ");
      }
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::existsMotoreAgricolo] Query="
                + query);
      }
      
      
      stmt = conn.prepareStatement(query);
      int indice = 0;
      
      if(Validator.isNotEmpty(idMarca))
      {
        stmt.setLong(++indice, idMarca.longValue());
      }
      if(Validator.isNotEmpty(modello))
      {
        stmt.setString(++indice, modello);
      }
      if(Validator.isNotEmpty(annoCostruzione))
      {
        stmt.setInt(++indice, annoCostruzione.intValue());
      }
      if(Validator.isNotEmpty(matricolaTelaio))
      {
        stmt.setString(++indice, matricolaTelaio);
      }
         
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        trovato = true;
      }
      
      return trovato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("idMarca", idMarca),
          new Variabile("modello", modello),
          new Variabile("annoCostruzione", annoCostruzione),
          new Variabile("matricolaTelaio", matricolaTelaio)   
       };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  new Parametro("trovato", trovato) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::existsMotoreAgricolo] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::existsMotoreAgricolo] END.");
    }
  }
  
  
  public Long insertMacchinaGaa(MacchinaGaaVO macchinaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idMacchina = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::insertMacchinaGaa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idMacchina = getNextPrimaryKey(SolmrConstants.SEQ_DB_MACCHINA);
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "INSERT INTO DB_MACCHINA_GAA (" + 
        "     ID_MACCHINA, " +
        "     ID_GENERE_MACCHINA, " + 
        "     ID_CATEGORIA, " +
        "     ID_MARCA, " +
        "     TIPO_MACCHINA, " +
        "     TARA, " +
        "     LORDO," +
        "     NUMERO_ASSI," +
        "     ILLUMINAZIONE," +
        "     NUMERO_OMOLOGAZIONE, " +
        "     CONSUMO_ORARIO, " +
        "     CALORIE," +
        "     POTENZA_CV, " +
        "     POTENZA_KW, " +
        "     ID_ALIMENTAZIONE," +
        "     ID_NAZIONALITA," +
        "     ID_TRAZIONE," +
        "     MATRICOLA_TELAIO," +
        "     MATRICOLA_MOTORE," +
        "     NUMERO_MATRICE," +
        "     EXT_ID_UTENTE_AGGIORNAMENTO," +
        "     DATA_AGGIORNAMENTO," +
        "     EXT_ID_MACCHINA_UMA," +
        "     EXT_ID_MATRICE_UMA," +
        "     ANNO_COSTRUZIONE," +
        "     NOTE)  " +
        "   VALUES" +
        "   (?,?,?,?,?,?,?,?,?,?," +
        "    ?,?,?,?,?,?,?,?,?,?," +
        "    ?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::insertMacchinaGaa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idMacchina.longValue());
      stmt.setLong(++indice, macchinaVO.getIdGenereMacchina());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(macchinaVO.getIdCategoria()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(macchinaVO.getIdMarca()));
      stmt.setString(++indice, macchinaVO.getModello());
      stmt.setBigDecimal(++indice, macchinaVO.getTara());
      stmt.setBigDecimal(++indice, macchinaVO.getLordo());
      stmt.setBigDecimal(++indice, null);
      stmt.setBigDecimal(++indice, null);
      stmt.setString(++indice, null);
      stmt.setString(++indice, null);
      stmt.setString(++indice, null);
      stmt.setString(++indice, null);
      stmt.setString(++indice, null);
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(macchinaVO.getIdAlimentazione()));
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(macchinaVO.getIdNazionalita()));
      stmt.setString(++indice, null);
      stmt.setString(++indice, macchinaVO.getMatricolaTelaio());
      stmt.setString(++indice, macchinaVO.getMatricolaMotore());
      stmt.setString(++indice, null);
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(macchinaVO.getExtIdUtenteAggiornamento()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));
      stmt.setBigDecimal(++indice, null);
      stmt.setString(++indice, null);
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(macchinaVO.getAnnoCostruzione()));
      stmt.setString(++indice, macchinaVO.getNote());
      
      stmt.executeUpdate();
      
      return idMacchina;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idMacchina", idMacchina)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("macchinaVO", macchinaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaDAO::insertMacchinaGaa] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::insertMacchinaGaa] END.");
    }
  }
  
  
  public void storicizzaPossessoMacchineAzienda(long idAzienda, long idUtente) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::storicizzaPossessoMacchineAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "UPDATE DB_POSSESSO_MACCHINA   " + 
        "SET    DATA_SCARICO = SYSDATE, " +
        "       ID_SCARICO = 101, " +
        "       DATA_AGGIORNAMENTO = SYSDATE, " +
        "       EXT_ID_UTENTE_AGGIORNAMENTO = ? " +
        "WHERE  ID_POSSESSO_MACCHINA IN (SELECT PM.ID_POSSESSO_MACCHINA " +
        "                                FROM   DB_POSSESSO_MACCHINA PM, " +
        "                                       DB_UTE UT " +
        "                                WHERE  UT.ID_AZIENDA = ? " +
        "                                AND    UT.ID_UTE = PM.ID_UTE " +
        "                                AND    PM.DATA_SCARICO IS NULL" +
        "                                AND    PM.DATA_FINE_VALIDITA IS NULL) ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::storicizzaPossessoMacchineAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idUtente);
      stmt.setLong(++indice, idAzienda);
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUtente", idUtente),
        new Parametro("idAzienda", idAzienda) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaDAO::storicizzaPossessoMacchineAzienda] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::storicizzaPossessoMacchineAzienda] END.");
    }
  }
  
  
  public boolean isMacchinaModificabile(long idPossessoMacchina, String codiceRuolo) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean trovato = false;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::isMacchinaModificabile] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT PM.ID_POSSESSO_MACCHINA " +
        "FROM   DB_POSSESSO_MACCHINA PM, " +
        "       DB_R_GENERE_MACCHINA_RUOLO RMR, " +
        "       DB_TIPO_GENERE_MACCHINA_GAA GMA," +
        "       DB_MACCHINA_GAA MG " +
        "WHERE  PM.ID_POSSESSO_MACCHINA = ? " +
        "AND    PM.ID_MACCHINA = MG.ID_MACCHINA " +
        "AND    MG.ID_GENERE_MACCHINA = GMA.ID_GENERE_MACCHINA " +
        "AND    GMA.FLAG_IMPORTABILE = 'N' " +
        "AND    PM.DATA_SCARICO IS NULL " +
        "AND    PM.DATA_FINE_VALIDITA IS NULL " +
        "AND    MG.ID_GENERE_MACCHINA = RMR.ID_GENERE_MACCHINA " +
        "AND    RMR.CODICE_RUOLO = ? ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::isMacchinaModificabile] Query="
                + query);
      }
      
      
      stmt = conn.prepareStatement(query);
      int indice = 0;
      
      stmt.setLong(++indice, idPossessoMacchina);
      stmt.setString(++indice, codiceRuolo);
      
         
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        trovato = true;
      }
      
      return trovato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("idPossessoMacchina", idPossessoMacchina),
          new Variabile("codiceRuolo", codiceRuolo)   
       };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  new Parametro("trovato", trovato) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::isMacchinaModificabile] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::isMacchinaModificabile] END.");
    }
  }
  
  /**
   * 
   * dice se la macchina è o è stata in possesso a più aziende...
   * 
   * 
   * 
   * @param idMacchina
   * @return
   * @throws DataAccessException
   */
  public boolean isMacchinaPossMultiplo(long idMacchina) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean trovato = false;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::isMacchinaPossMultiplo] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT COUNT(*) AS NUM_POSS " +
        "FROM   DB_POSSESSO_MACCHINA PM " +
        "WHERE  PM.ID_MACCHINA = ? " +
        "GROUP BY PM.ID_MACCHINA ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::isMacchinaPossMultiplo] Query="
                + query);
      }
      
      
      stmt = conn.prepareStatement(query);
      int indice = 0;
      
      stmt.setLong(++indice, idMacchina);
      
         
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        int numPoss = rs.getInt("NUM_POSS");
        
        if(numPoss > 1)
          trovato = true;
      }
      
      return trovato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("idMacchina", idMacchina)
       };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  new Parametro("trovato", trovato) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::isMacchinaPossMultiplo] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::isMacchinaPossMultiplo] END.");
    }
  }
  
  
  public Vector<CodeDescription> getElencoTipoScarico() 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<CodeDescription> vTipoScarico = null;
    CodeDescription tipoScarico = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoScarico] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      "SELECT TS.ID_SCARICO, " +
      "       TS.DESCRIZIONE " + 
      "FROM   DB_TIPO_SCARICO TS " +
      "ORDER BY TS.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::getElencoTipoScarico] Query="
                + query);
      }
      
      
      stmt = conn.prepareStatement(query);     
         
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoScarico == null)
          vTipoScarico = new Vector<CodeDescription>();
        
        tipoScarico = new CodeDescription();
        tipoScarico.setCode(new Integer(rs.getInt("ID_SCARICO")));
        tipoScarico.setDescription(rs.getString("DESCRIZIONE"));
               
        vTipoScarico.add(tipoScarico);
      }
      
      return vTipoScarico;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoScarico", vTipoScarico),
          new Variabile("tipoScarico", tipoScarico) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoScarico] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getElencoTipoScarico] END.");
    }
  }
  
  
  public void updateMacchinaGaa(MacchinaGaaVO macchinaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::updateMacchinaGaa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "UPDATE DB_MACCHINA_GAA   " + 
        "SET    TIPO_MACCHINA = ?, " +
        "       ANNO_COSTRUZIONE = ?, " +
        "       ID_MARCA = ?, " +
        "       MATRICOLA_TELAIO = ?," +
        "       NOTE = ?, " +
        "       DATA_AGGIORNAMENTO = SYSDATE, " +
        "       EXT_ID_UTENTE_AGGIORNAMENTO = ? " +
        "WHERE  ID_MACCHINA  = ? ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::updateMacchinaGaa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setString(++indice, macchinaVO.getModello());
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(macchinaVO.getAnnoCostruzione()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(macchinaVO.getIdMarca()));
      stmt.setString(++indice, macchinaVO.getMatricolaTelaio());
      stmt.setString(++indice, macchinaVO.getNote());
      stmt.setLong(++indice, macchinaVO.getExtIdUtenteAggiornamento());
      stmt.setLong(++indice, macchinaVO.getIdMacchina());
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("macchinaVO", macchinaVO) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaDAO::updateMacchinaGaa] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::updateMacchinaGaa] END.");
    }
  }
  
  
  public void storicizzaPossessoMacchinaPerScarico(long idPossessoMacchina, 
      Date dataScarico, long idScarico, long idUtente) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::storicizzaPossessoMacchinaPerScarico] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "UPDATE DB_POSSESSO_MACCHINA   " + 
        "SET    DATA_SCARICO = ?, " +
        "       ID_SCARICO = ?, " +
        "       DATA_AGGIORNAMENTO = SYSDATE, " +
        "       EXT_ID_UTENTE_AGGIORNAMENTO = ? " +
        "WHERE  ID_POSSESSO_MACCHINA = ? ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::storicizzaPossessoMacchinaPerScarico] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setTimestamp(++indice, convertDateToTimestamp(dataScarico));
      stmt.setLong(++indice, idScarico);
      stmt.setLong(++indice, idUtente);
      stmt.setLong(++indice, idPossessoMacchina);
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUtente", idUtente),
        new Parametro("idPossessoMacchina", idPossessoMacchina),
        new Parametro("dataScarico", dataScarico),
        new Parametro("idScarico", idScarico)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaDAO::storicizzaPossessoMacchinaPerScarico] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::storicizzaPossessoMacchinaPerScarico] END.");
    }
  }
  
  
  public void storicizzaPossessoMacchina(long idPossessoMacchina) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::storicizzaPossessoMacchina] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "UPDATE DB_POSSESSO_MACCHINA   " + 
        "SET    DATA_FINE_VALIDITA = SYSDATE " +
        "WHERE  ID_POSSESSO_MACCHINA = ? ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::storicizzaPossessoMacchina] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idPossessoMacchina);
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idPossessoMacchina", idPossessoMacchina) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaDAO::storicizzaPossessoMacchina] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::storicizzaPossessoMacchina] END.");
    }
  }
  
  
  
  
  public boolean isMacchinaGiaPossesso(long idMacchina, long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean trovato = false;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::isMacchinaGiaPossesso] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT PM.ID_POSSESSO_MACCHINA " +
        "FROM   DB_POSSESSO_MACCHINA PM, " +
        "       DB_UTE UT " +
        "WHERE  PM.ID_MACCHINA = ? " +
        "AND    PM.ID_UTE = UT.ID_UTE " +
        "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
        "AND    UT.ID_AZIENDA = ? " +
        "AND    PM.DATA_SCARICO IS NULL " +
        "AND    PM.DATA_FINE_VALIDITA IS NULL ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::isMacchinaGiaPossesso] Query="
                + query);
      }
      
      
      stmt = conn.prepareStatement(query);
      int indice = 0;
      
      stmt.setLong(++indice, idMacchina);
      stmt.setLong(++indice, idAzienda);
         
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        trovato = true;
      }
      
      return trovato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("idMacchina", idMacchina), 
          new Variabile("idAzienda", idAzienda)
       };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  new Parametro("trovato", trovato) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::isMacchinaGiaPossesso] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::isMacchinaGiaPossesso] END.");
    }
  }
  
  /**
   * 
   * estrae la percentuale possesso già detenuta della macchina..
   * 
   * 
   * 
   * @param idMacchina
   * @return
   * @throws DataAccessException
   */
  public BigDecimal percMacchinaGiaInPossesso(long idMacchina) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    BigDecimal percPoss = new BigDecimal(0);
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::percMacchinaGiaInPossesso] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT SUM(PM.PERCENTUALE_POSSESSO) AS PERC_POSS " +
        "FROM   DB_POSSESSO_MACCHINA PM " +
        "WHERE  PM.ID_MACCHINA = ? " +
        "AND    PM.DATA_SCARICO IS NULL " +
        "AND    PM.DATA_FINE_VALIDITA IS NULL " +
        "GROUP BY PM.ID_MACCHINA ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::percMacchinaGiaInPossesso] Query="
                + query);
      }
      
      
      stmt = conn.prepareStatement(query);
      int indice = 0;
      
      stmt.setLong(++indice, idMacchina);
         
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        percPoss = percPoss.add(rs.getBigDecimal("PERC_POSS"));
      }
      
      return percPoss;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("idMacchina", idMacchina)
       };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  new Parametro("percPoss", percPoss) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::percMacchinaGiaInPossesso] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::percMacchinaGiaInPossesso] END.");
    }
  }
  
  
  public TipoCategoriaGaaVO getTipoCategoriaFromPK(long idCategoria) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    TipoCategoriaGaaVO tipoCategoriaGaaVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getTipoCategoriaFromPK] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      "SELECT TC.ID_CATEGORIA, " +
      "       TC.ID_GENERE_MACCHINA, " +
      "       TC.CODICE_CATEGORIA," +
      "       TC.DESCRIZIONE, " +
      "       TC.FLAG_CONTROLLI_UNIVOCITA " + 
      "FROM   DB_TIPO_CATEGORIA_GAA TC " +
      "WHERE  TC.ID_CATEGORIA = ? ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaGaaDAO::getTipoCategoriaFromPK] Query="
                + query);
      }
      
      
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idCategoria);     
      
         
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {        
        tipoCategoriaGaaVO = new TipoCategoriaGaaVO();
        tipoCategoriaGaaVO.setIdCategoria(rs.getLong("ID_CATEGORIA"));
        tipoCategoriaGaaVO.setIdGenereMacchina(rs.getLong("ID_GENERE_MACCHINA"));
        tipoCategoriaGaaVO.setCodiceCategoria(rs.getString("CODICE_CATEGORIA"));
        tipoCategoriaGaaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoCategoriaGaaVO.setFlagControlliUnivocita(rs.getString("FLAG_CONTROLLI_UNIVOCITA"));       
        
      }
      
      return tipoCategoriaGaaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("tipoCategoriaGaaVO", tipoCategoriaGaaVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idCategoria", idCategoria) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaGaaDAO::getTipoCategoriaFromPK] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaGaaDAO::getTipoCategoriaFromPK] END.");
    }
  }
  
  public Long insertTipoMarcaFromUma(MatriceVO matriceVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger.debug(this, "[MacchineAgricoleGaaDAO::insertTipoMarcaFromUma] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "INSERT INTO DB_TIPO_MARCA_GAA (" + 
        "     ID_MARCA, " +
        "     DESCRIZIONE," +
        "     MATRICE, " +
        "     FLAG_PRINCIPALE)  " +
        "   VALUES" +
        "   (?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[MacchineAgricoleGaaDAO::insertTipoMarcaFromUma] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, matriceVO.getIdMarcaLong());
      stmt.setString(++indice, matriceVO.getDescMarca());
      stmt.setString(++indice, matriceVO.getMatriceMarca());
      stmt.setString(++indice, "S");
      
      stmt.executeUpdate();
      
      return matriceVO.getIdMarcaLong();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("matriceVO.getIdMarcaLong()", matriceVO.getIdMarcaLong())};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("matriceVO", matriceVO) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[MacchineAgricoleGaaDAO::insertTipoMarcaFromUma] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[MacchineAgricoleGaaDAO::insertTipoMarcaFromUma] END.");
    }
  }
  
  
    
  
  
  
  
}
