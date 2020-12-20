package ghost;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import processing.core.PApplet;
import processing.core.PImage;

public interface Moveable {

  /**
   * Handles the object's logic that happens per frame
   */
  public void tick();

  /**
  * Handles graphics relating to the object
  *
  * @param app the app to which the object's sprite is drawn on
  */
  public void draw(PApplet app);
}
