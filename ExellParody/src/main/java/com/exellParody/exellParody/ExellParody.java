package com.exellParody.exellParody;

import com.exellParody.parserFiles.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * @version 1.0 11/09/98
 */

class RowHeaderRenderer extends JLabel implements ListCellRenderer {

    RowHeaderRenderer(JTable table) {
        JTableHeader header = table.getTableHeader();
        setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(CENTER);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

public class ExellParody extends JFrame {

    private final ImageIcon icon1 = new ImageIcon("mong.jpg");
    private final ImageIcon icon2 = new ImageIcon("unnamed.jpg");
    private JPanel background1 = new JPanel( new BorderLayout() ) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(icon1.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };
    private JPanel background2 = new JPanel( new BorderLayout() ) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(icon2.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };
    private FileInputStream input = null;
    private FileOutputStream output = null;
    private String headers[] = { "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "10", "11", "12", "13",
            "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25"};
    private static HashSet<String> set = new HashSet<>();
    private static HashSet<Character> numsToCheck =
            new HashSet<>(Arrays.asList('0','1', '2',
                                        '3', '4', '5',
                                        '6', '7', '8','9'));

    private ListModel lm = lm = new AbstractListModel() {

        public int getSize() {
            return headers.length;
        }

        public Object getElementAt(int index) {
            return headers[index];
        }
    };;

    private DefaultTableModel dm = new DefaultTableModel(lm.getSize(), 10);
    private JTable table = new JTable(dm);
    private JScrollPane scroll = new JScrollPane(table);;

    private JButton calculate = new JButton("Порахувати");
    private JButton addrow = new JButton("Додати строку");
    private JButton addСolumn = new JButton("Додати колонку");
    private JButton removeRow = new JButton("Видалити строку");
    private JButton removeCol = new JButton("Видалити колонку");

    private JButton save;
    private JButton load;
    private JButton tableButton;
    private JButton menuButton;
    private JButton help;

    private JTextField formula = new JTextField();
    private String[][] formulas =new String[25][25];

    public ExellParody() {
        super("ExellParody");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/4, dim.height/9);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        MouseListener tableMouseListener = new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                int row = table.rowAtPoint(e.getPoint());
                formula.setText(formulas[row][col]);
            }
        };

        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.addMouseListener(tableMouseListener);
        table.setCellSelectionEnabled(true);
        setSize(200, 250);


        calculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int col=table.getSelectedColumn();
                int row=table.getSelectedRow();
                try {
                    formulas[row][col]=formula.getText();
                    String temp = formula.getText();
                    temp = Evaluate.link(temp, formulas);
                    dm.setValueAt(Evaluate.evaluate(temp), row, col);
                } catch (Exception ex) {
                    dm.setValueAt("ERROR", row, col);
                }
            }
        });

        addrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getRowCount() != 25) {
                    int idx = table.getSelectedRow();
                    dm.addRow(new Object[]{});
                } else {
                    JOptionPane.showMessageDialog(null,"Досягнено максимальний розмір таблиці");
                }

            }
        });

        addСolumn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getColumnCount() != 26) {
                    String header = String.valueOf((char) (65 + table.getColumnCount()));
                    dm.addColumn(header);
                } else {
                    JOptionPane.showMessageDialog(null,"Досягнено максимальний розмір таблиці");
                }
            }
        });

        removeRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(dm.getRowCount() != 1) {
                    int rowCnt = dm.getRowCount();
                    for (int i = 0; i < dm.getColumnCount() ; i++) {
                        System.out.println(rowCnt + " " + i);
                        formulas[rowCnt - 1][i] = null;
                        table.setValueAt(null, rowCnt - 1, i);
                    }
                    dm.removeRow(table.getRowCount() - 1);
                } else {
                    JOptionPane.showMessageDialog(null, "Досягнений мінімальний розмір таблиці");
                }
            }
        });

        removeCol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int colCnt=dm.getColumnCount();
                if(dm.getColumnCount() != 1) {
                    for (int i = 0; i < dm.getRowCount() ; i++) {
                        System.out.println(i + " " + colCnt);
                        formulas[i][colCnt - 1] = null;
                    }
                    TableColumn col = table.getColumnModel().getColumn(colCnt-1);
                    table.removeColumn(col);
                    dm.setColumnCount(colCnt-1);
                } else {
                    JOptionPane.showMessageDialog(null, "Досягнений мінімальний розмір таблиці");
                }
            }
        });

        tableButton = new JButton("                   Table                  ");
        tableButton.setBackground(Color.white);
        tableButton.setForeground(Color.black);
        tableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuButton = new JButton("Menu");
                menuButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Box menuContents = new Box(BoxLayout.PAGE_AXIS);
                        menuContents.add(tableButton);
                        menuContents.add(save);
                        menuContents.add(load);
                        menuContents.add(help);
                        setContentPane(menuContents);
                        getContentPane().add(background1);
                        setSize(200, 250);
                        setVisible(true);
                    }
                });
                Box table = new Box(BoxLayout.Y_AXIS);
                table.add(scroll);
                setContentPane(table);

                JPanel table1 = new JPanel();
                formula.setPreferredSize(new Dimension(700, 20));
                table1.add(formula);
                getContentPane().add(table1, "South");

                JPanel tableContents = new JPanel();
                tableContents.setSize(800,200);
                tableContents.add(calculate);
                tableContents.add(addrow);
                tableContents.add(addСolumn);
                tableContents.add(removeRow);
                tableContents.add(removeCol);
                tableContents.add(menuButton);
                getContentPane().add(tableContents, "South");
                getContentPane().add(background2);
                setSize(800, 700);
                setVisible(true);
            }
        });

        load = new JButton("                   Load                   ");
        load.setBackground(Color.white);
        load.setForeground(Color.black);
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String fileName = JOptionPane.showInputDialog("Enter file name");
                    File file = new File(fileName);
                    if(file.exists()){
                        input = new FileInputStream(file);
                        BufferedReader bi = new BufferedReader(new InputStreamReader(input));
                        int rows = Integer.parseInt(bi.readLine());
                        int columns = Integer.parseInt(bi.readLine());
                        System.out.println(rows+" "+columns);

                        for (int i = 0; i < rows; i++) {
                            for (int j = 0; j < columns; j++) {
                                formulas[i][j] = bi.readLine();
                                if(formulas[i][j].equals("null"))
                                    formulas[i][j]="";
                            }
                        }

                        for (int i = 0; i < rows ; i++) {
                            for (int j = 0; j < columns; j++) {
                                String cell = formulas[i][j];
                                System.out.println(cell);
                                try {
                                    dm.setValueAt(Evaluate.evaluate((Evaluate.link(cell, formulas))), i, j);
                                } catch (Exception ex) {
                                    if(!formulas[i][j].equals("")) {
                                        dm.setValueAt("ERROR", i, j);
                                    }
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,"ERROR: Enter right File name");
                    }
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"File was not loaded");
                }
                try {
                    input.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        save = new JButton("                  Save                    ");
        save.setBackground(Color.white);
        save.setForeground(Color.black);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String fileName = JOptionPane.showInputDialog("Enter file name");
                    String temp = fileName.substring(fileName.length() - 4);
                    if (temp.equals(".txt")){
                        temp = "";
                        output = new FileOutputStream(fileName);
                        BufferedWriter bo = new BufferedWriter(new OutputStreamWriter(output));
                        bo.write(String.valueOf(dm.getRowCount()));
                        bo.newLine();
                        bo.write(String.valueOf(dm.getColumnCount()));
                        bo.newLine();
                        for (int i = 0; i < dm.getRowCount() ; i++) {
                            for (int j = 0; j < dm.getColumnCount(); j++) {
                                bo.write(String.valueOf(formulas[i][j]));
                                bo.newLine();
                            }
                        }
                        bo.close();
                    }
                    else throw new Exception();
                }
                catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(null,"File was not saved");
                }
            }
        });
        help = new JButton("                   Help                    ");
        help.setBackground(Color.white);
        help.setForeground(Color.black);
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String temp = "Wellcome to Help ( ͡° ͜ʖ ͡°)\n";
                temp += "Доступні операції:\n";
                temp += "+ or - == Додавання та віднімання\n";
                temp += "/ or * == ділення та множення\n";
                temp += "^ == експонента\n";
                temp += "mod or div == ділення з остачею \n";
                temp += "max(x,y) or min(x,y) == мінімму та максимум з х та у\n";
                temp += "(expr) == вираз в лапках\n";
                temp += "\n";
                temp += "Приклад посилання на клітинку:  #A1 \n";
                temp += "Нумо, скористуйтесь ж додатком ( ͡° ͜ʖ ͡°)\n";
                JOptionPane.showMessageDialog(null, temp);
            }
        });

        JList rowHeader = new JList(lm);
        rowHeader.setFixedCellWidth(50);
        rowHeader.setFixedCellHeight(table.getRowHeight());
        rowHeader.setCellRenderer(new RowHeaderRenderer(table));
        scroll.setRowHeaderView(rowHeader);

        Box content1 = new Box(BoxLayout.PAGE_AXIS);
        content1.add(tableButton);
        content1.add(save);
        content1.add(load);
        content1.add(help);
        setContentPane(content1);
        getContentPane().add(background1);
        setVisible(true);

    }

    public static void main(String[] args) {
        ExellParody frame = new ExellParody();
    }
}
