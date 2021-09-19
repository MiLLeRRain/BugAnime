import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Main game Pane for everything to happen.
 */
public class CreatureControl extends Pane {

    // 2 lists hold all living creatures.
    private final ArrayList<Plant> plants;
    private final ArrayList<Trex> trexes;

    // Creature limit for eco-balancing and user visual health
    private final int plantsLimit = 60;
    private final int trexesLimit = 30;

    // Field for score report
    private int totalPlants = 0;
    private int totalTrexs = 0;
    private int heroMamaPlant = 0;
    private int heroMamaTrex = 0;
    private int bestFeast = 0;

    // Size of the world
    private final int GAMEPANE_WIDTH = 600, GAMEPANE_HEIGHT = 500;

    // Random helper
    private final Random random = new Random();

    // Game status flag
    private boolean gameSet = true;

    /**
     * Build an empty scene
     */
    CreatureControl() {
        plants = new ArrayList<>();
        trexes = new ArrayList<>();
        this.setStyle("-fx-background-image: url('bg.gif'); " +
                "-fx-background-repeat: stretch; " +
                "-fx-background-color: DAE6F3; " +
                "-fx-background-position: center center;");
        this.setPrefSize(GAMEPANE_WIDTH, GAMEPANE_HEIGHT);
        showTitle();
    }

    /**
     * Build a title box and show up
     */
    private void showTitle() {
        VBox titleVB = new VBox();
        outputTextSetAndPlay(titleVB, "- YOSHI ISLAND -");
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
        this.setPrefWidth(GAMEPANE_WIDTH);
        this.setPrefHeight(GAMEPANE_HEIGHT);
        initGameMap(ps, ts);
    }

    /**
     * Initial the game map by input quantity of 2 types of creatures
     * @param ps quantity of plants
     * @param ts quantity of trexes
     */
    private void initGameMap(int ps, int ts) {
        setPlants(ps);
        setTrexes(ts);
        gameSet = false;
    }


    /**
     * Randomly placing plants.
     * @param ps quantity of the plants
     */
    private void setPlants(int ps) {
        for (int i = 0; i < ps; i++) {
            int x = random.nextInt(GAMEPANE_WIDTH);
            int y = random.nextInt(GAMEPANE_HEIGHT);
            String name = "plant" + random.nextInt(4);
            Plant p = new Plant(x, y, name);
            plants.add(p);
            this.getChildren().add(p);
            totalPlants++;
        }
    }

    /**
     * Randomly placing trexes.
     *
     * @param ts quantity of the trexes
     */
    private void setTrexes(int ts) {
        for (int i = 0; i < ts; i++) {
            int x = random.nextInt(GAMEPANE_WIDTH);
            int y = random.nextInt(GAMEPANE_HEIGHT);
            Trex t = new Trex(x, y);
            trexes.add(t);
            this.getChildren().add(t);
            totalTrexs++;
        }
    }

    /**
     * Director of the show, call behaviour controller for 2 actors (Plant and Trex)
     */
    public boolean run() {
        processPlants();
        if (!processTrexes() && !gameSet) {
            gameOver();
            return false;
        }
        return true;
    }

    /**
     * Show Game Over Banner.
     */
    private void gameOver() {
        VBox goVB = new VBox();
        outputTextSetAndPlay(goVB, "- GAME OVER -");
        gameSet = true;
        this.setStyle("-fx-background-image: url('bg.gif'); " +
                "-fx-background-repeat: stretch; " +
                "-fx-background-color: DAE6F3; " +
                "-fx-background-position: center center;");

    }

    /**
     * Method to set up an output Text and show up
     * @param goVB the VBox need to be set up
     * @param str the String to be shown
     */
    private void outputTextSetAndPlay(VBox goVB, String str) {
        goVB.setPrefWidth(GAMEPANE_WIDTH);
        goVB.setSpacing(10);
        goVB.setAlignment(Pos.CENTER);
        goVB.setTranslateY(100);

        Group showing = new Group();
        if (str.equals("- GAME OVER -")) showing = statics(str);
        else if (str.equals("- YOSHI ISLAND -")) showing = statics(str);
        goVB.getChildren().add(showing);

        this.getChildren().add(goVB);

        Timeline textAnime = new Timeline();

        Scale scale = new Scale(1, 1, GAMEPANE_WIDTH /2.0, GAMEPANE_HEIGHT /2.0);
        goVB.getTransforms().add(scale);

        KeyValue kv1x = new KeyValue(scale.xProperty(), 0.5);
        KeyValue kv1y = new KeyValue(scale.yProperty(), 0.5);
        KeyFrame kf1 = new KeyFrame(Duration.seconds(0), kv1x, kv1y);

        KeyValue kv2x = new KeyValue(scale.xProperty(), 1);
        KeyValue kv2y = new KeyValue(scale.yProperty(), 1);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(2), kv2x, kv2y);

