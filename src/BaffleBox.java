//
// Original Program by Jimmy Zheng
// Decompiled by Procyon v0.5.30
// 

class BaffleBox
{
    private Grid grid;
    private int score;
    private int total;

    /**
     * Normal Constructor for BaffleBox
     * @param n number of baffles
     */
    BaffleBox(final int n)
    {
        total = n;
        grid = new Grid(n);
        score = 0;
    }

    /**
     * Gets the current score
     * @return score
     */
    int getScore() {
        return score;
    }

    /**
     * Gets number of remaining baffles
     * @return remaining baffles
     */
    int getRemaining() { return total; }

    /**
     * Gets the Grid object
     * @return Grid object
     */
    Grid getGrid() { return grid; }

    /**
     * Fires the laser and returns a message
     * @param position initial firing position
     * @return message on where the laser was fired and where it exited the box
     */
    String fireLaser(final int position)
    {
        --score;
        final int[] i = posToIndex(position);
        final int direction = position / 10;
        return "Laser fired at " + position + " exits at " + move(direction, i[0], i[1]);
    }

    /**
     * Compare the guess to the actual grid of baffles and updates the score
     * @param r row index
     * @param c column index
     * @param type baffle type
     * @return true if correct, else false
     */
    boolean guessBaffle(final int r, final int c, final int type)
    {
        if (grid.getElement(r, c) == type && grid.getNotFound(r, c))
        {
            score += 7;
            --total;
            grid.toFound(r, c);
            return true;
        }
        score -= 3;
        return false;
    }

    /**
     * Moves the laser from its firing point to its exit
     * @param d direction
     * @param r row index
     * @param c column index
     * @return number of the exit
     */
    private int move(int d, int r, int c)
    {
        while (r != 0 && r != 11 && c != 0 && c != 11)
        {
            if (r > 0 && r < 11 && c > 0 && c < 11 && grid.getElement(r, c) != 0)
                d = changeDirection(d, grid.getElement(r, c));
            switch (d)
            {
                default: { continue; }
                case 0: { ++c; continue; }
                case 1: { ++r; continue; }
                case 2: { --c; continue; }
                case 3: { --r; }
            }
        }
        return grid.getElement(r, c);
    }

    /**
     * Changes the direction of the laser
     * @param d direction
     * @param b baffle value
     * @return new direction value
     */
    private int changeDirection(final int d, final int b)
    {
        if (d % 2 == 0)
            return (d + b) % 4;
        return Math.abs(d - b) % 4;
    }

    /**
     * Converts initial position to index
     * @param pos position value
     * @return position of the laser in {row index, column index}
     */
    private int[] posToIndex(final int pos)
    {
        final int[] i = new int[2];
        if (pos / 10 == 0)
        {
            i[0] = 10 - pos;
            i[1] = 1;
        }
        else if (pos / 10 == 1)
        {
            i[1] = pos % 10 + 1;
            i[0] = 1;
        }
        else if (pos / 10 == 2)
        {
            i[0] = pos % 10 + 1;
            i[1] = 10;
        }
        else if (pos / 10 == 3)
        {
            i[1] = 10 - pos % 10;
            i[0] = 10;
        }
        return i;
    }
}
