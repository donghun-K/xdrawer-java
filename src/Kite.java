import java.awt.Color;
import java.awt.Graphics;
import java.io.Serial;
import java.util.ArrayList;

public class Kite extends Box {

  @Serial
  private static final long serialVersionUID = -4081464961756929206L;
  Circle center;
  Line line1;
  Line line2;
  Line line3;
  Line line4;
  ArrayList<Figure> children = new ArrayList<Figure>();

  Kite(Color color) {
    super(color);
    children.add(center = new Circle(color));
    children.add(line1 = new Line(color));
    children.add(line2 = new Line(color));
    children.add(line3 = new Line(color));
    children.add(line4 = new Line(color));
  }

  Kite(Color color, int x, int y) {
    super(color, x, y);
    children.add(center = new Circle(color, x, y));
    children.add(line1 = new Line(color, x, y));
    children.add(line2 = new Line(color, x, y));
    children.add(line3 = new Line(color, x, y));
    children.add(line4 = new Line(color, x, y));
  }

  Kite(Color color, int x1, int y1, int x2, int y2) {
    super(color, x1, y1, x2, y2);
    int w = Math.abs(x2 - x1);
    int h = Math.abs(y2 - y1);
    int x = Math.min(x1, x2);
    int y = Math.min(y1, y2);

    children.add(center = new Circle(color, x + w / 4, y + h / 4, x + 3 * w / 4, y + 3 * h / 4));
    children.add(line1 = new Line(color, x1, y1, x2, y2));
    children.add(line2 = new Line(color, x1 + w / 2, y1, x1 + w / 2, y2));
    children.add(line3 = new Line(color, x2, y1, x1, y2));
    children.add(line4 = new Line(color, x1, y1 + h / 2, x2, y1 + h / 2));
  }

  void draw(Graphics g) {
    super.draw(g);
    for (Figure child : children) {
      child.draw(g);
    }
  }

  void move(int dx, int dy) {
    super.move(dx, dy);
    for (Figure child : children) {
      child.move(dx, dy);
    }
  }

  Figure copy() {
    Kite newKite = new Kite(color, x1, y1, x2, y2);
    newKite.popup = popup;
    newKite.center.setFillFlag(center.getFillFlag());
    newKite.move(MOVE_DX, MOVE_DY);
    return newKite;
  }

  void setXY2(int newX, int newY) {
    super.setXY2(newX, newY);
    int w = Math.abs(x2 - x1);
    int h = Math.abs(y2 - y1);
    int x = Math.min(x1, x2);
    int y = Math.min(y1, y2);

    center.setXY12(x + w / 4, y + h / 4, x + 3 * w / 4, y + 3 * h / 4);
    line1.setXY12(x1, y1, x2, y2);
    line2.setXY12(x + w / 2, y1, x + w / 2, y2);
    line3.setXY12(x2, y1, x1, y2);
    line4.setXY12(x1, y + h / 2, x2, y + h / 2);
  }

  void setColor(Color color) {
    super.setColor(color);
    for (Figure child : children) {
      child.setColor(color);
    }

  }

  void setFill() {
    center.setFill();
  }
}
