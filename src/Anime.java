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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Anime extends Application {

    // Universal stylish attributes for this application.
    final int STAGE_WIDTH = 600, STAGE_HEIGHT = 630;
    final BackgroundFill bgf =  new BackgroundFill(Paint.valueOf("#DAE6F3"), new CornerRadii(5), new Insets(2));
    final BorderStroke bos = new BorderStroke(Paint.valueOf("#009900"), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2));

    BorderPane root; // Main Pane for the Stage
    CreatureControl gameMap = new CreatureControl(); // Game map, and Action controller for all Creature.
    KeyFrame frame; // Animation KeyFrame for Main Pane
    Timeline timeline = new Timeline(); // Animation Timeline for Main Pane
    MediaPlayer audio; // Background Music

    boolean gameStart = false; // Game status flag for reset feature

    // Default starting parameter
    private int plantQuantity = 10;
    private int trexQuantity = 10;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        // Set root BorderPane
        root = new BorderPane();
        root.setPrefSize(STAGE_WIDTH, STAGE_HEIGHT);

        // Set menu pane
        HBox menu = addTopBox();
        root.setTop(menu);

        // Set center pane
        root.setCenter(gameMap);
        root.getCenter().prefWidth(stage.getWidth());
        root.getCenter().prefHeight(stage.getHeight());

        // Set bottom pane
        HBox bottom = addBottomBox();
        root.setBottom(bottom);

        timeline.setCycleCount(Timeline.INDEFINITE);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(STAGE_WIDTH); stage.setHeight(STAGE_HEIGHT);
        stage.centerOnScreen();
        stage.setTitle("Yoshi Anime");
        stage.show();
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
        menuTab.setPadding(new Insets(8, 12, 8, 12));
        menuTab.setPrefHeight(40); //TODO height
        menuTab.setSpacing(10);


        // Game status label and Speed controller
        HBox speedTab = new HBox();
        speedTab.setPrefWidth(200);
        speedTab.setSpacing(10);
        speedTab.setAlignment(Pos.CENTER_LEFT);
        Label speedLbl= new Label("Speed: ");
        speedLbl.setPrefWidth(80);
        speedLbl.setPrefHeight(24); //TODO height
        speedLbl.setAlignment(Pos.CENTER);

        Slider speedCtrl = new Slider();
        speedCtrl.setPrefWidth(80);
        speedCtrl.setMin(0); speedCtrl.setMax(2);
        speedCtrl.setValue(1);
        speedCtrl.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                timeline.setRate(speedCtrl.getValue());
                audio.setRate(speedCtrl.getValue());
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

        bb.setStyle("-fx-background-color: DAE6F3;");
        bb.getButtons().forEach(b -> {
            ((Button) b).setPrefWidth(80);
            ((Button) b).setBackground(new Background(bgf));
            ((Button) b).setBorder(new Border(bos));
        });

        // Set ActionEvent for 3 buttons
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!gameStart) {
                    gameMap = new CreatureControl(plantQuantity, trexQuantity);
                    root.setCenter(gameMap);
                    frame = new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            if (!gameStart) return;
                            gameMap.run(); //TODO Fully controlled by CreatureControl
                        }
                    });
                    timeline.getKeyFrames().add(frame);
                    setBGM();
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
                if (gameStart) {
                    setBGM();
                    timeline.stop();
                    timeline.getKeyFrames().remove(frame);
                    gameMap = new CreatureControl(plantQuantity, trexQuantity);
                    gameStart = false;
                }
                root.setCenter(gameMap);
                frame = new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        gameMap.run();
                    }
                });
                timeline.getKeyFrames().add(frame);
                timeline.setRate(1);
                speedCtrl.setValue(1);
                setBGM();
                gameStart = true;
                timeline.play();
            }
        });

        // Assemble all nodes into menuTab.

        menuTab.getChildren().addAll(bb, speedTab);
        menuTab.setBorder(new Border(bos));
        return menuTab;
    }

    /**
     * Set a bottom box for text input area.
     * @return a HBox holding 2 text fields.
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

    private void updateGameCondition(String s, int v) {
        if (s.equals("Plant")) this.plantQuantity = v;
        else this.trexQuantity = v;
    }

    private boolean checkInteger(String str) {
        try {
            return Integer.parseInt(str) <= 30; // Check if the entered value is an Integer and <= 30;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Background Music Player setting
     */
    private void setBGM() {
        if (!gameStart) {
            Media buzzer = new Media(this.getClass().getResource("yoshi.mp3").toExternalForm());
            audio = new MediaPlayer(buzzer);
            audio.setCycleCount(MediaPlayer.INDEFINITE);
            audio.play();
        } else audio.stop();
    }
}
