import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class CreatureControl extends Pane {
    private ArrayList<Plant> plants;
    private ArrayList<Trex> trexes;
    private int plantsLimit = 60;
    private int trexesLimit = 30;

    final private int w = 600, h = 500;

    private Random random = new Random();

    /**
     * For testing only.
     */
    CreatureControl() {
        plants = new ArrayList<>();
        trexes = new ArrayList<>();
        this.setStyle("-fx-background-color: DAE6F3;");    //-fx-background-image: url('dino.jpeg');
        this.setPrefSize(w, h);
        Trex d = new Trex();
        Trex d2 = new Trex();
        trexes.add(d);
        trexes.add(d2);
        Plant p1 = new Plant();
        Plant p2 = new Plant();
        plants.add(p1);
        plants.add(p2);

//        this.add(d, 0, 0);
//        this.add(d2, 20, 20);
//        this.add(p1, 50, 50);
//        this.add(p2, 80, 50);
    }

    /**
     * Construct the game map and initialize the creatures.
     *
     * @param ps quantity of plants
     * @param ts quantity of trexes
     */
    CreatureControl(int ps, int ts) {
        plants = new ArrayList<>();
        trexes = new ArrayList<>();
        this.setStyle("-fx-background-color: DAE6F3;");    //-fx-background-image: url('dino.jpeg');
//        this.setPrefSize(w, h); //TODO is this the right one?
        this.setPrefWidth(w);
        this.setPrefHeight(h);
        setPlants(ps);
        setTrexes(ts);
    }

    /**
     * Randomly placing plants.
     * @param ps quantity of the plants
     */
    private void setPlants(int ps) {
        for (int i = 0; i < ps; i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            Plant p = new Plant(x, y);
            plants.add(p);
            this.getChildren().add(p);
        }
    }

    /**
     * Randomly placing trexes.
     * @param ts quantity of the trexes
     */
    private void setTrexes(int ts) {
        for (int i = 0; i < ts; i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            Trex t = new Trex(x, y);
            trexes.add(t);
            this.getChildren().add(t);
        }
    }

    /**
     * Method to directing the show.
     */
    public void run() {
        if (trexes.isEmpty()) gameOver(); //TODO build later
        processPlants();
        processTrexes();
    }

    /**
     * Show Game Over Banner.
     */
    private void gameOver() {
        Pane gameOver = new HBox();
        gameOver.getChildren().add(new Text("Game Over"));
        this.getChildren().add(gameOver);
    }

    /**
     * Processing plants.
     * Rules:
     * 1. if plant energy <= 0, destroy it, remove from the list and the pane.
     * 2. else if plant energy >= 15, respawn baby trees if size < 50. update energy after respawn.
     * //TODO respawn within a range if can retrieve the x,y.
     * 3. else plan grow.
     */
    private void processPlants() {
        ArrayList<Plant> plantsToRemove = new ArrayList<>();
        ArrayList<Plant> plantsToAdd = new ArrayList<>();

        //Go through plants holding list.
        for (Plant p : plants) {
//            System.out.println("I'm Plant" + plants.indexOf(p) + "Energy" + p.energy);
            //Rule 1 Death control
            if (p.energy <= 0) {
                plantsToRemove.add(p);
                this.getChildren().remove(p);
            }

            //Rule 2 Birth control //TODO debug for Trex? Is this related to plant seeding?
            else if (isReadyToSeeding(p, plantsToAdd)) {
                p.respawn();
                double[] spawnCoord = spawnCoord(p.x, p.y);
                Plant baby = new Plant(spawnCoord[0], spawnCoord[1]);
                plantsToAdd.add(baby);
                this.getChildren().add(baby);
            }

            //Rule 3 Grow control
            else if (p.energy < p.energyLimit) p.grow();
        }

        // Update plants container.
        if (!plantsToRemove.isEmpty()) plants.removeAll(plantsToRemove);
        if (!plantsToAdd.isEmpty()) plants.addAll(plantsToAdd);
    }

    /**
     * Check if the seeding condition is fulfilled
     * @param p is the Plant enquire the permission for seeding
     * @return is the result of the permission.
     */
    private boolean isReadyToSeeding(Plant p, ArrayList<Plant> toAdd) {
        return p.matured() && random.nextDouble() < p.birthRate
                && plants.size() + toAdd.size() < plantsLimit;
    }

    private double[] spawnCoord(double x, double y) {
        // TODO Auto-generated method stub
        double[] toReturn;
        double toReturnX = -42, toReturnY = -42;
        while (toReturnX < 20 || toReturnX > w - 20) {
            toReturnX = x + (random.nextInt(60) - 30);
        }
        while (toReturnY < 20 || toReturnY > h - 20) {
            toReturnY = y + (random.nextInt(60) - 30);
        }
        toReturn = new double[]{toReturnX, toReturnY};
        return toReturn;
    }

    /**
     * Go through trexes.
     * Rules:
     * 1. if energy = 0, destroy it, remove from map.
     * 2. if contact plant, eat, swap energy.
     * 3. if contact other trex, check energy then roll and respawn baby.
     * 4. if there is plant with in range, approach to it.
     * 5. else, move randomly, consume energy.
     */
    private void processTrexes() {
        ArrayList<Trex> trexesToRemove = new ArrayList<>();
        ArrayList<Trex> trexesToAdd = new ArrayList<>();
        ArrayList<Trex> otherTrexes = (ArrayList) trexes.clone();
        // update when detect plant.
        // Theta is the angle for random move or to define later by detected plant.
        double theta;
//        System.out.println(trexes.size());
        for (Trex t : trexes) {

            // Remove this t, to avoid double contact by other trex in loop.
            if (otherTrexes.size() >= 1) otherTrexes.remove(t);

            //TODO Testing
//            System.out.println("I'm Trex"+trexes.indexOf(t) + " Energy" + t.energy);

            // Randomise a moving direction at Rate of 0.5%.
            if (random.nextDouble() < 0.005 && avoidGlitch(t)) theta = random.nextDouble() * 2 * Math.PI; //TODO Testing here
            else theta = t.thetaMoveTo;

            // Sort the plants by distance to the Trex t, and find the target or a contacted one.
            plants.sort(new Comparator<Plant>() {
                @Override
                public int compare(Plant o1, Plant o2) {
                    if (dist(o1, t) < dist(o2, t)) return -1;
                    else if (dist(o1, t) > dist(o2, t)) return 1;
                    return 0;
                }
            });

            Plant detectedPlant = null;
            Plant contactPlant = null;
            double distToPlant = -42;
            // TODO testing length 0.
            if (!plants.isEmpty()) {
                distToPlant = dist(plants.get(0), t);
                if (distToPlant <= t.detectorRange) {
                    detectedPlant = plants.get(0);
                    if (distToPlant <= t.contactRange) {
                        contactPlant = plants.get(0);
                    }
                }
            }

            // Sort the other trexes except this Trex t, and find the contacted one.
            Trex contactTrex = null;
            contactTrex = getContactTrex(otherTrexes, t, contactTrex);


            //Rule 1
            if (t.energy <= 0) {
                trexesToRemove.add(t);
                this.getChildren().remove(t);
            }

            //Rule 2
            else if (contactPlant != null) {
//                System.out.println("Before Eating" + plants.size());
                t.eat(contactPlant);
                contactPlant.die();
//                System.out.println("After Eating" + plants.size());
            }

            //Rule 3
            else if (isReadyToBreed(t, contactTrex)) {
                t.respawn();
                contactTrex.respawn();
                System.out.println("I'm contacted Trex E="+contactTrex.energy);
                Trex baby = new Trex(t.x + (random.nextInt(60) - 30), t.y + (random.nextInt(60) - 30));
                trexesToAdd.add(baby);
                this.getChildren().add(baby);
            }

            //Rule 4
            else if (detectedPlant != null) {
                System.out.println("Detected" + plants.indexOf(detectedPlant)
                +"@" + detectedPlant.x + "/" + detectedPlant.y);
                theta = Math.acos((detectedPlant.x - t.x)/distToPlant);
                System.out.println(t.speed * Math.cos(theta));
                System.out.println(t.speed * Math.sin(theta));
                boolean debug = t.y > detectedPlant.y;
                t.move(theta, debug);
            }

            //Rule 5
            else t.move(theta, true);

        }

        // Updating trexes container
        if (!trexesToRemove.isEmpty()) trexes.removeAll(trexesToRemove);
        if (!trexesToAdd.isEmpty()) trexes.addAll(trexesToAdd);
    }

    /**
     * Check if the Trex has made a successful date with the contacted one.
     * @param t is looking for a mate.
     * @param contactTrex is a potential mate.
     * @return if a baby could be born.
     */
    private boolean isReadyToBreed(Trex t, Trex contactTrex) {
        return contactTrex != null
                && contactTrex.matured() && t.matured()
                && random.nextDouble() < t.birthRate
                && trexes.size() < trexesLimit;
    }

    /**
     * Find a contacted Trex.
     * @param otherTrexes is the List contains non contacted Trexes excluding Trex t.
     * @param t is the Trex that is looking for a mate.
     * @param contactTrex is the contacted Trex.
     * @return
     */
    private Trex getContactTrex(ArrayList<Trex> otherTrexes, Trex t, Trex contactTrex) {
        if (!otherTrexes.isEmpty()) {
            otherTrexes.sort(new Comparator<Trex>() {
                @Override
                public int compare(Trex o1, Trex o2) {
                    if (dist(o1, t) < dist(o2, t)) return -1;
                    else if (dist(o1, t) > dist(o2, t)) return 1;
                    return 0;
                }
            });

            double distToTrex = dist(otherTrexes.get(0), t);

            if (distToTrex <= t.contactRange) {
                contactTrex = otherTrexes.get(0);
            }
        }
        return contactTrex;
    }

    /**
     * A glitch prevent method, Sometime the Trex would be trapped at boundary,
     * while randomized theta keeps pushing them to the border.
     * Or the Plant spawn baby over boundary.
     * @param c is the Creature
     * @return is a boolean to tell if the Creature is getting too close to the border.
     */
    private boolean avoidGlitch(Creature c) {
        return (c.x > 20 && c.x + 20 < w) && (c.y > 20 && c.y + 20 < h);
    }

    /**
     * Calculator for one creature to the other.
     * @param o this creature
     * @param t that creature
     * @return the distance
     */
    private double dist(Creature o, Creature t) {
        return Math.sqrt(Math.pow((o.x - t.x), 2) + Math.pow((o.y - t.y), 2));
    }

}
