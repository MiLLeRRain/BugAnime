import javafx.scene.image.Image;

import java.util.Random;

public class Trex extends Creature {
    protected double speed = 1.5;
    protected double detectorRange = 100;
    protected double contactRange = 10;
    protected double thetaMoveTo = - Math.PI / 2; // The angle trex is moving forward to.
    private Image trex = new Image("trex.png");
    private Image trex2 = new Image("trex2.png");

    Trex() {
        super(300, 300, "trex", 30);
        this.energyLimit = 20;
        this.matureEnergy = 15;
        this.birthRate = 0.4;
    }

    Trex(double x, double y) {
        super(x, y, "trex", 30);
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
        if (this.getX() < 0 ||
                this.getX() > this.getParent().getLayoutBounds().getWidth()) {
            this.thetaMoveTo += Math.PI;
        }
        if (this.getY() < 0 ||
                this.getY() > this.getParent().getLayoutBounds().getHeight()) {
//            System.out.println("WHYyyy: Y:"+ this.getY());
            this.thetaMoveTo -= Math.PI;
        }

//        System.out.println(x + "/" + y + " GET " + this.getX() + "/" + this.getY());

        this.setX(x += speed * Math.cos(thetaMoveTo));
        if (debug) this.setY(y -= speed * Math.sin(thetaMoveTo));
        else this.setY(y += speed * Math.sin(thetaMoveTo));

        updateImg();
        updateEnergy(-0.1);//TODO testing

    }

    /**
     * Animation of 2 status of trex.
     */
    private void updateImg() {
        if ((new Random().nextInt(10) % 3) != 0) {
            this.setImage(trex);
        } else this.setImage(trex2);
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

    @Override
    public void eat(Creature target) {
        updateEnergy(target.energy);
    }

    @Override
    public void respawn() {
        updateEnergy(-this.energy / 2);
    }


}
