import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Creature class
 */
public abstract class Creature extends ImageView {

    protected String name; // Name
    protected Image img; // Image
    protected int width; // Icon size
    protected double energy = 10; // Creature energy
    protected double energyLimit; // Max energy
    protected double matureEnergy; // Adult flag
    protected double birthRate; // Birthrate to control for balancing eco-system
    protected int babies; // How many offsprings before life ending

    protected double x; // Position
    protected double y; // Position
    protected double thetaMoveTo; // Moving direction

    public Creature() {}

    /**
     * Constructor for creature
     * @param x position
     * @param y position
     * @param name creature name
     * @param size creature image width
     */
    public Creature(double x, double y, String name, int size) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.width = size;
        this.setFitWidth(width);
        this.img = new Image(name+".png");
        this.setImage(img);
        this.setPreserveRatio(true);
        this.setTranslateX(x);
        this.setTranslateY(y);
    }

    public void move(double theta) {}

    public void move(double theta, boolean debug) {}

    public void rotate(double theta, boolean debug) {}

    /**
     * Bouncing back from walls
     */
    public void bounce() {
        if (this.getTranslateX() < 0 ||
                this.getTranslateX() > this.getParent().getLayoutBounds().getWidth()) {
            this.thetaMoveTo += Math.PI;
        }
        if (this.getTranslateY() < 0 ||
                this.getTranslateY() > this.getParent().getLayoutBounds().getHeight()) {
            this.thetaMoveTo -= Math.PI;
        }
    }

    /**
     * Updating energy
     */
    public void updateEnergy(double e) {
        this.energy += e;
        energy = Math.min(energy, energyLimit);
        energy = Math.max(energy, 0);
        updateScale();
    }

    /**
     * Update scale for size growing or hurting effect.
     */
    public void updateScale() {
        this.setOpacity(Math.max(0.2, this.energy / 22));
        this.setScaleX(Math.max(0.5, this.energy / 10));
        this.setScaleY(Math.max(0.5, this.energy / 10));
    }

    public abstract void respawn();

    /**
     * Take over the target's energy
     * @param target is a creature been eaten
     */
    public void eat(Creature target) {}

    /**
     * Energy up
     */
    public void grow() {}

    /**
     * Ending life
     */
    public void die() {this.energy = 0;}

    /**
     * Boolean flag
     * @return if this creature is an adult
     */
    public boolean matured() {return this.energy >= matureEnergy;}

}
