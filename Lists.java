import tester.*;
import javalib.funworld.*;
import java.util.Random;

// to represent all the pieces on the screen as a list
interface ILoGamePiece {

  // moves all game pieces in the list
  ILoGamePiece moveAll();

  // returns the pair of collided pieces and removes them
  ILoGamePiece checkCollInList(AGamePiece other);

  // removes the two collided AGamePiece from the list of pieces
  ILoGamePiece removeViaCollision(ILoGamePiece other);

  // helps remove game pieces through an accumulator
  ILoGamePiece removeViaCollisionHelp(ILoGamePiece other, ILoGamePiece acc);

  // removes the given piece from the list
  ILoGamePiece removeGiven(AGamePiece other);

  // goes through the entire list and removes all collided pieces
  ILoGamePiece removeAllCollision();

  // helps remove all collided pieces with an accumulator
  ILoGamePiece removeAllCollisionHelp(ILoGamePiece track);

  // removes every gamepiece that is offscreen
  ILoGamePiece removeOffScreen();

  // places all game pieces in the list onto the world scene
  WorldScene placeAll(WorldScene scene);

  // places all game pieces in the list onto the world scene via an accumulator
  WorldScene placeAllHelper(WorldScene acc);

  // is the list of pieces empty?
  boolean isEmpty();

  // are there any bullet pieces in the list? (true if no bullets)
  boolean noBulletsLeft();

  // merges the give list with the current list
  ILoGamePiece merge(ILoGamePiece that);

  // gets the total size of the list
  int getSize();

  // finds the total amount of points to increase the score by
  int addAmount();

  // spawns a random amount of ships at random locations
  ILoGamePiece spawn();

  // helps spawn a random amount of ships at random locations
  // via a counter that decrements to keep track of how many ships to create
  ILoGamePiece spawnHelp(int track);

}

//represents an empty list of game pieces
class MtLoGamePiece implements ILoGamePiece {

  // moves all the pieces in the list
  public ILoGamePiece moveAll() {
    return this;
  }

  // removes pieces that are offscreen in the list
  public ILoGamePiece removeOffScreen() {
    return this;
  }

  // places all the pieces in the list onto the worldScene
  public WorldScene placeAll(WorldScene scene) {
    return scene;
  }

  public WorldScene placeAllHelper(WorldScene acc) {
    return acc;
  }

  // checks if a piece collides with any piece in the list
  public ILoGamePiece checkCollInList(AGamePiece other) {
    return this;
  }

  // removes the given piece from the list
  public ILoGamePiece removeGiven(AGamePiece other) {
    return this;
  }

  // removes the two collided AGamePiece from the list of pieces
  public ILoGamePiece removeViaCollision(ILoGamePiece other) {
    return this;
  }

  // helps remove game pieces through an accumulator
  public ILoGamePiece removeViaCollisionHelp(ILoGamePiece other, ILoGamePiece acc) {
    return acc;
  }

  // goes through the entire list and removes all collided pieces
  public ILoGamePiece removeAllCollision() {
    return this;
  }

  // helps remove all collided pieces with an accumulator
  public ILoGamePiece removeAllCollisionHelp(ILoGamePiece track) {
    return track;
  }

  // is the list empty
  public boolean isEmpty() {
    return true;
  }

  // are there any bullet pieces in the list?
  // true for empty case because there is no bullet in it)
  public boolean noBulletsLeft() {
    return true;
  }

  // merges the given list with the current list
  public ILoGamePiece merge(ILoGamePiece that) {
    return that;
  }

  // gets the size of the list
  public int getSize() {
    return 0;
  }

  // finds the total amount to increase the score by
  public int addAmount() {
    return 0;
  }

  // spawns a random amount of ships at random locations
  public ILoGamePiece spawn() {
    // 0 - 2
    int randAmount = new Random().nextInt(3);
    return this.spawnHelp(randAmount);
  }

