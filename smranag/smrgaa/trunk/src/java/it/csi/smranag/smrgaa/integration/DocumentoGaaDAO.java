package it.csi.smranag.smrgaa.integration;

import it.csi.smranag.smrgaa.dto.AllegatoDichiarazioneVO;
import it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO;
import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.TipoFirmaVO;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.terreni.FaseRiesameDocumentoVO;
import it.csi.smranag.smrgaa.dto.terreni.IstanzaRiesameAziendaVO;
import it.csi.smranag.smrgaa.dto.terreni.IstanzaRiesameVO;
import it.csi.smranag.smrgaa.dto.terreni.ParticellaIstanzaRiesameVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.DocumentoConduzioneVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.TipoCategoriaDocumentoVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

public class DocumentoGaaDAO extends it.csi.solmr.integration.BaseDAO
{

  public DocumentoGaaDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public DocumentoGaaDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  
  public Vector<BaseCodeDescription> getCategoriaDocumentiTerritorialiAzienda(
      long idAzienda) throws DataAccessException
  {
    String query = null;
    StringBuffer queryBuf = new StringBuffer();
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<BaseCodeDescription> vCatDocumenti = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getCategoriaDocumentiTerritorialiAzienda] BEGIN.");

      queryBuf.append(
          "SELECT DISTINCT " +
          "       TCD.ID_CATEGORIA_DOCUMENTO, " +
          "       TCD.DESCRIZIONE " +
          "FROM   DB_DOCUMENTO DOC, " + 
          "       DB_DOCUMENTO_CONDUZIONE DCON, " +
          "       DB_TIPO_CATEGORIA_DOCUMENTO TCD, " +
          "       DB_DOCUMENTO_CATEGORIA DCAT, " +
          "       DB_TIPO_DOCUMENTO TDOC " +
          "WHERE  DOC.ID_AZIENDA = ? " +
          "AND    DOC.ID_DOCUMENTO = DCON.ID_DOCUMENTO " +
          "AND    DOC.EXT_ID_DOCUMENTO = TDOC.ID_DOCUMENTO " +
          "AND    TDOC.ID_TIPOLOGIA_DOCUMENTO = 2 " +
          "AND    TDOC.ID_DOCUMENTO = DCAT.ID_DOCUMENTO " +
          "AND    DCAT.ID_CATEGORIA_DOCUMENTO = TCD.ID_CATEGORIA_DOCUMENTO " +
          "ORDER BY TCD.DESCRIZIONE ");
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getCategoriaDocumentiTerritorialiAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vCatDocumenti == null)
        {
          vCatDocumenti = new Vector<BaseCodeDescription>();
        }
        BaseCodeDescription elem = new BaseCodeDescription();
        
        elem.setCode(rs.getLong("ID_CATEGORIA_DOCUMENTO"));
        elem.setDescription(rs.getString("DESCRIZIONE"));
        
        vCatDocumenti.add(elem);
      }
      
      
      return vCatDocumenti;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vCatDocumenti", vCatDocumenti) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda), };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[DocumentoGaaDAO::getCategoriaDocumentiTerritorialiAzienda] ", t,
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
      SolmrLogger.debug(this,
          "[DocumentoGaaDAO::getCategoriaDocumentiTerritorialiAzienda] END.");
    }
  }
  
  
  public Vector<BaseCodeDescription> getDocumentiTerritorialiAzienda(
      long idAzienda) throws DataAccessException
  {
    String query = null;
    StringBuffer queryBuf = new StringBuffer();
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<BaseCodeDescription> vDocumenti = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getDocumentiTerritorialiAzienda] BEGIN.");

      queryBuf.append(
          "SELECT DISTINCT " +
          "       TDOC.ID_DOCUMENTO, " +
          "       TDOC.DESCRIZIONE, " +
          "       DCAT.ID_CATEGORIA_DOCUMENTO " +
          "FROM   DB_DOCUMENTO DOC, " + 
          "       DB_DOCUMENTO_CONDUZIONE DCON, " +
          "       DB_DOCUMENTO_CATEGORIA DCAT, " +
          "       DB_TIPO_DOCUMENTO TDOC " +
          "WHERE  DOC.ID_AZIENDA = ? " +
          "AND    DOC.ID_DOCUMENTO = DCON.ID_DOCUMENTO " +
          "AND    DOC.EXT_ID_DOCUMENTO = TDOC.ID_DOCUMENTO " +
          "AND    TDOC.ID_TIPOLOGIA_DOCUMENTO = 2 " +
          "AND    TDOC.ID_DOCUMENTO = DCAT.ID_DOCUMENTO " +
          "ORDER BY TDOC.DESCRIZIONE ");
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getDocumentiTerritorialiAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vDocumenti == null)
        {
          vDocumenti = new Vector<BaseCodeDescription>();
        }
        BaseCodeDescription elem = new BaseCodeDescription();
        
        elem.setCode(rs.getLong("ID_DOCUMENTO"));
        elem.setDescription(rs.getString("DESCRIZIONE"));
        elem.setItem(new Long(rs.getLong("ID_CATEGORIA_DOCUMENTO")));
        
        vDocumenti.add(elem);
      }
      
      
      return vDocumenti;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vDocumenti", vDocumenti) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda), };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[DocumentoGaaDAO::getDocumentiTerritorialiAzienda] ", t,
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
      SolmrLogger.debug(this,
          "[DocumentoGaaDAO::getDocumentiTerritorialiAzienda] END.");
    }
  }
  
  /**
   * restituisce i dati dei protocolli relativi all'azienda in 
   * questione indipendentemente dal fascicolo
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<BaseCodeDescription> getProtocolliDocumentiTerritorialiAzienda(
      long idAzienda) throws DataAccessException
  {
    String query = null;
    StringBuffer queryBuf = new StringBuffer();
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<BaseCodeDescription> vProtoclliDocumenti = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getProtocolliDocumentiTerritorialiAzienda] BEGIN.");

      queryBuf.append(
          "SELECT DISTINCT DOC.ID_DOCUMENTO, " +
          "       DOC.EXT_ID_DOCUMENTO, " +
          "       DOC.NUMERO_PROTOCOLLO, " +
          "       DOC.DATA_PROTOCOLLO " +
          "FROM   DB_DOCUMENTO DOC, " + 
          "       DB_DOCUMENTO_CONDUZIONE DCON, " +
          "       DB_TIPO_DOCUMENTO TDOC " +
          "WHERE  DOC.ID_AZIENDA = ? " +
          "AND    DOC.ID_DOCUMENTO = DCON.ID_DOCUMENTO " +
          "AND    DOC.EXT_ID_DOCUMENTO = TDOC.ID_DOCUMENTO " +
          "AND    TDOC.ID_TIPOLOGIA_DOCUMENTO = 2 " +
          "ORDER BY DOC.DATA_PROTOCOLLO DESC ");
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getProtocolliDocumentiTerritorialiAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vProtoclliDocumenti == null)
        {
          vProtoclliDocumenti = new Vector<BaseCodeDescription>();
        }
        BaseCodeDescription elem = new BaseCodeDescription();
        
        String protocollo = "Repertorio " +rs.getString("NUMERO_PROTOCOLLO");
        protocollo += " del " + DateUtils.formatDateNotNull(rs.getDate("DATA_PROTOCOLLO"));
        elem.setCode(rs.getLong("ID_DOCUMENTO"));
        elem.setDescription(protocollo);
        elem.setItem(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
        
        vProtoclliDocumenti.add(elem);
      }
      
      
      return vProtoclliDocumenti;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vProtoclliDocumenti", vProtoclliDocumenti) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda), };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[DocumentoGaaDAO::getProtocolliDocumentiTerritorialiAzienda] ", t,
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
      SolmrLogger.debug(this,
          "[DocumentoGaaDAO::getProtocolliDocumentiTerritorialiAzienda] END.");
    }
  }
  
  /**
   * rstituisce true se il documento è istanza di riesame
   * 
   * 
   * @param idDocumento
   * @return
   * @throws DataAccessException
   */
  public boolean isDocumentoIstanzaRiesame(
      long idDocumento) throws DataAccessException
  {
    String query = null;
    StringBuffer queryBuf = new StringBuffer();
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean isDocumentoIstanza = false;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::isDocumentoIstanzaRiesame] BEGIN.");

      queryBuf.append(
          "SELECT " +
          "        TCD.IDENTIFICATIVO, " +
          "        TCD.TIPO_IDENTIFICATIVO " +
          " FROM   DB_DOCUMENTO D, " +
          "        DB_TIPO_DOCUMENTO TD, " +
          "        DB_DOCUMENTO_CATEGORIA DC, " +
          "        DB_TIPO_CATEGORIA_DOCUMENTO TCD " +
          " WHERE  D.ID_DOCUMENTO = ? " +
          " AND    D.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
          " AND    TD.ID_TIPOLOGIA_DOCUMENTO = TCD.ID_TIPOLOGIA_DOCUMENTO " +
          " AND    TCD.ID_CATEGORIA_DOCUMENTO = DC.ID_CATEGORIA_DOCUMENTO " +
          " AND    DC.ID_DOCUMENTO = TD.ID_DOCUMENTO ");
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isDocumentoIstanzaRiesame] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idDocumento);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        Long identificativo = checkLongNull(rs.getString("IDENTIFICATIVO"));
        String tipoIdentificativo = rs.getString("TIPO_IDENTIFICATIVO");
        
        if(Validator.isNotEmpty(identificativo) && Validator.isNotEmpty(tipoIdentificativo))
        {
          if((identificativo.longValue() == 518)
              && tipoIdentificativo.equalsIgnoreCase("TC"))
          {
            isDocumentoIstanza = true;
          }
            
        }
      }
      
      
      return isDocumentoIstanza;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("isDocumentoIstanza", isDocumentoIstanza) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento), };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[DocumentoGaaDAO::isDocumentoIstanzaRiesame] ", t,
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
      SolmrLogger.debug(this,
          "[DocumentoGaaDAO::isDocumentoIstanzaRiesame] END.");
    }
  }
  
  /**
   * Restituisce tutte le casuali modifica del documento
   * con data fine validita a null
   * 
   * 
   * 
   * @return
   * @throws DataAccessException
   */
  public Vector<BaseCodeDescription> getCausaleModificaDocumentoValid() throws DataAccessException
  {
    String query = null;
    StringBuffer queryBuf = new StringBuffer();
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<BaseCodeDescription> vCasDocumenti = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getCausaleModificaDocumentoValid] BEGIN.");

      queryBuf.append(
          "SELECT " +
          "       CMD.ID_CAUSALE_MODIFICA_DOCUMENTO, " +
          "       CMD.DESCRIZIONE " +
          "FROM   DB_CAUSALE_MODIFICA_DOCUMENTO CMD " + 
          "WHERE  CMD.DATA_FINE_VALIDITA IS NULL " +
          "ORDER BY CMD.DESCRIZIONE ");
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getCausaleModificaDocumentoValid] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vCasDocumenti == null)
        {
          vCasDocumenti = new Vector<BaseCodeDescription>();
        }
        BaseCodeDescription elem = new BaseCodeDescription();
        
        elem.setCode(rs.getLong("ID_CAUSALE_MODIFICA_DOCUMENTO"));
        elem.setDescription(rs.getString("DESCRIZIONE"));
        
        vCasDocumenti.add(elem);
      }
      
      
      return vCasDocumenti;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vCasDocumenti", vCasDocumenti) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[DocumentoGaaDAO::getCausaleModificaDocumentoValid] ", t,
          query, variabili, null);
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
      SolmrLogger.debug(this,
          "[DocumentoGaaDAO::getCausaleModificaDocumentoValid] END.");
    }
  }
  
  /**
   * 
   * Restituisce un vettore di idConduzioneParticella.
   * idConduzioneParticella è valorizzato se alla conduzione
   * è associato almeno un documento attivo.
   * 
   * 
   * 
   * 
   * 
   * @param elencoConduzioni
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> esisteDocumentoAttivoByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<Long> result = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentooGaaDAO::esisteDocumentoAttivoByConduzioneAndAzienda] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
          "SELECT DC.ID_CONDUZIONE_PARTICELLA " +
          "FROM   DB_DOCUMENTO_CONDUZIONE DC, " +
          "       DB_DOCUMENTO DOC " +
          "WHERE  DOC.ID_AZIENDA = ? " + 
          "AND    DOC.ID_STATO_DOCUMENTO IS NULL " + 
          "AND    DOC.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
          "AND    NVL(DC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > TRUNC(SYSDATE) " +
          "AND    DC.ID_CONDUZIONE_PARTICELLA IN ( ")
          .append(this.getIdListFromVectorLongForSQL(elencoConduzioni)).append(")");
      
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[DocumentooGaaDAO::esisteDocumentoAttivoByConduzioneAndAzienda] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);      
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<Long>();
        }
        Long idConduzioneParticella = new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")); 
        if(!result.contains(idConduzioneParticella))
        {
          result.add(idConduzioneParticella);
        }
        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("trovato", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("elencoConduzioni", elencoConduzioni),
          new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[DocumentooGaaDAO::esisteDocumentoAttivoByConduzioneAndAzienda] ", t,
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
      SolmrLogger.debug(this,
          "[DocumentooGaaDAO::esisteDocumentoAttivoByConduzioneAndAzienda] END.");
    }
  }
  
  /**
   * 
   * Restituisce l'elenco dei file allegati
   * per il documento in questione.
   * 
   * 
   * @param idDocumento
   * @return
   * @throws DataAccessException
   */
  public Vector<AllegatoDocumentoVO> getElencoFileAllegati(
      long idDocumento) throws DataAccessException
  {
    String query = null;
    StringBuffer queryBuf = new StringBuffer();
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<AllegatoDocumentoVO> vElencoFileAllegati = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getElencoFileAllegati] BEGIN.");

      queryBuf.append(
          "SELECT AD.ID_ALLEGATO, " +
          "       AD.ID_DOCUMENTO, " +
          "       AL.NOME_LOGICO, " +
          "       AL.NOME_FISICO, " +
          "       AL.DATA_REGISTRAZIONE, " +
          "       AL.ID_TIPO_FIRMA " +
          "FROM   DB_ALLEGATO_DOCUMENTO AD, " +
          "       DB_ALLEGATO AL " +
          "WHERE  AD.ID_DOCUMENTO = ? " +
          "AND    AD.ID_ALLEGATO = AL.ID_ALLEGATO " +
          "ORDER BY AL.NOME_LOGICO ");
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getElencoFileAllegati] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idDocumento);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vElencoFileAllegati == null)
        {
          vElencoFileAllegati = new Vector<AllegatoDocumentoVO>();
        }
        AllegatoDocumentoVO allegatoDocumentoVO = new AllegatoDocumentoVO();
        
        allegatoDocumentoVO.setIdAllegato(checkLong(rs.getString("ID_ALLEGATO")));
        allegatoDocumentoVO.setIdDocumento(checkLong(rs.getString("ID_DOCUMENTO")));
        
        allegatoDocumentoVO.setNomeLogico(rs.getString("NOME_LOGICO"));
        allegatoDocumentoVO.setNomeFisico(rs.getString("NOME_FISICO"));
        allegatoDocumentoVO.setDataRagistrazione(rs.getTimestamp("DATA_REGISTRAZIONE"));
        allegatoDocumentoVO.setIdTipoFirma(checkLongNull(rs.getString("ID_TIPO_FIRMA")));
        
        vElencoFileAllegati.add(allegatoDocumentoVO);
      }
      
      
      return vElencoFileAllegati;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vElencoFileAllegati", vElencoFileAllegati) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento), };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[DocumentoGaaDAO::getElencoFileAllegati] ", t,
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
      SolmrLogger.debug(this,
          "[DocumentoGaaDAO::getElencoFileAllegati] END.");
    }
  }
  
  /**
   * Elimina l'allegato del documento su DB_ALLEGATO
   * 
   * 
   * @param idAllegatoDocumento
   * @throws DataAccessException
   */
  public void deleteFileAllegato(long idAllegato) 
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
              "[DocumentoGaaDAO::deleteFileAllegato] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   DELETE DB_ALLEGATO WHERE ID_ALLEGATO = ?  " );
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::deleteFileAllegato] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllegato);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAllegato", idAllegato)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::deleteFileAllegato] ",
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
              "[DocumentoGaaDAO::deleteFileAllegato] END.");
    }
  }
  
  /**
   * 
   * Inserisce il documento blob!!!
   * 
   * 
   * @param allegatoDocumentoVO
   * @return
   * @throws DataAccessException
   */
  public Long insertFileAllegato(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idAllegato = null;
    ResultSet rs = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::insertFileAllegato] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idAllegato = getNextPrimaryKey(SolmrConstants.SEQ_DB_ALLEGATO);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_ALLEGATO   " 
              + "     (ID_ALLEGATO, "
              + "     DATA_REGISTRAZIONE,   "
              + "     DATA_ULTIMO_AGGIORNAMENTO, "
              + "     ID_UTENTE_AGGIORNAMENTO, "
              + "     NOME_LOGICO, "
              + "     NOME_FISICO," 
              + "     ALLEGATO, "
              + "     ID_TIPO_ALLEGATO) "
              + "   VALUES(?,?,?,?,?,?, empty_blob(),?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::insertFileAllegato] Query1="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllegato.longValue());
      stmt.setTimestamp(++indice, convertDateToTimestamp(allegatoDocumentoVO.getDataRagistrazione()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(allegatoDocumentoVO.getDataUltimoAggiornamento()));
      stmt.setLong(++indice, allegatoDocumentoVO.getIdUtenteAggiornamento().longValue());
      stmt.setString(++indice, allegatoDocumentoVO.getNomeLogico());
      stmt.setString(++indice, allegatoDocumentoVO.getNomeFisico());
      if(Validator.isNotEmpty(allegatoDocumentoVO.getIdTipoAllegato()))
        stmt.setLong(++indice, allegatoDocumentoVO.getIdTipoAllegato().longValue());
      else
        stmt.setLong(++indice, 1);
  
      stmt.executeUpdate();
      
      stmt.close();
      
      queryBuf = new StringBuffer();
      queryBuf
      .append("" +
      	"SELECT AL.ID_ALLEGATO," +
      	"       AL.ALLEGATO " +
        "FROM   DB_ALLEGATO AL  " + 
        "WHERE  ID_ALLEGATO = ? " +
        "FOR UPDATE ");
         
  
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::insertFileAllegato] Query2="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      indice = 0;
      stmt.setLong(++indice, idAllegato.longValue());
      
      rs = stmt.executeQuery();      
      rs.next();      
      java.sql.Blob blob = rs.getBlob("ALLEGATO");
      java.io.OutputStream os =  convertBlobToOutputStream(blob);

      os.write(allegatoDocumentoVO.getFileAllegato(), 0, allegatoDocumentoVO.getFileAllegato().length);
      os.flush();
        
      return idAllegato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idAllegato", idAllegato)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("allegatoDocumentoVO", allegatoDocumentoVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::insertFileAllegato] ",
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
              "[DocumentoGaaDAO::insertFileAllegato] END.");
    }
  }
  
  
  /**
   * 
   * inserisco il record senza l'array di byte
   * 
   * 
   * @param allegatoDocumentoVO
   * @return
   * @throws DataAccessException
   */
  public Long insertFileAllegatoNoFile(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idAllegato = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::insertFileAllegatoNoFile] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idAllegato = getNextPrimaryKey(SolmrConstants.SEQ_DB_ALLEGATO);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_ALLEGATO   " 
              + "     (ID_ALLEGATO, "
              + "     DATA_REGISTRAZIONE,   "
              + "     DATA_ULTIMO_AGGIORNAMENTO, "
              + "     ID_UTENTE_AGGIORNAMENTO, "
              + "     NOME_LOGICO, "
              + "     NOME_FISICO," 
              + "     ALLEGATO, "
              + "     EXT_ID_DOCUMENTO_INDEX, "
              + "     ID_TIPO_ALLEGATO, "
              + "     ID_TIPO_FIRMA) "
              + "   VALUES(?,?,?,?,?,?, empty_blob(), ?, ?, ?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::insertFileAllegatoNoFile] Query1="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllegato.longValue());
      stmt.setTimestamp(++indice, convertDateToTimestamp(allegatoDocumentoVO.getDataRagistrazione()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(allegatoDocumentoVO.getDataUltimoAggiornamento()));
      stmt.setLong(++indice, allegatoDocumentoVO.getIdUtenteAggiornamento().longValue());
      stmt.setString(++indice, allegatoDocumentoVO.getNomeLogico());
      stmt.setString(++indice, allegatoDocumentoVO.getNomeFisico());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(allegatoDocumentoVO.getExtIdDocumentoIndex()));
      if(Validator.isNotEmpty(allegatoDocumentoVO.getIdTipoAllegato()))
        stmt.setLong(++indice, allegatoDocumentoVO.getIdTipoAllegato().longValue());
      else
        stmt.setLong(++indice, 1);
      
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(allegatoDocumentoVO.getIdTipoFirma()));
      
      stmt.executeUpdate();
        
      return idAllegato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idAllegato", idAllegato)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("allegatoDocumentoVO", allegatoDocumentoVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::insertFileAllegatoNoFile] ",
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
              "[DocumentoGaaDAO::insertFileAllegatoNoFile] END.");
    }
  }
  
  /**
   * Ritorna il documento allegato!!!
   * 
   * 
   * @param idAllegatoDocumento
   * @return
   * @throws DataAccessException
   */
  public AllegatoDocumentoVO getFileAllegato(
      long idAllegato) throws DataAccessException
  {
    String query = null;
    StringBuffer queryBuf = new StringBuffer();
    Connection conn = null;
    PreparedStatement stmt = null;
    AllegatoDocumentoVO allegatoDocumentoVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getFileAllegato] BEGIN.");

      queryBuf.append(
          "SELECT AL.ID_ALLEGATO, " +
          "       AL.NOME_LOGICO, " +
          "       AL.NOME_FISICO, " +
          "       AL.ALLEGATO, " +
          "       AL.EXT_ID_DOCUMENTO_INDEX " +
          "FROM   DB_ALLEGATO AL " +
          "WHERE  AL.ID_ALLEGATO = ? ");
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getFileAllegato] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllegato);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        allegatoDocumentoVO = new AllegatoDocumentoVO();
        
        allegatoDocumentoVO.setIdAllegato(checkLong(rs.getString("ID_ALLEGATO")));        
        allegatoDocumentoVO.setNomeLogico(rs.getString("NOME_LOGICO"));
        allegatoDocumentoVO.setNomeFisico(rs.getString("NOME_FISICO"));
        allegatoDocumentoVO.setExtIdDocumentoIndex(checkLongNull(rs.getString("EXT_ID_DOCUMENTO_INDEX")));
         
        Blob blob = rs.getBlob("ALLEGATO");
        if(blob!=null && (blob.length() >0))
        {
          allegatoDocumentoVO.setFileAllegato(blob.getBytes(1, (int)blob.length()));
        }
        
      }
      
      
      return allegatoDocumentoVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("allegatoDocumentoVO", allegatoDocumentoVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAllegato", idAllegato), };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[DocumentoGaaDAO::getFileAllegato] ", t,
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
      SolmrLogger.debug(this,
          "[DocumentoGaaDAO::getFileAllegato] END.");
    }
  }
  
  /**
   * Restituisce il numero di volte che l'allegato 
   * è legato ai ducumenti.
   * Server per capire se è legato a documenti storicizzati o meno.
   * 
   * 
   * @param idAllegatoDocumento
   * @return
   * @throws DataAccessException
   */
  public int getNumFileAllegato(long idAllegato) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    int numAllegato = 0;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getNumFileAllegato] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT COUNT(*) AS NUMOCCURS " +
        "FROM   DB_ALLEGATO_DOCUMENTO AD " +
        "WHERE  AD.ID_ALLEGATO = ? " );
             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getNumFileAllegato] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllegato);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        numAllegato = rs.getInt("NUMOCCURS");        
      }
      
      
      return numAllegato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAllegato", idAllegato)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getNumFileAllegato] ",
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
              "[DocumentoGaaDAO::getNumFileAllegato] END.");
    }
  }
  
  
  /**
   * Elimna il record su DB_ALLEGATO_DOCUMENTO.
   * ELimina lA relazione tra il documento e l'allegato.
   * 
   * 
   * 
   * @param idAllegatoDocumento
   * @param idDocumento
   * @throws DataAccessException
   */
  public void deleteAllegatoDocumento(long idAllegato, long idDocumento) 
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
              "[DocumentoGaaDAO::deleteAllegatoDocumento] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      	"DELETE DB_ALLEGATO_DOCUMENTO " +
      	"WHERE  ID_ALLEGATO = ?  " +
      	"AND    ID_DOCUMENTO = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::deleteAllegatoDocumento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllegato);
      stmt.setLong(++indice, idDocumento);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAllegato", idAllegato),
        new Parametro("idDocumento", idDocumento)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::deleteAllegatoDocumento] ",
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
              "[DocumentoGaaDAO::deleteAllegatoDocumento] END.");
    }
  }
  
  public void deleteAllegatoRichiesta(long idAllegato, long idRichiestaDocumento) 
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
              "[DocumentoGaaDAO::deleteAllegatoRichiesta] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE DB_ALLEGATO_RICHIESTA " +
        "WHERE  ID_ALLEGATO = ?  " +
        "AND    ID_RICHIESTA_AZIENDA_DOCUMENTO = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::deleteAllegatoRichiesta] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllegato);
      stmt.setLong(++indice, idRichiestaDocumento);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAllegato", idAllegato),
        new Parametro("idRichiestaDocumento", idRichiestaDocumento)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::deleteAllegatoRichiesta] ",
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
              "[DocumentoGaaDAO::deleteAllegatoRichiesta] END.");
    }
  }
  
  public void deleteAllegatiDocumentoRichiesta(long idRichiestaDocumento) 
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
              "[DocumentoGaaDAO::deleteAllegatiDocumentoRichiesta] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE DB_ALLEGATO_RICHIESTA " +
        "WHERE  ID_RICHIESTA_AZIENDA_DOCUMENTO = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::deleteAllegatiDocumentoRichiesta] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaDocumento);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaDocumento", idRichiestaDocumento)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::deleteAllegatiDocumentoRichiesta] ",
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
              "[DocumentoGaaDAO::deleteAllegatiDocumentoRichiesta] END.");
    }
  }
  
  public void deleteAllegatoNotifica(long idAllegato, long idNotifica) 
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
              "[DocumentoGaaDAO::deleteAllegatoNotifica] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE DB_ALLEGATO_NOTIFICA " +
        "WHERE  ID_ALLEGATO = ?  " +
        "AND    ID_NOTIFICA = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::deleteAllegatoNotifica] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllegato);
      stmt.setLong(++indice, idNotifica);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAllegato", idAllegato),
        new Parametro("idNotifica", idNotifica)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::deleteAllegatoNotifica] ",
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
              "[DocumentoGaaDAO::deleteAllegatoNotifica] END.");
    }
  }
  
  
  
  public void insertAllegatoDocumento(long idDocumento, long idAllegato) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idAllegatoDocumento = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::insertAllegatoDocumento] BEGIN.");
      
      idAllegatoDocumento = getNextPrimaryKey(SolmrConstants.SEQ_DB_ALLEGATO_DOCUMENTO);
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_ALLEGATO_DOCUMENTO   " 
              + "     (ID_ALLEGATO_DOCUMENTO, "
              + "     ID_DOCUMENTO, "
              + "     ID_ALLEGATO) "
              + "   VALUES(?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::insertAllegatoDocumento] Query1="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllegatoDocumento.longValue());
      stmt.setLong(++indice, idDocumento);
      stmt.setLong(++indice, idAllegato);
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento),
        new Parametro("idAllegato", idAllegato)  };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::insertAllegatoDocumento] ",
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
              "[DocumentoGaaDAO::insertAllegatoDocumento] END.");
    }
  }
  
  public void insertAllegatoRichiesta(long idRichiestaDocumento, long idAllegato) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idAllegatoRichiesta = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::insertAllegatoRichiesta] BEGIN.");
      
      idAllegatoRichiesta = getNextPrimaryKey(SolmrConstants.SEQ_DB_ALLEGATO_RICHIESTA);
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_ALLEGATO_RICHIESTA   " 
              + "     (ID_ALLEGATO_RICHIESTA, "
              + "     ID_RICHIESTA_AZIENDA_DOCUMENTO, "
              + "     ID_ALLEGATO) "
              + "   VALUES(?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::insertAllegatoRichiesta] Query1="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllegatoRichiesta.longValue());
      stmt.setLong(++indice, idRichiestaDocumento);
      stmt.setLong(++indice, idAllegato);
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaDocumento", idRichiestaDocumento),
        new Parametro("idAllegato", idAllegato)  };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::insertAllegatoRichiesta] ",
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
              "[DocumentoGaaDAO::insertAllegatoRichiesta] END.");
    }
  }
  
  
  public void insertAllegatoNotifica(long idNotifica, long idAllegato) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idAllegatoRichiesta = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::insertAllegatoNotifica] BEGIN.");
      
      idAllegatoRichiesta = getNextPrimaryKey(SolmrConstants.SEQ_DB_ALLEGATO_NOTIFICA);
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_ALLEGATO_NOTIFICA   " 
              + "     (ID_ALLEGATO_NOTIFICA, "
              + "     ID_NOTIFICA, "
              + "     ID_ALLEGATO) "
              + "   VALUES(?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::insertAllegatoNotifica] Query1="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllegatoRichiesta.longValue());
      stmt.setLong(++indice, idNotifica);
      stmt.setLong(++indice, idAllegato);
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idNotifica", idNotifica),
        new Parametro("idAllegato", idAllegato)  };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::insertAllegatoNotifica] ",
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
              "[DocumentoGaaDAO::insertAllegatoNotifica] END.");
    }
  }
  
  
  public void insertAllegatoDichiarazione(long idDichiarazioneConsistenza, long idAllegato) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idAllegatoDichiarazione = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::insertAllegatoDichiarazione] BEGIN.");
      
      idAllegatoDichiarazione = getNextPrimaryKey(SolmrConstants.SEQ_DB_ALLEGATO_DICHIARAZIONE);
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_ALLEGATO_DICHIARAZIONE   " 
              + "     (ID_ALLEGATO_DICHIARAZIONE, "
              + "     ID_DICHIARAZIONE_CONSISTENZA, "
              + "     ID_ALLEGATO, "
              + "     DATA_INIZIO_VALIDITA) "
              + "   VALUES(?,?,?,SYSDATE) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::insertAllegatoDichiarazione] Query1="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllegatoDichiarazione.longValue());
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      stmt.setLong(++indice, idAllegato);
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza),
        new Parametro("idAllegato", idAllegato)  };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::insertAllegatoDichiarazione] ",
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
              "[DocumentoGaaDAO::insertAllegatoDichiarazione] END.");
    }
  }
  
  
  /**
   * 
   * ritorna la data_inserimento del primo documento inserito
   * (senza le eventuali modifiche/storicizzazioni)
   * 
   * 
   * @param idDocumento
   * @return
   * @throws DataAccessException
   */
  public Date getFirstDataInserimentoDocumento(long idDocumento) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Date dataInserimento = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getFirstDataInserimentoDocumento] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT DATA_INSERIMENTO, LEVEL " + 
        "FROM DB_DOCUMENTO " +
        "CONNECT BY PRIOR ID_DOCUMENTO_PRECEDENTE = ID_DOCUMENTO " +
        "START WITH ID_DOCUMENTO = ? " +
        "ORDER BY LEVEL DESC" );
             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getFirstDataInserimentoDocumento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idDocumento);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      //mi ineteressa solo il primo record (il primo inserito)
      if(rs.next())
      {
        dataInserimento = rs.getTimestamp("DATA_INSERIMENTO");        
      }
      
      
      return dataInserimento;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getFirstDataInserimentoDocumento] ",
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
              "[DocumentoGaaDAO::getFirstDataInserimentoDocumento] END.");
    }
  }
  
  
  /**
   * 
   * Controlla se almeno una particella del documento
   * è in stato lavorata.
   * Se relativamente alla particella mi trovo tutte le date fine validita
   * valorizzate ritorno true
   * 
   * 
   * @param idDocumento
   * @return
   * @throws DataAccessException
   */
  public boolean isIstanzaRiesameFotoInterpretataByDocumento(long idDocumento) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean flagTrovato = true;
    boolean flagTrovatoAlmenoUnRecord = false;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::isIstanzaRiesameFotoInterpretataByDocumento] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT  CP.ID_PARTICELLA, " +
        "        DC.DATA_FINE_VALIDITA " +
        "FROM    DB_CONDUZIONE_PARTICELLA CP, " +
        "        DB_DOCUMENTO_CONDUZIONE DC " +
        "WHERE   DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
        "AND     DC.ID_DOCUMENTO = ? " +
        "ORDER BY CP.ID_PARTICELLA");
             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isIstanzaRiesameFotoInterpretataByDocumento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idDocumento);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      //mi ineteressa solo il primo record (il primo inserito)
      long idParticellaTmp = 0;
      int i=0;
      while(rs.next())
      {
        if(!flagTrovatoAlmenoUnRecord)
          flagTrovatoAlmenoUnRecord = true;
        long idParticella = rs.getLong("ID_PARTICELLA");
        if(i !=0)
        {
          if(idParticellaTmp == idParticella)
          {
            if(flagTrovato == true)
            {
              Date dataFineValidita = rs.getTimestamp("DATA_FINE_VALIDITA");
              if(Validator.isNotEmpty(dataFineValidita) 
                  && (dataFineValidita.before(new Date())))
              {              
                flagTrovato = true;
              }
              else
              {
                flagTrovato = false;
              }
            }
          }
          else
          {
            if(flagTrovato)
              return flagTrovato;
            
            Date dataFineValidita = rs.getTimestamp("DATA_FINE_VALIDITA");
            if(Validator.isNotEmpty(dataFineValidita) 
                && (dataFineValidita.before(new Date())))
            {              
              flagTrovato = true;
            }
            else
            {
              flagTrovato = false;
            }
          }
        }
        else
        {
          Date dataFineValidita = rs.getTimestamp("DATA_FINE_VALIDITA");
          if(Validator.isNotEmpty(dataFineValidita) 
              && (dataFineValidita.before(new Date())))
          {              
            flagTrovato = true;
          }
          else
          {
            flagTrovato = false;
          }
        }
        
        idParticellaTmp = idParticella;
        
        i++;        
      }
      
      //se non ho record.....
      if(!flagTrovatoAlmenoUnRecord)
        flagTrovato = false;
      
      
      return flagTrovato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isIstanzaRiesameFotoInterpretataByDocumento] ",
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
              "[DocumentoGaaDAO::isIstanzaRiesameFotoInterpretataByDocumento] END.");
    }
  }
  
  /**
   * ritorna gli id delle particelle legate ad un documento
   * se fotointerpretate
   * 
   * 
   * @param idDocumento
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> getParticelleIstanzaRiesameFotoInterpretataByDocumento(long idDocumento) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIdParticella = null;
    boolean flagTrovato = true;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getParticelleIstanzaRiesameFotoInterpretataByDocumento] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT  CP.ID_PARTICELLA, " +
        "        DC.DATA_FINE_VALIDITA " +
        "FROM    DB_CONDUZIONE_PARTICELLA CP, " +
        "        DB_DOCUMENTO_CONDUZIONE DC " +
        "WHERE   DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
        "AND     DC.ID_DOCUMENTO = ? " +
        "ORDER BY CP.ID_PARTICELLA");
             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getParticelleIstanzaRiesameFotoInterpretataByDocumento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idDocumento);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      //mi ineteressa solo il primo record (il primo inserito)
      long idParticellaTmp = 0;
      long idParticella = 0;
      int i=0;
      while(rs.next())
      {
        idParticella = rs.getLong("ID_PARTICELLA");
        if(i !=0)
        {
          if(idParticellaTmp == idParticella)
          {
            if(flagTrovato == true)
            {
              Date dataFineValidita = rs.getTimestamp("DATA_FINE_VALIDITA");
              if(Validator.isNotEmpty(dataFineValidita) 
                  && (dataFineValidita.before(new Date())))
              {              
                flagTrovato = true;
              }
              else
              {
                flagTrovato = false;
              }
            }
          }
          else
          {
            if(flagTrovato)
            {
              if(vIdParticella == null)
              {
                vIdParticella = new Vector<Long>();
              }
              
              vIdParticella.add(new Long(idParticellaTmp));
            }
            
            Date dataFineValidita = rs.getTimestamp("DATA_FINE_VALIDITA");
            if(Validator.isNotEmpty(dataFineValidita) 
                && (dataFineValidita.before(new Date())))
            {              
              flagTrovato = true;
            }
            else
            {
              flagTrovato = false;
            }
          }
        }
        else
        {
          Date dataFineValidita = rs.getTimestamp("DATA_FINE_VALIDITA");
          if(Validator.isNotEmpty(dataFineValidita) 
              && (dataFineValidita.before(new Date())))
          {              
            flagTrovato = true;
          }
          else
          {
            flagTrovato = false;
          }
        }
        
        idParticellaTmp = idParticella;
        
        i++;        
      }
      
      if(flagTrovato)
      {
        if(vIdParticella == null)
        {
          vIdParticella = new Vector<Long>();
        }
        
        vIdParticella.add(new Long(idParticellaTmp));
      }
      
      
      return vIdParticella;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vIdParticella", vIdParticella)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getParticelleIstanzaRiesameFotoInterpretataByDocumento] ",
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
              "[DocumentoGaaDAO::isIstanzaRiesameFotoInterpretataByDocumento] END.");
    }
  }
  
  
  /**
   * ritorna true se la particella è stata evasa
   * 
   * 
   * @param idDocumento
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public boolean isIstanzaRiesameFotoInterpretataByDocumentoAndParticella(
    long idDocumento, long idParticella) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean flagTrovato = true;
    boolean flagTrovatoAlemnoUna = false;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::isIstanzaRiesameFotoInterpretataByDocumentoAndParticella] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT  DC.DATA_FINE_VALIDITA " +
        "FROM    DB_CONDUZIONE_PARTICELLA CP, " +
        "        DB_DOCUMENTO_CONDUZIONE DC " +
        "WHERE   DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
        "AND     DC.ID_DOCUMENTO = ? " +
        "AND     CP.ID_PARTICELLA = ? ");
             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isIstanzaRiesameFotoInterpretataByDocumento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idDocumento);
      stmt.setLong(++indice, idParticella);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      java.util.Date dataFineValidita = null;
      while(rs.next())
      {
        if(!flagTrovatoAlemnoUna)
        {
          flagTrovatoAlemnoUna = true;
        }
        if(flagTrovato == true) 
        {
          dataFineValidita = rs.getTimestamp("DATA_FINE_VALIDITA");
          //java.util.Date dataValidita = rs.getDate("DATA_FINE_VALIDITA");
          System.out.println(rs.getTimestamp("DATA_FINE_VALIDITA"));
          if(Validator.isNotEmpty(dataFineValidita) 
              && (dataFineValidita.before(new Date())))
          {              
            flagTrovato = true;
          }
          else
          {
            flagTrovato = false;
          }
        }
          
        if(!flagTrovato)
          return flagTrovato;
            
      }
      
      //se non ho record.....
      if(!flagTrovatoAlemnoUna)
      {
        flagTrovato = false;
      }
      
      return flagTrovato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento),
        new Parametro("idParticella", idParticella) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isIstanzaRiesameFotoInterpretataByDocumento] ",
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
              "[DocumentoGaaDAO::isIstanzaRiesameFotoInterpretataByDocumento] END.");
    }
  }
  
  
  /**
   * 
   * mi ritorna i dati di db_documentoCOnduzione per tutte le conduzioni
   * delle particella legate al documento fotointerpretate
   * 
   * 
   * 
   * @param idDocumento
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public Vector<DocumentoConduzioneVO> getDocCondIstanzaRiesameFotoInterpretataByDocumentoAndParticella(
    long idDocumento, long idParticella) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<DocumentoConduzioneVO> vDocCond = null;
    DocumentoConduzioneVO documentoConduzioneVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getDocCondIstanzaRiesameFotoInterpretataByDocumentoAndParticella] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT  DC.ID_CONDUZIONE_PARTICELLA, " +
        "        DC.DATA_INSERIMENTO, " +
        "        DC.DATA_INIZIO_VALIDITA, " +
        "        DC.DATA_FINE_VALIDITA, " +
        "        DC.NOTE, " +
        "        DC.LAVORAZIONE_PRIORITARIA " +
        "FROM    DB_CONDUZIONE_PARTICELLA CP, " +
        "        DB_DOCUMENTO_CONDUZIONE DC " +
        "WHERE   DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
        "AND     DC.ID_DOCUMENTO = ? " +
        "AND     CP.ID_PARTICELLA = ? ");
             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getDocCondIstanzaRiesameFotoInterpretataByDocumentoAndParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idDocumento);
      stmt.setLong(++indice, idParticella);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vDocCond == null)
        {
          vDocCond = new Vector<DocumentoConduzioneVO>();
        }
        documentoConduzioneVO = new DocumentoConduzioneVO();
        documentoConduzioneVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
        documentoConduzioneVO.setDataInserimento(rs.getTimestamp("DATA_INSERIMENTO"));
        documentoConduzioneVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        documentoConduzioneVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        documentoConduzioneVO.setNote(rs.getString("NOTE"));
        documentoConduzioneVO.setLavorazionePrioritaria(rs.getString("LAVORAZIONE_PRIORITARIA"));
        
        vDocCond.add(documentoConduzioneVO);    
      }
      
      return vDocCond;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("vDocCond", vDocCond), new Variabile("documentoConduzioneVO", documentoConduzioneVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento),
        new Parametro("idParticella", idParticella) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isIstanzaRiesameFotoInterpretataByDocumento] ",
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
              "[DocumentoGaaDAO::isIstanzaRiesameFotoInterpretataByDocumento] END.");
    }
  }
  
  
  
  
  /**
   * Metodo utilizzato per verificare se esistono 
   * doc istanza di riesame associati alla particella dell'azienda in questione.
   * 
   * - Se idDocumento è valorizzato (caso aggiornamento) ritorna true
   * se oltra a quello corrente esiste un altro documento IS associato
   * alla particella
   * - Altrimenti ritorna true se esiste già un doc IS associato alla particella
   * in questione (caso inserimento)
   * 
   * 
   * @param idParticella
   * @param idDocumento
   * @return
   * @throws DataAccessException
   */
  public boolean exitsOtherDocISForParticellaAndAzienda(long idParticella, long idAzienda, Long idDocumento) 
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
              "[DocumentoGaaDAO::exitsOtherDocISForParticellaAndAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT COUNT(*) AS NUMERO_RECORD " +
        "FROM   DB_UTE UT, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_DOCUMENTO_CONDUZIONE DC, " +
        "       DB_DOCUMENTO DOC, " +
        "       DB_DOCUMENTO_CATEGORIA DDC, " +
        "       DB_TIPO_CATEGORIA_DOCUMENTO TCD " +
        "WHERE  CP.ID_PARTICELLA = ? " +
        "AND    UT.ID_AZIENDA = ? " +
        "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
        "AND    CP.ID_UTE = UT.ID_UTE " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    CP.ID_CONDUZIONE_PARTICELLA = DC.ID_CONDUZIONE_PARTICELLA " +
        "AND    DC.DATA_FINE_VALIDITA IS NULL " +
        "AND    DC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
        "AND    DOC.ID_STATO_DOCUMENTO IS NULL " +
        "AND    DOC.EXT_ID_DOCUMENTO = DDC.ID_DOCUMENTO " +
        "AND    DDC.ID_CATEGORIA_DOCUMENTO = TCD.ID_CATEGORIA_DOCUMENTO " +
        "AND    TCD.IDENTIFICATIVO =  518 " +
        "AND    TCD.TIPO_IDENTIFICATIVO = 'TC' ");
      
      if(Validator.isNotEmpty(idDocumento))
      {
        queryBuf.append(
        "AND    DC.ID_DOCUMENTO <> ? ");    
      }
             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::exitsOtherDocISForParticellaAndAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      
      stmt.setLong(++indice, idParticella);
      stmt.setLong(++indice, idAzienda);
      if(Validator.isNotEmpty(idDocumento))
      {
        stmt.setLong(++indice, idDocumento);
      }
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        int numRecord = rs.getInt("NUMERO_RECORD");
        if(numRecord > 0)
        {
          trovato = true;
        }
      }
      
      return trovato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("trovato", trovato) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento),
        new Parametro("idParticella", idParticella), 
        new Parametro("idAzienda", idAzienda) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::exitsOtherDocISForParticellaAndAzienda] ",
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
              "[DocumentoGaaDAO::exitsOtherDocISForParticellaAndAzienda] END.");
    }
  }
  
  
  
  /**
   * 
   * Verifica se per il tipo di documento istanza di riesame 
   * (in base ad extIdDocumento) sono stati evase tutte
   * le fasi precedenti di istanza di riesame
   * per le particelle presenti nel documento corrente
   * 
   * 
   * @param idAzienda
   * @param extIdDocumento
   * @param vIdParticella
   * @param parametro
   * @return
   * @throws DataAccessException
   */  
  /*public boolean isFaseIstanzaRiesameEvasa(long idAzienda, int fase, Vector<Long> vIdParticella,
      String parametro) throws DataAccessException
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
              "[DocumentoGaaDAO::isFaseIstanzaRiesameEvasa] BEGIN.");
  
      // CONCATENAZIONE/CREAZIONE QUERY BEGIN. 
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT DC.DATA_FINE_VALIDITA " +
        "FROM   DB_DOCUMENTO DOC, " +
        "       DB_ALTRI_DATI AD, " +
        "       DB_DOCUMENTO_CONDUZIONE DC, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_R_FASE_RIESAME_TP_DOCUMENTO FRD " +
        "WHERE DOC.ID_AZIENDA = ? " +
        "AND   FRD.ID_FASE_ISTANZA_RIESAME = ? " +
        "AND   FRD.DATA_FINE_VALIDITA IS NULL " +
        "AND   DOC.EXT_ID_DOCUMENTO = FRD.ID_DOCUMENTO " +
        "AND   DOC.ID_STATO_DOCUMENTO IS NULL " +
        "AND   DOC.DATA_INSERIMENTO <= SYSDATE " +
        "AND   DOC.DATA_INSERIMENTO >= (SYSDATE - AD.VALORE_NUMERICO) " +
        "AND   AD.CODICE = ? " +
        "AND   DOC.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
        "AND   DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
        "AND   CP.ID_PARTICELLA IN ( ")
          .append(getIdListFromVectorLongForSQL(vIdParticella))
          .append(") ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isFaseIstanzaRiesameEvasa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
     
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, fase);
      stmt.setString(++indice, parametro);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        Date dataFineValidita = rs.getTimestamp("DATA_FINE_VALIDITA");
        if(Validator.isEmpty(dataFineValidita))
        {
          trovato = false;
          break;
        }
        else
        {
          trovato = true;
        }
      }
      
      return trovato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("trovato", trovato) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("fase", fase),
        new Parametro("vIdParticella", vIdParticella),
        new Parametro("parametro", parametro)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isFaseIstanzaRiesameEvasa] ",
              t, query, variabili, parametri);
      
      //Rimappo e rilancio l'eccezione come DataAccessException.
      
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
      //Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
      //ignora ogni eventuale eccezione)
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::isFaseIstanzaRiesameEvasa] END.");
    }
  }*/
  
  
  /**
   * 
   * Verifica se per la particella in questione esiste un documento
   * della fase precedente
   * 
   * 
   * @param idDocumento
   * @param extIdDocumento
   * @param parametro
   * @return
   * @throws DataAccessException
   */
  public boolean exsitsDocFaseIstanzaRiesameFasePrec(long idAzienda, int fase, 
      long idParticella, String parametro) 
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
              "[DocumentoGaaDAO::exsitsDocFaseIstanzaRiesameFasePrec] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT DC.ID_DOCUMENTO_CONDUZIONE " +
        "FROM   DB_DOCUMENTO DOC, " +
        "       DB_ALTRI_DATI AD, "+
        "       DB_DOCUMENTO_CONDUZIONE DC, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_R_FASE_RIESAME_TP_DOCUMENTO FRD " +
        "WHERE  DOC.ID_AZIENDA = ? " +
        "AND    FRD.ID_FASE_ISTANZA_RIESAME = ? " +
        "AND    FRD.DATA_FINE_VALIDITA IS NULL " +
        "AND    DOC.EXT_ID_DOCUMENTO = FRD.ID_DOCUMENTO " +
        "AND    DOC.ID_STATO_DOCUMENTO IS NULL " +
        "AND    DOC.DATA_INSERIMENTO <= SYSDATE " +
        "AND    DOC.DATA_INSERIMENTO >= (SYSDATE - AD.VALORE_NUMERICO) " +
        "AND    AD.CODICE = ? " +
        "AND    DOC.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
        "AND    CP.ID_CONDUZIONE_PARTICELLA = DC.ID_CONDUZIONE_PARTICELLA " +
        "AND    CP.ID_PARTICELLA = ? ");             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::exsitsDocFaseIstanzaRiesameFasePrec] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
     
      stmt.setLong(++indice, idAzienda);
      stmt.setInt(++indice, fase);
      stmt.setString(++indice, parametro);
      stmt.setLong(++indice, idParticella);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
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
        new Variabile("trovato", trovato) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("fase", fase),
        new Parametro("parametro", parametro),
        new Parametro("idParticella", idParticella)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::exsitsDocFaseIstanzaRiesameFasePrec] ",
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
              "[DocumentoGaaDAO::exsitsDocFaseIstanzaRiesameFasePrec] END.");
    }
  }
  
  /**
   * 
   * ritorna true se per quella particella è possibile creare un doc istanza di riesame per 
   * la fase sucessiva
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @param idFase
   * @param parametro
   * @return
   * @throws DataAccessException
   */
  public boolean isPossCreateDocFaseIstanzaRiesameFaseSucc(long idAzienda, long idParticella,
      int idFase, String parametro) 
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
              "[DocumentoGaaDAO::isPossCreateDocFaseIstanzaRiesameFaseSucc] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IR.DATA_EVASIONE " +
        "FROM   DB_ISTANZA_RIESAME IR, " +
        "       DB_ALTRI_DATI AD, " +
        "       DB_ALTRI_DATI AD2 " +
        "WHERE  IR.ID_AZIENDA = ? " +
        "AND    IR.DATA_ANNULLAMENTO IS NULL " +
        "AND    IR.ID_PARTICELLA = ? " +
        "AND    IR.ID_FASE_ISTANZA_RIESAME = ? " +
        "AND    IR.DATA_EVASIONE IS NOT NULL "+
        "AND    IR.DATA_SOSPENSIONE_SCADUTA IS NULL "+
        "AND    IR.ANNO = (SELECT MAX(IR2.ANNO) " +
        "                  FROM   DB_ISTANZA_RIESAME IR2 " +
        "                  WHERE  IR2.ID_AZIENDA = IR.ID_AZIENDA " +
        "                  AND    IR2.DATA_ANNULLAMENTO IS NULL " +
        "                  AND    IR2.ID_PARTICELLA = IR.ID_PARTICELLA " +
        "                  AND    IR2.ID_FASE_ISTANZA_RIESAME = IR.ID_FASE_ISTANZA_RIESAME) " +
        "AND    AD2.CODICE = 'DATA_X_ISTANZA' "+
        "AND    DECODE(SIGN(AD2.VALORE_DATA - IR.DATA_AGGIORNAMENTO), 1, AD2.VALORE_DATA, IR.DATA_AGGIORNAMENTO)  >= (SYSDATE  - AD.VALORE_NUMERICO) " +
        "AND    AD.CODICE = ? ");             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isPossCreateDocFaseIstanzaRiesameFaseSucc] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
     
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idParticella);
      stmt.setInt(++indice, idFase);
      stmt.setString(++indice, parametro);
      
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
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
        new Variabile("trovato", trovato) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idParticella", idParticella),
        new Parametro("idFase", idFase),
        new Parametro("parametro", parametro)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isPossCreateDocFaseIstanzaRiesameFaseSucc] ",
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
              "[DocumentoGaaDAO::isPossCreateDocFaseIstanzaRiesameFaseSucc] END.");
    }
  }
  
  
  /**
   * 
   * ritorna la data dell'esecuzione corretta dell'ultimoi batch che carica i dati
   * su SITICONVOCA ***Non più usato!!!!!!*****
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @param anno
   * @param idFase
   * @return
   * @throws DataAccessException
   */
  public Date getDateLastBatchIstanzaRiesameOk() 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Date dataInizioEsecuzione = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getDateLastBatchIstanzaRiesameOk] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT EB.DT_INIZIO_ESECUZIONE " +
        "FROM   SMRGAA_T_ESECUZIONE_BATCH EB " +
        "WHERE  EB.ID_APPLICAZIONE = 14 " +
        "AND    EB.FLAG_ESITO = 0 " +
        "AND    EB.DT_INIZIO_ESECUZIONE = (SELECT MAX(DT_INIZIO_ESECUZIONE) " +
        "                                  FROM   SMRGAA_T_ESECUZIONE_BATCH " +
        "                                  WHERE  ID_APPLICAZIONE = 14 " +
        "                                  AND    FLAG_ESITO = 0) ");             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getDateLastBatchIstanzaRiesameOk] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
       
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        dataInizioEsecuzione = rs.getTimestamp("DT_INIZIO_ESECUZIONE");
      }
      
      return dataInizioEsecuzione;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("dataInizioEsecuzione", dataInizioEsecuzione) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getDateLastBatchIstanzaRiesameOk] ",
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
              "[DocumentoGaaDAO::getDateLastBatchIstanzaRiesameOk] END.");
    }
  }
  
 /**
  * 
  * Se è già stato inviato un record a SITI CONVOCA
  * 
  * 
  * 
  * @param idAzienda
  * @param anno
  * @param fase
  * @return
  * @throws DataAccessException
  */
  public boolean isSitiConvocaValid(long idAzienda, int anno, int fase) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean isSitiConvoca = false;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::isSitiConvocaValid] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IR.ID_ISTANZA_RIESAME " +
        "FROM   DB_ISTANZA_RIESAME IR " +
        "WHERE  IR.ID_AZIENDA = ? " +
        "AND    IR.DATA_EVASIONE IS NULL " +
        "AND    IR.ANNO = ? " +
        "AND    IR.ID_FASE_ISTANZA_RIESAME = ? " +
        "AND    IR.DATA_ANNULLAMENTO IS NULL " +
        "AND    IR.DATA_INVIO_GIS IS NOT NULL ");             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isSitiConvocaValid] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setInt(++indice, anno);
      stmt.setInt(++indice, fase);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        isSitiConvoca = true;
      }
      
      return isSitiConvoca;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("isSitiConvoca", isSitiConvoca) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isSitiConvocaValid] ",
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
              "[DocumentoGaaDAO::isSitiConvocaValid] END.");
    }
  }
  
  public boolean isDataSospensioneScaduta(long idAzienda, long idParticella, int anno) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean isDatSospensioneScaduta = false;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::isDataSospensioneScaduta] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IR.ID_ISTANZA_RIESAME " +
        "FROM   DB_ISTANZA_RIESAME IR " +
        "WHERE  IR.ID_AZIENDA = ? " +
        "AND    IR.ID_PARTICELLA = ? " +
        "AND    IR.ANNO = ? " +
        "AND    IR.DATA_EVASIONE IS NOT NULL " +
        "AND    IR.DATA_SOSPENSIONE_SCADUTA IS NOT NULL ");             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isDataSospensioneScaduta] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idParticella);
      stmt.setInt(++indice, anno);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        isDatSospensioneScaduta = true;
      }
      
      return isDatSospensioneScaduta;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("isDatSospensioneScaduta", isDatSospensioneScaduta) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella),
        new Parametro("anno", anno)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isDataSospensioneScaduta] ",
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
              "[DocumentoGaaDAO::isDataSospensioneScaduta] END.");
    }
  }
  
  /**
   * 
   * ritorna tru se per quell'anno e quelal fase la particella risulta evasa
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @param fase
   * @param anno
   * @return
   * @throws DataAccessException
   */
  public boolean isParticellaEvasa(long idAzienda, long idParticella, int fase, int anno) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean isParticellaEvasa = false;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::isParticellaEvasa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IR.ID_ISTANZA_RIESAME " +
        "FROM   DB_ISTANZA_RIESAME IR " +
        "WHERE  IR.ID_AZIENDA = ? " +
        "AND    IR.ID_PARTICELLA = ? " +
        "AND    IR.ID_FASE_ISTANZA_RIESAME = ? " +
        "AND    IR.ANNO = ? " +
        "AND    IR.DATA_EVASIONE IS NOT NULL ");             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isParticellaEvasa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idParticella);
      stmt.setInt(++indice, fase);
      stmt.setInt(++indice, anno);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        isParticellaEvasa = true;
      }
      
      return isParticellaEvasa;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("isParticellaEvasa", isParticellaEvasa) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella),
        new Parametro("fase", fase),
      new Parametro("anno", anno)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isParticellaEvasa] ",
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
              "[DocumentoGaaDAO::isParticellaEvasa] END.");
    }
  }
  
  public boolean isVisibleTastoElimina(long idAzienda, int anno, int fase) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean isVisible = true;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::isVisibleTastoElimina] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IR.ID_ISTANZA_RIESAME " +
        "FROM   DB_ISTANZA_RIESAME IR " +
        "WHERE  IR.ID_AZIENDA = ? " +
        "AND    IR.ANNO = ? " +
        "AND    IR.ID_FASE_ISTANZA_RIESAME = ? " +
        "AND    IR.DATA_INVIO_GIS IS NOT NULL ");             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isVisibleTastoElimina] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setInt(++indice, anno);
      stmt.setInt(++indice, fase);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        isVisible = false;
      }
      
      return isVisible;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("isVisible", isVisible) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("anno", anno),
        new Parametro("fase", fase) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isVisibleTastoElimina] ",
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
              "[DocumentoGaaDAO::isVisibleTastoElimina] END.");
    }
  }
  
  
  public Vector<DocumentoVO> getListDocFromRicerca(String strDescDocumento, boolean attivi) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<DocumentoVO> vListDocumenti = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getListDocFromRicerca] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT TD.ID_DOCUMENTO, " +
        "       TD.DESCRIZIONE AS DESC_TIPO_DOC, " +
        "       TTD.ID_TIPOLOGIA_DOCUMENTO, " +
        "       TTD.DESCRIZIONE AS DESC_TIPOLOGIA_DOC, " +
        "       TCD.ID_CATEGORIA_DOCUMENTO, " +
        "       TCD.DESCRIZIONE AS DESC_CATEGORIA_DOC " +
        "FROM   DB_TIPO_DOCUMENTO TD, " +
        "       DB_TIPO_CATEGORIA_DOCUMENTO TCD, " +
        "       DB_DOCUMENTO_CATEGORIA DC, " +
        "       DB_TIPO_TIPOLOGIA_DOCUMENTO TTD " + 
        "WHERE  UPPER(TD.DESCRIZIONE) LIKE ? " +
        "AND    TD.ID_TIPOLOGIA_DOCUMENTO = TTD.ID_TIPOLOGIA_DOCUMENTO " +
        "AND    TD.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
        "AND    DC.ID_CATEGORIA_DOCUMENTO = TCD.ID_CATEGORIA_DOCUMENTO ");
      if(attivi)
      {
        queryBuf.append(
        "AND    TD.DATA_FINE_VALIDITA IS NULL ");
      }
      
      queryBuf.append(
        "ORDER BY TTD.DESCRIZIONE ASC, " +
        "         TCD.DESCRIZIONE ASC, " +
        "         TD.DESCRIZIONE ASC");
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getListDocFromRicerca] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setString(++indice, "%"+strDescDocumento.toUpperCase()+"%");
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vListDocumenti == null)
        {
          vListDocumenti = new  Vector<DocumentoVO>();
        }
        DocumentoVO documentoVO = new DocumentoVO();
        TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO(); 
        tipoDocumentoVO.setIdDocumento(checkLongNull(rs.getString("ID_DOCUMENTO")));
        tipoDocumentoVO.setDescrizione(rs.getString("DESC_TIPO_DOC"));
        documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
        CodeDescription tipoTipologiaDocumento = new CodeDescription();
        tipoTipologiaDocumento.setCode(checkInt(rs.getString("ID_TIPOLOGIA_DOCUMENTO")));
        tipoTipologiaDocumento.setDescription(rs.getString("DESC_TIPOLOGIA_DOC"));
        documentoVO.setTipoTipologiaDocumento(tipoTipologiaDocumento);
        TipoCategoriaDocumentoVO tipoCategoriaDocumentoVO = new TipoCategoriaDocumentoVO();
        tipoCategoriaDocumentoVO.setIdCategoriaDocumento(checkLongNull(rs.getString("ID_CATEGORIA_DOCUMENTO")));
        tipoCategoriaDocumentoVO.setDescrizione(rs.getString("DESC_CATEGORIA_DOC"));
        documentoVO.setTipoCategoriaDocumentoVO(tipoCategoriaDocumentoVO);
        
        
        vListDocumenti.add(documentoVO);
      }
      
      return vListDocumenti;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("vListDocumenti", vListDocumenti) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("strDescDocumento", strDescDocumento),
          new Parametro("attivi", attivi)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getListDocFromRicerca] ",
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
              "[DocumentoGaaDAO::getListDocFromRicerca] END.");
    }
  }
  
  
  /**
   * 
   * Mi ritorna tutte le particella di un'azienda che sono per l'anno
   * specificato nello stato fotointerpretato e che hanno almeno una unita vitata
   * 
   * 
   * @param idAzienda
   * @param anno
   * @return
   * @throws DataAccessException
   */
  public Vector<ParticellaIstanzaRiesameVO> getLisParticellaFromIstanzaFoto(long idAzienda, int anno) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<ParticellaIstanzaRiesameVO> vListParticelle = null;
    ParticellaIstanzaRiesameVO particellaIstanzaRiesameVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getLisParticellaFromIstanzaFoto] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IR.ID_ISTANZA_RIESAME, " +
        "       IR.ANNO, " +
        "       IR.DATA_RICHIESTA, " +
        "       IR.DATA_EVASIONE, " +
        "       COM.DESCOM, " +
        "       PROV.SIGLA_PROVINCIA, " +
        "       SP.PARTICELLA, " +
        "       SP.FOGLIO, " +
        "       SP.SEZIONE, " +
        "       SP.SUBALTERNO " +
        "FROM   DB_ISTANZA_RIESAME IR, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       COMUNE COM, " +
        "       PROVINCIA PROV " +
        "WHERE  IR.ID_AZIENDA = ? " + 
        "AND    IR.ANNO = ? " +
        "AND    IR.DATA_ANNULLAMENTO IS NULL " +
        "AND    IR.DATA_EVASIONE IS NOT NULL " +
        "AND    IR.ID_LISTA_CAMPIONE IS NULL " +
        "AND    IR.ID_FASE_ISTANZA_RIESAME = 1 " +
        "AND    IR.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
        "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
        "AND EXISTS (SELECT SUA.ID_STORICO_UNITA_ARBOREA " +
        "            FROM   DB_STORICO_UNITA_ARBOREA SUA " +
        "            WHERE  SUA.ID_AZIENDA = IR.ID_AZIENDA " +
        "            AND    SUA.ID_PARTICELLA = IR.ID_PARTICELLA) " +
        "ORDER BY COM.DESCOM, " +
        "         PROV.SIGLA_PROVINCIA, " +
        "         SP.SEZIONE, " +
        "         SP.FOGLIO, " +
        "         SP.PARTICELLA, " +
        "         SP.SUBALTERNO");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getLisParticellaFromIstanzaFoto] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setInt(++indice, anno);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vListParticelle == null)
        {
          vListParticelle = new  Vector<ParticellaIstanzaRiesameVO>();
        }
        particellaIstanzaRiesameVO = new ParticellaIstanzaRiesameVO();
        particellaIstanzaRiesameVO.setIdIstanzaRiesame(checkLong(rs.getString("ID_ISTANZA_RIESAME")));
        particellaIstanzaRiesameVO.setAnno(checkInt(rs.getString("ANNO")));
        particellaIstanzaRiesameVO.setDataRichiesta(rs.getTimestamp("DATA_RICHIESTA"));
        particellaIstanzaRiesameVO.setDataEvasione(rs.getTimestamp("DATA_EVASIONE"));
        particellaIstanzaRiesameVO.setDescComune(rs.getString("DESCOM")+" ("+rs.getString("SIGLA_PROVINCIA")+")");
        particellaIstanzaRiesameVO.setParticella(rs.getString("PARTICELLA"));
        particellaIstanzaRiesameVO.setFoglio(rs.getString("FOGLIO"));
        particellaIstanzaRiesameVO.setSubalterno(rs.getString("SUBALTERNO"));
        particellaIstanzaRiesameVO.setSezione(rs.getString("SEZIONE"));
        
        vListParticelle.add(particellaIstanzaRiesameVO);
      }
      
      return vListParticelle;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("vListParticelle", vListParticelle),
        new Variabile("particellaIstanzaRiesameVO", particellaIstanzaRiesameVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("anno", anno)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getLisParticellaFromIstanzaFoto] ",
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
              "[DocumentoGaaDAO::getLisParticellaFromIstanzaFoto] END.");
    }
  }
  
  
  
  /**
   * 
   * Mi ritorna true se ha trovato per una particella
   * una istana di riesame valida in fase successiva alla fase di fotointerpretazione
   * 
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public boolean existFaseSucessivaFotoPraticella(long idAzienda, long idParticella) 
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
              "[DocumentoGaaDAO::existFaseSucessivaFotoPraticella] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT ID_ISTANZA_RIESAME " +
        "FROM   DB_ISTANZA_RIESAME " +
        "WHERE  ID_FASE_ISTANZA_RIESAME > 1 " +
        "AND    DATA_ANNULLAMENTO IS NULL " +
        "AND    ID_AZIENDA = ? " +
        "AND    ID_PARTICELLA = ? ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::existFaseSucessivaFotoPraticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idParticella);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
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
        new Variabile("trovato", trovato) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idParticella", idParticella)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::existFaseSucessivaFotoPraticella] ",
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
              "[DocumentoGaaDAO::existFaseSucessivaFotoPraticella] END.");
    }
  }
  
  
  
  /**
   * Mi ritorna la chiave catastale della particella partendo
   * dall'istanza di riesame 
   * 
   * 
   * 
   * @param idIstanzaRiesame
   * @return
   * @throws DataAccessException
   */
  public ParticellaIstanzaRiesameVO getParticellaFromIstanza(long idIstanzaRiesame) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    ParticellaIstanzaRiesameVO particellaIstanzaRiesameVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getParticellaFromIstanza] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT COM.DESCOM, " +
        "       PROV.SIGLA_PROVINCIA, " +
        "       IR.ID_PARTICELLA, " +
        "       SP.PARTICELLA, " +
        "       SP.FOGLIO, " +
        "       SP.SEZIONE, " +
        "       SP.SUBALTERNO " +
        "FROM   DB_ISTANZA_RIESAME IR, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       COMUNE COM, " +
        "       PROVINCIA PROV " +
        "WHERE  IR.ID_ISTANZA_RIESAME = ? " +
        "AND    IR.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
        "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getParticellaFromIstanza] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idIstanzaRiesame);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        particellaIstanzaRiesameVO = new ParticellaIstanzaRiesameVO();
        particellaIstanzaRiesameVO.setIdParticella(rs.getLong("ID_PARTICELLA"));
        particellaIstanzaRiesameVO.setDescComune(rs.getString("DESCOM")+" ("+rs.getString("SIGLA_PROVINCIA")+")");
        particellaIstanzaRiesameVO.setParticella(rs.getString("PARTICELLA"));
        particellaIstanzaRiesameVO.setFoglio(rs.getString("FOGLIO"));
        particellaIstanzaRiesameVO.setSubalterno(rs.getString("SUBALTERNO"));
        particellaIstanzaRiesameVO.setSezione(rs.getString("SEZIONE"));
        
      }
      
      return particellaIstanzaRiesameVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("particellaIstanzaRiesameVO", particellaIstanzaRiesameVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idIstanzaRiesame", idIstanzaRiesame)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getParticellaFromIstanza] ",
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
              "[DocumentoGaaDAO::getParticellaFromIstanza] END.");
    }
  }
  
  
  /**
   * Mi ritorna true se trova per la particella indicata
   * una fase di istanza riesame successiva o uguale all'anno passato come parametro
   * 
   * 
   * 
   * @param idAzienda
   * @param particella
   * @param anno
   * @return
   * @throws DataAccessException
   */
  public boolean existAltraFaseFotoParticella(long idAzienda, long idParticella, int anno, long idFase) 
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
              "[DocumentoGaaDAO::existAltraFaseFotoParticella] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT ID_ISTANZA_RIESAME " +
        "FROM   DB_ISTANZA_RIESAME " +
        "WHERE  ID_FASE_ISTANZA_RIESAME = ? " +
        "AND    DATA_ANNULLAMENTO IS NULL " +
        "AND    ID_AZIENDA = ? " +
        "AND    ID_PARTICELLA = ? " +
        "AND    ANNO >= ? ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::existAltraFaseFotoParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idFase);
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idParticella);
      stmt.setInt(++indice, anno);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
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
        new Variabile("trovato", trovato) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idParticella", idParticella),
          new Parametro("anno", anno)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::existAltraFaseFotoParticella] ",
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
              "[DocumentoGaaDAO::existAltraFaseFotoParticella] END.");
    }
  }
  
  
  /**
   * Verifica se esiste una fotointerpretazione per un anno successivo a quello passato
   * come parametro
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @param anno
   * @return
   * @throws DataAccessException
   */
  public boolean existAltraFaseFotoParticellaPerAnnulla(long idAzienda, long idParticella, int anno) 
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
              "[DocumentoGaaDAO::existAltraFaseFotoParticellaPerAnnulla] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT ID_ISTANZA_RIESAME " +
        "FROM   DB_ISTANZA_RIESAME " +
        "WHERE  ID_FASE_ISTANZA_RIESAME = 1 " +
        "AND    DATA_ANNULLAMENTO IS NULL " +
        "AND    ID_AZIENDA = ? " +
        "AND    ID_PARTICELLA = ? " +
        "AND    ANNO > ? ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::existAltraFaseFotoParticellaPerAnnulla] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idParticella);
      stmt.setInt(++indice, anno);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
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
        new Variabile("trovato", trovato) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idParticella", idParticella),
          new Parametro("anno", anno)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::existAltraFaseFotoParticellaPerAnnulla] ",
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
              "[DocumentoGaaDAO::existAltraFaseFotoParticellaPerAnnulla] END.");
    }
  }
  
  
  /**
   * setta a sysdate data_annullamente du DB_ISTANZA_RIESAME
   * 
   * 
   * 
   * @param vIdIstanzaRiesame
   * @throws DataAccessException
   */
  public void annullaIstanzaFromId(Vector<Long> vIdIstanzaRiesame, Long idUtente) 
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
              "[DocumentoGaaDAO::annullaIstanzaFromId] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("UPDATE DB_ISTANZA_RIESAME " +
                  "SET    DATA_ANNULLAMENTO = SYSDATE, " +
                  "       DATA_AGGIORNAMENTO = SYSDATE, " +
                  "       ID_UTENTE_RICHIEDENTE = ? " +
                  "WHERE  ID_ISTANZA_RIESAME = ? ");
      
      query = queryBuf.toString();     

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::annullaIstanzaFromId] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);

      for(int i = 0; i < vIdIstanzaRiesame.size(); i++) 
      {
        int indice = 0;
        stmt.setBigDecimal(++indice, convertLongToBigDecimal(idUtente)); 
        stmt.setLong(++indice, vIdIstanzaRiesame.get(i).longValue());        
        stmt.addBatch();
      }
      
      stmt.executeBatch();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdIstanzaRiesame", vIdIstanzaRiesame) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::annullaIstanzaFromId] ",
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
              "[DocumentoGaaDAO::annullaIstanzaFromId] END.");
    }
  }
  
  
  public Long insertIstanzaRiesame(IstanzaRiesameVO istanzaRiesameVO, Long idUtente) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idIstanzaRiesame = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::insertIstanzaRiesame] BEGIN.");
      
      idIstanzaRiesame = getNextPrimaryKey(SolmrConstants.SEQ_DB_ISTANZA_RIESAME);
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_ISTANZA_RIESAME   " 
              + "     (ID_ISTANZA_RIESAME, "
              + "     ID_FASE_ISTANZA_RIESAME, "
              + "     ANNO, "
              + "     ID_AZIENDA, "
              + "     ID_PARTICELLA, "
              + "     DATA_RICHIESTA, "
              + "     DATA_ANNULLAMENTO, "
              + "     DATA_EVASIONE, "
              + "     DATA_AGGIORNAMENTO, "
              + "     ID_LISTA_CAMPIONE, "
              + "     DESC_LISTA_CAMPIONE, "
              + "     LAVORAZIONE_PRIORITARIA, "
              + "     NOTE, " 
              + "     DATA_CHIUSURA_ISTANZA, "
              + "     DATA_SITICONVOCA, "
              + "     ID_UTENTE_RICHIEDENTE, "
              + "     PROTOCOLLO_ISTANZA, "
              + "     ID_STATO_SITICONVOCA) "
              + "   VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::insertIstanzaRiesame] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idIstanzaRiesame.longValue());
      stmt.setLong(++indice, istanzaRiesameVO.getIdFaseIstanzaRiesame().longValue());
      stmt.setInt(++indice, istanzaRiesameVO.getAnno().intValue());
      stmt.setLong(++indice, istanzaRiesameVO.getIdAzienda());
      stmt.setLong(++indice, istanzaRiesameVO.getIdParticella());
      stmt.setTimestamp(++indice, convertDateToTimestamp(istanzaRiesameVO.getDataRichiesta()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(istanzaRiesameVO.getDataAnnullamento()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(istanzaRiesameVO.getDataEvasione()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(istanzaRiesameVO.getDataAggiornamento()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(istanzaRiesameVO.getIdListaCampione()));
      stmt.setString(++indice, istanzaRiesameVO.getDescListaCampione());
      stmt.setString(++indice, istanzaRiesameVO.getLavorazionePrioritaria());
      stmt.setString(++indice, istanzaRiesameVO.getNote());
      stmt.setTimestamp(++indice, convertDateToTimestamp(istanzaRiesameVO.getDataChiusuraIstanza()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(istanzaRiesameVO.getDataSiticonvoca()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(idUtente));
      stmt.setString(++indice, istanzaRiesameVO.getProtocolloIstanza());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(istanzaRiesameVO.getIdStatoSitiConvoca()));
  
      stmt.executeUpdate();
      
      return idIstanzaRiesame;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("istanzaRiesameVO", istanzaRiesameVO)  };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::insertIstanzaRiesame] ",
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
              "[DocumentoGaaDAO::insertIstanzaRiesame] END.");
    }
  }
  
  
  public void insertIstanzaRiesameAzienda(IstanzaRiesameAziendaVO istanzaRiesameAziendaVO) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idIstanzaRiesameAzienda = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::insertIstanzaRiesameAzienda] BEGIN.");
      
      idIstanzaRiesameAzienda = getNextPrimaryKey(SolmrConstants.SEQ_DB_ISTANZA_RIESAME_AZIENDA);
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_ISTANZA_RIESAME_AZIENDA   " 
              + "     (ID_ISTANZA_RIESAME_AZIENDA, "
              + "     PROTOCOLLO_ISTANZA, "
              + "     ID_AZIENDA,"
              + "     ANNO_ISTANZA) "
              + "   VALUES(?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::insertIstanzaRiesameAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idIstanzaRiesameAzienda.longValue());
      stmt.setString(++indice, istanzaRiesameAziendaVO.getProtocolloIstanza());
      stmt.setLong(++indice, istanzaRiesameAziendaVO.getIdAzienda());
      stmt.setInt(++indice, istanzaRiesameAziendaVO.getAnnoIstanza());
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("istanzaRiesameAziendaVO", istanzaRiesameAziendaVO)  };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::insertIstanzaRiesameAzienda] ",
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
              "[DocumentoGaaDAO::insertIstanzaRiesameAzienda] END.");
    }
  }
  
  
  /**
   * 
   * Controllo se esistono altri legami per quel tipo documento
   * istanza di riesame per latre coduzioni della stessa particella 
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @param anno
   * @return
   * @throws DataAccessException
   */
  public boolean existAltroLegameIstRiesameParticella(long idAzienda, 
      long idParticella, int anno, long extIdDocumento) 
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
              "[DocumentoGaaDAO::existAltroLegameIstRiesameParticella] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT CP.ID_PARTICELLA " +
        "FROM   DB_DOCUMENTO_CONDUZIONE DC, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_DOCUMENTO DOC " +
        "WHERE  CP.ID_PARTICELLA = ? " +
        "AND    DOC.ID_AZIENDA = ?  " +
        "AND    CP.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO+
        "AND    CP.ID_CONDUZIONE_PARTICELLA = DC.ID_CONDUZIONE_PARTICELLA " +
        "AND    DC.DATA_FINE_VALIDITA IS NULL " +
        "AND    DC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
        "AND    TO_CHAR(DOC.DATA_INSERIMENTO,'yyyy') = ? " +  
        "AND    DOC.ID_STATO_DOCUMENTO IS NULL " +
        "AND    DOC.EXT_ID_DOCUMENTO = ? ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::existAltroLegameIstRiesameParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idParticella);
      stmt.setLong(++indice, idAzienda);      
      stmt.setInt(++indice, anno);
      stmt.setLong(++indice, extIdDocumento);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
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
        new Variabile("trovato", trovato) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idParticella", idParticella),
          new Parametro("extIdDocumento", extIdDocumento),
          new Parametro("anno", anno)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::existAltroLegameIstRiesameParticella] ",
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
              "[DocumentoGaaDAO::existAltroLegameIstRiesameParticella] END.");
    }
  }
  
  
  public Long getIstRiesameParticellaFaseAnno(long idAzienda, 
      long idParticella, int anno, int fase) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idIstanzaRiesame = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getIstRiesameParticellaFaseAnno] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IR.ID_ISTANZA_RIESAME " +
        "FROM   DB_ISTANZA_RIESAME IR " +
        "WHERE  IR.ID_PARTICELLA = ? " +
        "AND    IR.ID_AZIENDA = ?  " +
        "AND    IR.ID_FASE_ISTANZA_RIESAME = ? " +
        "AND    IR.ANNO = ? " +
        "AND    IR.DATA_ANNULLAMENTO IS NULL " +
        "AND    IR.DATA_EVASIONE IS NULL " +
        "AND    IR.DATA_CHIUSURA_ISTANZA IS NULL ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getIstRiesameParticellaFaseAnno] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idParticella);
      stmt.setLong(++indice, idAzienda);
      stmt.setInt(++indice, fase);
      stmt.setInt(++indice, anno);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        idIstanzaRiesame = new Long(rs.getLong("ID_ISTANZA_RIESAME"));
      }
      
      return idIstanzaRiesame;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idIstanzaRiesame", idIstanzaRiesame) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idParticella", idParticella),
          new Parametro("fase", fase),
          new Parametro("anno", anno)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getIstRiesameParticellaFaseAnno] ",
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
              "[DocumentoGaaDAO::getIstRiesameParticellaFaseAnno] END.");
    }
  }
  
  /**
   * ritorna true se tutti i recood hanno data_annullamento valorizzata!!!
   * 
   * 
   * @param idAzienda
   * @param vIdParticella
   * @param anno
   * @param idFase
   * @return
   * @throws DataAccessException
   */
  public boolean isAllIstanzaAnnullata(long idAzienda, Vector<Long> vIdParticella, int anno, long idFase) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean trovato = true;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::isAllIstanzaAnnullata] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT ID_ISTANZA_RIESAME " +
        "FROM   DB_ISTANZA_RIESAME " +
        "WHERE  ID_FASE_ISTANZA_RIESAME = ? " +
        "AND    DATA_ANNULLAMENTO IS NULL " +
        "AND    ID_AZIENDA = ? " +
        "AND    ID_PARTICELLA IN (").append(this.getIdListFromVectorLongForSQL(vIdParticella)).append(")")
      .append(
        "AND    ANNO = ? ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isAllIstanzaAnnullata] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idFase);
      stmt.setLong(++indice, idAzienda);
      stmt.setInt(++indice, anno);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(trovato)
          trovato = false;
      }
      
      return trovato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("trovato", trovato) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("vIdParticella", vIdParticella),
          new Parametro("anno", anno)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isAllIstanzaAnnullata] ",
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
              "[DocumentoGaaDAO::isAllIstanzaAnnullata] END.");
    }
  }
  
  /**
   * 
   * estrae i tipo documnento validi per la nuova iscrizione
   * 
   * 
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoDocumentoVO> getDocumentiNuovaIscrizione() 
      throws DataAccessException
  {
    String query = null;
    StringBuffer queryBuf = new StringBuffer();
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoDocumentoVO> vDocumenti = null;
    TipoDocumentoVO tipoDocumentoVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getDocumentiNuovaIscrizione] BEGIN.");

      queryBuf.append(
          "SELECT TDOC.ID_DOCUMENTO, " +
          "       TDOC.DESCRIZIONE, " +
          "       TDOC.FLAG_OBBLIGO_ENTE_NUMERO " +
          "FROM   DB_TIPO_DOCUMENTO TDOC " +
          "WHERE  TDOC.FLAG_RICHIESTA_AZIENDA = 'S' " +
          "AND    TDOC.DATA_FINE_VALIDITA IS NULL " +
          "ORDER BY TDOC.DESCRIZIONE ");
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getDocumentiNuovaIscrizione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vDocumenti == null)
        {
          vDocumenti = new Vector<TipoDocumentoVO>();
        }
        tipoDocumentoVO = new TipoDocumentoVO();
        
        tipoDocumentoVO.setIdDocumento(rs.getLong("ID_DOCUMENTO"));
        tipoDocumentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoDocumentoVO.setFlagObbligoEnteNumero(rs.getString("FLAG_OBBLIGO_ENTE_NUMERO"));
        
        vDocumenti.add(tipoDocumentoVO);
      }
      
      
      return vDocumenti;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vDocumenti", vDocumenti),
          new Variabile("tipoDocumentoVO", tipoDocumentoVO)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[DocumentoGaaDAO::getDocumentiNuovaIscrizione] ", t,
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
      SolmrLogger.debug(this,
          "[DocumentoGaaDAO::getDocumentiNuovaIscrizione] END.");
    }
  }
  
  
  public Vector<AllegatoDocumentoVO> getElencoFileAllegatiRichiesta(
      long idRichiestaDocumento) throws DataAccessException
  {
    String query = null;
    StringBuffer queryBuf = new StringBuffer();
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<AllegatoDocumentoVO> vElencoFileAllegati = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getElencoFileAllegatiRichiesta] BEGIN.");

      queryBuf.append(
          "SELECT AR.ID_ALLEGATO, " +
          "       AR.ID_RICHIESTA_AZIENDA_DOCUMENTO, " +
          "       AL.NOME_LOGICO, " +
          "       AL.NOME_FISICO " +
          "FROM   DB_ALLEGATO_RICHIESTA AR, " +
          "       DB_ALLEGATO AL " +
          "WHERE  AR.ID_RICHIESTA_AZIENDA_DOCUMENTO = ? " +
          "AND    AR.ID_ALLEGATO = AL.ID_ALLEGATO " +
          "ORDER BY AL.NOME_LOGICO ");
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getElencoFileAllegatiRichiesta] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaDocumento);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vElencoFileAllegati == null)
        {
          vElencoFileAllegati = new Vector<AllegatoDocumentoVO>();
        }
        AllegatoDocumentoVO allegatoDocumentoVO = new AllegatoDocumentoVO();
        
        allegatoDocumentoVO.setIdAllegato(checkLong(rs.getString("ID_ALLEGATO")));
        allegatoDocumentoVO.setIdDocumento(checkLong(rs.getString("ID_RICHIESTA_AZIENDA_DOCUMENTO")));
        
        allegatoDocumentoVO.setNomeLogico(rs.getString("NOME_LOGICO"));
        allegatoDocumentoVO.setNomeFisico(rs.getString("NOME_FISICO"));     
        
        vElencoFileAllegati.add(allegatoDocumentoVO);
      }
      
      
      return vElencoFileAllegati;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vElencoFileAllegati", vElencoFileAllegati) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaDocumento", idRichiestaDocumento), };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[DocumentoGaaDAO::getElencoFileAllegatiRichiesta] ", t,
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
      SolmrLogger.debug(this,
          "[DocumentoGaaDAO::getElencoFileAllegatiRichiesta] END.");
    }
  }
  
  public Vector<AllegatoDocumentoVO> getElencoFileAllegatiNotifica(
      long idNotifica) throws DataAccessException
  {
    String query = null;
    StringBuffer queryBuf = new StringBuffer();
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<AllegatoDocumentoVO> vElencoFileAllegati = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getElencoFileAllegatiNotifica] BEGIN.");

      queryBuf.append(
          "SELECT AN.ID_ALLEGATO, " +
          "       AN.ID_NOTIFICA, " +
          "       AL.NOME_LOGICO, " +
          "       AL.NOME_FISICO " +
          "FROM   DB_ALLEGATO_NOTIFICA AN, " +
          "       DB_ALLEGATO AL " +
          "WHERE  AN.ID_NOTIFICA = ? " +
          "AND    AN.ID_ALLEGATO = AL.ID_ALLEGATO " +
          "ORDER BY AL.NOME_LOGICO ");
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getElencoFileAllegatiNotifica] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idNotifica);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vElencoFileAllegati == null)
        {
          vElencoFileAllegati = new Vector<AllegatoDocumentoVO>();
        }
        AllegatoDocumentoVO allegatoDocumentoVO = new AllegatoDocumentoVO();
        
        allegatoDocumentoVO.setIdAllegato(checkLong(rs.getString("ID_ALLEGATO")));
        allegatoDocumentoVO.setIdDocumento(checkLong(rs.getString("ID_NOTIFICA")));
        
        allegatoDocumentoVO.setNomeLogico(rs.getString("NOME_LOGICO"));
        allegatoDocumentoVO.setNomeFisico(rs.getString("NOME_FISICO"));     
        
        vElencoFileAllegati.add(allegatoDocumentoVO);
      }
      
      
      return vElencoFileAllegati;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vElencoFileAllegati", vElencoFileAllegati) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idNotifica", idNotifica), };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[DocumentoGaaDAO::getElencoFileAllegatiNotifica] ", t,
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
      SolmrLogger.debug(this,
          "[DocumentoGaaDAO::getElencoFileAllegatiNotifica] END.");
    }
  }
  
  
  
  public boolean isIstanzaAttiva(long idAzienda) 
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
              "[DocumentoGaaDAO::isIstanzaAttiva] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT ID_ISTANZA_RIESAME " +
        "FROM   DB_ISTANZA_RIESAME " +
        "WHERE  DATA_ANNULLAMENTO IS NULL " +
        "AND    ID_AZIENDA = ? " +
        "AND    DATA_EVASIONE IS NULL ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isIstanzaAttiva] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
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
        new Variabile("trovato", trovato) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isIstanzaAttiva] ",
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
              "[DocumentoGaaDAO::isIstanzaAttiva] END.");
    }
  }
  
  
  public long getNewIdMaxProtocolloIstanza(int anno) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    long newMax = 1;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getNewIdMaxProtocolloIstanza] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT MAX(SUBSTR(IR.PROTOCOLLO_ISTANZA,10)+0) AS PROT_IST " +
        "FROM   DB_ISTANZA_RIESAME IR " +
        "WHERE  SUBSTR(IR.PROTOCOLLO_ISTANZA,5,4) = ? ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getNewIdMaxProtocolloIstanza] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setString(++indice, new Integer(anno).toString());
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        newMax = rs.getLong("PROT_IST");
        newMax++;
      }
      
      return newMax;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("newMax", newMax) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {   new Parametro("anno", anno)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getNewIdMaxProtocolloIstanza] ",
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
              "[DocumentoGaaDAO::getNewIdMaxProtocolloIstanza] END.");
    }
  }
  
  public String getProtocolloIstanza(long idAzienda, int anno) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    String protocolloIstanza = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getProtocolloIstanza] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IR.PROTOCOLLO_ISTANZA " +
        "FROM   DB_ISTANZA_RIESAME IR " +
        "WHERE  IR.ID_AZIENDA = ? " +
        "AND    SUBSTR(IR.PROTOCOLLO_ISTANZA,5,4) = ? " +
        "AND    IR.PROTOCOLLO_ISTANZA NOT IN ( " +
        "       SELECT IR1.PROTOCOLLO_ISTANZA " +
        "       FROM   DB_ISTANZA_RIESAME IR1 " +
        "       WHERE  IR1.ID_AZIENDA = IR.ID_AZIENDA " +
        "       AND    IR1.ID_FASE_ISTANZA_RIESAME > 1 " +
        "       AND    IR1.DATA_INVIO_GIS IS NOT NULL " +
        "       AND    SUBSTR(IR.PROTOCOLLO_ISTANZA,5,4) = ?  ) ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getProtocolloIstanza] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setString(++indice, new Integer(anno).toString());
      stmt.setString(++indice, new Integer(anno).toString());
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        protocolloIstanza = rs.getString("PROTOCOLLO_ISTANZA");
      }
      
      return protocolloIstanza;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("protocolloIstanza", protocolloIstanza) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("anno", anno)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getProtocolloIstanza] ",
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
              "[DocumentoGaaDAO::getProtocolloIstanza] END.");
    }
  }
  
  /**
   * 
   * ritorna true quando la conduzione della particella è cancellabile.
   * Questo accade se nn ci sono istanze riesame attive che insistono su quella particella
   * indipendemente dalla fase e dall'anno dell'istanza.
   * 
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public boolean isParticellaIstRiesameCancellabile(long idAzienda, 
      Vector<Long> vIdConduzioneParticella) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean cancellabile = true;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::isParticellaIstRiesameCancellabile] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IR.ID_ISTANZA_RIESAME " +
        "FROM   DB_ISTANZA_RIESAME IR, " +
        "       DB_CONDUZIONE_PARTICELLA CP " +
        "WHERE  IR.ID_AZIENDA = ?  " +
        "AND    CP.ID_PARTICELLA = IR.ID_PARTICELLA " +
        "AND    IR.DATA_ANNULLAMENTO IS NULL " +
        "AND    CP.ID_CONDUZIONE_PARTICELLA IN ( ")
          .append(this.getIdListFromVectorLongForSQL(vIdConduzioneParticella)).append(")");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isParticellaIstRiesameCancellabile] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        cancellabile = false;
      }
      
      return cancellabile;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("cancellabile", cancellabile) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("vIdConduzioneParticella", vIdConduzioneParticella)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isParticellaIstRiesameCancellabile] ",
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
              "[DocumentoGaaDAO::isParticellaIstRiesameCancellabile] END.");
    }
  }
  
  public Long getParticellaIstRiesameDocProto(long idAzienda, 
      Long idParticella, long idFase) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idIstanzaRiesame = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getParticellaIstRiesameDocProto] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IR.ID_ISTANZA_RIESAME " +
        "FROM   DB_ISTANZA_RIESAME IR " +
        "WHERE  IR.ID_AZIENDA = ?  " +
        "AND    IR.ID_PARTICELLA = ? " +
        "AND    ID_FASE_ISTANZA_RIESAME = ? " +
        "AND    IR.DATA_ANNULLAMENTO IS NULL ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getParticellaIstRiesameDocProto] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idParticella.longValue());
      stmt.setLong(++indice, idFase);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        idIstanzaRiesame = new Long(rs.getLong("ID_ISTANZA_RIESAME"));
      }
      
      return idIstanzaRiesame;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idIstanzaRiesame", idIstanzaRiesame) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idParticella", idParticella),
          new Parametro("idFase", idFase)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getParticellaIstRiesameDocProto] ",
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
              "[DocumentoGaaDAO::getParticellaIstRiesameDocProto] END.");
    }
  }
  
  public void aggiornaStatoSiticonvoca(Long idIstanzaRiesame, int idStatoSitiConvoca, 
    Long idUtente) 
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
              "[DocumentoGaaDAO::aggiornaStatoSiticonvoca] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("UPDATE DB_ISTANZA_RIESAME " +
                  "SET    ID_STATO_SITICONVOCA = ?, " +
                  "       ID_UTENTE_RICHIEDENTE = ? " +
                  "WHERE  ID_ISTANZA_RIESAME = ? ");
      
      query = queryBuf.toString();     

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::aggiornaStatoSiticonvoca] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);

      int indice = 0;
      stmt.setInt(++indice, idStatoSitiConvoca); 
      stmt.setLong(++indice, idUtente.longValue());
      stmt.setLong(++indice, idIstanzaRiesame.longValue());
      
      
      stmt.executeUpdate();
      
      
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idIstanzaRiesame", idIstanzaRiesame),
          new Parametro("idStatoSitiConvoca", idStatoSitiConvoca),
          new Parametro("idUtente", idUtente) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::aggiornaStatoSiticonvoca] ",
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
              "[DocumentoGaaDAO::aggiornaStatoSiticonvoca] END.");
    }
  }
  
  /**
   * Dovrebbe sostituire il metodo exitsOtherDocISForParticellaAndAzienda.
   * Verifice se per la fase passata per l'azienda e per la particella esiste un istanza attiva!!!
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @param idFase
   * @return
   * @throws DataAccessException
   */
  public boolean existIstanzaEsameAttivaFase(long idAzienda, 
      Long idParticella, long idFase) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean esiste = false;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::existIstanzaEsameAttivaFase] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IR.ID_ISTANZA_RIESAME " +
        "FROM   DB_ISTANZA_RIESAME IR " +
        "WHERE  IR.ID_AZIENDA = ?  " +
        "AND    IR.ID_PARTICELLA = ? " +
        "AND    IR.ID_FASE_ISTANZA_RIESAME = ? " +
        "AND    IR.DATA_ANNULLAMENTO IS NULL " +
        "AND    IR.DATA_EVASIONE IS NULL ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::existIstanzaEsameAttivaFase] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idParticella.longValue());
      stmt.setLong(++indice, idFase);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        esiste = true;
      }
      
      return esiste;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("esiste", esiste) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idParticella", idParticella),
          new Parametro("idFase", idFase)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::existIstanzaEsameAttivaFase] ",
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
              "[DocumentoGaaDAO::existIstanzaEsameAttivaFase] END.");
    }
  }
  
  public FaseRiesameDocumentoVO getFaseRiesameDocumentoByIdDocumento(long idDocumento) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    FaseRiesameDocumentoVO faseRiesameDocumentoVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getFaseRiesameDocumentoByIdDocumento] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT FRD.ID_FASE_RIESAME_TP_DOCUMENTO," +
        "       FRD.ID_FASE_ISTANZA_RIESAME, " +
        "       FRD.DATA_INIZIO_VALIDITA, " +
        "       FRD.EXTRA_SISTEMA " +
        "FROM   DB_R_FASE_RIESAME_TP_DOCUMENTO FRD " +
        "WHERE  FRD.ID_DOCUMENTO = ?  " +
        "AND    FRD.DATA_FINE_VALIDITA IS NULL ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getFaseRiesameDocumentoByIdDocumento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idDocumento);
      
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        faseRiesameDocumentoVO = new FaseRiesameDocumentoVO();
        faseRiesameDocumentoVO.setIdFaseRiesameTpDocumento(rs.getLong("ID_FASE_RIESAME_TP_DOCUMENTO"));
        faseRiesameDocumentoVO.setIdFaseIstanzaRiesame(rs.getInt("ID_FASE_ISTANZA_RIESAME"));
        faseRiesameDocumentoVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        faseRiesameDocumentoVO.setExtraSistema(rs.getString("EXTRA_SISTEMA"));
        
      }
      
      return faseRiesameDocumentoVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("faseRiesameDocumentoVO", faseRiesameDocumentoVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getFaseRiesameDocumentoByIdDocumento] ",
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
              "[DocumentoGaaDAO::getFaseRiesameDocumentoByIdDocumento] END.");
    }
  }  
  
  public boolean isParticellaInPotenziale(long idAzienda, 
      long idParticella, int anno) 
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
              "[DocumentoGaaDAO::isParticellaInPotenziale] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IRP.ID_PARTICELLA " +
        "FROM   DB_ISTANZA_RIESAME_POTENZIALE IRP " +
        "WHERE  IRP.ID_AZIENDA = ?  " +
        "AND    IRP.ID_PARTICELLA = ? " +
        "AND    IRP.ANNO_ISTANZA = ? " +
        "AND    (IRP.ANNO_PRATICA = IRP.ANNO_ISTANZA " +
        "        OR IRP.ANNO_PRATICA IS NULL) " +
        "AND    IRP.DATA_FINE_VALIDITA IS NULL " +
        "AND    IRP.ID_ISTANZA_RIESAME IS NULL ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isParticellaInPotenziale] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idParticella);
      stmt.setInt(++indice, anno);
      
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
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
        new Variabile("trovato", trovato) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idParticella", idParticella),
          new Parametro("anno", anno)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isParticellaInPotenziale] ",
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
              "[DocumentoGaaDAO::isParticellaInPotenziale] END.");
    }
  }
  
  public void updateIstanzaPotenziale(long idAzienda, long idParticella, 
    int anno, long idIstanzaRiesame, long idUtente) 
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
              "[DocumentoGaaDAO::updateIstanzaPotenziale] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
          "UPDATE DB_ISTANZA_RIESAME_POTENZIALE " +
          "   SET ID_ISTANZA_RIESAME = ?, " +
          "       ID_UTENTE_AGGIORNAMENTO = ?, " +
          "       DATA_AGGIORNAMENTO = SYSDATE " +
          "WHERE  ID_AZIENDA = ? " +
          "AND    ID_PARTICELLA = ? " +
          "AND    ANNO_ISTANZA = ? " +
          "AND    ID_ISTANZA_RIESAME IS NULL ");         
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::updateIstanzaPotenziale] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idIstanzaRiesame);
      stmt.setLong(++indice, idUtente);
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idParticella);
      stmt.setInt(++indice, anno);
     
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella),
        new Parametro("anno", anno),
        new Parametro("idIstanzaRiesame", idIstanzaRiesame)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::updateIstanzaPotenziale] ",
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
              "[DocumentoGaaDAO::updateIstanzaPotenziale] END.");
    }
  }
  
  /**
   * Particella legata azienda buon aper il contradittorio e non in p26
   * 
   * @param idAzienda
   * @param idParticella
   * @param anno
   * @return
   * @throws DataAccessException
   */
  public boolean isParticellaInPotenzialeContra(long idAzienda, 
      long idParticella, int anno) 
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
              "[DocumentoGaaDAO::isParticellaInPotenzialeContra] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IRP.ID_PARTICELLA " +
        "FROM   DB_ISTANZA_RIESAME_POTENZIALE IRP " +
        "WHERE  IRP.ID_AZIENDA = ?  " +
        "AND    IRP.ID_PARTICELLA = ? " +
        "AND    IRP.ANNO_ISTANZA = ? " +
        "AND    IRP.DATA_FINE_VALIDITA IS NULL " +
        "AND    IRP.ID_ISTANZA_RIESAME IS NOT NULL ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isParticellaInPotenzialeContra] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idParticella);
      stmt.setInt(++indice, anno);
      
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
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
        new Variabile("trovato", trovato) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idParticella", idParticella),
          new Parametro("anno", anno)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isParticellaInPotenzialeContra] ",
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
              "[DocumentoGaaDAO::isParticellaInPotenzialeContra] END.");
    }
  }
  
  
  public IstanzaRiesameVO getIstanzaRiesameFaseEvasa(long idAzienda, long idParticella, 
    int fase, int anno) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    IstanzaRiesameVO istanzaRiesameVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getIstanzaRiesameFaseEvasa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IR.ID_ISTANZA_RIESAME, " +
        "       IR.PROTOCOLLO_ISTANZA " +
        "FROM   DB_ISTANZA_RIESAME IR " +
        "WHERE  IR.ID_AZIENDA = ? " +
        "AND    IR.ID_PARTICELLA = ? " +
        "AND    IR.ID_FASE_ISTANZA_RIESAME = ? " +
        "AND    IR.ANNO = ? " +
        "AND    IR.DATA_EVASIONE IS NOT NULL ");             
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getIstanzaRiesameFaseEvasa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idParticella);
      stmt.setInt(++indice, fase);
      stmt.setInt(++indice, anno);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        istanzaRiesameVO = new IstanzaRiesameVO();
        
        istanzaRiesameVO.setIdIstanzaRiesame(rs.getLong("ID_ISTANZA_RIESAME"));
        istanzaRiesameVO.setProtocolloIstanza(rs.getString("PROTOCOLLO_ISTANZA"));        
      }
      
      return istanzaRiesameVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("istanzaRiesameVO", istanzaRiesameVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella),
        new Parametro("fase", fase),
      new Parametro("anno", anno)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getIstanzaRiesameFaseEvasa] ",
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
              "[DocumentoGaaDAO::getIstanzaRiesameFaseEvasa] END.");
    }
  }
  
  
  public void updateIstanzaProtocolloFasePrec(long idIstanzaRiesame, String protocolloIstanza) 
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
              "[DocumentoGaaDAO::updateIstanzaProtocolloFasePrec] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
          "UPDATE DB_ISTANZA_RIESAME " +
          "   SET PROTOCOLLO_ISTANZA = ? " +
          "WHERE  ID_ISTANZA_RIESAME = ? ");         
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::updateIstanzaProtocolloFasePrec] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setString(++indice, protocolloIstanza);
      stmt.setLong(++indice, idIstanzaRiesame);
      
     
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("protocolloIstanza", protocolloIstanza),
        new Parametro("idIstanzaRiesame", idIstanzaRiesame) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::updateIstanzaProtocolloFasePrec] ",
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
              "[DocumentoGaaDAO::updateIstanzaProtocolloFasePrec] END.");
    }
  }
  
  
  /**
   * ritorna un record
   * se per azienda, particella anno e fase esiste ancora
   * un'istanza di riesame non conclusa attiva oppure
   * se l'sitanza ri riesame è conlusa ma sono passati 
   * troppi giorni identificati dal parametro.
   * Cioè nei casio ch enon è possibile creare un istanza per la fase successiva
   * 
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @param idFase
   * @param anno
   * @param parametro
   * @return
   * @throws DataAccessException
   */
  public boolean isNotPossibleIstanzaRiesameFaseSuccessiva(long idAzienda, 
      long idParticella, int idFase, int anno, String parametro) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean esiste = false;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::isNotPossibleIstanzaRiesameFaseSuccessiva] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT IR.ID_ISTANZA_RIESAME " +
        "FROM   DB_ISTANZA_RIESAME IR, " +
        "       DB_ALTRI_DATI AD, " +
        "       DB_ALTRI_DATI AD2 " +
        "WHERE  IR.ID_AZIENDA = ?  " +
        "AND    IR.ID_PARTICELLA = ? " +
        "AND    IR.ID_FASE_ISTANZA_RIESAME = ? " +
        "AND    IR.ANNO = ? " +
        "AND    AD.CODICE = ? " +
        "AND    AD2.CODICE = 'DATA_X_ISTANZA' "+
        "AND    IR.DATA_ANNULLAMENTO IS NULL " +
        "AND    (IR.DATA_EVASIONE IS NULL " +
        "        OR  (IR.DATA_EVASIONE IS NOT NULL " +
        "             AND    DECODE(SIGN(AD2.VALORE_DATA - IR.DATA_AGGIORNAMENTO), 1, AD2.VALORE_DATA, IR.DATA_AGGIORNAMENTO)  < (SYSDATE  - AD.VALORE_NUMERICO) " +
        "        ))");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::isNotPossibleIstanzaRiesameFaseSuccessiva] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idParticella);
      stmt.setInt(++indice, idFase);
      stmt.setInt(++indice, anno);
      stmt.setString(++indice, parametro);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        esiste = true;
      }
      
      return esiste;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("esiste", esiste) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idParticella", idParticella),
          new Parametro("idFase", idFase),
          new Parametro("anno", anno),
          new Parametro("parametro", parametro)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::isNotPossibleIstanzaRiesameFaseSuccessiva] ",
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
              "[DocumentoGaaDAO::isNotPossibleIstanzaRiesameFaseSuccessiva] END.");
    }
  }
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdDichiarazione(
      long idDichiarazioneConsistenza, long idTipoAllegato) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AllegatoDichiarazioneVO allegatoDichiarazioneVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getAllegatoDichiarazioneFromIdDichiarazione] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT AD.ID_ALLEGATO_DICHIARAZIONE, " +
        "       AL.ID_ALLEGATO, " +
        "       AL.EXT_ID_DOCUMENTO_INDEX, " +
        "       AL.ID_TIPO_FIRMA, " +
        "       TF.DESCRIZIONE_TIPO_FIRMA, " +
        "       AD.ID_DICHIARAZIONE_CONSISTENZA, " +
        "       AL.DATA_ULTIMO_AGGIORNAMENTO, " +
        "       AL.ID_UTENTE_AGGIORNAMENTO, " +
        "       AD.DATA_INIZIO_VALIDITA, " +
        "       TA.DESCRIZIONE_TIPO_ALLEGATO " +
        "FROM   DB_ALLEGATO AL, " +
        "       DB_ALLEGATO_DICHIARAZIONE AD, " +
        "       DB_TIPO_FIRMA TF, " +
        "       DB_TIPO_ALLEGATO TA " +
        "WHERE  AD.ID_DICHIARAZIONE_CONSISTENZA = ?  " +
        "AND    AD.ID_ALLEGATO = AL.ID_ALLEGATO " +
        "AND    AL.ID_TIPO_ALLEGATO = ? " +
        "AND    AD.DATA_FINE_VALIDITA IS NULL " +
        "AND    AL.ID_TIPO_FIRMA = TF.ID_TIPO_FIRMA (+) " +
        "AND    AL.ID_TIPO_ALLEGATO = TA.ID_TIPO_ALLEGATO ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getAllegatoDichiarazioneFromIdDichiarazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      stmt.setLong(++indice, idTipoAllegato);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        allegatoDichiarazioneVO = new AllegatoDichiarazioneVO();
        allegatoDichiarazioneVO.setIdAllegatoDichiarazione(new Long(rs.getLong("ID_ALLEGATO_DICHIARAZIONE")));
        allegatoDichiarazioneVO.setIdAllegato(new Long(rs.getLong("ID_ALLEGATO")));
        allegatoDichiarazioneVO.setExtIdDocumentoIndex(checkLongNull(rs.getString("EXT_ID_DOCUMENTO_INDEX")));
        allegatoDichiarazioneVO.setIdTipoFirma(checkLongNull(rs.getString("ID_TIPO_FIRMA")));
        allegatoDichiarazioneVO.setDescTipoFirma(rs.getString("DESCRIZIONE_TIPO_FIRMA"));
        allegatoDichiarazioneVO.setIdDichiarazioneConsistenza(new Long(rs.getLong("ID_DICHIARAZIONE_CONSISTENZA")));
        allegatoDichiarazioneVO.setDataUltimoAggiornamento(rs.getTimestamp("DATA_ULTIMO_AGGIORNAMENTO"));
        allegatoDichiarazioneVO.setIdUtenteAggiornamento(checkLongNull(rs.getString("ID_UTENTE_AGGIORNAMENTO")));
        allegatoDichiarazioneVO.setDescTipoAllegato(rs.getString("DESCRIZIONE_TIPO_ALLEGATO"));
        allegatoDichiarazioneVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
      }
      
      return allegatoDichiarazioneVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("allegatoDichiarazioneVO", allegatoDichiarazioneVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza),
          new Parametro("idTipoAllegato", idTipoAllegato)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getAllegatoDichiarazioneFromIdDichiarazione] ",
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
              "[DocumentoGaaDAO::getAllegatoDichiarazioneFromIdDichiarazione] END.");
    }
  }
  
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdAllegato(
      long idAllegato) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AllegatoDichiarazioneVO allegatoDichiarazioneVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getAllegatoDichiarazioneFromIdAllegato] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT AD.ID_ALLEGATO_DICHIARAZIONE, " +
        "       AL.ID_ALLEGATO, " +
        "       AL.EXT_ID_DOCUMENTO_INDEX, " +
        "       AL.ID_TIPO_FIRMA, " +
        "       TF.DESCRIZIONE_TIPO_FIRMA, " +
        "       AD.ID_DICHIARAZIONE_CONSISTENZA, " +
        "       AL.DATA_ULTIMO_AGGIORNAMENTO, " +
        "       AL.ID_UTENTE_AGGIORNAMENTO, " +
        "       AD.DATA_INIZIO_VALIDITA, " +
        "       TA.DESCRIZIONE_TIPO_ALLEGATO " +
        "FROM   DB_ALLEGATO AL, " +
        "       DB_ALLEGATO_DICHIARAZIONE AD, " +
        "       DB_TIPO_FIRMA TF, " +
        "       DB_TIPO_ALLEGATO TA " +
        "WHERE  AL.ID_ALLEGATO = ?  " +
        "AND    AD.ID_ALLEGATO = AL.ID_ALLEGATO " +
        "AND    AL.ID_TIPO_FIRMA = TF.ID_TIPO_FIRMA (+) " +
        "AND    AL.ID_TIPO_ALLEGATO = TA.ID_TIPO_ALLEGATO ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getAllegatoDichiarazioneFromIdAllegato] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllegato);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        allegatoDichiarazioneVO = new AllegatoDichiarazioneVO();
        allegatoDichiarazioneVO.setIdAllegatoDichiarazione(new Long(rs.getLong("ID_ALLEGATO_DICHIARAZIONE")));
        allegatoDichiarazioneVO.setIdAllegato(new Long(rs.getLong("ID_ALLEGATO")));
        allegatoDichiarazioneVO.setExtIdDocumentoIndex(checkLongNull(rs.getString("EXT_ID_DOCUMENTO_INDEX")));
        allegatoDichiarazioneVO.setIdTipoFirma(checkLongNull(rs.getString("ID_TIPO_FIRMA")));
        allegatoDichiarazioneVO.setDescTipoFirma(rs.getString("DESCRIZIONE_TIPO_FIRMA"));
        allegatoDichiarazioneVO.setIdDichiarazioneConsistenza(new Long(rs.getLong("ID_DICHIARAZIONE_CONSISTENZA")));
        allegatoDichiarazioneVO.setDataUltimoAggiornamento(rs.getTimestamp("DATA_ULTIMO_AGGIORNAMENTO"));
        allegatoDichiarazioneVO.setIdUtenteAggiornamento(checkLongNull(rs.getString("ID_UTENTE_AGGIORNAMENTO")));
        allegatoDichiarazioneVO.setDescTipoAllegato(rs.getString("DESCRIZIONE_TIPO_ALLEGATO"));
        allegatoDichiarazioneVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
      }
      
      return allegatoDichiarazioneVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("allegatoDichiarazioneVO", allegatoDichiarazioneVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  new Parametro("idAllegato", idAllegato)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getAllegatoDichiarazioneFromIdAllegato] ",
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
              "[DocumentoGaaDAO::getAllegatoDichiarazioneFromIdAllegato] END.");
    }
  }
  
  /**
   * Utilizzato nel protocolla dichiarazione per beccare l'ultimo storicizzato per fare upload del file...
   * 
   * 
   * 
   * @param idDichiarazioneConsistenza
   * @param idTipoAllegato
   * @return
   * @throws DataAccessException
   */
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric(
      long idDichiarazioneConsistenza, long idTipoAllegato) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AllegatoDichiarazioneVO allegatoDichiarazioneVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT AD.ID_ALLEGATO_DICHIARAZIONE, " +
        "       AL.ID_ALLEGATO, " +
        "       AL.EXT_ID_DOCUMENTO_INDEX, " +
        "       AL.ID_TIPO_FIRMA, " +
        "       TF.DESCRIZIONE_TIPO_FIRMA, " +
        "       AD.ID_DICHIARAZIONE_CONSISTENZA " +
        "FROM   DB_ALLEGATO AL, " +
        "       DB_ALLEGATO_DICHIARAZIONE AD, " +
        "       DB_TIPO_FIRMA TF " +
        "WHERE  AD.ID_DICHIARAZIONE_CONSISTENZA = ?  " +
        "AND    AD.ID_ALLEGATO = AL.ID_ALLEGATO " +
        "AND    AL.ID_TIPO_ALLEGATO = ? " +
        "AND    AD.ID_ALLEGATO_DICHIARAZIONE = (SELECT MAX(AD2.ID_ALLEGATO_DICHIARAZIONE) " +
        "                                       FROM   DB_ALLEGATO_DICHIARAZIONE AD2, " +
        "                                              DB_ALLEGATO AL2 " +
        "                                       WHERE  AD2.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "                                       AND    AD2.ID_ALLEGATO = AL2.ID_ALLEGATO " +
        "                                       AND    AD2.DATA_FINE_VALIDITA IS NOT NULL " +
        "                                       AND    AL2.ID_TIPO_ALLEGATO = ? ) " +
        "AND    AL.ID_TIPO_FIRMA = TF.ID_TIPO_FIRMA (+) ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      stmt.setLong(++indice, idTipoAllegato);
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      stmt.setLong(++indice, idTipoAllegato);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        allegatoDichiarazioneVO = new AllegatoDichiarazioneVO();
        allegatoDichiarazioneVO.setIdAllegatoDichiarazione(new Long(rs.getLong("ID_ALLEGATO_DICHIARAZIONE")));
        allegatoDichiarazioneVO.setIdAllegato(new Long(rs.getLong("ID_ALLEGATO")));
        allegatoDichiarazioneVO.setExtIdDocumentoIndex(checkLongNull(rs.getString("EXT_ID_DOCUMENTO_INDEX")));
        allegatoDichiarazioneVO.setIdTipoFirma(checkLongNull(rs.getString("ID_TIPO_FIRMA")));
        allegatoDichiarazioneVO.setDescTipoFirma(rs.getString("DESCRIZIONE_TIPO_FIRMA"));
        allegatoDichiarazioneVO.setIdDichiarazioneConsistenza(new Long(rs.getLong("ID_DICHIARAZIONE_CONSISTENZA")));
      }
      
      return allegatoDichiarazioneVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("allegatoDichiarazioneVO", allegatoDichiarazioneVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza),
          new Parametro("idTipoAllegato", idTipoAllegato)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric] ",
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
              "[DocumentoGaaDAO::getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric] END.");
    }
  }
  
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromTipoStampaAndIdDichiarazione(
      long idDichiarazioneConsistenza, String tipoStampa) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AllegatoDichiarazioneVO allegatoDichiarazioneVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getAllegatoDichiarazioneFromTipoStampaAndIdDichiarazione] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT AD.ID_ALLEGATO_DICHIARAZIONE, " +
        "       AL.ID_ALLEGATO, " +
        "       AL.EXT_ID_DOCUMENTO_INDEX, " +
        "       AL.ID_TIPO_FIRMA, " +
        "       TF.DESCRIZIONE_TIPO_FIRMA, " +
        "       AD.ID_DICHIARAZIONE_CONSISTENZA " +
        "FROM   DB_ALLEGATO AL, " +
        "       DB_ALLEGATO_DICHIARAZIONE AD, " +
        "       DB_TIPO_FIRMA TF, " +
        "       DB_TIPO_REPORT TR " +
        "WHERE  AD.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND    AD.ID_ALLEGATO = AL.ID_ALLEGATO " +
        "AND    TR.CODICE_REPORT = ? " +
        "AND    TR.ID_TIPO_ALLEGATO = AL.ID_TIPO_ALLEGATO " +
        "AND    AD.DATA_FINE_VALIDITA IS NULL " +
        "AND    AL.ID_TIPO_FIRMA = TF.ID_TIPO_FIRMA (+) ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getAllegatoDichiarazioneFromTipoStampaAndIdDichiarazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      stmt.setString(++indice, tipoStampa);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        allegatoDichiarazioneVO = new AllegatoDichiarazioneVO();
        allegatoDichiarazioneVO.setIdAllegatoDichiarazione(new Long(rs.getLong("ID_ALLEGATO_DICHIARAZIONE")));
        allegatoDichiarazioneVO.setIdAllegato(new Long(rs.getLong("ID_ALLEGATO")));
        allegatoDichiarazioneVO.setExtIdDocumentoIndex(new Long(rs.getLong("EXT_ID_DOCUMENTO_INDEX")));
        allegatoDichiarazioneVO.setIdTipoFirma(checkLongNull(rs.getString("ID_TIPO_FIRMA")));
        allegatoDichiarazioneVO.setDescTipoFirma(rs.getString("DESCRIZIONE_TIPO_FIRMA"));
        allegatoDichiarazioneVO.setIdDichiarazioneConsistenza(new Long(rs.getLong("ID_DICHIARAZIONE_CONSISTENZA")));
      }
      
      return allegatoDichiarazioneVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("allegatoDichiarazioneVO", allegatoDichiarazioneVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza),
          new Parametro("tipoStampa", tipoStampa)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getAllegatoDichiarazioneFromTipoStampaAndIdDichiarazione] ",
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
              "[DocumentoGaaDAO::getAllegatoDichiarazioneFromTipoStampaAndIdDichiarazione] END.");
    }
  }
  
  public Vector<AllegatoDichiarazioneVO> getElencoAllegatoDichiarazioneFromIdDichiarazione(
      long idDichiarazioneConsistenza) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AllegatoDichiarazioneVO> vAllegatoDichiarazioneVO = null;
    AllegatoDichiarazioneVO allegatoDichiarazioneVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getElencoAllegatoDichiarazioneFromIdDichiarazione] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT AD.ID_ALLEGATO_DICHIARAZIONE, " +
        "       AL.ID_ALLEGATO, " +
        "       AL.EXT_ID_DOCUMENTO_INDEX, " +
        "       AL.ID_TIPO_FIRMA, " +
        "       AD.ID_DICHIARAZIONE_CONSISTENZA " +
        "FROM   DB_ALLEGATO AL, " +
        "       DB_ALLEGATO_DICHIARAZIONE AD " +
        "WHERE  AD.ID_DICHIARAZIONE_CONSISTENZA = ?  " +
        "AND    AD.ID_ALLEGATO = AL.ID_ALLEGATO ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getElencoAllegatoDichiarazioneFromIdDichiarazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      SolmrLogger.debug(this, "-- idDichiarazioneConsistenza ="+idDichiarazioneConsistenza);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vAllegatoDichiarazioneVO == null)
          vAllegatoDichiarazioneVO = new Vector<AllegatoDichiarazioneVO>();
        
        allegatoDichiarazioneVO = new AllegatoDichiarazioneVO();
        allegatoDichiarazioneVO.setIdAllegatoDichiarazione(new Long(rs.getLong("ID_ALLEGATO_DICHIARAZIONE")));
        allegatoDichiarazioneVO.setIdAllegato(new Long(rs.getLong("ID_ALLEGATO")));
        
        String extIdDocumentoIndex = rs.getString("EXT_ID_DOCUMENTO_INDEX");
        SolmrLogger.debug(this, "-- extIdDocumentoIndex ="+extIdDocumentoIndex);
        
        if(Validator.isNotEmpty(extIdDocumentoIndex))
          allegatoDichiarazioneVO.setExtIdDocumentoIndex(new Long(rs.getLong("EXT_ID_DOCUMENTO_INDEX")));
        
        allegatoDichiarazioneVO.setIdTipoFirma(checkLongNull(rs.getString("ID_TIPO_FIRMA")));
        allegatoDichiarazioneVO.setIdDichiarazioneConsistenza(new Long(rs.getLong("ID_DICHIARAZIONE_CONSISTENZA")));
      
        vAllegatoDichiarazioneVO.add(allegatoDichiarazioneVO);
      }
      
      return vAllegatoDichiarazioneVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("allegatoDichiarazioneVO", allegatoDichiarazioneVO),
        new Variabile("vAllegatoDichiarazioneVO", vAllegatoDichiarazioneVO)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getElencoAllegatoDichiarazioneFromIdDichiarazione] ",
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
              "[DocumentoGaaDAO::getElencoAllegatoDichiarazioneFromIdDichiarazione] END.");
    }
  }
  
  
  public Vector<AllegatoDichiarazioneVO> getElencoAllegatiAttiviDichiarazione(
      long idDichiarazioneConsistenza) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AllegatoDichiarazioneVO> vAllegatoDichiarazioneVO = null;
    AllegatoDichiarazioneVO allegatoDichiarazioneVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getElencoAllegatiAttiviDichiarazione] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT AD.ID_ALLEGATO_DICHIARAZIONE, " +
        "       AL.ID_ALLEGATO, " +
        "       AL.EXT_ID_DOCUMENTO_INDEX, " +
        "       AL.ID_TIPO_FIRMA, " +
        "       AD.ID_DICHIARAZIONE_CONSISTENZA, " +
        "       TA.FLAG_INSERIBILE, " +
        "       TA.ID_TIPO_ALLEGATO " +
        "FROM   DB_ALLEGATO AL, " +
        "       DB_ALLEGATO_DICHIARAZIONE AD, " +
        "       DB_TIPO_ALLEGATO TA " +
        "WHERE  AD.ID_DICHIARAZIONE_CONSISTENZA = " +idDichiarazioneConsistenza +" "+
        "AND    AD.ID_ALLEGATO = AL.ID_ALLEGATO " +
        "AND    AD.DATA_FINE_VALIDITA IS NULL " +
        "AND    AL.ID_TIPO_ALLEGATO = TA.ID_TIPO_ALLEGATO " +
        "AND    TA.FLAG_INSERIBILE <> 'N' ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getElencoAllegatiAttiviDichiarazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      /*int indice = 0;
      stmt.setLong(++indice, idDichiarazioneConsistenza);*/
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vAllegatoDichiarazioneVO == null)
          vAllegatoDichiarazioneVO = new Vector<AllegatoDichiarazioneVO>();
        
        allegatoDichiarazioneVO = new AllegatoDichiarazioneVO();
        allegatoDichiarazioneVO.setIdAllegatoDichiarazione(new Long(rs.getLong("ID_ALLEGATO_DICHIARAZIONE")));
        allegatoDichiarazioneVO.setIdAllegato(new Long(rs.getLong("ID_ALLEGATO")));
        allegatoDichiarazioneVO.setExtIdDocumentoIndex(checkLongNull(rs.getString("EXT_ID_DOCUMENTO_INDEX")));
        allegatoDichiarazioneVO.setIdTipoFirma(checkLongNull(rs.getString("ID_TIPO_FIRMA")));
        allegatoDichiarazioneVO.setIdDichiarazioneConsistenza(new Long(rs.getLong("ID_DICHIARAZIONE_CONSISTENZA")));
        allegatoDichiarazioneVO.setFlagInseribile(rs.getString("FLAG_INSERIBILE"));
        allegatoDichiarazioneVO.setIdTipoAllegato(checkLongNull(rs.getString("ID_TIPO_ALLEGATO")));
        
        vAllegatoDichiarazioneVO.add(allegatoDichiarazioneVO);
      }
      
      return vAllegatoDichiarazioneVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("allegatoDichiarazioneVO", allegatoDichiarazioneVO),
        new Variabile("vAllegatoDichiarazioneVO", vAllegatoDichiarazioneVO)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getElencoAllegatiAttiviDichiarazione] ",
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
              "[DocumentoGaaDAO::getElencoAllegatiAttiviDichiarazione] END.");
    }
  }
  
  
  public Vector<AllegatoDichiarazioneVO> getAllElencoAllegatiDichiarazione(
      long idDichiarazioneConsistenza) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AllegatoDichiarazioneVO> vAllegatoDichiarazioneVO = null;
    AllegatoDichiarazioneVO allegatoDichiarazioneVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getElencoAllegatiAttiviDichiarazione] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT AD.ID_ALLEGATO_DICHIARAZIONE, " +
        "       AL.ID_ALLEGATO, " +
        "       AL.EXT_ID_DOCUMENTO_INDEX, " +
        "       AL.ID_TIPO_FIRMA, " +
        "       AL.DATA_ULTIMO_AGGIORNAMENTO, " +
        "       AD.ID_DICHIARAZIONE_CONSISTENZA, " +
        "       TA.FLAG_INSERIBILE, " +
        "       TA.ID_TIPO_ALLEGATO, " +
        "       TA.DESCRIZIONE_TIPO_ALLEGATO, " +
        "       AD.DATA_INIZIO_VALIDITA, " +
        "       TF.DESCRIZIONE_TIPO_FIRMA " +
        "FROM   DB_ALLEGATO AL, " +
        "       DB_ALLEGATO_DICHIARAZIONE AD, " +
        "       DB_TIPO_FIRMA TF, " +
        "       DB_TIPO_ALLEGATO TA " +
        "WHERE  AD.ID_DICHIARAZIONE_CONSISTENZA = ? "+
        "AND    AD.ID_ALLEGATO = AL.ID_ALLEGATO " +
        "AND    AD.DATA_FINE_VALIDITA IS NULL " +
        "AND    AL.ID_TIPO_ALLEGATO = TA.ID_TIPO_ALLEGATO " +
        "AND    AL.ID_TIPO_FIRMA = TF.ID_TIPO_FIRMA (+) " +
        "ORDER BY DESCRIZIONE_TIPO_ALLEGATO ");
      
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getElencoAllegatiAttiviDichiarazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vAllegatoDichiarazioneVO == null)
          vAllegatoDichiarazioneVO = new Vector<AllegatoDichiarazioneVO>();
        
        allegatoDichiarazioneVO = new AllegatoDichiarazioneVO();
        allegatoDichiarazioneVO.setIdAllegatoDichiarazione(new Long(rs.getLong("ID_ALLEGATO_DICHIARAZIONE")));
        allegatoDichiarazioneVO.setIdAllegato(new Long(rs.getLong("ID_ALLEGATO")));
        allegatoDichiarazioneVO.setExtIdDocumentoIndex(checkLongNull(rs.getString("EXT_ID_DOCUMENTO_INDEX")));
        allegatoDichiarazioneVO.setIdTipoFirma(checkLongNull(rs.getString("ID_TIPO_FIRMA")));
        allegatoDichiarazioneVO.setIdDichiarazioneConsistenza(new Long(rs.getLong("ID_DICHIARAZIONE_CONSISTENZA")));
        allegatoDichiarazioneVO.setFlagInseribile(rs.getString("FLAG_INSERIBILE"));
        allegatoDichiarazioneVO.setIdTipoAllegato(checkLongNull(rs.getString("ID_TIPO_ALLEGATO")));
        allegatoDichiarazioneVO.setDataUltimoAggiornamento(rs.getTimestamp("DATA_ULTIMO_AGGIORNAMENTO"));
        allegatoDichiarazioneVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        allegatoDichiarazioneVO.setDescTipoAllegato(rs.getString("DESCRIZIONE_TIPO_ALLEGATO"));
        allegatoDichiarazioneVO.setDescTipoFirma(rs.getString("DESCRIZIONE_TIPO_FIRMA"));
        
        vAllegatoDichiarazioneVO.add(allegatoDichiarazioneVO);
      }
      
      return vAllegatoDichiarazioneVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("allegatoDichiarazioneVO", allegatoDichiarazioneVO),
        new Variabile("vAllegatoDichiarazioneVO", vAllegatoDichiarazioneVO)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getElencoAllegatiAttiviDichiarazione] ",
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
              "[DocumentoGaaDAO::getElencoAllegatiAttiviDichiarazione] END.");
    }
  }
  
  
  
  public void storicizzaAllegatoDichiarazione(long idAllegatoDichiarazione) 
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
              "[DocumentoGaaDAO::storicizzaAllegatoDichiarazione] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
          "UPDATE DB_ALLEGATO_DICHIARAZIONE " +
          "   SET DATA_FINE_VALIDITA = SYSDATE " +
          "WHERE  ID_ALLEGATO_DICHIARAZIONE = ? ");         
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::storicizzaAllegatoDichiarazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllegatoDichiarazione);
      
     
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAllegatoDichiarazione", idAllegatoDichiarazione) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::storicizzaAllegatoDichiarazione] ",
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
              "[DocumentoGaaDAO::storicizzaAllegatoDichiarazione] END.");
    }
  }
  
  /**
   * Storicizza gli allegati automatici alla protocollazione...
   * 
   * 
   * @param idDichiarazioneConsistenza
   * @throws DataAccessException
   */
  public void storicizzaAllegatiAutomaticiDichiarazione(long idDichiarazioneConsistenza) 
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
              "[DocumentoGaaDAO::storicizzaAllegatiAutomaticiDichiarazione] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
          "UPDATE DB_ALLEGATO_DICHIARAZIONE " +
          "   SET DATA_FINE_VALIDITA = SYSDATE " +
          "WHERE  ID_ALLEGATO_DICHIARAZIONE " +
          "       IN (SELECT AD.ID_ALLEGATO_DICHIARAZIONE " +
          "           FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
          "                  DB_ALLEGATO AL, " +
          "                  DB_ALLEGATO_DICHIARAZIONE AD, " +
          "                  DB_TIPO_ALLEGATO TA " +
          "           WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "           AND    DC.ID_DICHIARAZIONE_CONSISTENZA = AD.ID_DICHIARAZIONE_CONSISTENZA  " +
          "           AND    AD.DATA_FINE_VALIDITA IS NULL " +
          "           AND    AD.ID_ALLEGATO = AL.ID_ALLEGATO " +
          "           AND    AL.ID_TIPO_ALLEGATO = TA.ID_TIPO_ALLEGATO " +
          "           AND    TA.FLAG_INSERIBILE = 'A') ");         
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::storicizzaAllegatiAutomaticiDichiarazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      
     
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::storicizzaAllegatiAutomaticiDichiarazione] ",
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
              "[DocumentoGaaDAO::storicizzaAllegatiAutomaticiDichiarazione] END.");
    }
  }
  
  public void updateAllegatoForFirma(long idAllegato, long idUtente, Long idTipoFirma) 
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
              "[DocumentoGaaDAO::updateAllegatoForFirma] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
          "UPDATE DB_ALLEGATO " +
          "   SET ID_TIPO_FIRMA = ?, " +
          "       ID_UTENTE_AGGIORNAMENTO = ?, " +
          "       DATA_ULTIMO_AGGIORNAMENTO = SYSDATE " +
          "WHERE  ID_ALLEGATO = ? ");         
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::updateAllegatoForFirma] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(idTipoFirma));
      stmt.setLong(++indice, idUtente);
      stmt.setLong(++indice, idAllegato);
     
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoFirma", idTipoFirma),
        new Parametro("idUtente", idUtente),
        new Parametro("idAllegato", idAllegato) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::updateAllegatoForFirma] ",
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
              "[DocumentoGaaDAO::updateAllegatoForFirma] END.");
    }
  }
  
  public void updateAllegatoForExtIdDocIndex(long idAllegato, long idUtente, Long extIdDocumentoIndex) 
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
              "[DocumentoGaaDAO::updateAllegatoForExtIdDocIndex] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
          "UPDATE DB_ALLEGATO " +
          "   SET EXT_ID_DOCUMENTO_INDEX = ?, " +
          "       ID_UTENTE_AGGIORNAMENTO = ?, " +
          "       DATA_ULTIMO_AGGIORNAMENTO = SYSDATE " +
          "WHERE  ID_ALLEGATO = ? ");         
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::updateAllegatoForExtIdDocIndex] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(extIdDocumentoIndex));
      stmt.setLong(++indice, idUtente);
      stmt.setLong(++indice, idAllegato);
     
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("extIdDocumentoIndex", extIdDocumentoIndex),
        new Parametro("idUtente", idUtente),
        new Parametro("idAllegato", idAllegato) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::updateAllegatoForExtIdDocIndex] ",
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
              "[DocumentoGaaDAO::updateAllegatoForExtIdDocIndex] END.");
    }
  }
  
  
  public void deleteAllegatoFromVId(Vector<Long> vIdAllegato) 
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
              "[DocumentoGaaDAO::deleteAllegatoFromVId] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      String parametri=" (";
      
      for(int i=0;i<vIdAllegato.size();i++)
      {
        parametri+="?";
        if (i<vIdAllegato.size()-1)
            parametri+=",";
      }
      parametri+=") ";
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE DB_ALLEGATO " +
        "WHERE  ID_ALLEGATO IN "+parametri);
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::deleteAllegatoFromVId] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      for(int i=0;i<vIdAllegato.size();i++)
        stmt.setLong(++idx, vIdAllegato.get(i).longValue());
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdAllegato", vIdAllegato)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::deleteAllegatoFromVId] ",
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
              "[DocumentoGaaDAO::deleteAllegatoFromVId] END.");
    }
  }
  
  public void deleteAllegatoDichiarazioneFromVId(Vector<Long> vIdAllegatoDichiarazione) 
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
              "[DocumentoGaaDAO::deleteAllegatoDichiarazioneFromVId] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      String parametri=" (";
      
      for(int i=0;i<vIdAllegatoDichiarazione.size();i++)
      {
        parametri+="?";
        if (i<vIdAllegatoDichiarazione.size()-1)
            parametri+=",";
      }
      parametri+=") ";
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE DB_ALLEGATO_DICHIARAZIONE " +
        "WHERE  ID_ALLEGATO_DICHIARAZIONE IN "+parametri);
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::deleteAllegatoDichiarazioneFromVId] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      for(int i=0;i<vIdAllegatoDichiarazione.size();i++)
        stmt.setLong(++idx, vIdAllegatoDichiarazione.get(i).longValue());
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdAllegatoDichiarazione", vIdAllegatoDichiarazione)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::deleteAllegatoDichiarazioneFromVId] ",
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
              "[DocumentoGaaDAO::deleteAllegatoDichiarazioneFromVId] END.");
    }
  }
  
  
  public HashMap<Long, Vector<AllegatoDichiarazioneVO>> getAllegatiDichiarazioneInseriti(
      long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    HashMap<Long, Vector<AllegatoDichiarazioneVO>> hAllegValid = null;
    Vector<AllegatoDichiarazioneVO> vAllegatoDichiarazioneVO = null;
    AllegatoDichiarazioneVO allegatoDichiarazioneVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::getAllegatiDichiarazioneInseriti] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT AD.ID_ALLEGATO_DICHIARAZIONE, " +
        "       AL.ID_ALLEGATO, " +
        "       AL.EXT_ID_DOCUMENTO_INDEX, " +
        "       AL.ID_TIPO_FIRMA, " +
        "       AD.ID_DICHIARAZIONE_CONSISTENZA, " +
        "       AD.DATA_INIZIO_VALIDITA, " +
        "       TA.DESCRIZIONE_TIPO_ALLEGATO, " +
        "       TA.ID_TIPO_ALLEGATO, " +
        "       TA.FLAG_INSERIBILE, " +
        "       TA.FLAG_DA_FIRMARE, " +
        "       TF.STILE_FIRMA, " +
        "       TF.DESCRIZIONE_TIPO_FIRMA " +
        "FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_ALLEGATO AL, " +
        "       DB_ALLEGATO_DICHIARAZIONE AD, " +
        "       DB_TIPO_ALLEGATO TA, " +
        "       DB_TIPO_FIRMA TF " +
        "WHERE  DC.ID_AZIENDA = ? " +
        "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = AD.ID_DICHIARAZIONE_CONSISTENZA  " +
        "AND    AD.DATA_FINE_VALIDITA IS NULL " +
        "AND    AD.ID_ALLEGATO = AL.ID_ALLEGATO " +
        "AND    AL.ID_TIPO_ALLEGATO = TA.ID_TIPO_ALLEGATO " +
        "AND    TA.FLAG_INSERIBILE <> 'N' " +
        "AND    AL.ID_TIPO_FIRMA = TF.ID_TIPO_FIRMA (+) " +
        "ORDER BY TA.DESCRIZIONE_TIPO_ALLEGATO ");
      
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::getAllegatiDichiarazioneInseriti] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(hAllegValid == null)
        {
          hAllegValid = new HashMap<Long, Vector<AllegatoDichiarazioneVO>>();
        }
        
        Long idDichiarazioneConsistenza = new Long(rs.getLong("ID_DICHIARAZIONE_CONSISTENZA"));
        
        if(hAllegValid.get(idDichiarazioneConsistenza) != null)
        {
          vAllegatoDichiarazioneVO = hAllegValid.get(idDichiarazioneConsistenza);
        }
        else
        {
          vAllegatoDichiarazioneVO = new Vector<AllegatoDichiarazioneVO>();
        }
        
        
        allegatoDichiarazioneVO = new AllegatoDichiarazioneVO();
        allegatoDichiarazioneVO.setIdAllegatoDichiarazione(new Long(rs.getLong("ID_ALLEGATO_DICHIARAZIONE")));
        allegatoDichiarazioneVO.setIdAllegato(new Long(rs.getLong("ID_ALLEGATO")));
        allegatoDichiarazioneVO.setExtIdDocumentoIndex(checkLongNull(rs.getString("EXT_ID_DOCUMENTO_INDEX")));
        allegatoDichiarazioneVO.setIdTipoFirma(checkLongNull(rs.getString("ID_TIPO_FIRMA")));
        allegatoDichiarazioneVO.setIdDichiarazioneConsistenza(new Long(rs.getLong("ID_DICHIARAZIONE_CONSISTENZA")));
        allegatoDichiarazioneVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        allegatoDichiarazioneVO.setDescTipoAllegato(rs.getString("DESCRIZIONE_TIPO_ALLEGATO"));
        allegatoDichiarazioneVO.setIdTipoAllegato(new Long(rs.getLong("ID_TIPO_ALLEGATO")));
        allegatoDichiarazioneVO.setFlagInseribile(rs.getString("FLAG_INSERIBILE"));
        allegatoDichiarazioneVO.setFlagDaFirmare(rs.getString("FLAG_DA_FIRMARE"));
        allegatoDichiarazioneVO.setStileFirma(rs.getString("STILE_FIRMA"));
        allegatoDichiarazioneVO.setDescrizioneTipoFirma(rs.getString("DESCRIZIONE_TIPO_FIRMA"));
        
        vAllegatoDichiarazioneVO.add(allegatoDichiarazioneVO);
        hAllegValid.put(idDichiarazioneConsistenza, vAllegatoDichiarazioneVO);
      }
      
      return hAllegValid;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("allegatoDichiarazioneVO", allegatoDichiarazioneVO),
        new Variabile("vAllegatoDichiarazioneVO", vAllegatoDichiarazioneVO),
        new Variabile("hAllegValid", hAllegValid)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::getAllegatiDichiarazioneInseriti] ",
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
              "[DocumentoGaaDAO::getAllegatiDichiarazioneInseriti] END.");
    }
  }
  
  
  public Vector<AllegatoDichiarazioneVO> getElencoAllegatiDichiarazioneDefault(
      int idMotivoDichiarazione) 
      throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<AllegatoDichiarazioneVO> vAllegatoDichiarazione = null;
    AllegatoDichiarazioneVO allegatoDichiarazioneVO = null;
    
    
    try
    {
      SolmrLogger.debug(this, "[DocumentoGaaDAO::getElencoAllegatiDichiarazioneDefault] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TR.CODICE_REPORT, " +
          "       TA.ID_TIPO_ALLEGATO, " +
          "       TA.DESCRIZIONE_TIPO_ALLEGATO, " +
          "       TA.DATA_INIZIO_VALIDITA, " +
          "       TA.QUERY_ABILITAZIONE " +
          "FROM   DB_REPORT_MOTIVO_DICHIARAZIONE RMD, " +
          "       DB_TIPO_REPORT TR, " +
          "       DB_TIPO_ALLEGATO TA " +
          "WHERE  RMD.ID_MOTIVO_DICHIARAZIONE = ? " +
          "AND    RMD.DATA_FINE_VALIDITA IS NULL " +
          "AND    TA.DATA_FINE_VALIDITA IS NULL " +
          "AND    RMD.ID_TIPO_REPORT = TR.ID_TIPO_REPORT " +
          "AND    TR.ID_TIPO_ALLEGATO = TA.ID_TIPO_ALLEGATO " +
          "AND    TA.FLAG_INSERIBILE = 'A' " +
          "ORDER BY TA.DESCRIZIONE_TIPO_ALLEGATO ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
                "[DocumentoGaaDAO::getElencoAllegatiDichiarazioneDefault] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      int idx = 0;
      stmt.setInt(++idx, idMotivoDichiarazione);      
      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vAllegatoDichiarazione == null)
          vAllegatoDichiarazione = new Vector<AllegatoDichiarazioneVO>();
        
        allegatoDichiarazioneVO = new AllegatoDichiarazioneVO();
        allegatoDichiarazioneVO.setIdTipoAllegato(rs.getLong("ID_TIPO_ALLEGATO"));
        allegatoDichiarazioneVO.setDescTipoAllegato(rs.getString("DESCRIZIONE_TIPO_ALLEGATO"));
        allegatoDichiarazioneVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        
        
        Clob clob = rs.getClob("QUERY_ABILITAZIONE");
        if(clob != null)
        {
          String queryAbilitazione = clob.getSubString(1, (int) clob.length());
          allegatoDichiarazioneVO.setQueryAbilitazione(queryAbilitazione);
        }
        
        
        vAllegatoDichiarazione.add(allegatoDichiarazioneVO);
        
      }
      
      return vAllegatoDichiarazione;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idMotivoDichiarazione", idMotivoDichiarazione) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("allegatoDichiarazioneVO", allegatoDichiarazioneVO), new Variabile("vAllegatoDichiarazione", vAllegatoDichiarazione)

      };
      LoggerUtils.logDAOError(this,
          "[DocumentoGaaDAO::getElencoAllegatiDichiarazioneDefault] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[DocumentoGaaDAO::getElencoAllegatiDichiarazioneDefault] END.");
    }
  }
  
  
  
  public Vector<TipoFirmaVO> getElencoTipoFirma()
      throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<TipoFirmaVO> vTipoFirma = null;
    TipoFirmaVO tipoFirmaVO = null;
    
    
    try
    {
      SolmrLogger.debug(this, "[DocumentoGaaDAO::getElencoTipoFirma] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TF.ID_TIPO_FIRMA, " +
          "       TF.CODICE_TIPO_FIRMA, " +
          "       TF.DESCRIZIONE_TIPO_FIRMA, " +
          "       TF.STILE_FIRMA, " +
          "       TF.FLAG_FIRMA_DOQUIAGRI " +
          "FROM   DB_TIPO_FIRMA TF ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
                "[DocumentoGaaDAO::getElencoTipoFirma] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vTipoFirma == null)
          vTipoFirma = new Vector<TipoFirmaVO>();
        
        tipoFirmaVO = new TipoFirmaVO();
        tipoFirmaVO.setIdTipoFirma(rs.getLong("ID_TIPO_FIRMA"));
        tipoFirmaVO.setCodiceTipoFirma(rs.getString("CODICE_TIPO_FIRMA"));
        tipoFirmaVO.setDescrizioneTipoFirma(rs.getString("DESCRIZIONE_TIPO_FIRMA"));
        tipoFirmaVO.setStileFirma(rs.getString("STILE_FIRMA"));
        tipoFirmaVO.setFlagFirmaDoquiAgri(rs.getString("FLAG_FIRMA_DOQUIAGRI"));
              
        vTipoFirma.add(tipoFirmaVO);
        
      }
      
      return vTipoFirma;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      {  };
      Variabile variabili[] = new Variabile[]
      { new Variabile("tipoFirmaVO", tipoFirmaVO), new Variabile("vTipoFirma", vTipoFirma)

      };
      LoggerUtils.logDAOError(this,
          "[DocumentoGaaDAO::getElencoTipoFirma] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[DocumentoGaaDAO::getElencoTipoFirma] END.");
    }
  }
  
  
  
  
  
  
  
}
