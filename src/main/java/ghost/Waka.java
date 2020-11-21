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
  private boolean frightenGhosts;
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
    this.frightenGhosts = false;
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

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getLives() {
    return this.lives;
  }

  public void setCanTurn(boolean bool) {
    this.canTurn = bool;
  }

  public void setVelocity(int x, int y) {
    this.xVelFuture = x;
    this.yVelFuture = y;
  }

  public void tick() {
    this.move();
    this.checkPosition();
    this.openMouth();
    this.checkDeath();
    frameCounter++;
  }

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

  /* changes Waka's currentSprite based on its current direction or, if its unable to move,
   * based on his future direction */
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

  // checks whether Waka can move, updates canTurn accordingly
  public void checkPosition() {
    // we only check if we are at the center of a grid space
    if ((this.x + 5) % 16 == 0 && (this.y + 5) % 16 == 0) {
      int gridX = (this.x + 5) / 16;
      int gridY = (this.y + 5) / 16;

      // check if a fruit occupies this grid space
      if (this.grid[gridY][gridX] == '7' || this.grid[gridY][gridX] == '8') {
        this.grid[gridY][gridX] = '0'; // no longer a fruit in this space
        if (this.grid[gridY][gridX] == '8') {
          this.frightenGhosts = true;
        }
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
        if (!this.frightenGhosts) {
          this.lives--;
          this.alive = false;
          this.resetPos();
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

  // checks if the char at the specified index represents any wall object in the char matrix
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

  // checks if Waka occupies the same grid space as a ghost
  public boolean checkGhost(int x, int y) {
    for (Ghost ghost : this.ghosts) {
      if (x == ghost.getGridX() && y == ghost.getGridY()) {
        return true;
      }
    }
    return false;
  }

  public void checkDeath() {
    if (this.lives == 0) {
      this.alive = false;
    }
  }

  public void move() {
    if (this.canTurn) {
      this.x += this.xVel;
      this.y += this.yVel;
    }
  }

  // resets Waka's initial position and velocity after colliding
  public void resetPos() {
    this.x = this.initialX;
    this.y = this.initialY;
    this.xVelFuture = -1 * this.speed;
    this.yVelFuture = 0;
    this.alive = true;
  }
}
