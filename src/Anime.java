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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Assignment for SWEN502 (ICT School). Week 37, 2021.
 * @Author Liam Han (Apang)
 *
 * Background gif image is made by Ben Matthews
 * https://canvasanimations.wordpress.com/2017/10/06/recreating-yoshis-island/
 * All copyrights of the characters used belongs to SUPER MARIO characters © NINTENDO.
 *
 */
public class Anime extends Application {

    // Universal stylish attributes for this application.
    private final int STAGE_WIDTH = 600, STAGE_HEIGHT = 630;
    private final BackgroundFill bgf =  new BackgroundFill(Paint.valueOf("#DAE6F3"), new CornerRadii(5), new Insets(2));
    private final BorderStroke bos = new BorderStroke(Paint.valueOf("#009900"), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2));

    private BorderPane root; // Main Pane for the Stage
    private CreatureControl gameMap; // Game map, and Action controller for all Creature.
    private KeyFrame frame; // Animation KeyFrame for Main Pane
    private Timeline timeline = new Timeline(); // Animation Timeline for Main Pane

    private boolean gameStart = false; // Game status flag for reset feature

    // Default starting parameter
    private int plantQuantity = 10;
    private int trexQuantity = 10;

    // Background Music
    private Media title = new Media(this.getClass().getResource("title.mp3").toExternalForm());
    private Media gaming = new Media(this.getClass().getResource("yoshi.mp3").toExternalForm());
    private Media playerDown = new Media(this.getClass().getResource("gameset.mp3").toExternalForm());
    private MediaPlayer audio = new MediaPlayer(title);

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
        audio.setCycleCount(MediaPlayer.INDEFINITE);
        setBGM();
        audio.play(); //TODO 是否移动下去

//        System.out.println(timeline.getStatus().toString());
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
        speedCtrl.setTooltip(new Tooltip("" + speedCtrl.getValue()));
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
                // 如果timeline在STOPPED， 则重开 相当于RESET， 如果在PAUSED，则进行下一步play
                // 或者用户输入数据改变，则可直接重置
                if (timeline.getStatus().toString().equals("STOPPED")) {
                    gameMap = new CreatureControl(plantQuantity, trexQuantity);
                    root.setCenter(gameMap);
                    frame = new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            if (!gameMap.run()) {
                                timeline.stop(); // 如果返回游戏结束 停止当前timeline回STOPPED状态
                                setBGM(); // 更改游戏为gameover音乐 然后接主题曲 TODO
                            }
                        }
                    });
                    timeline.getKeyFrames().add(frame);
                    timeline.setRate(1);
                    speedCtrl.setValue(1);
                    setBGM(); // 更改音乐为游戏进行曲 TODO
                    gameStart = true;
                }
                timeline.play(); // 此处只有状态为 RUNNING 或 PAUSE才能触发
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
                // 无论何种状态直接重置回初始画面
                timeline.stop();
                setBGM(); // 更改音乐回主题歌 TODO
//                timeline.getKeyFrames().removeAll();
                gameMap = new CreatureControl();
                root.setCenter(gameMap);
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
     * Background Music Player setting
     */
    // audio.stop()都在此处做
    // 几种STOP状态
    // 1. GAMEOVER导致的STOP 需要先播放gameover音乐 然后接主题曲
    // 2. reset导致的STOP 需要直接接主题曲
    // 3. 最早先开始的STOP
    private void setBGM() {
        if (!gameStart) {
            audio.play();
        } else audio.stop();
    }

}
