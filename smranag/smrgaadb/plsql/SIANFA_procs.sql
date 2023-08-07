--------------------------------------------------------
--  File creato - lunedì-luglio-31-2023   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure DELETECASCADE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "SIANFA"."DELETECASCADE" (table_owner   VARCHAR2,
                        parent_table  VARCHAR2,
                        where_clause  VARCHAR2) IS
    /*   Example call:  execute delete_cascade('MY_SCHEMA', 'MY_MASTER', 'where ID=1'); */

  child_cons        VARCHAR2(30);
  parent_cons       VARCHAR2(30);
  child_table       VARCHAR2(30);
  child_cols        VARCHAR(500);
  parent_cols       VARCHAR(500);
  delete_command    VARCHAR(10000);
  new_where_clause  VARCHAR2(10000);

    /* gets the foreign key constraints on other tables which depend on columns in parent_table */
  CURSOR cons_cursor IS SELECT owner, constraint_name, r_constraint_name, table_name, delete_rule
                        FROM   user_constraints
                        WHERE  constraint_type   = 'R'
                        AND    delete_rule       = 'NO ACTION'
                        AND    r_constraint_name IN (SELECT constraint_name
                                                     FROM   user_constraints
                                                     WHERE  constraint_type IN ('P', 'U')
                                                     AND    table_name      = parent_table
                                                     AND    owner           = table_owner)
                        AND    NOT table_name    = parent_table; -- ignore self-referencing constraints


    /* for the current constraint, gets the child columns and corresponding parent columns */
  CURSOR columns_cursor IS SELECT cc1.column_name AS child_col, cc2.column_name AS parent_col
                           FROM   user_cons_columns cc1, user_cons_columns cc2
                           WHERE  cc1.constraint_name = child_cons
                           AND    cc1.table_name      = child_table
                           AND    cc2.constraint_name = parent_cons
                           AND    cc1.position        = cc2.position
                           ORDER BY cc1.position;
BEGIN
    /* loops through all the constraints which refer back to parent_table */
  FOR cons IN cons_cursor LOOP
    child_cons  := cons.constraint_name;
    parent_cons := cons.r_constraint_name;
    child_table := cons.table_name;
    child_cols  := '';
    parent_cols := '';

    /* loops through the child/parent column pairs, building the column lists of the DELETE statement */
    FOR cols IN columns_cursor LOOP
      IF child_cols IS NULL THEN
        child_cols := cols.child_col;
      ELSE
        child_cols := child_cols || ', ' || cols.child_col;
      END IF;

      IF parent_cols IS NULL THEN
        parent_cols := cols.parent_col;
      ELSE
        parent_cols := parent_cols || ', ' || cols.parent_col;
      END IF;
    END LOOP;

  /* construct the WHERE clause of the delete statement, including a subquery to get the related parent rows */
    new_where_clause := 'where (' || child_cols || ') in (select ' || parent_cols || ' from ' || table_owner || '.' || parent_table ||
                        ' ' || where_clause || ')';

    DeleteCascade(cons.owner, child_table, new_where_clause);
    --delete_command := 'delete from ' || cons.owner || '.' || child_table || ' ' || new_where_clause;
    --EXECUTE IMMEDIATE delete_command;
    --DBMS_OUTPUT.put_line(delete_command);
  END LOOP;

  /* construct the delete statement for the current table */
  if parent_table not in ('SIAN_D_FASE_OPERAZIONE','SIAN_SERVIZI','TMP_SIAN','WRK_GENERE_MACCHINA') then
    delete_command := 'delete from ' || table_owner || '.' || parent_table || ' ' || where_clause;
  end if;

  -- this just prints the delete command
  --DBMS_OUTPUT.put_line('2 = '||delete_command);
  --DBMS_OUTPUT.put_line(delete_command||';');
  -- uncomment if you want to actually execute it:
  EXECUTE IMMEDIATE delete_command;
  COMMIT;
  -- remember to issue a COMMIT (not included here, for safety)
END DeleteCascade;

/
--------------------------------------------------------
--  DDL for Package PCK_SIANFA_POPOLA_FASC_AGENT
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "SIANFA"."PCK_SIANFA_POPOLA_FASC_AGENT" AS

  /*====================================================
  ====================== COSTANTI ======================
  ====================================================*/
  kIDUtente                CONSTANT NUMBER(10)                            := 9999999999;
  kIDOPR                   CONSTANT NUMBER(4)                             := 2; -- basilicata
  --kDataFineMax             CONSTANT CHAR(8)    := '99991231';
  kDataFineMax             CONSTANT CHAR(9)                               := '31-DIC-99';
  kIDFormaGiurDittaIndiv   CONSTANT NUMBER(10)                            := 1;
  kIDRegione               CONSTANT SMRGAA.PROVINCIA.ID_REGIONE%TYPE      := '17';
  kIstatPotenza            CONSTANT SMRGAA.PROVINCIA.ISTAT_PROVINCIA%TYPE := '076';
  KIDCategoriaDocIdentita  CONSTANT NUMBER(10)                            := 12;
  KIDCategoriaDocTP        CONSTANT NUMBER(10)                            := 37;
  kFormatoDataStd          CONSTANT CHAR(10)                              := 'DD/MM/YYYY';
  kIDTipoAltriImpianti     CONSTANT NUMBER(10)                            := 7; -- db_tipo_impianto
  kAnnoVersMatrix          CONSTANT SMRGAA.DB_TIPO_VERSIONE_MATRICE.ANNO_VERSIONE%TYPE := 2016;

  /*====================================================
  ===================== PROCEDURE ======================
  ====================================================*/
  PROCEDURE Main(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                 pCuaaInput       SIAN_CHIAMATA_SERVIZIO.CUAA_INPUT%TYPE,
                 pCodErrore       OUT NUMBER,
                 pDescErrore      OUT VARCHAR2);

  FUNCTION ReturnComune(pCodFisc  SMRGAA.COMUNE.CODFISC%TYPE) RETURN SMRGAA.COMUNE.ISTAT_COMUNE%TYPE;

  PROCEDURE popolaDatiSoggetti(pIdChiamata SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                               pIdElab          NUMBER,
                               pCodErrore   OUT NUMBER,
                               pDescErrore  OUT VARCHAR2);

  PROCEDURE popolaDatiAzienda(pIdChiamata SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                              pIdElab           NUMBER,
                              pAggAz            NUMBER,
                              pCodErrore    OUT NUMBER,
                              pDescErrore   OUT VARCHAR2);

  PROCEDURE popolaNuovoFascicolo(pIdChiamata SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                                 pIdElab          NUMBER,
                                 pIdAzienda   OUT NUMBER,
                                 pCodErrore   OUT NUMBER,
                                 pDescErrore  OUT VARCHAR2);

  FUNCTION ReturnData(pDataStringa VARCHAR2) RETURN DATE;

  FUNCTION ReturnIdAzienda(pIdChiamata SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE) RETURN SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE;

  /*PROCEDURE insertLog(pIdChiamata NUMBER,
                             pStato NUMBER,
                             pEsito VARCHAR2,
                             pMessaggio VARCHAR2,
                             pInizioElab DATE,
                             pFineElab DATE,
                             pCodErrore  VARCHAR2,
                             pDescErrore VARCHAR2,
				pCuaa    VARCHAR2);*/

  PROCEDURE startLog(pIdChiamata      NUMBER, 
                     pMessaggio       VARCHAR2, 
                     pIdElab      OUT NUMBER);
  PROCEDURE endLog(pIdChiamata  NUMBER,
                   pIdElab      NUMBER,
                   pStato       NUMBER, 
                   pEsito       VARCHAR2, 
                   pMessaggio   VARCHAR2);
  PROCEDURE insertLog(pIdChiamata NUMBER, 
                      pIdElab     NUMBER, 
                      pEsito      VARCHAR2, 
                      pMessaggio  VARCHAR2);
  PROCEDURE lockAgent(pStato VARCHAR2);
  PROCEDURE loopElaboraScheda(pScheda NUMBER);
  PROCEDURE wrapperCuaaNoSistema;
  
  PROCEDURE elaboraScheda(pPrgScheda      SINC_FA_AABRFASC_TAB.PRG_SCHEDA@DB_SIAN%TYPE,
                          pCodErrore  OUT NUMBER,
                          pDescErrore OUT VARCHAR2);
  
  --pf 30/07/22: procedura per settare la sede legale alle aziende frutto di import SIAN che ne siano sprovviste
  PROCEDURE fixNoSedeLegale;
                            
END PCK_SIANFA_POPOLA_FASC_AGENT;

/
--------------------------------------------------------
--  DDL for Package PCK_SIANFA_POPOLA_FASCICOLO
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "SIANFA"."PCK_SIANFA_POPOLA_FASCICOLO" AS
  
  kIDUtente                CONSTANT NUMBER(10) := 9999999999;
  kIDOPR                   CONSTANT NUMBER(4)  := 2; -- basilicata
  kDataFineMax             CONSTANT CHAR(8)    := '99991231';
  kIDFormaGiurDittaIndiv   CONSTANT NUMBER(10) := 1;
  kIDRegione               CONSTANT SMRGAA.PROVINCIA.ID_REGIONE%TYPE := '17';
  kIstatPotenza            CONSTANT SMRGAA.PROVINCIA.ISTAT_PROVINCIA%TYPE := '076';
  KIDCategoriaDocIdentita  CONSTANT NUMBER(10) := 12;
  KIDCategoriaDocTP        CONSTANT NUMBER(10) := 37;
  kAnnoVersMatrix          CONSTANT SMRGAA.DB_TIPO_VERSIONE_MATRICE.ANNO_VERSIONE%TYPE := 2016;
  kFormatoDataStd          CONSTANT CHAR(10) := 'DD/MM/YYYY';
  kIDTipoAltriImpianti     CONSTANT NUMBER(10) := 7; -- db_tipo_impianto

  TYPE typeCatalogoAll IS RECORD(IDCatalogMatrix  SMRGAA.DB_R_CATALOGO_MATRICE.ID_CATALOGO_MATRICE%TYPE,
                                 IDUtilizzo       SMRGAA.DB_R_CATALOGO_MATRICE.ID_UTILIZZO%TYPE,
                                 IDDettUso        SMRGAA.DB_R_CATALOGO_MATRICE.ID_TIPO_DETTAGLIO_USO%TYPE,
                                 IDVarieta        SMRGAA.DB_R_CATALOGO_MATRICE.ID_VARIETA%TYPE,
                                 IDTipoEfa        SMRGAA.DB_UTILIZZO_PARTICELLA.ID_TIPO_EFA%TYPE,
                                 IDPeriodoSemina  SMRGAA.DB_UTILIZZO_PARTICELLA.ID_TIPO_PERIODO_SEMINA%TYPE,
                                 dataIniSemina    SMRGAA.DB_UTILIZZO_PARTICELLA.DATA_INIZIO_DESTINAZIONE%TYPE,
                                 dataFineSemina   SMRGAA.DB_UTILIZZO_PARTICELLA.DATA_FINE_DESTINAZIONE%TYPE,
                                 IDPraticaMant    SMRGAA.DB_UTILIZZO_PARTICELLA.ID_PRATICA_MANTENIMENTO%TYPE,
                                 flagPratoPerm    SMRGAA.DB_R_CATALOGO_MATRICE.FLAG_PRATO_PERMANENTE%TYPE);

  PROCEDURE Main(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                 pCuaaInput       SIAN_CHIAMATA_SERVIZIO.CUAA_INPUT%TYPE,
                 pCodErrore   OUT NUMBER,
                 pDescErrore  OUT VARCHAR2);

  FUNCTION ReturnComune(pCodFisc  SMRGAA.COMUNE.CODFISC%TYPE) RETURN SMRGAA.COMUNE.ISTAT_COMUNE%TYPE;

  FUNCTION ReturnData(pDataStringa  VARCHAR2) RETURN DATE;

  FUNCTION ReturnIdAzienda(pIdChiamata  SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE) RETURN SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE;

END PCK_SIANFA_POPOLA_FASCICOLO;

/
--------------------------------------------------------
--  DDL for Package Body PCK_SIANFA_POPOLA_FASC_AGENT
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "SIANFA"."PCK_SIANFA_POPOLA_FASC_AGENT" AS

  FUNCTION ReturnComune(pCodFisc  SMRGAA.COMUNE.CODFISC%TYPE) RETURN SMRGAA.COMUNE.ISTAT_COMUNE%TYPE IS
    vIstatComune  SMRGAA.COMUNE.ISTAT_COMUNE%TYPE;
  BEGIN
  
    SELECT ISTAT_COMUNE
    INTO   vIstatComune
    FROM   SMRGAA.COMUNE
    WHERE  CODFISC      = pCodFisc
    AND    FLAG_ESTINTO = 'N';
    
    RETURN vIstatComune;
    
  EXCEPTION
    WHEN OTHERS THEN
      RETURN NULL;  
  END ReturnComune;
  
  FUNCTION ReturnData(pDataStringa  VARCHAR2) RETURN DATE IS
  BEGIN
    RETURN TO_DATE(pDataStringa,'YYYYMMDD');
  END ReturnData;
  
  PROCEDURE popolaDatiSoggetti(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                               pIdElab NUMBER,
                               pCodErrore   OUT NUMBER,
                               pDescErrore  OUT VARCHAR2) IS
  
    nCont             SIMPLE_INTEGER := 0;
    nIdSoggetto       SMRGAA.DB_SOGGETTO.ID_SOGGETTO%TYPE;
    recIndirResid     SINC_FA_AABRRECA_TAB@db_sian%ROWTYPE;
    recResidComuProv  SINC_FA_AABRSOGG_TAB@db_sian%ROWTYPE;
    recIndirDom       SINC_FA_AABRRECA_TAB@db_sian%ROWTYPE;
    --nPrgSch	        SINC_FA_AABRSCHE_TAB.PRG_SCHEDA@db_sian%TYPE;
    vCuaa             SINC_FA_AABRSCHE_TAB.CUAA@db_sian%TYPE;
    rec 		          SINC_FA_AABRSOGG_TAB@db_sian%ROWTYPE;
    -- vTel		        SINC_FA_AABRTELE_TAB.DESC_NUME_TELE@db_sian%TYPE;
    --vFax		        SINC_FA_AABRTELE_TAB.DESC_NUME_TELE@db_sian%TYPE;
    --vMail		        SINC_FA_AABRMAIL_TAB.DESC_MAIL_ACNT@db_sian%TYPE;
    vTel		          SMRGAA.DB_PERSONA_FISICA.RES_TELEFONO%TYPE;
    vFax		          SMRGAA.DB_PERSONA_FISICA.RES_FAX%TYPE;
    vMail		          SMRGAA.DB_PERSONA_FISICA.RES_MAIL%TYPE;
    
  
  BEGIN
    pCodErrore  := 0;
    pDescErrore := '';
  
    insertLog(pIdChiamata, pIdElab, null, 'popolaDatiSoggetti - Inizio elaborazione');
  
      SELECT CUAA
      INTO vCuaa
      FROM SINC_FA_AABRSCHE_TAB@DB_SIAN
      WHERE PRG_SCHEDA = pIdChiamata;
  
      ----insertLog(pIdChiamata, NULL, NULL, 'popolaDatiSoggetti - Inizio elaborazione'||CHR(13), SYSDATE, NULL, NULL , NULL, vCuaa);
  
    BEGIN
  
      SELECT a.desc_nume_tele
      INTO vTel
      FROM   SINC_FA_AABRTELE_TAB@db_sian a
      WHERE  a.PRG_SCHEDA           	 = pIdChiamata
      AND    a.DATA_FINE_TELE             >= SYSDATE
      AND    a.DECO_TIPO_RECA              =  61
      AND DATA_INIZ_TELE = (SELECT MAX(DATA_INIZ_TELE) 
                            FROM SINC_FA_AABRTELE_TAB@db_sian 
                            WHERE  PRG_SCHEDA           	 = pIdChiamata
                            AND    DATA_FINE_TELE             >= SYSDATE);
  
  
  
     EXCEPTION
      WHEN NO_DATA_FOUND THEN
        vTel := NULL;
        insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiSoggetti -  NO_DATA_FOUND: vTel = NULL');
        ----insertLog(pIdChiamata, NULL, NULL, 'popolaDatiSoggetti - NO_DATA_FOUND: vTel = NULL'||CHR(13), NULL, NULL, NULL , NULL, vCuaa);
    END;
  
    BEGIN
  
      SELECT a.desc_nume_tele
      INTO vFax
      FROM   SINC_FA_AABRTELE_TAB@db_sian a
      WHERE  a.PRG_SCHEDA           	 = pIdChiamata
      AND    a.DATA_FINE_TELE             >= SYSDATE
      AND    a.DECO_TIPO_RECA              =  64
      AND DATA_INIZ_TELE = (SELECT MAX(DATA_INIZ_TELE) 
                            FROM SINC_FA_AABRTELE_TAB@db_sian 
                            WHERE  PRG_SCHEDA           	 = pIdChiamata
                            AND    DATA_FINE_TELE             >= SYSDATE);
  
      EXCEPTION
      WHEN NO_DATA_FOUND THEN
        vFax := NULL;
        insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiSoggetti - NO_DATA_FOUND: vFax = NULL');
  
    END;
  
    BEGIN
  
  --10/03/2021
  --aggiunta condizione sulla data inizio mail perché in alcuni casi è presente più di un record con data fine > di oggi
    
      SELECT SUBSTR(DESC_MAIL_ACNT, 1, 50)
      INTO vMail
      FROM SINC_FA_AABRMAIL_TAB@db_sian
      WHERE PRG_SCHEDA           	 = pIdChiamata
      AND DATA_FINE_MAIL >= SYSDATE
      AND DATA_INIZ_MAIL = (SELECT MAX(DATA_INIZ_MAIL) 
                            FROM SINC_FA_AABRMAIL_TAB@db_sian 
                            WHERE PRG_SCHEDA = pIdChiamata 
                            AND DATA_FINE_MAIL >= SYSDATE)
      AND ROWNUM = 1;
  
  
     EXCEPTION
      WHEN NO_DATA_FOUND THEN
        vMail := NULL;
        insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiSoggetti - NO_DATA_FOUND: vMail = NULL');
  
    END;
  
    BEGIN
      SELECT *
      INTO   recIndirResid
      FROM  SINC_FA_AABRRECA_TAB@db_sian 
      WHERE PRG_SCHEDA           	 = pIdChiamata
      AND DECO_TIPO_RECA = 208;
      
      
  
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        recIndirResid := NULL;
        insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiSoggetti - NO_DATA_FOUND: recIndirResid = NULL');
  
    END;
  
    BEGIN
      SELECT *
      INTO   recResidComuProv
      FROM  SINC_FA_AABRSOGG_TAB@db_sian 
      WHERE PRG_SCHEDA           	 = pIdChiamata
      AND FLAG_PERS_FISI = 1;
  
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        recResidComuProv := NULL;
        insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiSoggetti - NO_DATA_FOUND: recResidComuProv = NULL');
  
    END;
  
    BEGIN
      SELECT *
      INTO   recIndirDom
      FROM  SINC_FA_AABRRECA_TAB@db_sian
      WHERE PRG_SCHEDA           	 = pIdChiamata
      AND DECO_TIPO_RECA = 56
      and rownum = 1;
  
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        recIndirDom := NULL;
        insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiSoggetti - NO_DATA_FOUND: recIndirDom = NULL');
  
    END;
  
  --insertLog(pIdChiamata, pIdElab, null, 'popolaDatiSoggetti - prima di rec');
    FOR rec IN (SELECT *
                FROM   SINC_FA_AABRSOGG_TAB@db_sian S
                WHERE  S.PRG_SCHEDA  = pIdChiamata
                AND    S.FLAG_PERS_FISI = 1) LOOP
  
      SELECT COUNT(*)
      INTO   nCont
      FROM   SMRGAA.DB_PERSONA_FISICA PF
      WHERE  PF.CODICE_FISCALE = rec.CODI_FISC;
      --insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiSoggetti - nCont: '||nCont);
  
      IF nCont != 0 THEN  
        UPDATE SMRGAA.DB_PERSONA_FISICA 
        SET    COGNOME                 = rec.DESC_COGN, 
               NASCITA_COMUNE          = ReturnComune(rec.CODI_BELF_NASC), 
               NOME                    = rec.DESC_NOME, 
               SESSO                   = rec.CODI_SESS, 
               --RES_COMUNE              = recResidComuProv.CODI_ISTA_PROV||recResidComuProv.CODI_ISTA_COMU, 
              -- RES_COMUNE              = ReturnComune(recResidComuProv.CODI_BELF_NASC),
               RES_COMUNE              = ReturnComune(recIndirResid.CODI_GEOG_BELF),
               NASCITA_DATA            = rec.DATA_NASC, 
               RES_INDIRIZZO           = recIndirResid.DESC_GEOG_STRD, 
               RES_CAP                 = recIndirResid.CODI_GEOG_CAPP,
               RES_TELEFONO            = SUBSTR(vTel,1,16), 
               RES_FAX                 = vFax, 
               RES_MAIL                = vMail, 
               DATA_AGGIORNAMENTO      = SYSDATE, 
               ID_UTENTE_AGGIORNAMENTO = kIDUtente, 
               DOM_INDIRIZZO           = recIndirDom.DESC_GEOG_STRD, 
               DOM_CAP                 = recIndirDom.CODI_GEOG_CAPP, 
               DOM_COMUNE              = ReturnComune(recIndirDom.CODI_GEOG_BELF), 
               DATA_INIZIO_RESIDENZA   = recIndirResid.DATA_INIZ_RECA
        WHERE  CODICE_FISCALE          = rec.CODI_FISC;
  
        insertLog(pIdChiamata, pIdElab, 'POSITIVO', 'popolaDatiSoggetti - UPDATE DB_PERSONA_FISICA');
        --insertLog(pIdChiamata, NULL, NULL, 'popolaDatiSoggetti - UPDATE DB_PERSONA_FISICA'||CHR(13), NULL, SYSDATE, NULL , NULL, vCuaa);
  
      ELSE
      --insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiSoggetti -  Prima di inserire il soggetto');
        INSERT INTO SMRGAA.DB_SOGGETTO
        (ID_SOGGETTO, FLAG_FISICO)
        VALUES
        (SMRGAA.SEQ_SOGGETTO.NEXTVAL,'S')
        RETURNING ID_SOGGETTO INTO nIdSoggetto;
  --insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiSoggetti -  Dopo inserire il soggetto: '||nIdSoggetto);
        INSERT INTO SMRGAA.DB_PERSONA_FISICA
        (ID_PERSONA_FISICA, ID_SOGGETTO, CODICE_FISCALE, COGNOME, NASCITA_COMUNE, NOME, SESSO, 
         RES_COMUNE, NASCITA_DATA, RES_INDIRIZZO,RES_CAP, 
         RES_TELEFONO, RES_FAX, RES_MAIL, DATA_AGGIORNAMENTO, NOTE, ID_UTENTE_AGGIORNAMENTO, DOM_INDIRIZZO, 
         DOM_CAP,NASCITA_CITTA_ESTERO, RES_CITTA_ESTERO, MODIFICA_INTERMEDIARIO, ID_TITOLO_STUDIO, ID_INDIRIZZO_STUDIO, DOM_COMUNE, 
         DOM_CITTA_ESTERO, DATA_INIZIO_RESIDENZA, 
         FLAG_CF_OK, NUMERO_CELLULARE, PREFISSO_INTER_CELLULARE, ID_PREFISSO_CELLULARE, DATA_DECESSO)
        VALUES
        (SMRGAA.SEQ_PERSONA_FISICA.NEXTVAL,nIdSoggetto,rec.CODI_FISC,rec.DESC_COGN,ReturnComune(rec.CODI_BELF_NASC),rec.DESC_NOME,rec.CODI_SESS,
         ReturnComune(recIndirResid.CODI_GEOG_BELF),rec.DATA_NASC,recIndirResid.DESC_GEOG_STRD,recIndirResid.CODI_GEOG_CAPP,
         SUBSTR(vTel,1,16),vFax,vMail,SYSDATE,NULL,kIDUtente,recIndirDom.DESC_GEOG_STRD,
         recIndirDom.CODI_GEOG_CAPP,NULL,NULL,NULL,NULL,NULL,ReturnComune(recIndirDom.CODI_GEOG_BELF),NULL,recIndirResid.DATA_INIZ_RECA,
         NULL,NULL,NULL,NULL,NULL);
  
         insertLog(pIdChiamata, pIdElab, 'POSITIVO', 'popolaDatiSoggetti - INSERT DB_PERSONA_FISICA. nIdSoggetto: '||nIdSoggetto);
  
  
      END IF;
    END LOOP;
  --insertLog(pIdChiamata, 1, 'POSITIVO', 'popolaDatiSoggetti - Fine elaborazione'||CHR(13), NULL, SYSDATE, NULL , NULL, vCuaa);  
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nel popolamento dei soggetti = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
      insertLog(pIdChiamata, pIdElab, 'NEGATIVO', 'popolaDatiSoggetti - Errore generico nel popolamento dei soggetti = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
  END popolaDatiSoggetti;
  
  PROCEDURE popolaDatiAzienda(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                              pIdElab NUMBER,
                              pAggAz NUMBER,
                              pCodErrore   OUT NUMBER,
                              pDescErrore  OUT VARCHAR2) IS
  
    vCodREA            ISWSISCRIZIONECC_REA.NUMEROISCRIZIONE%TYPE;
    vDataInizioREA     ISWSISCRIZIONECC_REA.DATAINIZIO%TYPE;
    vDataFineREA       ISWSISCRIZIONECC_REA.DATACESSAZIONE%TYPE;
    vPVREA             SMRGAA.DB_ANAGRAFICA_AZIENDA.CCIAA_PROVINCIA_REA%TYPE;
    nNumCodREA         SMRGAA.DB_ANAGRAFICA_AZIENDA.CCIAA_NUMERO_REA%TYPE;
    /*vCodRI             ISWSISCRIZIONECC_REG_IMPRESE.NUMEROISCRIZIONE%TYPE;
    vDataInizioRI      ISWSISCRIZIONECC_REG_IMPRESE.DATAINIZIO%TYPE;
    vDataFineRI        ISWSISCRIZIONECC_REG_IMPRESE.DATACESSAZIONE%TYPE;*/
    vCodRI		     SINC_FA_AABRIDES_TAB.DESC_VALO_IDES@db_sian%TYPE;
    vDataFineRI	     SINC_FA_AABRIDES_TAB.DATA_FINE_IDES@db_sian%TYPE;
    vDataInizioRI      SINC_FA_AABRIDES_TAB.DATA_INIZ_IDES@db_sian%TYPE;
    nIdFormaGiuridica  SMRGAA.DB_TIPO_FORMA_GIURIDICA.ID_FORMA_GIURIDICA%TYPE;
    dDataCessazione    SMRGAA.DB_ANAGRAFICA_AZIENDA.DATA_CESSAZIONE%TYPE;
    vDenominazione     SMRGAA.DB_ANAGRAFICA_AZIENDA.DENOMINAZIONE%TYPE;
    vPVCompetenza      SMRGAA.DB_ANAGRAFICA_AZIENDA.PROVINCIA_COMPETENZA%TYPE;
    nCont              SIMPLE_INTEGER := 0;
    recAzienda         SMRGAA.DB_ANAGRAFICA_AZIENDA%ROWTYPE;
    nIdAzienda         SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE;
    dInizioValAz       SMRGAA.DB_ANAGRAFICA_AZIENDA.DATA_INIZIO_VALIDITA%TYPE;
    nZonaAlt           SMRGAA.COMUNE.ZONAALT%TYPE;
    recUte             SMRGAA.DB_UTE%ROWTYPE;
    dDataFine          SMRGAA.DB_CONTITOLARE.DATA_FINE_RUOLO%TYPE;
    nIDRuolo           SMRGAA.DB_CONTITOLARE.ID_RUOLO%TYPE;
    recContitolare     SMRGAA.DB_CONTITOLARE%ROWTYPE;
    nIdUte             SMRGAA.DB_UTE.ID_UTE%TYPE;
    idCont             TBL_ID := TBL_ID();
    nIdContitolare     SMRGAA.DB_CONTITOLARE.ID_CONTITOLARE%TYPE;
    vNomeIstanza       VARCHAR2(30);
    nPrgSch	        SINC_FA_AABRSCHE_TAB.PRG_SCHEDA@db_sian%TYPE;
    vCuaa             SINC_FA_AABRSCHE_TAB.CUAA@db_sian%TYPE;
    nAnno             NUMBER;
    vProv VARCHAR2(3);
    vComune VARCHAR2(3);
    indir VARCHAR2(100);
  
  BEGIN
    pCodErrore  := 0;
    pDescErrore := '';
  
    insertLog(pIdChiamata, pIdElab, null, 'popolaDatiAzienda - Inizio elaborazione');
  
    vNomeIstanza := UPPER(SYS_CONTEXT('USERENV','INSTANCE_NAME'));
  
  
    --Devo ricavare il "progressivo scheda" dal parametro di input pIdChiamataid che corrisponde all'id elaborazione
      SELECT sche.cuaa
      INTO vCuaa
      FROM SINC_FA_AABRSCHE_TAB@db_sian sche
      WHERE SCHE.PRG_SCHEDA = pIdChiamata;
  
  
        FOR rec in (SELECT distinct FA.CUAA, NVL(SO.DESC_COGN, NULL)||SO.DESC_RAGI_SOCI DENOMINAZIONE, FA.DATA_INIZ_VALI DATAAPERTURAFASCICOLO,
      (SELECT DESC_MAIL_ACNT 
             FROM SINC_FA_AABRMAIL_TAB@db_sian
             WHERE FLAG_MAIL_CERT != 1
             AND FA.PRG_SCHEDA = PRG_SCHEDA
             AND DATA_INIZ_MAIL = (SELECT MAX(DATA_INIZ_MAIL)
                                   FROM SINC_FA_AABRMAIL_TAB@db_sian
                                   WHERE FLAG_MAIL_CERT != 1
                                   AND FA.PRG_SCHEDA = PRG_SCHEDA)) EMAIL,
      (SELECT DESC_MAIL_ACNT 
             FROM SINC_FA_AABRMAIL_TAB@db_sian
             WHERE FLAG_MAIL_CERT = 1
             AND FA.PRG_SCHEDA = PRG_SCHEDA
             AND DATA_INIZ_MAIL = (SELECT MAX(DATA_INIZ_MAIL)
                                   FROM SINC_FA_AABRMAIL_TAB@db_sian
                                   WHERE FLAG_MAIL_CERT = 1
                                   AND FA.PRG_SCHEDA = PRG_SCHEDA)
             AND ROWNUM = 1) DESCPEC,
      (SELECT DESC_VALO_IDES
      FROM SINC_FA_AABRIDES_TAB@db_sian
      WHERE PRG_SCHEDA = FA.PRG_SCHEDA
      AND DECO_TIPO_IDES = 31
      AND DATA_INIZ_IDES = (SELECT MAX(DATA_INIZ_IDES)
                            FROM SINC_FA_AABRIDES_TAB@db_sian
                            WHERE PRG_SCHEDA = FA.PRG_SCHEDA
                            AND DECO_TIPO_IDES = 31)) CODIPARTIVAA,
      (SELECT DESC_VALO_IDES
      FROM SINC_FA_AABRIDES_TAB@db_sian
      WHERE PRG_SCHEDA = FA.PRG_SCHEDA
      AND DECO_TIPO_IDES = 35
      AND DATA_INIZ_IDES = (SELECT MAX(DATA_INIZ_IDES)
                            FROM SINC_FA_AABRIDES_TAB@db_sian
                            WHERE PRG_SCHEDA = FA.PRG_SCHEDA
                            AND DECO_TIPO_IDES = 35)) CODICAMECIAAREAA,
      (SELECT DESC_VALO_IDES
      FROM SINC_FA_AABRIDES_TAB@db_sian
      WHERE PRG_SCHEDA = FA.PRG_SCHEDA
      AND DECO_TIPO_IDES = 3459
      AND DATA_INIZ_IDES = (SELECT MAX(DATA_INIZ_IDES)
                            FROM SINC_FA_AABRIDES_TAB@db_sian
                            WHERE PRG_SCHEDA = FA.PRG_SCHEDA
                            AND DECO_TIPO_IDES = 3459)) CODICAMECIAARIII, DECO.DESC_DECO DESCNATUGIUR, /*DES.DESC_DENO_PIVA*/
      (SELECT DESC_DENO_PIVA 
      FROM SINC_FA_AABRIDES_TAB@db_sian 
      WHERE DECO_TIPO_IDES = 31 
      AND FA.PRG_SCHEDA = PRG_SCHEDA
      AND DATA_INIZ_IDES = (SELECT MAX(DATA_INIZ_IDES)
                            FROM SINC_FA_AABRIDES_TAB@db_sian
                            WHERE PRG_SCHEDA = FA.PRG_SCHEDA
                            AND DECO_TIPO_IDES = 31)) DESCINTE, 
      RECA.CODI_GEOG_CAPP CAP,
      (SELECT DESC_VALO_IDES
      FROM SINC_FA_AABRIDES_TAB@db_sian
      WHERE PRG_SCHEDA = FA.PRG_SCHEDA
      AND DECO_TIPO_IDES = 152
      AND DATA_INIZ_IDES = (SELECT MAX(DATA_INIZ_IDES)
                            FROM SINC_FA_AABRIDES_TAB@db_sian
                            WHERE PRG_SCHEDA = FA.PRG_SCHEDA
                            AND DECO_TIPO_IDES = 152)) CODICESTATOESTERO, /*SUBSTR(FA.CODI_ENTE, 9) COMUNE,*/
      --RECA.DESC_GEOG_STRD INDIRIZZO, 
      /*SUBSTR(FA.CODI_ENTE, 6, 3) PROVINCIA,*/
                            RECA.CODI_GEOG_BELF,
      SO.ID_SOGG ID_DSWS,
      CASE WHEN FA.DATA_FINE_VALI != kDataFineMax THEN FA.DATA_FINE_VALI ELSE NULL END DATACHIUSURAFASCICOLO
      FROM SINC_FA_AABRFASC_TAB@db_sian FA, /*SINC_FA_AABRMAIL_TAB@db_sian MA, SINC_FA_AABRMAIL_TAB@db_sian MAILP,*/
      SINC_FA_AABRSOGG_TAB@db_sian SO, /*SINC_FA_AABRIDES_TAB@db_sian DES,*/ AABRDECO_TAB@db_sian DECO, SINC_FA_AABRRECA_TAB@db_sian RECA
      WHERE  FA.PRG_SCHEDA                       = pIdChiamata
      --AND RECA.DECO_TIPO_RECA                    = 208
      --AND RECA.DECO_TIPO_RECA                    = 56
      AND RECA.DECO_TIPO_RECA                    IN (208, 56)
      --AND DES.DECO_TIPO_IDES                     = 31
      AND DECO.ID_DECO                           IN (2511, 2517, 2516, 2499)
      --AND SO.CODI_ISTA_PROV                      IS NOT NULL
      --AND SO.CODI_ISTA_COMU                      IS NOT NULL
      AND SO.FLAG_PERS_FISI                      = 1
      AND FA.PRG_SCHEDA                          = RECA.PRG_SCHEDA
      --AND (FA.PRG_SCHEDA                         = MA.PRG_SCHEDA(+) AND MA.FLAG_MAIL_CERT(+) != 1)
     -- AND (FA.PRG_SCHEDA                         = MAILP.PRG_SCHEDA(+) AND MAILP.FLAG_MAIL_CERT(+) = 1)
      AND FA.PRG_SCHEDA                          = SO.PRG_SCHEDA
      AND SO.DECO_FORM_GIUR                      = DECO.ID_DECO
      --AND FA.PRG_SCHEDA                          = DES.PRG_SCHEDA
      AND FA.CUAA                                IS NOT NULL) LOOP
  
      BEGIN
      
      SELECT DESC_GEOG_STRD
      INTO indir
      FROM (
      SELECT DECO_TIPO_RECA, DESC_GEOG_STRD
      FROM SINC_FA_AABRRECA_TAB@db_sian
      WHERE PRG_SCHEDA = pIdChiamata
      AND DECO_TIPO_RECA                    IN (208, 56)
      ORDER BY 1 DESC)
      WHERE ROWNUM = 1;
  
          --RICAVO PROVINCIA E COMUNE
              SELECT SUBSTR(ISTAT_COMUNE,1, 3), SUBSTR(ISTAT_COMUNE, 4)
              INTO vProv, vComune
              FROM SMRGAA.COMUNE
              WHERE CODFISC = rec.CODI_GEOG_BELF
              AND FLAG_ESTINTO = 'N';
              
      SELECT NUMEROISCRIZIONE,DATA_INIZIO,DATA_CESSAZIONE
        INTO   vCodREA,vDataInizioREA,vDataFineREA
        FROM   (SELECT I.DESC_VALO_IDES NUMEROISCRIZIONE,I.DATA_INIZ_IDES DATA_INIZIO,I.DATA_FINE_IDES DATA_CESSAZIONE                 
                FROM   SINC_FA_AABRFASC_TAB@db_sian RAF, SINC_FA_AABRIDES_TAB@db_sian I
                WHERE  RAF.PRG_SCHEDA               = pIdChiamata
                AND    RAF.PRG_SCHEDA               = I.PRG_SCHEDA
                AND    RAF.CUAA                      = rec.CUAA  
                AND    DECO_TIPO_IDES                = 35
                AND    I.DESC_VALO_IDES            IS NOT NULL
                ORDER BY I.DATA_INIZ_IDES DESC)
        WHERE   ROWNUM = 1;
  
      EXCEPTION
        WHEN NO_DATA_FOUND THEN  
          vCodREA        := rec.CODICAMECIAAREAA;
          vDataInizioREA := NULL;
          vDataFineREA   := NULL;
          --DBMS_OUTPUT.PUT_LINE('vCodREA: '||to_char(vCodREA)); 
          insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiAzienda - NO_DATA_FOUND. vCodREA: '||vCodREA||' vDataInizioREA: NULL vDataFineREA: NULL');
  
      END;
  
      IF vCodREA IS NOT NULL AND LENGTH(TRIM(TRANSLATE(REPLACE(vCodREA,' '),'(.|/-1234567890',' '))) BETWEEN 2 AND 4 THEN
        vPVREA := UPPER(SUBSTR(TRIM(TRANSLATE(REPLACE(vCodREA,' '),'(.|/-1234567890',' ')),1,2));
      END IF;
  
      IF vCodREA IS NOT NULL AND LENGTH(TRIM(TRANSLATE(REPLACE(vCodREA,' '),'(.|/-abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ',' '))) > 0 THEN
        vCodREA := TRIM(TRANSLATE(REPLACE(vCodREA,' '),'(.|/-abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ',' '));     
      END IF;
  
      vCodREA := TRIM(REPLACE(vCodREA,'    '));
  
      BEGIN
        nNumCodREA := TO_NUMBER(vCodREA);
      EXCEPTION
        WHEN OTHERS THEN
          nNumCodREA := NULL;
          --DBMS_OUTPUT.PUT_LINE('nNumCodREA: NULL'); 
          insertLog(pIdChiamata, pIdElab, 'NEGATIVO', 'popolaDatiAzienda - OTHERS nNumCodREA: NULL');
          ----insertLog(1108, 'OTHERS', 'nNumCodREA: NULL', vCuaa);
      END;
  
      IF vDataFineREA = kDataFineMax THEN     
        vDataFineREA := NULL;
      END IF;
  
      BEGIN
      SELECT NUMEROISCRIZIONE,DATA_INIZIO,DATA_CESSAZIONE
        INTO   vCodRI,vDataInizioRI,vDataFineRI
        FROM   (SELECT I.DESC_VALO_IDES NUMEROISCRIZIONE,I.DATA_INIZ_IDES DATA_INIZIO, I.DATA_FINE_IDES DATA_CESSAZIONE              
                FROM   SINC_FA_AABRFASC_TAB@db_sian RAF, SINC_FA_AABRIDES_TAB@db_sian I
                WHERE  RAF.PRG_SCHEDA               = pIdChiamata
                AND    RAF.PRG_SCHEDA               = I.PRG_SCHEDA
                AND    DECO_TIPO_IDES               = 32
                AND    RAF.CUAA                      = rec.CUAA  
                AND    I.DESC_VALO_IDES            IS NOT NULL
                ORDER BY I.DATA_INIZ_IDES DESC)
        WHERE  ROWNUM = 1;
  
      EXCEPTION
        WHEN NO_DATA_FOUND THEN  
          vCodRI        := rec.CODICAMECIAARIII;
          vDataInizioRI := NULL;
          vDataFineRI   := NULL;
          --DBMS_OUTPUT.PUT_LINE('vCodRI: '||to_char(rec.CODICAMECIAARIII)); 
           insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiAzienda - NO_DATA_FOUND, vCodRI: '||rec.CODICAMECIAARIII||' vDataInizioRI: NULL vDataFineRI: NULL');
  
      END;
  
      IF vDataFineRI = kDataFineMax THEN     
        vDataFineRI := NULL;
      END IF;
  
      BEGIN
        SELECT FG.ID_FORMA_GIURIDICA
        INTO   nIdFormaGiuridica        
        FROM   SMRGAA.DB_TIPO_FORMA_GIURIDICA FG
        WHERE  FG.DESCRIZIONE = rec.DESCNATUGIUR;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          nIdFormaGiuridica := NULL; 
          --DBMS_OUTPUT.PUT_LINE('nIdFormaGiuridica: NULL');
          insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiAzienda - NO_DATA_FOUND, nIdFormaGiuridica: NULL');
  
      END;
  
      dDataCessazione := rec.DATACHIUSURAFASCICOLO;
      vDenominazione  := rec.DENOMINAZIONE;
  
      -- se ditta individuale la denominazione e' l'intestazione
      IF nIdFormaGiuridica = kIDFormaGiurDittaIndiv AND TRIM(rec.DESCINTE) IS NOT NULL THEN
        vDenominazione := rec.DESCINTE;                        
      END IF;
  
      -- se Pv di Basilicata tengo provincia della sede legale, se no fisso POTENZA
      vPVCompetenza := vProv;
  
      SELECT COUNT(*) 
      INTO   nCont                            
      FROM   SMRGAA.PROVINCIA P 
      WHERE  P.ISTAT_PROVINCIA = vProv 
      AND    P.ID_REGIONE      = kIDRegione;
  
      IF nCont = 0 THEN
        vPVCompetenza := kIstatPotenza;
      END IF;
  
   SELECT *
        INTO   recAzienda
        FROM   SMRGAA.DB_ANAGRAFICA_AZIENDA AA
        WHERE  AA.CUAA               = rec.CUAA
        AND    AA.DATA_FINE_VALIDITA IS NULL;
  
         nIdAzienda := recAzienda.ID_AZIENDA;
  --INIZIO CONDIZIONE se devo aggiornare/inserire azienda
      IF pAggAz = 0 THEN
  
      BEGIN
  
        insertLog(pIdChiamata, pIdElab, 'NEGATIVO', 'popolaDatiAzienda nIdAzienda: '||to_char(nIdAzienda));
  
        UPDATE SMRGAA.DB_AZIENDA
        SET    FLAG_OBBLIGO_FASCICOLO       = 'S',
               FLAG_ESENTE_DELEGA           = 'N',
               FLAG_COMPETENZA_ESCLUSIVA_PA = 'N',
               DATA_APERTURA_FASCICOLO      = rec.DATAAPERTURAFASCICOLO,
               DATA_CHIUSURA_FASCICOLO      = rec.DATACHIUSURAFASCICOLO,
               DATA_AGGIORNAMENTO_PRATICA   = SYSDATE,
               FASCICOLO_DEMATERIALIZZATO   = 'N'
        WHERE  ID_AZIENDA                   = recAzienda.ID_AZIENDA;
  --insertLog(pIdChiamata, pIdElab, 'NEGATIVO', 'popolaDatiAzienda fatto update DB_AZIENDA');
        -- basta che solo uno dei campi sia cambiato storicizzo ed inserisco
        IF NVL(recAzienda.ID_TIPOLOGIA_AZIENDA,1)             != 1 OR
           NVL(recAzienda.PARTITA_IVA,'0')                    != NVL(rec.CODIPARTIVAA,'0') OR
           NVL(recAzienda.DENOMINAZIONE,'0')                  != NVL(vDenominazione,'0') OR
           NVL(recAzienda.ID_FORMA_GIURIDICA,0)               != NVL(nIdFormaGiuridica,0) OR
           NVL(recAzienda.PROVINCIA_COMPETENZA,'0')           != NVL(vPVCompetenza,'0') OR
           NVL(recAzienda.CCIAA_PROVINCIA_REA,'0')            != NVL(vPVREA,'0') OR
           NVL(recAzienda.CCIAA_NUMERO_REA,0)                 != NVL(SUBSTR(TO_NUMBER(nNumCodREA),1,9),0) OR
           NVL(recAzienda.MAIL,'0')                           != NVL(rec.EMAIL,'0') OR
           NVL(recAzienda.PEC,'0')                            != NVL(rec.DESCPEC,'0') OR
           NVL(recAzienda.SEDELEG_COMUNE,'0')                 != NVL(vProv||vComune,'0') OR
           NVL(recAzienda.SEDELEG_INDIRIZZO,'0')              != NVL(indir,'0') OR
           NVL(recAzienda.SEDELEG_CITTA_ESTERO,'0')           != NVL(rec.CODICESTATOESTERO,'0') OR
           NVL(recAzienda.SEDELEG_CAP,'0')                    != NVL(rec.CAP,'0') OR
           NVL(recAzienda.DATA_CESSAZIONE,SYSDATE)            != NVL(dDataCessazione,SYSDATE) OR
           NVL(recAzienda.CCIAA_NUMERO_REGISTRO_IMPRESE,'0')  != NVL(SUBSTR(vCodRI,1,21),'0') OR
           NVL(recAzienda.CCIAA_ANNO_ISCRIZIONE,0)            != NVL(TO_NUMBER(TO_CHAR(vDataInizioRI, 'YYYY')),0) OR
           NVL(recAzienda.FLAG_IAP,'N')                       != 'N' OR
           NVL(recAzienda.DATA_ISCRIZIONE_REA,SYSDATE)        != NVL(ReturnData(vDataInizioREA),SYSDATE) OR
           NVL(recAzienda.DATA_CESSAZIONE_REA,SYSDATE)        != NVL(ReturnData(vDataFineREA),SYSDATE) OR
           NVL(recAzienda.DATA_ISCRIZIONE_RI,SYSDATE)         != NVL(ReturnData(vDataInizioRI),SYSDATE) OR
           NVL(recAzienda.DATA_CESSAZIONE_RI,SYSDATE)         != NVL(ReturnData(vDataFineRI),SYSDATE) THEN
  
          UPDATE SMRGAA.DB_ANAGRAFICA_AZIENDA
          SET    DATA_FINE_VALIDITA      = SYSDATE,
                 ID_UTENTE_AGGIORNAMENTO = kIDUtente,
                 DATA_AGGIORNAMENTO      = SYSDATE
          WHERE  ID_AZIENDA              = recAzienda.ID_AZIENDA
          AND    DATA_FINE_VALIDITA      IS NULL;
          insertLog(pIdChiamata, pIdElab, 'NEGATIVO', 'popolaDatiAzienda fatto update DB_ANAGRAFICA_AZIENDA');
          --vCciaaAnno := ;
  --insertLog(pIdChiamata, pIdElab, 'NEGATIVO', 'popolaDatiAzienda nNumCodREA: '||to_char(nNumCodREA));
         -- nAnno := TO_NUMBER( EXTRACT (YEAR FROM vDataInizioRI));
          INSERT INTO SMRGAA.DB_ANAGRAFICA_AZIENDA 
          (ID_ANAGRAFICA_AZIENDA, ID_AZIENDA, ID_TIPOLOGIA_AZIENDA,DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, CUAA,PARTITA_IVA, DENOMINAZIONE, 
           ID_FORMA_GIURIDICA,ID_ATTIVITA_ATECO, PROVINCIA_COMPETENZA, CCIAA_PROVINCIA_REA,CCIAA_NUMERO_REA, MAIL, SEDELEG_COMUNE,
           SEDELEG_INDIRIZZO, SITOWEB, SEDELEG_CITTA_ESTERO,SEDELEG_CAP, DATA_CESSAZIONE, CAUSALE_CESSAZIONE,NOTE, DATA_AGGIORNAMENTO, 
           ID_UTENTE_AGGIORNAMENTO,ID_ATTIVITA_OTE, MOTIVO_MODIFICA, CCIAA_NUMERO_REGISTRO_IMPRESE,CCIAA_ANNO_ISCRIZIONE, MODIFICA_INTERMEDIARIO, 
           NUMERO_AGEA,INTESTAZIONE_PARTITA_IVA, ID_CESSAZIONE, ID_DIMENSIONE_AZIENDA,ID_UDE, RLS, ULU,CODICE_AGRITURISMO, TELEFONO, FAX,PEC, 
           ESONERO_PAGAMENTO_GF, DATA_AGGIORNAMENTO_UMA,FLAG_IAP, DATA_ISCRIZIONE_REA, DATA_CESSAZIONE_REA,DATA_ISCRIZIONE_RI, 
           DATA_CESSAZIONE_RI, DATA_INIZIO_ATECO) 
          VALUES 
          (SMRGAA.SEQ_ANAGRAFICA_AZIENDA.NEXTVAL,recAzienda.ID_AZIENDA,1,SYSDATE,NULL,rec.CUAA,rec.CODIPARTIVAA,vDenominazione,
           nIdFormaGiuridica,NULL,vPVCompetenza,vPVREA, SUBSTR(TO_NUMBER(nNumCodREA),1,9),rec.EMAIL, vProv||vComune,
           indir,null,rec.CODICESTATOESTERO,rec.CAP,dDataCessazione,null,null,sysdate,
           kIDUtente,null,null,SUBSTR(vCodRI,1,21),TO_CHAR(vDataInizioRI, 'YYYY'),null,
           null,null,null,null,null,null,null,null,null,null,rec.DESCPEC,
           null,null,'N',vDataInizioREA,vDataFineREA,vDataInizioRI,
           vDataFineRI,null)
          RETURNING DATA_INIZIO_VALIDITA INTO dInizioValAz;
  
  
          UPDATE SMRGAA.DB_AZIENDA
          SET    DATA_INIZIO_VALIDITA = dInizioValAz
          WHERE  ID_AZIENDA           = recAzienda.ID_AZIENDA;
        END IF;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          dInizioValAz := SYSDATE;
          --DBMS_OUTPUT.PUT_LINE('dInizioValAz: '||TO_CHAR(SYSDATE));
          ----insertLog(1108, 'NO_DATA_FOUND', 'dInizioValAz: '||TO_CHAR(SYSDATE), vCuaa);
          insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiAzienda - NO_DATA_FOUND dInizioValAz: '||TO_CHAR(SYSDATE));
  
          INSERT INTO SMRGAA.DB_AZIENDA
          (ID_AZIENDA, DATA_INIZIO_VALIDITA, FLAG_BONIFICA_DATI,VARIAZIONE_UTILIZZI_AMMESSA, FLAG_OBBLIGO_FASCICOLO, ID_AZIENDA_PROVENIENZA, 
           FLAG_AZIENDA_PROVVISORIA, DATA_INSEDIAMENTO, FLAG_ESENTE_DELEGA,FLAG_COMPETENZA_ESCLUSIVA_PA, DATA_CONTROLLO, ID_OPR,
           DATA_APERTURA_FASCICOLO, DATA_CHIUSURA_FASCICOLO, DATA_AGGIORNAMENTO_OPR,ID_UTENTE_AGGIORNAMENTO_OPR, DATA_AGGIORNAMENTO_PRATICA, 
           ID_TIPO_FORMA_ASSOCIATA,FASCICOLO_DEMATERIALIZZATO, DATA_CONTROLLI_ALLEVAMENTI) 
          VALUES 
          (SMRGAA.SEQ_AZIENDA.NEXTVAL,dInizioValAz,NULL,NULL,'S',NULL,
           NULL,NULL,'N','N',NULL,kIDOPR,
           rec.DATAAPERTURAFASCICOLO,rec.DATACHIUSURAFASCICOLO,NULL,NULL,SYSDATE,
           NULL,'N',NULL)
          RETURNING ID_AZIENDA INTO nIdAzienda;
  
          INSERT INTO SMRGAA.DB_ANAGRAFICA_AZIENDA 
          (ID_ANAGRAFICA_AZIENDA, ID_AZIENDA, ID_TIPOLOGIA_AZIENDA,DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, CUAA,PARTITA_IVA, DENOMINAZIONE, 
           ID_FORMA_GIURIDICA,ID_ATTIVITA_ATECO, PROVINCIA_COMPETENZA, CCIAA_PROVINCIA_REA,CCIAA_NUMERO_REA, MAIL, SEDELEG_COMUNE,
           SEDELEG_INDIRIZZO, SITOWEB, SEDELEG_CITTA_ESTERO,SEDELEG_CAP, DATA_CESSAZIONE, CAUSALE_CESSAZIONE,NOTE, DATA_AGGIORNAMENTO, 
           ID_UTENTE_AGGIORNAMENTO,ID_ATTIVITA_OTE, MOTIVO_MODIFICA, CCIAA_NUMERO_REGISTRO_IMPRESE,CCIAA_ANNO_ISCRIZIONE, MODIFICA_INTERMEDIARIO, 
           NUMERO_AGEA,INTESTAZIONE_PARTITA_IVA, ID_CESSAZIONE, ID_DIMENSIONE_AZIENDA,ID_UDE, RLS, ULU,CODICE_AGRITURISMO, TELEFONO, FAX,PEC, 
           ESONERO_PAGAMENTO_GF, DATA_AGGIORNAMENTO_UMA,FLAG_IAP, DATA_ISCRIZIONE_REA, DATA_CESSAZIONE_REA,DATA_ISCRIZIONE_RI, 
           DATA_CESSAZIONE_RI, DATA_INIZIO_ATECO) 
          VALUES 
          (SMRGAA.SEQ_ANAGRAFICA_AZIENDA.NEXTVAL,nIdAzienda,1,dInizioValAz,NULL,rec.CUAA,rec.CODIPARTIVAA,vDenominazione,
           nIdFormaGiuridica,NULL,vPVCompetenza,vPVREA,SUBSTR(TO_NUMBER(nNumCodREA),1,9),rec.EMAIL,vProv||vComune,
           indir,null,rec.CODICESTATOESTERO,rec.CAP,dDataCessazione,null,null,sysdate,
           kIDUtente,null,null,SUBSTR(vCodRI,1,21),TO_CHAR(vDataInizioRI, 'YYYY'),null,
           null,null,null,null,null,null,null,null,null,null,rec.DESCPEC,
           null,null,'N',vDataInizioREA,vDataFineREA,vDataInizioRI,
           vDataFineRI,null);
      END;
      END IF;
  --FINO QUI CONDIZIONARE
  
  
      SELECT ZONAALT
      INTO   nZonaAlt                        
      FROM   SMRGAA.COMUNE C
      WHERE  C.ISTAT_COMUNE = vProv||vComune
      AND FLAG_ESTINTO = 'N';
  
  
      BEGIN
        SELECT *
        INTO   recUte
        FROM   SMRGAA.DB_UTE
        WHERE  ID_AZIENDA         = nIdAzienda
        AND    DATA_FINE_ATTIVITA IS NULL
        AND    COMUNE             = vProv||vComune
        AND    NVL(INDIRIZZO,'0') = NVL(indir,'0');
  
        nIdUte := recUte.ID_UTE;
  
        IF NVL(recUte.CAP,'0')        != NVL(rec.CAP,'0') OR
           recUte.ID_ZONA_ALTIMETRICA != nZonaAlt THEN
  
          UPDATE SMRGAA.DB_UTE
          SET    CAP                     = rec.CAP,
                 ID_ZONA_ALTIMETRICA     = nZonaAlt,
                 ID_UTENTE_AGGIORNAMENTO = kIDUtente,
                 DATA_AGGIORNAMENTO      = SYSDATE
          WHERE  ID_AZIENDA              = nIdAzienda
          AND    DATA_FINE_ATTIVITA      IS NULL
          AND    COMUNE                  = vProv||vComune
          AND    NVL(INDIRIZZO,'0')      = NVL(indir,'0');
        END IF;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          INSERT INTO SMRGAA.DB_UTE 
          (ID_UTE, ID_AZIENDA, DENOMINAZIONE,INDIRIZZO, COMUNE, CAP,ID_ZONA_ALTIMETRICA, ID_ATTIVITA_ATECO, TELEFONO,FAX, NOTE, 
           DATA_INIZIO_ATTIVITA,DATA_FINE_ATTIVITA, CAUSALE_CESSAZIONE, DATA_AGGIORNAMENTO,ID_UTENTE_AGGIORNAMENTO, MOTIVO_MODIFICA, 
           ID_ATTIVITA_OTE,TIPO_SEDE) 
          VALUES 
          (SMRGAA.SEQ_UTE.NEXTVAL,nIdAzienda,null,indir,vProv||vComune,rec.CAP,nZonaAlt, null, null,null, null, 
           SYSDATE,null, null, SYSDATE,kIDUtente, null, null,
           null)
          RETURNING ID_UTE INTO nIdUte;
          --DBMS_OUTPUT.PUT_LINE('INSERT INTO SMRGAA.DB_UTE.ID_UTE: '||to_char(nIdUte));
          ----insertLog(1108, 'NO_DATA_FOUND', 'INSERT INTO SMRGAA.DB_UTE.ID_UTE: '||nIdUte, vCuaa);
          insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiAzienda - NO_DATA_FOUND INSERT INTO SMRGAA.DB_UTE.ID_UTE: '||nIdUte);
      END;
  
      -- chiudo le ute che non arrivano piu' dal SIAN
      UPDATE SMRGAA.DB_UTE
      SET    ID_UTENTE_AGGIORNAMENTO  = kIDUtente,
             DATA_AGGIORNAMENTO       = SYSDATE,
             DATA_FINE_ATTIVITA       = SYSDATE
      WHERE  ID_AZIENDA               = nIdAzienda
      AND    ID_UTE                  != nIdUte;
  
      
      
      FOR recCont IN (SELECT PF.CODICE_FISCALE,PF.ID_SOGGETTO, MAX(DS.DATA_NASC) DATA_INIZIO, MAX(DS.DATA_MORT) DATA_FINE,1 ID_RUOLO
      FROM   SINC_FA_AABRSOGG_TAB@db_sian DS, SMRGAA.DB_PERSONA_FISICA PF
      WHERE  DS.PRG_SCHEDA               = pIdChiamata
      AND    DS.FLAG_PERS_FISI           = 1 -- PERS FISICA
      AND    DS.CODI_FISC                = PF.CODICE_FISCALE     
      AND    DS.ID_SOGG                  = rec.ID_DSWS
      GROUP BY PF.CODICE_FISCALE,PF.ID_SOGGETTO) LOOP 
      
      /*UNION
      SELECT PF.CODICE_FISCALE,PF.ID_SOGGETTO,MAX(RL.DATA_INIZ_INCA) DATA_INIZIO,MAX(RL.DATA_FINE_INCA) DATA_FINE,1 ID_RUOLO
      FROM   SINC_FA_AABRSOGG_TAB@db_sian DS,  SMRGAA.DB_PERSONA_FISICA PF, SINC_FA_AABRRLEG_TAB@db_sian RL
      WHERE  DS.PRG_SCHEDA                = pIdChiamata
      AND    DS.FLAG_PERS_FISI            = 1 -- PERS FISICA
      AND    DS.CODI_FISC                 = PF.CODICE_FISCALE     
      AND    DS.ID_SOGG                   = RL.ID_SOGG_FISI
      AND    RL.DATA_FINE_INCA >= SYSDATE
      AND    DS.ID_SOGG                   = rec.ID_DSWS
      GROUP BY PF.CODICE_FISCALE,PF.ID_SOGGETTO) LOOP*/
  
        dDataFine := NULL;
  
        IF TO_CHAR(TRUNC(recCont.DATA_FINE)) != kDataFineMax THEN     
          dDataFine := recCont.DATA_FINE;
        END IF;  
  
        nIDRuolo := recCont.ID_RUOLO;
  
        IF recCont.ID_RUOLO = 99 AND recCont.CODICE_FISCALE = rec.CUAA THEN
          nIDRuolo := 1;    
        END IF;
  
        BEGIN
          SELECT *
          INTO   recContitolare
          FROM   SMRGAA.DB_CONTITOLARE
          WHERE  ID_RUOLO        = nIDRuolo
          AND    ID_AZIENDA      = nIdAzienda
          AND    ID_SOGGETTO     = recCont.ID_SOGGETTO
          AND    DATA_FINE_RUOLO IS NULL;
  
          idCont.EXTEND;
          idCont(idCont.COUNT) := OBJ_ID(recContitolare.ID_CONTITOLARE);
  
          IF recContitolare.DATA_INIZIO_RUOLO     != NVL(recCont.DATA_INIZIO,SYSDATE) OR
             recContitolare.DATA_INIZIO_RUOLO_MOD != NVL(recCont.DATA_INIZIO,SYSDATE) OR
             recContitolare.DATA_FINE_RUOLO_MOD   != dDataFine                                    THEN
  
            UPDATE SMRGAA.DB_CONTITOLARE
            SET    DATA_FINE_RUOLO = SYSDATE
            WHERE  ID_RUOLO        = nIDRuolo
            AND    ID_AZIENDA      = nIdAzienda
            AND    ID_SOGGETTO     = recCont.ID_SOGGETTO
            AND    DATA_FINE_RUOLO IS NULL;
  
            INSERT INTO SMRGAA.DB_CONTITOLARE 
            (ID_CONTITOLARE, ID_SOGGETTO, ID_RUOLO,ID_AZIENDA, DATA_INIZIO_RUOLO, 
             DATA_FINE_RUOLO,FLAG_ACCESSO_FORZATO, DATA_INIZIO_RUOLO_MOD,DATA_FINE_RUOLO_MOD) 
            VALUES 
            (SMRGAA.SEQ_CONTITOLARE.NEXTVAL,recCont.ID_SOGGETTO,nIDRuolo,nIdAzienda,NVL(recCont.DATA_INIZIO,SYSDATE),
             dDataFine,null,NVL(recCont.DATA_INIZIO,SYSDATE),dDataFine)
            RETURNING ID_CONTITOLARE INTO nIdContitolare;
  
            idCont.EXTEND;
            idCont(idCont.COUNT) := OBJ_ID(nIdContitolare);
          END IF;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
  
            INSERT INTO SMRGAA.DB_CONTITOLARE 
            (ID_CONTITOLARE, ID_SOGGETTO, ID_RUOLO,ID_AZIENDA, DATA_INIZIO_RUOLO, 
             DATA_FINE_RUOLO,FLAG_ACCESSO_FORZATO, DATA_INIZIO_RUOLO_MOD,DATA_FINE_RUOLO_MOD) 
            VALUES 
            (SMRGAA.SEQ_CONTITOLARE.NEXTVAL,recCont.ID_SOGGETTO,nIDRuolo,nIdAzienda,NVL(recCont.DATA_INIZIO,SYSDATE),
             dDataFine,null,NVL(recCont.DATA_INIZIO,SYSDATE),dDataFine)
            RETURNING ID_CONTITOLARE INTO nIdContitolare;
  
  
            insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiAzienda - NO_DATA_FOUND INSERT INTO SMRGAA.DB_CONTITOLARE.ID_CONTITOLARE: '||nIdContitolare);
            idCont.EXTEND;
            idCont(idCont.COUNT) := OBJ_ID(nIdContitolare);
        END;
      END LOOP;
  
      -- chiusura dei contitolari attivi e non salvati in precedenza
      UPDATE SMRGAA.DB_CONTITOLARE
      SET    DATA_FINE_RUOLO = SYSDATE
      WHERE  ID_AZIENDA      = nIdAzienda
      AND    ID_CONTITOLARE  NOT IN (SELECT ID
                                     FROM   TABLE(idCont));
    END LOOP;
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nel popolamento dei dati dell''azienda = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
      --DBMS_OUTPUT.PUT_LINE('Errore generico nel popolamento dei dati dell''azienda = '||to_char(SQLERRM));
  
      insertLog(pIdChiamata, pIdElab, 'NEGATIVO', 'popolaDatiAzienda - OTHERS Errore generico nel popolamento dei dati dell''azienda = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
  END popolaDatiAzienda;
  
  PROCEDURE popolaDatiDocumenti(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                                pIdElab NUMBER,
                                pCodErrore   OUT NUMBER,
                                pDescErrore  OUT VARCHAR2) IS
  
    recDocumento  SMRGAA.DB_DOCUMENTO%ROWTYPE;
    nIdDocumento  SMRGAA.DB_DOCUMENTO.ID_DOCUMENTO%TYPE;
    idDoc         TBL_ID := TBL_ID();
    nIdAzienda    SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE;
    vCuaa         SINC_FA_AABRSCHE_TAB.CUAA@db_sian%TYPE;
  BEGIN
    pCodErrore  := 0;
    pDescErrore := '';
    insertLog(pIdChiamata, pIdElab, null, 'popolaDatiDocumenti - Inizio elaborazione');
  --ricavo il valore del cuaa
    SELECT sche.cuaa
    INTO  vCuaa
    FROM SINC_FA_AABRSCHE_TAB@db_sian sche
    WHERE sche.prg_scheda = pIdChiamata;
  
  
    /*FOR rec IN (SELECT AA.ID_AZIENDA,AA.CUAA,TD.ID_DOCUMENTO ID_TIPO_DOC,RAF.NUMERODOCUMENTO,RAF.DATADOCUMENTO,RAF.DATASCADDOCUMENTO
                FROM   ISWSRESPANAGFASCICOLO15 RAF,SMRGAA.DB_TIPO_DOCUMENTO TD,SMRGAA.DB_DOCUMENTO_CATEGORIA DC,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
                WHERE  RAF.CUAA                  = AA.CUAA
                AND    AA.DATA_FINE_VALIDITA     IS NULL
                AND    RAF.ID_CHIAMATA           = pIdChiamata
                AND    TD.IDENTIFICATIVO_SIAN    = RAF.TIPODOCUMENTO 
                AND    DC.ID_DOCUMENTO           = TD.ID_DOCUMENTO
                AND    DC.ID_CATEGORIA_DOCUMENTO = KIDCategoriaDocIdentita) LOOP */
  
      FOR rec IN (SELECT distinct AA.ID_AZIENDA,AA.CUAA,TD.ID_DOCUMENTO ID_TIPO_DOC,IDES.DESC_VALO_IDES NUMERODOCUMENTO, IDES.DATA_INIZ_IDES DATADOCUMENTO, IDES.DATA_FINE_IDES DATASCADDOCUMENTO
                FROM  SINC_FA_AABRFASC_TAB@db_sian RAF, SINC_FA_AABRIDES_TAB@db_sian IDES, SINC_FA_AABRSOGG_TAB@db_sian SOGG,
                SMRGAA.DB_TIPO_DOCUMENTO TD,SMRGAA.DB_DOCUMENTO_CATEGORIA DC,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
                WHERE RAF.PRG_SCHEDA             = pIdChiamata
                AND   AA.DATA_FINE_VALIDITA      IS NULL
                AND   IDES.DECO_TIPO_IDES        in (33, 35)
                AND   DC.ID_DOCUMENTO            = 472
                AND   DC.ID_CATEGORIA_DOCUMENTO  = KIDCategoriaDocIdentita
                AND   RAF.CUAA                   = AA.CUAA
                AND   RAF.ID_SOGG                = SOGG.ID_SOGG
                AND   SOGG.ID_SOGG               = IDES.ID_SOGG
                AND   DC.ID_DOCUMENTO            = TD.ID_DOCUMENTO) LOOP
  
      nIdAzienda := rec.ID_AZIENDA;
  
      BEGIN
       /* SELECT *
        INTO   recDocumento
        FROM   SMRGAA.DB_DOCUMENTO
        WHERE  ID_AZIENDA         = rec.ID_AZIENDA
        AND    EXT_ID_DOCUMENTO   = rec.ID_TIPO_DOC
        AND    NUMERO_DOCUMENTO   = rec.NUMERODOCUMENTO
        AND    CUAA               = rec.CUAA
        and    id_stato_documento is null;*/
        SELECT *
          INTO   recDocumento
          FROM   SMRGAA.DB_DOCUMENTO doc
          WHERE  doc.id_documento = (SELECT max(dd.id_documento)
                                      FROM   SMRGAA.DB_DOCUMENTO dd
                                      WHERE  dd.ID_AZIENDA         = rec.ID_AZIENDA
                                      AND    dd.EXT_ID_DOCUMENTO   = rec.ID_TIPO_DOC
                                      AND    dd.NUMERO_DOCUMENTO   = rec.NUMERODOCUMENTO
                                      AND    dd.CUAA               = rec.CUAA
                                      and    dd.id_stato_documento is null);
  
        UPDATE SMRGAA.DB_DOCUMENTO
        SET    DATA_INIZIO_VALIDITA        = ReturnData(rec.DATADOCUMENTO),
               DATA_FINE_VALIDITA          = ReturnData(rec.DATASCADDOCUMENTO),
               DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE,
               UTENTE_ULTIMO_AGGIORNAMENTO = kIDUtente
        WHERE  ID_AZIENDA                  = rec.ID_AZIENDA
        AND    EXT_ID_DOCUMENTO            = rec.ID_TIPO_DOC
        AND    NUMERO_DOCUMENTO            = rec.NUMERODOCUMENTO
        AND    CUAA                        = rec.CUAA;
  
        idDoc.EXTEND;
        idDoc(idDoc.COUNT) := OBJ_ID(recDocumento.ID_DOCUMENTO);
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
        IF rec.NUMERODOCUMENTO = 'N.D.' THEN
          insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiDocumenti - NO_DATA_FOUND. nIdDocumento: N.D.');
        ELSE
          INSERT INTO SMRGAA.DB_DOCUMENTO 
          (ID_DOCUMENTO, EXT_ID_DOCUMENTO, ID_STATO_DOCUMENTO,ID_AZIENDA, CUAA, DATA_INIZIO_VALIDITA,
           DATA_FINE_VALIDITA, NUMERO_PROTOCOLLO,DATA_PROTOCOLLO,NUMERO_DOCUMENTO, ENTE_RILASCIO_DOCUMENTO, ID_DOCUMENTO_PRECEDENTE,NOTE, 
           DATA_ULTIMO_AGGIORNAMENTO,UTENTE_ULTIMO_AGGIORNAMENTO,DATA_INSERIMENTO, DATA_VARIAZIONE_STATO, ID_UTENTE_AGGIORNAMENTO_SRV,
           NUMERO_PROTOCOLLO_ESTERNO,CUAA_SOCCIDARIO, ESITO_CONTROLLO,DATA_ESECUZIONE, FLAG_CUAA_SOCCIDARIO_VALIDATO, ID_CONTO_CORRENTE,
           ID_CAUSALE_MODIFICA_DOCUMENTO) 
          VALUES 
          (SMRGAA.SEQ_DOCUMENTO.NEXTVAL,rec.ID_TIPO_DOC,null,rec.ID_AZIENDA,rec.CUAA,rec.DATADOCUMENTO,
           rec.DATASCADDOCUMENTO,null,null,rec.NUMERODOCUMENTO,null,null,null,
           SYSDATE,kIDUtente,SYSDATE,null,null,
           null,null,null,null,null,null,
           null)
          RETURNING ID_DOCUMENTO INTO nIdDocumento;
  
          insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiDocumenti - NO_DATA_FOUND. nIdDocumento: '||nIdDocumento);
        END IF; 
  
          idDoc.EXTEND;
          idDoc(idDoc.COUNT) := OBJ_ID(nIdDocumento);
      END;
    END LOOP;
  
    -- aggiornare data_fine con sysdate-1 di tutti i doc non toccati x quell'azienda x la categoria KIDCategoriaDocIdentita
    UPDATE SMRGAA.DB_DOCUMENTO
    SET    DATA_FINE_VALIDITA          = SYSDATE - 1,
           DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE,
           UTENTE_ULTIMO_AGGIORNAMENTO = kIDUtente
    WHERE  ID_AZIENDA                  = nIdAzienda
    AND    EXT_ID_DOCUMENTO            IN (SELECT DC.ID_DOCUMENTO
                                           FROM   SMRGAA.DB_DOCUMENTO_CATEGORIA DC
                                           WHERE  DC.ID_CATEGORIA_DOCUMENTO = KIDCategoriaDocIdentita)
    AND    ID_DOCUMENTO                NOT IN (SELECT ID
                                               FROM   TABLE(idDoc));
  
    idDoc := TBL_ID();
  
    /*FOR rec IN (SELECT DISTINCT AA.ID_AZIENDA,AA.CUAA,DATADOCUMENTO,NUMERODOCUMENTO,TIPODOCUMENTO,TD.ID_DOCUMENTO ID_TIPO_DOCUMENTO
                FROM   ISWSRESPANAGFASCICOLO15 T,SMRGAA.DB_TIPO_DOCUMENTO TD,SMRGAA.DB_DOCUMENTO_CATEGORIA DC,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
                WHERE  T.NUMERODOCUMENTO         IS NOT NULL
                AND    T.ID_CHIAMATA             = pIdChiamata
                AND    T.TIPODOCUMENTO           = TD.IDENTIFICATIVO_SIAN
                AND    DC.ID_DOCUMENTO           = TD.ID_DOCUMENTO
                AND    DC.ID_CATEGORIA_DOCUMENTO = KIDCategoriaDocTP
                AND    T.CUAA                    = AA.CUAA
                AND    AA.DATA_FINE_VALIDITA     IS NULL
                AND    TD.DATA_FINE_VALIDITA     IS NULL) LOOP*/
  
    FOR rec IN (SELECT DISTINCT AA.ID_AZIENDA,AA.CUAA,IDES.DATA_INIZ_IDES DATADOCUMENTO,
                IDES.DESC_VALO_IDES NUMERODOCUMENTO, '1' TIPODOCUMENTO, TD.ID_DOCUMENTO ID_TIPO_DOCUMENTO
                FROM   SINC_FA_AABRFASC_TAB@db_sian RAF, SINC_FA_AABRIDES_TAB@db_sian IDES,
                SINC_FA_AABRSOGG_TAB@db_sian SOGG, SMRGAA.DB_TIPO_DOCUMENTO TD,
                SMRGAA.DB_DOCUMENTO_CATEGORIA DC, SMRGAA.DB_ANAGRAFICA_AZIENDA AA
                WHERE  RAF.PRG_SCHEDA            = pIdChiamata
                AND    IDES.DECO_TIPO_IDES       = 33
                AND    DC.ID_CATEGORIA_DOCUMENTO = KIDCategoriaDocTP
                AND    DC.ID_DOCUMENTO            = 507
                AND    RAF.CUAA                  = AA.CUAA
                AND    DC.ID_DOCUMENTO           = TD.ID_DOCUMENTO
                AND    AA.DATA_FINE_VALIDITA     IS NULL
                AND    TD.DATA_FINE_VALIDITA     IS NULL
                AND    RAF.ID_SOGG                = SOGG.ID_SOGG
                AND    SOGG.ID_SOGG               = IDES.ID_SOGG) LOOP
      BEGIN
        SELECT *
        INTO   recDocumento
        FROM   SMRGAA.DB_DOCUMENTO
        WHERE  ID_AZIENDA       = rec.ID_AZIENDA
        AND    EXT_ID_DOCUMENTO = rec.ID_TIPO_DOCUMENTO
        AND    NUMERO_DOCUMENTO = rec.NUMERODOCUMENTO
        AND    CUAA             = rec.CUAA;
  
        nIdDocumento := recDocumento.ID_DOCUMENTO;
  
        UPDATE SMRGAA.DB_DOCUMENTO
        SET    DATA_INIZIO_VALIDITA        = ReturnData(rec.DATADOCUMENTO),
               DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE,
               UTENTE_ULTIMO_AGGIORNAMENTO = kIDUtente
        WHERE  ID_AZIENDA                  = rec.ID_AZIENDA
        AND    EXT_ID_DOCUMENTO            = rec.ID_TIPO_DOCUMENTO
        AND    NUMERO_DOCUMENTO            = rec.NUMERODOCUMENTO
        AND    CUAA                        = rec.CUAA;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
  
        IF rec.NUMERODOCUMENTO = 'N.D.' THEN
          insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiDocumenti - NO_DATA_FOUND. nIdDocumento: N.D.');
        ELSE
          INSERT INTO SMRGAA.DB_DOCUMENTO 
          (ID_DOCUMENTO, EXT_ID_DOCUMENTO, ID_STATO_DOCUMENTO,ID_AZIENDA, CUAA, DATA_INIZIO_VALIDITA,
           DATA_FINE_VALIDITA, NUMERO_PROTOCOLLO,DATA_PROTOCOLLO,NUMERO_DOCUMENTO, ENTE_RILASCIO_DOCUMENTO, ID_DOCUMENTO_PRECEDENTE,NOTE, 
           DATA_ULTIMO_AGGIORNAMENTO,UTENTE_ULTIMO_AGGIORNAMENTO,DATA_INSERIMENTO, DATA_VARIAZIONE_STATO, ID_UTENTE_AGGIORNAMENTO_SRV,
           NUMERO_PROTOCOLLO_ESTERNO,CUAA_SOCCIDARIO, ESITO_CONTROLLO,DATA_ESECUZIONE, FLAG_CUAA_SOCCIDARIO_VALIDATO, ID_CONTO_CORRENTE,
           ID_CAUSALE_MODIFICA_DOCUMENTO) 
          VALUES 
          (SMRGAA.SEQ_DOCUMENTO.NEXTVAL,rec.ID_TIPO_DOCUMENTO,null,rec.ID_AZIENDA,rec.CUAA,rec.DATADOCUMENTO,
           null,null,null,rec.NUMERODOCUMENTO,null,null,null,
           SYSDATE,kIDUtente,SYSDATE,null,null,
           null,null,null,null,null,null,
           null)
          RETURNING ID_DOCUMENTO INTO nIdDocumento;
          ----insertLog(1108, 'NO_DATA_FOUND popolaDatiDocumenti', 'nIdDocumento: '||nIdDocumento, vCuaa);
          insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaDatiDocumenti - NO_DATA_FOUND. nIdDocumento: '||nIdDocumento);
          END IF;
  
      END;
  
      idDoc.EXTEND;
      idDoc(idDoc.COUNT) := OBJ_ID(nIdDocumento);
  
      -- prima di inserire cancello
      DELETE SMRGAA.DB_DOCUMENTO_PROPRIETARIO
      WHERE  ID_DOCUMENTO = nIdDocumento;
  
      /*FOR recDocPr IN (SELECT DISTINCT P.PROPRIETARIO,PF.COGNOME,PF.NOME
                       FROM   ISWSPROPRIETARIO P,ISWSTERRITORIOFS6 T,SMRGAA.DB_PERSONA_FISICA PF
                       WHERE  T.ID_CHIAMATA          = pIdChiamata
                       AND    P.ID_ISWSTERRITORIOFS6 = T.ID_ISWSTERRITORIOFS6
                       AND    P.PROPRIETARIO         = PF.CODICE_FISCALE(+)
                       AND    T.CUAA                 = rec.CUAA
                       AND    T.NUMERODOCUMENTO      = rec.NUMERODOCUMENTO) LOOP*/
  
      FOR recDocPr IN (SELECT DISTINCT P.CODI_FISC PROPRIETARIO,PF.COGNOME,PF.NOME
                       FROM   SINC_FA_AABRSOGG_TAB@db_sian P, SINC_FA_AABRIDES_TAB@db_sian IDES , SMRGAA.DB_PERSONA_FISICA PF
                       WHERE  P.PRG_SCHEDA          = pIdChiamata
                       AND    P.CODI_FISC            = rec.CUAA
                       AND    IDES.DESC_VALO_IDES   = rec.NUMERODOCUMENTO
                       AND    P.CODI_FISC            = PF.CODICE_FISCALE(+)
                       AND    P.ID_SOGG               = IDES.ID_SOGG) LOOP
  
        INSERT INTO SMRGAA.DB_DOCUMENTO_PROPRIETARIO 
        (ID_DOCUMENTO_PROPRIETARIO, ID_DOCUMENTO, CUAA,DENOMINAZIONE, DATA_ULTIMO_AGGIORNAMENTO, UTENTE_ULTIMO_AGGIORNAMENTO,
         DATA_INSERIMENTO,FLAG_VALIDATO) 
        VALUES 
        (SMRGAA.SEQ_DOCUMENTO_PROPRIETARIO.NEXTVAL,nIdDocumento,recDocPr.PROPRIETARIO,recDocPr.COGNOME||' '||recDocPr.NOME,SYSDATE,kIDUtente,
         SYSDATE,'N');
      END LOOP;
    END LOOP;
  
    -- aggiornare data_fine con sysdate-1 di tutti i doc non toccati x quell'azienda x la categoria KIDCategoriaDocTP
    UPDATE SMRGAA.DB_DOCUMENTO
    SET    DATA_FINE_VALIDITA          = SYSDATE - 1,
           DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE,
           UTENTE_ULTIMO_AGGIORNAMENTO = kIDUtente
    WHERE  ID_AZIENDA                  = nIdAzienda
    AND    EXT_ID_DOCUMENTO            IN (SELECT DC.ID_DOCUMENTO
                                           FROM   SMRGAA.DB_DOCUMENTO_CATEGORIA DC
                                           WHERE  DC.ID_CATEGORIA_DOCUMENTO = KIDCategoriaDocTP)
    AND    ID_DOCUMENTO                NOT IN (SELECT ID
                                               FROM   TABLE(idDoc));
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nel popolamento dei dati dei documenti = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
      insertLog(pIdChiamata, pIdElab, 'NEGATIVO', 'popolaDatiDocumenti - OTHERS. Errore generico nel popolamento dei dati dei documenti = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
  END popolaDatiDocumenti;
  
  PROCEDURE popolaDatiTerreni(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                              pCodErrore   OUT NUMBER,
                              pDescErrore  OUT VARCHAR2) IS
  
    recStPart         SMRGAA.DB_STORICO_PARTICELLA%ROWTYPE;
    nCont             SIMPLE_INTEGER := 0;
    vFlagBio          SMRGAA.DB_PARTICELLA.BIOLOGICO%TYPE;
    nIdParticella     SMRGAA.DB_PARTICELLA.ID_PARTICELLA%TYPE;
    nZonaAlt          SMRGAA.COMUNE.ZONAALT%TYPE;
    nMediaAltitudine  SMRGAA.DB_STORICO_PARTICELLA.METRI_ALTITUDINE_MEDIA%TYPE;
    nPrgSch	    SINC_FA_AABRSCHE_TAB.PRG_SCHEDA@db_sian%TYPE;
    vCuaa         SINC_FA_AABRSCHE_TAB.CUAA@db_sian%TYPE;
  BEGIN
    pCodErrore  := 0;
    pDescErrore := '';
  
    --Devo ricavare il "progressivo scheda" dal parametro di input pIdChiamataid che corrisponde all'id elaborazione
    SELECT sche.prg_scheda, sche.cuaa
    INTO nPrgSch, vCuaa
    FROM SINC_FA_AABRSCHE_TAB@db_sian sche, SINC_DU_ABRAREDU_TAB@db_sian redu
    WHERE redu.id_elab = pIdChiamata
    AND lpad(to_char(redu.sche_fasc), 11, '0') = sche.id_scheda;
  
    /*FOR rec IN (SELECT T.PROVINCIA,T.COMUNE,T.SEZIONE,T.FOGLIO,T.PARTICELLA,TO_NUMBER(T.FOGLIO) N_FOGLIO,TO_NUMBER(T.PARTICELLA) N_PARTICELLA,
                       TRIM(T.SUBALTERNO) SUBALTERNO,T.SUPERFICIECATASTALE,T.CASIPARTICOLARI,T.SUPERFICIEGRAFICA,PI.ID_POTENZIALITA_IRRIGUA,
                       TZ.ID_TERRAZZAMENTO,RC.ID_ROTAZIONE_COLTURALE
                FROM   ISWSTERRITORIOFS6 T,SMRGAA.DB_TIPO_POTENZIALITA_IRRIGUA PI,SMRGAA.DB_TIPO_TERRAZZAMENTO TZ,
                       SMRGAA.DB_TIPO_ROTAZIONE_COLTURALE RC    
                WHERE  T.ID_CHIAMATA            = pIdChiamata
                AND    PI.CODICE(+)             = NVL(T.FLAGIRRIGUA,0)         
                AND    PI.DATA_FINE_VALIDITA(+) IS NULL
                AND    TZ.CODICE(+)             = NVL(T.FLAGTERRAZZATA,0)
                AND    TZ.DATA_FINE_VALIDITA(+) IS NULL
                AND    RC.CODICE(+)             = NVL(T.ROTAZIONECOLTUREORTIVE,0)
                AND    RC.DATA_FINE_VALIDITA(+) IS NULL
                -- alcune particelle NON sono SOLO numeriche le scarto
                AND    NVL(LENGTH(TRIM(TRANSLATE(REPLACE(PARTICELLA,' '),'1234567890',' '))),0) = 0) LOOP*/
  
    FOR rec IN (SELECT DISTINCT SEZI.CODI_PROV PROVINCIA, SEZI.CODI_COMU COMUNE, PART.SEZI SEZIONE,
                PART.FOGL FOGLIO, PART.PART PARTICELLA, TO_NUMBER(PART.FOGL) N_FOGLIO, 
                TO_NUMBER(PART.PART) N_PARTICELLA, TRIM(PART.SUB) SUBALTERNO,
                PART.SUPE_CATA SUPERFICIECATASTALE, NULL CASIPARTICOLARI, /*T.CASIPARTICOLARI,*/ 
                NULL SUPERFICIEGRAFICA/*PART.SUPERFICIEGRAFICA*/, T.TIPO_IRRIGAZIONE ID_POTENZIALITA_IRRIGUA, T.PRESENZA_TERRAZZAMENTI ID_TERRAZZAMENTO,
                T.ROTAZIONE_COLTURALE ID_ROTAZIONE_COLTURALE
                FROM SINC_FA_AABRAPPE_DETT_TAB@db_sian T, SINC_FA_AABRAPPE_TAB@db_sian APPE,
                SINC_FA_AABRPART_TAB@db_sian PART, AABRSEZI_TAB@db_sian SEZI, SMRGAA.DB_TIPO_POTENZIALITA_IRRIGUA PI,
                SMRGAA.DB_TIPO_TERRAZZAMENTO TZ, SMRGAA.DB_TIPO_ROTAZIONE_COLTURALE RC    
                WHERE  T.PRG_SCHEDA             = nPrgSch
                AND    PART.PRG_SCHEDA          = nPrgSch
                AND    T.CODI_DEST_UTIL         = 660
                AND    PART.CODI_NAZI           = SEZI.CODI_FISC_LUNA
                AND    T.COD_APPE               = APPE.COD_APPE
                AND    PI.CODICE(+)             = NVL(T.TIPO_IRRIGAZIONE,0)         
                AND    PI.DATA_FINE_VALIDITA(+) IS NULL
                AND    TZ.CODICE(+)             = NVL(T.PRESENZA_TERRAZZAMENTI,0)
                AND    TZ.DATA_FINE_VALIDITA(+) IS NULL
                AND    RC.CODICE(+)             = NVL(T.ROTAZIONE_COLTURALE,0)
                AND    RC.DATA_FINE_VALIDITA(+) IS NULL
                -- alcune particelle NON sono SOLO numeriche le scarto
                AND NVL(LENGTH(TRIM(TRANSLATE(REPLACE(PART,' '),'1234567890',' '))),0) = 0) LOOP
  
      SELECT COUNT(*)
      INTO   nCont                        
      FROM   ISWSTERRITORIOFS6 T,WSUTILIZZOTERRACABA UT 
      WHERE  T.ID_CHIAMATA             = pIdChiamata
      AND    UT.ID_ISWSTERRITORIOFS6   = T.ID_ISWSTERRITORIOFS6
      AND    T.PROVINCIA               = rec.PROVINCIA 
      AND    T.COMUNE                  = rec.COMUNE
      AND    NVL(T.SEZIONE, '-')       = NVL(rec.SEZIONE, '-')
      AND    T.FOGLIO                  = rec.FOGLIO 
      AND    T.PARTICELLA              = rec.PARTICELLA
      AND    NVL(T.SUBALTERNO, '-')    = NVL(rec.SUBALTERNO, '-')
      AND    UT.CODICECOLTIVAZIONEBIO  IS NOT NULL; -- e' il giusto test x capire se l'azienda fa biologico o no?
  
      IF nCont > 0 THEN
        vFlagBio := 'S';
      ELSE
        vFlagBio := NULL;
      END IF;
  
      SELECT ZONAALT
      INTO   nZonaAlt                        
      FROM   SMRGAA.COMUNE C
      WHERE  C.ISTAT_COMUNE = rec.PROVINCIA||rec.COMUNE
      AND FLAG_ESTINTO = 'N';
  
      BEGIN
        SELECT AVG(NVL(TO_NUMBER(UT.ALTITUDINE),0)) 
        INTO   nMediaAltitudine                            
        FROM   ISWSTERRITORIOFS6 T,WSUTILIZZOTERRACABA UT
        WHERE  T.ID_CHIAMATA           = pIdChiamata
        AND    UT.ID_ISWSTERRITORIOFS6 = T.ID_ISWSTERRITORIOFS6
        AND    T.PROVINCIA             = rec.PROVINCIA 
        AND    T.COMUNE                = rec.COMUNE
        AND    NVL(T.SEZIONE, '-')     = NVL(rec.SEZIONE, '-')
        AND    T.FOGLIO                = rec.FOGLIO 
        AND    T.PARTICELLA            = rec.PARTICELLA
        AND    NVL(T.SUBALTERNO, '-')  = NVL(rec.SUBALTERNO, '-');  
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          nMediaAltitudine := NULL;
      END;
  
      BEGIN
        SELECT *
        INTO   recStPart
        FROM   SMRGAA.DB_STORICO_PARTICELLA SP
        WHERE  SP.COMUNE              = rec.PROVINCIA||rec.COMUNE
        AND    NVL(SP.SEZIONE,'-')    = NVL(rec.SEZIONE,'-')
        AND    SP.FOGLIO              = rec.N_FOGLIO
        AND    SP.PARTICELLA          = rec.N_PARTICELLA
        AND    NVL(SP.SUBALTERNO,'-') = NVL(rec.SUBALTERNO,'-')
        AND    SP.DATA_FINE_VALIDITA  IS NULL;
  
        IF recStPart.SUP_CATASTALE                  != NVL(TO_NUMBER(rec.SUPERFICIECATASTALE),0) / 10000 OR
           NVL(recStPart.ID_ZONA_ALTIMETRICA,0)     != NVL(nZonaAlt,0)                                   OR
           NVL(recStPart.ID_CASO_PARTICOLARE,99)    != NVL(TO_NUMBER(rec.CASIPARTICOLARI),99)            OR
           recStPart.SUPERFICIE_GRAFICA             != NVL(TO_NUMBER(rec.SUPERFICIEGRAFICA),0) / 10000   OR 
           NVL(recStPart.ID_POTENZIALITA_IRRIGUA,0) != NVL(rec.ID_POTENZIALITA_IRRIGUA,0)                OR
           recStPart.ID_TERRAZZAMENTO               != rec.ID_TERRAZZAMENTO                              OR
           recStPart.ID_ROTAZIONE_COLTURALE         != rec.ID_ROTAZIONE_COLTURALE                        OR
           NVL(recStPart.METRI_ALTITUDINE_MEDIA,0)  != NVL(nMediaAltitudine,0)                           THEN
  
          UPDATE SMRGAA.DB_STORICO_PARTICELLA
          SET    SUP_CATASTALE           = NVL(TO_NUMBER(rec.SUPERFICIECATASTALE),0) / 10000, 
                 ID_ZONA_ALTIMETRICA     = nZonaAlt,
                 ID_CASO_PARTICOLARE     = NVL(TO_NUMBER(rec.CASIPARTICOLARI),99),
                 SUPERFICIE_GRAFICA      = NVL(TO_NUMBER(rec.SUPERFICIEGRAFICA),0) / 10000,
                 ID_POTENZIALITA_IRRIGUA = rec.ID_POTENZIALITA_IRRIGUA,
                 ID_TERRAZZAMENTO        = rec.ID_TERRAZZAMENTO,
                 ID_ROTAZIONE_COLTURALE  = rec.ID_ROTAZIONE_COLTURALE,
                 METRI_ALTITUDINE_MEDIA  = nMediaAltitudine,
                 ID_UTENTE_AGGIORNAMENTO = kIDUtente,
                 DATA_AGGIORNAMENTO      = SYSDATE
          WHERE  COMUNE                  = rec.PROVINCIA||rec.COMUNE
          AND    NVL(SEZIONE,'-')        = NVL(rec.SEZIONE,'-')
          AND    FOGLIO                  = rec.N_FOGLIO
          AND    PARTICELLA              = rec.N_PARTICELLA
          AND    NVL(SUBALTERNO,'-')     = NVL(rec.SUBALTERNO,'-')
          AND    DATA_FINE_VALIDITA      IS NULL;
        END IF;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          INSERT INTO SMRGAA.DB_PARTICELLA 
          (ID_PARTICELLA, DATA_CREAZIONE, DATA_CESSAZIONE,BIOLOGICO, DATA_INIZIO_VALIDITA, FLAG_SCHEDARIO,FLAG_INVIO_SITI) 
          VALUES 
          (SMRGAA.SEQ_PARTICELLA.NEXTVAL,SYSDATE,NULL,vFlagBio,SYSDATE,'N','N')
          RETURNING ID_PARTICELLA INTO nIdParticella;
  
          INSERT INTO SMRGAA.DB_STORICO_PARTICELLA 
          (ID_PARTICELLA, ID_STORICO_PARTICELLA, SEZIONE,COMUNE, DATA_INIZIO_VALIDITA, FOGLIO,
           DATA_FINE_VALIDITA, PARTICELLA, SUP_CATASTALE,SUBALTERNO, ID_AREA_A, ID_ZONA_ALTIMETRICA,FLAG_IRRIGABILE, ID_AREA_B, 
           ID_CASO_PARTICOLARE,ID_AREA_C, ID_AREA_D, DATA_AGGIORNAMENTO,ID_UTENTE_AGGIORNAMENTO, ID_CAUSALE_MOD_PARTICELLA, FLAG_CAPTAZIONE_POZZI,
           MOTIVO_MODIFICA, SUP_NON_ELEGGIBILE,SUP_NE_BOSCO_ACQUE_FABBRICATO,SUP_NE_FORAGGIERE, SUP_EL_FRUTTA_GUSCIO, SUP_EL_PRATO_PASCOLO,
           SUP_EL_COLTURE_MISTE,SUP_COLTIVAZ_ARBOREA_CONS, SUP_COLTIVAZ_ARBOREA_SPEC,DATA_FOTO, ID_FONTE, TIPO_FOTO,ID_DOCUMENTO, ID_IRRIGAZIONE, 
           ID_DOCUMENTO_PROTOCOLLATO,ID_FASCIA_FLUVIALE, ID_AREA_G, ID_AREA_H,SUPERFICIE_GRAFICA, ID_POTENZIALITA_IRRIGUA, ID_TERRAZZAMENTO, 
           ID_ROTAZIONE_COLTURALE, ID_AREA_L, ID_AREA_I,ID_AREA_M, PERCENTUALE_PENDENZA_MEDIA, GRADI_PENDENZA_MEDIA,GRADI_ESPOSIZIONE_MEDIA, 
           METRI_ALTITUDINE_MEDIA, ID_METODO_IRRIGUO) 
          VALUES 
          (nIdParticella,SMRGAA.SEQ_STORICO_PARTICELLA.NEXTVAL,rec.SEZIONE,rec.PROVINCIA||rec.COMUNE,SYSDATE,rec.N_FOGLIO,
           null,rec.N_PARTICELLA,NVL(TO_NUMBER(rec.SUPERFICIECATASTALE),0) / 10000,rec.SUBALTERNO,null,nZonaAlt,'N',null,
           NVL(TO_NUMBER(rec.CASIPARTICOLARI),99),null,null,SYSDATE,kIDUtente,null,'N',
           null,null,null,null,null,null,
           null,null,null,null,null,null,null,null,
           null,null,null,null,NVL(TO_NUMBER(rec.SUPERFICIEGRAFICA),0) / 10000,rec.ID_POTENZIALITA_IRRIGUA,rec.ID_TERRAZZAMENTO,
           rec.ID_ROTAZIONE_COLTURALE,null,null,null,null,null,null,
           nMediaAltitudine,null);
      END;
    END LOOP;
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nel popolamento dei dati dei terreni = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
  END popolaDatiTerreni;
  
  PROCEDURE popolaDatiConsistenza(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                                  pCodErrore   OUT NUMBER,
                                  pDescErrore  OUT VARCHAR2) IS
  
    nIDUte                   SMRGAA.DB_UTE.ID_UTE%TYPE;
    nIDPart                  SMRGAA.DB_PARTICELLA.ID_PARTICELLA%TYPE;
    dDataFineConduz          SMRGAA.DB_CONDUZIONE_PARTICELLA.DATA_FINE_CONDUZIONE%TYPE;
    nPercPossesso            SMRGAA.DB_CONDUZIONE_PARTICELLA.PERCENTUALE_POSSESSO%TYPE;
    nIdConduzioneParticella  SMRGAA.DB_CONDUZIONE_PARTICELLA.ID_CONDUZIONE_PARTICELLA%TYPE;
    nIDDocumento             SMRGAA.DB_DOCUMENTO.ID_DOCUMENTO%TYPE;
    recCondPart              SMRGAA.DB_CONDUZIONE_PARTICELLA%ROWTYPE;
    nCont                    SIMPLE_INTEGER := 0;
    idCondu                  TBL_ID := TBL_ID();
    idUte                    TBL_ID := TBL_ID(); n1 number; n2 number;
    nPrgSch	        SINC_FA_AABRSCHE_TAB.PRG_SCHEDA@db_sian%TYPE;
    vCuaa             SINC_FA_AABRSCHE_TAB.CUAA@db_sian%TYPE;
  
  BEGIN
    pCodErrore  := 0;
    pDescErrore := '';
  
    SELECT sche.prg_scheda, sche.cuaa
    INTO nPrgSch, vCuaa
    FROM SINC_FA_AABRSCHE_TAB@db_sian sche, SINC_DU_ABRAREDU_TAB@db_sian redu
    WHERE redu.id_elab = pIdChiamata
    AND lpad(to_char(redu.sche_fasc), 11, '0') = sche.id_scheda;
  
    FOR rec IN (SELECT AA.ID_AZIENDA,AA.CUAA,C.CODI_ISTA_PROV PROVINCIA, C.CODI_ISTA_COMU COMUNE,T.SEZI SEZIONE,T.FOGL FOGLIO,T.PART PARTICELLA,
                     TO_NUMBER(T.FOGL) N_FOGLIO,
                     TO_NUMBER(T.PART) N_PARTICELLA, TRIM(T.SUB) SUBALTERNO, C.CODI_ISTA_PROV|| C.CODI_ISTA_COMU ISTAT_COMUNE,
                     TO_NUMBER(DECODE(T.TIPO_COND,'9',5,T.TIPO_COND)) ID_TITOLO_POSSESSO,T.DATA_INIZ DATAINIZIOCONDUZIONE,
                     T.DATA_FINE DATAFINECONDUZIONE, T.SUPE_COND SUPERFICIECONDOTTA, T.SUPE_CATA SUPERFICIECATASTALE, F.DATA_INIZ_VALI DATADOCUMENTO, 
                     B.DECO_TIPO_DOCU TIPODOCUMENTO, B.ID_ATTO_AMMI NUMERODOCUMENTO
              FROM   SINC_FA_AABRPART_TAB@db_sian T, SMRGAA.DB_ANAGRAFICA_AZIENDA AA, SINC_FA_AABRFASC_TAB@db_sian F,
                     SINC_FA_AABRSOGG_TAB@db_sian C, SINC_DU_ABRAREDU_TAB@db_sian B
              WHERE  NVL(LENGTH(TRIM(TRANSLATE(REPLACE(T.PART,' '),'1234567890',' '))),0) = 0 -- alcune particelle NON sono SOLO numeriche le scarto      
              AND    T.PRG_SCHEDA            = nPrgSch
              AND    C.CODI_FISC             = F.CUAA
              AND    B.DATA_AGGI = (SELECT MAX(DATA_AGGI)FROM SINC_DU_ABRAREDU_TAB@db_sian WHERE SCHE_FASC = B.SCHE_FASC)
              AND    TO_NUMBER(T.ID_SCHEDA)  = B.SCHE_FASC
              AND    T.PRG_SCHEDA            = F.PRG_SCHEDA
              AND    AA.CUAA                 = F.CUAA
              AND    AA.DATA_FINE_VALIDITA   IS NULL
              ORDER BY 1,2,3,4,5,6,7,8,9,10,11,12) LOOP
  
      BEGIN
        SELECT SP.ID_PARTICELLA
        INTO   nIDPart                        
        FROM   SMRGAA.DB_STORICO_PARTICELLA SP
        WHERE  SP.COMUNE              = rec.ISTAT_COMUNE
        AND    NVL(SP.SEZIONE,'-')    = NVL(rec.SEZIONE,'-')
        AND    SP.FOGLIO              = rec.N_FOGLIO
        AND    SP.PARTICELLA          = rec.N_PARTICELLA
        AND    NVL(SP.SUBALTERNO,'-') = NVL(rec.SUBALTERNO,'-')
        AND    SP.DATA_FINE_VALIDITA  IS NULL;
  
        SELECT U.ID_UTE
        INTO   nIDUte
        FROM   SMRGAA.DB_UTE U
        WHERE  U.ID_AZIENDA         = rec.ID_AZIENDA
        AND    U.DATA_FINE_ATTIVITA IS NULL; 
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          nIDPart := NULL;
          nIDUte  := NULL;
          ----insertLog(1108, 'NO_DATA_FOUND', 'nIDPart: NULL nIDUte: NULL', vCuaa);
      END;
  
      IF nIDPart IS NOT NULL AND nIDUte IS NOT NULL THEN
        BEGIN   
          dDataFineConduz := ReturnData(rec.DATAFINECONDUZIONE);
  
          IF dDataFineConduz >= SYSDATE THEN
            dDataFineConduz := NULL;
          END IF;
  
          nPercPossesso := 0;
  
          n1 := TO_NUMBER(rec.SUPERFICIECONDOTTA);
          n2 := TO_NUMBER(rec.SUPERFICIECATASTALE);
  
          IF NVL(TO_NUMBER(rec.SUPERFICIECATASTALE),0) > 0 THEN
            nPercPossesso := ROUND(NVL(TO_NUMBER(rec.SUPERFICIECONDOTTA),0) / NVL(TO_NUMBER(rec.SUPERFICIECATASTALE),0),2) * 100;
          END IF;
  
          IF ABS(NVL(TO_NUMBER(rec.SUPERFICIECONDOTTA),0) - NVL(TO_NUMBER(rec.SUPERFICIECATASTALE),0)) <= 50 OR nPercPossesso > 100 THEN
            nPercPossesso := 100;
          END IF;
  
          SELECT *
          INTO   recCondPart
          FROM   SMRGAA.DB_CONDUZIONE_PARTICELLA CP
          WHERE  CP.ID_PARTICELLA      = nIDPart
          AND    CP.ID_UTE             = nIDUte
          AND    CP.ID_TITOLO_POSSESSO = rec.ID_TITOLO_POSSESSO;
  
          nIdConduzioneParticella := recCondPart.ID_CONDUZIONE_PARTICELLA;
  
          IF NVL(NVL(recCondPart.DATA_FINE_CONDUZIONE,dDataFineConduz),SYSDATE) != NVL(dDataFineConduz,SYSDATE) OR 
             recCondPart.DATA_INIZIO_CONDUZIONE                                 != ReturnData(rec.DATAINIZIOCONDUZIONE) OR
             recCondPart.SUPERFICIE_CONDOTTA                                    != (NVL(TO_NUMBER(rec.SUPERFICIECONDOTTA),0) / 10000) OR
             recCondPart.PERCENTUALE_POSSESSO                                   != nPercPossesso THEN
  
            UPDATE SMRGAA.DB_CONDUZIONE_PARTICELLA
            SET    DATA_FINE_CONDUZIONE    = dDataFineConduz,
                   DATA_INIZIO_CONDUZIONE  = ReturnData(rec.DATAINIZIOCONDUZIONE),
                   SUPERFICIE_CONDOTTA     = NVL(TO_NUMBER(rec.SUPERFICIECONDOTTA),0) / 10000,
                   PERCENTUALE_POSSESSO    = nPercPossesso,
                   DATA_AGGIORNAMENTO      = SYSDATE, 
                   ID_UTENTE_AGGIORNAMENTO = kIDUtente
            WHERE  ID_PARTICELLA           = nIDPart
            AND    ID_UTE                  = nIDUte
            AND    ID_TITOLO_POSSESSO      = rec.ID_TITOLO_POSSESSO;
          END IF;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            INSERT INTO SMRGAA.DB_CONDUZIONE_PARTICELLA 
            (ID_CONDUZIONE_PARTICELLA, ID_PARTICELLA, ID_TITOLO_POSSESSO,ID_UTE, SUPERFICIE_CONDOTTA, 
             FLAG_UTILIZZO_PARTE,DATA_INIZIO_CONDUZIONE,DATA_FINE_CONDUZIONE, NOTE,DATA_AGGIORNAMENTO, ID_UTENTE_AGGIORNAMENTO, ESITO_CONTROLLO,
             DATA_ESECUZIONE, RECORD_MODIFICATO,DICHIARAZIONE_RIPRISTINATA,ID_DICHIARAZIONE_CONSISTENZA, SUPERFICIE_AGRONOMICA, 
             PERCENTUALE_POSSESSO) 
            VALUES 
            (SMRGAA.SEQ_CONDUZIONE_PARTICELLA.NEXTVAL,nIDPart,rec.ID_TITOLO_POSSESSO,nIDUte,NVL(TO_NUMBER(rec.SUPERFICIECONDOTTA),0) / 10000,
             null,ReturnData(rec.DATAINIZIOCONDUZIONE),dDataFineConduz,null,SYSDATE,kIDUtente,null,
             null,null,null,null,null,
             nPercPossesso)
            RETURNING ID_CONDUZIONE_PARTICELLA INTO nIdConduzioneParticella;
            ----insertLog(1108, 'NO_DATA_FOUND', 'nIdConduzioneParticella: '||nIdConduzioneParticella, vCuaa); 
        END;
  
        idCondu.EXTEND;
        idCondu(idCondu.COUNT) := OBJ_ID(nIdConduzioneParticella);
  
        idUte.EXTEND;
        idUte(idUte.COUNT) := OBJ_ID(nIdConduzioneParticella);
  
        BEGIN
          SELECT D.ID_DOCUMENTO
          INTO   nIDDocumento                        
          FROM   SMRGAA.DB_TIPO_DOCUMENTO TD,SMRGAA.DB_DOCUMENTO D
          WHERE  D.NUMERO_PROTOCOLLO    = rec.NUMERODOCUMENTO
          AND    rec.TIPODOCUMENTO      = TD.IDENTIFICATIVO_SIAN
          AND    D.EXT_ID_DOCUMENTO     = TD.ID_DOCUMENTO
          AND    D.DATA_INIZIO_VALIDITA = ReturnData(rec.DATADOCUMENTO)
          AND    D.DATA_FINE_VALIDITA   IS NULL
          AND    D.ID_AZIENDA           = rec.ID_AZIENDA
          AND    TD.DATA_FINE_VALIDITA  IS NULL;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            nIDDocumento := NULL;
            ----insertLog(1108, 'NO_DATA_FOUND', 'nIDDocumento: NULL', vCuaa);
        END;
  
        IF nIDDocumento IS NOT NULL THEN
          SELECT COUNT(*)
          INTO   nCont
          FROM   SMRGAA.DB_DOCUMENTO_CONDUZIONE
          WHERE  ID_CONDUZIONE_PARTICELLA = nIdConduzioneParticella
          AND    ID_DOCUMENTO             = nIDDocumento
          AND    DATA_FINE_VALIDITA       IS NULL;
  
          IF nCont = 0 THEN
            INSERT INTO SMRGAA.DB_DOCUMENTO_CONDUZIONE 
            (ID_DOCUMENTO_CONDUZIONE, ID_CONDUZIONE_PARTICELLA, ID_DOCUMENTO,DATA_INSERIMENTO, DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA,
             LAVORAZIONE_PRIORITARIA, NOTE, ID_ALLEGATO) 
            VALUES 
            (SMRGAA.SEQ_DOCUMENTO_CONDUZIONE.NEXTVAL,nIdConduzioneParticella,nIDDocumento,SYSDATE,ReturnData(rec.DATAINIZIOCONDUZIONE),NULL,
             NULL,NULL,NULL);
          ELSE
            UPDATE SMRGAA.DB_DOCUMENTO_CONDUZIONE
            SET    DATA_INIZIO_VALIDITA     = ReturnData(rec.DATAINIZIOCONDUZIONE)
            WHERE  ID_CONDUZIONE_PARTICELLA = nIdConduzioneParticella
            AND    ID_DOCUMENTO             = nIDDocumento
            AND    DATA_FINE_VALIDITA       IS NULL;
          END IF;
        END IF;
      END IF;
    END LOOP;
  
    -- chiudere DB_CONDUZIONE_PARTICELLA x id_azienda e id non trattati
    UPDATE SMRGAA.DB_CONDUZIONE_PARTICELLA
    SET    DATA_FINE_CONDUZIONE     = dDataFineConduz,
           DATA_AGGIORNAMENTO       = SYSDATE, 
           ID_UTENTE_AGGIORNAMENTO  = kIDUtente
    WHERE  ID_UTE                   IN (SELECT ID
                                        FROM   TABLE(idUte))
    AND    ID_CONDUZIONE_PARTICELLA NOT IN (SELECT ID
                                            FROM   TABLE(idCondu));
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nel popolamento dei dati di consistenza = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE
                     ||' - '||n1||' - '||n2;
      /*--insertLog(1108, 'OTHERS', 'Errore generico nel popolamento dei dati di consistenza = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE
                     ||' - '||n1||' - '||n2, vCuaa);*/
  END popolaDatiConsistenza;
  
  PROCEDURE popolaDatiColture(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                              pCodErrore   OUT NUMBER,
                              pDescErrore  OUT VARCHAR2) IS
  
    TYPE typeCatalogoAll IS RECORD(IDCatalogMatrix  SMRGAA.DB_R_CATALOGO_MATRICE.ID_CATALOGO_MATRICE%TYPE,
                                   IDUtilizzo       SMRGAA.DB_R_CATALOGO_MATRICE.ID_UTILIZZO%TYPE,
                                   IDDettUso        SMRGAA.DB_R_CATALOGO_MATRICE.ID_TIPO_DETTAGLIO_USO%TYPE,
                                   IDVarieta        SMRGAA.DB_R_CATALOGO_MATRICE.ID_VARIETA%TYPE,
                                   IDTipoEfa        SMRGAA.DB_UTILIZZO_PARTICELLA.ID_TIPO_EFA%TYPE,
                                   IDPeriodoSemina  SMRGAA.DB_UTILIZZO_PARTICELLA.ID_TIPO_PERIODO_SEMINA%TYPE,
                                   dataIniSemina    SMRGAA.DB_UTILIZZO_PARTICELLA.DATA_INIZIO_DESTINAZIONE%TYPE,
                                   dataFineSemina   SMRGAA.DB_UTILIZZO_PARTICELLA.DATA_FINE_DESTINAZIONE%TYPE,
                                   IDPraticaMant    SMRGAA.DB_UTILIZZO_PARTICELLA.ID_PRATICA_MANTENIMENTO%TYPE,
                                   flagPratoPerm    SMRGAA.DB_R_CATALOGO_MATRICE.FLAG_PRATO_PERMANENTE%TYPE);
  
  
    nIDUte          SMRGAA.DB_UTE.ID_UTE%TYPE;
    nIDStoricoPart  SMRGAA.DB_STORICO_PARTICELLA.ID_STORICO_PARTICELLA%TYPE;
    nIDPart         SMRGAA.DB_PARTICELLA.ID_PARTICELLA%TYPE;        
    nIDConduzPart   SMRGAA.DB_CONDUZIONE_PARTICELLA.ID_CONDUZIONE_PARTICELLA%TYPE;
    nIDSemina       SMRGAA.DB_UTILIZZO_PARTICELLA.ID_SEMINA%TYPE;
    nIDFaseAllevam  SMRGAA.DB_UTILIZZO_PARTICELLA.ID_FASE_ALLEVAMENTO%TYPE;
    nIDPraticaMant  SMRGAA.DB_UTILIZZO_PARTICELLA.ID_PRATICA_MANTENIMENTO%TYPE;
    recCatalogoAll  typeCatalogoAll;                                                
  BEGIN
    pCodErrore  := 0;
    pDescErrore := '';
  
    -- cancellare DB_UTILIZZO_PARTICELLA x l'azienda in input
   /* DELETE SMRGAA.DB_UTILIZZO_PARTICELLA
    WHERE  ID_CONDUZIONE_PARTICELLA IN (SELECT ID_CONDUZIONE_PARTICELLA
                                        FROM   SMRGAA.DB_CONDUZIONE_PARTICELLA CP,SMRGAA.DB_UTE U,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,
                                               ISWSRESPANAGFASCICOLO15 RAF
                                        WHERE  AA.CUAA               = RAF.CUAA
                                        AND    AA.DATA_FINE_VALIDITA IS NULL
                                        AND    RAF.ID_CHIAMATA       = pIdChiamata
                                        AND    U.ID_AZIENDA          = AA.ID_AZIENDA
                                        AND    CP.ID_UTE             = U.ID_UTE);*/
  
    -- cancellare DB_UTILIZZO_PARTICELLA x l'azienda in input                                  
    DELETE SMRGAA.DB_UTILIZZO_PARTICELLA
    WHERE  ID_CONDUZIONE_PARTICELLA IN (SELECT ID_CONDUZIONE_PARTICELLA
                                        FROM SMRGAA.DB_CONDUZIONE_PARTICELLA CP, SMRGAA.DB_UTE U, SMRGAA.DB_ANAGRAFICA_AZIENDA AA,
                                             SINC_FA_AABRFASC_TAB@db_sian RAF
                                        WHERE  AA.CUAA               = RAF.CUAA
                                        AND    AA.DATA_FINE_VALIDITA IS NULL
                                        AND    RAF.PRG_SCHEDA       = pIdChiamata
                                        AND    U.ID_AZIENDA          = AA.ID_AZIENDA
                                        AND    CP.ID_UTE             = U.ID_UTE);
  
    FOR rec IN (SELECT AA.ID_AZIENDA,AA.CUAA,T.PROVINCIA,T.COMUNE,T.SEZIONE,T.FOGLIO,T.PARTICELLA,TO_NUMBER(T.FOGLIO) N_FOGLIO,
                       TO_NUMBER(T.PARTICELLA) N_PARTICELLA,TRIM(T.SUBALTERNO) SUBALTERNO,
                       TO_NUMBER(DECODE(T.TIPO_CONDUZIONE,9,5,T.TIPO_CONDUZIONE)) ID_TITOLO_POSSESSO,T.DATAINIZIOCONDUZIONE,
                       T.DATAFINECONDUZIONE,UT.CODICEMACROUSO,UT.CODICEQUALITA,UT.DATAFINEUTILIZZO,UT.DATAINIZIOUTILIZZO,UT.SUPERFICIEELEGGIBILE, 
                       UT.SUPERFICIEUTILIZZATA,UT.CODICEPRODOTTO,UT.CODICEVARIETA,UT.FLAGCOLTPRINCIPALE,UT.ALTITUDINE,UT.ANNOIMPIANTO,
                       UT.CAPACITAPRODUTTIVA,UT.CODICECOLTIVAZIONEBIO,UT.CODICEFASEALLEVAMENTO,UT.CODICEFORMAALLEVAMENTO,
                       UT.MANTPRATIPERMANENTE,DECODE(UT.MANTENSUPAGRICOLA,'2','9','1','8',NULL) MANTENSUPAGRICOLA,UT.MENZIONE,
                       UT.NUMEROPIANTE,UT.PERCENTUALE,UT.CODICEPROTEZCOLTURE,UT.SESTOIMPIANTOSUFILA,UT.SESTOIMPIANTOTRAFILA,
                       UT.SUPERFICIEELEGGIBILE SUPERFICIEELIGIBILE_DET,UT.SUPERFICIEUTILIZZATA SUPERFICIEUTILIZZATA_DET,
                       UT.TIPOCTIPERTIFICAZIONE,UT.TIPOIMPIANTO,UT.TIPOUTILIZZOOLIVO,UT.CODICETIPOIRRIGAZIONE,UT.CODICETIPOSEMINA,
                       T.PROVINCIA||T.COMUNE ISTAT_COMUNE,CODICEUSO,CODICEDESTIINAZIONEUSO,CODICEOCCUPAZIONEVARIETA,CODICEOCCUPAZIONESUOLO,
                       NVL(RAF.DATASCHEDAVALIDAZIONE,RAF.DATAVALIDAZFASCICOLO) DATASCHEDAVALIDAZIONE
                FROM   ISWSTERRITORIOFS6 T,WSUTILIZZOTERRACABA UT,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,ISWSRESPANAGFASCICOLO15 RAF
                WHERE  AA.CUAA                 = RAF.CUAA
                AND    AA.DATA_FINE_VALIDITA   IS NULL
                AND    RAF.ID_CHIAMATA         = pIdChiamata
                AND    T.ID_CHIAMATA           = RAF.ID_CHIAMATA
                AND    UT.ID_ISWSTERRITORIOFS6 = T.ID_ISWSTERRITORIOFS6 
                AND    NVL(LENGTH(TRIM(TRANSLATE(REPLACE(PARTICELLA,' '),'1234567890',' '))),0) = 0) LOOP -- alcune particelle NON sono SOLO numeriche le scarto
  
                /*BOZZA NUOVA QUERY
                SELECT AA.ID_AZIENDA,AA.CUAA,T.PROVINCIA,T.COMUNE,T.SEZIONE,T.FOGLIO,T.PARTICELLA,
    TO_NUMBER(T.FOGLIO) N_FOGLIO, TO_NUMBER(T.PARTICELLA) N_PARTICELLA,
    TRIM(T.SUBALTERNO) SUBALTERNO,
    TO_NUMBER(DECODE(T.TIPO_CONDUZIONE,9,5,T.TIPO_CONDUZIONE)) ID_TITOLO_POSSESSO,--TIPO_COND tabella SINC_FA_AABRPART_TAB
    T.DATAINIZIOCONDUZIONE,--presente, è la data inizio della tabella SINC_FA_AABRPART_TAB 
    T.DATAFINECONDUZIONE,--presente, è la data fine della tabella SINC_FA_AABRPART_TAB 
    UT.CODICEMACROUSO,--CODI_RILE tabella SINC_FA_AABRAPPE_TAB 
    UT.CODICEQUALITA,--non presente
    UT.DATAFINEUTILIZZO,--DATA_INIZIO tabella SINC_FA_AABRAPPE_TAB 
    UT.DATAINIZIOUTILIZZO,--DATA_FINE tabella SINC_FA_AABRAPPE_TAB 
    UT.SUPERFICIEELEGGIBILE, --presenti diversi campi superficie ma non so quale sia eleggibile
   UT.SUPERFICIEUTILIZZATA,--presenti diversi campi superficie ma non so quale sia utilizzata
   UT.CODICEPRODOTTO,--NON PRESENTE
   UT.CODICEVARIETA,--NON PRESENTE
   UT.FLAGCOLTPRINCIPALE,--presente
   UT.ALTITUDINE,-- ALTITUDINE_SLM  tabella SITIUNAR da mettere in join tramite dati catastali
   UT.ANNOIMPIANTO,--NON PRESENTE
   UT.CAPACITAPRODUTTIVA,--NON PRESENTE
   UT.CODICECOLTIVAZIONEBIO,--FLAG_BIOL
   UT.CODICEFASEALLEVAMENTO,--FASE_DI_ALLEVAMENTO
   UT.CODICEFORMAALLEVAMENTO,--FORMA_DI_ALLEVAMENTO
   UT.MANTPRATIPERMANENTE,--CODI_PRAT_PERM tabella SINC_FA_AABRAPPE_DETT_TAB
   DECODE(UT.MANTENSUPAGRICOLA,'2','9','1','8',NULL) MANTENSUPAGRICOLA,--CODI_PRAT_ALTR
   UT.MENZIONE,--non presente
   UT.NUMEROPIANTE,--NUME_PIAN tabella SITIPIANTE da legare tramite dati catastali
   UT.PERCENTUALE, --non presente
   UT.CODICEPROTEZCOLTURE,--non presente
   UT.SESTOIMPIANTOSUFILA,--SESTO_SU_FILA tabella SITIUNAR da mettere in join tramite dati catastali
   UT.SESTOIMPIANTOTRAFILA,--SESTO_TRA_FILE  tabella SITIUNAR da mettere in join tramite dati catastali
   UT.SUPERFICIEELEGGIBILE SUPERFICIEELIGIBILE_DET,--non presente 
   UT.SUPERFICIEUTILIZZATA SUPERFICIEUTILIZZATA_DET,--non presente
   UT.TIPOCTIPERTIFICAZIONE, --non presente
   UT.TIPOIMPIANTO,--TIPO_DI_IMPIANTO
   UT.TIPOUTILIZZOOLIVO,--non presente
   UT.CODICETIPOIRRIGAZIONE,--TIPO_IRRIGAZIONE
   UT.CODICETIPOSEMINA,--TIPO_DI_SEMINA
   T.PROVINCIA||T.COMUNE ISTAT_COMUNE,--presente
   CODICEUSO, --CODI_USOO tabella SINC_FA_AABRAPPE_DETT_TAB 
   CODICEDESTIINAZIONEUSO,----CODI_DEST_USO tabella SINC_FA_AABRAPPE_DETT_TAB join con AABRCATA5_TAB campo CODI_PROD.
   CODI_DEST_USO CODICEOCCUPAZIONEVARIETA,--CODI_DEST_USO tabella SINC_FA_AABRAPPE_DETT_TAB
   CODICEOCCUPAZIONESUOLO,--CODI_OCCU tabella SINC_FA_AABRAPPE_DETT_TAB 
   NVL(RAF.DATASCHEDAVALIDAZIONE, RAF.DATAVALIDAZFASCICOLO) DATASCHEDAVALIDAZIONE--DATA_SCHEDA tabella SINC_FA_AABRSCHE_TAB, altrimenti DATA_INIZ_VALI tabella SINC_FA_AABRFASC_TAB
    FROM   SINC_FA_AABRAPPE_DETT_TAB T, SINC_FA_AABRAPPE_TAB UT,
           SMRGAA.DB_ANAGRAFICA_AZIENDA AA, SINC_FA_AABRFASC_TAB RAF,
           SINC_FA_AABRSCHE_TAB SCHE, SINC_FA_AABRPART_TAB PART
    WHERE  RAF.PRG_SCHEDA         = pIdChiamata
    AND    AA.CUAA                = RAF.CUAA
    AND    SCHE.PRG_SCHEDA        = RAF.PRG_SCHEDA
    AND    T.COD_APPE             = UT.COD_APPE
    --MANCA LA JOIN CON SINC_FA_AABRPART_TAB
    AND    AA.DATA_FINE_VALIDITA  IS NULL
    AND    T.PRG_SCHEDA           = RAF.PRG_SCHEDA
    AND    NVL(LENGTH(TRIM(TRANSLATE(REPLACE(PART.PART,' '),'1234567890',' '))),0) = 0;
                */
  
      BEGIN
        SELECT U.ID_UTE,SP.ID_STORICO_PARTICELLA,SP.ID_PARTICELLA,CP.ID_CONDUZIONE_PARTICELLA
        INTO   nIDUte,nIDStoricoPart,nIDPart,nIDConduzPart
        FROM   SMRGAA.DB_STORICO_PARTICELLA SP,SMRGAA.DB_UTE U,SMRGAA.DB_CONDUZIONE_PARTICELLA CP
        WHERE  SP.COMUNE                                              = rec.ISTAT_COMUNE
        AND    NVL(SP.SEZIONE,'-')                                    = NVL(rec.SEZIONE,'-')
        AND    SP.FOGLIO                                              = rec.N_FOGLIO
        AND    SP.PARTICELLA                                          = rec.N_PARTICELLA
        AND    NVL(SP.SUBALTERNO,'-')                                 = NVL(rec.SUBALTERNO,'-')
        AND    SP.DATA_FINE_VALIDITA                                  IS NULL
        AND    U.ID_AZIENDA                                           = rec.ID_AZIENDA
        AND    U.DATA_FINE_ATTIVITA                                   IS NULL
        AND    CP.ID_UTE                                              = U.ID_UTE
        AND    CP.ID_PARTICELLA                                       = SP.ID_PARTICELLA
        AND    CP.ID_TITOLO_POSSESSO                                  = rec.ID_TITOLO_POSSESSO
        AND    NVL(CP.DATA_FINE_CONDUZIONE,ReturnData(kDataFineMax)) >= ReturnData(rec.DATAFINECONDUZIONE)
        AND    CP.DATA_INIZIO_CONDUZIONE                              = ReturnData(rec.DATAINIZIOCONDUZIONE);
  
        -- cerco CATALOGO MATRICE...                                  
        BEGIN
          SELECT DISTINCT M.ID_CATALOGO_MATRICE,M.ID_UTILIZZO,M.ID_TIPO_DETTAGLIO_USO,M.ID_VARIETA,TETV.ID_TIPO_EFA,M.ID_TIPO_PERIODO_SEMINA,
                          TO_DATE(CMS.INIZIO_DESTINAZIONE_DEFAULT||'/'||SUBSTR(rec.DATASCHEDAVALIDAZIONE,1,4) ,kFormatoDataStd) DATA_INI_SEMINA,
                          TO_DATE(CMS.FINE_DESTINAZIONE_DEFAULT||'/'||SUBSTR(rec.DATASCHEDAVALIDAZIONE,1,4) ,kFormatoDataStd) DATA_FINE_SEMINA,
                          CMA.ID_PRATICA_MANTENIMENTO,M.FLAG_PRATO_PERMANENTE
          INTO   recCatalogoAll
          FROM   SMRGAA.SMRGAA_V_MATRICE M,SMRGAA.DB_TIPO_UTILIZZO TU,SMRGAA.DB_TIPO_DESTINAZIONE TD,SMRGAA.DB_TIPO_DETTAGLIO_USO TDU,
                 SMRGAA.DB_TIPO_QUALITA_USO TQU,SMRGAA.DB_TIPO_VARIETA TV,SMRGAA.DB_TIPO_LIVELLO TL,SMRGAA.DB_R_CATALOGO_MANTENIMENTO CMA ,
                 SMRGAA.DB_TIPO_EFA_TIPO_VARIETA TETV,SMRGAA.DB_R_CATALOGO_MATRICE_SEMINA CMS
          WHERE  TU.ID_UTILIZZO               = M.ID_UTILIZZO
          AND    TD.ID_TIPO_DESTINAZIONE(+)   = M.ID_TIPO_DESTINAZIONE
          AND    TDU.ID_TIPO_DETTAGLIO_USO(+) = M.ID_TIPO_DETTAGLIO_USO
          AND    TQU.ID_TIPO_QUALITA_USO(+)   = M.ID_TIPO_QUALITA_USO
          AND    TU.ID_UTILIZZO               = TV.ID_UTILIZZO
          AND    TV.ID_VARIETA                = M.ID_VARIETA
          AND    M.ID_TIPO_LIVELLO            = TL.ID_TIPO_LIVELLO
          AND    M.ID_CATALOGO_MATRICE        = TETV.ID_CATALOGO_MATRICE(+)
          AND    TETV.DATA_FINE_VALIDITA(+)   IS NULL
          AND    TU.CODICE                    = rec.codiceOccupazioneSuolo
          AND    TV.CODICE_VARIETA            = NVL(rec.CodiceOccupazioneVarieta,'000') 
          AND    TD.CODICE_DESTINAZIONE       = NVL(rec.CodiceDestiInazioneUso,'000')
          AND    TDU.CODICE_DETTAGLIO_USO     = NVL(rec.CodiceUso,'000')
          AND    TQU.CODICE_QUALITA_USO       = NVL(rec.CodiceQualita,'000')
          AND    NVL(M.FINE_VALIDITA_MATRICE,TO_DATE('31/12/9999','DD/MM/YYYY')) > SYSDATE
          AND    M.ID_CATALOGO_MATRICE        = CMA.ID_CATALOGO_MATRICE(+)
          AND    CMA.FLAG_DEFAULT(+)          = 'S'
          AND    CMA.DATA_FINE_VALIDITA(+)    IS NULL
          AND    M.ID_CATALOGO_MATRICE        = CMS.ID_CATALOGO_MATRICE(+)
          AND    CMS.DATA_FINE_VALIDITA(+)    IS NULL
          AND    CMS.FLAG_DEFAULT(+)          = 'S';
        EXCEPTION
          WHEN OTHERS THEN
            pCodErrore  := 1;
            pDescErrore := 'eccezione sulla ricerca catalogo matrice. Codice Occupazione Suolo = '||rec.codiceOccupazioneSuolo||
                           ', Codice Occupazione Varieta = '||rec.CodiceOccupazioneVarieta||
                           ', Codice Destinazione Uso = '||rec.CodiceDestiInazioneUso||
                           ', Codice Uso = '||rec.CodiceUso||
                           ', Codice Qualita = '||rec.CodiceQualita;
            RETURN;
        END;
        /*
        BEGIN
          -- cerco catalogo valido per 5 livelli quindi 2016
          SELECT distinct CM.ID_CATALOGO_MATRICE,CM.ID_UTILIZZO,CM.ID_TIPO_DETTAGLIO_USO,CM.ID_VARIETA,EV.ID_TIPO_EFA,CMS.ID_TIPO_PERIODO_SEMINA,
                 TO_DATE(CMS.INIZIO_DESTINAZIONE_DEFAULT || '/' || TO_CHAR(kAnnoVersMatrix - CMS.ANNO_DECODIFICA_PRE_DATA),kFormatoDataStd) DATA_INI_SEMINA,
                 TO_DATE(CMS.FINE_DESTINAZIONE_DEFAULT || '/' || TO_CHAR(kAnnoVersMatrix - CMS.ANNO_DECODIFICA_POST_DATA),kFormatoDataStd) DATA_FINE_SEMINA,
                 CMN.ID_PRATICA_MANTENIMENTO,CM.FLAG_PRATO_PERMANENTE
          INTO   recCatalogoAll                         
          FROM   SMRGAA.DB_TIPO_UTILIZZO TU,SMRGAA.DB_TIPO_DETTAGLIO_USO DU,SMRGAA.DB_TIPO_VARIETA TV,SMRGAA.DB_TIPO_DESTINAZIONE TD,
                 SMRGAA.DB_TIPO_QUALITA_USO QU,SMRGAA.DB_R_CATALOGO_MATRICE CM,SMRGAA.DB_TIPO_VERSIONE_MATRICE VM,
                 SMRGAA.DB_TIPO_EFA_TIPO_VARIETA EV,SMRGAA.DB_R_CATALOGO_MATRICE_SEMINA CMS,SMRGAA.DB_R_CATALOGO_MANTENIMENTO CMN
          WHERE  CM.ID_UTILIZZO           = TU.ID_UTILIZZO
          AND    CM.ID_TIPO_DETTAGLIO_USO = DU.ID_TIPO_DETTAGLIO_USO
          AND    CM.ID_VARIETA            = TV.ID_VARIETA
          AND    TU.CODICE                = rec.CODICEPRODOTTO
          AND    ((DU.CODICE_DETTAGLIO_USO = rec.CODICEVARIETA AND TV.CODICE_VARIETA = '000') -- DEFAULT
                   OR (NOT EXISTS (SELECT 'X'
                                   FROM   SMRGAA.DB_TIPO_DETTAGLIO_USO DU2,SMRGAA.DB_R_CATALOGO_MATRICE CM2
                                   WHERE  CM2.ID_UTILIZZO           = CM.ID_UTILIZZO
                                   AND    CM2.ID_VERSIONE_MATRICE   = CM.ID_VERSIONE_MATRICE
                                   AND    CM2.ID_TIPO_DETTAGLIO_USO = DU2.ID_TIPO_DETTAGLIO_USO
                                   AND    DU2.CODICE_DETTAGLIO_USO  = rec.CODICEVARIETA)
                       AND TV.CODICE_VARIETA       = rec.CODICEVARIETA
                       AND DU.CODICE_DETTAGLIO_USO = '000')) -- DEFAULT
          AND    CM.ID_TIPO_DESTINAZIONE                              = TD.ID_TIPO_DESTINAZIONE    
          AND    CM.ID_TIPO_QUALITA_USO                               = QU.ID_TIPO_QUALITA_USO
          AND    QU.CODICE_QUALITA_USO                                = '000'   -- DEFAULT --      
          AND    CM.ID_CATALOGO_MATRICE                               = EV.ID_CATALOGO_MATRICE(+)
          AND    EV.DATA_FINE_VALIDITA(+)                             IS NULL
          AND    CM.ID_CATALOGO_MATRICE                               = CMS.ID_CATALOGO_MATRICE(+)
          AND    CMS.DATA_FINE_VALIDITA(+)                            IS NULL
          AND    CMS.FLAG_DEFAULT(+)                                  = 'S'
          AND    CM.ID_CATALOGO_MATRICE                               = CMN.ID_CATALOGO_MATRICE(+)
          AND    CMN.DATA_FINE_VALIDITA(+)                            IS NULL
          AND    CMN.FLAG_DEFAULT(+)                                  = 'S' 
          AND    CM.DATA_INIZIO_VALIDITA                             <= NVL(ReturnData(rec.DATAINIZIOUTILIZZO),CM.DATA_INIZIO_VALIDITA)
          AND    NVL(CM.DATA_FINE_VALIDITA,ReturnData(kDataFineMax)) >=  NVL(ReturnData(rec.DATAFINEUTILIZZO),SYSDATE)
          AND    CM.ID_VERSIONE_MATRICE                               = VM.ID_VERSIONE_MATRICE
          AND    VM.ANNO_VERSIONE                                     = kAnnoVersMatrix;
        EXCEPTION
          -- se prodotto,varieta/uso sono ante 2016 cerco nelle matrici vecchie
          WHEN NO_DATA_FOUND THEN
            BEGIN
              SELECT DISTINCT CM2.ID_CATALOGO_MATRICE,CM2.ID_UTILIZZO,CM2.ID_TIPO_DETTAGLIO_USO,CM2.ID_VARIETA,EV.ID_TIPO_EFA,
                     CMS.ID_TIPO_PERIODO_SEMINA,
                     TO_DATE(CMS.INIZIO_DESTINAZIONE_DEFAULT || '/' || TO_CHAR(kAnnoVersMatrix - CMS.ANNO_DECODIFICA_PRE_DATA),kFormatoDataStd) DATA_INI_SEMINA,
                     TO_DATE(CMS.FINE_DESTINAZIONE_DEFAULT || '/' || TO_CHAR(kAnnoVersMatrix - CMS.ANNO_DECODIFICA_POST_DATA),kFormatoDataStd) DATA_FINE_SEMINA,
                     CMN.ID_PRATICA_MANTENIMENTO,CM2.FLAG_PRATO_PERMANENTE
              INTO   recCatalogoAll                                  
              FROM   SMRGAA.DB_TIPO_UTILIZZO TU,SMRGAA.DB_TIPO_DETTAGLIO_USO DU,SMRGAA.DB_TIPO_VARIETA TV,SMRGAA.DB_R_CATALOGO_MATRICE CM,
                     SMRGAA.DB_R_CATALOGO_MATRICE CM2,SMRGAA.DB_TIPO_VERSIONE_MATRICE VM,SMRGAA.DB_TIPO_EFA_TIPO_VARIETA EV,
                     SMRGAA.DB_R_CATALOGO_MATRICE_SEMINA CMS,SMRGAA.DB_R_CATALOGO_MANTENIMENTO CMN
              WHERE  CM.ID_UTILIZZO           = TU.ID_UTILIZZO
              AND    CM.ID_TIPO_DETTAGLIO_USO = DU.ID_TIPO_DETTAGLIO_USO
              AND    CM.ID_VARIETA            = TV.ID_VARIETA
              AND    TU.CODICE                = rec.CODICEPRODOTTO
              AND    ((DU.CODICE_DETTAGLIO_USO = rec.CODICEVARIETA AND TV.CODICE_VARIETA = '000') -- DEFAULT
                      OR (NOT EXISTS (SELECT 'X'
                                      FROM   SMRGAA.DB_TIPO_DETTAGLIO_USO DU2,SMRGAA.DB_R_CATALOGO_MATRICE CM3
                                      WHERE  CM3.ID_UTILIZZO           = CM.ID_UTILIZZO
                                      AND    CM3.ID_VERSIONE_MATRICE   = CM.ID_VERSIONE_MATRICE
                                      AND    CM3.ID_TIPO_DETTAGLIO_USO = DU2.ID_TIPO_DETTAGLIO_USO
                                      AND    DU2.CODICE_DETTAGLIO_USO  = rec.CODICEVARIETA)
                          AND TV.CODICE_VARIETA       = rec.CODICEVARIETA
                          AND DU.CODICE_DETTAGLIO_USO = '000')) -- DEFAULT
              AND    CM2.ID_CATALOGO_MATRICE        = EV.ID_CATALOGO_MATRICE(+)
              AND    EV.DATA_FINE_VALIDITA(+)       IS NULL         
              AND    CM2.ID_CATALOGO_MATRICE        = CMS.ID_CATALOGO_MATRICE(+)
              AND    CMS.DATA_FINE_VALIDITA(+)      IS NULL
              AND    CMS.FLAG_DEFAULT(+)            = 'S'
              AND    CM2.ID_CATALOGO_MATRICE        = CMN.ID_CATALOGO_MATRICE(+)
              AND    CMN.DATA_FINE_VALIDITA(+)      IS NULL
              AND    CMN.FLAG_DEFAULT(+)            = 'S'
              AND    CM.ID_CATALOGO_MATRICE_FIGLIO  = CM2.ID_CATALOGO_MATRICE   -- N.B. un padre puo' avere 2 figli ...quindi ciapo ID CAT FIGLIO per sapere qual è mio figlio, non uso ID CAT PADRE
              AND    VM.DATA_VERSIONE              <= NVL(ReturnData(rec.DATAINIZIOUTILIZZO),CM.DATA_INIZIO_VALIDITA)
              AND    CM.ID_VERSIONE_MATRICE         = VM.ID_VERSIONE_MATRICE
              AND    VM.ANNO_VERSIONE               < kAnnoVersMatrix;
            EXCEPTION
              WHEN NO_DATA_FOUND THEN
                pCodErrore  := 1;
                pDescErrore := 'Eccezione sulla ricerca catalogo matrice. Prodotto = '||rec.CODICEPRODOTTO||', Varieta = '||rec.CODICEVARIETA||
                               ', Data Inizio Utilizzo = '||rec.DATAINIZIOUTILIZZO;
                RETURN;
            END;
          -- se trovo piu' destinazioni o cmq piu' record..
          WHEN TOO_MANY_ROWS THEN
            BEGIN
              SELECT CM.ID_CATALOGO_MATRICE,CM.ID_UTILIZZO,CM.ID_TIPO_DETTAGLIO_USO,CM.ID_VARIETA,EV.ID_TIPO_EFA,CMS.ID_TIPO_PERIODO_SEMINA,
                     TO_DATE(CMS.INIZIO_DESTINAZIONE_DEFAULT||'/'||TO_CHAR(kAnnoVersMatrix - CMS.ANNO_DECODIFICA_PRE_DATA),kFormatoDataStd) DATA_INI_SEMINA,
                     TO_DATE(CMS.FINE_DESTINAZIONE_DEFAULT||'/'||TO_CHAR(kAnnoVersMatrix - CMS.ANNO_DECODIFICA_POST_DATA),kFormatoDataStd) DATA_FINE_SEMINA,
                     CMN.ID_PRATICA_MANTENIMENTO,CM.FLAG_PRATO_PERMANENTE
              INTO   recCatalogoAll                                                                    
              FROM   SMRGAA.DB_TIPO_UTILIZZO TU,SMRGAA.DB_TIPO_DETTAGLIO_USO DU,SMRGAA.DB_TIPO_VARIETA TV,SMRGAA.DB_TIPO_DESTINAZIONE TD,
                     SMRGAA.DB_TIPO_QUALITA_USO QU,SMRGAA.DB_R_CATALOGO_MATRICE CM,SMRGAA.DB_TIPO_VERSIONE_MATRICE VM,
                     SMRGAA.DB_TIPO_EFA_TIPO_VARIETA EV,SMRGAA.DB_R_CATALOGO_MATRICE_SEMINA CMS,SMRGAA.DB_R_CATALOGO_MANTENIMENTO CMN
              WHERE  CM.ID_UTILIZZO            = TU.ID_UTILIZZO
              AND    CM.ID_TIPO_DETTAGLIO_USO  = DU.ID_TIPO_DETTAGLIO_USO
              AND    CM.ID_VARIETA             = TV.ID_VARIETA
              AND    TU.CODICE                 = rec.CODICEPRODOTTO
              AND    ((DU.CODICE_DETTAGLIO_USO = rec.CODICEVARIETA AND TV.CODICE_VARIETA = '000') -- DEFAULT
                       OR (NOT EXISTS (SELECT 'X'
                                       FROM   SMRGAA.DB_TIPO_DETTAGLIO_USO DU2,SMRGAA.DB_R_CATALOGO_MATRICE CM2
                                       WHERE  CM2.ID_UTILIZZO           = CM.ID_UTILIZZO
                                       AND    CM2.ID_VERSIONE_MATRICE   = CM.ID_VERSIONE_MATRICE
                                       AND    CM2.ID_TIPO_DETTAGLIO_USO = DU2.ID_TIPO_DETTAGLIO_USO
                                       AND    DU2.CODICE_DETTAGLIO_USO  = rec.CODICEVARIETA)
                           AND TV.CODICE_VARIETA       = rec.CODICEVARIETA
                           AND DU.CODICE_DETTAGLIO_USO = '000')) -- DEFAULT
              AND    CM.ID_TIPO_DESTINAZIONE   = TD.ID_TIPO_DESTINAZIONE    
              AND    TD.CODICE_DESTINAZIONE    = '000' -- DEFAULT --          
              AND    CM.ID_TIPO_QUALITA_USO    = QU.ID_TIPO_QUALITA_USO
              AND    QU.CODICE_QUALITA_USO     = '000'   -- DEFAULT --    
              AND    CM.ID_CATALOGO_MATRICE    = EV.ID_CATALOGO_MATRICE(+)
              AND    EV.DATA_FINE_VALIDITA(+)  IS NULL
              AND    CM.ID_CATALOGO_MATRICE    = CMS.ID_CATALOGO_MATRICE(+)
              AND    CMS.DATA_FINE_VALIDITA(+) IS NULL
              AND    CMS.FLAG_DEFAULT(+)       = 'S'
              AND    CM.ID_CATALOGO_MATRICE    = CMN.ID_CATALOGO_MATRICE(+)
              AND    CMN.DATA_FINE_VALIDITA(+) IS NULL
              AND    CMN.FLAG_DEFAULT(+)       = 'S'
              AND    CM.ID_VERSIONE_MATRICE    = VM.ID_VERSIONE_MATRICE
              AND    VM.ANNO_VERSIONE          = kAnnoVersMatrix
              AND    CM.DATA_FINE_VALIDITA     IS NULL;
            EXCEPTION
              -- se con destinazione generica non trovo.. prendo la prima                                        
              WHEN NO_DATA_FOUND THEN
                SELECT *
                INTO   recCatalogoAll  
                FROM   (SELECT CM.ID_CATALOGO_MATRICE,CM.ID_UTILIZZO,CM.ID_TIPO_DETTAGLIO_USO,CM.ID_VARIETA,EV.ID_TIPO_EFA,
                               CMS.ID_TIPO_PERIODO_SEMINA,
                               TO_DATE(CMS.INIZIO_DESTINAZIONE_DEFAULT||'/'||TO_CHAR(kAnnoVersMatrix - CMS.ANNO_DECODIFICA_PRE_DATA),kFormatoDataStd) DATA_INI_SEMINA,
                               TO_DATE(CMS.FINE_DESTINAZIONE_DEFAULT||'/'||TO_CHAR(kAnnoVersMatrix - CMS.ANNO_DECODIFICA_POST_DATA),kFormatoDataStd) DATA_FINE_SEMINA,
                               CMN.ID_PRATICA_MANTENIMENTO,CM.FLAG_PRATO_PERMANENTE
                        FROM   SMRGAA.DB_TIPO_UTILIZZO TU,SMRGAA.DB_TIPO_DETTAGLIO_USO DU,SMRGAA.DB_TIPO_VARIETA TV,
                               SMRGAA.DB_TIPO_DESTINAZIONE TD,SMRGAA.DB_TIPO_QUALITA_USO QU,SMRGAA.DB_R_CATALOGO_MATRICE CM,
                               SMRGAA.DB_TIPO_VERSIONE_MATRICE VM,SMRGAA.DB_TIPO_EFA_TIPO_VARIETA EV,SMRGAA.DB_R_CATALOGO_MATRICE_SEMINA CMS,
                               SMRGAA.DB_R_CATALOGO_MANTENIMENTO CMN
                        WHERE  CM.ID_UTILIZZO            = TU.ID_UTILIZZO
                        AND    CM.ID_TIPO_DETTAGLIO_USO  = DU.ID_TIPO_DETTAGLIO_USO
                        AND    CM.ID_VARIETA             = TV.ID_VARIETA
                        AND    TU.CODICE                 = rec.CODICEPRODOTTO
                        AND    ((DU.CODICE_DETTAGLIO_USO = rec.CODICEVARIETA AND TV.CODICE_VARIETA = '000') -- DEFAULT
                                 OR (NOT EXISTS (SELECT 'X'
                                                 FROM    SMRGAA.DB_TIPO_DETTAGLIO_USO DU2,SMRGAA.DB_R_CATALOGO_MATRICE CM2
                                                 WHERE   CM2.ID_UTILIZZO           = CM.ID_UTILIZZO
                                                  AND    CM2.ID_VERSIONE_MATRICE   = CM.ID_VERSIONE_MATRICE
                                                  AND    CM2.ID_TIPO_DETTAGLIO_USO = DU2.ID_TIPO_DETTAGLIO_USO
                                                  AND    DU2.CODICE_DETTAGLIO_USO  = rec.CODICEVARIETA)
                                     AND TV.CODICE_VARIETA       = rec.CODICEVARIETA
                                     AND DU.CODICE_DETTAGLIO_USO = '000')) -- DEFAULT
                        AND    CM.ID_TIPO_DESTINAZIONE   = TD.ID_TIPO_DESTINAZIONE    
                        AND    CM.ID_TIPO_QUALITA_USO    = QU.ID_TIPO_QUALITA_USO
                        AND    QU.CODICE_QUALITA_USO     = '000'   -- DEFAULT --      
                        AND    CM.ID_CATALOGO_MATRICE    = EV.ID_CATALOGO_MATRICE(+)
                        AND    EV.DATA_FINE_VALIDITA(+)  IS NULL
                        AND    CM.ID_CATALOGO_MATRICE    = CMS.ID_CATALOGO_MATRICE(+)
                        AND    CMS.DATA_FINE_VALIDITA(+) IS NULL
                        AND    CMS.FLAG_DEFAULT(+)       = 'S'
                        AND    CM.ID_CATALOGO_MATRICE    = CMN.ID_CATALOGO_MATRICE(+)
                        AND    CMN.DATA_FINE_VALIDITA(+) IS NULL
                        AND    CMN.FLAG_DEFAULT(+)       = 'S' 
                        AND    CM.ID_VERSIONE_MATRICE    = VM.ID_VERSIONE_MATRICE
                        AND    VM.ANNO_VERSIONE          = kAnnoVersMatrix
                        AND    CM.DATA_FINE_VALIDITA     IS NULL
                        ORDER BY CM.ID_UTILIZZO, CM.ID_TIPO_DETTAGLIO_USO,CM.ID_VARIETA,CM.ID_TIPO_DESTINAZIONE,CM.ID_TIPO_QUALITA_USO)
                WHERE  ROWNUM = 1;                                             
            END;     
          WHEN OTHERS THEN
            pCodErrore  := 1;
            pDescErrore := 'eccezione sulla ricerca catalogo matrice. Prodotto = '||rec.CODICEPRODOTTO||', Varieta = '||rec.CODICEVARIETA||
                           ', Data Inizio Utilizzo = '||rec.DATAINIZIOUTILIZZO||', Data Fine Utilizzo = '||rec.DATAFINEUTILIZZO;
            RETURN;
        END;*/
  
        BEGIN
          SELECT ID_SEMINA
          INTO   nIDSemina                            
          FROM   SMRGAA.DB_TIPO_SEMINA TS
          WHERE  TS.CODICE_SEMINA      = rec.CODICETIPOSEMINA
          AND    TS.DATA_FINE_VALIDITA IS NULL;                                                        
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            nIDSemina := NULL;                            
        END;
  
        BEGIN
          SELECT ID_FASE_ALLEVAMENTO
          INTO   nIDFaseAllevam                            
          FROM   SMRGAA.DB_TIPO_FASE_ALLEVAMENTO FA
          WHERE  FA.CODICE_FASE_ALLEVAMENTO = rec.CODICEFASEALLEVAMENTO
          AND    FA.DATA_FINE_VALIDITA      IS NULL;                                                      
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            nIDFaseAllevam := NULL;                            
        END;
  
        nIDPraticaMant := recCatalogoAll.IDPraticaMant;
  
        IF recCatalogoAll.flagPratoPerm = 'S' AND rec.MANTPRATIPERMANENTE IS NOT NULL THEN
          BEGIN
            SELECT ID_PRATICA_MANTENIMENTO
            INTO   nIDPraticaMant                            
            FROM   SMRGAA.DB_TIPO_PRATICA_MANTENIMENTO TP
            WHERE  TP.CODICE_PRATICA_MANTENIMENTO = rec.MANTPRATIPERMANENTE
            AND    TP.DATA_FINE_VALIDITA          IS NULL;                                                      
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              nIDPraticaMant := recCatalogoAll.IDPraticaMant;
          END;
        ELSIF recCatalogoAll.flagPratoPerm = 'N' AND rec.MANTENSUPAGRICOLA IS NOT NULL THEN
          nIDPraticaMant := TO_NUMBER(rec.MANTENSUPAGRICOLA);
        END IF;
  
        IF rec.DATAINIZIOUTILIZZO IS NOT NULL THEN
          recCatalogoAll.dataIniSemina := ReturnData(rec.DATAINIZIOUTILIZZO);
        END IF;
  
        IF rec.DATAFINEUTILIZZO IS NOT NULL THEN
          recCatalogoAll.dataFineSemina := ReturnData(rec.DATAFINEUTILIZZO);
        END IF;
  
        INSERT INTO SMRGAA.DB_UTILIZZO_PARTICELLA 
        (ID_UTILIZZO_PARTICELLA, ID_UTILIZZO, ID_CONDUZIONE_PARTICELLA,SUPERFICIE_UTILIZZATA, 
         DATA_AGGIORNAMENTO, ID_UTENTE_AGGIORNAMENTO,ANNO, NOTE, ID_UTILIZZO_SECONDARIO,SUP_UTILIZZATA_SECONDARIA, ID_VARIETA, 
         ID_VARIETA_SECONDARIA,ANNO_IMPIANTO, ID_IMPIANTO,SESTO_SU_FILE,SESTO_TRA_FILE, 
         NUMERO_PIANTE_CEPPI, ID_TIPO_DETTAGLIO_USO,ID_TIPO_DETT_USO_SECONDARIO, ID_TIPO_EFA, VALORE_ORIGINALE, 
         VALORE_DOPO_CONVERSIONE, VALORE_DOPO_PONDERAZIONE, ID_TIPO_PERIODO_SEMINA,ID_TIPO_PERIODO_SEMINA_SECOND, ID_SEMINA, ID_SEMINA_SECONDARIA, 
         ID_CATALOGO_MATRICE, DATA_INIZIO_DESTINAZIONE, DATA_FINE_DESTINAZIONE,ID_FASE_ALLEVAMENTO, ID_PRATICA_MANTENIMENTO, 
         ID_CATALOGO_MATRICE_SECONDARIO,DATA_INIZIO_DESTINAZIONE_SEC, DATA_FINE_DESTINAZIONE_SEC)  
        VALUES 
        (SMRGAA.SEQ_UTILIZZO_PARTICELLA.NEXTVAL,recCatalogoAll.IDUtilizzo,nIDConduzPart,NVL(TO_NUMBER(rec.SUPERFICIEUTILIZZATA_DET),0) / 10000,
         SYSDATE,kIDUtente,TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')),null,null,null,recCatalogoAll.IDVarieta,
         null,TO_NUMBER(rec.ANNOIMPIANTO),kIDTipoAltriImpianti,TO_NUMBER(rec.SESTOIMPIANTOSUFILA),TO_NUMBER(rec.SESTOIMPIANTOTRAFILA),
         TO_NUMBER(rec.NUMEROPIANTE),recCatalogoAll.IDDettUso,null,recCatalogoAll.IDTipoEfa,null,
         null,null,recCatalogoAll.IDPeriodoSemina,null,nIDSemina,null,
         recCatalogoAll.IDCatalogMatrix,recCatalogoAll.dataIniSemina,recCatalogoAll.dataFineSemina,nIDFaseAllevam,nIDPraticaMant,
         null,null,null);
      EXCEPTION
        WHEN OTHERS THEN
          pCodErrore  := 1;
          pDescErrore := dbms_utility.FORMAT_ERROR_BACKTRACE||' particella non trovata. Codice Istat = '||rec.ISTAT_COMUNE||', Foglio = '||rec.N_FOGLIO||', Particella = '||
                          rec.N_PARTICELLA||', Subalterno = '||NVL(rec.SUBALTERNO,'-')||', Sezione = '||NVL(rec.SEZIONE,'-')||
                          ', Azienda = '||rec.ID_AZIENDA||', Titolo Possesso = '||rec.ID_TITOLO_POSSESSO||', Data Fine Conduzione = '|| 
                          rec.DATAFINECONDUZIONE||', Data Inizio Conduzione = '||rec.DATAINIZIOCONDUZIONE;
          RETURN;    
      END;
    END LOOP;
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nel popolamento dei dati delle colture = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
  END popolaDatiColture;
  
  PROCEDURE popolaContoCorrente(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                                pCodErrore   OUT NUMBER,
                                pDescErrore  OUT VARCHAR2) IS
  
    recCC             SMRGAA.DB_CONTO_CORRENTE%ROWTYPE;
    bIns              BOOLEAN := FALSE;
    idCc              TBL_ID := TBL_ID();
    nIdContoCorrente  SMRGAA.DB_CONTO_CORRENTE.ID_CONTO_CORRENTE%TYPE;
    nIdAzienda        SMRGAA.DB_CONTO_CORRENTE.ID_AZIENDA%TYPE;
  BEGIN
    pCodErrore  := 0;
    pDescErrore := '';
  
    FOR rec IN (SELECT distinct
                       (SELECT ID_SPORTELLO
                        FROM   SMRGAA.DB_TIPO_SPORTELLO TS, SMRGAA.DB_TIPO_BANCA TB
                        WHERE  TS.CAB                = CC.CODICABB
                        AND    TS.DATA_FINE_VALIDITA IS NULL
                        AND    TS.ID_BANCA           = TB.ID_BANCA
                        AND    TB.ABI                = CC.CODIABII
                        AND    TB.DATA_FINE_VALIDITA IS NULL) ID_SPORTELLO,AA.ID_AZIENDA,CC.CODINUMECCOR NUMERO_CONTO_CORRENTE,
                       CC.CODIPIN CIN,AA.DENOMINAZIONE INTESTAZIONE,
                       DECODE(CC.DATAINIZ,kDataFineMax,NULL,ReturnData(CC.DATAINIZ)) DATA_INIZIO_VALIDITA,
                       DECODE(CC.DATAFINE,kDataFineMax,NULL,ReturnData(CC.DATAFINE)) DATA_FINE_VALIDITA,
                       DECODE(CC.DATAFINE,kDataFineMax,NULL,ReturnData(CC.DATAFINE)) DATA_ESTINZIONE,
                       SYSDATE DATA_AGGIORNAMENTO,kIDUtente ID_UTENTE_AGGIORNAMENTO,
                       CC.CODIPIN||CC.CODIABII||CC.CODICABB||CC.CODINUMECCOR BBAN,
                       CC.CHECKDIGIT CIFRA_CONTROLLO,CC.CODINAZI||CC.CHECKDIGIT||CC.CODIPIN||CC.CODIABII||CC.CODICABB||CC.CODINUMECCOR IBAN,
                       'S' FLAG_VALIDATO,CC.CODICABB,CC.CODIABII
                FROM   CONTOCORRENTEWS CC, DETTAGLIOSOGGETTOWS DS, ISWSRESPANAGFASCICOLO15 RAF,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
                WHERE  RAF.ID_CHIAMATA          = pIdChiamata
                AND    AA.CUAA                  = RAF.CUAA
                AND    AA.DATA_FINE_VALIDITA    IS NULL
                AND    CC.ID_DSWS               = DS.ID_DSWS
                AND    DS.ID_CHIAMATA           = RAF.ID_CHIAMATA
                AND    LENGTH(CC.CODINUMECCOR) <= 12) LOOP -- escludo i casi con conto corrente maggiore di 12 (limite campo)
  
      bIns       := FALSE;
      nIdAzienda := rec.ID_AZIENDA;
  
      IF rec.ID_SPORTELLO IS NOT NULL THEN
        BEGIN
          SELECT *
          INTO   recCC
          FROM   SMRGAA.DB_CONTO_CORRENTE
          WHERE  ID_AZIENDA         = rec.ID_AZIENDA
          AND    IBAN               = rec.IBAN
          AND    DATA_FINE_VALIDITA IS NULL;
  
          idCc.EXTEND;
          idCc(idCc.COUNT) := OBJ_ID(recCC.ID_CONTO_CORRENTE);
  
          IF recCC.ID_SPORTELLO                 != rec.ID_SPORTELLO OR
             recCC.NUMERO_CONTO_CORRENTE        != rec.NUMERO_CONTO_CORRENTE OR
             NVL(recCC.CIN,'0')                 != NVL(rec.CIN,'0') OR 
             NVL(recCC.INTESTAZIONE,'0')        != NVL(rec.INTESTAZIONE,'0') OR  
             NVL(recCC.DATA_ESTINZIONE,SYSDATE) != NVL(rec.DATA_ESTINZIONE,SYSDATE) OR
             NVL(recCC.BBAN,'0')                != NVL(rec.BBAN,'0') OR  
             NVL(recCC.CIFRA_CONTROLLO,'0')     != NVL(rec.CIFRA_CONTROLLO,'0') OR
             NVL(recCC.IBAN,'0')                != NVL(rec.IBAN,'0') THEN
  
  --          DBMS_OUTPUT.PUT_LINE('update cc');
  --          DBMS_OUTPUT.PUT_LINE('rec.ID_AZIENDA'||' - '||rec.ID_AZIENDA);
  --          DBMS_OUTPUT.PUT_LINE('rec.IBAN'||' - '||rec.IBAN);
  --          DBMS_OUTPUT.PUT_LINE('rec.DATA_ESTINZIONE'||' - '||rec.DATA_ESTINZIONE);
  --          DBMS_OUTPUT.PUT_LINE('rec.DATA_FINE_VALIDITA'||' - '||rec.DATA_FINE_VALIDITA);
  
            UPDATE SMRGAA.DB_CONTO_CORRENTE
            SET    DATA_AGGIORNAMENTO      = SYSDATE, 
                   DATA_FINE_VALIDITA      = SYSDATE, 
                   ID_UTENTE_AGGIORNAMENTO = rec.ID_UTENTE_AGGIORNAMENTO
            WHERE  ID_AZIENDA              = rec.ID_AZIENDA
            AND    IBAN                    = rec.IBAN
            AND    DATA_FINE_VALIDITA      IS NULL;
  
            bIns := TRUE;
          END IF;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            bIns := TRUE;
        END;
  
        IF bIns THEN
  
  --        DBMS_OUTPUT.PUT_LINE('insert cc');
  --        DBMS_OUTPUT.PUT_LINE('rec.ID_AZIENDA'||' - '||rec.ID_AZIENDA);
  --        DBMS_OUTPUT.PUT_LINE('rec.IBAN'||' - '||rec.IBAN);
  --        DBMS_OUTPUT.PUT_LINE('rec.DATA_ESTINZIONE'||' - '||rec.DATA_ESTINZIONE);
  --        DBMS_OUTPUT.PUT_LINE('rec.DATA_FINE_VALIDITA'||' - '||rec.DATA_FINE_VALIDITA);
  
          INSERT INTO SMRGAA.DB_CONTO_CORRENTE
          (ID_CONTO_CORRENTE,ID_SPORTELLO,ID_AZIENDA,NUMERO_CONTO_CORRENTE,CIN,INTESTAZIONE,
           DATA_INIZIO_VALIDITA,DATA_FINE_VALIDITA,DATA_ESTINZIONE,DATA_AGGIORNAMENTO,ID_UTENTE_AGGIORNAMENTO,BBAN,
           CIFRA_CONTROLLO,IBAN,FLAG_VALIDATO)
          VALUES
          (SMRGAA.SEQ_CONTO_CORRENTE.NEXTVAL,rec.ID_SPORTELLO,rec.ID_AZIENDA,rec.NUMERO_CONTO_CORRENTE,rec.CIN,rec.INTESTAZIONE,
           rec.DATA_INIZIO_VALIDITA,rec.DATA_FINE_VALIDITA,rec.DATA_ESTINZIONE,rec.DATA_AGGIORNAMENTO,rec.ID_UTENTE_AGGIORNAMENTO,rec.BBAN,
           rec.CIFRA_CONTROLLO,rec.IBAN,rec.FLAG_VALIDATO)
          RETURNING ID_CONTO_CORRENTE INTO nIdContoCorrente;
  
          idCc.EXTEND;
          idCc(idCc.COUNT) := OBJ_ID(nIdContoCorrente);
        END IF;
      END IF;
    END LOOP;
  
    -- chiusura DB_CONTO_CORRENTE dei record x l'azienda non trattati 
    UPDATE SMRGAA.DB_CONTO_CORRENTE
    SET    DATA_AGGIORNAMENTO      = SYSDATE, 
           DATA_FINE_VALIDITA      = SYSDATE + 1/86400, -- aggiungo un secondo per evitare la violazione dell'AK
           ID_UTENTE_AGGIORNAMENTO = kIDUtente
    WHERE  ID_AZIENDA              = nIdAzienda
    AND    DATA_FINE_VALIDITA      IS NULL
    AND    ID_CONTO_CORRENTE       NOT IN (SELECT ID
                                           FROM   TABLE(idCc));
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nel popolamento dei conti corrente = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
  END popolaContoCorrente;
  
  PROCEDURE popolaDatiManodopera(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                                 pCodErrore   OUT NUMBER,
                                 pDescErrore  OUT VARCHAR2) IS
  
    nIdManodopera   SMRGAA.DB_MANODOPERA.ID_MANODOPERA%TYPE;
    nUomini         SMRGAA.DB_DETTAGLIO_MANODOPERA.UOMINI%TYPE;
    nDonne          SMRGAA.DB_DETTAGLIO_MANODOPERA.DONNE%TYPE;
    nGiornateAnnue  SMRGAA.DB_DETTAGLIO_MANODOPERA.GIORNATE_ANNUE%TYPE;
    nCont           SIMPLE_INTEGER := 0;
    recMan          SMRGAA.DB_MANODOPERA%ROWTYPE;
    bInsMan         BOOLEAN := FALSE;
    bInsDettMan     BOOLEAN := FALSE;
    idMan           TBL_ID := TBL_ID();
    idClasse        TBL_ID := TBL_ID();
    nPrgSch	        SINC_FA_AABRSCHE_TAB.PRG_SCHEDA@db_sian%TYPE;
    vCuaa             SINC_FA_AABRSCHE_TAB.CUAA@db_sian%TYPE;
  BEGIN
    pCodErrore  := 0;
    pDescErrore := '';
  
  --Devo ricavare il "progressivo scheda" dal parametro di input pIdChiamataid che corrisponde all'id elaborazione
      SELECT sche.prg_scheda, sche.cuaa
      INTO nPrgSch, vCuaa
      FROM SINC_FA_AABRSCHE_TAB@db_sian sche, SINC_DU_ABRAREDU_TAB@db_sian redu
      WHERE redu.id_elab = pIdChiamata
      AND lpad(to_char(redu.sche_fasc), 11, '0') = sche.id_scheda;
  
    FOR rec IN (SELECT AA.ID_AZIENDA,
               desc_VALO_IDES CODICE_INPS, SYSDATE DATA_INIZIO_VALIDITA,
               NULL DATA_FINE_VALIDITA,SYSDATE DATA_AGGIORNAMENTO, 9999999999/*kIDUtente*/ ID_UTENTE_AGGIORNAMENTO,
               (SELECT ID_FORMA_CONDUZIONE 
                FROM   SMRGAA.DB_TIPO_FORMA_CONDUZIONE 
                WHERE  ID_FORMA_CONDUZIONE = DECODE(L.TIPO_COND, 0, 1, 1, 2, 2, 3, 3, 6, 4, 7, NULL)) ID_FORMA_CONDUZIONE,
               NULL MATRICOLA_INAIL,
               DECO_TIPO_ISCR ID_TIPO_ISCRIZIONE_INPS,
               DATA_INIZ_IDES DATA_INIZIO_ISCRIZIONE,
               DATA_FINE_IDES DATA_CESSAZIONE_ISCRIZIONE,
      /*DECODE(TIPOCOLLABORAZIONE||UNITAMISURA,'00' ,6,'01' ,6,'02' ,6,'03' ,6,'61',6,'62',6,'63',6,'10',6,'11',1,'12',2,'13',2,
                                          '14',1,'15',2,'16',2,'20',6,'21',3,'22',4,'23',4,'24',3,'25',4,'26',4,'30',6,
                                          '31',6,'32',6,'33',6,'34',6,'35',6,'36',6)*/NULL ID_CLASSE_MANODOPERA,
                                         NULL TIPOLAVORATORI, 
               /*CASE WHEN TO_NUMBER(REPLACE(QUANTITA,'.',',')) > 365 THEN 365 ELSE TO_NUMBER(REPLACE(QUANTITA,'.',',')) END*/NULL QUANTITA
                FROM   SINC_FA_AABRPART_TAB@db_sian L, SINC_FA_AABRFASC_TAB@db_sian RAF,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,
                SINC_FA_AABRIDES_TAB@db_sian DES
                WHERE  RAF.PRG_SCHEDA          = nPrgSch
                AND    DES.DECO_TIPO_IDES = 33
                AND    AA.CUAA                 = RAF.CUAA
                AND    AA.DATA_FINE_VALIDITA   IS NULL
                AND    L.PRG_SCHEDA            = RAF.PRG_SCHEDA
                AND    DES.PRG_SCHEDA          = RAF.PRG_SCHEDA) LOOP
  
    /*FOR rec IN (SELECT AA.ID_AZIENDA,
                       (SELECT INPS.NUMEROISCRIZIONE 
                        FROM   ISWSCODICEINPS INPS 
                        WHERE  INPS.ID_ISWSRESPANAGFASCICOLO15 = RAF.IDISWSRESPANAGFASCICOLO15) CODICE_INPS,SYSDATE DATA_INIZIO_VALIDITA,
                       NULL DATA_FINE_VALIDITA,SYSDATE DATA_AGGIORNAMENTO,kIDUtente ID_UTENTE_AGGIORNAMENTO,
                       (SELECT ID_FORMA_CONDUZIONE 
                        FROM   SMRGAA.DB_TIPO_FORMA_CONDUZIONE 
                        WHERE  ID_FORMA_CONDUZIONE = DECODE(L.TIPOLAVOROPREVALENTE, 0, 1, 1, 2, 2, 3, 3, 6, 4, 7, NULL)) ID_FORMA_CONDUZIONE,
                       NULL MATRICOLA_INAIL,
                       (SELECT ID_TIPO_ISCRIZIONE_INPS
                        FROM   SMRGAA.DB_TIPO_ISCRIZIONE_INPS TII, ISWSCODICEINPS INPS
                        WHERE  CODICE_TIPO_ISCRIZIONE          = INPS.CODICETIPOISCRIZIONE
                        AND    INPS.ID_ISWSRESPANAGFASCICOLO15 = RAF.IDISWSRESPANAGFASCICOLO15) ID_TIPO_ISCRIZIONE_INPS,
                       (SELECT DECODE(INPS.DATAINIZIO, kDataFineMax, NULL, ReturnData(INPS.DATAINIZIO)) 
                        FROM   ISWSCODICEINPS INPS 
                        WHERE  INPS.ID_ISWSRESPANAGFASCICOLO15 = RAF.IDISWSRESPANAGFASCICOLO15) DATA_INIZIO_ISCRIZIONE,
                       (SELECT DECODE(INPS.DATACESSAZIONE, kDataFineMax, NULL, ReturnData(INPS.DATACESSAZIONE)) 
                        FROM   ISWSCODICEINPS INPS 
                        WHERE  INPS.ID_ISWSRESPANAGFASCICOLO15 = RAF.IDISWSRESPANAGFASCICOLO15) DATA_CESSAZIONE_ISCRIZIONE,
                       DECODE(TIPOCOLLABORAZIONE||UNITAMISURA,'00' ,6,'01' ,6,'02' ,6,'03' ,6,'61',6,'62',6,'63',6,'10',6,'11',1,'12',2,'13',2,
                                                              '14',1,'15',2,'16',2,'20',6,'21',3,'22',4,'23',4,'24',3,'25',4,'26',4,'30',6,
                                                              '31',6,'32',6,'33',6,'34',6,'35',6,'36',6) ID_CLASSE_MANODOPERA,TIPOLAVORATORI, 
                       CASE WHEN TO_NUMBER(REPLACE(QUANTITA,'.',',')) > 365 THEN 365 ELSE TO_NUMBER(REPLACE(QUANTITA,'.',',')) END QUANTITA
                FROM   ISWSLAVORO L,ISWSRESPANAGFASCICOLO15 RAF,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
                WHERE  RAF.ID_CHIAMATA          = pIdChiamata
                AND    AA.CUAA                  = RAF.CUAA
                AND    AA.DATA_FINE_VALIDITA    IS NULL
                AND    L.IDCHIAMATA             = RAF.ID_CHIAMATA) LOOP*/
  
      bInsMan     := FALSE;
      bInsDettMan := FALSE;
  
      BEGIN
        SELECT *
        INTO   recMan
        FROM   SMRGAA.DB_MANODOPERA
        WHERE  ID_AZIENDA         = rec.ID_AZIENDA
        AND    DATA_FINE_VALIDITA IS NULL;
  
        nIdManodopera := recMan.ID_MANODOPERA;
  
        IF NVL(recMan.CODICE_INPS,'0')                    != NVL(rec.CODICE_INPS,'0') OR
           NVL(recMan.ID_FORMA_CONDUZIONE,0)              != NVL(rec.ID_FORMA_CONDUZIONE,0) OR
           NVL(recMan.MATRICOLA_INAIL,'0')                != NVL(rec.MATRICOLA_INAIL,'0') OR
           NVL(recMan.ID_TIPO_ISCRIZIONE_INPS,0)          != NVL(rec.ID_TIPO_ISCRIZIONE_INPS,0) OR
           NVL(recMan.DATA_INIZIO_ISCRIZIONE,SYSDATE)     != NVL(rec.DATA_INIZIO_ISCRIZIONE,SYSDATE) OR
           NVL(recMan.DATA_CESSAZIONE_ISCRIZIONE,SYSDATE) != NVL(rec.DATA_CESSAZIONE_ISCRIZIONE,SYSDATE) THEN
  
          UPDATE SMRGAA.DB_MANODOPERA
          SET    DATA_AGGIORNAMENTO      = SYSDATE, 
                 DATA_FINE_VALIDITA      = SYSDATE, 
                 ID_UTENTE_AGGIORNAMENTO = rec.ID_UTENTE_AGGIORNAMENTO
          WHERE  ID_AZIENDA              = rec.ID_AZIENDA
          AND    DATA_FINE_VALIDITA      IS NULL;
  
          bInsMan     := TRUE;
          bInsDettMan := TRUE;
        END IF;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN  
          bInsMan := TRUE;
          ----insertLog(1108, 'NO_DATA_FOUND', 'bInsMan: TRUE', vCuaa);
      END;
  
      IF bInsMan THEN
        INSERT INTO SMRGAA.DB_MANODOPERA
        (ID_MANODOPERA,ID_AZIENDA,CODICE_INPS,DATA_INIZIO_VALIDITA,DATA_FINE_VALIDITA,DATA_AGGIORNAMENTO,
         ID_UTENTE_AGGIORNAMENTO,ID_FORMA_CONDUZIONE,MATRICOLA_INAIL,ID_TIPO_ISCRIZIONE_INPS,DATA_INIZIO_ISCRIZIONE,
         DATA_CESSAZIONE_ISCRIZIONE)
        VALUES
        (SMRGAA.SEQ_MANODOPERA.NEXTVAL,rec.ID_AZIENDA,rec.CODICE_INPS,rec.DATA_INIZIO_VALIDITA,rec.DATA_FINE_VALIDITA,rec.DATA_AGGIORNAMENTO,
         rec.ID_UTENTE_AGGIORNAMENTO,rec.ID_FORMA_CONDUZIONE,rec.MATRICOLA_INAIL,rec.ID_TIPO_ISCRIZIONE_INPS,rec.DATA_INIZIO_ISCRIZIONE,
         rec.DATA_CESSAZIONE_ISCRIZIONE)
        RETURNING ID_MANODOPERA INTO nIdManodopera;
      END IF;
  
      idMan.EXTEND;
      idMan(idMan.COUNT) := OBJ_ID(nIdManodopera);
  
      idClasse.EXTEND;
      idClasse(idClasse.COUNT) := OBJ_ID(rec.ID_CLASSE_MANODOPERA);
  
      nUomini        := 0;
      nDonne         := 0;
      nGiornateAnnue := 0;
  
      if rec.TIPOLAVORATORI in (0,3,4) then
        nGiornateAnnue := rec.QUANTITA;
      end if;
  
      if rec.TIPOLAVORATORI = 1 then
        nUomini := rec.QUANTITA;
      end if;
  
      if rec.TIPOLAVORATORI = 2 then
        nDonne := rec.QUANTITA;
      end if;
  
      SELECT COUNT(*)
      INTO   nCont
      FROM   SMRGAA.DB_DETTAGLIO_MANODOPERA
      WHERE  ID_MANODOPERA        = nIdManodopera
      AND    ID_CLASSE_MANODOPERA = rec.ID_CLASSE_MANODOPERA;
  
      IF nCont = 0 OR bInsDettMan THEN 
        INSERT INTO SMRGAA.DB_DETTAGLIO_MANODOPERA
        (ID_DETTAGLIO_MANODOPERA, ID_MANODOPERA, ID_CLASSE_MANODOPERA, UOMINI, DONNE,GIORNATE_ANNUE)
        VALUES
        (SMRGAA.SEQ_DETTAGLIO_MANODOPERA.NEXTVAL, nIdManodopera, rec.ID_CLASSE_MANODOPERA, nUomini, nDonne,nGiornateAnnue);
      ELSE
        UPDATE SMRGAA.DB_DETTAGLIO_MANODOPERA
        SET    UOMINI               = NVL(UOMINI,0) + nUomini, 
               DONNE                = NVL(DONNE,0) + nDonne, 
               GIORNATE_ANNUE       = NVL(GIORNATE_ANNUE,0) + nGiornateAnnue
        WHERE  ID_MANODOPERA        = nIdManodopera
        AND    ID_CLASSE_MANODOPERA = rec.ID_CLASSE_MANODOPERA;           
      END IF;
    END LOOP;
  
    -- delete DB_DETTAGLIO_MANODOPERA x l'ID_MANODOPERA di tutti ID_CLASSE_MANODOPERA non trattati
    DELETE SMRGAA.DB_DETTAGLIO_MANODOPERA
    WHERE  ID_MANODOPERA        IN (SELECT ID
                                    FROM   TABLE(idMan))
    AND    ID_CLASSE_MANODOPERA NOT IN (SELECT ID
                                        FROM   TABLE(idClasse)); 
  
    DELETE SMRGAA.DB_DETTAGLIO_MANODOPERA
    WHERE  UOMINI         = 0 
    AND    DONNE          = 0 
    AND    GIORNATE_ANNUE = 0;  
  
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nel popolamento dei dati manodopera = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
      ----insertLog(1108, 'OTHERS', 'Errore generico nel popolamento dei dati manodopera = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE, vCuaa);
  
  END popolaDatiManodopera;
  
  PROCEDURE popolaMotoriAgricoli(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                                 pCodErrore   OUT NUMBER,
                                 pDescErrore  OUT VARCHAR2) IS
  
    nIdMacchina   SMRGAA.DB_MACCHINA.ID_MACCHINA%TYPE;
    bInsMacchina  BOOLEAN DEFAULT TRUE;
    nIdUte        NUMBER;
    nIdMarca      NUMBER;
    recMacc       SMRGAA.DB_MACCHINA%ROWTYPE;
    bInsPosMacc   BOOLEAN := FALSE;
    recPosMacc    SMRGAA.DB_POSSESSO_MACCHINA%ROWTYPE;
    recNumTarga   SMRGAA.DB_NUMERO_TARGA%ROWTYPE;
    idMacc        TBL_ID := TBL_ID();
    nIdAzienda    SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE;
  BEGIN
    pCodErrore  := 0;
    pDescErrore := '';
  
    FOR rec IN (SELECT MARCA DESCRIZIONE, NULL MATRICE, 'S' FLAG_PRINCIPALE
                FROM   ISWSMACCHINA ISWS
                WHERE  ISWS.IDCHIAMATA = pIdChiamata
                AND    MARCA           IS NOT NULL
                AND    NOT EXISTS      (SELECT 'X' 
                                        FROM   SMRGAA.DB_TIPO_MARCA 
                                        WHERE  DESCRIZIONE = ISWS.MARCA)) LOOP
  
      INSERT INTO SMRGAA.DB_TIPO_MARCA
      (ID_MARCA, DESCRIZIONE, MATRICE, FLAG_PRINCIPALE)
      VALUES
      (SMRGAA.SEQ_DB_TIPO_MARCA.NEXTVAL, rec.DESCRIZIONE, rec.MATRICE, rec.FLAG_PRINCIPALE);
    END LOOP;
  
    FOR rec IN (SELECT distinct
                       WRK.ID_GENERE_MACCHINA_SIAP ID_GENERE_MACCHINA,WRK.ID_CATEGORIA ID_CATEGORIA,ISWS.MODELLO TIPO_MACCHINA,
                       ISWS.TELAIO MATRICOLA_TELAIO,AA.ID_AZIENDA,
                       DECODE(ISWS.CARBURANTE, 'B', 1, 'G', 2, 'P', 5, 'N', 3, NULL) ID_ALIMENTAZIONE,
                       DECODE(ISWS.TRAZIONE, 'C', 1, 'DT', 4, 'F', 3, 'M', 7, 'R', 2, 'SC', 5, NULL, 6) ID_TRAZIONE,
                       DECODE(ISWS.TIPOTARGA, 'F', 4, 'S', 3, 'R', 2, 'T', 1, NULL) ID_TARGA,
                       REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(TRIM(REPLACE(ISWS.TARGA, ' ', '')),'-',NULL),'.',NULL),'*',NULL),'/',NULL),'_',
                               NULL) NUMERO_TARGA,
                       SUBSTR((SELECT MAX(provincia_competenza) 
                               FROM   SMRGAA.DB_ANAGRAFICA_AZIENDA AA
                               WHERE  AA.CUAA               = ISWS.CUAA
                               AND    AA.DATA_FINE_VALIDITA IS NULL), 1, 3) ID_PROVINCIA_ANA,'S' FLAG_TARGA_NUOVA,
                       DECODE(ISWS.DATAISCRIZIONE, kDataFineMax, NULL, NULL, NULL, ReturnData(ISWS.DATAISCRIZIONE)) DATA_PRIMA_IMMATRICOLAZIONE,
                       DECODE(ISWS.FORMAPOSSESSO, 'L', 4, 'N', 3, 'P', 1, 'PU', 1, 'U', 3, 'A ', 8, 'M ', 8, NULL, 8) ID_TIPO_FORMA_POSSESSO,
                       100 PERCENTUALE_POSSESSO,'9999' ID_SCARICO,
                       DECODE(ISWS.DataIscrizione, kDataFineMax, SYSDATE, NULL, SYSDATE, ReturnData(ISWS.DATAISCRIZIONE)) DATA_CARICO, -- bot nullable
                       DECODE(ISWS.DataCessazione, kDataFineMax, NULL, NULL, NULL, ReturnData(ISWS.DataCessazione)) DATA_SCARICO,
                       SYSDATE DATA_INIZIO_VALIDITA,kIDUtente EXT_ID_UTENTE_AGGIORNAMENTO,SYSDATE DATA_AGGIORNAMENTO,isws.marca
                FROM   ISWSMACCHINA ISWS, WRK_GENERE_MACCHINA WRK,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,ISWSRESPANAGFASCICOLO15 RAF
                WHERE  AA.CUAA                       = RAF.CUAA
                AND    AA.DATA_FINE_VALIDITA         IS NULL
                AND    RAF.ID_CHIAMATA               = pIdChiamata
                AND    WRK.CODICE_TIPO_MACCHINA_SIAN = ISWS.TIPOMACCHINA
                AND    RAF.ID_CHIAMATA               = ISWS.IDCHIAMATA) LOOP
  
      bInsMacchina := TRUE;
      bInsPosMacc  := FALSE;
      nIdAzienda   := rec.ID_AZIENDA;
  
      IF rec.ID_GENERE_MACCHINA IS NOT NULL THEN
        BEGIN
          SELECT DTM.ID_MARCA
          INTO   nIdMarca
          FROM   SMRGAA.DB_TIPO_MARCA DTM
          WHERE  UPPER(DTM.DESCRIZIONE) = UPPER(rec.MARCA);
        EXCEPTION
          WHEN OTHERS THEN
            SELECT MIN(DTM.ID_MARCA)
            INTO   nIdMarca
            FROM   SMRGAA.DB_TIPO_MARCA DTM
            WHERE  UPPER(DTM.DESCRIZIONE) = UPPER(rec.MARCA)
            AND    DTM.FLAG_PRINCIPALE    = 'S';
        END;
  
        -- CONTROLLARE SE ESISTE IL RECORD A PARITA' ID_GENERE_MACCHINA,TIPO_MACCHINA, ID_CATEGORIA, ID_MARCA,MATRICOLA_TELAIO ED AGGIORNO SE E'
        -- CAMBIATO QUALCOSA
        BEGIN
          SELECT *
          INTO   recMacc
          FROM   SMRGAA.DB_MACCHINA
          WHERE  ID_GENERE_MACCHINA        = rec.ID_GENERE_MACCHINA
          AND    NVL(TIPO_MACCHINA,'0')    = NVL(rec.TIPO_MACCHINA,'0')
          AND    NVL(ID_CATEGORIA,0)       = NVL(rec.ID_CATEGORIA,0) 
          AND    NVL(ID_MARCA,0)           = NVL(nIdMarca,0)
          AND    NVL(MATRICOLA_TELAIO,'0') = NVL(rec.MATRICOLA_TELAIO,'0')
          AND    ROWNUM                    = 1;
  
          nIdMacchina := recMacc.ID_MACCHINA;
  
          IF NVL(recMacc.ID_ALIMENTAZIONE,0) != NVL(rec.ID_ALIMENTAZIONE,0) OR
             NVL(recMacc.ID_TRAZIONE,0)      != NVL(rec.ID_TRAZIONE,0)      THEN
  
            UPDATE SMRGAA.DB_MACCHINA
            SET    ID_ALIMENTAZIONE            = rec.ID_ALIMENTAZIONE,
                   ID_TRAZIONE                 = rec.ID_TRAZIONE,
                   DATA_AGGIORNAMENTO          = rec.DATA_AGGIORNAMENTO,
                   EXT_ID_UTENTE_AGGIORNAMENTO = rec.EXT_ID_UTENTE_AGGIORNAMENTO
            WHERE  ID_MACCHINA                 = nIdMacchina;
          END IF;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN    
            INSERT INTO SMRGAA.DB_MACCHINA
            (ID_MACCHINA, ID_GENERE_MACCHINA, ID_CATEGORIA, TIPO_MACCHINA, MATRICOLA_TELAIO, ID_MARCA, 
             ID_ALIMENTAZIONE, ID_TRAZIONE,EXT_ID_UTENTE_AGGIORNAMENTO, DATA_AGGIORNAMENTO)
            VALUES
            (SMRGAA.SEQ_DB_MACCHINA.NEXTVAL,rec.ID_GENERE_MACCHINA,rec.ID_CATEGORIA,rec.TIPO_MACCHINA,rec.MATRICOLA_TELAIO,nIdMarca,
             rec.ID_ALIMENTAZIONE,rec.ID_TRAZIONE,rec.EXT_ID_UTENTE_AGGIORNAMENTO,rec.DATA_AGGIORNAMENTO)
            RETURNING ID_MACCHINA INTO nIdMacchina;
        END;
  
        BEGIN
          SELECT ID_UTE
          INTO   nIdUte
          FROM   SMRGAA.DB_UTE
          WHERE  ID_AZIENDA         = rec.ID_AZIENDA
          AND    DATA_FINE_ATTIVITA IS NULL;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN  
            nIdUte := NULL;
        END;
  
        IF nIdUte IS NOT NULL THEN
          -- CONTROLLARE SE ESISTE A PARITA' DI ID_UTE,ID_MACCHINA,ID_TIPO_FORMA_POSSESSO,DATA FINE NULL
          -- SE ESISTE CHIUDO ED INSERISCO SE E' CAMBIATO QUALCOSA
          BEGIN
            SELECT *
            INTO   recPosMacc
            FROM   SMRGAA.DB_POSSESSO_MACCHINA
            WHERE  ID_UTE                 = nIdUte
            AND    ID_MACCHINA            = nIdMacchina
            AND    ID_TIPO_FORMA_POSSESSO = rec.ID_TIPO_FORMA_POSSESSO
            AND    DATA_FINE_VALIDITA     IS NULL;
  
            idMacc.EXTEND;
            idMacc(idMacc.COUNT) := OBJ_ID(nIdMacchina);
  
            IF NVL(recPosMacc.PERCENTUALE_POSSESSO,0) != NVL(rec.PERCENTUALE_POSSESSO,0) OR
               NVL(recPosMacc.ID_SCARICO,0)           != NVL(rec.ID_SCARICO,0) OR
               recPosMacc.DATA_CARICO                 != rec.DATA_CARICO OR
               NVL(recPosMacc.DATA_SCARICO,SYSDATE)   != NVL(rec.DATA_SCARICO,SYSDATE) THEN
  
              UPDATE SMRGAA.DB_POSSESSO_MACCHINA
              SET    DATA_FINE_VALIDITA          = SYSDATE, 
                     DATA_AGGIORNAMENTO          = rec.DATA_AGGIORNAMENTO, 
                     EXT_ID_UTENTE_AGGIORNAMENTO = rec.EXT_ID_UTENTE_AGGIORNAMENTO
              WHERE  ID_POSSESSO_MACCHINA        = recPosMacc.ID_POSSESSO_MACCHINA;
  
              bInsPosMacc := TRUE;
            END IF;             
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              bInsPosMacc := TRUE;
          END;
  
          IF bInsPosMacc THEN
            INSERT INTO SMRGAA.DB_POSSESSO_MACCHINA
            (ID_POSSESSO_MACCHINA,ID_UTE,ID_MACCHINA,ID_TIPO_FORMA_POSSESSO,PERCENTUALE_POSSESSO,ID_SCARICO,
             DATA_CARICO,DATA_SCARICO,DATA_INIZIO_VALIDITA,EXT_ID_UTENTE_AGGIORNAMENTO,DATA_AGGIORNAMENTO,FLAG_VALIDA)
            VALUES
            (SMRGAA.SEQ_DB_POSSESSO_MACCHINA.NEXTVAL,nIdUte,nIdMacchina,rec.ID_TIPO_FORMA_POSSESSO,rec.PERCENTUALE_POSSESSO,rec.ID_SCARICO,
             rec.DATA_CARICO,rec.DATA_SCARICO,rec.DATA_INIZIO_VALIDITA,rec.EXT_ID_UTENTE_AGGIORNAMENTO,rec.DATA_AGGIORNAMENTO,'S');
  
            idMacc.EXTEND;
            idMacc(idMacc.COUNT) := OBJ_ID(nIdMacchina); 
          END IF;
        END IF;
      ELSE
        bInsMacchina := FALSE;
      END IF;
  
      IF bInsMacchina THEN
        IF rec.NUMERO_TARGA IS NOT NULL AND LENGTH(rec.NUMERO_TARGA) <= 8 THEN
  
          --DBMS_OUTPUT.PUT_LINE('nIdMacchina - ID_TARGA - NUMERO_TARGA'||' --- '||nIdMacchina||' - '||rec.ID_TARGA||' - '||rec.NUMERO_TARGA);
  
          -- SE ESISTE A PARITA' DI ID_MACCHINA, ID_TARGA, NUMERO_TARGA AGGIORNARE SE E' CAMBIATO QUALCOSA
          BEGIN
            SELECT *
            INTO   recNumTarga
            FROM   SMRGAA.DB_NUMERO_TARGA
            WHERE  ID_MACCHINA  = nIdMacchina  
            AND    ID_TARGA     = rec.ID_TARGA
            AND    NUMERO_TARGA = rec.NUMERO_TARGA;
  
            IF recNumTarga.ID_PROVINCIA                             != rec.ID_PROVINCIA_ANA OR
               NVL(recNumTarga.DATA_PRIMA_IMMATRICOLAZIONE,SYSDATE) != NVL(rec.DATA_PRIMA_IMMATRICOLAZIONE,SYSDATE) THEN
  
              UPDATE SMRGAA.DB_NUMERO_TARGA
              SET    ID_PROVINCIA                = rec.ID_PROVINCIA_ANA,
                     DATA_PRIMA_IMMATRICOLAZIONE = rec.DATA_PRIMA_IMMATRICOLAZIONE,
                     EXT_ID_UTENTE_AGGIORNAMENTO = rec.EXT_ID_UTENTE_AGGIORNAMENTO,
                     DATA_AGGIORNAMENTO          = rec.DATA_AGGIORNAMENTO
              WHERE  ID_NUMERO_TARGA             = recNumTarga.ID_NUMERO_TARGA;
            END IF;
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              INSERT INTO SMRGAA.DB_NUMERO_TARGA
              (ID_NUMERO_TARGA, ID_MACCHINA, ID_TARGA, NUMERO_TARGA, ID_PROVINCIA, FLAG_TARGA_NUOVA, 
               DATA_PRIMA_IMMATRICOLAZIONE, EXT_ID_UTENTE_AGGIORNAMENTO, DATA_AGGIORNAMENTO)
              VALUES
              (SMRGAA.SEQ_DB_NUMERO_TARGA.NEXTVAL,nIdMacchina,rec.ID_TARGA,rec.NUMERO_TARGA,rec.ID_PROVINCIA_ANA,rec.FLAG_TARGA_NUOVA,
               rec.DATA_PRIMA_IMMATRICOLAZIONE,rec.EXT_ID_UTENTE_AGGIORNAMENTO,rec.DATA_AGGIORNAMENTO);
          END;
        ELSE
          bInsMacchina := FALSE;
        END IF;
      END IF;
    END LOOP;
  
    -- mi salvo tutti gli id_macchina dell'azienda e setto la DATA_SCARICO = sysdate - 1  sulla DB_POSSESSO_MACCHINA
    UPDATE SMRGAA.DB_POSSESSO_MACCHINA
    SET    DATA_SCARICO                = SYSDATE - 1, 
           DATA_AGGIORNAMENTO          = SYSDATE, 
           EXT_ID_UTENTE_AGGIORNAMENTO = kIDUtente
    WHERE  ID_UTE                      IN (SELECT ID_UTE
                                           FROM   SMRGAA.DB_UTE
                                           WHERE  ID_AZIENDA = nIdAzienda)
    AND    ID_MACCHINA                 NOT IN (SELECT ID
                                               FROM   TABLE(idMacc));
  
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nel popolamento dei motori agricoli = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
  END popolaMotoriAgricoli;
  
  PROCEDURE popolaFabbricati(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                             pCodErrore   OUT NUMBER,
                             pDescErrore  OUT VARCHAR2) IS
  
    nIdFabbricato            SMRGAA.DB_FABBRICATO.ID_FABBRICATO%TYPE;
    nIdParticella            NUMBER;
    nZonaAlt                 SMRGAA.COMUNE.ZONAALT%TYPE;
    nIdConduzioneParticella  NUMBER;
    nPrgSch	        SINC_FA_AABRSCHE_TAB.PRG_SCHEDA@db_sian%TYPE;
    vCuaa             SINC_FA_AABRSCHE_TAB.CUAA@db_sian%TYPE;
  BEGIN
    pCodErrore  := 0;
    pDescErrore := '';
  
    --Devo ricavare il "progressivo scheda" dal parametro di input pIdChiamataid che corrisponde all'id elaborazione
     SELECT sche.prg_scheda, sche.cuaa
     INTO nPrgSch, vCuaa
     FROM SINC_FA_AABRSCHE_TAB@db_sian sche, SINC_DU_ABRAREDU_TAB@db_sian redu
     WHERE redu.id_elab = pIdChiamata
     AND lpad(to_char(redu.sche_fasc), 11, '0') = sche.id_scheda;
  
  -- CHIUSURA DB_FABBRICATO X TUTTI I RECORD DELLE UTE DELL'AZIENDA 
    UPDATE SMRGAA.DB_FABBRICATO
    SET    DATA_AGGIORNAMENTO      = SYSDATE, 
           ID_UTENTE_AGGIORNAMENTO = kIDUtente, 
           DATA_FINE_VALIDITA      = SYSDATE
    WHERE  ID_UTE                  IN (SELECT U.ID_UTE
                                       FROM   SMRGAA.DB_UTE U,SMRGAA.DB_ANAGRAFICA_AZIENDA AA, SINC_FA_AABRFASC_TAB@db_sian RAF
                                       WHERE  U.ID_AZIENDA          = AA.ID_AZIENDA
                                       AND    AA.CUAA               = RAF.CUAA
                                       AND    AA.DATA_FINE_VALIDITA IS NULL
                                       AND    RAF.PRG_SCHEDA       = nPrgSch);
  
    -- CHIUSURA DB_FABBRICATO X TUTTI I RECORD DELLE UTE DELL'AZIENDA 
    /*UPDATE SMRGAA.DB_FABBRICATO
    SET    DATA_AGGIORNAMENTO      = SYSDATE, 
           ID_UTENTE_AGGIORNAMENTO = kIDUtente, 
           DATA_FINE_VALIDITA      = SYSDATE
    WHERE  ID_UTE                  IN (SELECT U.ID_UTE
                                       FROM   SMRGAA.DB_UTE U,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,ISWSRESPANAGFASCICOLO15 RAF
                                       WHERE  U.ID_AZIENDA          = AA.ID_AZIENDA
                                       AND    AA.CUAA               = RAF.CUAA
                                       AND    AA.DATA_FINE_VALIDITA IS NULL
                                       AND    RAF.ID_CHIAMATA       = pIdChiamata);*/
  
    FOR rec IN (SELECT  DECODE(F.MACROUSO,301,1,303,2,304,3,305,4,307,5,324,5,326,5,328,5,306,6,308,8,310,9,312,10,309,11,313,12,311,13,314,14,322,
                          14,315,15,316,16,317,17,318,18,319,19,320,20,321,21,323,23,325,23,327,23,329,32,302,33,NULL) ID_TIPOLOGIA_FABBRICATO,
                   DECODE(F.MACROUSO,301,NULL,303,NULL,304,NULL,305,NULL,307,NULL,324,1,326,2,328,3,306,NULL,308,NULL,310,NULL,312,NULL,309,NULL,
                          313,NULL,311,NULL,314,NULL,322,NULL,315,NULL,316,NULL,317,NULL,318,NULL,319,NULL,320,NULL,321,NULL,323,1,325,2,
                          327,3,329,NULL,302,NULL,NULL) ID_FORMA_FABBRICATO,NULL DENOMINAZIONE,
                   (PAR.SUPE_COND) SUPERFICIE,NULL ANNO_COSTRUZIONE,0 DIMENSIONE,0 LUNGHEZZA,0 LARGHEZZA,
                   SYSDATE DATA_AGGIORNAMENTO,0 ALTEZZA,NULL UTM_X,NULL UTM_Y,kIDUtente ID_UTENTE_AGGIORNAMENTO,
                   (SELECT MAX(U.ID_UTE) 
                    FROM   SMRGAA.DB_UTE U
                    WHERE  U.ID_AZIENDA         = AA.ID_AZIENDA
                    AND    U.DATA_FINE_ATTIVITA IS NULL) ID_UTE,SYSDATE DATA_INIZIO_VALIDITA,NULL DATA_FINE_VALIDITA,NULL ID_COLTURA_SERRA,
                   NULL MESI_RISCALDAMENTO_SERRA,NULL ORE_RISCALDAMENTO_SERRA,NULL DICHIARAZIONE_RIPRISTINATA,
                   NULL ID_DICHIARAZIONE_CONSISTENZA,null VOLUME_UTILE_PRESUNTO, null  SUPERFICIE_COPERTA,
                  null  SUPERFICIE_SCOPERTA,0 SUPERFICIE_SCOPERTA_EXTRA,F.CODI_ISTA_PROV||F.CODI_ISTA_COMU istatComune,F.CODI_SEZI SEZIONE,
                   TO_NUMBER(F.FOGL_CATA) FOGLIO,TO_NUMBER(F.PART_CATA) PARTICELLA,trim(F.CODI_SUBA) SUBALTERNO,
                   TO_NUMBER(PAR.TIPO_COND) ID_TITOLO_POSSESSO
                  FROM SINC_PSR_SUP_AABRREPA_TAB@db_sian F, SINC_FA_AABRPART_TAB@db_sian PAR, 
                  SINC_FA_AABRFASC_TAB@db_sian RAF, SMRGAA.DB_ANAGRAFICA_AZIENDA AA
                  WHERE  RAF.PRG_SCHEDA       = nPrgSch
                  AND    AA.CUAA               = RAF.CUAA
                  AND    F.CUAA                = RAF.CUAA
                  AND    PAR.PRG_SCHEDA        = RAF.PRG_SCHEDA
                  AND    PAR.FOGL              = F.FOGL_CATA
                  AND    PAR.PART              = F.PART_CATA
                  AND    PAR.SUB               = NVL(F.CODI_SUBA, ' ')
                  AND    AA.DATA_FINE_VALIDITA IS NULL) LOOP
  
    /*FOR rec IN (SELECT DECODE(F.TIPO,301,1,303,2,304,3,305,4,307,5,324,5,326,5,328,5,306,6,308,8,310,9,312,10,309,11,313,12,311,13,314,14,322,
                              14,315,15,316,16,317,17,318,18,319,19,320,20,321,21,323,23,325,23,327,23,329,32,302,33,NULL) ID_TIPOLOGIA_FABBRICATO,
                       DECODE(F.TIPO,301,NULL,303,NULL,304,NULL,305,NULL,307,NULL,324,1,326,2,328,3,306,NULL,308,NULL,310,NULL,312,NULL,309,NULL,
                              313,NULL,311,NULL,314,NULL,322,NULL,315,NULL,316,NULL,317,NULL,318,NULL,319,NULL,320,NULL,321,NULL,323,1,325,2,
                              327,3,329,NULL,302,NULL,NULL) ID_FORMA_FABBRICATO,NULL DENOMINAZIONE,
                       (F.SUPERFICIECOPERTA + F.SUPERFICIESCOPERTA) SUPERFICIE,NULL ANNO_COSTRUZIONE,0 DIMENSIONE,0 LUNGHEZZA,0 LARGHEZZA,
                       SYSDATE DATA_AGGIORNAMENTO,0 ALTEZZA,NULL UTM_X,NULL UTM_Y,kIDUtente ID_UTENTE_AGGIORNAMENTO,
                       (SELECT MAX(U.ID_UTE) 
                        FROM   SMRGAA.DB_UTE U
                        WHERE  U.ID_AZIENDA         = AA.ID_AZIENDA
                        AND    U.DATA_FINE_ATTIVITA IS NULL) ID_UTE,SYSDATE DATA_INIZIO_VALIDITA,NULL DATA_FINE_VALIDITA,NULL ID_COLTURA_SERRA,
                       NULL MESI_RISCALDAMENTO_SERRA,NULL ORE_RISCALDAMENTO_SERRA,NULL DICHIARAZIONE_RIPRISTINATA,
                       NULL ID_DICHIARAZIONE_CONSISTENZA,F.VOLUME VOLUME_UTILE_PRESUNTO,SUPERFICIECOPERTA SUPERFICIE_COPERTA,
                       SUPERFICIESCOPERTA SUPERFICIE_SCOPERTA,0 SUPERFICIE_SCOPERTA_EXTRA,PROVINCIA||COMUNE istatComune,SEZIONE,
                       TO_NUMBER(FOGLIO) FOGLIO,TO_NUMBER(PARTICELLA) PARTICELLA,trim(SUBALTERNO) SUBALTERNO,
                       TO_NUMBER(TIPOCONDUZIONE) ID_TITOLO_POSSESSO
                FROM   ISWSFABBRICATOFS6 F,ISWSRESPANAGFASCICOLO15 RAF,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
                WHERE  AA.CUAA               = RAF.CUAA
                AND    AA.DATA_FINE_VALIDITA IS NULL
                AND    RAF.ID_CHIAMATA       = pIdChiamata
                AND    F.ID_CHIAMATA         = RAF.ID_CHIAMATA) LOOP*/
  
      IF rec.ID_UTE IS NOT NULL THEN
        INSERT INTO SMRGAA.DB_FABBRICATO
        (ID_FABBRICATO,ID_TIPOLOGIA_FABBRICATO,ID_FORMA_FABBRICATO,DENOMINAZIONE,SUPERFICIE,ANNO_COSTRUZIONE,
         DIMENSIONE,LUNGHEZZA,LARGHEZZA,DATA_AGGIORNAMENTO,ALTEZZA,UTM_X,UTM_Y,NOTE,ID_UTENTE_AGGIORNAMENTO,
         ID_UTE,DATA_INIZIO_VALIDITA,DATA_FINE_VALIDITA,ID_COLTURA_SERRA,MESI_RISCALDAMENTO_SERRA,ORE_RISCALDAMENTO_SERRA,
         DICHIARAZIONE_RIPRISTINATA,ID_DICHIARAZIONE_CONSISTENZA,VOLUME_UTILE_PRESUNTO,SUPERFICIE_COPERTA,SUPERFICIE_SCOPERTA,
         SUPERFICIE_SCOPERTA_EXTRA)
        VALUES
        (SMRGAA.SEQ_FABBRICATO.NEXTVAL,rec.ID_TIPOLOGIA_FABBRICATO,rec.ID_FORMA_FABBRICATO,rec.DENOMINAZIONE,rec.SUPERFICIE,rec.ANNO_COSTRUZIONE,
         rec.DIMENSIONE,rec.LUNGHEZZA,rec.LARGHEZZA,rec.DATA_AGGIORNAMENTO,rec.ALTEZZA,rec.UTM_X,rec.UTM_Y,NULL,rec.ID_UTENTE_AGGIORNAMENTO,
         rec.ID_UTE,rec.DATA_INIZIO_VALIDITA,rec.DATA_FINE_VALIDITA,rec.ID_COLTURA_SERRA,rec.MESI_RISCALDAMENTO_SERRA,rec.ORE_RISCALDAMENTO_SERRA,
         rec.DICHIARAZIONE_RIPRISTINATA,rec.ID_DICHIARAZIONE_CONSISTENZA,rec.VOLUME_UTILE_PRESUNTO,rec.SUPERFICIE_COPERTA,rec.SUPERFICIE_SCOPERTA,
         rec.SUPERFICIE_SCOPERTA_EXTRA)
        RETURNING ID_FABBRICATO INTO nIdFabbricato;
  
        BEGIN
          SELECT SP.ID_PARTICELLA
          INTO   nIdParticella                        
          FROM   SMRGAA.DB_STORICO_PARTICELLA SP
          WHERE  SP.COMUNE              = rec.istatComune
          AND    NVL(SP.SEZIONE,'-')    = NVL(rec.SEZIONE,'-')
          AND    SP.FOGLIO              = rec.FOGLIO
          AND    SP.PARTICELLA          = rec.PARTICELLA
          AND    NVL(SP.SUBALTERNO,'-') = NVL(rec.SUBALTERNO,'-')
          AND    SP.DATA_FINE_VALIDITA  IS NULL;
  
          INSERT INTO SMRGAA.DB_FABBRICATO_PARTICELLA
          (ID_FABBRICATO_PARTICELLA, ID_FABBRICATO, ID_PARTICELLA, DATA_INIZIO_VALIDITA)
          VALUES
          (SMRGAA.SEQ_PARTICELLA_FABBRICATO.NEXTVAL,nIdFabbricato,nIdParticella,rec.DATA_INIZIO_VALIDITA);
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            INSERT INTO SMRGAA.DB_PARTICELLA 
            (ID_PARTICELLA, DATA_CREAZIONE, DATA_CESSAZIONE,BIOLOGICO, DATA_INIZIO_VALIDITA, FLAG_SCHEDARIO,FLAG_INVIO_SITI) 
            VALUES 
            (SMRGAA.SEQ_PARTICELLA.NEXTVAL,SYSDATE,null,'N',rec.DATA_INIZIO_VALIDITA,'N','N')
            RETURNING ID_PARTICELLA INTO nIdParticella;
  
          ----insertLog(1108, 'NO_DATA_FOUND', 'popolaFabbricati - nIdParticella:'|| nIdParticella, vCuaa);
  
            SELECT ZONAALT
            INTO   nZonaAlt
            FROM   SMRGAA.COMUNE C
            WHERE  C.ISTAT_COMUNE = rec.istatComune
            AND FLAG_ESTINTO = 'N';
  
            INSERT INTO SMRGAA.DB_STORICO_PARTICELLA 
            (ID_PARTICELLA, ID_STORICO_PARTICELLA, SEZIONE,COMUNE, DATA_INIZIO_VALIDITA, FOGLIO,DATA_FINE_VALIDITA, 
             PARTICELLA, SUP_CATASTALE,SUBALTERNO, ID_AREA_A, ID_ZONA_ALTIMETRICA,FLAG_IRRIGABILE, ID_AREA_B, ID_CASO_PARTICOLARE,
             ID_AREA_C, ID_AREA_D, DATA_AGGIORNAMENTO, ID_UTENTE_AGGIORNAMENTO, ID_CAUSALE_MOD_PARTICELLA, FLAG_CAPTAZIONE_POZZI,MOTIVO_MODIFICA,
             SUP_NON_ELEGGIBILE,SUP_NE_BOSCO_ACQUE_FABBRICATO,SUP_NE_FORAGGIERE, SUP_EL_FRUTTA_GUSCIO, SUP_EL_PRATO_PASCOLO,SUP_EL_COLTURE_MISTE,
             SUP_COLTIVAZ_ARBOREA_CONS, SUP_COLTIVAZ_ARBOREA_SPEC,DATA_FOTO, ID_FONTE, TIPO_FOTO,ID_DOCUMENTO, ID_IRRIGAZIONE, 
             ID_DOCUMENTO_PROTOCOLLATO,ID_FASCIA_FLUVIALE, ID_AREA_G, ID_AREA_H,SUPERFICIE_GRAFICA, ID_POTENZIALITA_IRRIGUA, ID_TERRAZZAMENTO, 
             ID_ROTAZIONE_COLTURALE, ID_AREA_L, ID_AREA_I,ID_AREA_M, PERCENTUALE_PENDENZA_MEDIA, GRADI_PENDENZA_MEDIA,GRADI_ESPOSIZIONE_MEDIA, 
             METRI_ALTITUDINE_MEDIA, ID_METODO_IRRIGUO) 
            VALUES 
            (nIdParticella,SMRGAA.SEQ_STORICO_PARTICELLA.NEXTVAL,rec.SEZIONE,rec.istatComune,rec.DATA_INIZIO_VALIDITA,rec.FOGLIO,null,
             rec.PARTICELLA,(NVL(rec.SUPERFICIE_COPERTA,0)+NVL(rec.SUPERFICIE_SCOPERTA,0)) / 10000,rec.SUBALTERNO,null,nZonaAlt,'N',null,3,
             null,null,SYSDATE,kIDUtente,null,'N',null,
             null,null,null,null,null,null,null,
             null,null,null,null,
             null,null,null,null,null,null,0,
             3,1,4,null,null,null,null,
             null,null,NULL,null); 
  
            INSERT INTO SMRGAA.DB_CONDUZIONE_PARTICELLA 
            (ID_CONDUZIONE_PARTICELLA, ID_PARTICELLA, ID_TITOLO_POSSESSO,ID_UTE, 
             SUPERFICIE_CONDOTTA, FLAG_UTILIZZO_PARTE,DATA_INIZIO_CONDUZIONE,DATA_FINE_CONDUZIONE, NOTE,DATA_AGGIORNAMENTO, 
             ID_UTENTE_AGGIORNAMENTO, ESITO_CONTROLLO,DATA_ESECUZIONE, RECORD_MODIFICATO,DICHIARAZIONE_RIPRISTINATA,ID_DICHIARAZIONE_CONSISTENZA,
             SUPERFICIE_AGRONOMICA, PERCENTUALE_POSSESSO) 
            VALUES 
            (SMRGAA.SEQ_CONDUZIONE_PARTICELLA.NEXTVAL,nIdParticella,rec.ID_TITOLO_POSSESSO,rec.ID_UTE,
             (NVL(rec.SUPERFICIE_COPERTA,0)+NVL(rec.SUPERFICIE_SCOPERTA,0)) / 10000,null,rec.DATA_INIZIO_VALIDITA,NULL,null,SYSDATE,
             kIDUtente,null,null,null,null,null,
             null,100)
            RETURNING ID_CONDUZIONE_PARTICELLA INTO nIdConduzioneParticella; 
  
            ----insertLog(1108, 'NO_DATA_FOUND', 'popolaFabbricati - nIdConduzioneParticella:'|| nIdConduzioneParticella, vCuaa);
  
            INSERT INTO SMRGAA.DB_UTILIZZO_PARTICELLA 
            (ID_UTILIZZO_PARTICELLA, ID_UTILIZZO, ID_CONDUZIONE_PARTICELLA,
             SUPERFICIE_UTILIZZATA, DATA_AGGIORNAMENTO, ID_UTENTE_AGGIORNAMENTO,ANNO, NOTE, 
             ID_UTILIZZO_SECONDARIO,SUP_UTILIZZATA_SECONDARIA, ID_VARIETA, ID_VARIETA_SECONDARIA,ANNO_IMPIANTO, ID_IMPIANTO, 
             SESTO_SU_FILE,SESTO_TRA_FILE, NUMERO_PIANTE_CEPPI, ID_TIPO_DETTAGLIO_USO,ID_TIPO_DETT_USO_SECONDARIO, ID_TIPO_EFA, VALORE_ORIGINALE, 
             VALORE_DOPO_CONVERSIONE, VALORE_DOPO_PONDERAZIONE, ID_TIPO_PERIODO_SEMINA,ID_TIPO_PERIODO_SEMINA_SECOND, ID_SEMINA, 
             ID_SEMINA_SECONDARIA,ID_CATALOGO_MATRICE, DATA_INIZIO_DESTINAZIONE, DATA_FINE_DESTINAZIONE,ID_FASE_ALLEVAMENTO, 
             ID_PRATICA_MANTENIMENTO, ID_CATALOGO_MATRICE_SECONDARIO,DATA_INIZIO_DESTINAZIONE_SEC, DATA_FINE_DESTINAZIONE_SEC)  
            VALUES 
            (SMRGAA.SEQ_UTILIZZO_PARTICELLA.NEXTVAL,198,nIdConduzioneParticella,
             (NVL(rec.SUPERFICIE_COPERTA,0)+NVL(rec.SUPERFICIE_SCOPERTA,0)) / 10000,SYSDATE,kIDUtente,TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')),null,
             null,null,2512,null,null,null,
             null,null,null,7612,null,null,null,
             null,null,null,null,null,null,19162,null,null,null,
             null,null,null,null);   
  
            INSERT INTO SMRGAA.DB_FABBRICATO_PARTICELLA
            (ID_FABBRICATO_PARTICELLA, ID_FABBRICATO, ID_PARTICELLA, DATA_INIZIO_VALIDITA)
            VALUES
            (SMRGAA.SEQ_PARTICELLA_FABBRICATO.NEXTVAL,nIdFabbricato,nIdParticella,rec.DATA_INIZIO_VALIDITA);      
        END;
      END IF;
    END LOOP;
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nel popolamento dei fabbricati = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
      ----insertLog(1108, 'OTHERS', 'Errore generico nel popolamento dei fabbricati = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE, vCuaa);
  END popolaFabbricati;
  
  PROCEDURE popolaUma(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                      pCodErrore   OUT NUMBER,
                      pDescErrore  OUT VARCHAR2) IS
  
    nIdDittaUma     SMRUMA.DB_DITTA_UMA.ID_DITTA_UMA%TYPE;
    nIdMarca        SMRUMA.DB_TIPO_MARCA.ID_MARCA%TYPE;
    nIdMacchina     SMRUMA.DB_MACCHINA.ID_MACCHINA%TYPE;
    nIdNumeroTarga  SMRUMA.DB_NUMERO_TARGA.ID_NUMERO_TARGA%TYPE;
    nIdUtilizzo     SMRUMA.DB_UTILIZZO.ID_UTILIZZO%TYPE;
  BEGIN
    pCodErrore  := 0;
    pDescErrore := '';
  
    FOR rec IN (SELECT A.*,ROWNUM NUM_REC
                FROM   (SELECT DISTINCT WRK.ID_GENERE_MACCHINA_SIAP ID_GENERE_MACCHINA,WRK.ID_CATEGORIA ID_CATEGORIA,
                               ISWS.MODELLO TIPO_MACCHINA,ISWS.TELAIO MATRICOLA_TELAIO,AA.ID_AZIENDA,SEDELEG_COMUNE,
                               WRK.CODICE_TIPO_MACCHINA_SIAN,
                               DECODE(ISWS.CARBURANTE, 'B', 1, 'G', 2, 'P', 5, 'N', 3, NULL) ID_ALIMENTAZIONE,
                               DECODE(ISWS.TRAZIONE, 'C', 1, 'DT', 4, 'F', 3, 'M', 6, 'R', 2, 'SC', 5, NULL, 6) ID_TRAZIONE,
                               DECODE(ISWS.TIPOTARGA, 'F', 4, 'S', 3, 'R', 2, 'T', 1, NULL) ID_TARGA,
                               REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(TRIM(REPLACE(ISWS.TARGA, ' ', '')),'-',NULL),'.',NULL),'*',NULL),'/',NULL),'_',
                                       NULL) NUMERO_TARGA,
                               SUBSTR((SELECT MAX(provincia_competenza) 
                                       FROM   SMRGAA.DB_ANAGRAFICA_AZIENDA AA
                                       WHERE  AA.CUAA               = ISWS.CUAA
                                       AND    AA.DATA_FINE_VALIDITA IS NULL), 1, 3) ID_PROVINCIA_ANA,'S' FLAG_TARGA_NUOVA,
                               DECODE(ISWS.DATAISCRIZIONE, kDataFineMax, NULL, NULL, NULL, ReturnData(ISWS.DATAISCRIZIONE)) DATA_PRIMA_IMMATRICOLAZIONE,
                               DECODE(ISWS.FORMAPOSSESSO, 'L', 4, 'N', 3, 'P', 1, 'PU', 1, 'U', 3, 'A ', 8, 'M ', 8, NULL, 8) ID_TIPO_FORMA_POSSESSO,
                               100 PERCENTUALE_POSSESSO,'9999' ID_SCARICO,
                               DECODE(ISWS.DataIscrizione, kDataFineMax, SYSDATE, NULL, SYSDATE, ReturnData(ISWS.DATAISCRIZIONE)) DATA_CARICO, -- bot nullable
                               DECODE(ISWS.DataCessazione, kDataFineMax, NULL, NULL, NULL, ReturnData(ISWS.DataCessazione)) DATA_SCARICO,
                               isws.marca
                        FROM   ISWSMACCHINA ISWS, WRK_GENERE_MACCHINA WRK,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,ISWSRESPANAGFASCICOLO15 RAF
                        WHERE  AA.CUAA                       = RAF.CUAA
                        AND    AA.DATA_FINE_VALIDITA         IS NULL
                        AND    RAF.ID_CHIAMATA               = pIdChiamata
                        AND    WRK.CODICE_TIPO_MACCHINA_SIAN = ISWS.TIPOMACCHINA
                        AND    RAF.ID_CHIAMATA               = ISWS.IDCHIAMATA) A) LOOP
  
      IF rec.ID_GENERE_MACCHINA IS NULL THEN
        pCodErrore  := 1;
        pDescErrore := 'Errore nel reperimento del genere macchina, Tipo Macchina = '||rec.CODICE_TIPO_MACCHINA_SIAN;
        RETURN;
      END IF;
  
      BEGIN
        SELECT DU.ID_DITTA_UMA
        INTO   nIdDittaUma
        FROM   SMRUMA.DB_DITTA_UMA DU
        WHERE  DU.EXT_ID_AZIENDA  = rec.ID_AZIENDA
        AND    DU.DATA_CESSAZIONE IS NULL;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          INSERT INTO SMRUMA.DB_DITTA_UMA
          (ID_DITTA_UMA, EXT_ID_AZIENDA, EXT_PROVINCIA_UMA, TIPO_DITTA, 
           DITTA_UMA,DATA_ISCRIZIONE, DATA_CESSAZIONE, EXT_ID_UTENTE_AGGIORNAMENTO,DATA_AGGIORNAMENTO, ID_DITTA_UMA_PROV, EXT_CUAA_AZIENDA_DEST, 
           RIMANENZA_CEDUTA)
          VALUES
          (SMRUMA.SEQ_DITTA_UMA.NEXTVAL,rec.ID_AZIENDA,SUBSTR(rec.SEDELEG_COMUNE,1,3),'U',
           (SELECT NVL(MAX(DITTA_UMA),0) + 1
            FROM   SMRUMA.DB_DITTA_UMA
            WHERE  EXT_PROVINCIA_UMA = SUBSTR(rec.SEDELEG_COMUNE,1,3)),SYSDATE,NULL,kIDUtente,SYSDATE,NULL,NULL,
           'N')
          RETURNING ID_DITTA_UMA INTO nIdDittaUma;
  
          INSERT INTO SMRUMA.DB_DATI_DITTA
          (ID_DATI_DITTA, ID_DITTA_UMA, ID_CONDUZIONE, EXT_COMUNE_PRINCIPALE_ATTIVITA, INDIRIZZO_CONSEGNA_CARBURANTE, NOTE, 
           DATA_INIZIO_VALIDITA,DATA_FINE_VALIDITA, EXT_ID_UTENTE_AGGIORNAMENTO, MODIFICA_INTERMEDIARIO, DATA_AGGIORNAMENTO, 
           DATA_RICEZ_DOCUM_ASSEGNAZ)
          VALUES
          (SMRUMA.SEQ_DATI_DITTA.NEXTVAL,nIdDittaUma,1,rec.SEDELEG_COMUNE,NULL,'ditta uma creata in fase di sincronizzazione del fascicolo',
           SYSDATE,NULL,kIDUtente,NULL,SYSDATE,
           NULL);
      END;
  
      IF rec.NUM_REC = 1 THEN
        UPDATE SMRUMA.DB_UTILIZZO
        SET    DATA_SCARICO = NVL(DATA_CARICO,SYSDATE-1),
               ID_SCARICO   = 0 -- sincronizzazione sian
        WHERE  ID_DITTA_UMA = nIdDittaUma
        AND    DATA_SCARICO IS NULL;
      END IF;
  
      BEGIN
        SELECT ID_MARCA
        INTO   nIdMarca
        FROM   SMRUMA.DB_TIPO_MARCA
        WHERE  DESCRIZIONE = rec.MARCA
        AND    ROWNUM      = 1;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          INSERT INTO SMRUMA.DB_TIPO_MARCA
          (ID_MARCA, DESCRIZIONE, MATRICE)
          VALUES
          ((SELECT MAX(ID_MARCA) + 1 FROM SMRUMA.DB_TIPO_MARCA),rec.MARCA,NULL)
          RETURNING ID_MARCA INTO nIdMarca;
      END;
  
      INSERT INTO SMRUMA.DB_MACCHINA
      (ID_MACCHINA, ID_MATRICE, MATRICOLA_TELAIO, MATRICOLA_MOTORE, DATA_AGGIORNAMENTO, EXT_ID_UTENTE_AGGIORNAMENTO, EXT_ID_MACCHINA_GAA, 
       ANNO_COSTRUZIONE, NOTE)
      VALUES
      (SMRUMA.SEQ_MACCHINA.NEXTVAL,NULL,rec.MATRICOLA_TELAIO,NULL,SYSDATE,kIDUtente,NULL,
       NULL,NULL)
      RETURNING ID_MACCHINA INTO nIdMacchina;
  
      INSERT INTO SMRUMA.DB_DATI_MACCHINA
      (ID_DATI_MACCHINA, ID_MACCHINA, MARCA, ID_GENERE_MACCHINA, TIPO_MACCHINA, ID_CATEGORIA, TARA, LORDO, NUMERO_ASSI, CALORIE, 
       EXT_ID_UTENTE_AGGIORNAMENTO, POTENZA, DATA_AGGIORNAMENTO, ID_ALIMENTAZIONE, ID_NAZIONALITA, ID_TRAZIONE, QUANTITA)
      VALUES
      (SMRUMA.SEQ_DATI_MACCHINA.NEXTVAL,nIdMacchina,rec.MARCA,rec.ID_GENERE_MACCHINA,rec.TIPO_MACCHINA,NVL(rec.ID_CATEGORIA,2),NULL,NULL,NULL,NULL,
       kIDUtente,NULL,SYSDATE,rec.ID_ALIMENTAZIONE,NULL,rec.ID_TRAZIONE,NULL);
  
      IF rec.NUMERO_TARGA IS NOT NULL AND LENGTH(rec.NUMERO_TARGA) <= 8 THEN
        INSERT INTO SMRUMA.DB_NUMERO_TARGA
        (ID_NUMERO_TARGA, ID_MACCHINA, ID_TARGA, NUMERO_TARGA, ID_PROVINCIA,FLAG_TARGA_NUOVA, EXT_ID_UTENTE_AGGIORNAMENTO, MC_824, 
         DATA_AGGIORNAMENTO, DATA_PRIMA_IMMATRICOLAZIONE, CERT_X_TARGA_TELAIO, CERT_X_TARGA, CERT_X_TELAIO)
        VALUES
        (SMRUMA.SEQ_NUMERO_TARGA.NEXTVAL,nIdMacchina,rec.ID_TARGA,rec.NUMERO_TARGA,SUBSTR(rec.SEDELEG_COMUNE,1,3),NULL,kIDUtente,NULL,
         SYSDATE,NULL,NULL,NULL,NULL)
        RETURNING ID_NUMERO_TARGA INTO nIdNumeroTarga;
  
        INSERT INTO SMRUMA.DB_MOVIMENTI_TARGA
        (ID_MOVIMENTI_TARGA, ID_NUMERO_TARGA, ID_MACCHINA, ID_MOVIMENTAZIONE, ID_PROVINCIA, DITTA_UMA, PROTOCOLLO_MODELLO_49, NUMERO_MODELLO_49,
         DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, DATA_PROTOCOLLO_49, DATA_AGGIORNAMENTO, EXT_ID_UTENTE_AGGIORNAMENTO, ANNO_MODELLO_49)
        VALUES
        (SMRUMA.SEQ_MOVIMENTI_TARGA.NEXTVAL,nIdNumeroTarga,nIdMacchina,11,NULL,NULL,NULL,NULL,
         SYSDATE,NULL,NULL,SYSDATE,kIDUtente,NULL);
      END IF;
  
      INSERT INTO SMRUMA.DB_UTILIZZO
      (ID_UTILIZZO, ID_DITTA_UMA, ID_MACCHINA, DATA_CARICO, DATA_SCARICO, ID_SCARICO, EXT_ID_UTENTE_AGGIORNAMENTO, DATA_AGGIORNAMENTO)
      VALUES
      (SMRUMA.SEQ_UTILIZZO.NEXTVAL,nIdDittaUma,nIdMacchina,SYSDATE,NULL,NULL,kIDUtente,SYSDATE)
      RETURNING ID_UTILIZZO INTO nIdUtilizzo;
  
      INSERT INTO SMRUMA.DB_POSSESSO
      (ID_POSSESSO, ID_UTILIZZO, ID_FORMA_POSSESSO, DATA_SCADENZA_LEASING, EXT_ID_AZIENDA, DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, 
       EXT_ID_UTENTE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, PERCENTUALE_POSSESSO)
      VALUES
      (SMRUMA.SEQ_POSSESSO.NEXTVAL,nIdUtilizzo,rec.ID_TIPO_FORMA_POSSESSO,NULL,rec.ID_AZIENDA,SYSDATE,NULL,
       kIDUtente,SYSDATE,NULL);
    END LOOP;
  
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nel popolamento di uma = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
  END popolaUma;
  
  PROCEDURE popolaAllevamenti(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                              pCodErrore   OUT NUMBER,
                              pDescErrore  OUT VARCHAR2) IS
  
    nIdUte                     SMRGAA.DB_UTE.ID_UTE%TYPE;
    nIdTipoProduzione          SMRGAA.DB_ALLEVAMENTO.ID_TIPO_PRODUZIONE%TYPE;
    nIdOrientamentoProduttivo  SMRGAA.DB_ALLEVAMENTO.ID_ORIENTAMENTO_PRODUTTIVO%TYPE;
    nIdAllevamento             SMRGAA.DB_ALLEVAMENTO.ID_ALLEVAMENTO%TYPE;
    nQuantita                  SMRGAA.DB_CATEGORIE_ALLEVAMENTO.QUANTITA%TYPE;
    nIdCategorieAllevamento    SMRGAA.DB_CATEGORIE_ALLEVAMENTO.ID_CATEGORIE_ALLEVAMENTO%TYPE;
    nPrgSch	        SINC_FA_AABRSCHE_TAB.PRG_SCHEDA@db_sian%TYPE;
    vCuaa             SINC_FA_AABRSCHE_TAB.CUAA@db_sian%TYPE;
  BEGIN
    pCodErrore  := 0;
    pDescErrore := '';
  
    --Devo ricavare il "progressivo scheda" dal parametro di input pIdChiamataid che corrisponde all'id elaborazione
     SELECT sche.prg_scheda, sche.cuaa
     INTO nPrgSch, vCuaa
     FROM SINC_FA_AABRSCHE_TAB@db_sian sche, SINC_DU_ABRAREDU_TAB@db_sian redu
     WHERE redu.id_elab = pIdChiamata
     AND lpad(to_char(redu.sche_fasc), 11, '0') = sche.id_scheda;
  
    /*SELECT MAX(U.ID_UTE)
    INTO   nIdUte 
    FROM   SMRGAA.DB_UTE U,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,ISWSRESPANAGFASCICOLO15 RAF
    WHERE  U.ID_AZIENDA          = AA.ID_AZIENDA
    AND    U.DATA_FINE_ATTIVITA  IS NULL
    AND    AA.CUAA               = RAF.CUAA
    AND    AA.DATA_FINE_VALIDITA IS NULL
    AND    RAF.ID_CHIAMATA       = pIdChiamata;*/
  
    SELECT MAX(U.ID_UTE)
    INTO   nIdUte 
    FROM   SMRGAA.DB_UTE U, SMRGAA.DB_ANAGRAFICA_AZIENDA AA, SINC_FA_AABRFASC_TAB@db_sian RAF
    WHERE  U.ID_AZIENDA          = AA.ID_AZIENDA
    AND    U.DATA_FINE_ATTIVITA  IS NULL
    AND    AA.CUAA               = RAF.CUAA
    AND    AA.DATA_FINE_VALIDITA IS NULL
    AND    RAF.PRG_SCHEDA       = nPrgSch;
  
    -- chiusura preventiva di tutti gli allevamenti dell¿azienda
    UPDATE SMRGAA.DB_ALLEVAMENTO
    SET    DATA_AGGIORNAMENTO      = SYSDATE, 
           ID_UTENTE_AGGIORNAMENTO = kIDUtente, 
           DATA_FINE               = SYSDATE - 1
    WHERE  ID_UTE                  IN (SELECT U.ID_UTE
                                       FROM   SMRGAA.DB_UTE U,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,SINC_FA_AABRFASC_TAB@db_sian RAF
                                       WHERE  U.ID_AZIENDA          = AA.ID_AZIENDA
                                       AND    AA.CUAA               = RAF.CUAA
                                       AND    AA.DATA_FINE_VALIDITA IS NULL
                                       AND    RAF.PRG_SCHEDA       = nPrgSch);
  
    FOR rec IN (SELECT SA.PROVINCIA||SA.COMUNE ISTAT_COMUNE,A.CODICEAZIENDA,ReturnData(A.DATAINIZIATTI) DATA_INIZIO,SA.INDIRIZZO,SA.CAP,
                       (SELECT SASS.ID_SPECIE_ANIMALE
                        FROM   SMRGAA.DB_R_SPECIE_AN_SIAN_SIAP SASS
                        WHERE  SASS.CODICE_SPECIE         = A.CODICESPECIE
                        AND    SASS.FLAG_RELAZIONE_ATTIVA = 'S') ID_SPECIE_ANIMALE,A.CODFISCALEPROP,A.DENOMPROP,DA.CODFISCALE,
                        DECODE(DA.FLAGSOCC,'0','N','S') FLAGSOCC,A.CODICESPECIE,A.TIPOPROD,ca.IDISWSCAPOALLEVAMENTO2
                FROM   ISWSAllevamento15 A,IsWsStallaAllevamento SA,iswscapoallevamento2 CA,IsWsPropallevamentodeteallev DA
                WHERE  A.IDCHIAMATA = pIdChiamata
                AND    SA.ID_ISWSALLEVAMENTO15 = A.IDISWSALLEVAMENTO15
                AND    CA.ID_ISWSALLEVAMENTO15 = A.IDISWSALLEVAMENTO15
                AND    A.IDISWSALLEVAMENTO15   = DA.ID_ISWSALLEVAMENTO15(+)) LOOP
  
      --DBMS_OUTPUT.PUT_LINE('rec.CODICESPECIE - ' || rec.CODICESPECIE);
      --DBMS_OUTPUT.PUT_LINE('rec.TIPOPROD - ' || rec.TIPOPROD);
  
      begin
         SELECT SSOP.ID_TIPO_PRODUZIONE, TOP.ID_ORIENTAMENTO_PRODUTTIVO
         INTO   nIdTipoProduzione,nIdOrientamentoProduttivo
         FROM   SMRGAA.DB_SIAN_SPECIE_ORIENTAM_PROD SSOP,SMRGAA.DB_TIPO_ORIENTAMENTO_PRODUT TOP
         WHERE  SSOP.ID_ORIENTAMENTO_PRODUTTIVO = TOP.ID_ORIENTAMENTO_PRODUTTIVO
         AND    SSOP.DATA_FINE_VALIDITA         IS NULL
         AND    SSOP.CODICE_SPECIE              = rec.CODICESPECIE
         AND    TOP.CODICE_SIAN                 = rec.TIPOPROD;
      EXCEPTION
      WHEN NO_DATA_FOUND THEN
         -- cerco un default
         SELECT SSOP.ID_TIPO_PRODUZIONE, TOP.ID_ORIENTAMENTO_PRODUTTIVO
         INTO   nIdTipoProduzione,nIdOrientamentoProduttivo
         FROM   SMRGAA.DB_SIAN_SPECIE_ORIENTAM_PROD SSOP,SMRGAA.DB_TIPO_ORIENTAMENTO_PRODUT TOP
         WHERE  SSOP.ID_ORIENTAMENTO_PRODUTTIVO = TOP.ID_ORIENTAMENTO_PRODUTTIVO
         AND    SSOP.DATA_FINE_VALIDITA         IS NULL
         AND    SSOP.CODICE_SPECIE              = rec.CODICESPECIE
         AND    SSOP.ID_ORIENTAMENTO_PRODUTTIVO = (SELECT min(SSOP2.ID_ORIENTAMENTO_PRODUTTIVO)
                                                   FROM   SMRGAA.DB_SIAN_SPECIE_ORIENTAM_PROD SSOP2
                                                   WHERE  SSOP2.DATA_FINE_VALIDITA IS NULL
                                                   AND    SSOP2.CODICE_SPECIE      = rec.CODICESPECIE);
      END;
  
      INSERT INTO SMRGAA.DB_ALLEVAMENTO
      (ID_ALLEVAMENTO, ID_UTE, ID_ASL, ISTAT_COMUNE, ID_SPECIE_ANIMALE, CODICE_AZIENDA_ZOOTECNICA, DATA_INIZIO, DATA_FINE, NOTE, 
       DATA_AGGIORNAMENTO, ID_UTENTE_AGGIORNAMENTO, INDIRIZZO, CAP, TELEFONO, DICHIARAZIONE_RIPRISTINATA, ID_DICHIARAZIONE_CONSISTENZA, 
       CODICE_FISCALE_PROPRIETARIO, DENOMINAZIONE_PROPRIETARIO, CODICE_FISCALE_DETENTORE, DENOMINAZIONE_DETENTORE, DATA_INIZIO_DETENZIONE, 
       DATA_FINE_DETENZIONE, FLAG_SOCCIDA, ID_TIPO_PRODUZIONE, ID_ORIENTAMENTO_PRODUTTIVO, DESCRIZIONE_ALTRI_TRATTAM, FLAG_DEIEZIONE_AVICOLI, 
       MEDIA_CAPI_LATTAZIONE, QUANTITA_ACQUA_LAVAGGIO, FLAG_ACQUE_EFFLUENTI, ID_MUNGITURA, SUPERFICIE_LETTIERA_PERMANENTE, 
       ALTEZZA_LETTIERA_PERMANENTE, DENOMINAZIONE_ALLEVAMENTO, LATITUDINE, LONGITUDINE, DATA_APERTURA_ALLEVAMENTO, MOTIVO_SOCCIDA, 
       DATA_ESECUZIONE_CONTROLLI, ID_TIPO_PRODUZIONE_COSMAN, FLAG_ASSICURATO_COSMAN)
      VALUES
      (SMRGAA.SEQ_ALLEVAMENTO.NEXTVAL,nIdUte,NULL,rec.ISTAT_COMUNE,rec.ID_SPECIE_ANIMALE,rec.CODICEAZIENDA,rec.DATA_INIZIO,NULL,NULL,
       SYSDATE,kIDUtente,rec.INDIRIZZO,rec.CAP,NULL,NULL,NULL,
       rec.CODFISCALEPROP,rec.DENOMPROP,rec.CODFISCALE,NULL,rec.DATA_INIZIO,
       NULL,rec.FLAGSOCC,nIdTipoProduzione,nIdOrientamentoProduttivo,NULL,NULL,
       NULL,NULL,NULL,NULL,NULL,
       NULL,NULL,NULL,NULL,NULL,NULL,
       NULL,NULL,NULL)
      RETURNING ID_ALLEVAMENTO INTO nIdAllevamento;
  
      FOR recCatAn IN (SELECT CAAS.*,C.COLUMN_NAME
                       FROM   SMRGAA.DB_R_CAT_ANIMALE_ALLEV_SIAN CAAS,SMRGAA.DB_TIPO_ALLEVAMENTO_SIAN TAS,
                              SMRGAA.DB_R_ALLEV_CAMPO_AGENT_SIAN ACAS,COLS C
                       WHERE  TAS.ID_TIPO_ALLEVAMENTO_SIAN   = CAAS.ID_TIPO_ALLEVAMENTO_SIAN
                       AND    CAAS.ID_CAT_ANIMALE_ALLEV_SIAN = ACAS.ID_CAT_ANIMALE_ALLEV_SIAN
                       AND    TAS.CODICE_ALLEVAMENTO_SIAN    = rec.CODICESPECIE
                       AND    C.TABLE_NAME                   = 'ISWSCAPOALLEVAMENTO2'
                       AND    TAS.DATA_FINE_VALIDITA         IS NULL
                       AND    CAAS.DATA_FINE_VALIDITA        IS NULL
                       AND    ACAS.NOME_CAMPO_AGENT_SIAN     = C.COLUMN_NAME
                       AND    ACAS.DATA_FINE_VALIDITA        IS NULL) LOOP
  
        EXECUTE IMMEDIATE('SELECT '||recCatAn.COLUMN_NAME||' FROM ISWSCAPOALLEVAMENTO2 WHERE IDISWSCAPOALLEVAMENTO2 = '||
                          rec.IDISWSCAPOALLEVAMENTO2) INTO nQuantita;
  
        INSERT INTO SMRGAA.DB_CATEGORIE_ALLEVAMENTO
        (ID_CATEGORIE_ALLEVAMENTO, ID_CATEGORIA_ANIMALE, ID_ALLEVAMENTO, QUANTITA, PESO_VIVO_UNITARIO, QUANTITA_PROPRIETA)
        VALUES
        (SMRGAA.SEQ_CATEGORIE_ALLEVAMENTO.NEXTVAL,recCatAn.ID_CATEGORIA_ANIMALE,nIdAllevamento,nQuantita,NULL,NULL)
        RETURNING ID_CATEGORIE_ALLEVAMENTO INTO nIdCategorieAllevamento;
  
        INSERT INTO SMRGAA.DB_SOTTOCATEGORIA_ALLEVAMENTO
        (ID_SOTTOCATEGORIA_ALLEVAMENTO, ID_SOTTOCATEGORIA_ANIMALE, ID_CATEGORIE_ALLEVAMENTO, ORE_PASCOLO_INVERNO, QUANTITA, PESO_VIVO, 
         GIORNI_VUOTO_SANITARIO, GIORNI_PASCOLO_ESTATE, ORE_PASCOLO_ESTATE, GIORNI_PASCOLO_INVERNO, CICLI, NUMERO_CICLI_ANNUALI, 
         QUANTITA_PROPRIETA)
        VALUES
        (SMRGAA.SEQ_SOTTOCATEGORIA_ALLEVAMENTO.NEXTVAL,recCatAn.ID_SOTTOCATEGORIA_ANIMALE,nIdCategorieAllevamento,NULL,nQuantita,NULL,
         NULL,NULL,NULL,NULL,NULL,NULL,
         NULL);
      END LOOP;
  
    END LOOP;
  
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nel popolamento degli allevamenti = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
  END popolaAllevamenti;
  
  PROCEDURE popolaNuovoFascicolo(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                                 pIdElab          NUMBER,
                                 pIdAzienda   OUT NUMBER,
                                 pCodErrore   OUT NUMBER,
                                 pDescErrore  OUT VARCHAR2) IS
  
    vNomeIstanza       VARCHAR2(30);
    vCodREA            ISWSISCRIZIONECC_REA.NUMEROISCRIZIONE%TYPE;
    vDataInizioREA     ISWSISCRIZIONECC_REA.DATAINIZIO%TYPE;
    vDataFineREA       ISWSISCRIZIONECC_REA.DATACESSAZIONE%TYPE;
    nNumCodREA         SMRGAA.DB_ANAGRAFICA_AZIENDA.CCIAA_NUMERO_REA%TYPE;
    vPVREA             SMRGAA.DB_ANAGRAFICA_AZIENDA.CCIAA_PROVINCIA_REA%TYPE;
    nIdAzienda         SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE;
    nIdFormaGiuridica  SMRGAA.DB_TIPO_FORMA_GIURIDICA.ID_FORMA_GIURIDICA%TYPE;
    vPVCompetenza      SMRGAA.DB_ANAGRAFICA_AZIENDA.PROVINCIA_COMPETENZA%TYPE;
    dDataCessazione    SMRGAA.DB_ANAGRAFICA_AZIENDA.DATA_CESSAZIONE%TYPE;
    nCont              SIMPLE_INTEGER := 0;
    vCodRI		         SINC_FA_AABRIDES_TAB.DESC_VALO_IDES@db_sian%TYPE;
    vDataFineRI	       SINC_FA_AABRIDES_TAB.DATA_FINE_IDES@db_sian%TYPE;
    vDataInizioRI      SINC_FA_AABRIDES_TAB.DATA_INIZ_IDES@db_sian%TYPE;
    vDenominazione     SMRGAA.DB_ANAGRAFICA_AZIENDA.DENOMINAZIONE%TYPE;
    vCuaa              SINC_FA_AABRFASC_TAB.CUAA@db_sian%TYPE;
    nIdAnagrafica      NUMBER;
    vIndir             VARCHAR2(4000);
    vDenom             VARCHAR2(4000);
    vProv              VARCHAR2(3);
    vComune            VARCHAR2(3);
    
    --pf 24Lug22 - gestione errore schede non trovate su SINC_FA_AABRFASC_TAB
    nContFasc          PLS_INTEGER := 0;
    FASC_TAB_NOREC     EXCEPTION;
   
  BEGIN
    
    vNomeIstanza := UPPER(SYS_CONTEXT('USERENV','INSTANCE_NAME'));
    
    --insertLog(pIdChiamata, NULL, NULL, 'popolaNuovoFascicolo - Inizio elaborazione'||CHR(13), SYSDATE, NULL, NULL , NULL, NULL);
    --startLog(pIdChiamata, 'popolaNuovoFascicolo - Inizio elaborazione', pIdElab);
    SELECT COUNT(*)
    INTO nContFasc
    FROM SINC_FA_AABRFASC_TAB@db_sian FF 
    WHERE FF.PRG_SCHEDA = pIdChiamata;
    
    IF nContFasc = 0 THEN
      RAISE FASC_TAB_NOREC;
    END IF;
    
    FOR rec IN (SELECT DISTINCT FA.DATA_INIZ_VALI DATAAPERTURAFASCICOLO,
                       CASE 
                         WHEN FA.DATA_FINE_VALI != kDataFineMax 
                          THEN FA.DATA_FINE_VALI 
                       ELSE NULL 
                       END DATACHIUSURAFASCICOLO,
                       FA.CUAA,
                      (SELECT DESC_VALO_IDES 
                       FROM SINC_FA_AABRIDES_TAB@db_sian 
                       WHERE PRG_SCHEDA     = FA.PRG_SCHEDA
                       AND   DECO_TIPO_IDES = 31
                       AND   DATA_INIZ_IDES = (SELECT MAX(DATA_INIZ_IDES) 
                                               FROM SINC_FA_AABRIDES_TAB@db_sian
                                               WHERE PRG_SCHEDA     = FA.PRG_SCHEDA
                                               AND   DECO_TIPO_IDES = 31)) CODIPARTIVAA,
                       --NVL(SO.DESC_COGN, NULL)||SO.DESC_RAGI_SOCI DENOMINAZIONE,
                       DECO.DESC_DECO DESCNATUGIUR,
                       --SO.CODI_ISTA_PROV PROVINCIA,
                      (SELECT DESC_VALO_IDES
                       FROM SINC_FA_AABRIDES_TAB@db_sian
                       WHERE PRG_SCHEDA     = FA.PRG_SCHEDA
                       AND   DECO_TIPO_IDES = 35
                       AND   DATA_INIZ_IDES = (SELECT MAX(DATA_INIZ_IDES) 
                                               FROM SINC_FA_AABRIDES_TAB@db_sian
                                               WHERE PRG_SCHEDA     = FA.PRG_SCHEDA
                                               AND   DECO_TIPO_IDES = 35)) CODICAMECIAAREAA,
                      (SELECT DESC_MAIL_ACNT 
                       FROM SINC_FA_AABRMAIL_TAB@db_sian
                       WHERE FLAG_MAIL_CERT != 1
                       AND   FA.PRG_SCHEDA   = PRG_SCHEDA
                       AND   DATA_INIZ_MAIL  = (SELECT MAX(DATA_INIZ_MAIL)
                                                FROM SINC_FA_AABRMAIL_TAB@db_sian
                                                WHERE FLAG_MAIL_CERT != 1
                                                AND   FA.PRG_SCHEDA   = PRG_SCHEDA)) EMAIL,
                       --SUBSTR(FA.CODI_ENTE, 9) COMUNE,
                       --SUBSTR(FA.CODI_ENTE, 6, 3) PROVINCIA,
                       RECA.CODI_GEOG_BELF,
                       --RECA.DESC_GEOG_STRD INDIRIZZO,
                      (SELECT DESC_VALO_IDES
                       FROM SINC_FA_AABRIDES_TAB@db_sian
                       WHERE PRG_SCHEDA     = FA.PRG_SCHEDA
                       AND   DECO_TIPO_IDES = 152
                       AND   DATA_INIZ_IDES = (SELECT MAX(DATA_INIZ_IDES) 
                                               FROM SINC_FA_AABRIDES_TAB@db_sian
                                               WHERE PRG_SCHEDA     = FA.PRG_SCHEDA
                                               AND   DECO_TIPO_IDES = 152)) CODICESTATOESTERO,
                       RECA.CODI_GEOG_CAPP CAP,
                      (SELECT DESC_MAIL_ACNT 
                       FROM SINC_FA_AABRMAIL_TAB@db_sian
                       WHERE FLAG_MAIL_CERT = 1
                       AND   FA.PRG_SCHEDA  = PRG_SCHEDA
                       AND   DATA_INIZ_MAIL = (SELECT MAX(DATA_INIZ_MAIL)
                                               FROM SINC_FA_AABRMAIL_TAB@db_sian
                                               WHERE FLAG_MAIL_CERT = 1
                                               AND   FA.PRG_SCHEDA  = PRG_SCHEDA)
                       AND ROWNUM = 1) DESCPEC
                FROM SINC_FA_AABRFASC_TAB@db_sian FA, SINC_FA_AABRSOGG_TAB@db_sian SO,AABRDECO_TAB@db_sian DECO, 
                     /*SINC_FA_AABRMAIL_TAB@db_sian MA,*/ SINC_FA_AABRRECA_TAB@db_sian RECA
                     /*,SINC_FA_AABRMAIL_TAB@db_sian MAILP*/
                WHERE FA.PRG_SCHEDA    	   = pIdChiamata
                AND   RECA.DECO_TIPO_RECA  in (208, 56)
                AND   DECO.ID_DECO         IN (2511, 2517, 2516, 2499)
                /*AND   SO.CODI_ISTA_PROV    IS NOT NULL
                AND   SO.CODI_ISTA_COMU    IS NOT NULL*/
                AND   SO.FLAG_PERS_FISI    = 1
                AND   FA.PRG_SCHEDA        = RECA.PRG_SCHEDA
                AND   FA.PRG_SCHEDA        = SO.PRG_SCHEDA
                AND   SO.DECO_FORM_GIUR    = DECO.ID_DECO
                /*AND   (FA.PRG_SCHEDA       = MA.PRG_SCHEDA(+) AND MA.FLAG_MAIL_CERT(+) != 1)
                AND   (FA.PRG_SCHEDA       = MAILP.PRG_SCHEDA(+) AND MAILP.FLAG_MAIL_CERT(+) = 1)*/) LOOP
    
      BEGIN
  
        --RICAVO PROVINCIA E COMUNE
        SELECT SUBSTR(ISTAT_COMUNE,1, 3), SUBSTR(ISTAT_COMUNE, 4)
        INTO vProv, vComune
        FROM SMRGAA.COMUNE
        WHERE CODFISC = rec.CODI_GEOG_BELF
        AND FLAG_ESTINTO = 'N';
        
        --SELECT PER RICAVARE RAGIONE SOCIALE 
        SELECT RAG_SOC
        INTO vDenom
        FROM (SELECT RAG_SOC, FLA
              FROM (SELECT DESC_COGN||' '||DESC_NOME RAG_SOC, FLAG_PERS_FISI FLA
                    FROM SINC_FA_AABRSOGG_TAB@db_sian
                    WHERE PRG_SCHEDA      = pIdChiamata
                    AND   FLAG_PERS_FISI  = 1
                    UNION 
                    SELECT DESC_RAGI_SOCI RAG_SOC, FLAG_PERS_FISI FLA
                    FROM SINC_FA_AABRSOGG_TAB@db_sian
                    WHERE PRG_SCHEDA      = pIdChiamata
                    AND   FLAG_PERS_FISI  = 0)
              ORDER BY FLA)
        WHERE ROWNUM = 1;
      
        vCuaa := REC.CUAA;
  
        SELECT NUMEROISCRIZIONE,DATA_INIZIO,DATA_CESSAZIONE
        INTO   vCodREA,vDataInizioREA,vDataFineREA
        FROM   (SELECT I.DESC_VALO_IDES NUMEROISCRIZIONE,I.DATA_INIZ_IDES DATA_INIZIO,I.DATA_FINE_IDES DATA_CESSAZIONE                 
                FROM   SINC_FA_AABRFASC_TAB@db_sian RAF, SINC_FA_AABRIDES_TAB@db_sian I
                WHERE  RAF.PRG_SCHEDA   = pIdChiamata
                AND    RAF.PRG_SCHEDA   = I.PRG_SCHEDA
                AND    RAF.CUAA         = rec.CUAA  
                AND    DECO_TIPO_IDES   = 35
                AND    I.DESC_VALO_IDES IS NOT NULL
                ORDER BY I.DATA_INIZ_IDES DESC)
        WHERE   ROWNUM = 1;
  
      EXCEPTION
        WHEN NO_DATA_FOUND THEN  
          vCodREA        := rec.CODICAMECIAAREAA;
          vDataInizioREA := NULL;
          vDataFineREA   := NULL;
          --insertLog(pIdChiamata, NULL, NULL, 'popolaNuovoFascicolo - NO_DATA_FOUND: vDataInizioREA = NULL'||CHR(13), NULL, NULL, NULL , NULL, vCuaa);
          insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaNuovoFascicolo - NO_DATA_FOUND: vDataInizioREA = NULL');
      END;
  
      IF vCodREA IS NOT NULL AND LENGTH(TRIM(TRANSLATE(REPLACE(vCodREA,' '),'(.|/-1234567890',' '))) BETWEEN 2 AND 4 THEN
        vPVREA := UPPER(SUBSTR(TRIM(TRANSLATE(REPLACE(vCodREA,' '),'(.|/-1234567890',' ')),1,2));
      END IF;
  
      IF vCodREA IS NOT NULL AND LENGTH(TRIM(TRANSLATE(REPLACE(vCodREA,' '),'(.|/-abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ',' '))) > 0 THEN
        vCodREA := TRIM(TRANSLATE(REPLACE(vCodREA,' '),'(.|/-abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ',' '));     
      END IF;
  
      vCodREA := TRIM(REPLACE(vCodREA,'    '));
  
      BEGIN
        nNumCodREA := TO_NUMBER(vCodREA);
      EXCEPTION
        WHEN OTHERS THEN
          nNumCodREA := NULL;
          --DBMS_OUTPUT.PUT_LINE('nNumCodREA: NULL'); 
       --insertLog(pIdChiamata, NULL, NULL, 'popolaNuovoFascicolo - NO_DATA_FOUND: nNumCodREA = NULL'||CHR(13), NULL, NULL, NULL , NULL, vCuaa);
        insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaNuovoFascicolo - NO_DATA_FOUND: nNumCodREA = NULL');
      END;
  
      INSERT INTO SMRGAA.DB_AZIENDA
        (ID_AZIENDA, DATA_INIZIO_VALIDITA, FLAG_BONIFICA_DATI,VARIAZIONE_UTILIZZI_AMMESSA, FLAG_OBBLIGO_FASCICOLO, ID_AZIENDA_PROVENIENZA, 
         FLAG_AZIENDA_PROVVISORIA, DATA_INSEDIAMENTO, FLAG_ESENTE_DELEGA,FLAG_COMPETENZA_ESCLUSIVA_PA, DATA_CONTROLLO, ID_OPR,
         DATA_APERTURA_FASCICOLO, DATA_CHIUSURA_FASCICOLO, DATA_AGGIORNAMENTO_OPR,ID_UTENTE_AGGIORNAMENTO_OPR, DATA_AGGIORNAMENTO_PRATICA, 
         ID_TIPO_FORMA_ASSOCIATA,FASCICOLO_DEMATERIALIZZATO, DATA_CONTROLLI_ALLEVAMENTI) 
      VALUES 
        (SMRGAA.SEQ_AZIENDA.NEXTVAL,SYSDATE,NULL,NULL,'S',NULL,
         NULL, NULL, 'N', 'N', NULL, kIDOPR, rec.DATAAPERTURAFASCICOLO, rec.DATACHIUSURAFASCICOLO,
         NULL, NULL, SYSDATE, NULL, 'N', NULL)
      RETURNING ID_AZIENDA INTO nIdAzienda;
  
      pIdAzienda := nIdAzienda;
  
      --insertLog(pIdChiamata, NULL, NULL, 'popolaNuovoFascicolo - nNumCodREA: nNumCodREA = '||nIdAzienda||CHR(13), NULL, NULL, NULL , NULL, vCuaa);
      insertLog(pIdChiamata, pIdElab, NULL, 'popolaNuovoFascicolo - nIdAzienda = '||nIdAzienda);
  
      SELECT FG.ID_FORMA_GIURIDICA
      INTO   nIdFormaGiuridica        
      FROM   SMRGAA.DB_TIPO_FORMA_GIURIDICA FG
      WHERE  FG.DESCRIZIONE = rec.DESCNATUGIUR;
  
      -- se Pv di Basilicata tengo provincia della sede legale, se no fisso POTENZA
      vPVCompetenza := vProv;
  
      SELECT COUNT(*) 
      INTO   nCont                            
      FROM   SMRGAA.PROVINCIA P 
      WHERE  P.ISTAT_PROVINCIA = vProv 
      AND    P.ID_REGIONE      = kIDRegione;
  
      IF nCont = 0 THEN
        vPVCompetenza := kIstatPotenza;
      END IF;
  
      dDataCessazione := rec.DATACHIUSURAFASCICOLO;
      --vDenominazione  := rec.DENOMINAZIONE;
      vDenominazione  := vDenom;
  
      BEGIN
        SELECT NUMEROISCRIZIONE,DATA_INIZIO,DATA_CESSAZIONE
        INTO   vCodRI,vDataInizioRI,vDataFineRI
        FROM   (SELECT I.DESC_VALO_IDES NUMEROISCRIZIONE,I.DATA_INIZ_IDES DATA_INIZIO, I.DATA_FINE_IDES DATA_CESSAZIONE              
                FROM   SINC_FA_AABRFASC_TAB@db_sian RAF, SINC_FA_AABRIDES_TAB@db_sian I
                WHERE  RAF.PRG_SCHEDA   = pIdChiamata
                AND    RAF.PRG_SCHEDA   = I.PRG_SCHEDA
                AND    DECO_TIPO_IDES   = 32
                AND    RAF.CUAA         = rec.CUAA  
                AND    I.DESC_VALO_IDES IS NOT NULL
                ORDER BY I.DATA_INIZ_IDES DESC)
        WHERE  ROWNUM = 1;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN  
          --insertLog(pIdChiamata, NULL, NULL, 'popolaNuovoFascicolo - NO_DATA_FOUND: NUMEROISCRIZIONE'||CHR(13), NULL, NULL, NULL , NULL, vCuaa);
          insertLog(pIdChiamata, pIdElab, 'WARNING', 'popolaNuovoFascicolo - NO_DATA_FOUND: NUMEROISCRIZIONE');
      END;          
              
      SELECT RECA.DESC_GEOG_STRD INDIRIZZO
      INTO vIndir
      FROM SINC_FA_AABRFASC_TAB@db_sian FA, SINC_FA_AABRRECA_TAB@db_sian RECA
      WHERE FA.PRG_SCHEDA    	   in (pIdChiamata)
      AND   RECA.DECO_TIPO_RECA  in (208, 56)
      AND   FA.PRG_SCHEDA        = RECA.PRG_SCHEDA
      AND   ROWNUM               = 1;  
        
      INSERT INTO SMRGAA.DB_ANAGRAFICA_AZIENDA 
          (ID_ANAGRAFICA_AZIENDA, ID_AZIENDA, ID_TIPOLOGIA_AZIENDA,DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, CUAA,PARTITA_IVA, DENOMINAZIONE, 
           ID_FORMA_GIURIDICA,ID_ATTIVITA_ATECO, PROVINCIA_COMPETENZA, CCIAA_PROVINCIA_REA,CCIAA_NUMERO_REA, MAIL, SEDELEG_COMUNE,
           SEDELEG_INDIRIZZO, SITOWEB, SEDELEG_CITTA_ESTERO,SEDELEG_CAP, DATA_CESSAZIONE, CAUSALE_CESSAZIONE,NOTE, DATA_AGGIORNAMENTO, 
           ID_UTENTE_AGGIORNAMENTO,ID_ATTIVITA_OTE, MOTIVO_MODIFICA, CCIAA_NUMERO_REGISTRO_IMPRESE,CCIAA_ANNO_ISCRIZIONE, MODIFICA_INTERMEDIARIO, 
           NUMERO_AGEA,INTESTAZIONE_PARTITA_IVA, ID_CESSAZIONE, ID_DIMENSIONE_AZIENDA,ID_UDE, RLS, ULU,CODICE_AGRITURISMO, TELEFONO, FAX,PEC, 
           ESONERO_PAGAMENTO_GF, DATA_AGGIORNAMENTO_UMA,FLAG_IAP, DATA_ISCRIZIONE_REA, DATA_CESSAZIONE_REA,DATA_ISCRIZIONE_RI, 
           DATA_CESSAZIONE_RI, DATA_INIZIO_ATECO) 
      VALUES (SMRGAA.SEQ_ANAGRAFICA_AZIENDA.NEXTVAL, nIdAzienda, 1, SYSDATE, NULL, rec.CUAA, rec.CODIPARTIVAA,
              vDenominazione, nIdFormaGiuridica, NULL, vPVCompetenza, vPVREA, nNumCodREA/*SUBSTR(TO_CHAR(nNumCodREA),1,9)*/, rec.EMAIL,
              vProv||vComune, vIndir /*rec.INDIRIZZO*/, null, rec.CODICESTATOESTERO, rec.CAP, dDataCessazione,
              null,null, sysdate, kIDUtente, null, null, SUBSTR(vCodRI,1,21), TO_CHAR(vDataInizioRI, 'YYYY')/*TO_NUMBER(SUBSTR(vDataInizioRI,1,4))*/,
              null, null, null, null, null, null, null, null, null, null, null, rec.DESCPEC,null, null, 'N', vDataInizioREA, vDataFineREA, vDataInizioRI, vDataFineRI, null)
      RETURNING ID_ANAGRAFICA_AZIENDA INTO nIdAnagrafica;
            
    END LOOP;
    
    --insertLog(pIdChiamata, 1, 'POSITIVO', 'popolaNuovoFascicolo - Fine elaborazione. nIdAnagrafica: '||nIdAnagrafica||CHR(13), NULL, SYSDATE, NULL , NULL, vCuaa);  
    endLog(pIdChiamata, pIdElab, 1, 'POSITIVO', 'popolaNuovoFascicolo - Fine elaborazione. nIdAnagrafica: '||nIdAnagrafica);
    
  EXCEPTION
    WHEN FASC_TAB_NOREC THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore procedura popolaNuovoFascicolo: nessun record trovato su SINC_FA_AABRFASC_TAB@db_sian '||
                      'con PRG_SCHEDA = '||pIdChiamata||'; impossibile inserire la posizione anagrafica.';
      endLog(pIdChiamata, pIdElab, 2, 'NEGATIVO',pDescErrore);
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico procedura popolaNuovoFascicolo = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
      --insertLog(pIdChiamata, 2, 'NEGATIVO', pDescErrore, NULL, SYSDATE, pCodErrore , pDescErrore, vCuaa);
      endLog(pIdChiamata, pIdElab, 2, 'NEGATIVO', 'popolaNuovoFascicolo - Fine elaborazione. Errore generico procedura popolaNuovoFascicolo = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
  END popolaNuovoFascicolo;
  
  PROCEDURE popolaMandato(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                          pIdElab      NUMBER,
                          pCodErrore   OUT NUMBER,
                          pDescErrore  OUT VARCHAR2) IS
  
      nIdIntermediario          SMRGAA.DB_UFFICIO_ZONA_INTERMEDIARIO.id_intermediario%TYPE;
      nIdUfficioZona            SMRGAA.DB_UFFICIO_ZONA_INTERMEDIARIO.id_ufficio_zona_intermediario%TYPE;
      nIdAzienda                SMRGAA.DB_ANAGRAFICA_AZIENDA.id_azienda%TYPE; 
      --vDetentore                ISWSRESPANAGFASCICOLO15.detentore%TYPE;
      vDetentore                SINC_FA_AABRFASC_TAB.codi_ente@db_sian%TYPE;
  
      nIdDelega                 SMRGAA.DB_DELEGA.id_delega%TYPE; 
  
  BEGIN
      pCodErrore  := 0;
      pDescErrore := '';
  
  insertLog(pIdChiamata, pIdElab, null, 'popolaMandato - Inizio elaborazione');
  
  
      SELECT aa.id_azienda, raf.CODI_ENTE
      INTO   nIdAzienda, vDetentore
      FROM   SMRGAA.DB_ANAGRAFICA_AZIENDA AA, SINC_FA_AABRFASC_TAB@DB_SIAN RAF
      WHERE  AA.CUAA               = RAF.CUAA
      AND    AA.DATA_FINE_VALIDITA IS NULL
      AND    RAF.PRG_SCHEDA      = pIdChiamata;
  
      vDetentore := SUBSTR(vDetentore, 1, 3)||SUBSTR(vDetentore, 6);
  
      begin
          select caa.id_intermediario, caa.id_ufficio_zona_intermediario
          into nIdIntermediario, nIdUfficioZona
          from smrgaa.db_ufficio_zona_intermediario caa 
          where caa.codice_agea = vDetentore;
      exception when no_data_found then
          --pCodErrore  := 1;
           pCodErrore  := 1;
          --pDescErrore := 'Errore inserimento delega: CAA non trovato = '|| vDetentore;
          insertLog(pIdChiamata, pIdElab, 'NEGATIVO', 'popolaMandato - Errore inserimento delega: CAA non trovato = '|| vDetentore);
  
          --return;
      end;
  
      begin
          select id_delega
          into nIdDelega
          from SMRGAA.DB_DELEGA man
          where man.id_azienda = nIdAzienda
          and   man.id_intermediario = nIdIntermediario
          and   man.id_ufficio_zona_intermediario = nIdUfficioZona
          and   man.data_fine is null
          and   man.data_fine_mandato is null;
      exception when no_data_found then
          update SMRGAA.DB_DELEGA man
          set man.data_fine = trunc(sysdate),
              man.data_fine_mandato = trunc(sysdate),
              man.id_utente_fine_delega = kIDUtente
          where man.id_azienda = nIdAzienda
          and   man.data_fine is null;
  
          insert into smrgaa.db_delega
              (ID_DELEGA,
              ID_INTERMEDIARIO,
              ID_PROCEDIMENTO,
              ID_AZIENDA,
              DATA_INIZIO,
              DATA_FINE,
              ID_UTENTE_INSERIMENTO_DELEGA,
              ID_UFFICIO_ZONA_INTERMEDIARIO,
              UFFICIO_FASCICOLO,
              ID_UTENTE_FINE_DELEGA,
              INDIRIZZO,
              COMUNE,
              CAP,
              CODICE_AMMINISTRAZIONE,
              RECAPITO,
              DATA_INIZIO_MANDATO,
              DATA_FINE_MANDATO,
              DATA_AGGIORNAMENTO_SIAN,
              DATA_RICEVUTA_RITORNO_REVOCA,
              ID_UTENTE_REVOCA)
          values
              (smrgaa.seq_delega.nextval,
              nIdIntermediario,
              7,
              nIdAzienda,
              trunc(sysdate),
              null,
              kIDUtente,
              nIdUfficioZona,
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              trunc(sysdate),
              null,
              null,
              null,
              null);
      end;
  
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nel popolamento del mandato CAA = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
      insertLog(pIdChiamata, pIdElab, 'NEGATIVO', 'popolaMandato - Errore generico nel popolamento del mandato CAA = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
  END popolaMandato;
  
  /*PROCEDURE insertLog(pIdChiamata NUMBER,
                               pStato NUMBER,
                               pEsito VARCHAR2,
                               pMessaggio VARCHAR2,
                               pInizioElab DATE,
                               pFineElab DATE,
                               pCodErrore  VARCHAR2,
                               pDescErrore VARCHAR2,
          pCuaa    VARCHAR2) IS
  
  nCount      NUMBER;
  nIdElab     NUMBER;
  nMaxElab    NUMBER;
  
  PRAGMA AUTONOMOUS_TRANSACTION;
  
  BEGIN
  
  
  
      FOR rec IN (SELECT CUAA, DATA_SCHEDA, DATA_RIFE, ID_ORPA, ID_SCHEDA 
      FROM SINC_FA_AABRSCHE_TAB@DB_SIAN
      WHERE PRG_SCHEDA = pIdChiamata) LOOP
  
      SELECT COUNT(*)
      INTO nCount
      FROM AGENT_LOG
      WHERE PRG_SCHEDA = pIdChiamata;
  
      IF nCount = 0 THEN
  
         INSERT INTO AGENT_LOG (ID_ELABORAZIONE, PRG_SCHEDA, CUAA, DATA_SCHEDA, DATA_RIFE, ID_ORPA, ID_SCHEDA,
         STATO, ESITO, MESSAGGIO, INIZIO_ELABORAZIONE, FINE_ELABORAZIONE)
         VALUES (SEQ_AGENT_LOG.NEXTVAL, pIdChiamata, rec.CUAA, rec.DATA_SCHEDA, 
         rec.DATA_RIFE, rec.ID_ORPA, rec.ID_SCHEDA, pStato, pEsito, pMessaggio,
         pInizioElab, pFineElab);
  
      ELSE
  
         SELECT MAX(ID_ELABORAZIONE)
         INTO nMaxElab
         FROM AGENT_LOG
         WHERE PRG_SCHEDA = pIdChiamata;
  
         SELECT COUNT(*)
         INTO nCount
         FROM AGENT_LOG
         WHERE PRG_SCHEDA = pIdChiamata
         AND FINE_ELABORAZIONE IS NULL;
  
         IF nCount > 0 THEN
             --il record è già presente, devo settare data fine elaborazione 
             UPDATE AGENT_LOG
             SET STATO = pStato,
                 ESITO = pEsito,
                 MESSAGGIO = MESSAGGIO||pMessaggio,
                 FINE_ELABORAZIONE = pFineElab
             WHERE PRG_SCHEDA = pIdChiamata;
  
         ELSE
             --siamo nel caso in cui c'è già stata un'elaborazione, quindi verifico se 
             --inserire o modificare nella AGENT_LOG_DETT
             SELECT COUNT(*)
             INTO nCount
             FROM AGENT_LOG_DETT
             WHERE ID_ELABORAZIONE = nMaxElab;
  
             IF nCount = 0 THEN
                 --Inserisco
                 INSERT INTO AGENT_LOG_DETT (ID_ELABORAZIONE, STATO, ESITO, MESSAGGIO, INIZIO_ELABORAZIONE, FINE_ELABORAZIONE, ID_DETT)
                 VALUES (nMaxElab, pStato, pEsito, pMessaggio, pInizioElab, NULL, SEQ_AGENT_LOG_DETT.NEXTVAL);
  
             ELSE
                 SELECT COUNT(*)
                 INTO nCount
                 FROM AGENT_LOG_DETT
                 WHERE ID_ELABORAZIONE = nMaxElab
                 AND FINE_ELABORAZIONE IS NULL;
  
                 IF nCount > 0 THEN
                     UPDATE AGENT_LOG_DETT
                     SET FINE_ELABORAZIONE = pFineElab,
                     STATO = pStato,
                     ESITO = pEsito,
                     MESSAGGIO = MESSAGGIO||pMessaggio
                     WHERE ID_ELABORAZIONE = nMaxElab
                     AND ID_DETT = (SELECT MAX(ID_DETT) FROM AGENT_LOG_DETT WHERE ID_ELABORAZIONE = nMaxElab);
  
                 ELSE
                     INSERT INTO AGENT_LOG_DETT (ID_ELABORAZIONE, STATO, ESITO, MESSAGGIO, INIZIO_ELABORAZIONE, FINE_ELABORAZIONE, ID_DETT)
                     VALUES (nMaxElab, pStato, pEsito, pMessaggio, pInizioElab, NULL, SEQ_AGENT_LOG_DETT.NEXTVAL);
                 END IF;
             END IF;
         END IF;
      END IF;
  
      END LOOP;
      COMMIT;
  
  END insertLog;*/
  
  PROCEDURE startLog(pIdChiamata NUMBER,
                     pMessaggio  VARCHAR2,
                     pIdElab	   OUT NUMBER) IS
    PRAGMA AUTONOMOUS_TRANSACTION;
    nCount      NUMBER;
    nIdElab     NUMBER;
    nMaxElab    NUMBER;  
  BEGIN
  
    --recupera valori della scheda
    FOR rec IN (SELECT CUAA, DATA_SCHEDA, DATA_RIFE, ID_ORPA, ID_SCHEDA 
                FROM SINC_FA_AABRSCHE_TAB@DB_SIAN
                WHERE PRG_SCHEDA = pIdChiamata) LOOP
  
      --inserisce nuova riga su AGENT_LOG
      INSERT INTO AGENT_LOG (ID_ELABORAZIONE, PRG_SCHEDA, CUAA, DATA_SCHEDA, DATA_RIFE, ID_ORPA, ID_SCHEDA,
                             STATO, ESITO, MESSAGGIO, INIZIO_ELABORAZIONE, FINE_ELABORAZIONE)
      VALUES (SEQ_AGENT_LOG.NEXTVAL, pIdChiamata, rec.CUAA, rec.DATA_SCHEDA, rec.DATA_RIFE, 
              rec.ID_ORPA, rec.ID_SCHEDA, NULL, NULL, pMessaggio,SYSDATE, NULL)
      RETURNING ID_ELABORAZIONE INTO pIdElab;
  
      --inserisce nuova riga su AGENT_LOG_DETT per tracciare inizio elaborazione
      INSERT INTO AGENT_LOG_DETT (ID_ELABORAZIONE, ESITO, MESSAGGIO, DATA_ELABORAZIONE, ID_DETT)
      VALUES (pIdElab, NULL, pMessaggio, SYSDATE, SEQ_AGENT_LOG_DETT.NEXTVAL);
    END LOOP;
    COMMIT;
  END startLog;
  
  
  PROCEDURE endLog(pIdChiamata  NUMBER,
                   pIdElab      NUMBER,
                   pStato 	    NUMBER,
                   pEsito 	    VARCHAR2,
                   pMessaggio   VARCHAR2) IS
    PRAGMA AUTONOMOUS_TRANSACTION;
  BEGIN
    --recupera valori della scheda
    FOR rec IN (SELECT CUAA, DATA_SCHEDA, DATA_RIFE, ID_ORPA, ID_SCHEDA 
                FROM SINC_FA_AABRSCHE_TAB@DB_SIAN
                WHERE PRG_SCHEDA = pIdChiamata) LOOP
      --inserisce nuova riga su AGENT_LOG_DETT per tracciare la fine dell'elaborazione
      INSERT INTO AGENT_LOG_DETT (ID_ELABORAZIONE, ESITO, MESSAGGIO, DATA_ELABORAZIONE, ID_DETT)
      VALUES (pIdElab, pEsito, pMessaggio, SYSDATE, SEQ_AGENT_LOG_DETT.NEXTVAL);
      
      --aggiorna riga inserita ad avvio elaborazione su agent_log
      UPDATE AGENT_LOG
      SET  STATO              = pStato,
           ESITO              = pEsito,
           FINE_ELABORAZIONE  = SYSDATE, 
           MESSAGGIO          = pMessaggio
      WHERE ID_ELABORAZIONE = pIdElab;
    END LOOP;
    
    COMMIT;
  
  END endLog;
  
  PROCEDURE insertLog(pIdChiamata NUMBER,
                      pIdElab     NUMBER,
                      pEsito 	    VARCHAR2,
                      pMessaggio  VARCHAR2) IS
    PRAGMA AUTONOMOUS_TRANSACTION;
  BEGIN
    --recupera valori della scheda
    FOR rec IN (SELECT CUAA, DATA_SCHEDA, DATA_RIFE, ID_ORPA, ID_SCHEDA 
                FROM SINC_FA_AABRSCHE_TAB@DB_SIAN
                WHERE PRG_SCHEDA = pIdChiamata) LOOP
  
      --Inserisce nuova riga su agent_log_dett
      INSERT INTO AGENT_LOG_DETT (ID_ELABORAZIONE, ESITO, MESSAGGIO, DATA_ELABORAZIONE, ID_DETT)
      VALUES (pIdElab, pEsito, pMessaggio, SYSDATE, SEQ_AGENT_LOG_DETT.NEXTVAL);
    END LOOP;
    
    COMMIT;
  END insertLog;
  
  PROCEDURE lockAgent(pStato  VARCHAR2) IS
    PRAGMA AUTONOMOUS_TRANSACTION;
  BEGIN
    UPDATE AGENT_LOCK
    SET STATO = pStato;
  
    COMMIT;
  END lockAgent;
  
  PROCEDURE loopElaboraScheda(pScheda NUMBER) IS 
    
    PRAGMA AUTONOMOUS_TRANSACTION;
    pCodErrore NUMBER;
    pDescErrore VARCHAR2(1000);
  
  BEGIN
    PCK_SIANFA_POPOLA_FASC_AGENT.elaboraScheda(pScheda, pCodErrore, pDescErrore);
    
    IF pCodErrore = 0 THEN
      UPDATE AGENT_LOG 
      SET FLAG_RIELABORA = NULL
      WHERE PRG_SCHEDA = pScheda;
      COMMIT;
    ELSE
      ROLLBACK;
    END IF;
  END loopElaboraScheda;
  
  FUNCTION ReturnIdAzienda(pIdChiamata  SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE) RETURN SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE IS
    nIdAzienda  SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE;
  BEGIN
    SELECT ID_AZIENDA
    INTO   nIdAzienda
    FROM   ISWSRespAnagFascicolo15 RAF,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
    WHERE  RAF.ID_CHIAMATA       = pIdChiamata
    AND    RAF.CUAA              = AA.CUAA
    AND    AA.DATA_FINE_VALIDITA IS NULL;
  
    RETURN nIdAzienda;
  END ReturnIdAzienda;
  
  PROCEDURE wrapperSchede IS
  pCodErrore NUMBER;
  pDescErrore VARCHAR2(1000);
  nCount NUMBER;
  vStato VARCHAR2(20);
  BEGIN
  
  SELECT STATO
  INTO vStato
  FROM AGENT_LOCK;
  
  IF vStato <> 'IN ESECUZIONE' THEN
  
     lockAgent('IN ESECUZIONE');
     
      --RICAVARE I PROGRESSIVI SCHEDA DA ELABORARE
      FOR rec IN (SELECT A.PRG_SCHEDA, A.CUAA
                  FROM SINC_FA_AABRSCHE_TAB@db_sian A
                  WHERE A.PRG_SCHEDA NOT IN (SELECT DISTINCT B.PRG_SCHEDA 
                                             FROM AGENT_LOG B 
                                             WHERE FLAG_RIELABORA IS NULL
                                             AND   PRG_SCHEDA NOT IN (SELECT DISTINCT PRG_SCHEDA 
                                                                      FROM AGENT_LOG
                                                                      WHERE FLAG_RIELABORA = 1))
                  ORDER BY 2, 1 ASC) LOOP
      --ELABORARE I PRG_SCHEDA DAL PIù VECCHIO AL PIù RECENTE
      
      loopElaboraScheda(rec.PRG_SCHEDA);
      
      /*PCK_SIANFA_POPOLA_FASC_AGENT.elaboraScheda(rec.PRG_SCHEDA, pCodErrore, pDescErrore);
      
      --Committo dopo l'elaborazione di ogni scheda, solo se la procedura va a buon fine
          IF pCodErrore = 0 THEN
              UPDATE AGENT_LOG 
              SET FLAG_RIELABORA = NULL
              WHERE PRG_SCHEDA = rec.PRG_SCHEDA;
  
              COMMIT;
          END IF;*/
      
      END LOOP;
      
     lockAgent('TERMINATO');
  
  END IF;
  END wrapperSchede;
  
  PROCEDURE wrapperCuaaNoSistema IS
    pCodErrore      NUMBER;
    pDescErrore     VARCHAR2(1000);
    nCount          NUMBER;
    nContaElaborati NUMBER := 0;
    pIdElab         NUMBER;
    vStato          VARCHAR2(20);
  BEGIN
  
    SELECT STATO
    INTO vStato
    FROM AGENT_LOCK;
    
    IF vStato <> 'IN ESECUZIONE' THEN
    
      lockAgent('IN ESECUZIONE');
      
  
      --RICAVARE I PROGRESSIVI SCHEDA DA ELABORARE
      FOR rec IN (SELECT A.PRG_SCHEDA, A.CUAA
                  FROM SINC_FA_AABRSCHE_TAB@db_sian A
                  WHERE A.PRG_SCHEDA NOT IN (SELECT DISTINCT B.PRG_SCHEDA 
                                             FROM AGENT_LOG B 
                                             WHERE FLAG_RIELABORA IS NULL
                                             AND   PRG_SCHEDA     NOT IN (SELECT DISTINCT PRG_SCHEDA 
                                                                          FROM AGENT_LOG
                                                                          WHERE FLAG_RIELABORA = 1))
                  AND   A.CUAA NOT IN (SELECT AA.CUAA 
                                       FROM SMRGAA.DB_ANAGRAFICA_AZIENDA AA 
                                       WHERE AA.DATA_FINE_VALIDITA IS NULL)
                  ORDER BY 2, 1 ASC) LOOP
      
      --elaboro le schede che non sono ancora state processate oppure che sono state processate ma hanno il flag rielabora a 1
      --ELABORARE I PRG_SCHEDA DAL PIù VECCHIO AL PIù RECENTE
      --startLog(rec.PRG_SCHEDA, 'elaboraScheda - Inizio elaborazione', pIdElab);
  
      --Verifico esistenza azienda
      /* SELECT count(*)
         INTO nCount
         FROM   SINC_FA_AABRFASC_TAB@DB_SIAN RAF,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
         WHERE  RAF.PRG_SCHEDA       = rec.PRG_SCHEDA
         AND    RAF.CUAA              = AA.CUAA
         AND    AA.DATA_FINE_VALIDITA IS NULL;*/
  
      --Se l'azienda non esiste (il CUAA non è quindi a sistema), allora procedo con l'elaborazione della scheda
      --IF nCount = 0 THEN
  
      loopElaboraScheda(rec.PRG_SCHEDA);
      
      /*PCK_SIANFA_POPOLA_FASC_AGENT.elaboraScheda(rec.PRG_SCHEDA, pCodErrore, pDescErrore);
        --Committo dopo l'elaborazione di ogni scheda, solo se la procedura va a buon fine
        IF pCodErrore = 0 THEN
          UPDATE AGENT_LOG 
          SET FLAG_RIELABORA = NULL
          WHERE PRG_SCHEDA = rec.PRG_SCHEDA;
          COMMIT;
        END IF;*/
  
        nContaElaborati := nContaElaborati + 1;
        --DBMS_OUTPUT.PUT_LINE('nContaElaborati: '||to_char(nContaElaborati)||' - '||to_char(rec.PRG_SCHEDA));
        --END IF;
        --Elaboro massimo 25 schede
        IF nContaElaborati > 24 THEN
          --DBMS_OUTPUT.PUT_LINE('Esco dal LOOP. nContaElaborati: '||to_char(nContaElaborati)||' - '||to_char(rec.PRG_SCHEDA));
          lockAgent('TERMINATO');
          RETURN;
        END IF;
      END LOOP;
      lockAgent('TERMINATO');
      
    END IF;
  
  END wrapperCuaaNoSistema;
  
  
  PROCEDURE Main(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                 pCuaaInput       SIAN_CHIAMATA_SERVIZIO.CUAA_INPUT%TYPE,
                 pCodErrore   OUT NUMBER,
                 pDescErrore  OUT VARCHAR2) IS
  
    recChiamataServizio          SIAN_CHIAMATA_SERVIZIO%ROWTYPE;
    nCont                        SIMPLE_INTEGER := 0;
    vResponsabile                SMRGAA.DB_RESPONSABILE_INTERMEDIARIO.RESPONSABILE%TYPE;
    vCuaa                        SMRGAA.DB_ANAGRAFICA_AZIENDA.CUAA%TYPE;
    nIdDichiarazioneConsistenza  SMRGAA.DB_DICHIARAZIONE_CONSISTENZA.ID_DICHIARAZIONE_CONSISTENZA%TYPE;
    nCodiceFotografiaTerreni     SMRGAA.DB_DICHIARAZIONE_CONSISTENZA.CODICE_FOTOGRAFIA_TERRENI%TYPE;
    nIdAzienda                   SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE := ReturnIdAzienda(pIdChiamata);
    pIdElab                      NUMBER;
    nAggAz                       NUMBER;
  BEGIN
    pCodErrore  := 0;
    pDescErrore := '';
  
    IF pIdChiamata IS NULL OR pCuaaInput IS NULL THEN
      pCodErrore  := 1;
      pDescErrore := 'Parametri di input obbligatori non valorizzati';
      RETURN;
    END IF;
  
    SELECT *
    INTO   recChiamataServizio
    FROM   SIAN_CHIAMATA_SERVIZIO CS
    WHERE  CS.ID_CHIAMATA = pIdChiamata;
  
    IF recChiamataServizio.CUAA_INPUT != pCuaaInput THEN
      pCodErrore  := 1;
      pDescErrore := 'CUAA di input errato';
      RETURN;
    END IF;
  
    IF recChiamataServizio.ID_SERVIZIO = 2 AND recChiamataServizio.COD_RET_OUTPUT = '012' THEN
      NULL;
    ELSE
      pCodErrore  := 1;
      pDescErrore := 'Dati non corretti';
      RETURN;
    END IF;
  
    -- verifica che la validazione inviata dal Sian non sia gia' presente come ultima validazione dell'azienda in esame
    SELECT COUNT(*)
    INTO   nCont
    FROM   SMRGAA.DB_DICHIARAZIONE_CONSISTENZA DC,ISWSRespAnagFascicolo15 RAF
    WHERE  RAF.ID_CHIAMATA                   = pIdChiamata
    AND    DC.ID_AZIENDA                     = nIdAzienda
    AND    DC.DATA_PROTOCOLLO                = ReturnData(RAF.DATASCHEDAVALIDAZIONE)
    AND    DC.NUMERO_PROTOCOLLO              = RAF.SCHEDAVALIDAZIONE
    AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE)
                                                FROM   SMRGAA.DB_DICHIARAZIONE_CONSISTENZA DC1
                                                WHERE  DC1.ID_AZIENDA = DC.ID_AZIENDA);
  
    IF nCont != 0 THEN
      pCodErrore  := 1;
      pDescErrore := 'Aggiornamento non effettuato';
      RETURN;
    END IF;
  
    -- Verifica della presenza della scheda di validazione inviata dal Sian
    SELECT count(*) 
    into   nCont
    FROM   ISWSRespAnagFascicolo15 RAF
    WHERE  RAF.SCHEDAVALIDAZIONE is not null
    and    RAF.ID_CHIAMATA       = pIdChiamata;
  
    -- se la validazione non viene inviata dal Sian non viene fatto aggiornamento del fascicolo 
    IF nCont = 0 THEN
      pCodErrore  := 1;
      pDescErrore := 'Aggiornamento fascicolo non effettuato per mancanza della scheda validazione';
      RETURN;
    END IF;
  
    popolaDatiSoggetti(pIdChiamata, pIdElab, pCodErrore,pDescErrore);
  
    IF pCodErrore != 0 THEN
      RETURN;
    END IF;
  
    popolaDatiAzienda(pIdChiamata,pIdElab,nAggAz,pCodErrore,pDescErrore);
  
    IF pCodErrore != 0 THEN
      RETURN;
    END IF;
  
    popolaDatiDocumenti(pIdChiamata,pIdElab, pCodErrore,pDescErrore);
  
    IF pCodErrore != 0 THEN
      RETURN;
    END IF;
  
    popolaDatiTerreni(pIdChiamata,pCodErrore,pDescErrore);
  
    IF pCodErrore != 0 THEN
      RETURN;
    END IF;
  
    popolaDatiConsistenza(pIdChiamata,pCodErrore,pDescErrore);
  
    IF pCodErrore != 0 THEN
      RETURN;
    END IF;
  
    popolaDatiColture(pIdChiamata,pCodErrore,pDescErrore);
  
    IF pCodErrore != 0 THEN
      RETURN;
    END IF;
  
    popolaContoCorrente(pIdChiamata,pCodErrore,pDescErrore);
  
    IF pCodErrore != 0 THEN
      RETURN;
    END IF;
  
    popolaDatiManodopera(pIdChiamata,pCodErrore,pDescErrore);
  
    IF pCodErrore != 0 THEN
      RETURN;
    END IF;
  
    popolaMotoriAgricoli(pIdChiamata,pCodErrore,pDescErrore);
  
    IF pCodErrore != 0 THEN
      RETURN;
    END IF;
  
    popolaFabbricati(pIdChiamata,pCodErrore,pDescErrore);
  
    IF pCodErrore != 0 THEN
      RETURN;
    END IF;
  
    popolaUma(pIdChiamata,pCodErrore,pDescErrore);
  
    IF pCodErrore != 0 THEN
      RETURN;
    END IF;
  
    popolaAllevamenti(pIdChiamata,pCodErrore,pDescErrore);
  
    IF pCodErrore != 0 THEN
      RETURN;
    END IF;
  
    -- Ricerca del responsabile del caa detentore del fascicolo
    SELECT RI.RESPONSABILE, AA.CUAA
    INTO   vResponsabile,vCuaa
    FROM   SMRGAA.DB_RESPONSABILE_INTERMEDIARIO RI,SMRGAA.DB_DELEGA D,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
    WHERE  RI.ID_PROCEDIMENTO(+)  = 7
    AND    RI.DATA_FINE(+)        IS NULL
    AND    RI.ID_INTERMEDIARIO(+) = D.ID_INTERMEDIARIO
    AND    D.DATA_FINE(+)         IS NULL
    AND    D.ID_AZIENDA(+)        = AA.ID_AZIENDA
    AND    AA.ID_AZIENDA          = nIdAzienda
    AND    AA.DATA_FINE_VALIDITA  IS NULL;
  
    nIdDichiarazioneConsistenza := SMRGAA.SEQ_DICHIARAZIONE_CONSISTENZA.NEXTVAL;
    nCodiceFotografiaTerreni    := SMRGAA.SEQ_CODICE_FOTOGRAFIA_TERRENI.NEXTVAL;
  
    INSERT INTO SMRGAA.DB_DICHIARAZIONE_CONSISTENZA
    (ID_DICHIARAZIONE_CONSISTENZA, ID_AZIENDA,ANNO,
     DATA,TIPO_CONVALIDA, CODICE_FOTOGRAFIA_TERRENI, ID_PROCEDIMENTO,ID_UTENTE,ID_MOTIVO_DICHIARAZIONE, RESPONSABILE, 
     DATA_AGGIORNAMENTO_FASCICOLO, CUAA_VALIDATO, FLAG_ANOMALIA,DATA_INSERIMENTO_DICHIARAZIONE,FLAG_INVIA_USO, ANNO_CAMPAGNA,DATA_PROTOCOLLO, 
     NUMERO_PROTOCOLLO,FLAG_VARIAZIONE_VERIFICATA)
    SELECT nIdDichiarazioneConsistenza,nIdAzienda,EXTRACT (YEAR FROM ReturnData(RAF.DATASCHEDAVALIDAZIONE)),
           SYSDATE,'D',nCodiceFotografiaTerreni,7,kIDUtente,6,vResponsabile,
           SYSDATE,RAF.CUAA,'N',SYSDATE,'S',EXTRACT (YEAR FROM ReturnData(RAF.DATASCHEDAVALIDAZIONE)),ReturnData(RAF.DATASCHEDAVALIDAZIONE),
           RAF.SCHEDAVALIDAZIONE,'N'
    FROM   ISWSRespAnagFascicolo15 RAF
    WHERE  RAF.ID_CHIAMATA = pIdChiamata;
  
    SMRGAA.PACK_DICHIARAZIONE_CONSISTENZA.SALVA_DICHIARAZIONE(nIdAzienda,EXTRACT (YEAR FROM SYSDATE),nCodiceFotografiaTerreni,kIDUtente,
                                                              pDescErrore,pCodErrore);
  
    IF pCodErrore IS NULL THEN
      pCodErrore := 0;
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nella procedura Main = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;  
  END Main;  
  
  PROCEDURE elaboraScheda(pPrgScheda       SINC_FA_AABRFASC_TAB.PRG_SCHEDA@DB_SIAN%TYPE,
                          pCodErrore   OUT NUMBER,
                          pDescErrore  OUT VARCHAR2) IS 
  
    nCountPrgScheda             NUMBER;
    nCountPrgSchedaRiel         NUMBER;
    nSche 		                  NUMBER;
    vCuaa 		                  SINC_FA_AABRFASC_TAB.CUAA@DB_SIAN%TYPE;
    nIdAzienda 	                SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE;
    nCont 		                  NUMBER;
    vResponsabile               SMRGAA.DB_RESPONSABILE_INTERMEDIARIO.RESPONSABILE%TYPE;
    nIdDichiarazioneConsistenza SMRGAA.DB_DICHIARAZIONE_CONSISTENZA.ID_DICHIARAZIONE_CONSISTENZA%TYPE;
    nCodiceFotografiaTerreni    SMRGAA.DB_DICHIARAZIONE_CONSISTENZA.CODICE_FOTOGRAFIA_TERRENI%TYPE;
    pIdElab                     NUMBER;
    nAggAz                      NUMBER;
  
  BEGIN
  
    pCodErrore  := 0;
    pDescErrore := '';
    
    --Verifico che la scheda non sia già stata elaborata o che ci sia il flag di rielabora attivo
    SELECT COUNT(*)
    INTO nCountPrgSchedaRiel
    FROM AGENT_LOG
    WHERE PRG_SCHEDA     = pPrgScheda
    AND   FLAG_RIELABORA = 1;
  
    SELECT COUNT(*)
    INTO nCountPrgScheda
    FROM AGENT_LOG
    WHERE PRG_SCHEDA = pPrgScheda;
  
    startLog(pPrgScheda, 'elaboraScheda - Inizio elaborazione', pIdElab);
  
    --Verifico esistenza scheda
    SELECT COUNT(*)
    INTO nSche
    FROM SINC_FA_AABRSCHE_TAB@DB_SIAN
    WHERE PRG_SCHEDA = pPrgScheda;
  
    IF pPrgScheda IS NULL OR nSche = 0 THEN
      pCodErrore  := 1;
      pDescErrore := 'Parametri di input obbligatori non valorizzati o errati';
      endLog(pPrgScheda, pIdElab, 2, 'NEGATIVO', 'elaboraScheda - Fine elaborazione. Parametri di input obbligatori non valorizzati o errati');
      RETURN;
    END IF;
  
    BEGIN
      --Verifico esistenza azienda
      SELECT AA.ID_AZIENDA, AA.CUAA
      INTO nIdAzienda, vCuaa
      FROM   SINC_FA_AABRFASC_TAB@DB_SIAN RAF,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
      WHERE  RAF.PRG_SCHEDA        = pPrgScheda
      AND    RAF.CUAA              = AA.CUAA
      AND    AA.DATA_FINE_VALIDITA IS NULL;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN  
        popolaNuovoFascicolo(pPrgScheda, pIdElab, nIdAzienda, pCodErrore, pDescErrore);
        nAggAz := 1;
        IF pDescErrore IS NOT NULL THEN
          RETURN;
        END IF;
    END;
  
    --Verifico se esiste già la dichiarazione di consistenza
    SELECT COUNT(*)
    INTO   nCont
    FROM   SMRGAA.DB_DICHIARAZIONE_CONSISTENZA DC, SINC_FA_AABRFASC_TAB@DB_SIAN RAF, SINC_FA_AABRSCHE_TAB@DB_SIAN SCHE
    WHERE  RAF.PRG_SCHEDA                    		    = pPrgScheda
    AND    DC.ID_AZIENDA                    		    = nIdAzienda
    AND    RAF.PRG_SCHEDA                    		    = SCHE.PRG_SCHEDA
    AND    TO_DATE(DC.DATA_PROTOCOLLO, 'DD-MON-YY') = TO_DATE(RAF.DATA_ULTI_VALI, 'DD-MON-YY')
    AND    DC.NUMERO_PROTOCOLLO              		    = SCHE.ID_SCHEDA
    AND    DC.DATA_INSERIMENTO_DICHIARAZIONE 		    = (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE)
                                                       FROM SMRGAA.DB_DICHIARAZIONE_CONSISTENZA DC1
                                                       WHERE DC1.ID_AZIENDA = DC.ID_AZIENDA);
  
    IF nCont != 0 THEN
      pCodErrore  := 1;
      pDescErrore := 'Aggiornamento non effettuato';
      endLog(pPrgScheda, pIdElab, 2, 'NEGATIVO', 'elaboraScheda - Fine elaborazione. Dichiarazione di consistenza già esistente. Aggiornamento non effettuato');
      RETURN;
    END IF;
    
    IF nCountPrgSchedaRiel = 1 OR nCountPrgScheda = 0 THEN
      --Quindi procedo con la chiamata alle varie procedure
      popolaDatiSoggetti(pPrgScheda, pIdElab, pCodErrore,pDescErrore);
  
      IF pCodErrore != 0 THEN
        endLog(pPrgScheda, pIdElab, 2, 'NEGATIVO', 'elaboraScheda - Fine elaborazione. Errore in popolaDatiSoggetti');
        RETURN;
      END IF;
  
      popolaDatiAzienda(pPrgScheda, pIdElab, nAggAz, pCodErrore,pDescErrore);
  
      IF pCodErrore != 0 THEN
        endLog(pPrgScheda, pIdElab, 2, 'NEGATIVO', 'elaboraScheda - Fine elaborazione. Errore in popolaDatiAzienda');
        RETURN;
      END IF;
  
      /*popolaDatiDocumenti(pPrgScheda, pIdElab, pCodErrore,pDescErrore);
  
        IF pCodErrore != 0 THEN
          RETURN;
        END IF;*/
  
      popolaMandato(pPrgScheda, pIdElab, pCodErrore, pDescErrore);
  
      IF pCodErrore != 0 THEN
        endLog(pPrgScheda, pIdElab, 2, 'NEGATIVO', 'elaboraScheda - Fine elaborazione. Errore in popolaMandato');
        RETURN;
      END IF;
  
      --##inserire tutte le altre procedure
  
      insertLog(pPrgScheda, pIdElab, NULL, 'elaboraScheda - nIdAzienda = '||nIdAzienda);
  
      -- Ricerca del responsabile del caa detentore del fascicolo
      SELECT RI.RESPONSABILE, AA.CUAA
      INTO   vResponsabile,vCuaa
      FROM   SMRGAA.DB_RESPONSABILE_INTERMEDIARIO RI,SMRGAA.DB_DELEGA D,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
      WHERE  RI.ID_PROCEDIMENTO(+)  = 7
      AND    RI.DATA_FINE(+)        IS NULL
      AND    RI.ID_INTERMEDIARIO(+) = D.ID_INTERMEDIARIO
      AND    D.DATA_FINE(+)         IS NULL
      AND    D.ID_AZIENDA(+)        = AA.ID_AZIENDA
      AND    AA.ID_AZIENDA          = nIdAzienda
      AND    AA.DATA_FINE_VALIDITA  IS NULL;
      --insertLog(pPrgScheda, pIdElab, NULL, 'elaboraScheda - DOPO Ricerca del responsabile vCuaa ='||vCuaa );
  
      nIdDichiarazioneConsistenza := SMRGAA.SEQ_DICHIARAZIONE_CONSISTENZA.NEXTVAL;
      nCodiceFotografiaTerreni    := SMRGAA.SEQ_CODICE_FOTOGRAFIA_TERRENI.NEXTVAL;
  
      INSERT INTO SMRGAA.DB_DICHIARAZIONE_CONSISTENZA 
        (ID_DICHIARAZIONE_CONSISTENZA, ID_AZIENDA,ANNO,DATA,TIPO_CONVALIDA, CODICE_FOTOGRAFIA_TERRENI, ID_PROCEDIMENTO,
         ID_UTENTE,ID_MOTIVO_DICHIARAZIONE, RESPONSABILE, DATA_AGGIORNAMENTO_FASCICOLO, CUAA_VALIDATO, FLAG_ANOMALIA,
         DATA_INSERIMENTO_DICHIARAZIONE,FLAG_INVIA_USO, ANNO_CAMPAGNA,DATA_PROTOCOLLO,NUMERO_PROTOCOLLO,FLAG_VARIAZIONE_VERIFICATA)
      SELECT nIdDichiarazioneConsistenza,nIdAzienda, EXTRACT (YEAR FROM RAF.DATA_ULTI_VALI),SYSDATE,'D', 
             nCodiceFotografiaTerreni,7,kIDUtente,6, vResponsabile,SYSDATE,RAF.CUAA,'N',SYSDATE,'S',
             EXTRACT (YEAR FROM RAF.DATA_ULTI_VALI), RAF.DATA_ULTI_VALI,SCHE.ID_SCHEDA,'N'
      FROM   SINC_FA_AABRFASC_TAB@DB_SIAN RAF, SINC_FA_AABRSCHE_TAB@DB_SIAN SCHE
      WHERE  RAF.PRG_SCHEDA = pPrgScheda
      AND    RAF.PRG_SCHEDA = SCHE.PRG_SCHEDA;
      
      insertLog(pPrgScheda, pIdElab, NULL, 'elaboraScheda - DOPO INSERT INTO SMRGAA.DB_DICHIARAZIONE_CONSISTENZA pPrgScheda = '||pPrgScheda );
    END IF;--casistica nCountPrgSchedaRiel = 1 OR nCountPrgScheda = 0
  
    SMRGAA.PACK_DICHIARAZIONE_CONSISTENZA.SALVA_DICHIARAZIONE(nIdAzienda,EXTRACT (YEAR FROM SYSDATE),nCodiceFotografiaTerreni,
                                                              kIDUtente,pDescErrore,pCodErrore);
  
    IF pCodErrore IS NULL THEN
      pCodErrore := 0;
    END IF;
    endLog(pPrgScheda, pIdElab, 1, 'POSITIVO', 'elaboraScheda - Fine elaborazione.');
  
  EXCEPTION
    WHEN OTHERS THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore generico nella procedura elaboraScheda = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;  
      endLog(pPrgScheda, pIdElab, 2, 'NEGATIVO', 'elaboraScheda - Fine elaborazione. Aggiornamento non effettuato. Errore generico nella procedura elaboraScheda = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
  END elaboraScheda;  
  
  PROCEDURE fixNoSedeLegale IS
    vIstatComune  SMRGAA.DB_ANAGRAFICA_AZIENDA.SEDELEG_COMUNE%TYPE;
    vErrCodiBELF  VARCHAR2(4000);
    vIdElab       PLS_INTEGER;
  BEGIN
    --Scorro le aziende senza sede legale, ma che hanno il dato sul DB SIAN
    FOR procNoSede IN (SELECT ID_ANAGRAFICA_AZIENDA,AA.ID_AZIENDA,CUAA,RECA.CODI_GEOG_BELF,
                              MAX(PRG_SCHEDA) PRG_SCHEDA--per loggare
                       FROM SINC_FA_AABRFASC_TAB@db_sian FA
                       JOIN SINC_FA_AABRRECA_TAB@db_sian RECA   USING(PRG_SCHEDA)
                       JOIN SMRGAA.DB_ANAGRAFICA_AZIENDA AA  USING(CUAA)
                       WHERE AA.DATA_FINE_VALIDITA  IS NULL
                       AND   RECA.DECO_TIPO_RECA    IN (208, 56)
                       AND   SEDELEG_COMUNE         IS NULL
                       GROUP BY ID_ANAGRAFICA_AZIENDA,AA.ID_AZIENDA,CUAA,RECA.CODI_GEOG_BELF) LOOP
      BEGIN
        --Inizializzo un paio di variabili
        vIstatComune := NULL;
        vErrCodiBELF := NULL;
        vIdElab      := NULL;
        
        --Apro il log
        startLog(procNoSede.PRG_SCHEDA,'[fixNoSedeLegale]',vIdElab);

        --Ricerca comune tramite codice Belfiore
        BEGIN
          SELECT ISTAT_COMUNE
          INTO vIstatComune
          FROM SMRGAA.COMUNE
          WHERE CODFISC       = procNoSede.CODI_GEOG_BELF
          AND   FLAG_ESTINTO  = 'N';
        EXCEPTION  
          WHEN OTHERS THEN
            --Se non trovo un comune con quel codice Belfiore, o ne trovo > 1, setto la sede d'ufficio nel capoluogo, POTENZA
            vIstatComune := '076063';/*POTENZA*/
            vErrCodiBELF := SUBSTR('comune non trovato o non univoco: '||SQLERRM,1,4000);
        END;
        
        UPDATE SMRGAA.DB_ANAGRAFICA_AZIENDA 
        SET SEDELEG_COMUNE = vIstatComune
        WHERE ID_ANAGRAFICA_AZIENDA = procNoSede.ID_ANAGRAFICA_AZIENDA
        AND   SEDELEG_COMUNE        IS NULL;
        
        --Loggo
        insertLog(procNoSede.PRG_SCHEDA,vIdElab/*Log non inserito nelle normali chiamate*/,'OK',
                  '[fixNoSedeLegale] La sede legale (codice ISTAT) dell''azienda '||procNoSede.CUAA||' è stata settata a '||vIstatComune||
                  CASE WHEN vErrCodiBELF IS NOT NULL THEN 'd''ufficio ('||vErrCodiBELF||')' ELSE NULL END);
      EXCEPTION 
        WHEN OTHERS THEN
          --Loggo
          insertLog(procNoSede.PRG_SCHEDA,vIdElab/*Log non inserito nelle normali chiamate*/,'KO',
                    '[fixNoSedeLegale] Errore generico: '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
          CONTINUE;--Passo all'azienda successiva
      END;--Fine blocco aggiornamento sede di un'azienda
    END LOOP;--Fine loop sulle aziende senza sede legale
  END fixNoSedeLegale;--Fine della procedura
  
END PCK_SIANFA_POPOLA_FASC_AGENT;

/
--------------------------------------------------------
--  DDL for Package Body PCK_SIANFA_POPOLA_FASCICOLO
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "SIANFA"."PCK_SIANFA_POPOLA_FASCICOLO" AS

PROCEDURE ReturnMatrice(pCodiceOccupazioneSuolo        VARCHAR2,
                        pCodiceOccupazioneVarieta      VARCHAR2,
                        pCodiceDestiInazioneUso        VARCHAR2,
                        pCodiceUso                     VARCHAR2,
                        pCodiceQualita                 VARCHAR2,
                        pDataSchedaValidazione         VARCHAR2,
                        pUsoSuoloDefault               VARCHAR2,
                        recCatalogoAll             OUT typeCatalogoAll,
                        pCodErrore                 OUT NUMBER,
                        pDescErrore                OUT VARCHAR2) IS
                        
BEGIN
  pCodErrore  := 0;
  pDescErrore := '';

  BEGIN
    SELECT DISTINCT M.ID_CATALOGO_MATRICE,M.ID_UTILIZZO,M.ID_TIPO_DETTAGLIO_USO,M.ID_VARIETA,TETV.ID_TIPO_EFA,CMS.ID_TIPO_PERIODO_SEMINA,
                    TO_DATE(CMS.INIZIO_DESTINAZIONE_DEFAULT||'/'||SUBSTR(pDataSchedaValidazione,1,4) ,kFormatoDataStd) DATA_INI_SEMINA,
                    TO_DATE(CMS.FINE_DESTINAZIONE_DEFAULT||'/'||SUBSTR(pDataSchedaValidazione,1,4) ,kFormatoDataStd) DATA_FINE_SEMINA,
                    CMA.ID_PRATICA_MANTENIMENTO,M.FLAG_PRATO_PERMANENTE
    INTO   recCatalogoAll
    FROM   SMRGAA.SMRGAA_V_MATRICE M,SMRGAA.DB_TIPO_UTILIZZO TU,SMRGAA.DB_TIPO_DESTINAZIONE TD,SMRGAA.DB_TIPO_DETTAGLIO_USO TDU,
           SMRGAA.DB_TIPO_QUALITA_USO TQU,SMRGAA.DB_TIPO_VARIETA TV,SMRGAA.DB_TIPO_LIVELLO TL,SMRGAA.DB_R_CATALOGO_MANTENIMENTO CMA ,
           SMRGAA.DB_TIPO_EFA_TIPO_VARIETA TETV,SMRGAA.DB_R_CATALOGO_MATRICE_SEMINA CMS
    WHERE  TU.ID_UTILIZZO               = M.ID_UTILIZZO
    AND    TD.ID_TIPO_DESTINAZIONE(+)   = M.ID_TIPO_DESTINAZIONE
    AND    TDU.ID_TIPO_DETTAGLIO_USO(+) = M.ID_TIPO_DETTAGLIO_USO
    AND    TQU.ID_TIPO_QUALITA_USO(+)   = M.ID_TIPO_QUALITA_USO
    AND    TU.ID_UTILIZZO               = TV.ID_UTILIZZO
    AND    TV.ID_VARIETA                = M.ID_VARIETA
    AND    M.ID_TIPO_LIVELLO            = TL.ID_TIPO_LIVELLO
    AND    M.ID_CATALOGO_MATRICE        = TETV.ID_CATALOGO_MATRICE(+)
    AND    TETV.DATA_FINE_VALIDITA(+)   IS NULL
    AND    TU.CODICE                    = pCodiceOccupazioneSuolo
    AND    TV.CODICE_VARIETA            = NVL(pCodiceOccupazioneVarieta,'000') 
    AND    TD.CODICE_DESTINAZIONE       = NVL(pCodiceDestiInazioneUso,'000')
    AND    TDU.CODICE_DETTAGLIO_USO     = NVL(pCodiceUso,'000')
    AND    TQU.CODICE_QUALITA_USO       = NVL(pCodiceQualita,'000')
    AND    NVL(M.FINE_VALIDITA_MATRICE,TO_DATE('31/12/9999','DD/MM/YYYY')) > SYSDATE
    AND    M.ID_CATALOGO_MATRICE        = CMA.ID_CATALOGO_MATRICE(+)
    AND    CMA.FLAG_DEFAULT(+)          = 'S'
    AND    CMA.DATA_FINE_VALIDITA(+)    IS NULL
    AND    M.ID_CATALOGO_MATRICE        = CMS.ID_CATALOGO_MATRICE(+)
    AND    CMS.DATA_FINE_VALIDITA(+)    IS NULL
    AND    CMS.FLAG_DEFAULT(+)          = 'S';
  EXCEPTION
    WHEN OTHERS THEN
      -- Nel caso in cui non trovi niente provo a prendere il record con il minimo codice varieta'
      BEGIN
        SELECT DISTINCT M.ID_CATALOGO_MATRICE,M.ID_UTILIZZO,M.ID_TIPO_DETTAGLIO_USO,M.ID_VARIETA,TETV.ID_TIPO_EFA,CMS.ID_TIPO_PERIODO_SEMINA,
                        TO_DATE(CMS.INIZIO_DESTINAZIONE_DEFAULT||'/'||SUBSTR(pDataSchedaValidazione,1,4) ,kFormatoDataStd) DATA_INI_SEMINA,
                        TO_DATE(CMS.FINE_DESTINAZIONE_DEFAULT||'/'||SUBSTR(pDataSchedaValidazione,1,4) ,kFormatoDataStd) DATA_FINE_SEMINA,
                        CMA.ID_PRATICA_MANTENIMENTO,M.FLAG_PRATO_PERMANENTE
        INTO   recCatalogoAll
        FROM   SMRGAA.SMRGAA_V_MATRICE M,SMRGAA.DB_TIPO_UTILIZZO TU,SMRGAA.DB_TIPO_DESTINAZIONE TD,SMRGAA.DB_TIPO_DETTAGLIO_USO TDU,
               SMRGAA.DB_TIPO_QUALITA_USO TQU,SMRGAA.DB_TIPO_VARIETA TV,SMRGAA.DB_TIPO_LIVELLO TL,SMRGAA.DB_R_CATALOGO_MANTENIMENTO CMA ,
               SMRGAA.DB_TIPO_EFA_TIPO_VARIETA TETV,SMRGAA.DB_R_CATALOGO_MATRICE_SEMINA CMS
        WHERE  TU.ID_UTILIZZO               = M.ID_UTILIZZO
        AND    TD.ID_TIPO_DESTINAZIONE(+)   = M.ID_TIPO_DESTINAZIONE
        AND    TDU.ID_TIPO_DETTAGLIO_USO(+) = M.ID_TIPO_DETTAGLIO_USO
        AND    TQU.ID_TIPO_QUALITA_USO(+)   = M.ID_TIPO_QUALITA_USO
        AND    TU.ID_UTILIZZO               = TV.ID_UTILIZZO
        AND    TV.ID_VARIETA                = M.ID_VARIETA
        AND    M.ID_TIPO_LIVELLO            = TL.ID_TIPO_LIVELLO
        AND    M.ID_CATALOGO_MATRICE        = TETV.ID_CATALOGO_MATRICE(+)
        AND    TETV.DATA_FINE_VALIDITA(+)   IS NULL
        AND    TU.CODICE                    = pCodiceOccupazioneSuolo
        AND    TV.CODICE_VARIETA            = (SELECT MIN(TV1.CODICE_VARIETA)
                                               FROM   SMRGAA.DB_TIPO_VARIETA TV1
                                               WHERE  TV1.ID_UTILIZZO        = TU.ID_UTILIZZO
                                               AND    TV1.DATA_FINE_VALIDITA IS NULL)
        AND    TD.CODICE_DESTINAZIONE       = NVL(pCodiceDestiInazioneUso,'000')
        AND    TDU.CODICE_DETTAGLIO_USO     = NVL(pCodiceUso,'000')
        AND    TQU.CODICE_QUALITA_USO       = NVL(pCodiceQualita,'000')
        AND    NVL(M.FINE_VALIDITA_MATRICE,TO_DATE('31/12/9999','DD/MM/YYYY')) > SYSDATE
        AND    M.ID_CATALOGO_MATRICE        = CMA.ID_CATALOGO_MATRICE(+)
        AND    CMA.FLAG_DEFAULT(+)          = 'S'
        AND    CMA.DATA_FINE_VALIDITA(+)    IS NULL
        AND    M.ID_CATALOGO_MATRICE        = CMS.ID_CATALOGO_MATRICE(+)
        AND    CMS.DATA_FINE_VALIDITA(+)    IS NULL
        AND    CMS.FLAG_DEFAULT(+)          = 'S';
      EXCEPTION
        WHEN OTHERS THEN
          IF pUsoSuoloDefault IS NOT NULL THEN
            BEGIN
              SELECT DISTINCT M.ID_CATALOGO_MATRICE,M.ID_UTILIZZO,M.ID_TIPO_DETTAGLIO_USO,M.ID_VARIETA,TETV.ID_TIPO_EFA,CMS.ID_TIPO_PERIODO_SEMINA,
                     TO_DATE(CMS.INIZIO_DESTINAZIONE_DEFAULT||'/'||SUBSTR(pDataSchedaValidazione,1,4) ,kFormatoDataStd) DATA_INI_SEMINA,
                     TO_DATE(CMS.FINE_DESTINAZIONE_DEFAULT||'/'||SUBSTR(pDataSchedaValidazione,1,4) ,kFormatoDataStd) DATA_FINE_SEMINA,
                     CMA.ID_PRATICA_MANTENIMENTO,M.FLAG_PRATO_PERMANENTE
              INTO   recCatalogoAll
              FROM   SMRGAA.SMRGAA_V_MATRICE M,SMRGAA.DB_TIPO_UTILIZZO TU,SMRGAA.DB_TIPO_DESTINAZIONE TD,SMRGAA.DB_TIPO_DETTAGLIO_USO TDU,
                     SMRGAA.DB_TIPO_QUALITA_USO TQU,SMRGAA.DB_TIPO_VARIETA TV,SMRGAA.DB_TIPO_LIVELLO TL,SMRGAA.DB_R_CATALOGO_MANTENIMENTO CMA ,
                     SMRGAA.DB_TIPO_EFA_TIPO_VARIETA TETV,SMRGAA.DB_R_CATALOGO_MATRICE_SEMINA CMS
              WHERE  TU.ID_UTILIZZO               = M.ID_UTILIZZO
              AND    TD.ID_TIPO_DESTINAZIONE(+)   = M.ID_TIPO_DESTINAZIONE
              AND    TDU.ID_TIPO_DETTAGLIO_USO(+) = M.ID_TIPO_DETTAGLIO_USO
              AND    TQU.ID_TIPO_QUALITA_USO(+)   = M.ID_TIPO_QUALITA_USO
              AND    TU.ID_UTILIZZO               = TV.ID_UTILIZZO
              AND    TV.ID_VARIETA                = M.ID_VARIETA
              AND    M.ID_TIPO_LIVELLO            = TL.ID_TIPO_LIVELLO
              AND    M.ID_CATALOGO_MATRICE        = TETV.ID_CATALOGO_MATRICE(+)
              AND    TETV.DATA_FINE_VALIDITA(+)   IS NULL
              AND    TU.CODICE                    = SUBSTR(pUsoSuoloDefault,1,3)
              AND    TV.CODICE_VARIETA            = SUBSTR(pUsoSuoloDefault,17,3) 
              AND    TD.CODICE_DESTINAZIONE       = SUBSTR(pUsoSuoloDefault,5,3)
              AND    TDU.CODICE_DETTAGLIO_USO     = SUBSTR(pUsoSuoloDefault,9,3)
              AND    TQU.CODICE_QUALITA_USO       = SUBSTR(pUsoSuoloDefault,13,3)
              AND    NVL(M.FINE_VALIDITA_MATRICE,TO_DATE('31/12/9999','DD/MM/YYYY')) > SYSDATE
              AND    M.ID_CATALOGO_MATRICE        = CMA.ID_CATALOGO_MATRICE(+)
              AND    CMA.FLAG_DEFAULT(+)          = 'S'
              AND    CMA.DATA_FINE_VALIDITA(+)    IS NULL
              AND    M.ID_CATALOGO_MATRICE        = CMS.ID_CATALOGO_MATRICE(+)
              AND    CMS.DATA_FINE_VALIDITA(+)    IS NULL
              AND    CMS.FLAG_DEFAULT(+)          = 'S';
            EXCEPTION
              WHEN OTHERS THEN
                pCodErrore  := 1;
                pDescErrore := 'eccezione sulla ricerca catalogo matrice con uso di default. Uso default = '||pUsoSuoloDefault;
                RETURN;
            END;
          ELSE
            pCodErrore  := 1;
            pDescErrore := 'eccezione sulla ricerca catalogo matrice. Codice Occupazione Suolo = '||pCodiceOccupazioneSuolo||
                           ', Codice Destinazione Uso = '||pCodiceDestiInazioneUso||
                           ', Codice Uso = '||pCodiceUso||
                           ', Codice Qualita = '||pCodiceQualita||
                           ', Codice Occupazione Varieta = '||pCodiceOccupazioneVarieta;
            RETURN;
          END IF;
      END;
  END;
END ReturnMatrice;                          

FUNCTION ReturnComune(pCodFisc  SMRGAA.COMUNE.CODFISC%TYPE) RETURN SMRGAA.COMUNE.ISTAT_COMUNE%TYPE IS

  vIstatComune  SMRGAA.COMUNE.ISTAT_COMUNE%TYPE;
BEGIN
  SELECT ISTAT_COMUNE
  INTO   vIstatComune
  FROM   SMRGAA.COMUNE
  WHERE  CODFISC      = pCodFisc
  AND    FLAG_ESTINTO = 'N';

  RETURN vIstatComune;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;  
END ReturnComune;

FUNCTION ReturnData(pDataStringa  VARCHAR2) RETURN DATE IS
BEGIN
  RETURN TO_DATE(pDataStringa,'YYYYMMDD');
END ReturnData;

PROCEDURE popolaDatiSoggetti(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                             pCodErrore   OUT NUMBER,
                             pDescErrore  OUT VARCHAR2) IS

  nCont          SIMPLE_INTEGER := 0;
  nIdSoggetto    SMRGAA.DB_SOGGETTO.ID_SOGGETTO%TYPE;
  recRecapito    RECAPITOWS%ROWTYPE;
  recIndirResid  ISWSINDIRIZZO%ROWTYPE;
BEGIN
  pCodErrore  := 0;
  pDescErrore := '';

  BEGIN
    SELECT *
    INTO   recRecapito
    FROM   RECAPITOWS R
    WHERE  R.ID_CHIAMATA           = pIdChiamata
    AND    ReturnData(R.DATAFINE) >= SYSDATE
    AND    R.DECOTIPORECA          IN (208,56,57);
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      recRecapito := NULL;
    WHEN TOO_MANY_ROWS THEN
      SELECT *
      INTO   recRecapito
      FROM   RECAPITOWS R
      WHERE  R.ID_CHIAMATA           = pIdChiamata
      AND    ReturnData(R.DATAFINE) >= SYSDATE
      AND    R.DECOTIPORECA          IN (208,56,57)
      AND    R.ID_RECAPITOWS         = (SELECT MAX(R1.ID_RECAPITOWS)
                                        FROM   RECAPITOWS R1
                                        WHERE  R1.ID_CHIAMATA           = pIdChiamata
                                        AND    ReturnData(R1.DATAFINE) >= SYSDATE
                                        AND    R1.DECOTIPORECA          IN (208,56,57));
  END;

  BEGIN
    SELECT I.*
    INTO   recIndirResid
    FROM   ISWSRESPANAGFASCICOLO15 F,ISWSINDIRIZZO I
    WHERE  F.ID_CHIAMATA                    = pIdChiamata
    AND    F.SEDERESIDENZA_ID_ISWSINDIRIZZO = I.ID_ISWSINDIRIZZO;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      recIndirResid := NULL;
  END;

  FOR rec IN (SELECT *
              FROM   SOGGETTOWS S
              WHERE  S.ID_CHIAMATA  = pIdChiamata
              AND    S.FLAGPERSFISI = 1) LOOP

    SELECT COUNT(*)
    INTO   nCont
    FROM   SMRGAA.DB_PERSONA_FISICA PF
    WHERE  PF.CODICE_FISCALE = rec.CUAA;

    IF nCont != 0 THEN  
      UPDATE SMRGAA.DB_PERSONA_FISICA 
      SET    COGNOME                 = rec.DESCCOGN, 
             NASCITA_COMUNE          = ReturnComune(rec.CODIFISCLUNA), 
             NOME                    = rec.DESCNOME, 
             SESSO                   = rec.CODISESS, 
             RES_COMUNE              = recIndirResid.PROVINCIA||recIndirResid.COMUNE, 
             NASCITA_DATA            = ReturnData(rec.DATANASC), 
             RES_INDIRIZZO           = recIndirResid.INDIRIZZO, 
             RES_CAP                 = recIndirResid.CAP,
             RES_TELEFONO            = SUBSTR(recRecapito.DESCTELE,1,16), 
             RES_FAX                 = recRecapito.DESCFAXINTE, 
             RES_MAIL                = recRecapito.DESCMAILINTE, 
             DATA_AGGIORNAMENTO      = SYSDATE, 
             ID_UTENTE_AGGIORNAMENTO = kIDUtente, 
             DOM_INDIRIZZO           = recRecapito.DESCINDI, 
             DOM_CAP                 = recRecapito.CODICAPP, 
             DOM_COMUNE              = ReturnComune(recRecapito.CODIFISCRECA), 
             DATA_INIZIO_RESIDENZA   = ReturnData(recRecapito.DATAINIZ)
      WHERE  CODICE_FISCALE          = rec.CUAA;
    ELSE
      INSERT INTO SMRGAA.DB_SOGGETTO
      (ID_SOGGETTO, FLAG_FISICO)
      VALUES
      (SMRGAA.SEQ_SOGGETTO.NEXTVAL,'S')
      RETURNING ID_SOGGETTO INTO nIdSoggetto;

      INSERT INTO SMRGAA.DB_PERSONA_FISICA
      (ID_PERSONA_FISICA, ID_SOGGETTO, CODICE_FISCALE, COGNOME, NASCITA_COMUNE, NOME, SESSO, 
       RES_COMUNE, NASCITA_DATA, RES_INDIRIZZO,RES_CAP, 
       RES_TELEFONO, RES_FAX, RES_MAIL, DATA_AGGIORNAMENTO, NOTE, ID_UTENTE_AGGIORNAMENTO, DOM_INDIRIZZO, 
       DOM_CAP,NASCITA_CITTA_ESTERO, RES_CITTA_ESTERO, MODIFICA_INTERMEDIARIO, ID_TITOLO_STUDIO, ID_INDIRIZZO_STUDIO, DOM_COMUNE, 
       DOM_CITTA_ESTERO, DATA_INIZIO_RESIDENZA, 
       FLAG_CF_OK, NUMERO_CELLULARE, PREFISSO_INTER_CELLULARE, ID_PREFISSO_CELLULARE, DATA_DECESSO)
      VALUES
      (SMRGAA.SEQ_PERSONA_FISICA.NEXTVAL,nIdSoggetto,rec.CUAA,rec.DESCCOGN,ReturnComune(rec.CODIFISCLUNA),rec.DESCNOME,rec.CODISESS,
       recIndirResid.PROVINCIA||recIndirResid.COMUNE,ReturnData(rec.DATANASC),recIndirResid.INDIRIZZO,recIndirResid.CAP,
       SUBSTR(recRecapito.DESCTELE,1,16),recRecapito.DESCFAXINTE,recRecapito.DESCMAILINTE,SYSDATE,NULL,kIDUtente,recRecapito.DESCINDI,
       recRecapito.CODICAPP,NULL,NULL,NULL,NULL,NULL,ReturnComune(recRecapito.CODIFISCRECA),NULL,ReturnData(recRecapito.DATAINIZ),
       NULL,NULL,NULL,NULL,NULL);
    END IF;
  END LOOP;
EXCEPTION
  WHEN OTHERS THEN
    pCodErrore  := 1;
    pDescErrore := 'Errore generico nel popolamento dei soggetti = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
END popolaDatiSoggetti;

PROCEDURE popolaDatiAzienda(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                            pCodErrore   OUT NUMBER,
                            pDescErrore  OUT VARCHAR2) IS

  vCodREA            ISWSISCRIZIONECC_REA.NUMEROISCRIZIONE%TYPE;
  vDataInizioREA     ISWSISCRIZIONECC_REA.DATAINIZIO%TYPE;
  vDataFineREA       ISWSISCRIZIONECC_REA.DATACESSAZIONE%TYPE;
  vPVREA             SMRGAA.DB_ANAGRAFICA_AZIENDA.CCIAA_PROVINCIA_REA%TYPE;
  nNumCodREA         SMRGAA.DB_ANAGRAFICA_AZIENDA.CCIAA_NUMERO_REA%TYPE;
  vCodRI             ISWSISCRIZIONECC_REG_IMPRESE.NUMEROISCRIZIONE%TYPE;
  vDataInizioRI      ISWSISCRIZIONECC_REG_IMPRESE.DATAINIZIO%TYPE;
  vDataFineRI        ISWSISCRIZIONECC_REG_IMPRESE.DATACESSAZIONE%TYPE;
  nIdFormaGiuridica  SMRGAA.DB_TIPO_FORMA_GIURIDICA.ID_FORMA_GIURIDICA%TYPE;
  dDataCessazione    SMRGAA.DB_ANAGRAFICA_AZIENDA.DATA_CESSAZIONE%TYPE;
  vDenominazione     SMRGAA.DB_ANAGRAFICA_AZIENDA.DENOMINAZIONE%TYPE;
  vPVCompetenza      SMRGAA.DB_ANAGRAFICA_AZIENDA.PROVINCIA_COMPETENZA%TYPE;
  nCont              SIMPLE_INTEGER := 0;
  recAzienda         SMRGAA.DB_ANAGRAFICA_AZIENDA%ROWTYPE;
  nIdAzienda         SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE;
  dInizioValAz       SMRGAA.DB_ANAGRAFICA_AZIENDA.DATA_INIZIO_VALIDITA%TYPE;
  nZonaAlt           SMRGAA.COMUNE.ZONAALT%TYPE;
  recUte             SMRGAA.DB_UTE%ROWTYPE;
  dDataFine          SMRGAA.DB_CONTITOLARE.DATA_FINE_RUOLO%TYPE;
  nIDRuolo           SMRGAA.DB_CONTITOLARE.ID_RUOLO%TYPE;
  recContitolare     SMRGAA.DB_CONTITOLARE%ROWTYPE;
  nIdUte             SMRGAA.DB_UTE.ID_UTE%TYPE;
  idCont             TBL_ID := TBL_ID();
  nIdContitolare     SMRGAA.DB_CONTITOLARE.ID_CONTITOLARE%TYPE;
  vNomeIstanza       VARCHAR2(30);
BEGIN
  pCodErrore  := 0;
  pDescErrore := '';

  vNomeIstanza := UPPER(SYS_CONTEXT('USERENV','INSTANCE_NAME'));

  FOR rec in (SELECT RAF.CUAA,RAF.DENOMINAZIONE,RAF.DATAAPERTURAFASCICOLO,
                     DECODE(vNomeIstanza,'SIARB',NULL,RAF.EMAIL) EMAIL,DECODE(vNomeIstanza,'SIARB',NULL,RAF.DESCPEC) DESCPEC,
                     S.CODIPARTIVAA,S.CODICAMECIAAREAA,
                     S.CODICAMECIAARIII,S.DESCNATUGIUR,S.DESCINTE,I.CAP,I.CODICESTATOESTERO,I.COMUNE,I.INDIRIZZO,I.PROVINCIA,DS.ID_DSWS,
                     CASE WHEN RAF.DATACHIUSURAFASCICOLO != kDataFineMax THEN ReturnData(RAF.DATACHIUSURAFASCICOLO) ELSE NULL END DATACHIUSURAFASCICOLO
              FROM   ISWSRESPANAGFASCICOLO15 RAF,DETTAGLIOSOGGETTOWS DS,SOGGETTOWS S,ISWSINDIRIZZO I
              WHERE  RAF.ID_CHIAMATA                    = pIdChiamata
              AND    DS.ID_CHIAMATA                     = RAF.ID_CHIAMATA
              AND    DS.SOGGETTOWS_ID_SOGGETTOWS        = S.ID_SOGGETTOWS
              AND    RAF.SEDERESIDENZA_ID_ISWSINDIRIZZO = I.ID_ISWSINDIRIZZO
              AND    S.ID_CHIAMATA                      = RAF.ID_CHIAMATA
              AND    I.ID_CHIAMATA                      = RAF.ID_CHIAMATA
              AND    RAF.CUAA                           IS NOT NULL) LOOP

    BEGIN
      SELECT NUMEROISCRIZIONE,DATA_INIZIO,DATA_CESSAZIONE
      INTO   vCodREA,vDataInizioREA,vDataFineREA
      FROM   (SELECT I.NUMEROISCRIZIONE,I.DATAINIZIO DATA_INIZIO,I.DATACESSAZIONE DATA_CESSAZIONE                 
              FROM   ISWSRESPANAGFASCICOLO15 RAF,ISWSISCRIZIONECC_REA I
              WHERE  RAF.ID_CHIAMATA               = pIdChiamata
              AND    RAF.IDISWSRESPANAGFASCICOLO15 = I.ID_ISWSRESPANAGFASCICOLO15
              AND    RAF.CUAA                      = rec.CUAA  
              AND    I.NUMEROISCRIZIONE            IS NOT NULL
              ORDER BY I.DATAINIZIO DESC)
      WHERE   ROWNUM = 1;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN  
        vCodREA        := rec.CODICAMECIAAREAA;
        vDataInizioREA := NULL;
        vDataFineREA   := NULL;
    END;

    IF vCodREA IS NOT NULL AND LENGTH(TRIM(TRANSLATE(REPLACE(vCodREA,' '),'(.|/-1234567890',' '))) BETWEEN 2 AND 4 THEN
      vPVREA := UPPER(SUBSTR(TRIM(TRANSLATE(REPLACE(vCodREA,' '),'(.|/-1234567890',' ')),1,2));
    END IF;

    IF vCodREA IS NOT NULL AND LENGTH(TRIM(TRANSLATE(REPLACE(vCodREA,' '),'(.|/-abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ',' '))) > 0 THEN
      vCodREA := TRIM(TRANSLATE(REPLACE(vCodREA,' '),'(.|/-abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ',' '));     
    END IF;

    vCodREA := TRIM(REPLACE(vCodREA,'    '));

    BEGIN
      nNumCodREA := TO_NUMBER(vCodREA);
    EXCEPTION
      WHEN OTHERS THEN
        nNumCodREA := NULL;
    END;

    IF vDataFineREA = kDataFineMax THEN     
      vDataFineREA := NULL;
    END IF;

    BEGIN
      SELECT NUMEROISCRIZIONE,DATA_INIZIO,DATA_CESSAZIONE
      INTO   vCodRI,vDataInizioRI,vDataFineRI
      FROM   (SELECT I.NUMEROISCRIZIONE,I.DATAINIZIO DATA_INIZIO,I.DATACESSAZIONE DATA_CESSAZIONE              
              FROM   ISWSRESPANAGFASCICOLO15 RAF,ISWSISCRIZIONECC_REG_IMPRESE I
              WHERE  RAF.ID_CHIAMATA               = pIdChiamata
              AND    RAF.IDISWSRESPANAGFASCICOLO15 = I.ID_ISWSRESPANAGFASCICOLO15
              AND    RAF.CUAA                      = rec.CUAA  
              AND    I.NUMEROISCRIZIONE            IS NOT NULL
              ORDER BY I.DATAINIZIO DESC)
      WHERE  ROWNUM = 1;      
    EXCEPTION
      WHEN NO_DATA_FOUND THEN  
        vCodRI        := rec.CODICAMECIAARIII;
        vDataInizioRI := NULL;
        vDataFineRI   := NULL;
    END;

    IF vDataFineRI = kDataFineMax THEN     
      vDataFineRI := NULL;
    END IF;

    BEGIN
      SELECT FG.ID_FORMA_GIURIDICA
      INTO   nIdFormaGiuridica        
      FROM   SMRGAA.DB_TIPO_FORMA_GIURIDICA FG
      WHERE  FG.DESCRIZIONE = rec.DESCNATUGIUR;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        nIdFormaGiuridica := NULL;                                    
    END;

    dDataCessazione := rec.DATACHIUSURAFASCICOLO;
    vDenominazione  := rec.DENOMINAZIONE;

    -- se ditta individuale la denominazione e' l'intestazione
    IF nIdFormaGiuridica = kIDFormaGiurDittaIndiv AND TRIM(rec.DESCINTE) IS NOT NULL THEN
      vDenominazione := rec.DESCINTE;                        
    END IF;

    -- se Pv di Basilicata tengo provincia della sede legale, se no fisso POTENZA
    vPVCompetenza := rec.PROVINCIA;

    SELECT COUNT(*) 
    INTO   nCont                            
    FROM   SMRGAA.PROVINCIA P 
    WHERE  P.ISTAT_PROVINCIA = rec.PROVINCIA 
    AND    P.ID_REGIONE      = kIDRegione;

    IF nCont = 0 THEN
      vPVCompetenza := kIstatPotenza;
    END IF;

    BEGIN
      SELECT *
      INTO   recAzienda
      FROM   SMRGAA.DB_ANAGRAFICA_AZIENDA AA
      WHERE  AA.CUAA               = rec.CUAA
      AND    AA.DATA_FINE_VALIDITA IS NULL;

      nIdAzienda := recAzienda.ID_AZIENDA;

      UPDATE SMRGAA.DB_AZIENDA
      SET    FLAG_OBBLIGO_FASCICOLO       = 'S',
             FLAG_ESENTE_DELEGA           = 'N',
             FLAG_COMPETENZA_ESCLUSIVA_PA = 'N',
             DATA_APERTURA_FASCICOLO      = ReturnData(rec.DATAAPERTURAFASCICOLO),
             DATA_CHIUSURA_FASCICOLO      = rec.DATACHIUSURAFASCICOLO,
             DATA_AGGIORNAMENTO_PRATICA   = SYSDATE,
             FASCICOLO_DEMATERIALIZZATO   = 'N'
      WHERE  ID_AZIENDA                   = recAzienda.ID_AZIENDA;

      -- basta che solo uno dei campi sia cambiato storicizzo ed inserisco
      IF NVL(recAzienda.ID_TIPOLOGIA_AZIENDA,1)             != 1 OR
         NVL(recAzienda.PARTITA_IVA,'0')                    != NVL(rec.CODIPARTIVAA,'0') OR
         NVL(recAzienda.DENOMINAZIONE,'0')                  != NVL(vDenominazione,'0') OR
         NVL(recAzienda.ID_FORMA_GIURIDICA,0)               != NVL(nIdFormaGiuridica,0) OR
         NVL(recAzienda.PROVINCIA_COMPETENZA,'0')           != NVL(vPVCompetenza,'0') OR
         NVL(recAzienda.CCIAA_PROVINCIA_REA,'0')            != NVL(vPVREA,'0') OR
         NVL(recAzienda.CCIAA_NUMERO_REA,0)                 != NVL(SUBSTR(TO_NUMBER(nNumCodREA),1,9),0) OR
         NVL(recAzienda.MAIL,'0')                           != NVL(rec.EMAIL,'0') OR
         NVL(recAzienda.PEC,'0')                            != NVL(rec.DESCPEC,'0') OR
         NVL(recAzienda.SEDELEG_COMUNE,'0')                 != NVL(rec.PROVINCIA||rec.COMUNE,'0') OR
         NVL(recAzienda.SEDELEG_INDIRIZZO,'0')              != NVL(rec.INDIRIZZO,'0') OR
         NVL(recAzienda.SEDELEG_CITTA_ESTERO,'0')           != NVL(rec.CODICESTATOESTERO,'0') OR
         NVL(recAzienda.SEDELEG_CAP,'0')                    != NVL(rec.CAP,'0') OR
         NVL(recAzienda.DATA_CESSAZIONE,SYSDATE)            != NVL(dDataCessazione,SYSDATE) OR
         NVL(recAzienda.CCIAA_NUMERO_REGISTRO_IMPRESE,'0')  != NVL(SUBSTR(vCodRI,1,21),'0') OR
         NVL(recAzienda.CCIAA_ANNO_ISCRIZIONE,0)            != NVL(TO_NUMBER(SUBSTR(vDataInizioRI,1,4)),0) OR
         NVL(recAzienda.FLAG_IAP,'N')                       != 'N' OR
         NVL(recAzienda.DATA_ISCRIZIONE_REA,SYSDATE)        != NVL(ReturnData(vDataInizioREA),SYSDATE) OR
         NVL(recAzienda.DATA_CESSAZIONE_REA,SYSDATE)        != NVL(ReturnData(vDataFineREA),SYSDATE) OR
         NVL(recAzienda.DATA_ISCRIZIONE_RI,SYSDATE)         != NVL(ReturnData(vDataInizioRI),SYSDATE) OR
         NVL(recAzienda.DATA_CESSAZIONE_RI,SYSDATE)         != NVL(ReturnData(vDataFineRI),SYSDATE) THEN

        UPDATE SMRGAA.DB_ANAGRAFICA_AZIENDA
        SET    DATA_FINE_VALIDITA      = SYSDATE,
               ID_UTENTE_AGGIORNAMENTO = kIDUtente,
               DATA_AGGIORNAMENTO      = SYSDATE
        WHERE  ID_AZIENDA              = recAzienda.ID_AZIENDA
        AND    DATA_FINE_VALIDITA      IS NULL;

        INSERT INTO SMRGAA.DB_ANAGRAFICA_AZIENDA 
        (ID_ANAGRAFICA_AZIENDA, ID_AZIENDA, ID_TIPOLOGIA_AZIENDA,DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, CUAA,PARTITA_IVA, DENOMINAZIONE, 
         ID_FORMA_GIURIDICA,ID_ATTIVITA_ATECO, PROVINCIA_COMPETENZA, CCIAA_PROVINCIA_REA,CCIAA_NUMERO_REA, MAIL, SEDELEG_COMUNE,
         SEDELEG_INDIRIZZO, SITOWEB, SEDELEG_CITTA_ESTERO,SEDELEG_CAP, DATA_CESSAZIONE, CAUSALE_CESSAZIONE,NOTE, DATA_AGGIORNAMENTO, 
         ID_UTENTE_AGGIORNAMENTO,ID_ATTIVITA_OTE, MOTIVO_MODIFICA, CCIAA_NUMERO_REGISTRO_IMPRESE,CCIAA_ANNO_ISCRIZIONE, MODIFICA_INTERMEDIARIO, 
         NUMERO_AGEA,INTESTAZIONE_PARTITA_IVA, ID_CESSAZIONE, ID_DIMENSIONE_AZIENDA,ID_UDE, RLS, ULU,CODICE_AGRITURISMO, TELEFONO, FAX,PEC, 
         ESONERO_PAGAMENTO_GF, DATA_AGGIORNAMENTO_UMA,FLAG_IAP, DATA_ISCRIZIONE_REA, DATA_CESSAZIONE_REA,DATA_ISCRIZIONE_RI, 
         DATA_CESSAZIONE_RI, DATA_INIZIO_ATECO) 
        VALUES 
        (SMRGAA.SEQ_ANAGRAFICA_AZIENDA.NEXTVAL,recAzienda.ID_AZIENDA,1,SYSDATE,NULL,rec.CUAA,rec.CODIPARTIVAA,vDenominazione,
         nIdFormaGiuridica,NULL,vPVCompetenza,vPVREA,SUBSTR(TO_NUMBER(nNumCodREA),1,9),rec.EMAIL,rec.PROVINCIA||rec.COMUNE,
         rec.INDIRIZZO,null,rec.CODICESTATOESTERO,rec.CAP,dDataCessazione,null,null,sysdate,
         kIDUtente,null,null,SUBSTR(vCodRI,1,21),TO_NUMBER(SUBSTR(vDataInizioRI,1,4)),null,
         null,null,null,null,null,null,null,null,null,null,rec.DESCPEC,
         null,null,'N',ReturnData(vDataInizioREA),ReturnData(vDataFineREA),ReturnData(vDataInizioRI),
         ReturnData(vDataFineRI),null)
        RETURNING DATA_INIZIO_VALIDITA INTO dInizioValAz;

        UPDATE SMRGAA.DB_AZIENDA
        SET    DATA_INIZIO_VALIDITA = dInizioValAz
        WHERE  ID_AZIENDA           = recAzienda.ID_AZIENDA;
      END IF;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        dInizioValAz := SYSDATE;

        INSERT INTO SMRGAA.DB_AZIENDA
        (ID_AZIENDA, DATA_INIZIO_VALIDITA, FLAG_BONIFICA_DATI,VARIAZIONE_UTILIZZI_AMMESSA, FLAG_OBBLIGO_FASCICOLO, ID_AZIENDA_PROVENIENZA, 
         FLAG_AZIENDA_PROVVISORIA, DATA_INSEDIAMENTO, FLAG_ESENTE_DELEGA,FLAG_COMPETENZA_ESCLUSIVA_PA, DATA_CONTROLLO, ID_OPR,
         DATA_APERTURA_FASCICOLO, DATA_CHIUSURA_FASCICOLO, DATA_AGGIORNAMENTO_OPR,ID_UTENTE_AGGIORNAMENTO_OPR, DATA_AGGIORNAMENTO_PRATICA, 
         ID_TIPO_FORMA_ASSOCIATA,FASCICOLO_DEMATERIALIZZATO, DATA_CONTROLLI_ALLEVAMENTI) 
        VALUES 
        (SMRGAA.SEQ_AZIENDA.NEXTVAL,dInizioValAz,NULL,NULL,'S',NULL,
         NULL,NULL,'N','N',NULL,kIDOPR,
         ReturnData(rec.DATAAPERTURAFASCICOLO),rec.DATACHIUSURAFASCICOLO,NULL,NULL,SYSDATE,
         NULL,'N',NULL)
        RETURNING ID_AZIENDA INTO nIdAzienda;

        INSERT INTO SMRGAA.DB_ANAGRAFICA_AZIENDA 
        (ID_ANAGRAFICA_AZIENDA, ID_AZIENDA, ID_TIPOLOGIA_AZIENDA,DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, CUAA,PARTITA_IVA, DENOMINAZIONE, 
         ID_FORMA_GIURIDICA,ID_ATTIVITA_ATECO, PROVINCIA_COMPETENZA, CCIAA_PROVINCIA_REA,CCIAA_NUMERO_REA, MAIL, SEDELEG_COMUNE,
         SEDELEG_INDIRIZZO, SITOWEB, SEDELEG_CITTA_ESTERO,SEDELEG_CAP, DATA_CESSAZIONE, CAUSALE_CESSAZIONE,NOTE, DATA_AGGIORNAMENTO, 
         ID_UTENTE_AGGIORNAMENTO,ID_ATTIVITA_OTE, MOTIVO_MODIFICA, CCIAA_NUMERO_REGISTRO_IMPRESE,CCIAA_ANNO_ISCRIZIONE, MODIFICA_INTERMEDIARIO, 
         NUMERO_AGEA,INTESTAZIONE_PARTITA_IVA, ID_CESSAZIONE, ID_DIMENSIONE_AZIENDA,ID_UDE, RLS, ULU,CODICE_AGRITURISMO, TELEFONO, FAX,PEC, 
         ESONERO_PAGAMENTO_GF, DATA_AGGIORNAMENTO_UMA,FLAG_IAP, DATA_ISCRIZIONE_REA, DATA_CESSAZIONE_REA,DATA_ISCRIZIONE_RI, 
         DATA_CESSAZIONE_RI, DATA_INIZIO_ATECO) 
        VALUES 
        (SMRGAA.SEQ_ANAGRAFICA_AZIENDA.NEXTVAL,nIdAzienda,1,dInizioValAz,NULL,rec.CUAA,rec.CODIPARTIVAA,vDenominazione,
         nIdFormaGiuridica,NULL,vPVCompetenza,vPVREA,SUBSTR(TO_NUMBER(nNumCodREA),1,9),rec.EMAIL,rec.PROVINCIA||rec.COMUNE,
         rec.INDIRIZZO,null,rec.CODICESTATOESTERO,rec.CAP,dDataCessazione,null,null,sysdate,
         kIDUtente,null,null,SUBSTR(vCodRI,1,21),TO_NUMBER(SUBSTR(vDataInizioRI,1,4)),null,
         null,null,null,null,null,null,null,null,null,null,rec.DESCPEC,
         null,null,'N',ReturnData(vDataInizioREA),ReturnData(vDataFineREA),ReturnData(vDataInizioRI),
         ReturnData(vDataFineRI),null);
    END;

    BEGIN
      SELECT ZONAALT
      INTO   nZonaAlt                        
      FROM   SMRGAA.COMUNE C
      WHERE  C.ISTAT_COMUNE = rec.PROVINCIA||rec.COMUNE;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        pCodErrore  := 1;
        pDescErrore := 'Il seguente comune con codice istat '||rec.PROVINCIA||rec.COMUNE||' non esiste';
        RETURN;
    END;

    BEGIN
      SELECT *
      INTO   recUte
      FROM   SMRGAA.DB_UTE
      WHERE  ID_AZIENDA         = nIdAzienda
      AND    DATA_FINE_ATTIVITA IS NULL
      AND    COMUNE             = rec.PROVINCIA||rec.COMUNE
      AND    NVL(INDIRIZZO,'0') = NVL(rec.INDIRIZZO,'0');

      nIdUte := recUte.ID_UTE;

      IF NVL(recUte.CAP,'0')        != NVL(rec.CAP,'0') OR
         recUte.ID_ZONA_ALTIMETRICA != nZonaAlt THEN

        UPDATE SMRGAA.DB_UTE
        SET    CAP                     = rec.CAP,
               ID_ZONA_ALTIMETRICA     = nZonaAlt,
               ID_UTENTE_AGGIORNAMENTO = kIDUtente,
               DATA_AGGIORNAMENTO      = SYSDATE
        WHERE  ID_AZIENDA              = nIdAzienda
        AND    DATA_FINE_ATTIVITA      IS NULL
        AND    COMUNE                  = rec.PROVINCIA||rec.COMUNE
        AND    NVL(INDIRIZZO,'0')      = NVL(rec.INDIRIZZO,'0');
      END IF;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        INSERT INTO SMRGAA.DB_UTE 
        (ID_UTE, ID_AZIENDA, DENOMINAZIONE,INDIRIZZO, COMUNE, CAP,ID_ZONA_ALTIMETRICA, ID_ATTIVITA_ATECO, TELEFONO,FAX, NOTE, 
         DATA_INIZIO_ATTIVITA,DATA_FINE_ATTIVITA, CAUSALE_CESSAZIONE, DATA_AGGIORNAMENTO,ID_UTENTE_AGGIORNAMENTO, MOTIVO_MODIFICA, 
         ID_ATTIVITA_OTE,TIPO_SEDE) 
        VALUES 
        (SMRGAA.SEQ_UTE.NEXTVAL,nIdAzienda,null,rec.INDIRIZZO,rec.PROVINCIA||rec.COMUNE,rec.CAP,nZonaAlt, null, null,null, null, 
         SYSDATE,null, null, SYSDATE,kIDUtente, null, null,
         null)
        RETURNING ID_UTE INTO nIdUte;
    END;

    -- chiudo le ute che non arrivano piu' dal SIAN
    UPDATE SMRGAA.DB_UTE
    SET    ID_UTENTE_AGGIORNAMENTO  = kIDUtente,
           DATA_AGGIORNAMENTO       = SYSDATE,
           DATA_FINE_ATTIVITA       = SYSDATE
    WHERE  ID_AZIENDA               = nIdAzienda
    AND    ID_UTE                  != nIdUte;

    FOR recCont IN (SELECT PF.CODICE_FISCALE,PF.ID_SOGGETTO,MAX(S.DATAINIZATTI) DATA_INIZIO,MAX(S.DATAFINEATTI) DATA_FINE,99 ID_RUOLO
                    FROM   DETTAGLIOSOGGETTOWS DS,SOGGETTOWS S,SMRGAA.DB_PERSONA_FISICA PF
                    WHERE  DS.ID_CHIAMATA              = pIdChiamata
                    AND    DS.SOGGETTOWS_ID_SOGGETTOWS = S.ID_SOGGETTOWS
                    AND    S.FLAGPERSFISI              = 1 -- PERS FISICA
                    AND    S.CUAA                      = PF.CODICE_FISCALE     
                    AND    DS.ID_DSWS                  = rec.ID_DSWS
                    GROUP BY PF.CODICE_FISCALE,PF.ID_SOGGETTO          
                    UNION
                    SELECT PF.CODICE_FISCALE,PF.ID_SOGGETTO,MAX(RL.DATAINIZRAPP) DATA_INIZIO,MAX(RL.DATAFINERAPP) DATA_FINE,1 ID_RUOLO
                    FROM   DETTAGLIOSOGGETTOWS DS,SOGGETTOWS S,SMRGAA.DB_PERSONA_FISICA PF,RAPPRESENTANTELEGALEWS RL
                    WHERE  DS.ID_CHIAMATA               = pIdChiamata
                    AND    S.FLAGPERSFISI               = 1 -- PERS FISICA
                    AND    S.CUAA                       = PF.CODICE_FISCALE     
                    AND    DS.ID_DSWS                   = RL.ID_DSWS
                    AND    ReturnData(RL.DATAFINERAPP) >= SYSDATE
                    AND    RL.SOGGETTOWS_ID_SOGGETTOWS  = S.ID_SOGGETTOWS
                    AND    DS.ID_DSWS                   = rec.ID_DSWS
                    GROUP BY PF.CODICE_FISCALE,PF.ID_SOGGETTO) LOOP

      dDataFine := NULL;

      IF recCont.DATA_FINE != kDataFineMax THEN     
        dDataFine := ReturnData(recCont.DATA_FINE);
      END IF;  

      nIDRuolo := recCont.ID_RUOLO;

      IF recCont.ID_RUOLO = 99 AND recCont.CODICE_FISCALE = rec.CUAA THEN
        nIDRuolo := 1;    
      END IF;

      BEGIN
        SELECT *
        INTO   recContitolare
        FROM   SMRGAA.DB_CONTITOLARE
        WHERE  ID_RUOLO        = nIDRuolo
        AND    ID_AZIENDA      = nIdAzienda
        AND    ID_SOGGETTO     = recCont.ID_SOGGETTO
        AND    DATA_FINE_RUOLO IS NULL;

        idCont.EXTEND;
        idCont(idCont.COUNT) := OBJ_ID(recContitolare.ID_CONTITOLARE);

        IF recContitolare.DATA_INIZIO_RUOLO     != NVL(ReturnData(recCont.DATA_INIZIO),SYSDATE) OR
           recContitolare.DATA_INIZIO_RUOLO_MOD != NVL(ReturnData(recCont.DATA_INIZIO),SYSDATE) OR
           recContitolare.DATA_FINE_RUOLO_MOD   != dDataFine                                    THEN

          UPDATE SMRGAA.DB_CONTITOLARE
          SET    DATA_FINE_RUOLO = SYSDATE
          WHERE  ID_RUOLO        = nIDRuolo
          AND    ID_AZIENDA      = nIdAzienda
          AND    ID_SOGGETTO     = recCont.ID_SOGGETTO
          AND    DATA_FINE_RUOLO IS NULL;

          INSERT INTO SMRGAA.DB_CONTITOLARE 
          (ID_CONTITOLARE, ID_SOGGETTO, ID_RUOLO,ID_AZIENDA, DATA_INIZIO_RUOLO, 
           DATA_FINE_RUOLO,FLAG_ACCESSO_FORZATO, DATA_INIZIO_RUOLO_MOD,DATA_FINE_RUOLO_MOD) 
          VALUES 
          (SMRGAA.SEQ_CONTITOLARE.NEXTVAL,recCont.ID_SOGGETTO,nIDRuolo,nIdAzienda,NVL(ReturnData(recCont.DATA_INIZIO),SYSDATE),
           dDataFine,null,NVL(ReturnData(recCont.DATA_INIZIO),SYSDATE),dDataFine)
          RETURNING ID_CONTITOLARE INTO nIdContitolare;

          idCont.EXTEND;
          idCont(idCont.COUNT) := OBJ_ID(nIdContitolare);
        END IF;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          INSERT INTO SMRGAA.DB_CONTITOLARE 
          (ID_CONTITOLARE, ID_SOGGETTO, ID_RUOLO,ID_AZIENDA, DATA_INIZIO_RUOLO, 
           DATA_FINE_RUOLO,FLAG_ACCESSO_FORZATO, DATA_INIZIO_RUOLO_MOD,DATA_FINE_RUOLO_MOD) 
          VALUES 
          (SMRGAA.SEQ_CONTITOLARE.NEXTVAL,recCont.ID_SOGGETTO,nIDRuolo,nIdAzienda,NVL(ReturnData(recCont.DATA_INIZIO),SYSDATE),
           dDataFine,null,NVL(ReturnData(recCont.DATA_INIZIO),SYSDATE),dDataFine)
          RETURNING ID_CONTITOLARE INTO nIdContitolare;

          idCont.EXTEND;
          idCont(idCont.COUNT) := OBJ_ID(nIdContitolare);
      END;
    END LOOP;

    -- chiusura dei contitolari attivi e non salvati in precedenza
    UPDATE SMRGAA.DB_CONTITOLARE
    SET    DATA_FINE_RUOLO = SYSDATE
    WHERE  ID_AZIENDA      = nIdAzienda
    AND    ID_RUOLO        <> 333
    AND    ID_CONTITOLARE  NOT IN (SELECT ID
                                   FROM   TABLE(idCont));
  END LOOP;
EXCEPTION
  WHEN OTHERS THEN
    pCodErrore  := 1;
    pDescErrore := 'Errore generico nel popolamento dei dati dell''azienda = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
END popolaDatiAzienda;

PROCEDURE popolaDatiDocumenti(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                              pCodErrore   OUT NUMBER,
                              pDescErrore  OUT VARCHAR2) IS

  recDocumento  SMRGAA.DB_DOCUMENTO%ROWTYPE;
  nIdDocumento  SMRGAA.DB_DOCUMENTO.ID_DOCUMENTO%TYPE;
  idDoc         TBL_ID := TBL_ID();
  nIdAzienda    SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE;
BEGIN
  pCodErrore  := 0;
  pDescErrore := '';

  FOR rec IN (SELECT AA.ID_AZIENDA,AA.CUAA,TD.ID_DOCUMENTO ID_TIPO_DOC,RAF.NUMERODOCUMENTO,RAF.DATADOCUMENTO,RAF.DATASCADDOCUMENTO
              FROM   ISWSRESPANAGFASCICOLO15 RAF,SMRGAA.DB_TIPO_DOCUMENTO TD,SMRGAA.DB_DOCUMENTO_CATEGORIA DC,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
              WHERE  RAF.CUAA                  = AA.CUAA
              AND    AA.DATA_FINE_VALIDITA     IS NULL
              AND    RAF.ID_CHIAMATA           = pIdChiamata
              AND    TD.IDENTIFICATIVO_SIAN    = RAF.TIPODOCUMENTO 
              AND    DC.ID_DOCUMENTO           = TD.ID_DOCUMENTO
              AND    DC.ID_CATEGORIA_DOCUMENTO = KIDCategoriaDocIdentita) LOOP 

    nIdAzienda := rec.ID_AZIENDA;

    BEGIN
        SELECT *
        INTO   recDocumento
        FROM   SMRGAA.DB_DOCUMENTO doc
        WHERE  doc.id_documento = (SELECT max(dd.id_documento)
                                    FROM   SMRGAA.DB_DOCUMENTO dd
                                    WHERE  dd.ID_AZIENDA         = rec.ID_AZIENDA
                                    AND    dd.EXT_ID_DOCUMENTO   = rec.ID_TIPO_DOC
                                    AND    dd.NUMERO_DOCUMENTO   = rec.NUMERODOCUMENTO
                                    AND    dd.CUAA               = rec.CUAA
                                    and    dd.id_stato_documento is null);

      UPDATE SMRGAA.DB_DOCUMENTO
      SET    DATA_INIZIO_VALIDITA        = ReturnData(rec.DATADOCUMENTO),
             DATA_FINE_VALIDITA          = ReturnData(rec.DATASCADDOCUMENTO),
             DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE,
             UTENTE_ULTIMO_AGGIORNAMENTO = kIDUtente
      WHERE  ID_AZIENDA                  = rec.ID_AZIENDA
      AND    EXT_ID_DOCUMENTO            = rec.ID_TIPO_DOC
      AND    NUMERO_DOCUMENTO            = rec.NUMERODOCUMENTO
      AND    CUAA                        = rec.CUAA;

      idDoc.EXTEND;
      idDoc(idDoc.COUNT) := OBJ_ID(recDocumento.ID_DOCUMENTO);
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        INSERT INTO SMRGAA.DB_DOCUMENTO 
        (ID_DOCUMENTO, EXT_ID_DOCUMENTO, ID_STATO_DOCUMENTO,ID_AZIENDA, CUAA, DATA_INIZIO_VALIDITA,
         DATA_FINE_VALIDITA, NUMERO_PROTOCOLLO,DATA_PROTOCOLLO,NUMERO_DOCUMENTO, ENTE_RILASCIO_DOCUMENTO, ID_DOCUMENTO_PRECEDENTE,NOTE, 
         DATA_ULTIMO_AGGIORNAMENTO,UTENTE_ULTIMO_AGGIORNAMENTO,DATA_INSERIMENTO, DATA_VARIAZIONE_STATO, ID_UTENTE_AGGIORNAMENTO_SRV,
         NUMERO_PROTOCOLLO_ESTERNO,CUAA_SOCCIDARIO, ESITO_CONTROLLO,DATA_ESECUZIONE, FLAG_CUAA_SOCCIDARIO_VALIDATO, ID_CONTO_CORRENTE,
         ID_CAUSALE_MODIFICA_DOCUMENTO) 
        VALUES 
        (SMRGAA.SEQ_DOCUMENTO.NEXTVAL,rec.ID_TIPO_DOC,null,rec.ID_AZIENDA,rec.CUAA,ReturnData(rec.DATADOCUMENTO),
         ReturnData(rec.DATASCADDOCUMENTO),null,null,rec.NUMERODOCUMENTO,null,null,null,
         SYSDATE,kIDUtente,SYSDATE,null,null,
         null,null,null,null,null,null,
         null)
        RETURNING ID_DOCUMENTO INTO nIdDocumento;

        idDoc.EXTEND;
        idDoc(idDoc.COUNT) := OBJ_ID(nIdDocumento);
    END;
  END LOOP;

  -- aggiornare data_fine con sysdate-1 di tutti i doc non toccati x quell'azienda x la categoria KIDCategoriaDocIdentita
  UPDATE SMRGAA.DB_DOCUMENTO
  SET    DATA_FINE_VALIDITA          = SYSDATE - 1,
         DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE,
         UTENTE_ULTIMO_AGGIORNAMENTO = kIDUtente
  WHERE  ID_AZIENDA                  = nIdAzienda
  AND    EXT_ID_DOCUMENTO            IN (SELECT DC.ID_DOCUMENTO
                                         FROM   SMRGAA.DB_DOCUMENTO_CATEGORIA DC
                                         WHERE  DC.ID_CATEGORIA_DOCUMENTO = KIDCategoriaDocIdentita)
  AND    ID_DOCUMENTO                NOT IN (SELECT ID
                                             FROM   TABLE(idDoc));

  idDoc := TBL_ID();

  FOR rec IN (SELECT DISTINCT AA.ID_AZIENDA,AA.CUAA,DATADOCUMENTO,NUMERODOCUMENTO,TIPODOCUMENTO,TD.ID_DOCUMENTO ID_TIPO_DOCUMENTO
              FROM   ISWSRESPANAGFASCICOLO15 T,SMRGAA.DB_TIPO_DOCUMENTO TD,SMRGAA.DB_DOCUMENTO_CATEGORIA DC,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
              WHERE  T.NUMERODOCUMENTO         IS NOT NULL
              AND    T.ID_CHIAMATA             = pIdChiamata
              AND    T.TIPODOCUMENTO           = TD.IDENTIFICATIVO_SIAN
              AND    DC.ID_DOCUMENTO           = TD.ID_DOCUMENTO
              AND    DC.ID_CATEGORIA_DOCUMENTO = KIDCategoriaDocTP
              AND    T.CUAA                    = AA.CUAA
              AND    AA.DATA_FINE_VALIDITA     IS NULL
              AND    TD.DATA_FINE_VALIDITA     IS NULL) LOOP

    BEGIN
      SELECT *
      INTO   recDocumento
      FROM   SMRGAA.DB_DOCUMENTO
      WHERE  ID_AZIENDA       = rec.ID_AZIENDA
      AND    EXT_ID_DOCUMENTO = rec.ID_TIPO_DOCUMENTO
      AND    NUMERO_DOCUMENTO = rec.NUMERODOCUMENTO
      AND    CUAA             = rec.CUAA;

      nIdDocumento := recDocumento.ID_DOCUMENTO;

      UPDATE SMRGAA.DB_DOCUMENTO
      SET    DATA_INIZIO_VALIDITA        = ReturnData(rec.DATADOCUMENTO),
             DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE,
             UTENTE_ULTIMO_AGGIORNAMENTO = kIDUtente
      WHERE  ID_AZIENDA                  = rec.ID_AZIENDA
      AND    EXT_ID_DOCUMENTO            = rec.ID_TIPO_DOCUMENTO
      AND    NUMERO_DOCUMENTO            = rec.NUMERODOCUMENTO
      AND    CUAA                        = rec.CUAA;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        INSERT INTO SMRGAA.DB_DOCUMENTO 
        (ID_DOCUMENTO, EXT_ID_DOCUMENTO, ID_STATO_DOCUMENTO,ID_AZIENDA, CUAA, DATA_INIZIO_VALIDITA,
         DATA_FINE_VALIDITA, NUMERO_PROTOCOLLO,DATA_PROTOCOLLO,NUMERO_DOCUMENTO, ENTE_RILASCIO_DOCUMENTO, ID_DOCUMENTO_PRECEDENTE,NOTE, 
         DATA_ULTIMO_AGGIORNAMENTO,UTENTE_ULTIMO_AGGIORNAMENTO,DATA_INSERIMENTO, DATA_VARIAZIONE_STATO, ID_UTENTE_AGGIORNAMENTO_SRV,
         NUMERO_PROTOCOLLO_ESTERNO,CUAA_SOCCIDARIO, ESITO_CONTROLLO,DATA_ESECUZIONE, FLAG_CUAA_SOCCIDARIO_VALIDATO, ID_CONTO_CORRENTE,
         ID_CAUSALE_MODIFICA_DOCUMENTO) 
        VALUES 
        (SMRGAA.SEQ_DOCUMENTO.NEXTVAL,rec.ID_TIPO_DOCUMENTO,null,rec.ID_AZIENDA,rec.CUAA,ReturnData(rec.DATADOCUMENTO),
         null,null,null,rec.NUMERODOCUMENTO,null,null,null,
         SYSDATE,kIDUtente,SYSDATE,null,null,
         null,null,null,null,null,null,
         null)
        RETURNING ID_DOCUMENTO INTO nIdDocumento;
    END;

    idDoc.EXTEND;
    idDoc(idDoc.COUNT) := OBJ_ID(nIdDocumento);

    -- prima di inserire cancello
    DELETE SMRGAA.DB_DOCUMENTO_PROPRIETARIO
    WHERE  ID_DOCUMENTO = nIdDocumento;

    FOR recDocPr IN (SELECT DISTINCT P.PROPRIETARIO,PF.COGNOME,PF.NOME
                     FROM   ISWSPROPRIETARIO P,ISWSTERRITORIOFS6 T,SMRGAA.DB_PERSONA_FISICA PF
                     WHERE  T.ID_CHIAMATA          = pIdChiamata
                     AND    P.ID_ISWSTERRITORIOFS6 = T.ID_ISWSTERRITORIOFS6
                     AND    P.PROPRIETARIO         = PF.CODICE_FISCALE(+)
                     AND    T.CUAA                 = rec.CUAA
                     AND    T.NUMERODOCUMENTO      = rec.NUMERODOCUMENTO) LOOP

      INSERT INTO SMRGAA.DB_DOCUMENTO_PROPRIETARIO 
      (ID_DOCUMENTO_PROPRIETARIO, ID_DOCUMENTO, CUAA,DENOMINAZIONE, DATA_ULTIMO_AGGIORNAMENTO, UTENTE_ULTIMO_AGGIORNAMENTO,
       DATA_INSERIMENTO,FLAG_VALIDATO) 
      VALUES 
      (SMRGAA.SEQ_DOCUMENTO_PROPRIETARIO.NEXTVAL,nIdDocumento,recDocPr.PROPRIETARIO,recDocPr.COGNOME||' '||recDocPr.NOME,SYSDATE,kIDUtente,
       SYSDATE,'N');
    END LOOP;
  END LOOP;

  -- aggiornare data_fine con sysdate-1 di tutti i doc non toccati x quell'azienda x la categoria KIDCategoriaDocTP
  UPDATE SMRGAA.DB_DOCUMENTO
  SET    DATA_FINE_VALIDITA          = SYSDATE - 1,
         DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE,
         UTENTE_ULTIMO_AGGIORNAMENTO = kIDUtente
  WHERE  ID_AZIENDA                  = nIdAzienda
  AND    EXT_ID_DOCUMENTO            IN (SELECT DC.ID_DOCUMENTO
                                         FROM   SMRGAA.DB_DOCUMENTO_CATEGORIA DC
                                         WHERE  DC.ID_CATEGORIA_DOCUMENTO = KIDCategoriaDocTP)
  AND    ID_DOCUMENTO                NOT IN (SELECT ID
                                             FROM   TABLE(idDoc));
EXCEPTION
  WHEN OTHERS THEN
    pCodErrore  := 1;
    pDescErrore := 'Errore generico nel popolamento dei dati dei documenti = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
END popolaDatiDocumenti;

PROCEDURE popolaDatiTerreni(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                            pCodErrore   OUT NUMBER,
                            pDescErrore  OUT VARCHAR2) IS

  recStPart         SMRGAA.DB_STORICO_PARTICELLA%ROWTYPE;
  nCont             SIMPLE_INTEGER := 0;
  vFlagBio          SMRGAA.DB_PARTICELLA.BIOLOGICO%TYPE;
  nIdParticella     SMRGAA.DB_PARTICELLA.ID_PARTICELLA%TYPE;
  nZonaAlt          SMRGAA.COMUNE.ZONAALT%TYPE;
  nMediaAltitudine  SMRGAA.DB_STORICO_PARTICELLA.METRI_ALTITUDINE_MEDIA%TYPE;
  nParticella       SMRGAA.DB_STORICO_PARTICELLA.PARTICELLA%TYPE;
BEGIN
  pCodErrore  := 0;
  pDescErrore := '';

  FOR rec IN (SELECT T.PROVINCIA,T.COMUNE,T.SEZIONE,T.FOGLIO,T.PARTICELLA,TO_NUMBER(T.FOGLIO) N_FOGLIO,
                     TRIM(T.SUBALTERNO) SUBALTERNO,T.SUPERFICIECATASTALE,T.CASIPARTICOLARI,T.SUPERFICIEGRAFICA,PI.ID_POTENZIALITA_IRRIGUA,
                     TZ.ID_TERRAZZAMENTO,RC.ID_ROTAZIONE_COLTURALE,
                     CASE WHEN NVL(LENGTH(TRIM(TRANSLATE(REPLACE(T.PARTICELLA,' '),'1234567890',' '))),0) = 0 THEN T.PARTICELLA
                          ELSE REPLACE(T.PARTICELLA,'DEM','999') END PARTICELLA_NUM
              FROM   ISWSTERRITORIOFS6 T,SMRGAA.DB_TIPO_POTENZIALITA_IRRIGUA PI,SMRGAA.DB_TIPO_TERRAZZAMENTO TZ,
                     SMRGAA.DB_TIPO_ROTAZIONE_COLTURALE RC    
              WHERE  T.ID_CHIAMATA            = pIdChiamata
              AND    PI.CODICE(+)             = NVL(T.FLAGIRRIGUA,0)         
              AND    PI.DATA_FINE_VALIDITA(+) IS NULL
              AND    TZ.CODICE(+)             = NVL(T.FLAGTERRAZZATA,0)
              AND    TZ.DATA_FINE_VALIDITA(+) IS NULL
              AND    RC.CODICE(+)             = NVL(T.ROTAZIONECOLTUREORTIVE,0)
              AND    RC.DATA_FINE_VALIDITA(+) IS NULL) LOOP

    BEGIN
      nParticella := TO_NUMBER(rec.PARTICELLA_NUM);
    EXCEPTION
      WHEN OTHERS THEN
        CONTINUE;
    END;

    SELECT COUNT(*)
    INTO   nCont                        
    FROM   ISWSTERRITORIOFS6 T,WSUTILIZZOTERRACABA UT 
    WHERE  T.ID_CHIAMATA             = pIdChiamata
    AND    UT.ID_ISWSTERRITORIOFS6   = T.ID_ISWSTERRITORIOFS6
    AND    T.PROVINCIA               = rec.PROVINCIA 
    AND    T.COMUNE                  = rec.COMUNE
    AND    NVL(T.SEZIONE, '-')       = NVL(rec.SEZIONE, '-')
    AND    T.FOGLIO                  = rec.FOGLIO 
    AND    T.PARTICELLA              = rec.PARTICELLA
    AND    NVL(T.SUBALTERNO, '-')    = NVL(rec.SUBALTERNO, '-')
    AND    UT.CODICECOLTIVAZIONEBIO  IS NOT NULL; -- e' il giusto test x capire se l'azienda fa biologico o no?

    IF nCont > 0 THEN
      vFlagBio := 'S';
    ELSE
      vFlagBio := NULL;
    END IF;

    BEGIN
      SELECT ZONAALT
      INTO   nZonaAlt                        
      FROM   SMRGAA.COMUNE C
      WHERE  C.ISTAT_COMUNE = rec.PROVINCIA||rec.COMUNE;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        pCodErrore  := 1;
        pDescErrore := 'Il seguente comune con codice istat '||rec.PROVINCIA||rec.COMUNE||' non esiste';
        RETURN;
    END;

    BEGIN
      SELECT AVG(NVL(TO_NUMBER(UT.ALTITUDINE),0)) 
      INTO   nMediaAltitudine                            
      FROM   ISWSTERRITORIOFS6 T,WSUTILIZZOTERRACABA UT
      WHERE  T.ID_CHIAMATA           = pIdChiamata
      AND    UT.ID_ISWSTERRITORIOFS6 = T.ID_ISWSTERRITORIOFS6
      AND    T.PROVINCIA             = rec.PROVINCIA 
      AND    T.COMUNE                = rec.COMUNE
      AND    NVL(T.SEZIONE, '-')     = NVL(rec.SEZIONE, '-')
      AND    T.FOGLIO                = rec.FOGLIO 
      AND    T.PARTICELLA            = rec.PARTICELLA
      AND    NVL(T.SUBALTERNO, '-')  = NVL(rec.SUBALTERNO, '-');  
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        nMediaAltitudine := NULL;
    END;

    BEGIN
      SELECT *
      INTO   recStPart
      FROM   SMRGAA.DB_STORICO_PARTICELLA SP
      WHERE  SP.COMUNE              = rec.PROVINCIA||rec.COMUNE
      AND    NVL(SP.SEZIONE,'-')    = NVL(rec.SEZIONE,'-')
      AND    SP.FOGLIO              = rec.N_FOGLIO
      AND    SP.PARTICELLA          = nParticella
      AND    NVL(SP.SUBALTERNO,'-') = NVL(rec.SUBALTERNO,'-')
      AND    SP.DATA_FINE_VALIDITA  IS NULL;

      IF recStPart.SUP_CATASTALE                  != NVL(TO_NUMBER(rec.SUPERFICIECATASTALE),0) / 10000 OR
         NVL(recStPart.ID_ZONA_ALTIMETRICA,0)     != NVL(nZonaAlt,0)                                   OR
         NVL(recStPart.ID_CASO_PARTICOLARE,99)    != NVL(TO_NUMBER(rec.CASIPARTICOLARI),99)            OR
         recStPart.SUPERFICIE_GRAFICA             != NVL(TO_NUMBER(rec.SUPERFICIEGRAFICA),0) / 10000   OR 
         NVL(recStPart.ID_POTENZIALITA_IRRIGUA,0) != NVL(rec.ID_POTENZIALITA_IRRIGUA,0)                OR
         recStPart.ID_TERRAZZAMENTO               != rec.ID_TERRAZZAMENTO                              OR
         recStPart.ID_ROTAZIONE_COLTURALE         != rec.ID_ROTAZIONE_COLTURALE                        OR
         NVL(recStPart.METRI_ALTITUDINE_MEDIA,0)  != NVL(nMediaAltitudine,0)                           THEN

        UPDATE SMRGAA.DB_STORICO_PARTICELLA
        SET    SUP_CATASTALE           = NVL(TO_NUMBER(rec.SUPERFICIECATASTALE),0) / 10000, 
               ID_ZONA_ALTIMETRICA     = nZonaAlt,
               ID_CASO_PARTICOLARE     = NVL(TO_NUMBER(rec.CASIPARTICOLARI),99),
               SUPERFICIE_GRAFICA      = NVL(TO_NUMBER(rec.SUPERFICIEGRAFICA),0) / 10000,
               ID_POTENZIALITA_IRRIGUA = rec.ID_POTENZIALITA_IRRIGUA,
               ID_TERRAZZAMENTO        = rec.ID_TERRAZZAMENTO,
               ID_ROTAZIONE_COLTURALE  = rec.ID_ROTAZIONE_COLTURALE,
               METRI_ALTITUDINE_MEDIA  = nMediaAltitudine,
               ID_UTENTE_AGGIORNAMENTO = kIDUtente,
               DATA_AGGIORNAMENTO      = SYSDATE
        WHERE  COMUNE                  = rec.PROVINCIA||rec.COMUNE
        AND    NVL(SEZIONE,'-')        = NVL(rec.SEZIONE,'-')
        AND    FOGLIO                  = rec.N_FOGLIO
        AND    PARTICELLA              = nParticella
        AND    NVL(SUBALTERNO,'-')     = NVL(rec.SUBALTERNO,'-')
        AND    DATA_FINE_VALIDITA      IS NULL;
      END IF;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        INSERT INTO SMRGAA.DB_PARTICELLA 
        (ID_PARTICELLA, DATA_CREAZIONE, DATA_CESSAZIONE,BIOLOGICO, DATA_INIZIO_VALIDITA, FLAG_SCHEDARIO,FLAG_INVIO_SITI) 
        VALUES 
        (SMRGAA.SEQ_PARTICELLA.NEXTVAL,SYSDATE,NULL,vFlagBio,SYSDATE,'N','N')
        RETURNING ID_PARTICELLA INTO nIdParticella;

        INSERT INTO SMRGAA.DB_STORICO_PARTICELLA 
        (ID_PARTICELLA, ID_STORICO_PARTICELLA, SEZIONE,COMUNE, DATA_INIZIO_VALIDITA, FOGLIO,
         DATA_FINE_VALIDITA, PARTICELLA, SUP_CATASTALE,SUBALTERNO, ID_AREA_A, ID_ZONA_ALTIMETRICA,FLAG_IRRIGABILE, ID_AREA_B, 
         ID_CASO_PARTICOLARE,ID_AREA_C, ID_AREA_D, DATA_AGGIORNAMENTO,ID_UTENTE_AGGIORNAMENTO, ID_CAUSALE_MOD_PARTICELLA, FLAG_CAPTAZIONE_POZZI,
         MOTIVO_MODIFICA, SUP_NON_ELEGGIBILE,SUP_NE_BOSCO_ACQUE_FABBRICATO,SUP_NE_FORAGGIERE, SUP_EL_FRUTTA_GUSCIO, SUP_EL_PRATO_PASCOLO,
         SUP_EL_COLTURE_MISTE,SUP_COLTIVAZ_ARBOREA_CONS, SUP_COLTIVAZ_ARBOREA_SPEC,DATA_FOTO, ID_FONTE, TIPO_FOTO,ID_DOCUMENTO, ID_IRRIGAZIONE, 
         ID_DOCUMENTO_PROTOCOLLATO,ID_FASCIA_FLUVIALE, ID_AREA_G, ID_AREA_H,SUPERFICIE_GRAFICA, ID_POTENZIALITA_IRRIGUA, ID_TERRAZZAMENTO, 
         ID_ROTAZIONE_COLTURALE, ID_AREA_L, ID_AREA_I,ID_AREA_M, PERCENTUALE_PENDENZA_MEDIA, GRADI_PENDENZA_MEDIA,GRADI_ESPOSIZIONE_MEDIA, 
         METRI_ALTITUDINE_MEDIA, ID_METODO_IRRIGUO) 
        VALUES 
        (nIdParticella,SMRGAA.SEQ_STORICO_PARTICELLA.NEXTVAL,rec.SEZIONE,rec.PROVINCIA||rec.COMUNE,SYSDATE,rec.N_FOGLIO,
         null,nParticella,NVL(TO_NUMBER(rec.SUPERFICIECATASTALE),0) / 10000,rec.SUBALTERNO,null,nZonaAlt,'N',null,
         NVL(TO_NUMBER(rec.CASIPARTICOLARI),99),null,null,SYSDATE,kIDUtente,null,'N',
         null,null,null,null,null,null,
         null,null,null,null,null,null,null,null,
         null,null,null,null,NVL(TO_NUMBER(rec.SUPERFICIEGRAFICA),0) / 10000,rec.ID_POTENZIALITA_IRRIGUA,rec.ID_TERRAZZAMENTO,
         rec.ID_ROTAZIONE_COLTURALE,null,null,null,null,null,null,
         nMediaAltitudine,null);
    END;
  END LOOP;
EXCEPTION
  WHEN OTHERS THEN
    pCodErrore  := 1;
    pDescErrore := 'Errore generico nel popolamento dei dati dei terreni = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
END popolaDatiTerreni;

PROCEDURE popolaDatiConsistenza(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                                pCodErrore   OUT NUMBER,
                                pDescErrore  OUT VARCHAR2) IS

  nIDUte                   SMRGAA.DB_UTE.ID_UTE%TYPE;
  nIDPart                  SMRGAA.DB_PARTICELLA.ID_PARTICELLA%TYPE;
  dDataFineConduz          SMRGAA.DB_CONDUZIONE_PARTICELLA.DATA_FINE_CONDUZIONE%TYPE;
  nPercPossesso            SMRGAA.DB_CONDUZIONE_PARTICELLA.PERCENTUALE_POSSESSO%TYPE;
  nIdConduzioneParticella  SMRGAA.DB_CONDUZIONE_PARTICELLA.ID_CONDUZIONE_PARTICELLA%TYPE;
  nIDDocumento             SMRGAA.DB_DOCUMENTO.ID_DOCUMENTO%TYPE;
  recCondPart              SMRGAA.DB_CONDUZIONE_PARTICELLA%ROWTYPE;
  nCont                    SIMPLE_INTEGER := 0;
  idCondu                  TBL_ID := TBL_ID();
  idUte                    TBL_ID := TBL_ID();
  nParticella              SMRGAA.DB_STORICO_PARTICELLA.PARTICELLA%TYPE;
BEGIN
  pCodErrore  := 0;
  pDescErrore := '';

  FOR rec IN (SELECT AA.ID_AZIENDA,AA.CUAA,T.PROVINCIA,T.COMUNE,T.SEZIONE,T.FOGLIO,T.PARTICELLA,TO_NUMBER(T.FOGLIO) N_FOGLIO,
                     CASE WHEN NVL(LENGTH(TRIM(TRANSLATE(REPLACE(T.PARTICELLA,' '),'1234567890',' '))),0) = 0 THEN T.PARTICELLA
                          ELSE REPLACE(T.PARTICELLA,'DEM','999') END PARTICELLA_NUM,
                     TRIM(T.SUBALTERNO) SUBALTERNO,T.PROVINCIA||T.COMUNE ISTAT_COMUNE,
                     TO_NUMBER(DECODE(T.TIPO_CONDUZIONE,'9',5,T.TIPO_CONDUZIONE)) ID_TITOLO_POSSESSO,T.DATAINIZIOCONDUZIONE,
                     T.DATAFINECONDUZIONE,T.SUPERFICIECONDOTTA,T.SUPERFICIECATASTALE,F.DATADOCUMENTO,T.TIPODOCUMENTO,T.NUMERODOCUMENTO
              FROM   ISWSTERRITORIOFS6 T,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,ISWSRESPANAGFASCICOLO15 F
              WHERE  T.ID_CHIAMATA         = pIdChiamata
              AND    T.ID_CHIAMATA         = F.ID_CHIAMATA
              AND    AA.CUAA               = T.CUAA
              AND    AA.DATA_FINE_VALIDITA IS NULL
              ORDER BY 1,2,3,4,5,6,7,8,9,10,11,12,13) LOOP

    BEGIN
      nParticella := TO_NUMBER(rec.PARTICELLA_NUM);
    EXCEPTION
      WHEN OTHERS THEN
        CONTINUE;
    END;

    BEGIN
      SELECT SP.ID_PARTICELLA
      INTO   nIDPart                        
      FROM   SMRGAA.DB_STORICO_PARTICELLA SP
      WHERE  SP.COMUNE              = rec.ISTAT_COMUNE
      AND    NVL(SP.SEZIONE,'-')    = NVL(rec.SEZIONE,'-')
      AND    SP.FOGLIO              = rec.N_FOGLIO
      AND    SP.PARTICELLA          = nParticella
      AND    NVL(SP.SUBALTERNO,'-') = NVL(rec.SUBALTERNO,'-')
      AND    SP.DATA_FINE_VALIDITA  IS NULL;

      SELECT U.ID_UTE
      INTO   nIDUte
      FROM   SMRGAA.DB_UTE U
      WHERE  U.ID_AZIENDA         = rec.ID_AZIENDA
      AND    U.DATA_FINE_ATTIVITA IS NULL; 
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        nIDPart := NULL;
        nIDUte  := NULL;
    END;

    IF nIDPart IS NOT NULL AND nIDUte IS NOT NULL THEN
      BEGIN   
        dDataFineConduz := ReturnData(rec.DATAFINECONDUZIONE);

        IF dDataFineConduz >= SYSDATE THEN
          dDataFineConduz := NULL;
        END IF;

        nPercPossesso := 0;

        IF NVL(TO_NUMBER(rec.SUPERFICIECATASTALE),0) > 0 THEN
          nPercPossesso := LEAST((ROUND(NVL(TO_NUMBER(rec.SUPERFICIECONDOTTA),0) / NVL(TO_NUMBER(rec.SUPERFICIECATASTALE),0),2) * 100),999);
        END IF;

        IF ABS(NVL(TO_NUMBER(rec.SUPERFICIECONDOTTA),0) - NVL(TO_NUMBER(rec.SUPERFICIECATASTALE),0)) <= 50 OR nPercPossesso > 100 THEN
          nPercPossesso := 100;
        END IF;

        SELECT *
        INTO   recCondPart
        FROM   SMRGAA.DB_CONDUZIONE_PARTICELLA
        WHERE  ID_CONDUZIONE_PARTICELLA = (SELECT MAX(ID_CONDUZIONE_PARTICELLA)
                                           FROM   SMRGAA.DB_CONDUZIONE_PARTICELLA CP
                                           WHERE  CP.ID_PARTICELLA      = nIDPart
                                           AND    CP.ID_UTE             = nIDUte
                                           AND    CP.ID_TITOLO_POSSESSO = rec.ID_TITOLO_POSSESSO);

        nIdConduzioneParticella := recCondPart.ID_CONDUZIONE_PARTICELLA;

        IF NVL(NVL(recCondPart.DATA_FINE_CONDUZIONE,dDataFineConduz),SYSDATE) != NVL(dDataFineConduz,SYSDATE) OR 
           recCondPart.DATA_INIZIO_CONDUZIONE                                 != ReturnData(rec.DATAINIZIOCONDUZIONE) OR
           recCondPart.SUPERFICIE_CONDOTTA                                    != (NVL(TO_NUMBER(rec.SUPERFICIECONDOTTA),0) / 10000) OR
           recCondPart.PERCENTUALE_POSSESSO                                   != nPercPossesso THEN

          UPDATE SMRGAA.DB_CONDUZIONE_PARTICELLA
          SET    DATA_FINE_CONDUZIONE    = dDataFineConduz,
                 DATA_INIZIO_CONDUZIONE  = ReturnData(rec.DATAINIZIOCONDUZIONE),
                 SUPERFICIE_CONDOTTA     = NVL(TO_NUMBER(rec.SUPERFICIECONDOTTA),0) / 10000,
                 PERCENTUALE_POSSESSO    = nPercPossesso,
                 DATA_AGGIORNAMENTO      = SYSDATE, 
                 ID_UTENTE_AGGIORNAMENTO = kIDUtente
          WHERE  ID_PARTICELLA           = nIDPart
          AND    ID_UTE                  = nIDUte
          AND    ID_TITOLO_POSSESSO      = rec.ID_TITOLO_POSSESSO;
        END IF;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          INSERT INTO SMRGAA.DB_CONDUZIONE_PARTICELLA 
          (ID_CONDUZIONE_PARTICELLA, ID_PARTICELLA, ID_TITOLO_POSSESSO,ID_UTE, SUPERFICIE_CONDOTTA, 
           FLAG_UTILIZZO_PARTE,DATA_INIZIO_CONDUZIONE,DATA_FINE_CONDUZIONE, NOTE,DATA_AGGIORNAMENTO, ID_UTENTE_AGGIORNAMENTO, ESITO_CONTROLLO,
           DATA_ESECUZIONE, RECORD_MODIFICATO,DICHIARAZIONE_RIPRISTINATA,ID_DICHIARAZIONE_CONSISTENZA, SUPERFICIE_AGRONOMICA, 
           PERCENTUALE_POSSESSO) 
          VALUES 
          (SMRGAA.SEQ_CONDUZIONE_PARTICELLA.NEXTVAL,nIDPart,rec.ID_TITOLO_POSSESSO,nIDUte,NVL(TO_NUMBER(rec.SUPERFICIECONDOTTA),0) / 10000,
           null,ReturnData(rec.DATAINIZIOCONDUZIONE),dDataFineConduz,null,SYSDATE,kIDUtente,null,
           null,null,null,null,null,
           nPercPossesso)
          RETURNING ID_CONDUZIONE_PARTICELLA INTO nIdConduzioneParticella;
      END;

      idCondu.EXTEND;
      idCondu(idCondu.COUNT) := OBJ_ID(nIdConduzioneParticella);

      idUte.EXTEND;
      idUte(idUte.COUNT) := OBJ_ID(nIdUte);

      BEGIN
        SELECT D.ID_DOCUMENTO
        INTO   nIDDocumento                        
        FROM   SMRGAA.DB_TIPO_DOCUMENTO TD,SMRGAA.DB_DOCUMENTO D
        WHERE  D.NUMERO_PROTOCOLLO    = rec.NUMERODOCUMENTO
        AND    rec.TIPODOCUMENTO      = TD.IDENTIFICATIVO_SIAN
        AND    D.EXT_ID_DOCUMENTO     = TD.ID_DOCUMENTO
        AND    D.DATA_INIZIO_VALIDITA = ReturnData(rec.DATADOCUMENTO)
        AND    D.DATA_FINE_VALIDITA   IS NULL
        AND    D.ID_AZIENDA           = rec.ID_AZIENDA
        AND    TD.DATA_FINE_VALIDITA  IS NULL;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          nIDDocumento := NULL;
      END;

      IF nIDDocumento IS NOT NULL THEN
        SELECT COUNT(*)
        INTO   nCont
        FROM   SMRGAA.DB_DOCUMENTO_CONDUZIONE
        WHERE  ID_CONDUZIONE_PARTICELLA = nIdConduzioneParticella
        AND    ID_DOCUMENTO             = nIDDocumento
        AND    DATA_FINE_VALIDITA       IS NULL;

        IF nCont = 0 THEN
          INSERT INTO SMRGAA.DB_DOCUMENTO_CONDUZIONE 
          (ID_DOCUMENTO_CONDUZIONE, ID_CONDUZIONE_PARTICELLA, ID_DOCUMENTO,DATA_INSERIMENTO, DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA,
           LAVORAZIONE_PRIORITARIA, NOTE, ID_ALLEGATO) 
          VALUES 
          (SMRGAA.SEQ_DOCUMENTO_CONDUZIONE.NEXTVAL,nIdConduzioneParticella,nIDDocumento,SYSDATE,ReturnData(rec.DATAINIZIOCONDUZIONE),NULL,
           NULL,NULL,NULL);
        ELSE
          UPDATE SMRGAA.DB_DOCUMENTO_CONDUZIONE
          SET    DATA_INIZIO_VALIDITA     = ReturnData(rec.DATAINIZIOCONDUZIONE)
          WHERE  ID_CONDUZIONE_PARTICELLA = nIdConduzioneParticella
          AND    ID_DOCUMENTO             = nIDDocumento
          AND    DATA_FINE_VALIDITA       IS NULL;
        END IF;
      END IF;
    END IF;
  END LOOP;

  -- chiudere DB_CONDUZIONE_PARTICELLA x id_azienda e id non trattati
  UPDATE SMRGAA.DB_CONDUZIONE_PARTICELLA
  SET    DATA_FINE_CONDUZIONE     = SYSDATE,
         DATA_AGGIORNAMENTO       = SYSDATE, 
         ID_UTENTE_AGGIORNAMENTO  = kIDUtente
  WHERE  ID_UTE                   IN (SELECT ID
                                      FROM   TABLE(idUte))
  AND    ID_CONDUZIONE_PARTICELLA NOT IN (SELECT ID
                                          FROM   TABLE(idCondu));

EXCEPTION
  WHEN OTHERS THEN
    pCodErrore  := 1;
    pDescErrore := 'Errore generico nel popolamento dei dati di consistenza = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
END popolaDatiConsistenza;

PROCEDURE popolaDatiColture(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                            pCodErrore   OUT NUMBER,
                            pDescErrore  OUT VARCHAR2) IS

  nIDUte          SMRGAA.DB_UTE.ID_UTE%TYPE;
  nIDStoricoPart  SMRGAA.DB_STORICO_PARTICELLA.ID_STORICO_PARTICELLA%TYPE;
  nIDPart         SMRGAA.DB_PARTICELLA.ID_PARTICELLA%TYPE;        
  nIDConduzPart   SMRGAA.DB_CONDUZIONE_PARTICELLA.ID_CONDUZIONE_PARTICELLA%TYPE;
  nIDSemina       SMRGAA.DB_UTILIZZO_PARTICELLA.ID_SEMINA%TYPE;
  nIDFaseAllevam  SMRGAA.DB_UTILIZZO_PARTICELLA.ID_FASE_ALLEVAMENTO%TYPE;
  nIDPraticaMant  SMRGAA.DB_UTILIZZO_PARTICELLA.ID_PRATICA_MANTENIMENTO%TYPE;
  recCatalogoAll  typeCatalogoAll;     
  pLogErrore      varchar2(1000);
  nParticella     SMRGAA.DB_STORICO_PARTICELLA.PARTICELLA%TYPE;
BEGIN
  pCodErrore  := 0;
  pDescErrore := '';

  -- cancellare DB_UTILIZZO_PARTICELLA x l'azienda in input
  DELETE SMRGAA.DB_UTILIZZO_PARTICELLA
  WHERE  ID_CONDUZIONE_PARTICELLA IN (SELECT ID_CONDUZIONE_PARTICELLA
                                      FROM   SMRGAA.DB_CONDUZIONE_PARTICELLA CP,SMRGAA.DB_UTE U,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,
                                             ISWSRESPANAGFASCICOLO15 RAF
                                      WHERE  AA.CUAA               = RAF.CUAA
                                      AND    AA.DATA_FINE_VALIDITA IS NULL
                                      AND    RAF.ID_CHIAMATA       = pIdChiamata
                                      AND    U.ID_AZIENDA          = AA.ID_AZIENDA
                                      AND    CP.ID_UTE             = U.ID_UTE);

  FOR rec IN (SELECT AA.ID_AZIENDA,AA.CUAA,T.PROVINCIA,T.COMUNE,T.SEZIONE,T.FOGLIO,T.PARTICELLA,TO_NUMBER(T.FOGLIO) N_FOGLIO,
                     CASE WHEN NVL(LENGTH(TRIM(TRANSLATE(REPLACE(T.PARTICELLA,' '),'1234567890',' '))),0) = 0 THEN T.PARTICELLA
                          ELSE REPLACE(T.PARTICELLA,'DEM','999') END PARTICELLA_NUM,
                     TRIM(T.SUBALTERNO) SUBALTERNO,
                     TO_NUMBER(DECODE(T.TIPO_CONDUZIONE,9,5,T.TIPO_CONDUZIONE)) ID_TITOLO_POSSESSO,MAX(T.DATAINIZIOCONDUZIONE) DATAINIZIOCONDUZIONE,
                     T.DATAFINECONDUZIONE,UT.CODICEMACROUSO,UT.CODICEQUALITA,UT.DATAFINEUTILIZZO,UT.DATAINIZIOUTILIZZO,UT.SUPERFICIEELEGGIBILE, 
                     UT.SUPERFICIEUTILIZZATA,UT.CODICEPRODOTTO,UT.CODICEVARIETA,UT.FLAGCOLTPRINCIPALE,UT.ALTITUDINE,UT.ANNOIMPIANTO,
                     UT.CAPACITAPRODUTTIVA,UT.CODICECOLTIVAZIONEBIO,UT.CODICEFASEALLEVAMENTO,UT.CODICEFORMAALLEVAMENTO,
                     UT.MANTPRATIPERMANENTE,DECODE(UT.MANTENSUPAGRICOLA,'2','9','1','8',NULL) MANTENSUPAGRICOLA,UT.MENZIONE,
                     UT.NUMEROPIANTE,UT.PERCENTUALE,UT.CODICEPROTEZCOLTURE,UT.SESTOIMPIANTOSUFILA,UT.SESTOIMPIANTOTRAFILA,
                     UT.SUPERFICIEELEGGIBILE SUPERFICIEELIGIBILE_DET,UT.SUPERFICIEUTILIZZATA SUPERFICIEUTILIZZATA_DET,
                     UT.TIPOCTIPERTIFICAZIONE,UT.TIPOIMPIANTO,UT.TIPOUTILIZZOOLIVO,UT.CODICETIPOIRRIGAZIONE,UT.CODICETIPOSEMINA,
                     T.PROVINCIA||T.COMUNE ISTAT_COMUNE,CODICEUSO,CODICEDESTIINAZIONEUSO,CODICEOCCUPAZIONEVARIETA,CODICEOCCUPAZIONESUOLO,
                     NVL(RAF.DATASCHEDAVALIDAZIONE,RAF.DATAVALIDAZFASCICOLO) DATASCHEDAVALIDAZIONE
              FROM   ISWSTERRITORIOFS6 T,WSUTILIZZOTERRACABA UT,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,ISWSRESPANAGFASCICOLO15 RAF
              WHERE  AA.CUAA                 = RAF.CUAA
              AND    AA.DATA_FINE_VALIDITA   IS NULL
              AND    RAF.ID_CHIAMATA         = pIdChiamata
              AND    T.ID_CHIAMATA           = RAF.ID_CHIAMATA
              AND    UT.ID_ISWSTERRITORIOFS6 = T.ID_ISWSTERRITORIOFS6
              GROUP BY AA.ID_AZIENDA,AA.CUAA,T.PROVINCIA,T.COMUNE,T.SEZIONE,T.FOGLIO,T.FOGLIO,T.PARTICELLA,TRIM(T.SUBALTERNO) ,
                     TO_NUMBER(DECODE(T.TIPO_CONDUZIONE,9,5,T.TIPO_CONDUZIONE)) ,
                     T.DATAFINECONDUZIONE,UT.CODICEMACROUSO,UT.CODICEQUALITA,UT.DATAFINEUTILIZZO,UT.DATAINIZIOUTILIZZO,UT.SUPERFICIEELEGGIBILE, 
                     UT.SUPERFICIEUTILIZZATA,UT.CODICEPRODOTTO,UT.CODICEVARIETA,UT.FLAGCOLTPRINCIPALE,UT.ALTITUDINE,UT.ANNOIMPIANTO,
                     UT.CAPACITAPRODUTTIVA,UT.CODICECOLTIVAZIONEBIO,UT.CODICEFASEALLEVAMENTO,UT.CODICEFORMAALLEVAMENTO,
                     UT.MANTPRATIPERMANENTE,DECODE(UT.MANTENSUPAGRICOLA,'2','9','1','8',NULL) ,UT.MENZIONE,
                     UT.NUMEROPIANTE,UT.PERCENTUALE,UT.CODICEPROTEZCOLTURE,UT.SESTOIMPIANTOSUFILA,UT.SESTOIMPIANTOTRAFILA,
                     UT.SUPERFICIEELEGGIBILE ,UT.SUPERFICIEUTILIZZATA ,
                     UT.TIPOCTIPERTIFICAZIONE,UT.TIPOIMPIANTO,UT.TIPOUTILIZZOOLIVO,UT.CODICETIPOIRRIGAZIONE,UT.CODICETIPOSEMINA,
                     T.PROVINCIA||T.COMUNE ,CODICEUSO,CODICEDESTIINAZIONEUSO,CODICEOCCUPAZIONEVARIETA,CODICEOCCUPAZIONESUOLO,
                     NVL(RAF.DATASCHEDAVALIDAZIONE,RAF.DATAVALIDAZFASCICOLO)) LOOP -- alcune particelle NON sono SOLO numeriche le scarto

    BEGIN
      nParticella := TO_NUMBER(rec.PARTICELLA_NUM);
    EXCEPTION
      WHEN OTHERS THEN
        CONTINUE;
    END;

    BEGIN
      SELECT U.ID_UTE,SP.ID_STORICO_PARTICELLA,SP.ID_PARTICELLA,CP.ID_CONDUZIONE_PARTICELLA
      INTO   nIDUte,nIDStoricoPart,nIDPart,nIDConduzPart
      FROM   SMRGAA.DB_STORICO_PARTICELLA SP,SMRGAA.DB_UTE U,SMRGAA.DB_CONDUZIONE_PARTICELLA CP
      WHERE  SP.COMUNE                = rec.ISTAT_COMUNE
      AND    NVL(SP.SEZIONE,'-')      = NVL(rec.SEZIONE,'-')
      AND    SP.FOGLIO                = rec.N_FOGLIO
      AND    SP.PARTICELLA            = nParticella
      AND    NVL(SP.SUBALTERNO,'-')   = NVL(rec.SUBALTERNO,'-')
      AND    SP.DATA_FINE_VALIDITA    IS NULL
      AND    U.ID_AZIENDA             = rec.ID_AZIENDA
      AND    U.DATA_FINE_ATTIVITA     IS NULL
      AND    CP.ID_UTE                = U.ID_UTE
      AND    CP.ID_PARTICELLA         = SP.ID_PARTICELLA
      AND    CP.ID_TITOLO_POSSESSO    = rec.ID_TITOLO_POSSESSO
      AND    CP.DATA_FINE_CONDUZIONE  IS NULL;  
      /*AND    NVL(CP.DATA_FINE_CONDUZIONE,ReturnData(kDataFineMax)) >= ReturnData(rec.DATAFINECONDUZIONE)
      AND    CP.DATA_INIZIO_CONDUZIONE                              = ReturnData(rec.DATAINIZIOCONDUZIONE);
     */

      -- cerco CATALOGO MATRICE...                                  
      ReturnMatrice(rec.codiceOccupazioneSuolo,rec.CodiceOccupazioneVarieta,rec.CodiceDestiInazioneUso,rec.CodiceUso,rec.CodiceQualita,
                    rec.DATASCHEDAVALIDAZIONE,NULL,recCatalogoAll,pCodErrore,pDescErrore);

      IF pCodErrore != 0 THEN
        RETURN;
      END IF;

      BEGIN
        SELECT ID_SEMINA
        INTO   nIDSemina                            
        FROM   SMRGAA.DB_TIPO_SEMINA TS
        WHERE  TS.CODICE_SEMINA      = rec.CODICETIPOSEMINA
        AND    TS.DATA_FINE_VALIDITA IS NULL;                                                        
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          nIDSemina := NULL;                            
      END;

      BEGIN
        SELECT ID_FASE_ALLEVAMENTO
        INTO   nIDFaseAllevam                            
        FROM   SMRGAA.DB_TIPO_FASE_ALLEVAMENTO FA
        WHERE  FA.CODICE_FASE_ALLEVAMENTO = rec.CODICEFASEALLEVAMENTO
        AND    FA.DATA_FINE_VALIDITA      IS NULL;                                                      
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          nIDFaseAllevam := NULL;                            
      END;

      nIDPraticaMant := recCatalogoAll.IDPraticaMant;

      IF recCatalogoAll.flagPratoPerm = 'S' AND rec.MANTPRATIPERMANENTE IS NOT NULL THEN
        BEGIN
          SELECT ID_PRATICA_MANTENIMENTO
          INTO   nIDPraticaMant                            
          FROM   SMRGAA.DB_TIPO_PRATICA_MANTENIMENTO TP
          WHERE  TP.CODICE_PRATICA_MANTENIMENTO = rec.MANTPRATIPERMANENTE
          AND    TP.DATA_FINE_VALIDITA          IS NULL;                                                      
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            nIDPraticaMant := recCatalogoAll.IDPraticaMant;
        END;
      ELSIF recCatalogoAll.flagPratoPerm = 'N' AND rec.MANTENSUPAGRICOLA IS NOT NULL THEN
        nIDPraticaMant := TO_NUMBER(rec.MANTENSUPAGRICOLA);
      END IF;

      IF rec.DATAINIZIOUTILIZZO IS NOT NULL THEN
        recCatalogoAll.dataIniSemina := ReturnData(rec.DATAINIZIOUTILIZZO);
      END IF;

      IF rec.DATAFINEUTILIZZO IS NOT NULL THEN
        recCatalogoAll.dataFineSemina := ReturnData(rec.DATAFINEUTILIZZO);
      END IF;

      INSERT INTO SMRGAA.DB_UTILIZZO_PARTICELLA 
      (ID_UTILIZZO_PARTICELLA, ID_UTILIZZO, ID_CONDUZIONE_PARTICELLA,SUPERFICIE_UTILIZZATA, 
       DATA_AGGIORNAMENTO, ID_UTENTE_AGGIORNAMENTO,ANNO, NOTE, ID_UTILIZZO_SECONDARIO,SUP_UTILIZZATA_SECONDARIA, ID_VARIETA, 
       ID_VARIETA_SECONDARIA,ANNO_IMPIANTO, ID_IMPIANTO,SESTO_SU_FILE,SESTO_TRA_FILE, 
       NUMERO_PIANTE_CEPPI, ID_TIPO_DETTAGLIO_USO,ID_TIPO_DETT_USO_SECONDARIO, ID_TIPO_EFA, VALORE_ORIGINALE, 
       VALORE_DOPO_CONVERSIONE, VALORE_DOPO_PONDERAZIONE, ID_TIPO_PERIODO_SEMINA,ID_TIPO_PERIODO_SEMINA_SECOND, ID_SEMINA, ID_SEMINA_SECONDARIA, 
       ID_CATALOGO_MATRICE, DATA_INIZIO_DESTINAZIONE, DATA_FINE_DESTINAZIONE,ID_FASE_ALLEVAMENTO, ID_PRATICA_MANTENIMENTO, 
       ID_CATALOGO_MATRICE_SECONDARIO,DATA_INIZIO_DESTINAZIONE_SEC, DATA_FINE_DESTINAZIONE_SEC)  
      VALUES 
      (SMRGAA.SEQ_UTILIZZO_PARTICELLA.NEXTVAL,recCatalogoAll.IDUtilizzo,nIDConduzPart,NVL(TO_NUMBER(rec.SUPERFICIEUTILIZZATA_DET),0) / 10000,
       SYSDATE,kIDUtente,TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')),null,null,null,recCatalogoAll.IDVarieta,
       null,TO_NUMBER(rec.ANNOIMPIANTO),kIDTipoAltriImpianti,least(TO_NUMBER(rec.SESTOIMPIANTOSUFILA), 9999), least(TO_NUMBER(rec.SESTOIMPIANTOTRAFILA), 9999),
       TO_NUMBER(rec.NUMEROPIANTE),recCatalogoAll.IDDettUso,null,recCatalogoAll.IDTipoEfa,null,
       null,null,recCatalogoAll.IDPeriodoSemina,null,nIDSemina,null,
       recCatalogoAll.IDCatalogMatrix,recCatalogoAll.dataIniSemina,recCatalogoAll.dataFineSemina,nIDFaseAllevam,nIDPraticaMant,
       null,null,null);
    EXCEPTION
      WHEN OTHERS THEN
        pCodErrore  := 1;
        pDescErrore := 'Particella non trovata. Codice Istat = '||rec.ISTAT_COMUNE||', Foglio = '||rec.N_FOGLIO||', Particella = '||
                       nParticella||', Subalterno = '||NVL(rec.SUBALTERNO,'-')||', Sezione = '||NVL(rec.SEZIONE,'-')||
                       ', Azienda = '||rec.ID_AZIENDA||', Titolo Possesso = '||rec.ID_TITOLO_POSSESSO||'. Errore = '||SQLERRM||
                       ' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
        RETURN;
/*
        pLogErrore := 'particella non trovata. Codice Istat = '||rec.ISTAT_COMUNE ||
                      ', Foglio = '||rec.N_FOGLIO||', Particella = '||rec.N_PARTICELLA||
                      ', Subalterno = '||NVL(rec.SUBALTERNO,'-')||', Sezione = '||NVL(rec.SEZIONE,'-')||
                      ', Azienda = '||rec.ID_AZIENDA||', Titolo Possesso = '||rec.ID_TITOLO_POSSESSO||
                      ', Data Fine Conduzione = '|| rec.DATAFINECONDUZIONE||', Data Inizio Conduzione = '||rec.DATAINIZIOCONDUZIONE || 
                      ', Cod macrouso = ' || rec.CODICEMACROUSO ||
                      ', Cod qualita = ' || rec.CODICEQUALITA ||
                      ', Cod prodotto = ' || rec.CODICEPRODOTTO ||
                      ', Cod varieta = ' || rec.CODICEVARIETA ||
                      ' -- ERRORE = ' || SQLERRM || ' - RIGA = ' || dbms_utility.FORMAT_ERROR_BACKTRACE;
        insert into SIANFA.SIAN_LOG_PL
            (ID_LOG_PL, ID_CHIAMATA, COD_ERRORE, DESC_ERRORE, CUAA, DATA_OPERAZIONE)
        values 
            (SIANFA.SEQ_SIAN_LOG_PL.nextval, pIdChiamata, null, pLogErrore, rec.CUAA, sysdate);
*/
    END;
  END LOOP;
EXCEPTION
  WHEN OTHERS THEN
    pCodErrore  := 1;
    pDescErrore := 'Errore generico nel popolamento dei dati delle colture = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
END popolaDatiColture;

PROCEDURE popolaContoCorrente(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                              pCodErrore   OUT NUMBER,
                              pDescErrore  OUT VARCHAR2) IS

  recCC             SMRGAA.DB_CONTO_CORRENTE%ROWTYPE;
  bIns              BOOLEAN := FALSE;
  idCc              TBL_ID := TBL_ID();
  nIdContoCorrente  SMRGAA.DB_CONTO_CORRENTE.ID_CONTO_CORRENTE%TYPE;
  nIdAzienda        SMRGAA.DB_CONTO_CORRENTE.ID_AZIENDA%TYPE;
BEGIN
  pCodErrore  := 0;
  pDescErrore := '';

  FOR rec IN (SELECT distinct
                     (SELECT ID_SPORTELLO
                      FROM   SMRGAA.DB_TIPO_SPORTELLO TS, SMRGAA.DB_TIPO_BANCA TB
                      WHERE  TS.CAB                = CC.CODICABB
                      AND    TS.DATA_FINE_VALIDITA IS NULL
                      AND    TS.ID_BANCA           = TB.ID_BANCA
                      AND    TB.ABI                = CC.CODIABII
                      AND    TB.DATA_FINE_VALIDITA IS NULL) ID_SPORTELLO,AA.ID_AZIENDA,CC.CODINUMECCOR NUMERO_CONTO_CORRENTE,
                     CC.CODIPIN CIN,AA.DENOMINAZIONE INTESTAZIONE,
                     DECODE(CC.DATAINIZ,kDataFineMax,NULL,ReturnData(CC.DATAINIZ)) DATA_INIZIO_VALIDITA,
                     DECODE(CC.DATAFINE,kDataFineMax,NULL,ReturnData(CC.DATAFINE)) DATA_FINE_VALIDITA,
                     DECODE(CC.DATAFINE,kDataFineMax,NULL,ReturnData(CC.DATAFINE)) DATA_ESTINZIONE,
                     SYSDATE DATA_AGGIORNAMENTO,kIDUtente ID_UTENTE_AGGIORNAMENTO,
                     CC.CODIPIN||CC.CODIABII||CC.CODICABB||CC.CODINUMECCOR BBAN,
                     CC.CHECKDIGIT CIFRA_CONTROLLO,CC.CODINAZI||CC.CHECKDIGIT||CC.CODIPIN||CC.CODIABII||CC.CODICABB||CC.CODINUMECCOR IBAN,
                     'S' FLAG_VALIDATO,CC.CODICABB,CC.CODIABII
              FROM   CONTOCORRENTEWS CC, DETTAGLIOSOGGETTOWS DS, ISWSRESPANAGFASCICOLO15 RAF,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
              WHERE  RAF.ID_CHIAMATA          = pIdChiamata
              AND    AA.CUAA                  = RAF.CUAA
              AND    AA.DATA_FINE_VALIDITA    IS NULL
              AND    CC.ID_DSWS               = DS.ID_DSWS
              AND    DS.ID_CHIAMATA           = RAF.ID_CHIAMATA
              AND    LENGTH(CC.CODINUMECCOR) <= 12) LOOP -- escludo i casi con conto corrente maggiore di 12 (limite campo)

    bIns       := FALSE;
    nIdAzienda := rec.ID_AZIENDA;

    IF rec.ID_SPORTELLO IS NOT NULL THEN
      BEGIN
        SELECT *
        INTO   recCC
        FROM   SMRGAA.DB_CONTO_CORRENTE
        WHERE  ID_AZIENDA         = rec.ID_AZIENDA
        AND    IBAN               = rec.IBAN
        AND    DATA_FINE_VALIDITA IS NULL;

        idCc.EXTEND;
        idCc(idCc.COUNT) := OBJ_ID(recCC.ID_CONTO_CORRENTE);

        IF recCC.ID_SPORTELLO                 != rec.ID_SPORTELLO OR
           recCC.NUMERO_CONTO_CORRENTE        != rec.NUMERO_CONTO_CORRENTE OR
           NVL(recCC.CIN,'0')                 != NVL(rec.CIN,'0') OR 
           NVL(recCC.INTESTAZIONE,'0')        != NVL(rec.INTESTAZIONE,'0') OR  
           NVL(recCC.DATA_ESTINZIONE,SYSDATE) != NVL(rec.DATA_ESTINZIONE,SYSDATE) OR
           NVL(recCC.BBAN,'0')                != NVL(rec.BBAN,'0') OR  
           NVL(recCC.CIFRA_CONTROLLO,'0')     != NVL(rec.CIFRA_CONTROLLO,'0') OR
           NVL(recCC.IBAN,'0')                != NVL(rec.IBAN,'0') THEN

--          DBMS_OUTPUT.PUT_LINE('update cc');
--          DBMS_OUTPUT.PUT_LINE('rec.ID_AZIENDA'||' - '||rec.ID_AZIENDA);
--          DBMS_OUTPUT.PUT_LINE('rec.IBAN'||' - '||rec.IBAN);
--          DBMS_OUTPUT.PUT_LINE('rec.DATA_ESTINZIONE'||' - '||rec.DATA_ESTINZIONE);
--          DBMS_OUTPUT.PUT_LINE('rec.DATA_FINE_VALIDITA'||' - '||rec.DATA_FINE_VALIDITA);

          UPDATE SMRGAA.DB_CONTO_CORRENTE
          SET    DATA_AGGIORNAMENTO      = SYSDATE, 
                 DATA_FINE_VALIDITA      = SYSDATE, 
                 ID_UTENTE_AGGIORNAMENTO = rec.ID_UTENTE_AGGIORNAMENTO
          WHERE  ID_AZIENDA              = rec.ID_AZIENDA
          AND    IBAN                    = rec.IBAN
          AND    DATA_FINE_VALIDITA      IS NULL;

          bIns := TRUE;
        END IF;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          bIns := TRUE;
      END;

      IF bIns THEN

--        DBMS_OUTPUT.PUT_LINE('insert cc');
--        DBMS_OUTPUT.PUT_LINE('rec.ID_AZIENDA'||' - '||rec.ID_AZIENDA);
--        DBMS_OUTPUT.PUT_LINE('rec.IBAN'||' - '||rec.IBAN);
--        DBMS_OUTPUT.PUT_LINE('rec.DATA_ESTINZIONE'||' - '||rec.DATA_ESTINZIONE);
--        DBMS_OUTPUT.PUT_LINE('rec.DATA_FINE_VALIDITA'||' - '||rec.DATA_FINE_VALIDITA);

        INSERT INTO SMRGAA.DB_CONTO_CORRENTE
        (ID_CONTO_CORRENTE,ID_SPORTELLO,ID_AZIENDA,NUMERO_CONTO_CORRENTE,CIN,INTESTAZIONE,
         DATA_INIZIO_VALIDITA,DATA_FINE_VALIDITA,DATA_ESTINZIONE,DATA_AGGIORNAMENTO,ID_UTENTE_AGGIORNAMENTO,BBAN,
         CIFRA_CONTROLLO,IBAN,FLAG_VALIDATO)
        VALUES
        (SMRGAA.SEQ_CONTO_CORRENTE.NEXTVAL,rec.ID_SPORTELLO,rec.ID_AZIENDA,rec.NUMERO_CONTO_CORRENTE,rec.CIN,rec.INTESTAZIONE,
         rec.DATA_INIZIO_VALIDITA,rec.DATA_FINE_VALIDITA,rec.DATA_ESTINZIONE,rec.DATA_AGGIORNAMENTO,rec.ID_UTENTE_AGGIORNAMENTO,rec.BBAN,
         rec.CIFRA_CONTROLLO,rec.IBAN,rec.FLAG_VALIDATO)
        RETURNING ID_CONTO_CORRENTE INTO nIdContoCorrente;

        idCc.EXTEND;
        idCc(idCc.COUNT) := OBJ_ID(nIdContoCorrente);
      END IF;
    END IF;
  END LOOP;

  -- chiusura DB_CONTO_CORRENTE dei record x l'azienda non trattati 
  UPDATE SMRGAA.DB_CONTO_CORRENTE
  SET    DATA_AGGIORNAMENTO      = SYSDATE, 
         DATA_FINE_VALIDITA      = SYSDATE + 1/86400, -- aggiungo un secondo per evitare la violazione dell'AK
         ID_UTENTE_AGGIORNAMENTO = kIDUtente
  WHERE  ID_AZIENDA              = nIdAzienda
  AND    DATA_FINE_VALIDITA      IS NULL
  AND    ID_CONTO_CORRENTE       NOT IN (SELECT ID
                                         FROM   TABLE(idCc));
EXCEPTION
  WHEN OTHERS THEN
    pCodErrore  := 1;
    pDescErrore := 'Errore generico nel popolamento dei conti corrente = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
END popolaContoCorrente;

PROCEDURE popolaDatiManodopera(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                               pCodErrore   OUT NUMBER,
                               pDescErrore  OUT VARCHAR2) IS

  nIdManodopera   SMRGAA.DB_MANODOPERA.ID_MANODOPERA%TYPE;
  nUomini         SMRGAA.DB_DETTAGLIO_MANODOPERA.UOMINI%TYPE;
  nDonne          SMRGAA.DB_DETTAGLIO_MANODOPERA.DONNE%TYPE;
  nGiornateAnnue  SMRGAA.DB_DETTAGLIO_MANODOPERA.GIORNATE_ANNUE%TYPE;
  nCont           SIMPLE_INTEGER := 0;
  recMan          SMRGAA.DB_MANODOPERA%ROWTYPE;
  bInsMan         BOOLEAN := FALSE;
  bInsDettMan     BOOLEAN := FALSE;
  idMan           TBL_ID := TBL_ID();
  idClasse        TBL_ID := TBL_ID();
BEGIN
  pCodErrore  := 0;
  pDescErrore := '';

  FOR rec IN (SELECT AA.ID_AZIENDA,
                     (SELECT INPS.NUMEROISCRIZIONE 
                      FROM   ISWSCODICEINPS INPS 
                      WHERE  INPS.ID_ISWSRESPANAGFASCICOLO15 = RAF.IDISWSRESPANAGFASCICOLO15) CODICE_INPS,SYSDATE DATA_INIZIO_VALIDITA,
                     NULL DATA_FINE_VALIDITA,SYSDATE DATA_AGGIORNAMENTO,kIDUtente ID_UTENTE_AGGIORNAMENTO,
                     (SELECT ID_FORMA_CONDUZIONE 
                      FROM   SMRGAA.DB_TIPO_FORMA_CONDUZIONE 
                      WHERE  ID_FORMA_CONDUZIONE = DECODE(L.TIPOLAVOROPREVALENTE, 0, 1, 1, 2, 2, 3, 3, 6, 4, 7, NULL)) ID_FORMA_CONDUZIONE,
                     NULL MATRICOLA_INAIL,
                     (SELECT ID_TIPO_ISCRIZIONE_INPS
                      FROM   SMRGAA.DB_TIPO_ISCRIZIONE_INPS TII, ISWSCODICEINPS INPS
                      WHERE  CODICE_TIPO_ISCRIZIONE          = INPS.CODICETIPOISCRIZIONE
                      AND    INPS.ID_ISWSRESPANAGFASCICOLO15 = RAF.IDISWSRESPANAGFASCICOLO15) ID_TIPO_ISCRIZIONE_INPS,
                     (SELECT DECODE(INPS.DATAINIZIO, kDataFineMax, NULL, ReturnData(INPS.DATAINIZIO)) 
                      FROM   ISWSCODICEINPS INPS 
                      WHERE  INPS.ID_ISWSRESPANAGFASCICOLO15 = RAF.IDISWSRESPANAGFASCICOLO15) DATA_INIZIO_ISCRIZIONE,
                     (SELECT DECODE(INPS.DATACESSAZIONE, kDataFineMax, NULL, ReturnData(INPS.DATACESSAZIONE)) 
                      FROM   ISWSCODICEINPS INPS 
                      WHERE  INPS.ID_ISWSRESPANAGFASCICOLO15 = RAF.IDISWSRESPANAGFASCICOLO15) DATA_CESSAZIONE_ISCRIZIONE,
                     DECODE(TIPOCOLLABORAZIONE||UNITAMISURA,'00' ,6,'01' ,6,'02' ,6,'03' ,6,'61',6,'62',6,'63',6,'10',6,'11',1,'12',2,'13',2,
                                                            '14',1,'15',2,'16',2,'20',6,'21',3,'22',4,'23',4,'24',3,'25',4,'26',4,'30',6,
                                                            '31',6,'32',6,'33',6,'34',6,'35',6,'36',6) ID_CLASSE_MANODOPERA,TIPOLAVORATORI, 
                     CASE WHEN TO_NUMBER(REPLACE(QUANTITA,'.',',')) > 365 THEN 365 ELSE TO_NUMBER(REPLACE(QUANTITA,'.',',')) END QUANTITA
              FROM   ISWSLAVORO L,ISWSRESPANAGFASCICOLO15 RAF,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
              WHERE  RAF.ID_CHIAMATA          = pIdChiamata
              AND    AA.CUAA                  = RAF.CUAA
              AND    AA.DATA_FINE_VALIDITA    IS NULL
              AND    L.IDCHIAMATA             = RAF.ID_CHIAMATA) LOOP

    bInsMan     := FALSE;
    bInsDettMan := FALSE;

    BEGIN
      SELECT *
      INTO   recMan
      FROM   SMRGAA.DB_MANODOPERA
      WHERE  ID_AZIENDA         = rec.ID_AZIENDA
      AND    DATA_FINE_VALIDITA IS NULL;

      nIdManodopera := recMan.ID_MANODOPERA;

      IF NVL(recMan.CODICE_INPS,'0')                    != NVL(rec.CODICE_INPS,'0') OR
         NVL(recMan.ID_FORMA_CONDUZIONE,0)              != NVL(rec.ID_FORMA_CONDUZIONE,0) OR
         NVL(recMan.MATRICOLA_INAIL,'0')                != NVL(rec.MATRICOLA_INAIL,'0') OR
         NVL(recMan.ID_TIPO_ISCRIZIONE_INPS,0)          != NVL(rec.ID_TIPO_ISCRIZIONE_INPS,0) OR
         NVL(recMan.DATA_INIZIO_ISCRIZIONE,SYSDATE)     != NVL(rec.DATA_INIZIO_ISCRIZIONE,SYSDATE) OR
         NVL(recMan.DATA_CESSAZIONE_ISCRIZIONE,SYSDATE) != NVL(rec.DATA_CESSAZIONE_ISCRIZIONE,SYSDATE) THEN

        UPDATE SMRGAA.DB_MANODOPERA
        SET    DATA_AGGIORNAMENTO      = SYSDATE, 
               DATA_FINE_VALIDITA      = SYSDATE, 
               ID_UTENTE_AGGIORNAMENTO = rec.ID_UTENTE_AGGIORNAMENTO
        WHERE  ID_AZIENDA              = rec.ID_AZIENDA
        AND    DATA_FINE_VALIDITA      IS NULL;

        bInsMan     := TRUE;
        bInsDettMan := TRUE;
      END IF;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN  
        bInsMan := TRUE;
    END;

    IF bInsMan THEN
      INSERT INTO SMRGAA.DB_MANODOPERA
      (ID_MANODOPERA,ID_AZIENDA,CODICE_INPS,DATA_INIZIO_VALIDITA,DATA_FINE_VALIDITA,DATA_AGGIORNAMENTO,
       ID_UTENTE_AGGIORNAMENTO,ID_FORMA_CONDUZIONE,MATRICOLA_INAIL,ID_TIPO_ISCRIZIONE_INPS,DATA_INIZIO_ISCRIZIONE,
       DATA_CESSAZIONE_ISCRIZIONE)
      VALUES
      (SMRGAA.SEQ_MANODOPERA.NEXTVAL,rec.ID_AZIENDA,rec.CODICE_INPS,rec.DATA_INIZIO_VALIDITA,rec.DATA_FINE_VALIDITA,rec.DATA_AGGIORNAMENTO,
       rec.ID_UTENTE_AGGIORNAMENTO,rec.ID_FORMA_CONDUZIONE,rec.MATRICOLA_INAIL,rec.ID_TIPO_ISCRIZIONE_INPS,rec.DATA_INIZIO_ISCRIZIONE,
       rec.DATA_CESSAZIONE_ISCRIZIONE)
      RETURNING ID_MANODOPERA INTO nIdManodopera;
    END IF;

    idMan.EXTEND;
    idMan(idMan.COUNT) := OBJ_ID(nIdManodopera);

    idClasse.EXTEND;
    idClasse(idClasse.COUNT) := OBJ_ID(rec.ID_CLASSE_MANODOPERA);

    nUomini        := 0;
    nDonne         := 0;
    nGiornateAnnue := 0;

    if rec.TIPOLAVORATORI in (0,3,4) then
      nGiornateAnnue := rec.QUANTITA;
    end if;

    if rec.TIPOLAVORATORI = 1 then
      nUomini := rec.QUANTITA;
    end if;

    if rec.TIPOLAVORATORI = 2 then
      nDonne := rec.QUANTITA;
    end if;

    SELECT COUNT(*)
    INTO   nCont
    FROM   SMRGAA.DB_DETTAGLIO_MANODOPERA
    WHERE  ID_MANODOPERA        = nIdManodopera
    AND    ID_CLASSE_MANODOPERA = rec.ID_CLASSE_MANODOPERA;

    IF nCont = 0 OR bInsDettMan THEN 
      INSERT INTO SMRGAA.DB_DETTAGLIO_MANODOPERA
      (ID_DETTAGLIO_MANODOPERA, ID_MANODOPERA, ID_CLASSE_MANODOPERA, UOMINI, DONNE,GIORNATE_ANNUE)
      VALUES
      (SMRGAA.SEQ_DETTAGLIO_MANODOPERA.NEXTVAL, nIdManodopera, rec.ID_CLASSE_MANODOPERA, nUomini, nDonne,nGiornateAnnue);
    ELSE
      UPDATE SMRGAA.DB_DETTAGLIO_MANODOPERA
      SET    UOMINI               = NVL(UOMINI,0) + nUomini, 
             DONNE                = NVL(DONNE,0) + nDonne, 
             GIORNATE_ANNUE       = NVL(GIORNATE_ANNUE,0) + nGiornateAnnue
      WHERE  ID_MANODOPERA        = nIdManodopera
      AND    ID_CLASSE_MANODOPERA = rec.ID_CLASSE_MANODOPERA;           
    END IF;
  END LOOP;

  -- delete DB_DETTAGLIO_MANODOPERA x l'ID_MANODOPERA di tutti ID_CLASSE_MANODOPERA non trattati
  DELETE SMRGAA.DB_DETTAGLIO_MANODOPERA
  WHERE  ID_MANODOPERA        IN (SELECT ID
                                  FROM   TABLE(idMan))
  AND    ID_CLASSE_MANODOPERA NOT IN (SELECT ID
                                      FROM   TABLE(idClasse)); 

  DELETE SMRGAA.DB_DETTAGLIO_MANODOPERA
  WHERE  UOMINI         = 0 
  AND    DONNE          = 0 
  AND    GIORNATE_ANNUE = 0;  

EXCEPTION
  WHEN OTHERS THEN
    pCodErrore  := 1;
    pDescErrore := 'Errore generico nel popolamento dei dati manodopera = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
END popolaDatiManodopera;

PROCEDURE popolaMotoriAgricoli(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                               pCodErrore   OUT NUMBER,
                               pDescErrore  OUT VARCHAR2) IS

  nIdMacchina   SMRGAA.DB_MACCHINA.ID_MACCHINA%TYPE;
  bInsMacchina  BOOLEAN DEFAULT TRUE;
  nIdUte        NUMBER;
  nIdMarca      NUMBER;
  recMacc       SMRGAA.DB_MACCHINA%ROWTYPE;
  bInsPosMacc   BOOLEAN := FALSE;
  recPosMacc    SMRGAA.DB_POSSESSO_MACCHINA%ROWTYPE;
  recNumTarga   SMRGAA.DB_NUMERO_TARGA%ROWTYPE;
  idMacc        TBL_ID := TBL_ID();
  nIdAzienda    SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE;
BEGIN
  pCodErrore  := 0;
  pDescErrore := '';

  FOR rec IN (SELECT MARCA DESCRIZIONE, NULL MATRICE, 'S' FLAG_PRINCIPALE
              FROM   ISWSMACCHINA ISWS
              WHERE  ISWS.IDCHIAMATA = pIdChiamata
              AND    MARCA           IS NOT NULL
              AND    NOT EXISTS      (SELECT 'X' 
                                      FROM   SMRGAA.DB_TIPO_MARCA 
                                      WHERE  DESCRIZIONE = ISWS.MARCA)) LOOP

    INSERT INTO SMRGAA.DB_TIPO_MARCA
    (ID_MARCA, DESCRIZIONE, MATRICE, FLAG_PRINCIPALE)
    VALUES
    (SMRGAA.SEQ_DB_TIPO_MARCA.NEXTVAL, rec.DESCRIZIONE, rec.MATRICE, rec.FLAG_PRINCIPALE);
  END LOOP;

  FOR rec IN (SELECT distinct
                     WRK.ID_GENERE_MACCHINA_SIAP ID_GENERE_MACCHINA,WRK.ID_CATEGORIA ID_CATEGORIA,ISWS.MODELLO TIPO_MACCHINA,
                     ISWS.TELAIO MATRICOLA_TELAIO,AA.ID_AZIENDA,
                     DECODE(ISWS.CARBURANTE, 'B', 1, 'G', 2, 'P', 5, 'N', 3, NULL) ID_ALIMENTAZIONE,
                     DECODE(ISWS.TRAZIONE, 'C', 1, 'DT', 4, 'F', 3, 'M', 7, 'R', 2, 'SC', 5, NULL, 6) ID_TRAZIONE,
                     DECODE(ISWS.TIPOTARGA, 'F', 4, 'S', 3, 'R', 2, 'T', 1, NULL) ID_TARGA,
                     REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(TRIM(REPLACE(ISWS.TARGA, ' ', '')),'-',NULL),'.',NULL),'*',NULL),'/',NULL),'_',
                             NULL) NUMERO_TARGA,
                     SUBSTR((SELECT MAX(provincia_competenza) 
                             FROM   SMRGAA.DB_ANAGRAFICA_AZIENDA AA
                             WHERE  AA.CUAA               = ISWS.CUAA
                             AND    AA.DATA_FINE_VALIDITA IS NULL), 1, 3) ID_PROVINCIA_ANA,'S' FLAG_TARGA_NUOVA,
                     MAX(DECODE(ISWS.DATAISCRIZIONE, kDataFineMax, NULL, NULL, NULL, ReturnData(ISWS.DATAISCRIZIONE))) DATA_PRIMA_IMMATRICOLAZIONE,
                     DECODE(ISWS.FORMAPOSSESSO, 'L', 4, 'N', 3, 'P', 1, 'PU', 1, 'U', 3, 'A ', 8, 'M ', 8, NULL, 8) ID_TIPO_FORMA_POSSESSO,
                     100 PERCENTUALE_POSSESSO,'9999' ID_SCARICO,
                     MAX(DECODE(ISWS.DataIscrizione, kDataFineMax, SYSDATE, NULL, SYSDATE, ReturnData(ISWS.DATAISCRIZIONE))) DATA_CARICO, -- bot nullable
                     DECODE(ISWS.DataCessazione, kDataFineMax, NULL, NULL, NULL, ReturnData(ISWS.DataCessazione)) DATA_SCARICO,
                     SYSDATE DATA_INIZIO_VALIDITA,kIDUtente EXT_ID_UTENTE_AGGIORNAMENTO,SYSDATE DATA_AGGIORNAMENTO,isws.marca
              FROM   ISWSMACCHINA ISWS, WRK_GENERE_MACCHINA WRK,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,ISWSRESPANAGFASCICOLO15 RAF
              WHERE  AA.CUAA                       = RAF.CUAA
              AND    AA.DATA_FINE_VALIDITA         IS NULL
              AND    RAF.ID_CHIAMATA               = pIdChiamata
              AND    WRK.CODICE_TIPO_MACCHINA_SIAN = ISWS.TIPOMACCHINA
              AND    RAF.ID_CHIAMATA               = ISWS.IDCHIAMATA
              -- x evitare l'errore di chiave duplicata AK_DB_POSSESSO_MACCHINA
              GROUP BY WRK.ID_GENERE_MACCHINA_SIAP,WRK.ID_CATEGORIA,ISWS.MODELLO,ISWS.TELAIO,AA.ID_AZIENDA,ISWS.CARBURANTE,ISWS.TRAZIONE,
                       ISWS.TIPOTARGA,ISWS.TARGA,ISWS.CUAA,ISWS.FORMAPOSSESSO,ISWS.DATACESSAZIONE,ISWS.MARCA) LOOP

    bInsMacchina := TRUE;
    bInsPosMacc  := FALSE;
    nIdAzienda   := rec.ID_AZIENDA;

    IF rec.ID_GENERE_MACCHINA IS NOT NULL THEN
      BEGIN
        SELECT DTM.ID_MARCA
        INTO   nIdMarca
        FROM   SMRGAA.DB_TIPO_MARCA DTM
        WHERE  UPPER(DTM.DESCRIZIONE) = UPPER(rec.MARCA);
      EXCEPTION
        WHEN OTHERS THEN
          SELECT MIN(DTM.ID_MARCA)
          INTO   nIdMarca
          FROM   SMRGAA.DB_TIPO_MARCA DTM
          WHERE  UPPER(DTM.DESCRIZIONE) = UPPER(rec.MARCA)
          AND    DTM.FLAG_PRINCIPALE    = 'S';
      END;

      -- CONTROLLARE SE ESISTE IL RECORD A PARITA' ID_GENERE_MACCHINA,TIPO_MACCHINA, ID_CATEGORIA, ID_MARCA,MATRICOLA_TELAIO ED AGGIORNO SE E'
      -- CAMBIATO QUALCOSA
      BEGIN
        SELECT *
        INTO   recMacc
        FROM   SMRGAA.DB_MACCHINA
        WHERE  ID_GENERE_MACCHINA        = rec.ID_GENERE_MACCHINA
        AND    NVL(TIPO_MACCHINA,'0')    = NVL(rec.TIPO_MACCHINA,'0')
        AND    NVL(ID_CATEGORIA,0)       = NVL(rec.ID_CATEGORIA,0) 
        AND    NVL(ID_MARCA,0)           = NVL(nIdMarca,0)
        AND    NVL(MATRICOLA_TELAIO,'0') = NVL(rec.MATRICOLA_TELAIO,'0')
        AND    NVL(ID_ALIMENTAZIONE,0)  != NVL(rec.ID_ALIMENTAZIONE,0)  
        AND    NVL(ID_TRAZIONE,0)       != NVL(rec.ID_TRAZIONE,0)  
        AND    ROWNUM                    = 1;

        nIdMacchina := recMacc.ID_MACCHINA;

        UPDATE SMRGAA.DB_MACCHINA
        SET    DATA_AGGIORNAMENTO          = rec.DATA_AGGIORNAMENTO,
               EXT_ID_UTENTE_AGGIORNAMENTO = rec.EXT_ID_UTENTE_AGGIORNAMENTO
        WHERE  ID_MACCHINA                 = nIdMacchina;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN    
          INSERT INTO SMRGAA.DB_MACCHINA
          (ID_MACCHINA, ID_GENERE_MACCHINA, ID_CATEGORIA, TIPO_MACCHINA, MATRICOLA_TELAIO, ID_MARCA, 
           ID_ALIMENTAZIONE, ID_TRAZIONE,EXT_ID_UTENTE_AGGIORNAMENTO, DATA_AGGIORNAMENTO)
          VALUES
          (SMRGAA.SEQ_DB_MACCHINA.NEXTVAL,rec.ID_GENERE_MACCHINA,rec.ID_CATEGORIA,rec.TIPO_MACCHINA,rec.MATRICOLA_TELAIO,nIdMarca,
           rec.ID_ALIMENTAZIONE,rec.ID_TRAZIONE,rec.EXT_ID_UTENTE_AGGIORNAMENTO,rec.DATA_AGGIORNAMENTO)
          RETURNING ID_MACCHINA INTO nIdMacchina;
      END;

      BEGIN
        SELECT ID_UTE
        INTO   nIdUte
        FROM   SMRGAA.DB_UTE
        WHERE  ID_AZIENDA         = rec.ID_AZIENDA
        AND    DATA_FINE_ATTIVITA IS NULL;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN  
          nIdUte := NULL;
      END;

      IF nIdUte IS NOT NULL THEN
        -- CONTROLLARE SE ESISTE A PARITA' DI ID_UTE,ID_MACCHINA,ID_TIPO_FORMA_POSSESSO,DATA FINE NULL
        -- SE ESISTE CHIUDO ED INSERISCO SE E' CAMBIATO QUALCOSA
        BEGIN
          SELECT *
          INTO   recPosMacc
          FROM   SMRGAA.DB_POSSESSO_MACCHINA
          WHERE  ID_UTE                 = nIdUte
          AND    ID_MACCHINA            = nIdMacchina
          AND    ID_TIPO_FORMA_POSSESSO = rec.ID_TIPO_FORMA_POSSESSO
          AND    DATA_FINE_VALIDITA     IS NULL;

          idMacc.EXTEND;
          idMacc(idMacc.COUNT) := OBJ_ID(nIdMacchina);

          IF NVL(recPosMacc.PERCENTUALE_POSSESSO,0) != NVL(rec.PERCENTUALE_POSSESSO,0) OR
             NVL(recPosMacc.ID_SCARICO,0)           != NVL(rec.ID_SCARICO,0) OR
             recPosMacc.DATA_CARICO                 != rec.DATA_CARICO OR
             NVL(recPosMacc.DATA_SCARICO,SYSDATE)   != NVL(rec.DATA_SCARICO,SYSDATE) THEN

            BEGIN
              UPDATE SMRGAA.DB_POSSESSO_MACCHINA
              SET    DATA_FINE_VALIDITA          = SYSDATE, 
                     DATA_AGGIORNAMENTO          = rec.DATA_AGGIORNAMENTO, 
                     EXT_ID_UTENTE_AGGIORNAMENTO = rec.EXT_ID_UTENTE_AGGIORNAMENTO
              WHERE  ID_POSSESSO_MACCHINA        = recPosMacc.ID_POSSESSO_MACCHINA;
            EXCEPTION
              WHEN OTHERS THEN
                -- per gestire il caso in cui per la stessa azienda arrivano piu' macchine con le stesse caratteristiche
                BEGIN
                  UPDATE SMRGAA.DB_POSSESSO_MACCHINA
                  SET    DATA_FINE_VALIDITA          = SYSDATE + INTERVAL '1' SECOND, 
                         DATA_AGGIORNAMENTO          = rec.DATA_AGGIORNAMENTO, 
                         EXT_ID_UTENTE_AGGIORNAMENTO = rec.EXT_ID_UTENTE_AGGIORNAMENTO
                  WHERE  ID_POSSESSO_MACCHINA        = recPosMacc.ID_POSSESSO_MACCHINA;
                EXCEPTION
                  WHEN OTHERS THEN
                    pCodErrore  := 1;
                    pDescErrore := 'Errore nell''aggiornamento DB_POSSESSO_MACCHINA, ID_MACCHINA = '||nIdMacchina||', ID_UTE = '||nIdUte||
                                   ', ID_TIPO_FORMA_POSSESSO = '||rec.ID_TIPO_FORMA_POSSESSO||' , DATA_CARICO = '||rec.DATA_CARICO||
                                   ', DATA_SCARICO = '||rec.DATA_SCARICO;
                    RETURN;
                END;
            END;

            bInsPosMacc := TRUE;
          END IF;             
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            bInsPosMacc := TRUE;
        END;

        IF bInsPosMacc THEN
          INSERT INTO SMRGAA.DB_POSSESSO_MACCHINA
          (ID_POSSESSO_MACCHINA,ID_UTE,ID_MACCHINA,ID_TIPO_FORMA_POSSESSO,PERCENTUALE_POSSESSO,ID_SCARICO,
           DATA_CARICO,DATA_SCARICO,DATA_INIZIO_VALIDITA,EXT_ID_UTENTE_AGGIORNAMENTO,DATA_AGGIORNAMENTO,FLAG_VALIDA)
          VALUES
          (SMRGAA.SEQ_DB_POSSESSO_MACCHINA.NEXTVAL,nIdUte,nIdMacchina,rec.ID_TIPO_FORMA_POSSESSO,rec.PERCENTUALE_POSSESSO,rec.ID_SCARICO,
           rec.DATA_CARICO,rec.DATA_SCARICO,rec.DATA_INIZIO_VALIDITA,rec.EXT_ID_UTENTE_AGGIORNAMENTO,rec.DATA_AGGIORNAMENTO,'S');

          idMacc.EXTEND;
          idMacc(idMacc.COUNT) := OBJ_ID(nIdMacchina); 
        END IF;
      END IF;
    ELSE
      bInsMacchina := FALSE;
    END IF;

    IF bInsMacchina THEN
      IF rec.NUMERO_TARGA IS NOT NULL AND LENGTH(rec.NUMERO_TARGA) <= 8 THEN

        --DBMS_OUTPUT.PUT_LINE('nIdMacchina - ID_TARGA - NUMERO_TARGA'||' --- '||nIdMacchina||' - '||rec.ID_TARGA||' - '||rec.NUMERO_TARGA);

        -- SE ESISTE A PARITA' DI ID_MACCHINA, ID_TARGA, NUMERO_TARGA AGGIORNARE SE E' CAMBIATO QUALCOSA
        BEGIN
          SELECT *
          INTO   recNumTarga
          FROM   SMRGAA.DB_NUMERO_TARGA
          WHERE  ID_MACCHINA  = nIdMacchina  
          AND    ID_TARGA     = rec.ID_TARGA
          AND    NUMERO_TARGA = rec.NUMERO_TARGA;

          IF recNumTarga.ID_PROVINCIA                             != rec.ID_PROVINCIA_ANA OR
             NVL(recNumTarga.DATA_PRIMA_IMMATRICOLAZIONE,SYSDATE) != NVL(rec.DATA_PRIMA_IMMATRICOLAZIONE,SYSDATE) THEN

            UPDATE SMRGAA.DB_NUMERO_TARGA
            SET    ID_PROVINCIA                = rec.ID_PROVINCIA_ANA,
                   DATA_PRIMA_IMMATRICOLAZIONE = rec.DATA_PRIMA_IMMATRICOLAZIONE,
                   EXT_ID_UTENTE_AGGIORNAMENTO = rec.EXT_ID_UTENTE_AGGIORNAMENTO,
                   DATA_AGGIORNAMENTO          = rec.DATA_AGGIORNAMENTO
            WHERE  ID_NUMERO_TARGA             = recNumTarga.ID_NUMERO_TARGA;
          END IF;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            INSERT INTO SMRGAA.DB_NUMERO_TARGA
            (ID_NUMERO_TARGA, ID_MACCHINA, ID_TARGA, NUMERO_TARGA, ID_PROVINCIA, FLAG_TARGA_NUOVA, 
             DATA_PRIMA_IMMATRICOLAZIONE, EXT_ID_UTENTE_AGGIORNAMENTO, DATA_AGGIORNAMENTO)
            VALUES
            (SMRGAA.SEQ_DB_NUMERO_TARGA.NEXTVAL,nIdMacchina,rec.ID_TARGA,rec.NUMERO_TARGA,rec.ID_PROVINCIA_ANA,rec.FLAG_TARGA_NUOVA,
             rec.DATA_PRIMA_IMMATRICOLAZIONE,rec.EXT_ID_UTENTE_AGGIORNAMENTO,rec.DATA_AGGIORNAMENTO);
        END;
      ELSE
        bInsMacchina := FALSE;
      END IF;
    END IF;
  END LOOP;

  -- mi salvo tutti gli id_macchina dell'azienda e setto la DATA_SCARICO = sysdate - 1  sulla DB_POSSESSO_MACCHINA
  UPDATE SMRGAA.DB_POSSESSO_MACCHINA
  SET    DATA_SCARICO                = SYSDATE - 1, 
         DATA_AGGIORNAMENTO          = SYSDATE, 
         EXT_ID_UTENTE_AGGIORNAMENTO = kIDUtente
  WHERE  ID_UTE                      IN (SELECT ID_UTE
                                         FROM   SMRGAA.DB_UTE
                                         WHERE  ID_AZIENDA = nIdAzienda)
  AND    ID_MACCHINA                 NOT IN (SELECT ID
                                             FROM   TABLE(idMacc));

EXCEPTION
  WHEN OTHERS THEN
    pCodErrore  := 1;
    pDescErrore := 'Errore generico nel popolamento dei motori agricoli = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
END popolaMotoriAgricoli;

PROCEDURE popolaFabbricati(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                           pCodErrore   OUT NUMBER,
                           pDescErrore  OUT VARCHAR2) IS

  nIdFabbricato            SMRGAA.DB_FABBRICATO.ID_FABBRICATO%TYPE;
  nIdParticella            NUMBER;
  nZonaAlt                 SMRGAA.COMUNE.ZONAALT%TYPE;
  nIdConduzioneParticella  NUMBER;
  recCatalogoAll           typeCatalogoAll;
  nParticella              SMRGAA.DB_STORICO_PARTICELLA.PARTICELLA%TYPE;
BEGIN
  pCodErrore  := 0;
  pDescErrore := '';

  -- CHIUSURA DB_FABBRICATO X TUTTI I RECORD DELLE UTE DELL'AZIENDA 
  UPDATE SMRGAA.DB_FABBRICATO
  SET    DATA_AGGIORNAMENTO      = SYSDATE, 
         ID_UTENTE_AGGIORNAMENTO = kIDUtente, 
         DATA_FINE_VALIDITA      = SYSDATE
  WHERE  ID_UTE                  IN (SELECT U.ID_UTE
                                     FROM   SMRGAA.DB_UTE U,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,ISWSRESPANAGFASCICOLO15 RAF
                                     WHERE  U.ID_AZIENDA          = AA.ID_AZIENDA
                                     AND    AA.CUAA               = RAF.CUAA
                                     AND    AA.DATA_FINE_VALIDITA IS NULL
                                     AND    RAF.ID_CHIAMATA       = pIdChiamata);

  FOR rec IN (SELECT TF.ID_TIPOLOGIA_FABBRICATO,TF.ID_FORMA_FABBRICATO,NULL DENOMINAZIONE,TF.USO_SUOLO_DEFAULT,
                     (F.SUPERFICIECOPERTA + F.SUPERFICIESCOPERTA) SUPERFICIE,NULL ANNO_COSTRUZIONE,F.VOLUME DIMENSIONE,0 LUNGHEZZA,0 LARGHEZZA,
                     SYSDATE DATA_AGGIORNAMENTO,0 ALTEZZA,NULL UTM_X,NULL UTM_Y,kIDUtente ID_UTENTE_AGGIORNAMENTO,
                     (SELECT MAX(U.ID_UTE) 
                      FROM   SMRGAA.DB_UTE U
                      WHERE  U.ID_AZIENDA         = AA.ID_AZIENDA
                      AND    U.DATA_FINE_ATTIVITA IS NULL) ID_UTE,SYSDATE DATA_INIZIO_VALIDITA,NULL DATA_FINE_VALIDITA,NULL ID_COLTURA_SERRA,
                     NULL MESI_RISCALDAMENTO_SERRA,NULL ORE_RISCALDAMENTO_SERRA,NULL DICHIARAZIONE_RIPRISTINATA,
                     NULL ID_DICHIARAZIONE_CONSISTENZA,F.VOLUME VOLUME_UTILE_PRESUNTO,SUPERFICIECOPERTA SUPERFICIE_COPERTA,
                     SUPERFICIESCOPERTA SUPERFICIE_SCOPERTA,0 SUPERFICIE_SCOPERTA_EXTRA,PROVINCIA||COMUNE istatComune,SEZIONE,
                     TO_NUMBER(FOGLIO) FOGLIO,
                     CASE WHEN NVL(LENGTH(TRIM(TRANSLATE(REPLACE(PARTICELLA,' '),'1234567890',' '))),0) = 0 THEN PARTICELLA
                          ELSE REPLACE(PARTICELLA,'DEM','999') END PARTICELLA_NUM,
                     trim(SUBALTERNO) SUBALTERNO,
                     TO_NUMBER(nvl(TIPOCONDUZIONE,1)) ID_TITOLO_POSSESSO,F.CODICEDESTINAZIONEUSO, F.CODICEOCCUPAZIONESUOLO, 
                     F.CODICEOCCUPAZIONEVARIETA,F.CODICEQUALITA, F.CODICEUSO,
                     NVL(RAF.DATASCHEDAVALIDAZIONE,RAF.DATAVALIDAZFASCICOLO) DATASCHEDAVALIDAZIONE
              FROM   ISWSFABBRICATOFS6 F,ISWSRESPANAGFASCICOLO15 RAF,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,WRK_TIPO_FABBRICATO TF
              WHERE  AA.CUAA               = RAF.CUAA
              AND    AA.DATA_FINE_VALIDITA IS NULL
              AND    RAF.ID_CHIAMATA       = pIdChiamata
              AND    F.ID_CHIAMATA         = RAF.ID_CHIAMATA
              AND    F.TIPO                = TF.TIPO_FABBRICATO_SIAN) LOOP

    IF rec.ID_UTE IS NOT NULL THEN
      BEGIN
        nParticella := TO_NUMBER(rec.PARTICELLA_NUM);
      EXCEPTION
        WHEN OTHERS THEN
          CONTINUE;
      END;

      INSERT INTO SMRGAA.DB_FABBRICATO
      (ID_FABBRICATO,ID_TIPOLOGIA_FABBRICATO,ID_FORMA_FABBRICATO,DENOMINAZIONE,SUPERFICIE,ANNO_COSTRUZIONE,
       DIMENSIONE,LUNGHEZZA,LARGHEZZA,DATA_AGGIORNAMENTO,ALTEZZA,UTM_X,UTM_Y,NOTE,ID_UTENTE_AGGIORNAMENTO,
       ID_UTE,DATA_INIZIO_VALIDITA,DATA_FINE_VALIDITA,ID_COLTURA_SERRA,MESI_RISCALDAMENTO_SERRA,ORE_RISCALDAMENTO_SERRA,
       DICHIARAZIONE_RIPRISTINATA,ID_DICHIARAZIONE_CONSISTENZA,VOLUME_UTILE_PRESUNTO,SUPERFICIE_COPERTA,SUPERFICIE_SCOPERTA,
       SUPERFICIE_SCOPERTA_EXTRA)
      VALUES
      (SMRGAA.SEQ_FABBRICATO.NEXTVAL,rec.ID_TIPOLOGIA_FABBRICATO,rec.ID_FORMA_FABBRICATO,rec.DENOMINAZIONE,rec.SUPERFICIE,rec.ANNO_COSTRUZIONE,
       rec.DIMENSIONE,rec.LUNGHEZZA,rec.LARGHEZZA,rec.DATA_AGGIORNAMENTO,rec.ALTEZZA,rec.UTM_X,rec.UTM_Y,NULL,rec.ID_UTENTE_AGGIORNAMENTO,
       rec.ID_UTE,rec.DATA_INIZIO_VALIDITA,rec.DATA_FINE_VALIDITA,rec.ID_COLTURA_SERRA,rec.MESI_RISCALDAMENTO_SERRA,rec.ORE_RISCALDAMENTO_SERRA,
       rec.DICHIARAZIONE_RIPRISTINATA,rec.ID_DICHIARAZIONE_CONSISTENZA,rec.VOLUME_UTILE_PRESUNTO,rec.SUPERFICIE_COPERTA,rec.SUPERFICIE_SCOPERTA,
       rec.SUPERFICIE_SCOPERTA_EXTRA)
      RETURNING ID_FABBRICATO INTO nIdFabbricato;

      BEGIN
        SELECT SP.ID_PARTICELLA
        INTO   nIdParticella                        
        FROM   SMRGAA.DB_STORICO_PARTICELLA SP
        WHERE  SP.COMUNE              = rec.istatComune
        AND    NVL(SP.SEZIONE,'-')    = NVL(rec.SEZIONE,'-')
        AND    SP.FOGLIO              = rec.FOGLIO
        AND    SP.PARTICELLA          = nParticella
        AND    NVL(SP.SUBALTERNO,'-') = NVL(rec.SUBALTERNO,'-')
        AND    SP.DATA_FINE_VALIDITA  IS NULL;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          INSERT INTO SMRGAA.DB_PARTICELLA 
          (ID_PARTICELLA, DATA_CREAZIONE, DATA_CESSAZIONE,BIOLOGICO, DATA_INIZIO_VALIDITA, FLAG_SCHEDARIO,FLAG_INVIO_SITI) 
          VALUES 
          (SMRGAA.SEQ_PARTICELLA.NEXTVAL,SYSDATE,null,'N',rec.DATA_INIZIO_VALIDITA,'N','N')
          RETURNING ID_PARTICELLA INTO nIdParticella;

          BEGIN
            SELECT ZONAALT
            INTO   nZonaAlt                        
            FROM   SMRGAA.COMUNE C
            WHERE  C.ISTAT_COMUNE = rec.istatComune;
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              pCodErrore  := 1;
              pDescErrore := 'Il seguente comune con codice istat '||rec.istatComune||' non esiste';
              RETURN;
          END;

          INSERT INTO SMRGAA.DB_STORICO_PARTICELLA 
          (ID_PARTICELLA, ID_STORICO_PARTICELLA, SEZIONE,COMUNE, DATA_INIZIO_VALIDITA, FOGLIO,DATA_FINE_VALIDITA, 
           PARTICELLA, SUP_CATASTALE,SUBALTERNO, ID_AREA_A, ID_ZONA_ALTIMETRICA,FLAG_IRRIGABILE, ID_AREA_B, ID_CASO_PARTICOLARE,
           ID_AREA_C, ID_AREA_D, DATA_AGGIORNAMENTO, ID_UTENTE_AGGIORNAMENTO, ID_CAUSALE_MOD_PARTICELLA, FLAG_CAPTAZIONE_POZZI,MOTIVO_MODIFICA,
           SUP_NON_ELEGGIBILE,SUP_NE_BOSCO_ACQUE_FABBRICATO,SUP_NE_FORAGGIERE, SUP_EL_FRUTTA_GUSCIO, SUP_EL_PRATO_PASCOLO,SUP_EL_COLTURE_MISTE,
           SUP_COLTIVAZ_ARBOREA_CONS, SUP_COLTIVAZ_ARBOREA_SPEC,DATA_FOTO, ID_FONTE, TIPO_FOTO,ID_DOCUMENTO, ID_IRRIGAZIONE, 
           ID_DOCUMENTO_PROTOCOLLATO,ID_FASCIA_FLUVIALE, ID_AREA_G, ID_AREA_H,SUPERFICIE_GRAFICA, ID_POTENZIALITA_IRRIGUA, ID_TERRAZZAMENTO, 
           ID_ROTAZIONE_COLTURALE, ID_AREA_L, ID_AREA_I,ID_AREA_M, PERCENTUALE_PENDENZA_MEDIA, GRADI_PENDENZA_MEDIA,GRADI_ESPOSIZIONE_MEDIA, 
           METRI_ALTITUDINE_MEDIA, ID_METODO_IRRIGUO) 
          VALUES 
          (nIdParticella,SMRGAA.SEQ_STORICO_PARTICELLA.NEXTVAL,rec.SEZIONE,rec.istatComune,rec.DATA_INIZIO_VALIDITA,rec.FOGLIO,null,
           nParticella,(NVL(rec.SUPERFICIE_COPERTA,0)+NVL(rec.SUPERFICIE_SCOPERTA,0)) / 10000,rec.SUBALTERNO,null,nZonaAlt,'N',null,3,
           null,null,SYSDATE,kIDUtente,null,'N',null,
           null,null,null,null,null,null,null,
           null,null,null,null,
           null,null,null,null,null,null,0,
           3,1,4,null,null,null,null,
           null,null,NULL,null); 
      END;

      BEGIN
        SELECT ID_CONDUZIONE_PARTICELLA
        INTO   nIdConduzioneParticella
        FROM   SMRGAA.DB_CONDUZIONE_PARTICELLA CP
        WHERE  CP.ID_PARTICELLA        = nIdParticella
        AND    CP.ID_UTE               = rec.ID_UTE
        AND    CP.ID_TITOLO_POSSESSO   = rec.ID_TITOLO_POSSESSO
        AND    CP.DATA_FINE_CONDUZIONE IS NULL;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          INSERT INTO SMRGAA.DB_CONDUZIONE_PARTICELLA 
          (ID_CONDUZIONE_PARTICELLA, ID_PARTICELLA, ID_TITOLO_POSSESSO,ID_UTE, 
           SUPERFICIE_CONDOTTA, FLAG_UTILIZZO_PARTE,DATA_INIZIO_CONDUZIONE,DATA_FINE_CONDUZIONE, NOTE,DATA_AGGIORNAMENTO, 
           ID_UTENTE_AGGIORNAMENTO, ESITO_CONTROLLO,DATA_ESECUZIONE, RECORD_MODIFICATO,DICHIARAZIONE_RIPRISTINATA,ID_DICHIARAZIONE_CONSISTENZA,
           SUPERFICIE_AGRONOMICA, PERCENTUALE_POSSESSO) 
          VALUES 
          (SMRGAA.SEQ_CONDUZIONE_PARTICELLA.NEXTVAL,nIdParticella,rec.ID_TITOLO_POSSESSO,rec.ID_UTE,
           (NVL(rec.SUPERFICIE_COPERTA,0)+NVL(rec.SUPERFICIE_SCOPERTA,0)) / 10000,null,rec.DATA_INIZIO_VALIDITA,NULL,null,SYSDATE,
           kIDUtente,null,null,null,null,null,
           null,100)
          RETURNING ID_CONDUZIONE_PARTICELLA INTO nIdConduzioneParticella;
      END;

      -- cerco CATALOGO MATRICE...                                  
      ReturnMatrice(rec.codiceOccupazioneSuolo,rec.CodiceOccupazioneVarieta,rec.CODICEDESTINAZIONEUSO,rec.CodiceUso,rec.CodiceQualita,
                    rec.DATASCHEDAVALIDAZIONE,rec.USO_SUOLO_DEFAULT,recCatalogoAll,pCodErrore,pDescErrore);

      INSERT INTO SMRGAA.DB_UTILIZZO_PARTICELLA 
      (ID_UTILIZZO_PARTICELLA, ID_UTILIZZO, ID_CONDUZIONE_PARTICELLA,
       SUPERFICIE_UTILIZZATA, DATA_AGGIORNAMENTO, ID_UTENTE_AGGIORNAMENTO,ANNO, NOTE,
       ID_CATALOGO_MATRICE,ID_TIPO_DETTAGLIO_USO,ID_VARIETA,ID_TIPO_EFA,
       ID_TIPO_PERIODO_SEMINA,DATA_INIZIO_DESTINAZIONE,DATA_FINE_DESTINAZIONE,ID_PRATICA_MANTENIMENTO,
       ID_SEMINA,
       ID_FASE_ALLEVAMENTO)  
      VALUES 
      (SMRGAA.SEQ_UTILIZZO_PARTICELLA.NEXTVAL,recCatalogoAll.IDUtilizzo,nIdConduzioneParticella,
       (NVL(rec.SUPERFICIE_COPERTA,0)+NVL(rec.SUPERFICIE_SCOPERTA,0)) / 10000,SYSDATE,kIDUtente,TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')),NULL,
      recCatalogoAll.IDCatalogMatrix,recCatalogoAll.IDDettUso,recCatalogoAll.IDVarieta,recCatalogoAll.IDTipoEfa,
      recCatalogoAll.IDPeriodoSemina,recCatalogoAll.dataIniSemina,recCatalogoAll.dataFineSemina,recCatalogoAll.IDPraticaMant,
      (SELECT ID_SEMINA FROM SMRGAA.DB_TIPO_SEMINA WHERE DATA_FINE_VALIDITA IS NULL AND FLAG_DEFAULT = 'S'),
      (SELECT ID_FASE_ALLEVAMENTO FROM SMRGAA.DB_TIPO_FASE_ALLEVAMENTO WHERE DATA_FINE_VALIDITA IS NULL AND FLAG_DEFAULT = 'S'));

      INSERT INTO SMRGAA.DB_FABBRICATO_PARTICELLA
      (ID_FABBRICATO_PARTICELLA, ID_FABBRICATO, ID_PARTICELLA, DATA_INIZIO_VALIDITA)
      VALUES
      (SMRGAA.SEQ_PARTICELLA_FABBRICATO.NEXTVAL,nIdFabbricato,nIdParticella,rec.DATA_INIZIO_VALIDITA); 
    END IF;
  END LOOP;
EXCEPTION
  WHEN OTHERS THEN
    pCodErrore  := 1;
    pDescErrore := 'Errore generico nel popolamento dei fabbricati = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
END popolaFabbricati;

PROCEDURE popolaUma(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                    pCodErrore   OUT NUMBER,
                    pDescErrore  OUT VARCHAR2) IS

  nIdDittaUma     SMRUMA.DB_DITTA_UMA.ID_DITTA_UMA%TYPE;
  nIdMarca        SMRUMA.DB_TIPO_MARCA.ID_MARCA%TYPE;
  nIdMacchina     SMRUMA.DB_MACCHINA.ID_MACCHINA%TYPE;
  nIdNumeroTarga  SMRUMA.DB_NUMERO_TARGA.ID_NUMERO_TARGA%TYPE;
  nIdUtilizzo     SMRUMA.DB_UTILIZZO.ID_UTILIZZO%TYPE;
BEGIN
  pCodErrore  := 0;
  pDescErrore := '';

  FOR rec IN (SELECT A.*,ROWNUM NUM_REC
              FROM   (SELECT DISTINCT WRK.ID_GENERE_MACCHINA_SIAP ID_GENERE_MACCHINA,WRK.ID_CATEGORIA ID_CATEGORIA,
                             ISWS.MODELLO TIPO_MACCHINA,ISWS.TELAIO MATRICOLA_TELAIO,AA.ID_AZIENDA,SEDELEG_COMUNE,
                             WRK.CODICE_TIPO_MACCHINA_SIAN,
                             DECODE(ISWS.CARBURANTE, 'B', 1, 'G', 2, 'P', 5, 'N', 3, NULL) ID_ALIMENTAZIONE,
                             DECODE(ISWS.TRAZIONE, 'C', 1, 'DT', 4, 'F', 3, 'M', 6, 'R', 2, 'SC', 5, NULL, 6) ID_TRAZIONE,
                             DECODE(ISWS.TIPOTARGA, 'F', 4, 'S', 3, 'R', 2, 'T', 1, NULL) ID_TARGA,
                             REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(TRIM(REPLACE(ISWS.TARGA, ' ', '')),'-',NULL),'.',NULL),'*',NULL),'/',NULL),'_',
                                     NULL) NUMERO_TARGA,
                             SUBSTR((SELECT MAX(provincia_competenza) 
                                     FROM   SMRGAA.DB_ANAGRAFICA_AZIENDA AA
                                     WHERE  AA.CUAA               = ISWS.CUAA
                                     AND    AA.DATA_FINE_VALIDITA IS NULL), 1, 3) ID_PROVINCIA_ANA,'S' FLAG_TARGA_NUOVA,
                             DECODE(ISWS.DATAISCRIZIONE, kDataFineMax, NULL, NULL, NULL, ReturnData(ISWS.DATAISCRIZIONE)) DATA_PRIMA_IMMATRICOLAZIONE,
                             DECODE(ISWS.FORMAPOSSESSO, 'L', 4, 'N', 3, 'P', 1, 'PU', 1, 'U', 3, 'A ', 8, 'M ', 8, NULL, 8) ID_TIPO_FORMA_POSSESSO,
                             100 PERCENTUALE_POSSESSO,'9999' ID_SCARICO,
                             DECODE(ISWS.DataIscrizione, kDataFineMax, SYSDATE, NULL, SYSDATE, ReturnData(ISWS.DATAISCRIZIONE)) DATA_CARICO, -- bot nullable
                             DECODE(ISWS.DataCessazione, kDataFineMax, NULL, NULL, NULL, ReturnData(ISWS.DataCessazione)) DATA_SCARICO,
                             isws.marca
                      FROM   ISWSMACCHINA ISWS, WRK_GENERE_MACCHINA WRK,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,ISWSRESPANAGFASCICOLO15 RAF
                      WHERE  AA.CUAA                       = RAF.CUAA
                      AND    AA.DATA_FINE_VALIDITA         IS NULL
                      AND    RAF.ID_CHIAMATA               = pIdChiamata
                      AND    WRK.CODICE_TIPO_MACCHINA_SIAN = ISWS.TIPOMACCHINA
                      AND    RAF.ID_CHIAMATA               = ISWS.IDCHIAMATA) A) LOOP

    IF rec.ID_GENERE_MACCHINA IS NULL THEN
      pCodErrore  := 1;
      pDescErrore := 'Errore nel reperimento del genere macchina, Tipo Macchina = '||rec.CODICE_TIPO_MACCHINA_SIAN;
      RETURN;
    END IF;

    BEGIN
      SELECT DU.ID_DITTA_UMA
      INTO   nIdDittaUma
      FROM   SMRUMA.DB_DITTA_UMA DU
      WHERE  DU.EXT_ID_AZIENDA  = rec.ID_AZIENDA
      AND    DU.DATA_CESSAZIONE IS NULL;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        INSERT INTO SMRUMA.DB_DITTA_UMA
        (ID_DITTA_UMA, EXT_ID_AZIENDA, EXT_PROVINCIA_UMA, TIPO_DITTA, 
         DITTA_UMA,DATA_ISCRIZIONE, DATA_CESSAZIONE, EXT_ID_UTENTE_AGGIORNAMENTO,DATA_AGGIORNAMENTO, ID_DITTA_UMA_PROV, EXT_CUAA_AZIENDA_DEST, 
         RIMANENZA_CEDUTA)
        VALUES
        (SMRUMA.SEQ_DITTA_UMA.NEXTVAL,rec.ID_AZIENDA, 
         decode(SUBSTR(rec.SEDELEG_COMUNE,1,3), '076', '076', '077', '077', '076'), -- fuori regione attestati su PZ
         'U',
         (SELECT NVL(MAX(DITTA_UMA),0) + 1
          FROM   SMRUMA.DB_DITTA_UMA
          WHERE  EXT_PROVINCIA_UMA = decode(SUBSTR(rec.SEDELEG_COMUNE,1,3), '076', '076', '077', '077', '076')),SYSDATE,NULL,kIDUtente,SYSDATE,NULL,NULL,
         'N')
        RETURNING ID_DITTA_UMA INTO nIdDittaUma;

        INSERT INTO SMRUMA.DB_DATI_DITTA
        (ID_DATI_DITTA, ID_DITTA_UMA, ID_CONDUZIONE, EXT_COMUNE_PRINCIPALE_ATTIVITA, INDIRIZZO_CONSEGNA_CARBURANTE, NOTE, 
         DATA_INIZIO_VALIDITA,DATA_FINE_VALIDITA, EXT_ID_UTENTE_AGGIORNAMENTO, MODIFICA_INTERMEDIARIO, DATA_AGGIORNAMENTO, 
         DATA_RICEZ_DOCUM_ASSEGNAZ)
        VALUES
        (SMRUMA.SEQ_DATI_DITTA.NEXTVAL,nIdDittaUma,1,rec.SEDELEG_COMUNE,NULL,'ditta uma creata in fase di sincronizzazione del fascicolo',
         SYSDATE,NULL,kIDUtente,NULL,SYSDATE,
         NULL);
    END;

    IF rec.NUM_REC = 1 THEN
      UPDATE SMRUMA.DB_UTILIZZO
      SET    DATA_SCARICO = NVL(DATA_CARICO,SYSDATE-1),
             ID_SCARICO   = 0 -- sincronizzazione sian
      WHERE  ID_DITTA_UMA = nIdDittaUma
      AND    DATA_SCARICO IS NULL;
    END IF;

    BEGIN
      SELECT ID_MARCA
      INTO   nIdMarca
      FROM   SMRUMA.DB_TIPO_MARCA
      WHERE  DESCRIZIONE = rec.MARCA
      AND    ROWNUM      = 1;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        INSERT INTO SMRUMA.DB_TIPO_MARCA
        (ID_MARCA, DESCRIZIONE, MATRICE)
        VALUES
        ((SELECT MAX(ID_MARCA) + 1 FROM SMRUMA.DB_TIPO_MARCA),rec.MARCA,NULL)
        RETURNING ID_MARCA INTO nIdMarca;
    END;

    INSERT INTO SMRUMA.DB_MACCHINA
    (ID_MACCHINA, ID_MATRICE, MATRICOLA_TELAIO, MATRICOLA_MOTORE, DATA_AGGIORNAMENTO, EXT_ID_UTENTE_AGGIORNAMENTO, EXT_ID_MACCHINA_GAA, 
     ANNO_COSTRUZIONE, NOTE)
    VALUES
    (SMRUMA.SEQ_MACCHINA.NEXTVAL,NULL,rec.MATRICOLA_TELAIO,NULL,SYSDATE,kIDUtente,NULL,
     NULL,NULL)
    RETURNING ID_MACCHINA INTO nIdMacchina;

    INSERT INTO SMRUMA.DB_DATI_MACCHINA
    (ID_DATI_MACCHINA, ID_MACCHINA, MARCA, ID_GENERE_MACCHINA, TIPO_MACCHINA, ID_CATEGORIA, TARA, LORDO, NUMERO_ASSI, CALORIE, 
     EXT_ID_UTENTE_AGGIORNAMENTO, POTENZA, DATA_AGGIORNAMENTO, ID_ALIMENTAZIONE, ID_NAZIONALITA, ID_TRAZIONE, QUANTITA)
    VALUES
    (SMRUMA.SEQ_DATI_MACCHINA.NEXTVAL,nIdMacchina,rec.MARCA,rec.ID_GENERE_MACCHINA,rec.TIPO_MACCHINA,NVL(rec.ID_CATEGORIA,2),NULL,NULL,NULL,NULL,
     kIDUtente,NULL,SYSDATE,rec.ID_ALIMENTAZIONE,NULL,rec.ID_TRAZIONE,NULL);

    IF rec.NUMERO_TARGA IS NOT NULL AND LENGTH(rec.NUMERO_TARGA) <= 8 THEN
      INSERT INTO SMRUMA.DB_NUMERO_TARGA
      (ID_NUMERO_TARGA, ID_MACCHINA, ID_TARGA, NUMERO_TARGA, ID_PROVINCIA,FLAG_TARGA_NUOVA, EXT_ID_UTENTE_AGGIORNAMENTO, MC_824, 
       DATA_AGGIORNAMENTO, DATA_PRIMA_IMMATRICOLAZIONE, CERT_X_TARGA_TELAIO, CERT_X_TARGA, CERT_X_TELAIO)
      VALUES
      (SMRUMA.SEQ_NUMERO_TARGA.NEXTVAL,nIdMacchina,rec.ID_TARGA,rec.NUMERO_TARGA,SUBSTR(rec.SEDELEG_COMUNE,1,3),NULL,kIDUtente,NULL,
       SYSDATE,NULL,NULL,NULL,NULL)
      RETURNING ID_NUMERO_TARGA INTO nIdNumeroTarga;

      INSERT INTO SMRUMA.DB_MOVIMENTI_TARGA
      (ID_MOVIMENTI_TARGA, ID_NUMERO_TARGA, ID_MACCHINA, ID_MOVIMENTAZIONE, ID_PROVINCIA, DITTA_UMA, PROTOCOLLO_MODELLO_49, NUMERO_MODELLO_49,
       DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, DATA_PROTOCOLLO_49, DATA_AGGIORNAMENTO, EXT_ID_UTENTE_AGGIORNAMENTO, ANNO_MODELLO_49)
      VALUES
      (SMRUMA.SEQ_MOVIMENTI_TARGA.NEXTVAL,nIdNumeroTarga,nIdMacchina,11,NULL,NULL,NULL,NULL,
       SYSDATE,NULL,NULL,SYSDATE,kIDUtente,NULL);
    END IF;

    INSERT INTO SMRUMA.DB_UTILIZZO
    (ID_UTILIZZO, ID_DITTA_UMA, ID_MACCHINA, DATA_CARICO, DATA_SCARICO, ID_SCARICO, EXT_ID_UTENTE_AGGIORNAMENTO, DATA_AGGIORNAMENTO)
    VALUES
    (SMRUMA.SEQ_UTILIZZO.NEXTVAL,nIdDittaUma,nIdMacchina,SYSDATE,NULL,NULL,kIDUtente,SYSDATE)
    RETURNING ID_UTILIZZO INTO nIdUtilizzo;

    INSERT INTO SMRUMA.DB_POSSESSO
    (ID_POSSESSO, ID_UTILIZZO, ID_FORMA_POSSESSO, DATA_SCADENZA_LEASING, EXT_ID_AZIENDA, DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, 
     EXT_ID_UTENTE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, PERCENTUALE_POSSESSO)
    VALUES
    (SMRUMA.SEQ_POSSESSO.NEXTVAL,nIdUtilizzo,rec.ID_TIPO_FORMA_POSSESSO,NULL,rec.ID_AZIENDA,SYSDATE,NULL,
     kIDUtente,SYSDATE,NULL);
  END LOOP;

EXCEPTION
  WHEN OTHERS THEN
    pCodErrore  := 1;
    pDescErrore := 'Errore generico nel popolamento di uma = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
END popolaUma;

PROCEDURE popolaAllevamenti(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                            pCodErrore   OUT NUMBER,
                            pDescErrore  OUT VARCHAR2) IS

  nIdUte                     SMRGAA.DB_UTE.ID_UTE%TYPE;
  nIdTipoProduzione          SMRGAA.DB_ALLEVAMENTO.ID_TIPO_PRODUZIONE%TYPE;
  nIdOrientamentoProduttivo  SMRGAA.DB_ALLEVAMENTO.ID_ORIENTAMENTO_PRODUTTIVO%TYPE;
  nIdAllevamento             SMRGAA.DB_ALLEVAMENTO.ID_ALLEVAMENTO%TYPE;
  nQuantita                  SMRGAA.DB_CATEGORIE_ALLEVAMENTO.QUANTITA%TYPE;
  nIdCategorieAllevamento    SMRGAA.DB_CATEGORIE_ALLEVAMENTO.ID_CATEGORIE_ALLEVAMENTO%TYPE;
BEGIN
  pCodErrore  := 0;
  pDescErrore := '';

  SELECT MAX(U.ID_UTE)
  INTO   nIdUte 
  FROM   SMRGAA.DB_UTE U,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,ISWSRESPANAGFASCICOLO15 RAF
  WHERE  U.ID_AZIENDA          = AA.ID_AZIENDA
  AND    U.DATA_FINE_ATTIVITA  IS NULL
  AND    AA.CUAA               = RAF.CUAA
  AND    AA.DATA_FINE_VALIDITA IS NULL
  AND    RAF.ID_CHIAMATA       = pIdChiamata;

  -- chiusura preventiva di tutti gli allevamenti dell¿azienda
  UPDATE SMRGAA.DB_ALLEVAMENTO
  SET    DATA_AGGIORNAMENTO      = SYSDATE, 
         ID_UTENTE_AGGIORNAMENTO = kIDUtente, 
         DATA_FINE               = SYSDATE - 1
  WHERE  ID_UTE                  IN (SELECT U.ID_UTE
                                     FROM   SMRGAA.DB_UTE U,SMRGAA.DB_ANAGRAFICA_AZIENDA AA,ISWSRESPANAGFASCICOLO15 RAF
                                     WHERE  U.ID_AZIENDA          = AA.ID_AZIENDA
                                     AND    AA.CUAA               = RAF.CUAA
                                     AND    AA.DATA_FINE_VALIDITA IS NULL
                                     AND    RAF.ID_CHIAMATA       = pIdChiamata);

  FOR rec IN (SELECT SA.PROVINCIA||SA.COMUNE ISTAT_COMUNE,A.CODICEAZIENDA,ReturnData(A.DATAINIZIATTI) DATA_INIZIO,SA.INDIRIZZO,SA.CAP,
                     (SELECT SASS.ID_SPECIE_ANIMALE
                      FROM   SMRGAA.DB_R_SPECIE_AN_SIAN_SIAP SASS
                      WHERE  SASS.CODICE_SPECIE         = A.CODICESPECIE
                      AND    SASS.FLAG_RELAZIONE_ATTIVA = 'S') ID_SPECIE_ANIMALE,A.CODFISCALEPROP,A.DENOMPROP,DA.CODFISCALE,
                      DECODE(DA.FLAGSOCC,'0','N','S') FLAGSOCC,A.CODICESPECIE,A.TIPOPROD,ca.IDISWSCAPOALLEVAMENTO2
              FROM   ISWSAllevamento15 A,IsWsStallaAllevamento SA,iswscapoallevamento2 CA,IsWsPropallevamentodeteallev DA
              WHERE  A.IDCHIAMATA = pIdChiamata
              AND    SA.ID_ISWSALLEVAMENTO15 = A.IDISWSALLEVAMENTO15
              AND    CA.ID_ISWSALLEVAMENTO15 = A.IDISWSALLEVAMENTO15
              AND    A.IDISWSALLEVAMENTO15   = DA.ID_ISWSALLEVAMENTO15(+)) LOOP

    --DBMS_OUTPUT.PUT_LINE('rec.CODICESPECIE - ' || rec.CODICESPECIE);
    --DBMS_OUTPUT.PUT_LINE('rec.TIPOPROD - ' || rec.TIPOPROD);

    begin
       SELECT SSOP.ID_TIPO_PRODUZIONE, TOP.ID_ORIENTAMENTO_PRODUTTIVO
       INTO   nIdTipoProduzione,nIdOrientamentoProduttivo
       FROM   SMRGAA.DB_SIAN_SPECIE_ORIENTAM_PROD SSOP,SMRGAA.DB_TIPO_ORIENTAMENTO_PRODUT TOP
       WHERE  SSOP.ID_ORIENTAMENTO_PRODUTTIVO = TOP.ID_ORIENTAMENTO_PRODUTTIVO
       AND    SSOP.DATA_FINE_VALIDITA         IS NULL
       AND    SSOP.CODICE_SPECIE              = rec.CODICESPECIE
       AND    TOP.CODICE_SIAN                 = rec.TIPOPROD;
    EXCEPTION
    WHEN NO_DATA_FOUND THEN
       -- cerco un default
       SELECT SSOP.ID_TIPO_PRODUZIONE, TOP.ID_ORIENTAMENTO_PRODUTTIVO
       INTO   nIdTipoProduzione,nIdOrientamentoProduttivo
       FROM   SMRGAA.DB_SIAN_SPECIE_ORIENTAM_PROD SSOP,SMRGAA.DB_TIPO_ORIENTAMENTO_PRODUT TOP
       WHERE  SSOP.ID_ORIENTAMENTO_PRODUTTIVO = TOP.ID_ORIENTAMENTO_PRODUTTIVO
       AND    SSOP.DATA_FINE_VALIDITA         IS NULL
       AND    SSOP.CODICE_SPECIE              = rec.CODICESPECIE
       AND    SSOP.ID_ORIENTAMENTO_PRODUTTIVO = (SELECT min(SSOP2.ID_ORIENTAMENTO_PRODUTTIVO)
                                                 FROM   SMRGAA.DB_SIAN_SPECIE_ORIENTAM_PROD SSOP2
                                                 WHERE  SSOP2.DATA_FINE_VALIDITA IS NULL
                                                 AND    SSOP2.CODICE_SPECIE      = rec.CODICESPECIE);
    END;

    INSERT INTO SMRGAA.DB_ALLEVAMENTO
    (ID_ALLEVAMENTO, ID_UTE, ID_ASL, ISTAT_COMUNE, ID_SPECIE_ANIMALE, CODICE_AZIENDA_ZOOTECNICA, DATA_INIZIO, DATA_FINE, NOTE, 
     DATA_AGGIORNAMENTO, ID_UTENTE_AGGIORNAMENTO, INDIRIZZO, CAP, TELEFONO, DICHIARAZIONE_RIPRISTINATA, ID_DICHIARAZIONE_CONSISTENZA, 
     CODICE_FISCALE_PROPRIETARIO, DENOMINAZIONE_PROPRIETARIO, CODICE_FISCALE_DETENTORE, DENOMINAZIONE_DETENTORE, DATA_INIZIO_DETENZIONE, 
     DATA_FINE_DETENZIONE, FLAG_SOCCIDA, ID_TIPO_PRODUZIONE, ID_ORIENTAMENTO_PRODUTTIVO, DESCRIZIONE_ALTRI_TRATTAM, FLAG_DEIEZIONE_AVICOLI, 
     MEDIA_CAPI_LATTAZIONE, QUANTITA_ACQUA_LAVAGGIO, FLAG_ACQUE_EFFLUENTI, ID_MUNGITURA, SUPERFICIE_LETTIERA_PERMANENTE, 
     ALTEZZA_LETTIERA_PERMANENTE, DENOMINAZIONE_ALLEVAMENTO, LATITUDINE, LONGITUDINE, DATA_APERTURA_ALLEVAMENTO, MOTIVO_SOCCIDA, 
     DATA_ESECUZIONE_CONTROLLI, ID_TIPO_PRODUZIONE_COSMAN, FLAG_ASSICURATO_COSMAN)
    VALUES
    (SMRGAA.SEQ_ALLEVAMENTO.NEXTVAL,nIdUte,NULL,rec.ISTAT_COMUNE,rec.ID_SPECIE_ANIMALE,rec.CODICEAZIENDA,rec.DATA_INIZIO,NULL,NULL,
     SYSDATE,kIDUtente,rec.INDIRIZZO,rec.CAP,NULL,NULL,NULL,
     rec.CODFISCALEPROP,substr(rec.DENOMPROP, 1, 100),rec.CODFISCALE,NULL,rec.DATA_INIZIO,
     NULL,rec.FLAGSOCC,nIdTipoProduzione,nIdOrientamentoProduttivo,NULL,NULL,
     NULL,NULL,NULL,NULL,NULL,
     NULL,NULL,NULL,NULL,NULL,NULL,
     NULL,NULL,NULL)
    RETURNING ID_ALLEVAMENTO INTO nIdAllevamento;

    FOR recCatAn IN (SELECT CAAS.*,C.COLUMN_NAME
                     FROM   SMRGAA.DB_R_CAT_ANIMALE_ALLEV_SIAN CAAS,SMRGAA.DB_TIPO_ALLEVAMENTO_SIAN TAS,
                            SMRGAA.DB_R_ALLEV_CAMPO_AGENT_SIAN ACAS,COLS C
                     WHERE  TAS.ID_TIPO_ALLEVAMENTO_SIAN   = CAAS.ID_TIPO_ALLEVAMENTO_SIAN
                     AND    CAAS.ID_CAT_ANIMALE_ALLEV_SIAN = ACAS.ID_CAT_ANIMALE_ALLEV_SIAN
                     AND    TAS.CODICE_ALLEVAMENTO_SIAN    = rec.CODICESPECIE
                     AND    C.TABLE_NAME                   = 'ISWSCAPOALLEVAMENTO2'
                     AND    TAS.DATA_FINE_VALIDITA         IS NULL
                     AND    CAAS.DATA_FINE_VALIDITA        IS NULL
                     AND    ACAS.NOME_CAMPO_AGENT_SIAN     = C.COLUMN_NAME
                     AND    ACAS.DATA_FINE_VALIDITA        IS NULL) LOOP

      EXECUTE IMMEDIATE('SELECT '||recCatAn.COLUMN_NAME||' FROM ISWSCAPOALLEVAMENTO2 WHERE IDISWSCAPOALLEVAMENTO2 = '||
                        rec.IDISWSCAPOALLEVAMENTO2) INTO nQuantita;

      INSERT INTO SMRGAA.DB_CATEGORIE_ALLEVAMENTO
      (ID_CATEGORIE_ALLEVAMENTO, ID_CATEGORIA_ANIMALE, ID_ALLEVAMENTO, QUANTITA, PESO_VIVO_UNITARIO, QUANTITA_PROPRIETA)
      VALUES
      (SMRGAA.SEQ_CATEGORIE_ALLEVAMENTO.NEXTVAL,recCatAn.ID_CATEGORIA_ANIMALE,nIdAllevamento,nQuantita,NULL,NULL)
      RETURNING ID_CATEGORIE_ALLEVAMENTO INTO nIdCategorieAllevamento;

      INSERT INTO SMRGAA.DB_SOTTOCATEGORIA_ALLEVAMENTO
      (ID_SOTTOCATEGORIA_ALLEVAMENTO, ID_SOTTOCATEGORIA_ANIMALE, ID_CATEGORIE_ALLEVAMENTO, ORE_PASCOLO_INVERNO, QUANTITA, PESO_VIVO, 
       GIORNI_VUOTO_SANITARIO, GIORNI_PASCOLO_ESTATE, ORE_PASCOLO_ESTATE, GIORNI_PASCOLO_INVERNO, CICLI, NUMERO_CICLI_ANNUALI, 
       QUANTITA_PROPRIETA)
      VALUES
      (SMRGAA.SEQ_SOTTOCATEGORIA_ALLEVAMENTO.NEXTVAL,recCatAn.ID_SOTTOCATEGORIA_ANIMALE,nIdCategorieAllevamento,NULL,nQuantita,NULL,
       NULL,NULL,NULL,NULL,NULL,NULL,
       NULL);
    END LOOP;

  END LOOP;

EXCEPTION
  WHEN OTHERS THEN
    pCodErrore  := 1;
    pDescErrore := 'Errore generico nel popolamento degli allevamenti = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
END popolaAllevamenti;


PROCEDURE popolaMandato(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
                        pCodErrore   OUT NUMBER,
                        pDescErrore  OUT VARCHAR2) IS

    nIdIntermediario          SMRGAA.DB_UFFICIO_ZONA_INTERMEDIARIO.id_intermediario%TYPE;
    nIdUfficioZona            SMRGAA.DB_UFFICIO_ZONA_INTERMEDIARIO.id_ufficio_zona_intermediario%TYPE;
    nIdAzienda                SMRGAA.DB_ANAGRAFICA_AZIENDA.id_azienda%TYPE; 
    vDetentore                ISWSRESPANAGFASCICOLO15.detentore%TYPE;
    nIdDelega                 SMRGAA.DB_DELEGA.id_delega%TYPE; 

BEGIN
    pCodErrore  := 0;
    pDescErrore := '';

    SELECT aa.id_azienda, raf.detentore
    INTO   nIdAzienda, vDetentore
    FROM   SMRGAA.DB_ANAGRAFICA_AZIENDA AA, ISWSRESPANAGFASCICOLO15 RAF
    WHERE  AA.CUAA               = RAF.CUAA
    AND    AA.DATA_FINE_VALIDITA IS NULL
    AND    RAF.ID_CHIAMATA       = pIdChiamata;

    begin
        select caa.id_intermediario, caa.id_ufficio_zona_intermediario
        into nIdIntermediario, nIdUfficioZona
        from smrgaa.db_ufficio_zona_intermediario caa 
        where caa.codice_agea = vDetentore;
    exception when no_data_found then
        --pCodErrore  := 1;
        --pDescErrore := 'Errore inserimento delega: CAA non trovato = '|| vDetentore;
        return;
    end;

    begin
        select id_delega
        into nIdDelega
        from SMRGAA.DB_DELEGA man
        where man.id_azienda = nIdAzienda
        and   man.id_intermediario = nIdIntermediario
        and   man.id_ufficio_zona_intermediario = nIdUfficioZona
        and   man.data_fine is null
        and   man.data_fine_mandato is null;
    exception when no_data_found then
        update SMRGAA.DB_DELEGA man
        set man.data_fine = trunc(sysdate),
            man.data_fine_mandato = trunc(sysdate),
            man.id_utente_fine_delega = kIDUtente
        where man.id_azienda = nIdAzienda
        and   man.data_fine is null;

        insert into smrgaa.db_delega
            (ID_DELEGA,
            ID_INTERMEDIARIO,
            ID_PROCEDIMENTO,
            ID_AZIENDA,
            DATA_INIZIO,
            DATA_FINE,
            ID_UTENTE_INSERIMENTO_DELEGA,
            ID_UFFICIO_ZONA_INTERMEDIARIO,
            UFFICIO_FASCICOLO,
            ID_UTENTE_FINE_DELEGA,
            INDIRIZZO,
            COMUNE,
            CAP,
            CODICE_AMMINISTRAZIONE,
            RECAPITO,
            DATA_INIZIO_MANDATO,
            DATA_FINE_MANDATO,
            DATA_AGGIORNAMENTO_SIAN,
            DATA_RICEVUTA_RITORNO_REVOCA,
            ID_UTENTE_REVOCA)
        values
            (smrgaa.seq_delega.nextval,
            nIdIntermediario,
            7,
            nIdAzienda,
            trunc(sysdate),
            null,
            kIDUtente,
            nIdUfficioZona,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            trunc(sysdate),
            null,
            null,
            null,
            null);
    end;

EXCEPTION
  WHEN OTHERS THEN
    pCodErrore  := 1;
    pDescErrore := 'Errore generico nel popolamento del mandato CAA = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;
END popolaMandato;


FUNCTION ReturnIdAzienda(pIdChiamata  SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE) RETURN SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE IS

  nIdAzienda  SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE;
BEGIN
  SELECT ID_AZIENDA
  INTO   nIdAzienda
  FROM   ISWSRespAnagFascicolo15 RAF,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
  WHERE  RAF.ID_CHIAMATA       = pIdChiamata
  AND    RAF.CUAA              = AA.CUAA
  AND    AA.DATA_FINE_VALIDITA IS NULL;

  RETURN nIdAzienda;
END ReturnIdAzienda;

PROCEDURE Main(pIdChiamata      SIAN_CHIAMATA_SERVIZIO.ID_CHIAMATA%TYPE,
               pCuaaInput       SIAN_CHIAMATA_SERVIZIO.CUAA_INPUT%TYPE,
               pCodErrore   OUT NUMBER,
               pDescErrore  OUT VARCHAR2) IS

  recChiamataServizio          SIAN_CHIAMATA_SERVIZIO%ROWTYPE;
  nCont                        SIMPLE_INTEGER := 0;
  vResponsabile                SMRGAA.DB_RESPONSABILE_INTERMEDIARIO.RESPONSABILE%TYPE;
  vCuaa                        SMRGAA.DB_ANAGRAFICA_AZIENDA.CUAA%TYPE;
  nIdDichiarazioneConsistenza  SMRGAA.DB_DICHIARAZIONE_CONSISTENZA.ID_DICHIARAZIONE_CONSISTENZA%TYPE;
  nCodiceFotografiaTerreni     SMRGAA.DB_DICHIARAZIONE_CONSISTENZA.CODICE_FOTOGRAFIA_TERRENI%TYPE;
  nIdAzienda                   SMRGAA.DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE := ReturnIdAzienda(pIdChiamata);
BEGIN
  pCodErrore  := 0;
  pDescErrore := '';

  IF pIdChiamata IS NULL OR pCuaaInput IS NULL THEN
    pCodErrore  := 1;
    pDescErrore := 'Parametri di input obbligatori non valorizzati';
    RETURN;
  END IF;

  SELECT *
  INTO   recChiamataServizio
  FROM   SIAN_CHIAMATA_SERVIZIO CS
  WHERE  CS.ID_CHIAMATA = pIdChiamata;

  IF recChiamataServizio.CUAA_INPUT != pCuaaInput THEN
    pCodErrore  := 1;
    pDescErrore := 'CUAA di input errato';
    RETURN;
  END IF;

  IF recChiamataServizio.ID_SERVIZIO = 2 AND recChiamataServizio.COD_RET_OUTPUT = '012' THEN
    NULL;
  ELSE
    pCodErrore  := 1;
    pDescErrore := 'Dati non corretti';
    RETURN;
  END IF;

  -- verifica che la validazione inviata dal Sian non sia gia' presente come ultima validazione dell'azienda in esame
  SELECT COUNT(*)
  INTO   nCont
  FROM   SMRGAA.DB_DICHIARAZIONE_CONSISTENZA DC,ISWSRespAnagFascicolo15 RAF
  WHERE  RAF.ID_CHIAMATA                   = pIdChiamata
  AND    DC.ID_AZIENDA                     = nIdAzienda
  AND    DC.DATA_PROTOCOLLO                = ReturnData(RAF.DATASCHEDAVALIDAZIONE)
  AND    DC.NUMERO_PROTOCOLLO              = RAF.SCHEDAVALIDAZIONE
  AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE)
                                              FROM   SMRGAA.DB_DICHIARAZIONE_CONSISTENZA DC1
                                              WHERE  DC1.ID_AZIENDA = DC.ID_AZIENDA);

  IF nCont != 0 THEN
    pCodErrore  := 1;
    pDescErrore := 'Aggiornamento non effettuato';
    RETURN;
  END IF;

  -- Verifica della presenza della scheda di validazione inviata dal Sian
  SELECT count(*) 
  into   nCont
  FROM   ISWSRespAnagFascicolo15 RAF
  WHERE  RAF.SCHEDAVALIDAZIONE is not null
  and    RAF.ID_CHIAMATA       = pIdChiamata;

  -- se la validazione non viene inviata dal Sian non viene fatto aggiornamento del fascicolo 
  IF nCont = 0 THEN
    pCodErrore  := 1;
    pDescErrore := 'Aggiornamento fascicolo non effettuato per mancanza della scheda validazione';
    RETURN;
  END IF;

  popolaDatiSoggetti(pIdChiamata,pCodErrore,pDescErrore);

  IF pCodErrore != 0 THEN
    RETURN;
  END IF;

  popolaDatiAzienda(pIdChiamata,pCodErrore,pDescErrore);

  IF pCodErrore != 0 THEN
    RETURN;
  END IF;

  popolaDatiDocumenti(pIdChiamata,pCodErrore,pDescErrore);

  IF pCodErrore != 0 THEN
    RETURN;
  END IF;

  popolaDatiTerreni(pIdChiamata,pCodErrore,pDescErrore);

  IF pCodErrore != 0 THEN
    RETURN;
  END IF;

  popolaDatiConsistenza(pIdChiamata,pCodErrore,pDescErrore);

  IF pCodErrore != 0 THEN
    RETURN;
  END IF;

  popolaDatiColture(pIdChiamata,pCodErrore,pDescErrore);

  IF pCodErrore != 0 THEN
    RETURN;
  END IF;

  popolaContoCorrente(pIdChiamata,pCodErrore,pDescErrore);

  IF pCodErrore != 0 THEN
    RETURN;
  END IF;

  popolaDatiManodopera(pIdChiamata,pCodErrore,pDescErrore);

  IF pCodErrore != 0 THEN
    RETURN;
  END IF;

  popolaMotoriAgricoli(pIdChiamata,pCodErrore,pDescErrore);

  IF pCodErrore != 0 THEN
    RETURN;
  END IF;

  popolaFabbricati(pIdChiamata,pCodErrore,pDescErrore);

  IF pCodErrore != 0 THEN
    RETURN;
  END IF;

  popolaUma(pIdChiamata,pCodErrore,pDescErrore);

  IF pCodErrore != 0 THEN
    RETURN;
  END IF;

  popolaAllevamenti(pIdChiamata,pCodErrore,pDescErrore);

  IF pCodErrore != 0 THEN
    RETURN;
  END IF;

  popolaMandato(pIdChiamata,pCodErrore,pDescErrore);

  IF pCodErrore != 0 THEN
    RETURN;
  END IF;

  -- Ricerca del responsabile del caa detentore del fascicolo
  SELECT RI.RESPONSABILE, AA.CUAA
  INTO   vResponsabile,vCuaa
  FROM   SMRGAA.DB_RESPONSABILE_INTERMEDIARIO RI,SMRGAA.DB_DELEGA D,SMRGAA.DB_ANAGRAFICA_AZIENDA AA
  WHERE  RI.ID_PROCEDIMENTO(+)  = 7
  AND    RI.DATA_FINE(+)        IS NULL
  AND    RI.ID_INTERMEDIARIO(+) = D.ID_INTERMEDIARIO
  AND    D.DATA_FINE(+)         IS NULL
  AND    D.ID_AZIENDA(+)        = AA.ID_AZIENDA
  AND    AA.ID_AZIENDA          = nIdAzienda
  AND    AA.DATA_FINE_VALIDITA  IS NULL;

  nIdDichiarazioneConsistenza := SMRGAA.SEQ_DICHIARAZIONE_CONSISTENZA.NEXTVAL;
  nCodiceFotografiaTerreni    := SMRGAA.SEQ_CODICE_FOTOGRAFIA_TERRENI.NEXTVAL;

  INSERT INTO SMRGAA.DB_DICHIARAZIONE_CONSISTENZA
  (ID_DICHIARAZIONE_CONSISTENZA, ID_AZIENDA,ANNO,
   DATA,TIPO_CONVALIDA, CODICE_FOTOGRAFIA_TERRENI, ID_PROCEDIMENTO,ID_UTENTE,ID_MOTIVO_DICHIARAZIONE, RESPONSABILE, 
   DATA_AGGIORNAMENTO_FASCICOLO, CUAA_VALIDATO, FLAG_ANOMALIA,DATA_INSERIMENTO_DICHIARAZIONE,FLAG_INVIA_USO, ANNO_CAMPAGNA,DATA_PROTOCOLLO, 
   NUMERO_PROTOCOLLO,FLAG_VARIAZIONE_VERIFICATA)
  SELECT nIdDichiarazioneConsistenza,nIdAzienda,EXTRACT (YEAR FROM ReturnData(RAF.DATASCHEDAVALIDAZIONE)),
         SYSDATE,'D',nCodiceFotografiaTerreni,7,kIDUtente,6,vResponsabile,
         SYSDATE,RAF.CUAA,'N',SYSDATE,'S',EXTRACT (YEAR FROM ReturnData(RAF.DATASCHEDAVALIDAZIONE)),ReturnData(RAF.DATASCHEDAVALIDAZIONE),
         RAF.SCHEDAVALIDAZIONE,'N'
  FROM   ISWSRespAnagFascicolo15 RAF
  WHERE  RAF.ID_CHIAMATA = pIdChiamata;

  SMRGAA.PACK_DICHIARAZIONE_CONSISTENZA.SALVA_DICHIARAZIONE(nIdAzienda,EXTRACT (YEAR FROM SYSDATE),nCodiceFotografiaTerreni,kIDUtente,
                                                            pDescErrore,pCodErrore);

  IF pCodErrore IS NULL THEN
    pCodErrore := 0;
  END IF;

EXCEPTION
  WHEN OTHERS THEN
    pCodErrore  := 1;
    pDescErrore := 'Errore generico nella procedura Main = '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;  
END Main;  

END PCK_SIANFA_POPOLA_FASCICOLO;

/