  // track indicates the amount of ships left to add to the list
  public ILoGamePiece spawnHelp(int track) {
    // should stop producing ships when track == -1
    // only produce 1 ship if track == 0
    int randX = new Random().nextInt(2);
    int randY = new Random().nextInt(Constants.SCREEN_HEIGHT);

    // create one ship and terminate if track == 0
    // should spawn on the left side and move to the right
    if (randX == 0 && track == 0) {
      return new ConsLoGamePiece(new Ship(Constants.SHIP_RADIUS, 15, randY, Constants.SHIP_SPEED),
          this);
    }

    // create one ship that spawns on the right and moves left
    if (randX == 1 && track == 0) {
      return new ConsLoGamePiece(new Ship(Constants.SHIP_RADIUS, Constants.SCREEN_WIDTH - 15, randY,
          Constants.SHIP_SPEED_NEG), this);
    }

    if (randX == 0 && track > 0) {
      return new ConsLoGamePiece(new Ship(Constants.SHIP_RADIUS, 15, randY, Constants.SHIP_SPEED),
          this.spawnHelp(track - 1));
    }
    else {
      return new ConsLoGamePiece(new Ship(Constants.SHIP_RADIUS, Constants.SCREEN_WIDTH - 15, randY,
          Constants.SHIP_SPEED_NEG), this.spawnHelp(track - 1));
    }
  }

}

//represents a non-empty list of game pieces
class ConsLoGamePiece implements ILoGamePiece {
  AGamePiece first;
  ILoGamePiece rest;

  ConsLoGamePiece(AGamePiece first, ILoGamePiece rest) {
    this.first = first;
    this.rest = rest;
  }

  // moves all the pieces in the list
  public ILoGamePiece moveAll() {
    return new ConsLoGamePiece(this.first.move(), this.rest.moveAll());
  }

  // removes all offscreen pieces in the list
  public ILoGamePiece removeOffScreen() {
    if (this.first.isOffScreen()) {
      return this.rest.removeOffScreen();
    }
    else {
      return new ConsLoGamePiece(this.first, this.rest.removeOffScreen());
    }
  }

  // removes the two collided AGamePiece from the list of pieces
  // other should be the main list of game pieces
  // public ILoGamePiece removeViaCollision(ILoGamePiece other) {
  // return
  // }
  //
  // // removes the given piece from the list
  // public ILoGamePiece removeGiven(AGamePiece other) {
  // if(this.first.eq)
  // }

  // places all pieces in the list onto the worldScene
  // the scene needs to be updated everytime you put a single gamepiece, so an
  // accumulator is best?
  public WorldScene placeAll(WorldScene scene) {
    return this.placeAllHelper(scene);
  }

  // places all pieces in the list onto the WorldScene using an accumulator
  public WorldScene placeAllHelper(WorldScene acc) {
    return this.rest.placeAllHelper(this.first.place(acc));
  }

  // returns the pair of collided game pieces as a list if there is a collision
  // if not, then an empty list should be returned
  public ILoGamePiece checkCollInList(AGamePiece other) {
    // AGamePiece firstPiece;
    // AGamePiece secondPiece;
    if (this.first.collision(other)) {
      return new ConsLoGamePiece(other, new ConsLoGamePiece(this.first, new MtLoGamePiece()));
    }
    else {
      // if there is no collision, then the exact same list is returned
      return this.rest.checkCollInList(other);
    }
  }

  // removes the given piece from the list
  // this is called in removeAll
  public ILoGamePiece removeGiven(AGamePiece other) {
    if (this.first.samePiece(other)) {
      return this.first.fragment().merge(this.rest.removeGiven(other));
    }
    else {
      // if the given game piece is not in the list, then return the same list
      return new ConsLoGamePiece(this.first, this.rest.removeGiven(other));
    }
  }

  // removes the two collided AGamePiece from the list of pieces
  // probably need an accumulator to update the list after removing the first one
  // other.removeGiven(this.first)
  // which would be stored in an accumulator
  // this.rest.removeViaCollision(ILoGamePiece other)
  // the list that the method is called on should be the pair of collided pieces
  // that need to be removed
  public ILoGamePiece removeViaCollision(ILoGamePiece other) {
    // other is the list of all gamem pieces
    return this.removeViaCollisionHelp(other, other);
  }

  // helps remove game pieces through an accumulator
  // for one collision, a bullet and a ship are removed
  // the list that the method is called on should be the pair of collided pieces
  // that need to be removed
  public ILoGamePiece removeViaCollisionHelp(ILoGamePiece other, ILoGamePiece acc) {
    return this.rest.removeViaCollisionHelp(other, acc.removeGiven(this.first));
  }

