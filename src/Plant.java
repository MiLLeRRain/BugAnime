import javafx.scene.Node;

import java.util.Random;

public class Plant extends Creature {


    Plant() {
        super(500, 500, "plant1", 10);
        this.energyLimit = 20;
        this.matureEnergy = 15;
        this.birthRate = 0.8;
    }

    Plant(double x, double y) {
        super(x, y, "plant1", 12);
        this.energyLimit = 20;
        this.matureEnergy = 15;
        this.birthRate = 0.5;
    }

    Plant(double x, double y, String name) {
        super(x, y, name, 12);
        this.energyLimit = 20;
        this.matureEnergy = 15;
        this.birthRate = 0.5;
    }


    @Override
    public void respawn() {
        updateEnergy(-10);
    }

    @Override
    public void grow() {
        updateEnergy(0.2);
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

}
