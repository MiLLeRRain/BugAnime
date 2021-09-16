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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.ArrayList;

public class Anime extends Application {

    final int STAGE_WIDTH = 600, STAGE_HEIGHT = 600;

    CreatureControl gameMap;

    private int plantQuantity = 10; //TODO user input later
    private int trexQuantity = 10; //TODO user input later


    Timeline timeline = new Timeline();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane root = new BorderPane();
        root.setPrefSize(STAGE_WIDTH, STAGE_HEIGHT);

        timeline.setCycleCount(Timeline.INDEFINITE);

        HBox menu = addHBox();

        root.setTop(menu);

//        Image dino = new Image("dino.jpeg");
//        ImageView iv = new ImageView();
//        iv.setImage(dino);
//        iv.setFitWidth(50);
//        iv.setPreserveRatio(true);
//
//        root.setCenter(iv);

//        final CirclePane circle = new CirclePane();
//        root.setCenter(circle);



        gameMap = new CreatureControl(plantQuantity, trexQuantity); //TODO 后期由用户界面添加

        root.setCenter(gameMap);
        root.getCenter().prefWidth(stage.getWidth());root.getCenter().prefHeight(stage.getHeight()-40);

        KeyFrame frame = new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                gameMap.run(); //TODO 全部由CC内控制

            }
        });

        timeline.getKeyFrames().addAll(frame);
        timeline.setAutoReverse(true);
        timeline.play();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(STAGE_WIDTH); stage.setHeight(STAGE_HEIGHT);
        stage.centerOnScreen();
        stage.setTitle("Dinosaur Anime");
        stage.show();
    }

    /**
     * Create a HBox to contain all control buttons.
     * @return a HBox to the TOP.
     */
    private HBox addHBox() {
        HBox hb = new HBox();
        //Text aniStatus = new Text("Anime ");
        hb.setStyle("-fx-background-color: DAE6F3;");
        hb.setAlignment(Pos.CENTER);
        hb.setPadding(new Insets(15, 12, 15, 12));
        hb.setSpacing(10);

        Button play = new Button("Play");
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                timeline.play();
            }
        });

        Button pause = new Button("Pause");
        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                timeline.pause();
            }
        });

        Button stop = new Button("Stop");
        stop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                timeline.stop();
            }
        });


        Group btns = new Group();
        btns.getChildren().add(play);


        Text speed = new Text("Game Speed: ");

        Slider playbackSpeed = new Slider();
        playbackSpeed.setMin(0); playbackSpeed.setMax(5);
        playbackSpeed.setValue(0.5);
        playbackSpeed.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                timeline.setRate(playbackSpeed.getValue());
            }
        });

        //TODO get status?
        timeline.statusProperty().addListener(new ChangeListener<Animation.Status>() {
            @Override
            public void changed(ObservableValue<? extends Animation.Status> observableValue, Animation.Status status, Animation.Status t1) {
                System.out.println(t1.toString());
            }
        });
        hb.getChildren().addAll(play, pause, stop, speed, playbackSpeed);

        return hb;
    }
}
