package tud.ai2.solitaire.exceptions;

import java.io.IOException;

/**
 * Diese Exception wird geworfen, falls die gewünschte Resource bzw. das Asset
 * nicht gefunden werden kann.
 */
public class ResourceNotFoundException extends IOException{

  public ResourceNotFoundException(Throwable e) {
    super(e);
  }

  public ResourceNotFoundException(String string) {
    super(string);
  }

  public ResourceNotFoundException(String string, Throwable e) {
    super(string, e);
  }
}
