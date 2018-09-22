import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.awt.Point;

// 
// Original Program by Jimmy Zheng
// Decompiled by Procyon v0.5.30
// 

class BaffleGUI extends JFrame
{
    //GUI Element Position Variables
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int GRID_WIDTH = 40;
    private static final int GRID_HEIGHT = 40;
    private static final int ITEM_WIDTH = 240;
    private static final int ITEM_HEIGHT = 40;
    private static final int X_OFFSET = 520;
    private static final int Y_OFFSET = 20;

    //Rules
    private static final String RULES =
            "[RULES]\n" +
                    "1. Shoot a laser by typing a number (0-39) in the text box below the shoot button as your starting position (denoted in black). The laser should end up on the opposite side of the grid unless there is a baffle in the way, which will deflect the laser.\n" +
                    "2. Guess the position of the baffle by typing row index, column index, and baffle type into the three textboxes below the guess button. Row and column index increases top to bottom and left to right respectively, starting at 1. A correct guess will reveal that baffle ('\\' is from top left to bottom right; '/' is from top right to bottom left)\n" +
                    "3. Click on the history button to see what you did previously.\n\n" +
                    "[SCORING GUIDE]\n" +
                    "Shoot: -1 point\n" +
                    "Correct Guess: +7 points\n" +
                    "Incorrect Guess: -3 points";

    //GUI Elements

    private JLabel[][] grid;
    private Point[][] gridCoords;
    private JTextArea gameMsg;
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
    private BaffleBox box;

    /**
     * Normal Constructor for BaffleGUI
     * @param mode 1 if debug mode
     */
    BaffleGUI(int mode)
    {
        // Prompt the user for difficulty
        if (mode == 0) promptDiff();
        else difficulty = Math.abs(mode);

        //Initialize the images used in the game
        ClassLoader cl = getClass().getClassLoader();
        empty = new ImageIcon(cl.getResource("img/blank.png"));
        slash1 = new ImageIcon(cl.getResource("img/backslash.png"));
        slash3 = new ImageIcon(cl.getResource("img/foreslash.png"));

        //Create an array for all the image locations
        box = new BaffleBox(difficulty);
        gridCoords = new Point[12][12];
        int x;
        int y = 20;
        for (int r = 0; r < gridCoords.length; ++r) {
            x = 20;
            for (int c = 0; c < gridCoords[r].length; ++c) {
                gridCoords[r][c] = new Point(x, y);
                x += 40;
            }
            y += 40;
        }
        history = new ArrayList<>();

        //Initializes the Display
        initDisplay();
        if (mode < 0) box.getGrid().printGrid();
        //Close the program when the user presses the red X
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Displays the Gameplay GUI
     */
    void displayGame() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }

    /**
     * Initializes the gameplay GUI
     */
    private void initDisplay() {
        //Create Main Panel
        JPanel panel = new JPanel() {
            public void paintComponent(final Graphics g) {
                super.paintComponent(g);
            }
        };
        setSize(new Dimension(WIDTH, HEIGHT));
        setTitle("BaffleBox");
        panel.setLayout(null);

        //Filling Grid
        grid = new JLabel[12][12];
        for (int r = 0; r < grid.length; ++r) {
            for (int c = 0; c < grid[r].length; ++c) {
                grid[r][c] = new JLabel("", SwingConstants.CENTER);
                panel.add(grid[r][c]);
                grid[r][c].setBounds(gridCoords[r][c].x, gridCoords[r][c].y, GRID_WIDTH, GRID_HEIGHT);
                //Corners
                if (box.getGrid().getElement(r, c) == -1) {
                    grid[r][c].setText("");
                }
                //Inside
                else if (r > 0 && r < 11 && c > 0 && c < 11) {
                    grid[r][c].setIcon(empty);
                }
                //Edges
                else {
                    grid[r][c].setText("" + box.getGrid().getElement(r, c));
                }
            }
        }

        //Loading Remaining Elements
        //Score Label
        score = new JLabel("Score: " + box.getScore());
        panel.add(score);
        score.setBounds(X_OFFSET, Y_OFFSET, ITEM_WIDTH / 2, ITEM_HEIGHT / 2);
        score.setVisible(true);

        //Baffle Remaining Label
        remaining = new JLabel("Baffles Remaining: " + box.getRemaining());
        panel.add(remaining);
        remaining.setBounds(X_OFFSET, 40, ITEM_WIDTH, ITEM_HEIGHT / 2);
        remaining.setVisible(true);

        //Shoot Button
        shootButton = new JButton("Shoot");
        sbHandler = new ShootButtonHandler();
        shootButton.addActionListener(sbHandler);
        panel.add(shootButton);
        shootButton.setBounds(X_OFFSET, 70, ITEM_WIDTH, ITEM_HEIGHT);

        //Shoot Text Field
        shootTF = new JTextField("Shoot Location (0-39)");
        panel.add(shootTF);
        shootTF.setBounds(X_OFFSET, 120, ITEM_WIDTH, ITEM_HEIGHT);

        //Guess Button
        guessButton = new JButton("Guess");
        gbHandler = new GuessButtonHandler();
        guessButton.addActionListener(gbHandler);
        panel.add(guessButton);
        guessButton.setBounds(X_OFFSET, 170, ITEM_WIDTH, ITEM_HEIGHT);

        //Guess Row Text Field
        guessRowTF = new JTextField("Row (1-10)");
        panel.add(guessRowTF);
        guessRowTF.setBounds(X_OFFSET, 220, ITEM_WIDTH / 3, ITEM_HEIGHT);

        //Guess Column Text Field
        guessColTF = new JTextField("Column (1-10)");
        panel.add(guessColTF);
        guessColTF.setBounds(600, 220, ITEM_WIDTH / 3, ITEM_HEIGHT);

        //Guess Baffle Text Field
        guessTypeTF = new JTextField("Type ('/' or '\\')");
        panel.add(guessTypeTF);
        guessTypeTF.setBounds(680, 220, ITEM_WIDTH / 3, ITEM_HEIGHT);

        //History Button
        historyButton = new JButton("History");
        hbHandler = new HistoryButtonHandler();
        historyButton.addActionListener(hbHandler);
        panel.add(historyButton);
        historyButton.setBounds(X_OFFSET, 270, ITEM_WIDTH, ITEM_HEIGHT);

        //Message Box
        gameMsg = new JTextArea();
        JScrollPane gameScr = new JScrollPane(gameMsg);
        panel.add(gameScr);
        gameMsg.setText(RULES);
        gameScr.setBounds(X_OFFSET, 320, ITEM_WIDTH, 180);
        gameMsg.setEditable(false);
        gameMsg.setWrapStyleWord(true);
        gameMsg.setLineWrap(true);
        gameScr.setVisible(true);
        getContentPane().add(panel);
        panel.setVisible(true);
    }

