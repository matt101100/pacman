package ghost;

import java.util.List;
import processing.core.PApplet;
import processing.core.PImage;

public class Waka implements Moveable {

  private int x;
  private int y;
  private int initialX;
  private int initialY;
  private int xVel; // current x velocity
  private int yVel; // current y velocity
  private int xVelFuture; // future x velocity
  private int yVelFuture; // future y velocity
  private int lives;
  private int speed;
  private int frameCounter; // counts what frame we are currently on
  private boolean alive;
  private boolean canTurn;
  private PImage spriteLeft;
  private PImage spriteRight;
  private PImage spriteUp;
  private PImage spriteDown;
  private PImage spriteClosed;
  private PImage currentSprite;
  private char[][] grid; // represents the map Waka can exist on and interact with
  private List<Wall> map;
  private List<Fruit> fruits;
  private List<Ghost> ghosts;


  public Waka(int x, int y, int lives, int speed, PImage spriteLeft, PImage spriteRight,
              PImage spriteUp, PImage spriteDown, PImage spriteClosed, char[][] grid,
              List<Fruit> fruits, List<Ghost> ghosts) {
    this.x = x;
    this.y = y;
    this.initialX = x;
    this.initialY = y;
    this.speed = speed;
    this.xVel = 0;
    this.yVel = 0;
    this.xVelFuture = -1 * this.speed;
    this.yVelFuture = 0;
    this.lives = lives;
    this.frameCounter = 0;
    this.alive = true;
    this.canTurn = true;
    this.spriteLeft = spriteLeft;
    this.spriteRight = spriteRight;
    this.spriteUp = spriteUp;
    this.spriteDown = spriteDown;
    this.spriteClosed = spriteClosed;
    this.currentSprite = this.spriteLeft;
    this.grid = grid;
    this.fruits = fruits;
    this.ghosts = ghosts;
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
  * @return Returns the current grid x-coordinate.
  */
  public int getGridX() {
    return (this.x + 5) / 16;
  }

  /**
  * @return Returns the current y-coordinate.
  */
  public int getGridY() {
    return (this.y + 5) / 16;
  }

  /**
  * @return Returns the current number of lives.
  */
  public int getLives() {
    return this.lives;
  }

  /**
  * Controls whether or not Waka can turn by setting the canTurn boolean.
  * @param bool Represents whether or not Waka can turn.
  */
  public void setCanTurn(boolean bool) {
    this.canTurn = bool;
  }

  /**
  * Sets Waka's future velocity.
  * @param x The future x-velocity.
  * @param y The future y-velocity.
  */
  public void setVelocity(int x, int y) {
    this.xVelFuture = x;
    this.yVelFuture = y;
  }

  /**
  * Tick() method that controls logic and movement.
  */
  public void tick() {
    this.move();
    this.checkPosition();
    this.openMouth();
    this.checkDeath();
    frameCounter++;
  }

  /**
  * Draws Waka's sprite to the screen. Ensures the correct Waka sprite is chosen based on the
  * current frame. Also, ensures Waka is not drawn if it is dead.
  * @param app the PApplet object to which the sprite is drawn.
  */
  public void draw(PApplet app) {
    if (this.alive) {
      if (this.frameCounter > 8 && this.frameCounter <= 16) {
          this.currentSprite = this.spriteClosed;
          app.image(this.currentSprite, this.x, this.y);
          if (this.frameCounter == 16) {
            this.frameCounter = 0; // resetting
          }
      } else {
        app.image(this.currentSprite, this.x, this.y);
      }
    }

    // drawing lives
    int i = 0;
    int j = 0; // handles the x direction
    final int yLifeCoord = 545;
    while (i < this.lives) {
      app.image(this.spriteRight, j, yLifeCoord);
      j += 32;
      i++;
    }
  }

  /** changes Waka's currentSprite based on its current direction or, if its unable to move,
   * based on his future direction.
   */
  public void openMouth() {
    if (this.xVel < 0 && this.yVel == 0) { // left
      this.currentSprite = this.spriteLeft;
    } else if (this.xVel > 0 && this.yVel == 0) { // right
      this.currentSprite = this.spriteRight;
    } else if (this.xVel == 0 && this.yVel < 0) { // up
      this.currentSprite = this.spriteUp;
    } else if (this.xVel == 0 && this.yVel > 0) { // down
      this.currentSprite = this.spriteDown;
    } else if (this.xVel == 0 && this.yVel == 0) { // check future velocities if Waka isn't moving
      if (this.xVelFuture < 0 && this.yVelFuture == 0) {
        this.currentSprite = this.spriteLeft;
      } else if (this.xVelFuture > 0 && this.yVel == 0) {
        this.currentSprite = this.spriteRight;
      } else if (this.xVelFuture == 0 && this.yVelFuture < 0) {
        this.currentSprite = this.spriteUp;
      } else if (this.xVelFuture == 0 && this.yVelFuture > 0) {
        this.currentSprite = this.spriteDown;
      }
    }
  }

  /**
  * Controls Waka's movement. Determines whether Waka is interacting with a Wall, Fruit, superFruit
  * or Ghost object and applies the correct logic to handle each case.
  */
  public void checkPosition() {
    // we only check if we are at the center of a grid space
    if ((this.x + 5) % 16 == 0 && (this.y + 5) % 16 == 0) {
      int gridX = (this.x + 5) / 16;
      int gridY = (this.y + 5) / 16;

      // check if a fruit occupies this grid space
      if (this.grid[gridY][gridX] == '7' || this.grid[gridY][gridX] == '8') {
        this.grid[gridY][gridX] = '0'; // no longer a fruit in this space
        for (Fruit fruit : this.fruits) {
          // we check to see which fruit Waka is currently passing through
          if (fruit.getX() / 16 == gridX && fruit.getY() / 16 == gridY) {
            if (!fruit.isEaten()) { // only update if the fruit isn't already eaten
              fruit.eat();
              break;
            }
          }
        }
      }

      // checking if we occupy the same space as a ghost
      if (this.checkGhost(gridX, gridY)) {
        this.lives--;
        this.alive = false;
        this.resetPos();
      } else {
        for (Ghost ghost : this.ghosts) {
          if (ghost.getGridX() == gridX && ghost.getGridY() == gridY) {
            if (ghost.isAlive()) {
              ghost.eatGhost();
              break;
            }
          }
        }
      }

      // first check if Waka can turn towards the last inputted direction
      if (this.checkGrid(gridX + this.xVelFuture, gridY + this.yVelFuture)) {
        // then check if the current direction is free
        if (this.checkGrid(gridX + this.xVel, gridY + this.yVel)) {
          this.canTurn = false; // if neither are free, Waka doesn't move
          // preventing Waka from moving
          this.xVel = 0;
          this.yVel = 0;
        } else {
          this.canTurn = true; // otherwise, Waka can continue moving in the current direction
        }
      } else { // once we are able to, Waka can turn
        this.xVel = this.xVelFuture;
        this.yVel = this.yVelFuture;
        this.canTurn = true;
      }
    }
  }

  /**
  * A method which checks if a wall object is at the specified coordinates.
  * @param x x-coordinate to check, in grid form.
  * @param y y-coordinate to check, in grid form.
  * @return Returns a boolean representing whether or not a wall exists at the checked location.
  */
  public boolean checkGrid(int x, int y) {
    if (this.grid[y][x] == '1') {
      return true;
    } else if (this.grid[y][x] == '2') {
      return true;
    } else if (this.grid[y][x] == '3') {
      return true;
    } else if (this.grid[y][x] == '4') {
      return true;
    } else if (this.grid[y][x] == '5') {
      return true;
    } else if (this.grid[y][x] == '6') {
      return true;
    } else {
      return false;
    }
  }

  /**
  * A method which checks if a Ghost object is at the specified coordinates.
  * @param x x-coordinate to check, in grid form.
  * @param y y-coordinate to check, in grid form.
  * @return Returns a boolean representing whether or not a wall exists at the checked location.
  */
  public boolean checkGhost(int x, int y) {
    for (Ghost ghost : this.ghosts) {
      if (x == ghost.getGridX() && y == ghost.getGridY()) {
        if (!ghost.isFrightened()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
  * Checks if Waka is dead (i.e. has no lives left).
  */
  public void checkDeath() {
    if (this.lives == 0) {
      this.alive = false;
    }
  }

    /**
    * Handles Waka movement once the velocity has been set.
    */
  public void move() {
    if (this.canTurn) {
      this.x += this.xVel;
      this.y += this.yVel;
    }
  }

  /**
  * Resets Waka's position to the default position once it has been killed by a Ghost.
  */
  public void resetPos() {
    this.x = this.initialX;
    this.y = this.initialY;
    this.xVelFuture = -1 * this.speed;
    this.yVelFuture = 0;
    this.alive = true;
  }
}