  // goes through the entire list of game pieces and removes all collided pieces
  public ILoGamePiece removeAllCollision() {
    return this.removeAllCollisionHelp(this);
    // checkCollInList(this.first).removeViaCollision(this)
    // this line should return the list of game pieces with the collided ones
    // removed
  }

  // helps remove all collided pieces with an accumulator
  // need to typecheck if the accumulator is empty or not
  // if track is empty, then the recursion should stop
  // track should maybe be the updated list with removed pieces
  public ILoGamePiece removeAllCollisionHelp(ILoGamePiece track) {
    if (this.checkCollInList(first).isEmpty()) {
      return this.rest.removeAllCollisionHelp(track);
    }
    return this.rest
        .removeAllCollisionHelp(this.checkCollInList(this.first).removeViaCollision(track));
  }

  // is the list empty
  public boolean isEmpty() {
    return false;
  }

  // spawns a random amount of ships at random locations
  public ILoGamePiece spawn() {
    // 0 - 2
    int randAmount = new Random().nextInt(3);
    return this.spawnHelp(randAmount);
  }

  // spawns a random amount of ships at random locations by using track as an
  // accumulator
  // that decrements
  public ILoGamePiece spawnHelp(int track) {
    // should stop producing ships when track == -1
    // only produce 1 ship if track == 0
    int randX = new Random().nextInt(2);
    int randY = new Random().nextInt(Constants.SCREEN_HEIGHT);

    // create one ship and terminate if track == 0
    // should spawn on the left side and move to the right
    if (randX == 0 && track == 0) {
      return new ConsLoGamePiece(new Ship(Constants.SHIP_RADIUS, 15, randY, Constants.SHIP_SPEED),
          this);
    }

    // create one ship that spawns on the right and moves left
    if (randX == 1 && track == 0) {
      return new ConsLoGamePiece(new Ship(Constants.SHIP_RADIUS, Constants.SCREEN_WIDTH - 15, randY,
          Constants.SHIP_SPEED_NEG), this);
    }

    if (randX == 0 && track > 0) {
      return new ConsLoGamePiece(new Ship(Constants.SHIP_RADIUS, 15, randY, Constants.SHIP_SPEED),
          this.spawnHelp(track - 1));
    }
    else {
      return new ConsLoGamePiece(new Ship(Constants.SHIP_RADIUS, Constants.SCREEN_WIDTH - 15, randY,
          Constants.SHIP_SPEED_NEG), this.spawnHelp(track - 1));
    }
  }

  // are there any bullet pieces in the list? (true if no bullets)
  public boolean noBulletsLeft() {
    return this.first.isShip() && this.rest.noBulletsLeft();
  }

  // merges the given list with the current list
  public ILoGamePiece merge(ILoGamePiece that) {
    return new ConsLoGamePiece(this.first, that.merge(this.rest));
  }

  // gets the size of the list (only counting ships)
  public int getSize() {
    if (this.first.isShip()) {
      return 1 + this.rest.getSize();
    }
    else {
      return this.rest.getSize();
    }
  }

  // finds the total amount to increase the score by
  public int addAmount() {
    return this.getSize() - this.removeAllCollision().getSize();
  }

}

//examples
class ExamplesList {

