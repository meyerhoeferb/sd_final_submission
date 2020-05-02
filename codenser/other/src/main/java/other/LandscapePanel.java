import java.awt.*;
import javax.swing.*;

class LandscapePanel extends JPanel {

    private Cell[][] land;
    private int size;

    public LandscapePanel(Cell[][] l, int m) {
        land = l;
        size = m;

        this.setBackground(Color.WHITE);
    }

    //draw landscape on panel
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int circleDim = 50;
        Color DARK_GREEN = new Color(0,100,0);
        Color DARK_ORANGE = new Color(255,140,0);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(land[i][j].getSymbol() != '0') {
                    Tree tree = land[i][j].getTree();

                    if (tree.getNormal()) {
                        if(tree.getType() == 1) {
                            g.setColor(Color.GREEN);
                        } else if (tree.getType() == 2) {
                            g.setColor(DARK_GREEN);
                        }
                    }
                    if (tree.getIgnited()) {
                        g.setColor(Color.YELLOW);
                    }
                    if (tree.getOnFire()) {
                        g.setColor(DARK_ORANGE);
                    }
                    if (tree.getType() == 2) {
                        if (tree.getStillOnFire()) {
                            g.setColor(Color.RED);
                        }
                    }
                    if (tree.getBurnt()) {
                        g.setColor(Color.GRAY);
                    }

                    g.fillOval(i * circleDim, j * circleDim, circleDim, circleDim);

                } else {
                    g.setColor(Color.BLACK);
                    g.drawOval(i * circleDim, j * circleDim, circleDim, circleDim);
                }
            }
        }

        //stats
        int numOneBurned = getNumTreesBurned(1);
        int numTwoBurned = getNumTreesBurned(2);
        int numTotBurned = numOneBurned + numTwoBurned;
        int numBurning = getNumTreesBurning();
        int numAlive = getNumTreesAlive();

        String s = "Type 1 burned: " + numOneBurned;
        String s2 = "Type 2 burned: " + numTwoBurned;
        String s3 = "Total burned: " + numTotBurned;
        String s4 = "Total burning: " + numBurning;
        String s5 = "Total alive: " + numAlive;

        g.drawString(s, 1200, 15);
        g.drawString(s2, 1200, 30);
        g.drawString(s3, 1200, 45);
        g.drawString(s4, 1200, 60);
        g.drawString(s5, 1200, 75);

        //legend
        g.drawString("Color key: ", 1200, 100);
        g.drawString("Type1: Green", 1200, 115);
        g.drawString("Type2: Dark Green", 1200, 130);
        g.drawString("Ignited: Yellow", 1200, 145);
        g.drawString("Burning: Orange", 1200, 160);
        g.drawString("Type2 Still Burning: Red", 1200, 175);
        g.drawString("Burnt: Gray", 1200, 190);
        g.drawString("No Tree: Empty", 1200, 205);


    }

    //count the number of trees that burned down, based on type of tree provided as a parameter
    public int getNumTreesBurned(int treeType) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((land[i][j].getSymbol() == 'X') && (land[i][j].getTree().getType() == treeType)) {
                    count++;
                }
            }
        }
        return count;
    }

    //count the number of trees that are currently burning
    public int getNumTreesBurning() {
        int count = 0;
        Tree tree;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tree = land[i][j].getTree();

                if(tree != null) {
                    if (tree.getType() == 1) {
                        if (tree.getOnFire()) {
                            count++;
                        }
                    }
                    if (tree.getType() == 2) {
                        if (tree.getOnFire() || tree.getStillOnFire()) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    //count the number of trees currently alive
    public int getNumTreesAlive() {
        int count = 0;
        Tree tree;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tree = land[i][j].getTree();

                if(tree != null) {
                    if (tree.getNormal()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}