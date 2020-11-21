package ghost;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;

public class SuperFruit extends Fruit {
  private ArrayList<Ghost> ghosts;

  public SuperFruit(int x, int y, PImage sprite, ArrayList<Ghost> ghosts) {
    super(x, y, sprite);
    this.eaten = false;
    this.ghosts = ghosts;
  }

  public void tick() {
    if (this.eaten) {
      this.frighten();
    }
  }

  public void frighten() {
    for (Ghost ghost : this.ghosts) {
      ghost.frightenGhost();
    }
  }



}
