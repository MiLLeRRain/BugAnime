/**
 * Plant Class
 */
public class Plant extends Creature {

    protected double speed = 0.5; // Step speed

    /**
     * Constructor
     * @param x position
     * @param y position
     * @param name creature name
     */
    Plant(double x, double y, String name) {
        super(x, y, name, 12);
        this.energyLimit = 20;
        this.matureEnergy = 15;
        this.birthRate = 0.5;
        this.thetaMoveTo = Math.PI / 6;
    }

    /**
     * Move method
     * @param theta the moving direction
     */
    @Override
    public void move(double theta) {
        rotate(theta, true);
        bounce();
        this.setTranslateX(x += speed * Math.cos(thetaMoveTo));
        this.setTranslateY(y += speed * Math.sin(thetaMoveTo));
    }

    /**
     * Give birth: consume energy, update field to store the number of its offspring
     */
    @Override
    public void respawn() {
        updateEnergy(-10);
        this.babies++;
    }

    @Override
    public void rotate(double theta, boolean debug) {
        this.thetaMoveTo = theta;
    }

    /**
     * Grow: increase energy
     */
    @Override
    public void grow() {
        updateEnergy(0.3);
    }

}
