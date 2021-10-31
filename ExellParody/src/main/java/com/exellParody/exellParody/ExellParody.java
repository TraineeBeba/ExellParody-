package com.exellParody.exellParody;

import com.exellParody.parserFiles.GrammarLexer;
import com.exellParody.parserFiles.GrammarParser;
import com.exellParody.parserFiles.ThrowingErrorListener;
import com.exellParody.parserFiles.VisitorClass;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

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

    FileInputStream in = null;
    FileOutputStream out = null;
    private JTable table;
    private String headers[];
    private ListModel lm;
    private DefaultTableModel dm;

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

    private JTextField formula= new JTextField();
    private String[][] formulas=new String[25][25];

    public static double evaluate(String expression) {
        GrammarLexer lexer = new GrammarLexer(new ANTLRInputStream(expression));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ThrowingErrorListener());
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        GrammarParser parser = new GrammarParser(tokenStream);
        ParseTree tree = parser.expression();
        VisitorClass visitor = new VisitorClass();
        return visitor.visit(tree);
    }

    public static String transform(String cell, DefaultTableModel dm){
        for (int k = 0; k < cell.length(); k++) {
            if (cell.charAt(k) == '#') {
                int x = cell.charAt(k + 1) - 65;
                int y = cell.charAt(k + 2) - 49;
                String val = String.valueOf(evaluate('(' + String.valueOf(dm.getValueAt(y, x)) + ')'));
                cell = cell.substring(0, k) + val + cell.substring(k + 3, cell.length());
                k = 0;
            }
        }
        System.out.println(cell);
        return cell;
    }

    public ExellParody() {
        super("Row Header Example");

        String headersCopy[] = { "1", "2", "3", "4", "5", "6",
                                "7", "8", "9", "10", "11", "12", "13",
                                "14", "15", "16", "17", "18", "19",
                                "20", "21", "22", "23", "24", "25"};
        headers = headersCopy;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);

        lm = new AbstractListModel() {

            public int getSize() {
                return headers.length;
            }

            public Object getElementAt(int index) {
                return headers[index];
            }
        };

        dm = new DefaultTableModel(lm.getSize(), 10);
        table = new JTable(dm);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        MouseListener tableMouseListener = new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                int row = table.rowAtPoint(e.getPoint());
                formula.setText(formulas[col][row]);
            }
        };
        table.addMouseListener(tableMouseListener);

        calculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int l=0; l<2; l++){
                    var col=table.getSelectedColumn();
                    var row=table.getSelectedRow();
                    formulas[col][row]=formula.getText();
                    for (int i = 0; i < dm.getRowCount(); i++) {
                        for (int j = 0; j < dm.getColumnCount(); j++) {
                            String cell=formulas[j][i];
                            System.out.println(cell);
                            try {
                                dm.setValueAt(evaluate((transform(cell, dm))), i, j);
                            }catch(Exception ex){
                                //DoNothing
                            }
                        }
                    }
                }
            }
        });

        addrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getRowCount() != 25) {
                    int idx = table.getSelectedRow();
                    // Вставка новой строки после выделенной
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
                    TableColumn col = table.getColumnModel().getColumn(colCnt-1);
                    table.removeColumn(col);
                    dm.setColumnCount(colCnt-1);
                } else {
                    JOptionPane.showMessageDialog(null, "Досягнений мінімальний розмір таблиці");
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        tableButton = new JButton("Table");
        tableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuButton = new JButton("Menu");
                menuButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Box menuContents = new Box(BoxLayout.Y_AXIS);
                        menuContents.add(tableButton);
                        menuContents.add(save);
                        menuContents.add(load);
                        menuContents.add(help);
                        setContentPane(menuContents);
                        setSize(150, 200);
                        setVisible(true);
                    }
                });
                Box tableContents = new Box(BoxLayout.Y_AXIS);
                tableContents.add(scroll);
                tableContents.add(formula);
                tableContents.add(addrow);
                tableContents.add(addСolumn);
                tableContents.add(removeRow);
                tableContents.add(removeCol);
                tableContents.add(calculate);
                tableContents.add(menuButton);
                setContentPane(tableContents);
                setSize(800, 700);
                setVisible(true);
            }
        });

        load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String fileName = JOptionPane.showInputDialog("Enter file name");
                    File file = new File(fileName);
                    if(file.exists()){
                        in = new FileInputStream(file);
                        BufferedReader bi = new BufferedReader(new InputStreamReader(in));
                        int rows = Integer.parseInt(bi.readLine());
                        int columns = Integer.parseInt(bi.readLine());
                        for (int i = 0; i < rows; i++) {
                            for (int j = 0; j < columns; j++) {
                                formulas[i][j] = "";
                            }
                        }
                        for (int i = 0; i < rows; i++) {
                            for (int j = 0; j < columns; j++) {
                                formulas[i][j] = bi.readLine();
                                if(formulas[i][j]==null)
                                    formulas[i][j]="";
                            }
                        }
                        for (int k = 0; k < 2; k++) {
                            for (int i = 0; i < rows ; i++) {
                                for (int j = 0; j < columns; j++) {
                                    String cell=formulas[j][i];
                                    try {
                                        dm.setValueAt(evaluate((transform(cell, dm))), i, j);
                                    }catch(Exception ex){
                                        //DoNothing
                                    }
                                }
                            }}
                    }
                    else throw new Exception();
                }
                catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(null,"ERROR: WRONG FILE NAME");
                }
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String fileName = JOptionPane.showInputDialog("Enter file name");
                    String temp = fileName.substring(fileName.length() - 4);
                    if (temp.equals(".txt")){
                        temp = "";
                        out = new FileOutputStream(fileName);
                        BufferedWriter bo = new BufferedWriter(new OutputStreamWriter(out));
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
                    JOptionPane.showMessageDialog(null,"ERROR: WRONG FILE NAME");
                }
            }
        });
        help = new JButton("Help");
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


        setSize(100, 200);
        ///


        JList rowHeader = new JList(lm);
        rowHeader.setFixedCellWidth(50);
        rowHeader.setFixedCellHeight(table.getRowHeight());
        rowHeader.setCellRenderer(new RowHeaderRenderer(table));
        scroll.setRowHeaderView(rowHeader);

//        Box content = new Box(BoxLayout.Y_AXIS);
//        content.add(scroll);
//        content.add(formula);
//        content.add(calculate);
//        content.add(addСolumn);
//        content.add(addrow);
//        content.add(removeRow);
//        content.add(removeCol);

        //setContentPane(content);



        Box content1 = new Box(BoxLayout.Y_AXIS);
        
        content1.add(tableButton);
        content1.add(save);
        content1.add(load);
        content1.add(help);
        setContentPane(content1);
        setVisible(true);

    }

    public static void main(String[] args) {
        ExellParody frame = new ExellParody();
    }
}