package it.csi.smranag.smrgaa.util;


public class FileUtils {
  /**
   * Classe di utilità per JStatTrace
   */
  
  
  public static String getExtension(String nomeFile, String extensionSeparator) 
  {
    int dot = nomeFile.lastIndexOf(extensionSeparator);
    return nomeFile.substring(dot + 1);
  }

  public static String getFilename(String nomeFile, String extensionSeparator, String pathSeparator) 
  { // gets filename without extension
    int dot = nomeFile.lastIndexOf(extensionSeparator);
    int sep = nomeFile.lastIndexOf(pathSeparator);
    return nomeFile.substring(sep + 1, dot);
  }

  public static String getPath(String nomeFile, String pathSeparator) 
  {
    int sep = nomeFile.lastIndexOf(pathSeparator);
    return nomeFile.substring(0, sep);
  }
	
}