        textAnime.getKeyFrames().addAll(kf1, kf2);
        textAnime.setAutoReverse(true);
        textAnime.play();
    }

    /**
     * Construct a group of text including:
     * game title or
     * game statics (totalPlants, totalTrexes, heroMamaPlant, heroMamaTrex, bestFeast)
     * @return a group with set texts
     */
    private Group statics(String str) {
        Group toReturn = new Group();

        // Title banner
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.setSpacing(10);
        Text banner = new Text(str);
        banner.setFont(Font.font("Ubuntu Light", FontWeight.BOLD, 30));
        banner.setFill(Paint.valueOf(randomHEXColor()));
        banner.setTextAlignment(TextAlignment.CENTER);

        playForFun(banner);

        hb.getChildren().add(banner);

        // Information
        VBox vb = new VBox();
        if (str.equals("- GAME OVER -")) {
            vb.setLayoutY(40);
            vb.setSpacing(10);
            Text report = new Text();
            report.setText(
                    "\n\nTotal Plants Spawned: " + totalPlants + "\n"
                            + "Total Dinosaurs Spawned: " + totalTrexs + "\n"
                            + "Best mama Plant seeded " + heroMamaPlant + " babies.\n"
                            + "Best mama Dino brood " + heroMamaTrex + " babies.\n"
                            + "\n\n"
                            + "Winner Dino had eaten " + bestFeast + " Plants."
            );
            report.setFont(Font.font("Ubuntu Light", FontWeight.THIN, 16));
            report.setFill(Paint.valueOf(randomHEXColor()));
            vb.getChildren().add(report);
        }

        toReturn.getChildren().addAll(hb, vb);

        return toReturn;
    }

    /**
     * Animate the Text color for opening and ending
     * @param banner is the Text for update
     */
    private void playForFun(Text banner) {
        Timeline fun = new Timeline();
        fun.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                banner.setFill(Paint.valueOf(randomHEXColor()));
            }
        });
        fun.getKeyFrames().add(kf);
        fun.play();
    }

    /**
     * Random a HEX color string
     * from https://mlog.club/article/1029576
     * @return the HEX color string
     */
    private String randomHEXColor() {
        // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
        int nextInt = random.nextInt(0xffffff + 1);
        // format it as hexadecimal string (with hashtag and leading zeros)
        return String.format("#%06x", nextInt);
    }

    /**
     * Processing plants.
     * Rules:
     * 1. if plant energy <= 0, destroy it, remove from the list and the pane.
     * 2. else if plant energy >= 15, respawn baby trees if size < 50. update energy after respawn.
     * //TODO respawn within a range if can retrieve the x,y.
     * 3. else if it is not Grown up plan grow.
     * 4. else move around randomly.
     */
    private void processPlants() {
        Collections.shuffle(plants); // Make processing order randomly, because Trex had sorted the list.
        ArrayList<Plant> plantsToRemove = new ArrayList<>();
        ArrayList<Plant> plantsToAdd = new ArrayList<>();
        double theta;


        //Go through plants holding list.
        for (Plant p : plants) {

            if (random.nextDouble() < 0.01 && avoidGlitch(p))
                theta = random.nextDouble() * 2 * Math.PI; //TODO Testing here
            else theta = p.thetaMoveTo;
//            System.out.println("I'm Plant" + plants.indexOf(p) + "Energy" + p.energy);
            //Rule 1 Death
            if (p.energy <= 0) {
                plantsToRemove.add(p);
                this.getChildren().remove(p);
            }

            //Rule 2 Breed my baby //TODO debug for Trex? Is this related to plant seeding?
            else if (isReadyToSeeding(p, plantsToAdd)) {
                p.respawn();
                double[] spawnCoord = spawnCoord(p.x, p.y);
                String name = "plant" + random.nextInt(4);
                Plant baby = new Plant(spawnCoord[0], spawnCoord[1], name);
                plantsToAdd.add(baby);
                this.getChildren().add(baby);
                totalPlants++;
                heroMamaPlant = Math.max(p.babies, heroMamaPlant);
            }

            //Rule 3 Getting strong
            else if (p.energy < p.energyLimit) p.grow();

            //Ruke 4 Run for your life
            else {
                p.move(theta);
            }
        }

        // Update plants container.
        if (!plantsToRemove.isEmpty()) plants.removeAll(plantsToRemove);
        if (!plantsToAdd.isEmpty()) plants.addAll(plantsToAdd);
    }

    /**
     * Check if the seeding condition is fulfilled
     *
     * @param p is the Plant enquire the permission for seeding
     * @return is the result of the permission.
     */
    private boolean isReadyToSeeding(Plant p, ArrayList<Plant> toAdd) {
        return p.matured() && random.nextDouble() < p.birthRate
                && plants.size() + toAdd.size() < plantsLimit;
    }

    /**
     * Offer a pair of glitch proof spawn coordinates.
     *
     * @param x is the x of mama Plant
     * @param y is the y of mama Plant
     * @return the spot for baby
     */
    private double[] spawnCoord(double x, double y) {
        double[] toReturn;
        double toReturnX = -42, toReturnY = -42;
        while (toReturnX < 20 || toReturnX > GAMEPANE_WIDTH - 20) {
            toReturnX = x + (random.nextInt(60) - 30);
        }
        while (toReturnY < 20 || toReturnY > GAMEPANE_HEIGHT - 20) {
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
    private boolean processTrexes() {
        if (trexes.isEmpty()) return false;
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
            if (random.nextDouble() < 0.005 && avoidGlitch(t))
                theta = random.nextDouble() * 2 * Math.PI; //TODO Testing here
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

            //Rule 1 Death penalty
            if (t.energy <= 0) {
                trexesToRemove.add(t);
                this.getChildren().remove(t);
            }

            //Rule 2 Enjoy a mushroom or a cactus
            else if (contactPlant != null) {
//                System.out.println("Before Eating" + plants.size());
                t.eat(contactPlant);
                contactPlant.die();
//                System.out.println("After Eating" + plants.size());
                bestFeast = Math.max(t.feastScore, bestFeast);
            }

            //Rule 3 Breed a baby
            else if (isReadyToBreed(t, contactTrex)) {
                t.respawn();
                contactTrex.respawn();
//                System.out.println("I'm contacted Trex E="+contactTrex.energy);
                Trex baby = new Trex(t.x + (random.nextInt(60) - 30), t.y + (random.nextInt(60) - 30));
                trexesToAdd.add(baby);
                this.getChildren().add(baby);
                totalTrexs++;
                heroMamaTrex = Math.max(t.babies, heroMamaTrex);
            }

            //Rule 4 Tracing yumyum
            else if (detectedPlant != null && t.energy > 3) { // t.energy > 3, to prevent Trex chansing same target to death.
//                System.out.println("Detected" + plants.indexOf(detectedPlant)
//                +"@" + detectedPlant.x + "/" + detectedPlant.y);
                theta = Math.acos((detectedPlant.x - t.x) / distToPlant);
//                System.out.println(t.speed * Math.cos(theta));
//                System.out.println(t.speed * Math.sin(theta));
                boolean debug = t.y > detectedPlant.y;
                t.move(theta, debug);
            }

            //Rule 5 Walk with no brain
            else t.move(theta, true);

        }

        // Updating trexes container
        if (!trexesToRemove.isEmpty()) trexes.removeAll(trexesToRemove);
        if (!trexesToAdd.isEmpty()) trexes.addAll(trexesToAdd);

        return true;
    }

    /**
     * Check if the Trex has made a successful date with the contacted one.
     *
     * @param t           is looking for a mate.
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
     *
     * @param otherTrexes is the List contains non contacted Trexes excluding Trex t.
     * @param t           is the Trex that is looking for a mate.
     * @param contactTrex is the contacted Trex.
     * @return the Contacted Trex
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
     * For testing
     * <p>
     * A glitch prevent method, Sometime the Trex would be trapped at boundary,
     * while randomized theta keeps pushing them to the border.
     * Or the Plant spawn baby over boundary.
     *
     * @param c is the Creature
     * @return is a boolean to tell if the Creature is getting too close to the border.
     */
    private boolean avoidGlitch(Creature c) {
        return (c.x > 20 && c.x + 20 < GAMEPANE_WIDTH) && (c.y > 20 && c.y + 20 < GAMEPANE_HEIGHT);
    }

    /**
     * Calculator for one creature to the other.
     *
     * @param o this creature
     * @param t that creature
     * @return the distance
     */
    private double dist(Creature o, Creature t) {
        return Math.sqrt(Math.pow((o.x - t.x), 2) + Math.pow((o.y - t.y), 2));
    }


}
