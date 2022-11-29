import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;

//represents a game piece. can be a ship or Bbllet
interface IGamePiece {

  // is the GamePiece a ship?
  boolean isShip();

  // determines if the game piece collided with the given piece
  boolean collision(AGamePiece other);

  // outputs a new AGamePiece in its new position after one tick
  AGamePiece move();

  // determines if a single bullet lies outside of the given width and height
  // of the screen
  boolean isOffScreen();

  // determines if the game piece is the same as the other gamepiece
  boolean samePiece(AGamePiece other);

  // determines if the ship is the same as the other ship
  boolean sameShip(Ship other);

  // determines if the bullet is the same as the other bullet
  boolean sameBullet(Bullet other);

  // draws the game piece
  WorldImage draw();

  // places the gamePiece onto the WorldScene
  // probably incorporates a randomization for the ship
  // all bullets are placed on the same place
  WorldScene place(WorldScene scene);

  // replaces this bullet with new fragmented bullets
  ILoGamePiece fragment();
}

//represents a game piece
abstract class AGamePiece implements IGamePiece {

  // MyPosn loc;
  int xCoord;
  int yCoord;
  int radius;
  int speed;

  AGamePiece(int radius, int xCoord, int yCoord, int speed) {
    this.radius = radius;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.speed = speed;
    // this.theta = theta; // in radians
    // this.velocity =
    // take in a radius, color, etc.
  }

  // is the Gamepiece a ship?
  public abstract boolean isShip();

  // determines if the gamepiece is the same as the given gamepiece
  public abstract boolean samePiece(AGamePiece other);

  // determines if the ship is the same as the given ship
  public abstract boolean sameShip(Ship other);

  // determines if the bullet is the same as the given bullet
  public abstract boolean sameBullet(Bullet other);

  // determines if the game piece collided with the given piece
  public abstract boolean collision(AGamePiece other);

  // outputs a new game piece with its new position after one tick
  public abstract AGamePiece move();

  // determines if a bullet is offscreen given the width and height of screen
  // if a ship is offscreen, then you need to remove it
  // if a bullet is offscreen, you need to remove it
  public boolean isOffScreen() {
    // need to use if statement to check if its a ship
    // access the constant radius accordingly
    return this.xCoord <= -this.radius || this.xCoord >= Constants.SCREEN_WIDTH + this.radius
        || this.yCoord <= -this.radius || this.yCoord >= Constants.SCREEN_HEIGHT + this.radius;
  }

  // draws the game piece as a worldImage
  public abstract WorldImage draw();

  // draws the gamePiece onto a worldScene
  public abstract WorldScene place(WorldScene scene);

}

//represents a ship
class Ship extends AGamePiece {
  Ship(int radius, int xCoord, int yCoord, int speed) {
    super(radius, xCoord, yCoord, speed);
  }

  // is the Ship a ship?
  public boolean isShip() {
    return true;
  }

  // does the game piece collide with the given game piece
  public boolean collision(AGamePiece other) {
    return this.isShip() && !other.isShip() && Math.sqrt(Math.pow(this.xCoord - other.xCoord, 2)
        + Math.pow(this.yCoord - other.yCoord, 2)) <= (this.radius + other.radius);
  }

  // determines if the gamepiece is the same as the given one
  public boolean samePiece(AGamePiece other) {
    return other.sameShip(this);
  }

  // determines if the ship is the same as the given ship
  public boolean sameShip(Ship other) {
    return this.radius == other.radius && this.xCoord == other.xCoord && this.yCoord == other.yCoord
        && this.speed == other.speed;
  }

  // determines if the bullet is the same as the given bullet
  public boolean sameBullet(Bullet other) {
    return false;
  }

  // moves the Ship
  public AGamePiece move() {
    return new Ship(this.radius, this.xCoord + this.speed, this.yCoord, this.speed);

  }

  // draws the ship
  public WorldImage draw() {
    return new CircleImage(this.radius, OutlineMode.SOLID, Constants.SHIP_COLOR);
  }

  // draws the ship onto a given worldscene
  public WorldScene place(WorldScene scene) {
    return scene.placeImageXY(this.draw(), this.xCoord, this.yCoord);
  }

  // a ship does not fragment
  public ILoGamePiece fragment() {
    return new MtLoGamePiece();
  }
}

//represents a bullet
class Bullet extends AGamePiece {
  double angle;
  int multiplier;

  Bullet(int radius, int xCoord, int yCoord, double angle, int speed, int multiplier) {
    super(radius, xCoord, yCoord, speed);
    this.angle = angle;
    this.multiplier = multiplier;
  }

  // is the bullet a ship?
  public boolean isShip() {
    return false;
  }

