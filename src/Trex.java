import javafx.scene.image.Image;
import java.util.Random;

/**
 * Trex (Yoshi) Class
 */
public class Trex extends Creature {

    protected double speed = 1.2; // Step speed
    protected double detectorRange = 150; // Detecting range
    protected double contactRange = 10; // Contact range, eat or mate
    protected double thetaMoveTo = - Math.PI / 2; // The direction this Trex is facing and moving forward to
    protected int feastScore; // The quantity of plants this Trex had eaten before it died.

    // 2 images to animate, when moving right to left
    private Image trex = new Image("trex.png");
    private Image trex2 = new Image("trex2.png");
    // 2 flipped images to animate, when moving right to left
    private Image trexflip = new Image("trexflip.png");
    private Image trex2flip = new Image("trex2flip.png");

    /**
     * Constructor
     * @param x position
     * @param y position
     */
    Trex(double x, double y) {
        super(x, y, "trex", 30);
        this.energy = 10;
        this.energyLimit = 20;
        this.matureEnergy = 15;
        this.birthRate = 0.4;
    }

    /**
     * Move by step and thetaMoveTO.
     * And consume the energy.
     */
    @Override
    public void move(double theta, boolean debug) {


//        System.out.println(theta); //TODO Testing point
        rotate(theta, debug);

//        if (x > 610 || y > 510) throw new RuntimeException("Aahhhhhh!");

//        System.out.println(this.getParent().getLayoutBounds().getWidth());
//        System.out.println(this.getParent().getLayoutBounds().getHeight());
        bounce();

        this.setTranslateX(x += speed * Math.cos(thetaMoveTo));
        // Because of the theta not always give the right positive / negative direction.
        // That's the reason we need to use a debug boolean here.
        // Same feature in rotate() method below.
        if (debug) this.setTranslateY(y -= speed * Math.sin(thetaMoveTo));
        else this.setTranslateY(y += speed * Math.sin(thetaMoveTo));
        boolean flip = speed * Math.cos(thetaMoveTo) < 0;
        updateImg(flip);
        updateEnergy(-0.06);//TODO testing

    }

    /**
     * Animation of 2 status of trex.
     */
    private void updateImg(boolean flip) {
        if (flip) {
            if ((new Random().nextInt(10) % 3) != 0) {
                this.setImage(trexflip);
            } else this.setImage(trex2flip);
        }
        else {
            if ((new Random().nextInt(10) % 3) != 0) {
                this.setImage(trex);
            } else this.setImage(trex2);
        }
    }

    /**
     * Control the thetaMoveTo, and rotate this Image facing to the moving direction.
     */
    @Override
    public void rotate(double theta, boolean debug) {
        this.thetaMoveTo = theta;
        if (debug) this.setRotate(- theta / Math.PI * 180);
        else this.setRotate(theta / Math.PI * 180); //TODO check here
    }

    /**
     * Eat, update energy, update score field
     * @param target the plant been eaten
     */
    @Override
    public void eat(Creature target) {
        updateEnergy(target.energy);
        feastScore++;
    }

    /**
     * Give birth: consume energy, update field to store the number of its offspring
     */
    @Override
    public void respawn() {
        updateEnergy(-this.energy / 3);
        this.babies++;
    }

}
