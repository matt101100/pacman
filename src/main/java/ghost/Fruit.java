package ghost;

import java.util.List;
import processing.core.PApplet;
import processing.core.PImage;

public class Fruit {
  protected int x;
  protected int y;
  protected boolean eaten;
  protected PImage sprite;

  public Fruit(int x, int y, PImage sprite) {
    this.x = x;
    this.y = y;
    this.sprite = sprite;
    this.eaten = false;
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
  * @return Returns the whether or not the fruit has been eaten.
  */
  public boolean isEaten() {
    return this.eaten;
  }

  /**
  * Sets the eaten boolean to true.
  */
  public void eat() {
    this.eaten = true;
  }

  public void tick() {

  }

  /**
  * Draws each fruit sprite to the screen.
  * @param the PApplet object to which the sprite is drawn.
  */
  public void draw(PApplet app) {
    if (!this.eaten) {
      app.image(this.sprite, this.x, this.y);
    }
  }
}
