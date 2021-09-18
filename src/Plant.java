import javafx.scene.Node;

import java.util.Random;

public class Plant extends Creature {
    protected double speed = 0.5;
    protected double thetaMoveTo = Math.PI / 2;

    Plant(double x, double y, String name) {
        super(x, y, name, 12);
        this.energyLimit = 20;
        this.matureEnergy = 15;
        this.birthRate = 0.5;
    }

    @Override
    public void move(double theta) {
        this.thetaMoveTo = theta;
        if (this.getTranslateX() < 0 ||
                this.getTranslateX() > this.getParent().getLayoutBounds().getWidth()) {
            this.thetaMoveTo += Math.PI;
        }
        if (this.getTranslateY() < 0 ||
                this.getTranslateY() > this.getParent().getLayoutBounds().getHeight()) {
            this.thetaMoveTo -= Math.PI;
        }

        this.setTranslateX(x += speed * Math.cos(thetaMoveTo));
        this.setTranslateY(y += speed * Math.sin(thetaMoveTo));
    }

    @Override
    public void respawn() {
        updateEnergy(-10);
        this.babys++;
    }

    @Override
    public void grow() {
        updateEnergy(0.3);
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

}
