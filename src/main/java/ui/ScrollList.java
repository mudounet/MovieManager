package ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class ScrollList extends JFrame {
    private static final long serialVersionUID = 1L;

  JScrollPane scrollpane;

  public ScrollList() {
    super("JScrollPane Demonstration");
    setSize(300, 200);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    String categories[] = { "Household", "Office", "Extended Family",
        "Company (US)", "Company (World)", "Team", "Will",
        "Birthday Card List", "High School", "Country", "Continent",
        "Planet" };
    JList list = new JList(categories);
    //list.setFixedCellHeight(100);
    list.setCellRenderer(new ui.thumb.JIListRenderer());
    //list.setFixedCellWidth(100);
    list.setLayoutOrientation(JList.VERTICAL_WRAP);
    list.setVisibleRowCount(-1);
    scrollpane = new JScrollPane(list);

    getContentPane().add(scrollpane, BorderLayout.CENTER);
  }

  public static void main(String args[]) {
    ScrollList sl = new ScrollList();
    sl.setVisible(true);
  }
}