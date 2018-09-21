// 
// Decompiled by Procyon v0.5.30
// 

public class BaffleBox
{
    private Grid grid;
    private int score;
    private int hiscore;
    private int total;
    
    public BaffleBox(final int n) {
        this.total = n;
        this.grid = new Grid(n);
        this.score = 0;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public int getHiScore() {
        return this.hiscore;
    }
    
    public int getRemaining() {
        return this.total;
    }
    
    public Grid getGrid() {
        return this.grid;
    }
    
    public String fireLaser(final int position) {
        --this.score;
        final int[] i = this.posToIndex(position);
        final int direction = position / 10;
        return "Laser fired at " + position + " exits at " + this.move(direction, i[0], i[1]);
    }
    
    public boolean guessBaffle(final int r, final int c, final int type) {
        if (this.grid.getElement(r, c) == type && this.grid.getNotFound(r, c)) {
            this.score += 7;
            --this.total;
            this.grid.toFound(r, c);
            return true;
        }
        this.score -= 3;
        return false;
    }
    
    private int move(int d, int r, int c) {
        while (r != 0 && r != 11 && c != 0 && c != 11) {
            if (r > 0 && r < 11 && c > 0 && c < 11 && this.grid.getElement(r, c) != 0) {
                d = this.changeDirection(d, this.grid.getElement(r, c));
            }
            switch (d) {
                default: {
                    continue;
                }
                case 0: {
                    ++c;
                    continue;
                }
                case 1: {
                    ++r;
                    continue;
                }
                case 2: {
                    --c;
                    continue;
                }
                case 3: {
                    --r;
                    continue;
                }
            }
        }
        return this.grid.getElement(r, c);
    }
    
    private int changeDirection(final int d, final int b) {
        if (d % 2 == 0) {
            return (d + b) % 4;
        }
        return Math.abs(d - b) % 4;
    }
    
    private int[] posToIndex(final int pos) {
        final int[] i = new int[2];
        if (pos / 10 == 0) {
            i[0] = 10 - pos;
            i[1] = 1;
        }
        else if (pos / 10 == 1) {
            i[1] = pos % 10 + 1;
            i[0] = 1;
        }
        else if (pos / 10 == 2) {
            i[0] = pos % 10 + 1;
            i[1] = 10;
        }
        else if (pos / 10 == 3) {
            i[1] = 10 - pos % 10;
            i[0] = 10;
        }
        return i;
    }
}
