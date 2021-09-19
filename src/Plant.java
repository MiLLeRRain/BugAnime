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
        this.thetaMoveTo = theta;
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

    /**
     * Grow: increase energy
     */
    @Override
    public void grow() {
        updateEnergy(0.3);
    }

}
