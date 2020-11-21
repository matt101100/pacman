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

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getHeight() {
    return this.sprite.height;
  }

  public int getWidth() {
    return this.sprite.width;
  }

  public void draw(PApplet app) {
    app.image(this.sprite, this.x, this.y);
  }
}
