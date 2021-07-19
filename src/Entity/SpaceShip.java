package Entity;

import GameState.GameState;

import java.awt.*;

public abstract class SpaceShip extends Entity {

    public static final byte SHOOT = 1;

    // pixels per second speed
    protected float speed;

    // flinching or not
    protected boolean flinching;
    protected float flinchTime;

    // amount of lives
    protected int lives;

    // shoot from the left or right side?
    protected boolean shootLeft;

    // number of bullets per shot
    protected int bulletCount;

    public SpaceShip() {
        super();

        // shoot from the left side to begin
        shootLeft = true;
    }

    @Override
    public void update(float dt) {

        // update flinch timer
        if (flinching) {
            flinchTime += dt;

            // only flinch for 2.5 seconds
            if (flinchTime > 2.5f)
                flinching = false;
        }
    }

    @Override
    public void draw(Graphics g) {

        // don't draw every once in a while if flinching
        if (flinching && flinchTime % 0.5f < 0.2f) return;

        // draw sprite
        super.draw(g);

    }

    /** instead of having an on destroy listener, I override
     the on destroy method since the player only loses after
     they lose all their lives */
    @Override
    public void destroy() {

        // if the ship is flinching, it cannot be destroyed
        if (flinching) return;

        // make the ship start flinching
        flinching = true;
        flinchTime = 0;

        // lose a life. if dead, remove it from the game
        setLives(lives - 1);
        if (!isAlive())
            super.destroy();
    }

    /** spawns a bullet at the given position and returns it */
    Bullet spawnBullet(float x, float y, boolean goesDown) {

        invokeListeners(SHOOT);

        // create a bullet with the x position dependent on
        // whether or not the bullet is coming from the left or right
        Bullet bullet = new Bullet(goesDown);
        bullet.setAbsolutePosition(x, y);

        // spawn the bullet
        bullet.spawn();

        // return that bullet
        return bullet;
    }

    void setLives(int lives) { this.lives = lives; }

    /** is the spaceship alive or not? */
    boolean isAlive() { return lives > 0; }

    /** set the speed of the spaceship in pixels per second */
    void setSpeed(float speed) { this.speed = speed; }

    /** set the amount of bullets per shot of the spaceship */
    void setBulletCount(int count) { bulletCount = count; }
    /** returns the amount of bullets per shot */
    int getBulletCount() { return bulletCount; }

}
