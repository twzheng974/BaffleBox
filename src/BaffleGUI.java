import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Point;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;

// 
// Decompiled by Procyon v0.5.30
// 

public class BaffleGUI extends JFrame
{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int OFFSET = 20;
    private static final int LABEL_SIDE = 40;
    private static final int COMP_WIDTH = 240;
    private static final int COMP_HEIGHT = 40;
    private static final String RULES = "[RULES]\n1. Shoot a laser by typing a number (0-39) in the text box below the shoot button as your starting position (denoted in black). The laser should end up on the opposite side of the grid unless there is a baffle in the way, which will deflect the laser.\n2. Guess the position of the baffle by typing row index, column index, and baffle type into the three textboxes below the guess button. Row and column index increases top to bottom and left to right respectively, starting at 1. A correct guess will reveal that baffle ('\\' is from top left to bottom right; '/' is from top right to bottom left)\n3. Click on the history button to see what you did previously.\n\n[SCORING GUIDE]\nShoot: -1 point\nCorrect Guess: +7 points\nIncorrect Guess: -3 points";
    private JPanel panel;
    private JPanel promptPanel;
    private JLabel[][] grid;
    private Point[][] gridCoords;
    private JTextArea gameMsg;
    private JScrollPane gameScr;
    private JLabel score;
    private JLabel remaining;
    private JButton shootButton;
    private JButton guessButton;
    private JButton historyButton;
    private JTextField shootTF;
    private JTextField guessRowTF;
    private JTextField guessColTF;
    private JTextField guessTypeTF;
    private ArrayList<String> history;
    private String message;
    private int difficulty;
    private Icon empty;
    private Icon slash1;
    private Icon slash3;
    private ShootButtonHandler sbHandler;
    private GuessButtonHandler gbHandler;
    private HistoryButtonHandler hbHandler;
    private ClassLoader cl;
    private BaffleBox box;
    
    public BaffleGUI() {
        this.promptDiff();
        this.cl = this.getClass().getClassLoader();
        this.empty = new ImageIcon(this.cl.getResource("img/blank.png"));
        this.slash1 = new ImageIcon(this.cl.getResource("img/backslash.png"));
        this.slash3 = new ImageIcon(this.cl.getResource("img/foreslash.png"));
        this.box = new BaffleBox(this.difficulty);
        this.gridCoords = new Point[12][12];
        int x = 20;
        int y = 20;
        for (int r = 0; r < this.gridCoords.length; ++r) {
            x = 20;
            for (int c = 0; c < this.gridCoords[r].length; ++c) {
                this.gridCoords[r][c] = new Point(x, y);
                x += 40;
            }
            y += 40;
        }
        this.history = new ArrayList<String>();
        this.initDisplay();
        this.setDefaultCloseOperation(3);
    }
    
