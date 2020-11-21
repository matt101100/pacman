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

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public boolean isEaten() {
    return this.eaten;
  }

  public void eat() {
    this.eaten = true;
  }

  public void tick() {

  }

  public void draw(PApplet app) {
    if (!this.eaten) {
      app.image(this.sprite, this.x, this.y);
    }
  }
}
