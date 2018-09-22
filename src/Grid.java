//
// Original Program by Jimmy Zheng
// Decompiled by Procyon v0.5.30
// 

public class Grid
{
    private int[][] grid;
    private boolean[][] baffle;

    /**
     * Normal Constructor for Grid
     * @param n number of baffles
     */
    Grid(final int n)
    {
        grid = new int[12][12];
        setGridBounds();
        baffle = new boolean[12][12];
        generateBaffles(n);
    }

    /**
     * Gets an element of the grid
     * @param r row index
     * @param c column index
     * @return 0 if no baffles, 1 if backslash baffle, 3 if forward slash baffle
     */
    int getElement(final int r, final int c) {
        return grid[r][c];
    }

    /**
     * Gets the status of a baffle
     * @param r row index
     * @param c column index
     * @return true if there is no baffle or if the baffle has been found, else false
     */
    boolean getNotFound(final int r, final int c) {
        return baffle[r][c];
    }

    /**
     * Checks if all the baffles had been found
     * @return true if all baffles had been found, else false
     */
    boolean allFound()
    {
        for (boolean[] r : baffle)
            for (boolean c : r)
                if (c) return false;
        return true;
    }


    /**
     * A string representation of the grid with baffles and boundaries labeled
     * FOR DEBUGGING PURPOSES
     * @return formatted string
     */
    @Override
    @Deprecated
    public String toString()
    {
        String str = "";
        for (int r = 0; r < 12; ++r)
        {
            for (int c = 0; c < 12; ++c)
            {
                int num = grid[r][c];
                if (baffle[r][c]) { num = 0; }
                str = String.valueOf(str) + String.format("%3s", num);
            }
            str = String.valueOf(str) + "\n";
        }
        return str;
    }

    /**
     * Prints the grid, including the boundaries and baffle location
     * FOR DEBUGGING PURPOSES
     */
    void printGrid()
    {
        System.out.println("Actual Grid");
        for (int r = 0; r < 12; ++r)
        {
            for (int c = 0; c < 12; ++c)
            {
                System.out.printf("%3s", grid[r][c]);
            }
            System.out.println();
        }
    }

    /**
     * Set the state of a baffle to found
     * @param r row index
     * @param c column index
     */
    void toFound(final int r, final int c) { baffle[r][c] = false; }

    /**
     * Randomly generates baffles to place on the grid
     * @param n number of baffles
     */
    private void generateBaffles(final int n)
    {
        for (int i = 0; i < n; ++i)
        {
            //Randomizing Position
            final int r = (int)(Math.random() * 10.0) + 1;
            final int c = (int)(Math.random() * 10.0) + 1;
            //Randomizing Type
            final int type = 2 * (int)(Math.random() * 2.0) + 1;

            //Filling Grid
            if (grid[r][c] == 0) //Prevent repeats and overwriting
            {
                grid[r][c] = type;
                baffle[r][c] = true;
            }
            else { --i; }
        }
    }

    /**
     * Sets the boundaries in the grid from bottom left in a clockwise fashion
     */
    private void setGridBounds()
    {
        int r = 10;
        int c = 0;
        int i = 0;
        //Numbering Left Edge
        while (r >= 1)
        {
            grid[r][c] = i;
            ++i;
            --r;
        }
        r = 0;
        //Numbering Top Edge
        for (c = 1; c <= 10; ++c)
        {
            grid[r][c] = i;
            ++i;
        }
        r = 1;
        c = 11;
        //Moving Right Edge
        while (r <= 10)
        {
            grid[r][c] = i;
            ++i;
            ++r;
        }
        r = 11;
        //Moving Bottom Edge
        for (c = 10; c >= 1; --c)
        {
            grid[r][c] = i;
            ++i;
        }
        //Filling in Corners
        grid[0][0] = -1;
        grid[0][11] = -1;
        grid[11][0] = -1;
        grid[11][11] = -1;
    }
}
