package it.csi.solmr.dto.anag;

import it.csi.solmr.util.ParticellaAssVOComparator;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class ParticellaAssVO implements Serializable
{

  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  private static final long serialVersionUID = 5169920582917025632L;
  
  private String primaryKey;
	private Long idParticella;
	private Long idStoricoParticella;
	private String sezione;
	private String istat;
	private String descComuneParticella;
	private String siglaProvinciaParticella;
	private Long foglio;
	private Long particella;
	private String supCatastale;
	private String supCondotta;
	private String supUtilizzata;
	private String subalterno;
	private Long idEvento;
	private Long idParticellaEvento;
	private String descrizioneEvento;
	private String idConduzione;
	private String idUtilizzo;
	private String idVarieta;
	private String descUsoPrimario;
	private BigDecimal supCatastaleB;
  private BigDecimal supCondottaB;
  private BigDecimal supUtilizzataB;
  private Long idCasoParticolare;
	
	private String[][]        errori;

	public ParticellaAssVO() 
	{
	}

	public void setIdStoricoParticella(Long idStoricoParticella) 
	{
		this.idStoricoParticella = idStoricoParticella;
	}
	public Long getIdStoricoParticella() 
	{
		return idStoricoParticella;
	}
	
	public Long getIdParticella() 
	{
		return idParticella;
	}
	public void setIdParticella(Long idParticella) 
	{
		this.idParticella = idParticella;
	}
	
	public void setSezione(String sezione) 
	{
		this.sezione = sezione;
	}
	public String getSezione() 
	{
		return sezione;
	}
	
 	public void setDescComuneParticella(String descComuneParticella) 
 	{
		this.descComuneParticella = descComuneParticella;
	}
	public String getDescComuneParticella() 
	{
		return descComuneParticella;
	}
	
	public void setSiglaProvinciaParticella(String siglaProvinciaParticella) 
	{
		this.siglaProvinciaParticella = siglaProvinciaParticella;
	}
	public String getSiglaProvinciaParticella() 
	{
		return siglaProvinciaParticella;
	}
	
	public void setFoglio(Long foglio) 
	{
		this.foglio = foglio;
	}
	public Long getFoglio() 
	{
		return foglio;
	}

	public void setParticella(Long particella) 
	{
		this.particella = particella;
	}
	public Long getParticella() 
	{
		return particella;
	}
	
	public void setSupCatastale(String supCatastale) 
	{
		this.supCatastale = supCatastale;
	}
	public String getSupCatastale() 
	{
		return supCatastale;
	}
	
	public void setSubalterno(String subalterno) 
	{
		this.subalterno = subalterno;
	}
	public String getSubalterno() 
	{
		return subalterno;
	}

  public String getPrimaryKey()
  {
    return primaryKey;
  }
  public void setPrimaryKey(String primaryKey)
  {
    this.primaryKey = primaryKey;
  }

  public Long getIdEvento()
  {
    return idEvento;
  }
  public void setIdEvento(Long idEvento)
  {
    this.idEvento = idEvento;
  }
  
  public Long getIdParticellaEvento()
  {
    return idParticellaEvento;
  }

  public void setIdParticellaEvento(Long idParticellaEvento)
  {
    this.idParticellaEvento = idParticellaEvento;
  }

  public String getDescrizioneEvento()
  {
    return descrizioneEvento;
  }
  public void setDescrizioneEvento(String descrizioneEvento)
  {
    this.descrizioneEvento = descrizioneEvento;
  }

  public String getSupCondotta()
  {
    return supCondotta;
  }

  public void setSupCondotta(String supCondotta)
  {
    this.supCondotta = supCondotta;
  }

  public String getIdConduzione()
  {
    return idConduzione;
  }

  public void setIdConduzione(String idConduzione)
  {
    this.idConduzione = idConduzione;
  }

  public String getSupUtilizzata()
  {
    return supUtilizzata;
  }

  public void setSupUtilizzata(String supUtilizzata)
  {
    this.supUtilizzata = supUtilizzata;
  }

  public String getIdUtilizzo()
  {
    return idUtilizzo;
  }

  public void setIdUtilizzo(String idUtilizzo)
  {
    this.idUtilizzo = idUtilizzo;
  }

  public String getIdVarieta()
  {
    return idVarieta;
  }

  public void setIdVarieta(String idVarieta)
  {
    this.idVarieta = idVarieta;
  }

  public String[][] getErrori()
  {
    return errori;
  }

  public void setErrori(String[][] errori)
  {
    this.errori = errori;
  }	
  
  
  public boolean validateInsert()
  {
    Vector<Object> errors = new Vector<Object>();
    
    //Controllo che la conduzione sia selezionata: è obbligatoria
    String errore1[] = new String[2];
    errore1[0] = "err_idConduzione";
    errore1[1] = "La conduzione è obbligatoria";
    
    if (Validator.isEmpty(this.idConduzione))
      errors.add(errore1);
    
    //Controllo la superficie condotta: è obbligatorio e deve essere >0  e <=  uguale della superficie catastale
    try
    {
      String errore[] = new String[2];
      errore[0] = "err_supConduzione";
      errore[1] = "La sup. in conduzione deve essere minore o uguale alla sup. catastale";

      supCondottaB = new BigDecimal(supCondotta.replace(',', '.'));
      if (supCondottaB.doubleValue()<=0 || supCondottaB.doubleValue()>supCatastaleB.doubleValue())
      {
        supCondottaB=null;
        errors.add(errore);
      }
      else
      {
        String temp = checkDecimals(supCondottaB, 4);
        if (temp != null)
        {
          supCondottaB=null;
          errore[1] = temp;
          errors.add(errore);
        }
      }
    }
    catch (Exception e)
    {
      String errore[] = new String[2];
      supCondottaB=null;
      errore[0] = "err_supConduzione";
      errore[1] = "La sup. in conduzione deve essere minore o uguale alla sup. catastale";
      errors.add(errore);
    }
    
    //Controllo che l'uso primario sia selezionato: è obbligatoria
    String errore2[] = new String[2];
    errore2[0] = "err_idTipoUtilizzo";
    errore2[1] = "L'uso primario è obbligatorio";
    
    if (Validator.isEmpty(this.idUtilizzo))
      errors.add(errore2);
    
    
    //Controllo che la varietà sia selezionata: è obbligatoria
    String errore3[] = new String[2];
    errore3[0] = "err_idVarieta";
    errore3[1] = "La varietà è obbligatoria";
    
    if (Validator.isEmpty(this.idVarieta))
      errors.add(errore3);
    
    
    
    
    
    //Controllo la superficie utilizzata: è obbligatoria  deve essere >0 e <=  uguale della superficie condotta
    try
    {
      String errore[] = new String[2];
      errore[0] = "err_supUtilizzata";
      errore[1] = "La sup. utilizzata deve essere minore o uguale alla sup. in conduzione";

      supUtilizzataB = new BigDecimal(supUtilizzata.replace(',', '.'));
      if (supUtilizzataB.doubleValue()<=0 || supUtilizzataB.doubleValue()>supCondottaB.doubleValue())
      {
        supUtilizzataB=null;
        errors.add(errore);
      }
      else
      {
        String temp = checkDecimals(supUtilizzataB, 4);
        if (temp != null)
        {
          supUtilizzataB=null;
          errore[1] = temp;
          errors.add(errore);
        }
      }
    }
    catch (Exception e)
    {
      String errore[] = new String[2];
      supUtilizzataB=null;
      errore[0] = "err_supUtilizzata";
      errore[1] = "La sup. utilizzata deve essere minore o uguale alla sup. in conduzione";
      errors.add(errore);
    }
    
    

    errori = errors.size() == 0 ? null : (String[][]) errors.toArray(new String[0][]);

    if (errori != null)
      return true;
    else
      return false;
  }

  public BigDecimal getSupCatastaleB()
  {
    return supCatastaleB;
  }

  public void setSupCatastaleB(BigDecimal supCatastaleB)
  {
    this.supCatastaleB = supCatastaleB;
  }

  public BigDecimal getSupCondottaB()
  {
    return supCondottaB;
  }

  public void setSupCondottaB(BigDecimal supCondottaB)
  {
    this.supCondottaB = supCondottaB;
  }

  public BigDecimal getSupUtilizzataB()
  {
    return supUtilizzataB;
  }

  public void setSupUtilizzataB(BigDecimal supUtilizzataB)
  {
    this.supUtilizzataB = supUtilizzataB;
  }
  
  private static String checkDecimals(BigDecimal value, int numDecimals)
  {
    if (numDecimals > 0)
    {
      double poweredValue = value.doubleValue() * Math.pow(10, numDecimals);
      poweredValue = Math.round(poweredValue);
      poweredValue = poweredValue / Math.pow(10, numDecimals);
      if (poweredValue != value.doubleValue())
        return "Attenzione, troppe cifre decimali (massimo " + numDecimals + ")";
    }
    return null;
  }
  
  public String getIstat()
  {
    return istat;
  }

  public void setIstat(String istat)
  {
    this.istat = istat;
  }
  
  //Questo metodo controlla che a parità di particella 
  // - se ci sono più conduzioni uguali queste devo presentare la stessa sup. condotta ed usi primari diversi
  // - la sommatoria della sup. condotta raggruppando per conduzione deve essere <= alla sup. catastale
  // - la sommatoria della sup. utilizzata deve essere <= alla sommatoria sup. condotta
  public static Vector<Object> checkValidazione(Vector<Object> particelleAssociate)
  {
    //Scorro il vettore di particelle e le suddivido in gruppi di particelle con uguale chiave catastale
    boolean errore=false;
    if (particelleAssociate!=null && particelleAssociate.size()>0)
    {
      //Per prima cosa ordino le particelle
      ParticellaAssVO[] temp=(ParticellaAssVO[])particelleAssociate.toArray(new ParticellaAssVO[0]);
      
      Arrays.sort(temp, new ParticellaAssVOComparator());
              
      particelleAssociate=new Vector<Object>(Arrays.asList(temp));
      
      
      int size=particelleAssociate.size();
      ParticellaAssVO partOld = (ParticellaAssVO) particelleAssociate.get(0);
      Vector<ParticellaAssVO> particella=new Vector<ParticellaAssVO>();
      for (int i=0;i<size;i++)
      {
        ParticellaAssVO partCurr= (ParticellaAssVO) particelleAssociate.get(i);
        if (partCurr.equals(partOld))
        {
        //aggiungo la particella trovata
          particella.add(partCurr);
        }
        else
        {
          //Elaboro le particelle ottenute
          errore=checkValidazioneParticella(particella) || errore;
          //ripulisco il vettore
          particella=new Vector<ParticellaAssVO>();
          //aggiungo la particella trovata
          particella.add(partCurr);
          //aggiorno la particella vecchia
          partOld=partCurr;
        }
      }
      //Elaboro le particelle ottenute
      errore=checkValidazioneParticella(particella) || errore;
    }
    particelleAssociate.add(new Boolean(errore));
    return particelleAssociate;
  }
  
  private static boolean checkValidazioneParticella(Vector<ParticellaAssVO> particelle)
  {
    boolean errore=false;
    //Scorro le particelle e le suddivido in gruppi di particelle con uguale conduzione
    if (particelle!=null && particelle.size()>0)
    {
      int size=particelle.size();
      String idConduzioneOld = ((ParticellaAssVO) particelle.get(0)).getIdConduzione();
      Vector<ParticellaAssVO> partCond=new Vector<ParticellaAssVO>();
      BigDecimal supCondottaTot=(((ParticellaAssVO) particelle.get(0)).getSupCondottaB());
      BigDecimal supUtilizzataTot=new BigDecimal(0);
      BigDecimal supCat=(((ParticellaAssVO) particelle.get(0)).getSupCatastaleB());
      for (int i=0;i<size;i++)
      {
        ParticellaAssVO partCurr= (ParticellaAssVO) particelle.get(i);
        supUtilizzataTot=supUtilizzataTot.add(partCurr.getSupUtilizzataB());
        if (idConduzioneOld.equals(partCurr.getIdConduzione()))
        {
         //aggiungo la particella trovata
          partCond.add(partCurr);
        }
        else
        {
          //Elaboro le particelle ottenute
          errore=checkValidazioneParticellaCond(partCond)||errore;
          supCondottaTot=supCondottaTot.add(partCurr.getSupCondottaB());
          //ripulisco il vettore
          partCond=new Vector<ParticellaAssVO>();
          //aggiungo la particella trovata
          partCond.add(partCurr);
          //aggiorno l'idConduzione
          idConduzioneOld=partCurr.getIdConduzione();
        }
      }
      //Elaboro le particelle ottenute
      errore=checkValidazioneParticellaCond(partCond)||errore;
      if (!errore)
      {
        //Se non ci sono errori controllo che la somma della sup. condotta sia minore o uguale 
        //di quella catastale e che quella utilizzata sia minore o uguale a quella condotta
        if (supCat.compareTo(supCondottaTot)<0 || supCondottaTot.compareTo(supUtilizzataTot)<0)
        {
          errore=true;
          
          Vector<Object> errors = new Vector<Object>();
          
          String errore1[] = new String[2];
          errore1[0] = "err_supConduzione";
          errore1[1] = "La somma delle sup. in conduzione deve essere minore o uguale alla sup. catastale";
          
          if (supCat.compareTo(supCondottaTot)<0) errors.add(errore1);
          
          String errore2[] = new String[2];
          errore2[0] = "err_supUtilizzata";
          errore2[1] = "La somma della sup. utilizzata deve essere minore o uguale alla somma della sup. in conduzione";
          
          if (supCondottaTot.compareTo(supUtilizzataTot)<0) errors.add(errore2);
          
          for (int i=0;i<size;i++)
          {
            ParticellaAssVO part= (ParticellaAssVO) particelle.get(i);
            part.setErrori( errors.size() == 0 ? null : (String[][]) errors.toArray(new String[0][]));
          }
          
          
        }
      }
    }
    return errore;
  }
  
  //controllo che le particelle con la stessa conduzione abbiano tutte la stessa sup. condotta
  //e diverso uso primario
  private static boolean checkValidazioneParticellaCond(Vector<ParticellaAssVO> particelle)
  {
    boolean errore=false;
    if (particelle!=null && particelle.size()>0)
    {
      int size=particelle.size();
      String supCondOld = ((ParticellaAssVO) particelle.get(0)).getSupCondotta();
      HashMap<Object,Object> map=new HashMap<Object,Object>();
      boolean erroreSupCond=false;
      //Vector<Object> partCond = new Vector<Object>();
      for (int i=0;i<size;i++)
      {
        ParticellaAssVO part= (ParticellaAssVO) particelle.get(i);
        if (!supCondOld.equals(part.getSupCondotta())) erroreSupCond=true;
        map.put(part.getIdUtilizzo() ,null);
      }
      if(map.size()!=size || erroreSupCond)
      {
        //c'è almeno una conduzione con lo stesso utilizzo
        errore=true;
        
        Vector<Object> errors = new Vector<Object>();
        
        String errore1[] = new String[2];
        errore1[0] = "err_supConduzione";
        errore1[1] = "La sup. in conduzione deve essere uguale per la particella che presenta la stessa conduzione";
        
        String errore2[] = new String[2];
        errore2[0] = "err_idTipoUtilizzo";
        errore2[1] = "L'uso primario deve essere univoco all'interno della particella che presenta la stessa conduzione";
        
        if (erroreSupCond) errors.add(errore1);
        
        if (map.size()!=size) errors.add(errore2);
        
        for (int i=0;i<size;i++)
        {
          ParticellaAssVO part= (ParticellaAssVO) particelle.get(i);
          part.setErrori( errors.size() == 0 ? null : (String[][]) errors.toArray(new String[0][]));
        }

      }
    }
    return errore;
  }
  
  
  //controllo che l'oggetto sia uguale solo per quanto concerne i dati catastali
  public boolean equals(Object o) 
  {
    if (o instanceof ParticellaAssVO) 
    {
      ParticellaAssVO other = (ParticellaAssVO)o;
      return( 
         (this.istat == null && other.istat == null || this.istat.equals(other.istat)) &&
         (this.sezione == null && other.sezione == null || this.sezione.equals(other.sezione)) &&
         (this.foglio == null && other.foglio == null || this.foglio.longValue()==other.foglio.longValue()) &&
         (this.particella == null && other.particella == null || this.particella.longValue()==other.particella.longValue()) &&
         (this.subalterno == null && other.subalterno == null || this.subalterno.equals(other.subalterno))
          );
    } else return false;
  }
  
  public boolean equalsForUpadate(Object o) 
  {
    if (this.equals(o))
    {
      ParticellaAssVO other = (ParticellaAssVO)o;
      return( 
         (this.idConduzione == null && other.idConduzione == null || this.idConduzione.equals(other.idConduzione)) &&
         (this.supCondottaB == null && other.supCondottaB == null || this.supCondottaB.compareTo(other.supCondottaB)==0) &&
         (this.idUtilizzo == null && other.idUtilizzo == null || this.idUtilizzo.equals(other.idUtilizzo)) &&
         (this.idVarieta == null && other.idVarieta == null || this.idVarieta.equals(other.idVarieta)) &&
         (this.supUtilizzataB == null && other.supUtilizzataB == null || this.supUtilizzataB.compareTo(other.supUtilizzataB)==0)
          );
    }
    else return false;
  }


  public Long getIdCasoParticolare()
  {
    return idCasoParticolare;
  }

  public void setIdCasoParticolare(Long idCasoParticolare)
  {
    this.idCasoParticolare = idCasoParticolare;
  }

  public String getDescUsoPrimario()
  {
    return descUsoPrimario;
  }

  public void setDescUsoPrimario(String descUsoPrimario)
  {
    this.descUsoPrimario = descUsoPrimario;
  }
 
  
}
