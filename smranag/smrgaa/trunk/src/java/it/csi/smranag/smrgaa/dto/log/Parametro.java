package it.csi.smranag.smrgaa.dto.log;

import java.io.Serializable;

/**
 * Classe di utilità che rappresenta un parametro un metodo. Viene utilizzato
 * dai metodi di logging per ricevere l'elenco dei parametri di un metodo quando
 * si verifica un errore e bisogna stamparli....
 * @author TOBECONFIG
 * @since 1.0
 */
public class Parametro implements Serializable
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -4835724540697548318L;
  /**
   * Nome del parametro
   */
  private String nome;
  /**
   * Valore del parametro
   */
  private Object valore;

  public Parametro(String nome, Object valore)
  {
    this.nome = nome;
    this.valore = valore;
  }

  public Parametro(String nome, long valore)
  {
    this.nome = nome;
    this.valore = new Long(valore);
  }

  public Parametro(String nome, int valore)
  {
    this.nome = nome;
    this.valore = new Long(valore);
  }

  public Parametro(String nome, boolean valore)
  {
    this.nome = nome;
    this.valore = new Boolean(valore);
  }

  public Parametro(String nome, char valore)
  {
    this.nome = nome;
    this.valore = String.valueOf(valore);
  }

  public Parametro(String nome, double valore)
  {
    this.nome = nome;
    this.valore = new Double(valore);
  }

  public Parametro(String nome, float valore)
  {
    this.nome = nome;
    this.valore = new Float(valore);
  }

  public String getNome()
  {
    return nome;
  }

  public Object getValore()
  {
    return valore;
  }
}
