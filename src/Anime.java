import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

/**
 * Assignment for SWEN502 (ICT). Week 37, 2021.
 * @Author Liam Han (Apang)
 *
 * Background gif image is made by Ben Matthews
 * https://canvasanimations.wordpress.com/2017/10/06/recreating-yoshis-island/
 *
 * All materials are used for personal only under Terms of Use, https://www.nintendo.com/terms-of-use/
 * © NINTENDO.
 */
public class Anime extends Application {

    // Universal stylish attributes for this application.
    private final int STAGE_WIDTH = 600, STAGE_HEIGHT = 630;
    private final BackgroundFill bgf =  new BackgroundFill(Paint.valueOf("#DAE6F3"), new CornerRadii(5), new Insets(2));
    private final BorderStroke bos = new BorderStroke(Paint.valueOf("#009900"), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2));

    private BorderPane root; // Main Pane for the Stage
    private CreatureControl gameMap; // Game map, and Action controller for all Creature.
    private KeyFrame frame; // Animation KeyFrame for Main Pane
    private final Timeline timeline = new Timeline(); // Animation Timeline for Main Pane

    private boolean gameStart = false; // Help BGM to play correctly

    // Default starting parameter
    private int plantQuantity = 10;
    private int trexQuantity = 10;

    // Background Music
    final private MediaPlayer title = new MediaPlayer(new Media(Objects.requireNonNull(this.getClass().getResource("title.mp3")).toExternalForm()));
    final private MediaPlayer gaming = new MediaPlayer(new Media(Objects.requireNonNull(this.getClass().getResource("yoshi.mp3")).toExternalForm()));
    final private AudioClip playerDown = new AudioClip(Objects.requireNonNull(this.getClass().getResource("gameset.mp3")).toExternalForm());
    boolean bgmHelper = false;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        root = initializeBorderPane();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setWidth(STAGE_WIDTH); stage.setHeight(STAGE_HEIGHT);
        stage.centerOnScreen();
        stage.setTitle("Yoshi Anime");
        stage.show();
    }

    /**
     * Initialize the root BorderPane including timeline and audio to Title status
     * @return root
     */
    private BorderPane initializeBorderPane() {
        BorderPane toReturn = new BorderPane();

        // Set menu pane
        HBox menu = addTopBox();
        toReturn.setTop(menu);

        // Set center pane
        gameMap = new CreatureControl();
        toReturn.setCenter(gameMap);

        // Set bottom pane
        HBox bottom = addBottomBox();
        toReturn.setBottom(bottom);

        // Roll the animation
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Roll the track
        title.setCycleCount(MediaPlayer.INDEFINITE);
        gaming.setCycleCount(MediaPlayer.INDEFINITE);
        playBGM();

        return toReturn;
    }

    /**
     * Create a HBox to contain all control buttons.
     * @return a HBox to the TOP.
     */
    private HBox addTopBox() {

        // Create a menuTab that is holding all menu nodes to return
        HBox menuTab = new HBox();
        menuTab.setStyle("-fx-background-color: DAE6F3;");
        menuTab.setAlignment(Pos.CENTER);
        menuTab.setPadding(new Insets(15, 12, 15, 12));
        menuTab.setSpacing(10);

        // Game status label and Speed controller
        HBox speedTab = new HBox();
        speedTab.setPrefWidth(200);
        speedTab.setSpacing(10);
        speedTab.setAlignment(Pos.CENTER_LEFT);
        Label speedLbl= new Label("Speed:");
        speedLbl.setPrefWidth(80);
        speedLbl.setAlignment(Pos.CENTER);

        Slider speedCtrl = new Slider();
        speedCtrl.setPrefWidth(80);
        speedCtrl.setMin(0); speedCtrl.setMax(2);
        speedCtrl.setValue(1);
        speedCtrl.setTooltip(new Tooltip("Game Speed"));
        speedCtrl.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                timeline.setRate(speedCtrl.getValue());
                gaming.setRate(Math.max(speedCtrl.getValue(), 0.01D));
            }
        });
        speedTab.getChildren().addAll(speedLbl, speedCtrl);
        //TODO get status?
        timeline.statusProperty().addListener(new ChangeListener<Animation.Status>() {
            @Override
            public void changed(ObservableValue<? extends Animation.Status> observableValue, Animation.Status status, Animation.Status t1) {
                speedLbl.setText(t1.toString());
            }
        });

        // Assemble buttons into menuTab
        ButtonBar bb = new ButtonBar();
        Button play = new Button("Play");
        Button pause = new Button("Pause");
        Button reset = new Button("Reset");
        bb.getButtons().addAll(play, pause, reset);

        bb.getButtons().forEach(b -> {
            ((Button) b).setPrefWidth(80);
            ((Button) b).setBackground(new Background(bgf));
            ((Button) b).setBorder(new Border(bos));
        });

        // Set ActionEvent for 3 buttons
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (timeline.getStatus().toString().equals("STOPPED")) {
                    gameMap = new CreatureControl(plantQuantity, trexQuantity);
                    root.setCenter(gameMap);
                    frame = new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            if (!gameMap.run()) {
                                bgmHelper = true;
                                playBGM();
                            }
                        }
                    });
                    if (!timeline.getKeyFrames().isEmpty()) timeline.getKeyFrames().remove(0); // Help to stable the reset
                    timeline.getKeyFrames().add(frame);
                    timeline.setRate(1.0D);
                    gaming.setRate(1.0D);
                    speedCtrl.setValue(1.0D);
                    playBGM();
                    gameStart = true;
                }
                timeline.play();
            }
        });

        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (gameStart) timeline.pause();
            }
        });

        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Reset to title frame in what ever status
                if (gameStart) {
                    timeline.stop();
                    timeline.setRate(1.0D);
                    gaming.setRate(1.0D);
                    speedCtrl.setValue(1.0D);
                    playBGM();
                    gameStart = false;
                    bgmHelper = false;
                    gameMap = new CreatureControl();
                    root.setCenter(gameMap);
                }
            }
        });

        // Assemble all nodes into menuTab.
        menuTab.getChildren().addAll(bb, speedTab);
        menuTab.setBorder(new Border(bos));
        return menuTab;
    }

    /**
     * Set a bottom box for text input area
     * @return a HBox holding 2 text fields
     */
    private HBox addBottomBox() {
        HBox bottom = new HBox();
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.setStyle("-fx-background-color: DAE6F3;");

        HBox pB = new HBox();
        pB.setPrefWidth(300);
        TextField pT = getTextField("Plant", "Enter an Integer number for Plant <=30", "Enter a custom number for Plant quantity.");
        pB.getChildren().add(pT);

        HBox tB = new HBox();
        TextField tT = getTextField("Yoshi", "Enter an Integer number for Yoshi <=30", "Enter a custom number for Yoshi quantity.");
        tB.getChildren().add(tT);

        bottom.getChildren().addAll(pB, tB);
        return bottom;
    }

    /**
     * TestField build helper
     * @param creature is the creature name
     * @param s is the PromptText
     * @param s2 is the TipText
     * @return is finished TextField
     */
    private TextField getTextField(String creature, String s, String s2) {
        TextField tf = new TextField();
        tf.setPrefWidth(300);
        tf.setStyle("-fx-background-color: DAE6F3;");
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.equals("") && !checkInteger(newValue)) tf.setText(oldValue);
                else if (!newValue.equals("")) updateGameCondition(creature, Integer.parseInt(newValue));
            }
        });
        tf.setBorder(new Border(bos));
        tf.setPromptText(s);
        tf.setTooltip(new Tooltip(s2));
        return tf;
    }

    /**
     * Update the starting parameter
     * @param s tells which field argument to change
     * @param v is the number of creature to spawn at the beginning
     */
    private void updateGameCondition(String s, int v) {
        if (s.equals("Plant")) this.plantQuantity = v;
        else this.trexQuantity = v;
    }

    /**
     * Helper to validate input value
     * @param str is the text input from user
     * @return if the input is a legit Integer and number is lower than or equal to 30
     */
    private boolean checkInteger(String str) {
        try {
            return Integer.parseInt(str) <= 30; // Check if the entered value is an Integer and <= 30;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Background Music Player setting\
     * all audio stop() in this method.
     * 1. Fresh start: new thread with no music && gameStart = false, play title
     * 2. Game playing: playing title && gameStart = false, stop title & play gaming
     * 3. GAMEOVER: playing gaming && gameStart = true, stop gaming, play gameover, play title
     * 4. RESET: playing gaming && gameStart = true, stop gaming, play title
     */
    private void playBGM() {
        if (!gameStart) {
            if (title.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                title.stop(); gaming.play();
            }
            else title.play();
        }
        else if (gaming.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            gaming.stop();
            if (gameStart && bgmHelper) playerDown.play();
            title.play();
        }
    }

}