    /**
     * Prompts the User for Game Difficulty
     */
    private void promptDiff() {
        JPanel promptPanel = new JPanel();
        final Object[] options = { "Easy", "Medium", "Hard" };
        final int n = JOptionPane.showOptionDialog(promptPanel, "Select your difficulty:", "BaffleBox", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        switch (n)
        {
            case 0: { difficulty = 5; break; }
            case 1: { difficulty = 7; break; }
            case 2: { difficulty = 10; }
        }
    }

    /**
     * Refreshes the Grid for each Correct Guess
     */
    private void updateGrid()
    {
        for (int r = 1; r < grid.length - 1; ++r)
        {
            for (int c = 1; c < grid[r].length - 1; ++c)
            {
                final int type = box.getGrid().getElement(r, c);
                if (type > 0 && !box.getGrid().getNotFound(r, c))
                {
                    if (type == 1) { grid[r][c].setIcon(slash1); }
                    else if (type == 3) { grid[r][c].setIcon(slash3); }
                }
            }
        }
    }

    /**
     * Event Handler for 'Shoot' Button
     */
    private class ShootButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent e)
        {
            int position;
            //Testing for Valid Inputs
            try
            {
                position = Integer.parseInt(shootTF.getText());
            }
            catch (NumberFormatException e2)
            {
                message = "Please only enter numbers 0 - 39 for shooting.";
                gameMsg.setText(message);
                shootTF.setText("");
                return;
            }

            //Fire Laser if Fire Position is Valid
            if (position >= 0 && position < 40)
            {
                message = box.fireLaser(position);
                history.add(message);
            }
            else { message = "Please only enter numbers 0 - 39 for shooting."; }
            shootTF.setText("");
            score.setText("Score: " + box.getScore());
            gameMsg.setText(message);
        }
    }

    /**
     * Event Handler for the 'Guess' Button
     */
    private class GuessButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent e)
        {
            int r, c, t;

            //Testing for Valid Inputs
            try
            {
                r = Integer.parseInt(guessRowTF.getText());
                c = Integer.parseInt(guessColTF.getText());
            }
            catch (NumberFormatException e2)
            {
                message = "Please enter 1 - 10 for row or column index.";
                gameMsg.setText(message);
                //Clear Input Fields
                guessRowTF.setText("");
                guessColTF.setText("");
                return;
            }

            //Testing for Valid Baffle Types
            final char type = guessTypeTF.getText().trim().charAt(0);

            switch (type)
            {
                case '\\': t = 1; break;
                case '/': t = 3; break;
                default:
                {
                    gameMsg.setText("Invalid Baffle Type");
                    //Clear Input Field
                    guessTypeTF.setText("");
                    return;
                }
            }

            //Comparing Guess With Actual Grid
            message = "Your guess of baffle '" + type + "' at [" + r + ", " + c + "]";
            if (box.guessBaffle(r, c, t))
            {
                message += " is correct.";
                updateGrid();
            }
            else { message += " is wrong."; }

            history.add(message);

            //If all the Baffles are Found
            if (box.getGrid().allFound()) { message += "\nYou found all the baffles!"; }

            //Clear Input Fields
            guessRowTF.setText("");
            guessColTF.setText("");
            guessTypeTF.setText("");

            //Update Textboxes
            score.setText("Score: " + box.getScore());
            remaining.setText("Baffles Remaining: " + box.getRemaining());
            gameMsg.setText(message);
        }
    }

    /**
     * Event Handler for the 'History' Button
     */
    private class HistoryButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent e)
        {
            StringBuilder sb = new StringBuilder();
            for (int i = history.size() - 1; i >= 0; --i)
                sb.append(history.get(i) + "\n");
            gameMsg.setText(sb.toString());
        }
    }
}