  // does the game piece collide with the given game piece
  public boolean collision(AGamePiece other) {
    return !this.isShip() && other.isShip() && Math.sqrt(Math.pow(this.xCoord - other.xCoord, 2)
        + Math.pow(this.yCoord - other.yCoord, 2)) <= (this.radius + other.radius);
    // return !this.isShip() && other.isShip()
    // && Math.hypot((Math.abs((double) this.xCoord - (double) other.xCoord)),
    // (Math.abs((double) this.yCoord - (double) other.yCoord))) <= this.radius +
    // other.radius;
  }

  // determines if the gamepiece is the same as the given one
  public boolean samePiece(AGamePiece other) {
    return other.sameBullet(this);
  }

  // determines if the ship is the same as the given ship
  public boolean sameShip(Ship other) {
    return false;
  }

  // determines if the bullet is the same as the given bullet
  public boolean sameBullet(Bullet other) {
    return this.radius == other.radius && this.xCoord == other.xCoord && this.yCoord == other.yCoord
        && this.speed == other.speed && this.angle == angle;
  }

  // moves the bullet
  public AGamePiece move() {
    return new Bullet(radius, this.xCoord + ((int) (this.speed * Math.cos(Math.toRadians(angle)))),
        this.yCoord + (int) (this.speed * Math.sin(Math.toRadians(angle))), this.angle, this.speed,
        this.multiplier);
  }

  // draws the bullet
  public WorldImage draw() {
    return new CircleImage(this.radius, OutlineMode.SOLID, Constants.BULLET_COLOR);
  }

  // places the bullet onto the scene, start from the firing point aka the middle
  // of the screen
  public WorldScene place(WorldScene scene) {
    return scene.placeImageXY(this.draw(), this.xCoord, this.yCoord);
  }

  // fragments the bullets into multiple bullets
  public ILoGamePiece fragment() {
    if (this.multiplier > 8) {
      // max fragments (number of bullets resulting from an explosion) = 8
      // so the game doesn't overload and crash because there are too many bullets at
      // once
      // this is how the actual 10 Bullets game is played
      return this.fragmentAcc(8, 8);
    }
    return this.fragmentAcc(this.multiplier + 1, this.multiplier + 1);
  }

  // fragments the bullets into multiple bullets
  public ILoGamePiece fragmentAcc(int multiplier, int stat) {
    // cap should stop incrementing at 8
    double newAngle = (360 / stat) * multiplier;

    if (multiplier == 1 && this.radius == 10) { // last bullet and max size
      return new ConsLoGamePiece(new Bullet(this.radius, this.xCoord, this.yCoord, newAngle,
          this.speed, this.multiplier + 1), new MtLoGamePiece());
    } else if (multiplier == 1) { // last bullet, not max size
      return new ConsLoGamePiece(new Bullet(this.radius + 2, this.xCoord, this.yCoord, newAngle,
          this.speed, this.multiplier + 1), new MtLoGamePiece());
    } else if (this.radius == 10) { // max size, not last bullet
      return new ConsLoGamePiece(new Bullet(this.radius, this.xCoord, this.yCoord, newAngle,
          this.speed, this.multiplier + 1), this.fragmentAcc(multiplier - 1, stat));
    } else { // not max size or last bullet
      return new ConsLoGamePiece(new Bullet(this.radius + 2, this.xCoord, this.yCoord, newAngle,
          this.speed, this.multiplier + 1), this.fragmentAcc(multiplier - 1, stat));
    }

  }

}

//examples
class ExamplesGamePiece {
  AGamePiece bull = new Bullet(Constants.BULLET_START_RADIUS, 50, 50, 270.0, Constants.BULLET_SPEED,
      1);
  Bullet bullb = new Bullet(Constants.BULLET_START_RADIUS, 50, 50, 270.0, Constants.BULLET_SPEED,
      1);
  AGamePiece bull2 = new Bullet(Constants.BULLET_START_RADIUS, 50, 10, 270.0,
      Constants.BULLET_SPEED, 1);
  AGamePiece s = new Ship(Constants.SHIP_RADIUS, 100, 100, Constants.SHIP_SPEED);
  AGamePiece z = new Ship(Constants.SHIP_RADIUS, 52, 52, Constants.SHIP_SPEED);
  AGamePiece os = new Ship(Constants.SHIP_RADIUS, 500, 1000, Constants.SHIP_SPEED);

  boolean testCollision(Tester t) {
    return t.checkExpect(this.s.collision(this.bull), false)
        && t.checkExpect(this.z.collision(this.bull), true);
  }

