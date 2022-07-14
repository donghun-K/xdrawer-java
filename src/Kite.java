import java.awt.Color;
import java.awt.Graphics;

public class Kite extends Box {

  Circle center;
  Line line1;
  Line line2;
  Line line3;
  Line line4;

  Kite(Color color) {
    super(color);
    center = new Circle(color);
    line1 = new Line(color);
    line2 = new Line(color);
    line3 = new Line(color);
    line4 = new Line(color);
  }

  Kite(Color color, int x, int y) {
    super(color, x, y);
    center = new Circle(color, x, y);
    line1 = new Line(color, x, y);
    line2 = new Line(color, x, y);
    line3 = new Line(color, x, y);
    line4 = new Line(color, x, y);
  }

  Kite(Color color, int x1, int y1, int x2, int y2) {
    super(color, x1, y1, x2, y2);
    int w = Math.abs(x2 - x1);
    int h = Math.abs(y2 - y1);
    int x = Math.min(x1, x2);
    int y = Math.min(y1, y2);

    center = new Circle(color, x + w / 4, y + h / 4, x + 3 * w / 4, y + 3 * h / 4);
    line1 = new Line(color, x1, y1, x2, y2);
    line2 = new Line(color, x1 + w / 2, y1, x1 + w / 2, y2);
    line3 = new Line(color, x2, y1, x1, y2);
    line4 = new Line(color, x1, y1 + h / 2, x2, y1 + h / 2);
  }

  void draw(Graphics g) {
    super.draw(g);
    center.draw(g);
    line1.draw(g);
    line2.draw(g);
    line3.draw(g);
    line4.draw(g);
  }

  void move(int dx, int dy) {
    super.move(dx, dy);
    center.move(dx, dy);
    line1.move(dx, dy);
    line2.move(dx, dy);
    line3.move(dx, dy);
    line4.move(dx, dy);
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
    center.setColor(color);
    line1.setColor(color);
    line2.setColor(color);
    line3.setColor(color);
    line4.setColor(color);

  }

  void setFill() {
    center.setFill();
  }
}
