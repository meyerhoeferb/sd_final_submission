class Cell {
    private char symbol;
    private Tree treeType;
    private Cell left;
    private Cell right;
    private Cell up;
    private Cell down;

    public Cell(char sym) {
        // used for printing the landscape
        symbol = sym;

        // set tree type based on symbol
        if (symbol == '0'){
            treeType = null;
        } else if (symbol == '1') {
            treeType = new TreeType1();
        } else if (symbol == '2') {
            treeType = new TreeType2();
        }

        // these are pointers for the neighbors, which will be set during construction of the landscape
        left = null;
        right = null;
        up = null;
        down = null;
    }

    //for printing object traits
    public String toString() {
        String s = "";

        s += "symbol: " + symbol + "\n";
        s += "tree: " + treeType + "\n";
        s += "left: " + left + "\n";
        s += "right: " + right + "\n";
        s += "up: " + up + "\n";
        s += "down: " + down + "\n";
        return s;
    }

    //setters and getters for stored variables
    public Cell getLeft() {
        return left;
    }
    public Cell getRight() {
        return right;
    }
    public Cell getUp() {
        return up;
    }
    public Cell getDown() {
        return down;
    }

    public void setLeft(Cell neighbor) {
        left = neighbor;
    }
    public void setRight(Cell neighbor) {
        right = neighbor;
    }
    public void setUp(Cell neighbor) {
        up = neighbor;
    }
    public void setDown(Cell neighbor) {
        down = neighbor;
    }

    //for getting the tree to set conditions
    public Tree getTree() {
        return treeType;
    }

    public char getSymbol() {
        return symbol;
    }
    public void setSymbol(char sym) {
        symbol = sym;
    }
}
