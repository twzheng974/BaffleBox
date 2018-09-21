// 
// Decompiled by Procyon v0.5.30
// 

public class Grid
{
    private int[][] grid;
    private boolean[][] baffle;
    
    public Grid(final int n) {
        this.grid = new int[12][12];
        this.setGridBounds();
        this.baffle = new boolean[12][12];
        this.generateBaffles(n);
    }
    
    public int getElement(final int r, final int c) {
        return this.grid[r][c];
    }
    
    public boolean getNotFound(final int r, final int c) {
        return this.baffle[r][c];
    }
    
    public boolean allFound() {
        boolean[][] baffle;
        for (int length = (baffle = this.baffle).length, i = 0; i < length; ++i) {
            final boolean[] row = baffle[i];
            boolean[] array;
            for (int length2 = (array = row).length, j = 0; j < length2; ++j) {
                final boolean b = array[j];
                if (b) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        String str = "";
        for (int r = 0; r < 12; ++r) {
            for (int c = 0; c < 12; ++c) {
                int num = this.grid[r][c];
                if (this.baffle[r][c]) {
                    num = 0;
                }
                str = String.valueOf(str) + String.format("%3s", num);
            }
            str = String.valueOf(str) + "\n";
        }
        return str;
    }
    
    private void printGrid() {
        System.out.println("Actual Grid");
        for (int r = 0; r < 12; ++r) {
            for (int c = 0; c < 12; ++c) {
                final int num = this.grid[r][c];
                System.out.printf("%3s", num);
            }
            System.out.println();
        }
    }
    
    public void toFound(final int r, final int c) {
        this.baffle[r][c] = false;
    }
    
    private void generateBaffles(final int n) {
        for (int i = 0; i < n; ++i) {
            final int r = (int)(Math.random() * 10.0) + 1;
            final int c = (int)(Math.random() * 10.0) + 1;
            final int type = 2 * (int)(Math.random() * 2.0) + 1;
            if (this.grid[r][c] == 0) {
                this.grid[r][c] = type;
                this.baffle[r][c] = true;
            }
            else {
                --i;
            }
        }
    }
    
    private void setGridBounds() {
        int r = 10;
        int c = 0;
        int i = 0;
        while (r >= 1) {
            this.grid[r][c] = i;
            ++i;
            --r;
        }
        r = 0;
        for (c = 1; c <= 10; ++c) {
            this.grid[r][c] = i;
            ++i;
        }
        r = 1;
        c = 11;
        while (r <= 10) {
            this.grid[r][c] = i;
            ++i;
            ++r;
        }
        r = 11;
        for (c = 10; c >= 1; --c) {
            this.grid[r][c] = i;
            ++i;
        }
        this.grid[0][0] = -1;
        this.grid[0][11] = -1;
        this.grid[11][0] = -1;
        this.grid[11][11] = -1;
    }
}