    public void displayGame() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                BaffleGUI.this.setVisible(true);
            }
        });
    }
    
    public void initDisplay() {
        this.panel = new JPanel() {
            public void paintComponent(final Graphics g) {
                super.paintComponent(g);
            }
        };
        this.setSize(new Dimension(800, 600));
        this.setTitle("BaffleBox");
        this.panel.setLayout(null);
        this.grid = new JLabel[12][12];
        for (int r = 0; r < this.grid.length; ++r) {
            for (int c = 0; c < this.grid[r].length; ++c) {
                this.grid[r][c] = new JLabel("", 0);
                this.panel.add(this.grid[r][c]);
                this.grid[r][c].setBounds(this.gridCoords[r][c].x, this.gridCoords[r][c].y, 40, 40);
                if (this.box.getGrid().getElement(r, c) == -1) {
                    this.grid[r][c].setText("");
                }
                else if (r > 0 && r < 11 && c > 0 && c < 11) {
                    this.grid[r][c].setIcon(this.empty);
                }
                else {
                    this.grid[r][c].setText(new StringBuilder().append(this.box.getGrid().getElement(r, c)).toString());
                }
            }
        }
        this.score = new JLabel("Score: " + this.box.getScore());
        this.panel.add(this.score);
        this.score.setBounds(520, 20, 120, 20);
        this.score.setVisible(true);
        this.remaining = new JLabel("Baffles Remaining: " + this.box.getRemaining());
        this.panel.add(this.remaining);
        this.remaining.setBounds(520, 40, 240, 20);
        this.remaining.setVisible(true);
        this.shootButton = new JButton("Shoot");
        this.sbHandler = new ShootButtonHandler();
        this.shootButton.addActionListener(this.sbHandler);
        this.panel.add(this.shootButton);
        this.shootButton.setBounds(520, 70, 240, 40);
        this.shootTF = new JTextField("Shoot Location (0-39)");
        this.panel.add(this.shootTF);
        this.shootTF.setBounds(520, 120, 240, 40);
        this.guessButton = new JButton("Guess");
        this.gbHandler = new GuessButtonHandler();
        this.guessButton.addActionListener(this.gbHandler);
        this.panel.add(this.guessButton);
        this.guessButton.setBounds(520, 170, 240, 40);
        this.guessRowTF = new JTextField("Row (1-10)");
        this.panel.add(this.guessRowTF);
        this.guessRowTF.setBounds(520, 220, 80, 40);
        this.guessColTF = new JTextField("Column (1-10)");
        this.panel.add(this.guessColTF);
        this.guessColTF.setBounds(600, 220, 80, 40);
        this.guessTypeTF = new JTextField("Type ('/' or '\\')");
        this.panel.add(this.guessTypeTF);
        this.guessTypeTF.setBounds(680, 220, 80, 40);
        this.historyButton = new JButton("History");
        this.hbHandler = new HistoryButtonHandler();
        this.historyButton.addActionListener(this.hbHandler);
        this.panel.add(this.historyButton);
        this.historyButton.setBounds(520, 270, 240, 40);
        this.gameMsg = new JTextArea();
        this.gameScr = new JScrollPane(this.gameMsg);
        this.panel.add(this.gameScr);
        this.gameMsg.setText("[RULES]\n1. Shoot a laser by typing a number (0-39) in the text box below the shoot button as your starting position (denoted in black). The laser should end up on the opposite side of the grid unless there is a baffle in the way, which will deflect the laser.\n2. Guess the position of the baffle by typing row index, column index, and baffle type into the three textboxes below the guess button. Row and column index increases top to bottom and left to right respectively, starting at 1. A correct guess will reveal that baffle ('\\' is from top left to bottom right; '/' is from top right to bottom left)\n3. Click on the history button to see what you did previously.\n\n[SCORING GUIDE]\nShoot: -1 point\nCorrect Guess: +7 points\nIncorrect Guess: -3 points");
        this.gameScr.setBounds(520, 320, 240, 180);
        this.gameMsg.setEditable(false);
        this.gameMsg.setWrapStyleWord(true);
        this.gameMsg.setLineWrap(true);
        this.gameScr.setVisible(true);
        this.getContentPane().add(this.panel);
        this.panel.setVisible(true);
    }
    
    public void promptDiff() {
        final Object[] options = { "Easy", "Medium", "Hard" };
        final int n = JOptionPane.showOptionDialog(this.promptPanel, "Select your difficulty:", "BaffleBox", 1, 3, null, options, options[0]);
        switch (n) {
            case 0: {
                this.difficulty = 5;
                break;
            }
            case 1: {
                this.difficulty = 7;
                break;
            }
            case 2: {
                this.difficulty = 10;
                break;
            }
        }
    }
    
    public void updateGrid() {
        for (int r = 1; r < this.grid.length - 1; ++r) {
            for (int c = 1; c < this.grid[r].length - 1; ++c) {
                final int type = this.box.getGrid().getElement(r, c);
                if (type > 0 && !this.box.getGrid().getNotFound(r, c)) {
                    if (type == 1) {
                        this.grid[r][c].setIcon(this.slash1);
                    }
                    else if (type == 3) {
                        this.grid[r][c].setIcon(this.slash3);
                    }
                }
            }
        }
    }
    
    static /* synthetic */ void access$1(final BaffleGUI baffleGUI, final String message) {
        baffleGUI.message = message;
    }
    
    private class ShootButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent e) {
            int position;
            try {
                position = Integer.parseInt(BaffleGUI.this.shootTF.getText());
            }
            catch (NumberFormatException e2) {
                BaffleGUI.access$1(BaffleGUI.this, "Please only enter numbers 0 - 39 for shooting.");
                BaffleGUI.this.gameMsg.setText(BaffleGUI.this.message);
                BaffleGUI.this.shootTF.setText("");
                return;
            }
            if (position >= 0 && position < 40) {
                BaffleGUI.access$1(BaffleGUI.this, BaffleGUI.this.box.fireLaser(position));
                BaffleGUI.this.history.add(BaffleGUI.this.message);
            }
            else {
                BaffleGUI.access$1(BaffleGUI.this, "Invalid Location");
            }
            BaffleGUI.this.shootTF.setText("");
            BaffleGUI.this.score.setText("Score: " + BaffleGUI.this.box.getScore());
            BaffleGUI.this.gameMsg.setText(BaffleGUI.this.message);
        }
    }
    
    private class GuessButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent e) {
            int r = 0;
            int c = 0;
            int t = -1;
            try {
                r = Integer.parseInt(BaffleGUI.this.guessRowTF.getText());
                c = Integer.parseInt(BaffleGUI.this.guessColTF.getText());
            }
            catch (NumberFormatException e2) {
                BaffleGUI.access$1(BaffleGUI.this, "Please enter 1 - 10 for row or column index.");
                BaffleGUI.this.gameMsg.setText(BaffleGUI.this.message);
                BaffleGUI.this.guessRowTF.setText("");
                BaffleGUI.this.guessColTF.setText("");
                BaffleGUI.this.guessTypeTF.setText("");
                return;
            }
            final String type = BaffleGUI.this.guessTypeTF.getText().trim().substring(0, 1);
            if (type.equals("\\")) {
                t = 1;
            }
            else if (type.equals("/")) {
                t = 3;
            }
            if (r > 0 && r <= 10 && c > 0 && c <= 10 && t != -1) {
                BaffleGUI.access$1(BaffleGUI.this, "Your guess of baffle '" + type + "' at [" + r + ", " + c + "]");
                if (BaffleGUI.this.box.guessBaffle(r, c, t)) {
                    final BaffleGUI this$0 = BaffleGUI.this;
                    BaffleGUI.access$1(this$0, String.valueOf(this$0.message) + " is correct.");
                    BaffleGUI.this.updateGrid();
                }
                else {
                    final BaffleGUI this$2 = BaffleGUI.this;
                    BaffleGUI.access$1(this$2, String.valueOf(this$2.message) + " is wrong.");
                }
                BaffleGUI.this.history.add(BaffleGUI.this.message);
            }
            else if (t != -1) {
                BaffleGUI.access$1(BaffleGUI.this, "Invalid Location");
            }
            else if (t == -1) {
                BaffleGUI.access$1(BaffleGUI.this, "Invalid Baffle Type");
            }
            else {
                BaffleGUI.access$1(BaffleGUI.this, "Invalid Location and Baffle Type");
            }
            if (BaffleGUI.this.box.getGrid().allFound()) {
                final BaffleGUI this$3 = BaffleGUI.this;
                BaffleGUI.access$1(this$3, String.valueOf(this$3.message) + "\nYou found all the baffles!");
            }
            BaffleGUI.this.guessRowTF.setText("");
            BaffleGUI.this.guessColTF.setText("");
            BaffleGUI.this.guessTypeTF.setText("");
            BaffleGUI.this.score.setText("Score: " + BaffleGUI.this.box.getScore());
            BaffleGUI.this.remaining.setText("Baffles Remaining: " + BaffleGUI.this.box.getRemaining());
            BaffleGUI.this.gameMsg.setText(BaffleGUI.this.message);
        }
    }
    
    private class HistoryButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent e) {
            BaffleGUI.access$1(BaffleGUI.this, "");
            for (int i = BaffleGUI.this.history.size() - 1; i >= 0; --i) {
                final BaffleGUI this$0 = BaffleGUI.this;
                BaffleGUI.access$1(this$0, String.valueOf(this$0.message) + BaffleGUI.this.history.get(i) + "\n");
            }
            BaffleGUI.this.gameMsg.setText(BaffleGUI.this.message);
        }
    }
}
