import java.io.Serial;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class MainPopup extends Popup {


  @Serial
  private static final long serialVersionUID = 6722207746813629419L;

  MainPopup(DrawerView view) {
    super(view, "그림");
    JMenuItem pointItem = new JMenuItem(view.getPointAction());
    popupPtr.add(pointItem);
    JMenuItem boxItem = new JMenuItem(view.getBoxAction());
    popupPtr.add(boxItem);
    JMenuItem lineItem = new JMenuItem(view.getLineAction());
    popupPtr.add(lineItem);
    JMenuItem circleItem = new JMenuItem(view.getCircleAction());
    popupPtr.add(circleItem);
    JMenuItem tvItem = new JMenuItem(view.getTVAction());
    popupPtr.add(tvItem);
    JMenuItem kiteItem = new JMenuItem(view.getKiteAction());
    popupPtr.add(kiteItem);
    JMenuItem textItem = new JMenuItem(view.getTextAction());
    popupPtr.add(textItem);
  }
}
