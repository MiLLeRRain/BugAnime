import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Creature extends ImageView {

    protected String name;
    protected Image img;
    protected int width;
    protected double energy = 10;
    protected double energyLimit;
    protected double matureEnergy;
    protected double rotateV = 0;
    protected double birthRate;

    protected double x;
    protected double y;

    public Creature() {
    }


    public Creature(double x, double y, String name, int size) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.width = size;
        this.setFitWidth(width);
        this.img = new Image(name+".png");
        this.setImage(img);
        this.setPreserveRatio(true);
        this.setX(x);
        this.setY(y);
    }

    public void move(double theta, boolean debug) {};

    public void rotate(double theta, boolean debug) {};

    /**
     * For testing
     */
    public void updateEnergy(double e) {
        this.energy += e;
        energy = Math.min(energy, energyLimit);
        energy = Math.max(energy, 0);
        updateScale(); //update the size.
    }

    /**
     * Update scale for size growing or dying effect.
     */
    public void updateScale() {
        this.setScaleX(Math.max(0.5, this.energy / 10));
        this.setScaleY(Math.max(0.5, this.energy / 10));
    }

    public abstract void respawn();

    public void eat(Creature target) {};

    public void grow() {};

    public void die() {this.energy = 0;}

    public boolean matured() {return this.energy >= matureEnergy;}

}
