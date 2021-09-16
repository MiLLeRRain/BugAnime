import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

class CirclePane extends Pane {
    private double radius = 30;

    private float x, y, dx = -1.5f, dy = -1.5f;

    Circle circle;
    Circle circle2;
    Circle circle3;

    Circle[] cl;

    CirclePane() {
        this(100,100);
    }

    CirclePane(float x, float y) {
        this.x = x;
        this.y = y;

        circle = new Circle(x, y, radius);
        circle2 = new Circle(200, 200, radius);
        circle3 = new Circle(300, 300, radius);

        cl = new Circle[]{circle};

        getChildren().addAll(cl);
    }


    public void updateCircle() {

        for (Circle c : cl) {

            if (c.getCenterX() < c.getRadius() ||
                    c.getCenterX() + c.getRadius() > CirclePane.this.getWidth()) {
                dx = -dx;
            }

            if (c.getCenterY() < c.getRadius() ||
                    c.getCenterY() + c.getRadius() > CirclePane.this.getHeight()) {
                dy = -dy;
            }


            c.setCenterX(x += dx);
            c.setCenterY(y += dy);
        }
    }
}