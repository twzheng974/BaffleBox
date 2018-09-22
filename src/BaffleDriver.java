//
// Original Program by Jimmy Zheng
// Decompiled by Procyon v0.5.30
// 

public class BaffleDriver
{
    private static final int BAFFLES = -3; // 0 BAFFLES is normal mode, negative BAFFLES is debug mode

    public static void main(final String[] args) {
        final BaffleGUI gui = new BaffleGUI(BAFFLES);
        gui.displayGame();
    }
}
