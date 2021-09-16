import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;





public class Main extends Application {
    GridPane grid;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        BorderPane border = new BorderPane();

        HBox hbox = addHBox();
        border.setTop(hbox);
        border.setLeft(addVBox());
        this.grid = addGridPane();
        border.setCenter(grid);
        border.setBottom(addBottomPane());

        Scene scene = new Scene(border,400,400);
//        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();



        Bounds boundsInScene = grid.localToScene(grid.getBoundsInLocal());
        Bounds boundsInScreen = grid.localToScreen(grid.getBoundsInLocal());
        System.out.println(grid.getLayoutBounds());
        System.out.println(boundsInScene);
        System.out.println(boundsInScreen);

    }



    public void createCircles()
    {

    }

    public void moveBugs()
    {

    }


    public HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");



        Button buttonCurrent = new Button("Start Moving");
        buttonCurrent.setPrefSize(100, 20);
        buttonCurrent.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> moveBugs());



        Button btnShow = new Button("Show Bugs");
        btnShow.setPrefSize(100, 20);
        btnShow.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> createCircles());



        hbox.getChildren().addAll(buttonCurrent, btnShow);
        return hbox;
    }

    public VBox addVBox(){
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);



        Text title = new Text("What to add?");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);



        Hyperlink options[] = new Hyperlink[] {
                new Hyperlink("Bee"),
                new Hyperlink("LadyBug"),
                new Hyperlink("Fox"),
                new Hyperlink("Whatever!")};



        for (int i=0; i<4; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);

            final Hyperlink hyperlink = options[i];
            hyperlink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    System.out.println("Hello: " + hyperlink.getText());
                }
            });
        }



        return vbox;
    }



    private HBox addBottomPane()
    {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");
        Text category = new Text("Log..");
        category.setFill(Color.RED);
        category.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        hbox.getChildren().add(category);
        return hbox;
    }

    public GridPane addGridPane() {
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #999999;");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));



        return grid;
    }



    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }
}