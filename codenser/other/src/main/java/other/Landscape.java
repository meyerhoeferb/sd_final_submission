import javafx.scene.layout.Border;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Landscape extends JFrame implements ActionListener, MouseListener {
    // holds the information for the forest about to be lit on fire. The constructor makes the array m x m large and
    // fills it with Cells

    private Cell[][] land;
    private int size;
    private int time = 0;
    private int step = time % 3;
    private LandscapePanel lPanel;
    private ButtonPanel bPanel;

    public Landscape(int[][] data, int m, int numTrees, int[] igniteX, int[] igniteY) {
        //create 2d array of size m
        land = new Cell[m][m];
        //set size equal to m for easier loops later on
        size = m;

        //fill array with cells with the correct type of tree, but not setting neighbors yet
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                if (data[i][j] == 0) {
                    land[i][j] = new Cell('0');
                } else if (data[i][j] == 1) {
                    land[i][j] = new Cell('1');
                } else {
                    land[i][j] = new Cell('2');
                }
            }
        }

        // set the neighbor pointers for each cell
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                // check for conditions if a cell is on the edge of the landscape, and therefore wont have a neighbor
                // in one or two of the directions

                //top left corner
                if (i == 0 && j == 0) {
                    land[i][j].setDown(land[i + 1][j]);
                    land[i][j].setUp(null);
                    land[i][j].setLeft(null);
                    land[i][j].setRight(land[i][j + 1]);
                }
                //top right corner
                else if (i == 0 && j == m - 1) {
                    land[i][j].setDown(land[i + 1][j]);
                    land[i][j].setUp(null);
                    land[i][j].setLeft(land[i][j - 1]);
                    land[i][j].setRight(null);
                }
                //bottom left corner
                else if (i == m - 1 && j == 0) {
                    land[i][j].setDown(null);
                    land[i][j].setUp(land[i - 1][j]);
                    land[i][j].setLeft(null);
                    land[i][j].setRight(land[i][j + 1]);
                }
                //bottom right corner
                else if (i == m - 1 && j == m - 1) {
                    land[i][j].setDown(null);
                    land[i][j].setUp(land[i - 1][j]);
                    land[i][j].setLeft(land[i][j - 1]);
                    land[i][j].setRight(null);
                }
                //top row not corner
                else if (i == 0 && j != 0 && j != m - 1) {
                    land[i][j].setDown(land[i + 1][j]);
                    land[i][j].setUp(null);
                    land[i][j].setLeft(land[i][j - 1]);
                    land[i][j].setRight(land[i][j + 1]);
                }
                //bottom row not corner
                else if (i == m - 1 && j != 0 && j != m - 1) {
                    land[i][j].setDown(null);
                    land[i][j].setUp(land[i - 1][j]);
                    land[i][j].setLeft(land[i][j - 1]);
                    land[i][j].setRight(land[i][j + 1]);
                }
                //left side not corner
                else if (j == 0 && i != 0 && i != m - 1) {
                    land[i][j].setDown(land[i + 1][j]);
                    land[i][j].setUp(land[i - 1][j]);
                    land[i][j].setLeft(null);
                    land[i][j].setRight(land[i][j + 1]);
                }
                //right side not corner
                else if (j == m - 1 && i != 0 && i != m - 1) {
                    land[i][j].setDown(land[i + 1][j]);
                    land[i][j].setUp(land[i - 1][j]);
                    land[i][j].setLeft(land[i][j - 1]);
                    land[i][j].setRight(null);
                }
                //normal case, aka central cells
                else {
                    land[i][j].setDown(land[i + 1][j]);
                    land[i][j].setUp(land[i - 1][j]);
                    land[i][j].setLeft(land[i][j - 1]);
                    land[i][j].setRight(land[i][j + 1]);
                }
            }
        }

        //intitial ignition
        Tree tree;
        for (int i = 0; i < numTrees; i++) {
            int x = igniteX[i];
            int y = igniteY[i];

            if (land[x][y].getSymbol() != '0') {
                tree = land[x][y].getTree();
                tree.setNormal(false);
                tree.setIgnited(true);
            }
        }

        //set up GUI
        this.setTitle("Forest Fire Simulation");
        this.setResizable(false);
        this.setSize(1400, 900);

        Container cPane = this.getContentPane();
        lPanel = new LandscapePanel(land, size);
        bPanel = new ButtonPanel();

        cPane.add(lPanel);

        lPanel.addMouseListener(this);

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(this);
        JButton stepButton = new JButton("nextStep");
        stepButton.addActionListener(this);
        JButton finishButton = new JButton("completeRun");
        finishButton.addActionListener(this);
        JButton newButton = new JButton("startNewRun");
        newButton.addActionListener(this);

        bPanel.setLayout(new FlowLayout());
        bPanel.add(quitButton);
        bPanel.add(stepButton);
        bPanel.add(finishButton);
        bPanel.add(newButton);
        cPane.add(bPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    //actionlistener method, calls proper method based on button pressed
    public void actionPerformed(ActionEvent a) {
        String event_desc = a.getActionCommand();

        if (event_desc.equalsIgnoreCase("Quit")) {
            System.exit(0);
        } else if (event_desc.equalsIgnoreCase("nextStep")) {
            stepForestFire();

            lPanel.repaint();
        } else if (event_desc.equalsIgnoreCase("completeRun")) {
            completeForestFire();

            lPanel.repaint();
        } else if (event_desc.equalsIgnoreCase("startNewRun")) {
            startNewRun();
        }
    }

    // mouseListener method for igniting clicked trees
    public void mouseClicked(MouseEvent m) {
        int igniteX;
        int igniteY;

        int x = m.getX();
        int y = m.getY();

        igniteX = x / 50;
        igniteY = y / 50;

        if(igniteX < size && igniteY < size) {
            land[igniteX][igniteY].getTree().setNormal(false);
            land[igniteX][igniteY].getTree().setIgnited(true);
        }
        this.repaint();
    }

    //implemented but don't do anything
    public void mouseEntered(MouseEvent m){}
    public void mouseExited(MouseEvent m) {}
    public void mousePressed(MouseEvent m) {}
    public void mouseReleased(MouseEvent m){}

    // burn the entire landscape (used for the completeRun button). It does so in 4 steps after initial ignition,
    // each a nested for loop going through the array. First it sets all the ignited trees to burning. Then it sets the
    // neighbors of burning trees to ignited based on the rules for the types. Then it sets burning type1s to burnt,
    // ignited t2 to burning, burning t2 to still burning, and stillburning t2 to burnt. Then it checks if any trees are
    // still burning, and ends the loop if not.
    public void completeForestFire() {
        boolean ongoing;        //boolean to keep while loop going while trees are still burning
        Tree tree;              //for use when going through trees
        Tree neighbor;          //for use in igniting neighbors
        Tree buddy;             //for use igniting type2 neighbors

        // start while loop that will end when nothing is burning anymore
        ongoing = true;
        while (ongoing) {

            //go through landscape and set ignited trees to burning, only does anything if there is a tree in the
            //current space
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    tree = land[i][j].getTree();
                    if (tree != null) {
                        if (tree.getIgnited()) {
                            tree.setIgnited(false);
                            tree.setOnFire(true);
                        }
                    }
                }
            }

            //go through landscape again and set neighbors of burning trees to ignited if they haven't already
            //burned or been ignited. Follow rules for type 1 and 2 trees.
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    tree = land[i][j].getTree();
                    if (tree != null && tree.getOnFire()) {
                        //up
                        if (land[i][j].getUp() != null) {
                            neighbor = land[i][j].getUp().getTree();
                            if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 1) {
                                neighbor.setNormal(false);
                                neighbor.setIgnited(true);
                            }
                            //if neighbor is type2 have to check its neighbors
                            else if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 2) {
                                if (land[i][j].getUp().getUp() != null) {
                                    buddy = land[i][j].getUp().getUp().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getUp().getLeft() != null) {
                                    buddy = land[i][j].getUp().getLeft().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getUp().getRight() != null) {
                                    buddy = land[i][j].getUp().getRight().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                //dont check direction came from
                            }
                        }
                        //right
                        if (land[i][j].getRight() != null) {
                            neighbor = land[i][j].getRight().getTree();
                            if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 1) {
                                neighbor.setNormal(false);
                                neighbor.setIgnited(true);
                            }
                            //if neighbor is type2 have to check its neighbors
                            else if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 2) {
                                if (land[i][j].getRight().getUp() != null) {
                                    buddy = land[i][j].getRight().getUp().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getRight().getDown() != null) {
                                    buddy = land[i][j].getRight().getDown().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getRight().getRight() != null) {
                                    buddy = land[i][j].getRight().getRight().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                //dont check direction came from
                            }
                        }
                        //down
                        if (land[i][j].getDown() != null) {
                            neighbor = land[i][j].getDown().getTree();
                            if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 1) {
                                neighbor.setNormal(false);
                                neighbor.setIgnited(true);
                            }
                            //if neighbor is type2 have to check its neighbors
                            else if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 2) {
                                if (land[i][j].getDown().getDown() != null) {
                                    buddy = land[i][j].getDown().getDown().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getDown().getLeft() != null) {
                                    buddy = land[i][j].getDown().getLeft().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getDown().getRight() != null) {
                                    buddy = land[i][j].getDown().getRight().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                //dont check direction came from
                            }
                        }
                        //left
                        if (land[i][j].getLeft() != null) {
                            neighbor = land[i][j].getLeft().getTree();
                            if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 1) {
                                neighbor.setNormal(false);
                                neighbor.setIgnited(true);
                            }
                            //if neighbor is type2 have to check its neighbors
                            else if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 2) {
                                if (land[i][j].getLeft().getUp() != null) {
                                    buddy = land[i][j].getLeft().getUp().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getLeft().getLeft() != null) {
                                    buddy = land[i][j].getLeft().getLeft().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getLeft().getDown() != null) {
                                    buddy = land[i][j].getLeft().getDown().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                //dont check direction came from
                            }
                        }
                    }
                }
            }

            //go through again and this time set burning trees to either burnt or still burning, based on their type
            //if burnt, set symbol to an X for when printing out landscape
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    tree = land[i][j].getTree();
                    if (tree != null) {
                        if (tree.getType() == 1 && tree.getOnFire()) {
                            tree.setOnFire(false);
                            tree.setBurnt(true);
                            land[i][j].setSymbol('X');
                        } else if (tree.getType() == 2 && tree.getOnFire()) {
                            tree.setOnFire(false);
                            tree.setStillOnFire(true);
                        } else if (tree.getType() == 2 && tree.getStillOnFire()) {
                            tree.setStillOnFire(false);
                            tree.setBurnt(true);
                            land[i][j].setSymbol('X');
                        }
                    }
                }
            }


            //go through one last time and see if anything is still burning or ignited, otherwise end loop by changing
            //the boolean

            boolean burning = false;  // another boolean to make this check work
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    tree = land[i][j].getTree();
                    if (tree != null) {
                        if (tree.getIgnited() || tree.getStillOnFire() || tree.getOnFire()) {
                            burning = true;
                        }
                    }
                }
            }
            if (!burning) {
                ongoing = false;
            }
        }
    }

    // step the forest fire once, same as completeForestFire but does not operate in a while loop, so each condition
    // change only happens once depending on step variable, which remembers which step was last executed and
    // does the next appropriate step next time the button is clicked
    public void stepForestFire() {
        Tree tree;              //for use when going through trees
        Tree neighbor;          //for use in igniting neighbors
        Tree buddy;             //for use igniting type2 neighbors

        //go through landscape and set ignited trees to burning, only does anything if there is a tree in the
        //current space
        if (step == 0) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    tree = land[i][j].getTree();
                    if (tree != null) {
                        if (tree.getIgnited()) {
                            tree.setIgnited(false);
                            tree.setOnFire(true);
                        }
                    }
                }
            }
            time++;
            step = time % 3;
            return;
        }

        //go through landscape again and set neighbors of burning trees to ignited if they haven't already
        //burned or been ignited. Follow rules for type 1 and 2 trees.
        if (step == 1) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    tree = land[i][j].getTree();
                    if (tree != null && tree.getOnFire()) {
                        //up
                        if (land[i][j].getUp() != null) {
                            neighbor = land[i][j].getUp().getTree();
                            if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 1) {
                                neighbor.setNormal(false);
                                neighbor.setIgnited(true);
                            }
                            //if neighbor is type2 have to check its neighbors
                            else if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 2) {
                                if (land[i][j].getUp().getUp() != null) {
                                    buddy = land[i][j].getUp().getUp().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getUp().getLeft() != null) {
                                    buddy = land[i][j].getUp().getLeft().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getUp().getRight() != null) {
                                    buddy = land[i][j].getUp().getRight().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                //dont check direction came from
                            }
                        }
                        //right
                        if (land[i][j].getRight() != null) {
                            neighbor = land[i][j].getRight().getTree();
                            if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 1) {
                                neighbor.setNormal(false);
                                neighbor.setIgnited(true);
                            }
                            //if neighbor is type2 have to check its neighbors
                            else if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 2) {
                                if (land[i][j].getRight().getUp() != null) {
                                    buddy = land[i][j].getRight().getUp().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getRight().getDown() != null) {
                                    buddy = land[i][j].getRight().getDown().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getRight().getRight() != null) {
                                    buddy = land[i][j].getRight().getRight().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                //dont check direction came from
                            }
                        }
                        //down
                        if (land[i][j].getDown() != null) {
                            neighbor = land[i][j].getDown().getTree();
                            if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 1) {
                                neighbor.setNormal(false);
                                neighbor.setIgnited(true);
                            }
                            //if neighbor is type2 have to check its neighbors
                            else if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 2) {
                                if (land[i][j].getDown().getDown() != null) {
                                    buddy = land[i][j].getDown().getDown().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getDown().getLeft() != null) {
                                    buddy = land[i][j].getDown().getLeft().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getDown().getRight() != null) {
                                    buddy = land[i][j].getDown().getRight().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                //dont check direction came from
                            }
                        }
                        //left
                        if (land[i][j].getLeft() != null) {
                            neighbor = land[i][j].getLeft().getTree();
                            if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 1) {
                                neighbor.setNormal(false);
                                neighbor.setIgnited(true);
                            }
                            //if neighbor is type2 have to check its neighbors
                            else if (neighbor != null && neighbor.getNormal() && neighbor.getType() == 2) {
                                if (land[i][j].getLeft().getUp() != null) {
                                    buddy = land[i][j].getLeft().getUp().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getLeft().getLeft() != null) {
                                    buddy = land[i][j].getLeft().getLeft().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                if (land[i][j].getLeft().getDown() != null) {
                                    buddy = land[i][j].getLeft().getDown().getTree();
                                    if (buddy != null) {
                                        if (buddy.getOnFire() || buddy.getStillOnFire()) {
                                            neighbor.setNormal(false);
                                            neighbor.setIgnited(true);
                                        }
                                    }
                                }
                                //dont check direction came from
                            }
                        }
                    }
                }
            }
            time++;
            step = time % 3;
            return;
        }

        //go through again and this time set burning trees to either burnt or still burning, based on their type
        if (step == 2) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    tree = land[i][j].getTree();
                    if (tree != null) {
                        if (tree.getType() == 1 && tree.getOnFire()) {
                            tree.setOnFire(false);
                            tree.setBurnt(true);
                            land[i][j].setSymbol('X');
                        } else if (tree.getType() == 2 && tree.getOnFire()) {
                            tree.setOnFire(false);
                            tree.setStillOnFire(true);
                        } else if (tree.getType() == 2 && tree.getStillOnFire()) {
                            tree.setStillOnFire(false);
                            tree.setBurnt(true);
                            land[i][j].setSymbol('X');
                        }
                    }
                }
            }
        }
        time++;
        step = time % 3;
        return;
    }

    //use methods from Assign2 to create a new landscape with random trees andignition, same size as what user initially
    // put
    public void startNewRun() {
        int[][] landscapeData = new int[size][size];
        double fillDensity = 0.7;
        double type1Frac = 0.4;

        Assign2.createRandomForest(landscapeData, size, fillDensity, type1Frac);

        int numTreesToIgnite = (int) UniformRandom.uniform(0,10);
        int[] igniteX = new int[numTreesToIgnite];
        int[] igniteY = new int[numTreesToIgnite];

        for (int i = 0; i < numTreesToIgnite; i++) {
            igniteX[i] = (int) UniformRandom.uniform(0, size - 1);
            igniteY[i] = (int) UniformRandom.uniform(0, size - 1);
        }

        Assign2.processLandscapeData(landscapeData, size, numTreesToIgnite, igniteX, igniteY);

    }
}