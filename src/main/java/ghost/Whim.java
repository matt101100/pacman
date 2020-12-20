package ghost;

import java.util.List;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;

public class Whim extends Ghost {

  public Whim(int x, int y, int speed, char[][] grid, ArrayList<Long> modeTimes,
              PImage sprite, PImage frightenedSprite, Waka waka) {
    super(x, y, speed, grid, modeTimes, sprite, frightenedSprite, waka);
    this.xVel = -1 * this.speed;
    this.yVel = 0;
  }

  /**
  * Whim's tick() method, handles in-game logic, movement and pathfinding.
  */
  public void tick() {
    this.move();
    this.determineDirection(this.waka);
    this.alternateMode();
  }

  /**
  * Whim's direction-finding method. Checks if Whim can move and makes the required moved
  * based on its current mode.
  * @param waka The waka object that Whim bases its chase target off of.
  */
  public void determineDirection(Waka waka) {
    // change directions only if at the center of a grid space
    if ((this.x + 6) % 16 == 0 && (this.y + 6) % 16 == 0) {
      // grid coordinates
      int gridX = (this.x + 6) / 16;
      int gridY = (this.y + 6) / 16;

      // first check if the ghost should change it's direction
      if (!this.checkJunction(gridX, gridY)) {
        // if the current position is not a junction, we don't do anything
        return;
      } else {
        // otherwise, we determine the future velocities based on the ghost's current mode
        this.canTurn = true;
        if (this.frightened) {
          this.frightenedMode(gridX, gridY);
        } else if (this.scatter) { // scatter mode
          this.offenseMode(gridX, gridY, 28, 36);
        } else if (!this.scatter) { // chase mode
          this.offenseMode(gridX, gridY, this.waka.getGridX(), this.waka.getGridY());
        }
        // making the ghost move
        this.xVel = this.xVelFuture;
        this.yVel = this.yVelFuture;
      }
    }
  }
}