  AGamePiece bull = new Bullet(Constants.BULLET_START_RADIUS, 50, 50, 270.0, Constants.BULLET_SPEED,
      1);
  AGamePiece s = new Ship(Constants.SHIP_RADIUS, 100, 100, Constants.SHIP_SPEED);
  AGamePiece z = new Ship(Constants.SHIP_RADIUS, 52, 52, Constants.SHIP_SPEED);
  AGamePiece so = new Ship(Constants.SHIP_RADIUS, 104, 1000, Constants.SHIP_SPEED);
  AGamePiece a = new Bullet(Constants.BULLET_START_RADIUS, 100, 100, 45.0, Constants.BULLET_SPEED,
      1);
  AGamePiece ax = new Bullet(Constants.BULLET_START_RADIUS, 50, 50, 180.0, Constants.BULLET_SPEED,
      1);
  AGamePiece az = new Bullet(Constants.BULLET_START_RADIUS, 30, 30, 270.0, Constants.BULLET_SPEED,
      1);
  AGamePiece bo = new Bullet(Constants.BULLET_START_RADIUS, 1000, 10, 180.0, Constants.BULLET_SPEED,
      1);
  ILoGamePiece mt = new MtLoGamePiece();
  ILoGamePiece full = new ConsLoGamePiece(this.bull, new ConsLoGamePiece(this.s,
      new ConsLoGamePiece(this.z, new ConsLoGamePiece(this.a, this.mt))));
  ILoGamePiece woo = new ConsLoGamePiece(this.bull, new ConsLoGamePiece(this.z, this.mt));
  ILoGamePiece nob = new ConsLoGamePiece(this.s, new ConsLoGamePiece(this.z, this.mt));
  ILoGamePiece bruhnob = new ConsLoGamePiece(this.s,
      new ConsLoGamePiece(this.z, new ConsLoGamePiece(bull, this.mt)));
  ILoGamePiece howdy = new ConsLoGamePiece(this.ax, new ConsLoGamePiece(this.az, this.mt));
  ILoGamePiece mo = new ConsLoGamePiece(this.bull, new ConsLoGamePiece(this.bo,
      new ConsLoGamePiece(this.z, new ConsLoGamePiece(this.so, this.mt))));
  WorldScene scene = new WorldScene(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

  boolean testMerge(Tester t) {
    return t.checkExpect(this.full.merge(this.howdy),
        new ConsLoGamePiece(this.bull,
            new ConsLoGamePiece(this.ax,
                new ConsLoGamePiece(this.s,
                    new ConsLoGamePiece(this.az,
                        new ConsLoGamePiece(this.z, new ConsLoGamePiece(this.a, this.mt)))))))
        && t.checkExpect(this.mt.merge(this.mt), this.mt)
        && t.checkExpect(this.full.merge(this.mt), this.full)
        && t.checkExpect(this.mt.merge(this.full), this.full);
  }

  boolean testRemoveGiven(Tester t) {
    return t.checkExpect(this.full.removeGiven(this.s),
        new ConsLoGamePiece(this.bull,
            new ConsLoGamePiece(this.z, new ConsLoGamePiece(this.a, this.mt))))
        && t.checkExpect(this.nob.removeGiven(this.z),
            new ConsLoGamePiece(this.s, new MtLoGamePiece()));
  }

  boolean testRemoveAllCollision(Tester t) {
    return t.checkExpect(this.full.removeAllCollision(), new ConsLoGamePiece(
        new Bullet(4, 50, 50, 360.0, 8, 2),
        new ConsLoGamePiece(new Bullet(4, 50, 50, 180.0, 8, 2),
            new ConsLoGamePiece(new Bullet(4, 100, 100, 360.0, 8, 2),
                new ConsLoGamePiece(new Bullet(4, 100, 100, 180.0, 8, 2), new MtLoGamePiece())))))
        && t.checkExpect(this.mt.removeAllCollision(), this.mt)
        && t.checkExpect(this.nob.removeAllCollision(), this.nob);
  }

  boolean testRemoveViaCollision(Tester t) {
    return t.checkExpect(this.woo.removeViaCollision(this.full), new ConsLoGamePiece(
        new Bullet(4, 50, 50, 360.0, 8, 2),
        new ConsLoGamePiece(new Ship(10, 100, 100, 4),
            new ConsLoGamePiece(new Bullet(4, 50, 50, 180.0, 8, 2),
                new ConsLoGamePiece(new Bullet(2, 100, 100, 45.0, 8, 1), new MtLoGamePiece())))))
        && t.checkExpect(this.mt.removeViaCollision(this.full), this.mt);
  }

  boolean testCheckInColl(Tester t) {
    return t.checkExpect(this.full.checkCollInList(this.bull), this.woo)
        && t.checkExpect(this.mt.checkCollInList(a), new MtLoGamePiece());
  }

  boolean testMoveAll(Tester t) {
    // new ConsLoGamePiece(this.first.move(), this.rest.moveAll())
    return t.checkExpect(this.mt.moveAll(), this.mt) && t.checkExpect(this.full.moveAll(),
        new ConsLoGamePiece(
            new Bullet(Constants.BULLET_START_RADIUS, 50, 42, 270.0, Constants.BULLET_SPEED, 1),
            new ConsLoGamePiece(new Ship(Constants.SHIP_RADIUS, 104, 100, Constants.SHIP_SPEED),
                new ConsLoGamePiece(new Ship(Constants.SHIP_RADIUS, 56, 52, Constants.SHIP_SPEED),
                    new ConsLoGamePiece(new Bullet(Constants.BULLET_START_RADIUS, 105, 105, 45.0,
                        Constants.BULLET_SPEED, 1), this.mt)))));
  }

  boolean testRemoveViaCollisionHelp(Tester t) {
    return t.checkExpect(this.mt.removeViaCollisionHelp(this.full, this.bruhnob), this.bruhnob)
        && t.checkExpect(this.nob.removeViaCollisionHelp(this.full, this.full),
            new ConsLoGamePiece(bull, new ConsLoGamePiece(a, new MtLoGamePiece())));
  }

  boolean testRemoveAllCollisionHelp(Tester t) {
    return t.checkExpect(this.mt.removeAllCollisionHelp(this.woo), this.woo)
        && t.checkExpect(this.full.removeAllCollisionHelp(this.full),
            new ConsLoGamePiece(new Bullet(4, 50, 50, 360.0, 8, 2),
                new ConsLoGamePiece(new Bullet(4, 50, 50, 180.0, 8, 2),
                    new ConsLoGamePiece(new Bullet(4, 100, 100, 360.0, 8, 2),
                        new ConsLoGamePiece(new Bullet(4, 100, 100, 180.0, 8, 2),
                            new MtLoGamePiece())))))
        && t.checkExpect(this.woo.removeAllCollisionHelp(this.mt), this.mt);
  }

  boolean testRemoveOffScreen(Tester t) {
    // mt, list w all on, list w mix of ships and bullets off
    return t.checkExpect(this.mt.removeOffScreen(), this.mt)
        && t.checkExpect(this.full.removeOffScreen(), this.full)
        && t.checkExpect(this.howdy.removeOffScreen(), this.howdy)
        && t.checkExpect(this.mo.removeOffScreen(),
            new ConsLoGamePiece(this.bull, new ConsLoGamePiece(this.z, this.mt)));
  }

  boolean testIsEmpty(Tester t) {
    return t.checkExpect(this.mt.isEmpty(), true) && t.checkExpect(this.full.isEmpty(), false);
  }

  boolean testNoBulletsLeft(Tester t) {
    // mt, list w bullet, list w/o bullet, list w mix
    return t.checkExpect(this.mt.noBulletsLeft(), true)
        && t.checkExpect(this.howdy.noBulletsLeft(), false)
        && t.checkExpect(this.nob.noBulletsLeft(), true)
        && t.checkExpect(this.woo.noBulletsLeft(), false);
  }

  boolean testGetSize(Tester t) {
    return t.checkExpect(this.mt.getSize(), 0) && t.checkExpect(this.full.getSize(), 2)
        && t.checkExpect(this.woo.getSize(), 1) && t.checkExpect(this.nob.getSize(), 2)
        && t.checkExpect(this.howdy.getSize(), 0);
  }

  boolean testAddAmount(Tester t) {
    // mt, list w collision, list w/o collision
    return t.checkExpect(this.mt.addAmount(), 0) && t.checkExpect(this.full.addAmount(), 2)
        && t.checkExpect(this.woo.addAmount(), 1) && t.checkExpect(this.mo.addAmount(), 1);
  }

  boolean testPlaceall(Tester t) {
    return t.checkExpect(this.mt.placeAll(this.scene), this.scene)
        && t.checkExpect(this.nob.placeAll(this.scene),
            this.scene.placeImageXY(s.draw(), 100, 100).placeImageXY(z.draw(), 52, 52))
        && t.checkExpect(this.bruhnob.placeAll(this.scene),
            this.scene.placeImageXY(s.draw(), 100, 100).placeImageXY(z.draw(), 52, 52)
                .placeImageXY(this.bull.draw(), 50, 50));

  }

  boolean testPlaceAllHelper(Tester t) {
    return t.checkExpect(this.mt.placeAllHelper(this.scene), this.scene)
        && t.checkExpect(this.nob.placeAllHelper(this.scene),
            this.scene.placeImageXY(s.draw(), 100, 100).placeImageXY(z.draw(), 52, 52))
        && t.checkExpect(this.bruhnob.placeAllHelper(this.scene),
            this.scene.placeImageXY(this.s.draw(), 100, 100).placeImageXY(this.z.draw(), 52, 52)
                .placeImageXY(this.bull.draw(), 50, 50));

  }

}
