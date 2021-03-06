import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DrawerView extends JPanel implements MouseListener, MouseMotionListener {

  static class TextEditor extends JTextArea implements DocumentListener, MouseListener {

    private DrawerView view;
    private int INIT_WIDTH = 100;
    private int INIT_HEIGHT = 150;
    private int DELAT = 30;
    private Font font;
    private FontMetrics fm;
    int x;
    int y;
    int width;
    int height;

    TextEditor(DrawerView view) {
      super();
      this.view = view;
      setBackground(view.getBackground());
      getDocument().addDocumentListener(this);
    }

    public void start(int x, int y) {
      setText("");
      font = getFont();
      fm = view.getGraphics().getFontMetrics();
      this.x = x;
      this.y = y;
      this.width = INIT_WIDTH;
      this.height = INIT_HEIGHT;

      setBounds(x, y, width, height);
      Graphics g = view.getGraphics();
      g.setColor(Color.blue);
      g.drawRect(x, y, INIT_WIDTH - 2, INIT_HEIGHT);
      setBorder(BorderFactory.createLineBorder(Color.red));
      setCaretPosition(0);

      view.removeMouseListener(view);
      view.removeMouseMotionListener(view);

      view.add(this);
      requestFocus();

      view.addMouseListener(this);
    }

    public void mouseClicked(MouseEvent e) {
      view.remove(this);
      view.removeMouseListener(this);
      view.addMouseListener(view);
      view.addMouseMotionListener(view);

      String text = getText();
      String[] lines = text.split("\n");
      int w;
      int maxWidth = 0;
      if (lines.length == 1 && lines[0].equals("")) {
        return;
      }

      for (String s : lines) {
        w = fm.stringWidth(s);
        if (w > maxWidth) {
          maxWidth = w;
        }
      }

      int maxHeight = lines.length * fm.getHeight();

      Text newFigure = new Text(Color.black, x, y, x + maxWidth, y + maxHeight, lines);
      newFigure.setPopup(view.getTextPopup());
      view.addFigure(newFigure);

      view.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      String text = getText();
      String[] lines = text.split("\n");

      int w;
      int maxWidth = 0;

      for (String s : lines) {
        w = fm.stringWidth(s);
        if (w > maxWidth) {
          maxWidth = w;
        }
      }
      if (maxWidth > width) {
        width = width + DELTA;
        setBounds(x, y, width, height);
      }
      int maxHeight = lines.length * fm.getHeight();
      if (maxHeight > height) {
        height = height + DELTA;
        setBounds(x, y, width, height);
      }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
  }

  public static int INIT_WIDTH = 3000;
  public static int INIT_HEIGHT = 1500;
  public static int DELTA = 500;

  public static int ID_POINT = 0;
  public static int ID_BOX = 1;
  public static int ID_LINE = 2;
  public static int ID_CIRCLE = 3;
  public static int ID_TV = 4;
  public static int ID_KITE = 5;
  public static int ID_TEXT = 6;
  public static int NOTHING = 0;
  public static int DRAWING = 1;
  public static int MOVING = 2;

  public static String[] figureTypes = {"Point", "Box", "Line", "Circle", "TV", "Kite", "Text"};
  public static ArrayList<String> figureTypeNames = new ArrayList<String>();

  static {
    Collections.addAll(figureTypeNames, figureTypes);
  }

  private int actionMode;
  private int whatToDraw;
  private Figure selectedFigure;
  // polymorphic collection or polymorphic container
  private ArrayList<Figure> figures = new ArrayList<Figure>();

  private int currentX;
  private int currentY;


  private Popup mainPopup;
  private Popup pointPopup;
  private Popup boxPopup;
  private Popup linePopup;
  private Popup circlePopup;
  private Popup tvPopup;
  private Popup kitePopup;
  private Popup textPopup;
  private Popup[] popups = new Popup[figureTypes.length];

  private SelectAction pointAction;
  private SelectAction boxAction;
  private SelectAction lineAction;
  private SelectAction circleAction;
  private SelectAction tvAction;
  private SelectAction kiteAction;
  private SelectAction textAction;

  private DrawerFrame mainFrame;

  private Double zoomRatio = 1.0;

  private int width = INIT_WIDTH;
  private int height = INIT_HEIGHT;

  private TextEditor textEditor;

  DrawerView(DrawerFrame mainFrame) {
    setLayout(null);
    this.mainFrame = mainFrame;

    actionMode = NOTHING;
    selectedFigure = null;

    textEditor = new TextEditor(this);

    pointAction = new SelectAction("Point(P)", new FigureIcon(figureTypes[0]), this, ID_POINT);
    boxAction = new SelectAction("Box(B)", new FigureIcon(figureTypes[1]), this, ID_BOX);
    lineAction = new SelectAction("Line(L)", new FigureIcon(figureTypes[2]), this, ID_LINE);
    circleAction = new SelectAction("Circle(C)", new FigureIcon(figureTypes[3]), this, ID_CIRCLE);
    tvAction = new SelectAction("TV(T)", new FigureIcon(figureTypes[4]), this, ID_TV);
    kiteAction = new SelectAction("Kite(K)", new FigureIcon(figureTypes[5]), this, ID_KITE);
    textAction = new SelectAction("Text(X)", new FigureIcon(figureTypes[6]), this, ID_TEXT);

    mainPopup = new MainPopup(this);
    pointPopup = new FigurePopup(this, "Point", false);
    boxPopup = new FigurePopup(this, "Box", true);
    linePopup = new FigurePopup(this, "Line", false);
    circlePopup = new FigurePopup(this, "Circle", true);
    tvPopup = new TVPopup(this);
    kitePopup = new FigurePopup(this, "Kite", true);
    textPopup = new FigurePopup(this, "Text", false);

    int i = 0;
    popups[i++] = pointPopup;
    popups[i++] = boxPopup;
    popups[i++] = linePopup;
    popups[i++] = circlePopup;
    popups[i++] = tvPopup;
    popups[i++] = kitePopup;
    popups[i] = textPopup;

    addMouseListener(this);
    addMouseMotionListener(this);
    setWhatToDraw(ID_BOX);

    setPreferredSize(new Dimension(width, height));
  }

  public void doFileNew() {
    figures.clear();
    repaint();
  }

  public void doOpen(String fileName) {
    try {
      FileInputStream fis = new FileInputStream(fileName);
      ObjectInputStream ois = new ObjectInputStream(fis);
      figures = (ArrayList<Figure>) ois.readObject();
      ois.close();
      fis.close();
      for (Figure ptr : figures) {
        String figureTypeName = ptr.getClass().getName();
        int index = figureTypeNames.indexOf(figureTypeName);
        ptr.setPopup(popups[index]);
      }
      repaint();
    } catch (IOException ex) {
    } catch (ClassNotFoundException ex) {
    }
  }

  public void doSave(String fileName) {
    try {
      FileOutputStream fos = new FileOutputStream(fileName);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(figures);
      oos.flush();
      oos.close();
      fos.close();
    } catch (IOException ex) {
    }
  }


  public void increaseHeight() {
    height = height + DELTA;
    setPreferredSize(new Dimension(width, height));
  }

  void setWhatToDraw(int figureType) {
    whatToDraw = figureType;
    mainFrame.writeFigureType(figureTypes[whatToDraw]);
  }

  public ArrayList<Figure> getFigures() {
    return figures;
  }

  //  select action getter
  SelectAction getPointAction() {
    return pointAction;
  }

  SelectAction getBoxAction() {
    return boxAction;
  }

  SelectAction getLineAction() {
    return lineAction;
  }

  SelectAction getCircleAction() {
    return circleAction;
  }

  SelectAction getTVAction() {
    return tvAction;
  }

  SelectAction getKiteAction() {
    return kiteAction;
  }

  SelectAction getTextAction() {
    return textAction;
  }

  //  ?????? getter
  public Popup getPointPopup() {
    return pointPopup;
  }

  public Popup getBoxPopup() {
    return boxPopup;
  }

  public Popup getLinePopup() {
    return linePopup;
  }

  public Popup getCirclePopup() {
    return circlePopup;
  }

  public Popup getTVPopup() {
    return tvPopup;
  }

  public Popup getKitePopup() {
    return kitePopup;
  }

  public Popup getTextPopup() {
    return textPopup;
  }

  //  ??? ??????
  void setFigureColor(Color color) {
    if (selectedFigure == null) {
      return;
    }
    selectedFigure.setColor(color);
    repaint();
  }

  void showColorChooser() {
    new JColorChooser();
    Color color = JColorChooser.showDialog(null, "Color Choose", Color.black);
    setFigureColor(color);
  }


  //  paint event ??? ?????? ??????
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    ((Graphics2D) g).scale(zoomRatio, zoomRatio);
    // Collection??? ?????? ?????? ?????? ??????????????? ?????????
    for (Figure pFigure : figures) {
      pFigure.draw(g);
    }
  }

  public void zoom(int ratio) {
    zoomRatio = (double) ratio / 100.0;
    repaint();
    removeMouseListener(this);
    removeMouseMotionListener(this);
    if (ratio == 100) {
      addMouseListener(this);
      addMouseMotionListener(this);
    }
  }

  public void addFigure(Figure newFigure) {
    newFigure.makeRegion();
    figures.add(newFigure);
    repaint();
  }

  public void deleteFigure() {
    if (selectedFigure == null) {
      return;
    }
    figures.remove(selectedFigure);
    selectedFigure = null;
    repaint();
  }

  public void copyFigure() {
    if (selectedFigure == null) {
      return;
    }
    Figure newFigure = selectedFigure.copy();
    addFigure(newFigure);
    selectedFigure = newFigure;
  }

  public void removeFromDialog(int index) {
    if (index < 0) {
      return;
    }
    selectedFigure = null;
    figures.remove(index);
    repaint();
  }

  public void removeFromDialog(Figure ptr) {
    selectedFigure = null;
    figures.remove(ptr);
    repaint();
  }

  public void fillFigure() {
    if (selectedFigure == null) {
      return;
    }
    selectedFigure.setFill();
    repaint();
  }

  private Figure find(int x, int y) {
    for (Figure pFigure : figures) {
      if (pFigure.contains(x, y)) {
        return pFigure;
      }
    }
    return (Figure) null;
  }

  //  TV ??????
  public void onOffTV() {
    if (selectedFigure == null) {
      return;
    }
    if (selectedFigure instanceof TV) {
      ((TV) selectedFigure).pressPowerButton();
      repaint();
    }
  }

  public void setAntenna() {
    if (selectedFigure == null) {
      return;
    }
    if (selectedFigure instanceof TV) {
      ((TV) selectedFigure).setAntenna();
      repaint();
    }
  }

  //  ????????? ????????? ?????????
  public void mousePressed(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();

    if (e.getButton() == MouseEvent.BUTTON3) {
      actionMode = NOTHING;
      return;
    }

    selectedFigure = find(x, y);
    if (selectedFigure != null) {
      actionMode = MOVING;
      currentX = x;
      currentY = y;
      figures.remove(selectedFigure);
      repaint();
      return;
    }

    if (whatToDraw == ID_POINT) {
      selectedFigure = new Point(Color.black, x, y);
      selectedFigure.setPopup(pointPopup);
    } else if (whatToDraw == ID_BOX) {
      selectedFigure = new Box(Color.black, x, y);
      selectedFigure.setPopup(boxPopup);
    } else if (whatToDraw == ID_LINE) {
      selectedFigure = new Line(Color.black, x, y);
      selectedFigure.setPopup(linePopup);
    } else if (whatToDraw == ID_CIRCLE) {
      selectedFigure = new Circle(Color.black, x, y);
      selectedFigure.setPopup(circlePopup);
    } else if (whatToDraw == ID_TV) {
      selectedFigure = new TV(Color.black, x, y, true);
      selectedFigure.setPopup(tvPopup);
      addFigure(selectedFigure);
      selectedFigure = null;
      actionMode = NOTHING;
      return;
    } else if (whatToDraw == ID_KITE) {
      selectedFigure = new Kite(Color.black, x, y);
      selectedFigure.setPopup(kitePopup);
    } else if (whatToDraw == ID_TEXT) {
      selectedFigure = null;
      actionMode = NOTHING;
      textEditor.start(x, y);
      return;
    }
    actionMode = DRAWING;

  }

  public void mouseMoved(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();

    selectedFigure = find(x, y);
    if (selectedFigure != null) {
      setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    } else {
      setCursor(Cursor.getDefaultCursor());
    }
    mainFrame.writePosition("x: " + x + "   y: " + y);
  }

  public void mouseDragged(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();

    Graphics g = getGraphics();
    g.setXORMode(getBackground());
    if (actionMode == DRAWING) {
      selectedFigure.drawing(g, x, y);
    } else if (actionMode == MOVING) {
      selectedFigure.move(g, x - currentX, y - currentY);
      currentX = x;
      currentY = y;
    }
  }

  public void mouseReleased(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();

    //    ????????? ?????????
    if (e.isPopupTrigger()) {
      selectedFigure = find(x, y);
      if (selectedFigure == null) {
        mainPopup.popup(x, y);
      } else {
        selectedFigure.popup(x, y);
      }
      return;
    }

    if (selectedFigure == null) {
      return;
    }

    Graphics g = getGraphics();
    if (actionMode == DRAWING) {
      selectedFigure.setXY2(x, y);
    }
    selectedFigure.draw(g);
    addFigure(selectedFigure);
    selectedFigure = null;
  }

  public void mouseClicked(MouseEvent e) {

  }

  public void mouseEntered(MouseEvent e) {

  }

  public void mouseExited(MouseEvent e) {

  }
}