  boolean testIsOffScreen(Tester t) {
    return t.checkExpect(this.os.isOffScreen(), true) && t.checkExpect(this.z.isOffScreen(), false)
        && t.checkExpect(this.bull.isOffScreen(), false);
  }

  boolean testIsShip(Tester t) {
    return t.checkExpect(this.bull.isShip(), false) && t.checkExpect(this.s.isShip(), true)
        && t.checkExpect(this.z.isShip(), true) && t.checkExpect(this.os.isShip(), true);
  }

  boolean testMove(Tester t) {
    return t.checkExpect(this.bull.move(),
        new Bullet(Constants.BULLET_START_RADIUS, 50, 42, 270.0, Constants.BULLET_SPEED, 1))
        && t.checkExpect(this.s.move(),
            new Ship(Constants.SHIP_RADIUS, 104, 100, Constants.SHIP_SPEED));
  }

  boolean testSamePiece(Tester t) {
    return t.checkExpect(this.s.samePiece(this.z), false)
        && t.checkExpect(this.z.samePiece(this.s), false)
        && t.checkExpect(this.s.samePiece(this.s), true)
        && t.checkExpect(this.s.samePiece(this.bull), false)
        && t.checkExpect(this.bull.samePiece(this.s), false)
        && t.checkExpect(this.bull.samePiece(this.bull), true)
        && t.checkExpect(this.bull2.samePiece(this.bull), false)
        && t.checkExpect(this.bull.samePiece(this.bull2), false);
  }

  boolean testSameShip(Tester t) {
    // gamepiece.sameShip(ship)
    return t.checkExpect(
        this.s.sameShip(new Ship(Constants.SHIP_RADIUS, 52, 52, Constants.SHIP_SPEED)), false)
        && t.checkExpect(
            this.z.sameShip(new Ship(Constants.SHIP_RADIUS, 100, 100, Constants.SHIP_SPEED)), false)
        && t.checkExpect(
            this.s.sameShip(new Ship(Constants.SHIP_RADIUS, 100, 100, Constants.SHIP_SPEED)), true)
        && t.checkExpect(
            this.bull.sameShip(new Ship(Constants.SHIP_RADIUS, 100, 100, Constants.SHIP_SPEED)),
            false);
  }

  boolean testSameBullet(Tester t) {
    // gamepiece.sameBullet(bullet)
    return t.checkExpect(
        this.s.sameBullet(
            new Bullet(Constants.BULLET_START_RADIUS, 50, 50, 270.0, Constants.BULLET_SPEED, 1)),
        false)
        && t.checkExpect(this.bull.sameBullet(
            new Bullet(Constants.BULLET_START_RADIUS, 50, 50, 270.0, Constants.BULLET_SPEED, 1)),
            true)
        && t.checkExpect(this.bull2.sameBullet(
            new Bullet(Constants.BULLET_START_RADIUS, 50, 50, 270.0, Constants.BULLET_SPEED, 1)),
            false)
        && t.checkExpect(this.bull.sameBullet(
            new Bullet(Constants.BULLET_START_RADIUS, 50, 10, 270.0, Constants.BULLET_SPEED, 1)),
            false);
  }

  boolean testFragment(Tester t) {
    return t.checkExpect(this.bull.fragment(),
        new ConsLoGamePiece(new Bullet(4, 50, 50, 360.0, 8, 2),
            new ConsLoGamePiece(new Bullet(4, 50, 50, 180.0, 8, 2), new MtLoGamePiece())))
        && t.checkExpect(this.s.fragment(), new MtLoGamePiece());
  }

  boolean testFragmentAcc(Tester t) {
    // only used on Bullets (can't call method on an AGamePiece, must necessarily be
    // a Bullet)
    return t.checkExpect(this.bullb.fragmentAcc(2, 2),
        new ConsLoGamePiece(new Bullet(4, 50, 50, 360.0, 8, 2),
            new ConsLoGamePiece(new Bullet(4, 50, 50, 180.0, 8, 2), new MtLoGamePiece())));
  }

  boolean testDraw(Tester t) {
    return t.checkExpect(this.bull.draw(), new CircleImage(2, OutlineMode.SOLID, Color.PINK))
        && t.checkExpect(this.s.draw(), new CircleImage(10, OutlineMode.SOLID, Color.CYAN));
  }

  boolean testPlace(Tester t) {
    WorldScene scene = new WorldScene(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    return t.checkExpect(bull.place(scene),
        scene.placeImageXY(this.bull.draw(), this.bull.xCoord, this.bull.yCoord))
        && t.checkExpect(s.place(scene),
            scene.placeImageXY(this.s.draw(), this.s.xCoord, this.s.yCoord));
  }
}
