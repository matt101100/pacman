package ghost;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import processing.core.PApplet;
import processing.core.PImage;

public class Wall {

  private int x;
  private int y;
  private PImage sprite;

  public Wall(int x, int y, PImage sprite) {
    this.x = x;
    this.y = y;
    this.sprite = sprite;
  }

  public void tick() {
  }

  /**
  * @return Returns the current x-coordinate.
  */
  public int getX() {
    return this.x;
  }
  /**
  * @return Returns the current y-coordinate.
  */
  public int getY() {
    return this.y;
  }

  /**
  * Draws each wall sprite to the screen.
  * @param the PApplet object to which the sprite is drawn.
  */
  public void draw(PApplet app) {
    app.image(this.sprite, this.x, this.y);
  }
}
