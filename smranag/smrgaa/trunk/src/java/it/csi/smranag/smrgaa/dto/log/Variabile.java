package it.csi.smranag.smrgaa.dto.log;

import java.io.Serializable;

/**
 * Classe di utilità che rappresenta una variabile un metodo. Viene utilizzato
 * dai metodi di logging per ricevere l'elenco delle variabili di un metodo quando
 * si verifica un errore e bisogna stamparle.
 * @author TOBECONFIG
 * @since 1.0
 */
public class Variabile implements Serializable
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -4835724540697548318L;
  /**
   * Nome del Variabile
   */
  private String nome;
  /**
   * Valore del Variabile
   */
  private Object valore;

  public Variabile(String nome, Object valore)
  {
    this.nome = nome;
    this.valore = valore;
  }

  public Variabile(String nome, long valore)
  {
    this.nome = nome;
    this.valore = new Long(valore);
  }

  public Variabile(String nome, int valore)
  {
    this.nome = nome;
    this.valore = new Long(valore);
  }

  public Variabile(String nome, boolean valore)
  {
    this.nome = nome;
    this.valore = new Boolean(valore);
  }

  public Variabile(String nome, char valore)
  {
    this.nome = nome;
    this.valore = String.valueOf(valore);
  }

  public Variabile(String nome, double valore)
  {
    this.nome = nome;
    this.valore = new Double(valore);
  }

  public Variabile(String nome, float valore)
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
