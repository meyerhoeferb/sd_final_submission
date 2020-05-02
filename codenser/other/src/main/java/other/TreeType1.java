class TreeType1 extends Tree {
    // booleans for tracking the various conditions of the tree
    private boolean normal;
    private boolean ignited;
    private boolean onFire;
    private boolean burnt;
    private int type;

    //constructor, sets normal to true and everything else false
    public TreeType1() {
        normal = true;
        ignited = false;
        onFire = false;
        burnt = false;
        type = 1;
    }

    //for printing object traits
    public String toString() {
        String s = "";

        s += "normal: " + normal + "\n";
        s += "ignited: " + ignited + "\n";
        s += "onFire: " + onFire + "\n";
        s += "burnt: " + burnt + "\n";
        s += "type: " + type + "\n";
        return s;
    }

    //getters and setters for the conditions
    public boolean getNormal() {
        return normal;
    }
    public boolean getIgnited() {
        return ignited;
    }
    public boolean getOnFire() {
        return onFire;
    }
    public boolean getBurnt() {
        return burnt;
    }
    public int getType() {
        return type;
    }

    public void setNormal(boolean cond) {
        normal = cond;
    }
    public void setIgnited(boolean cond) {
        ignited = cond;
    }
    public void setOnFire(boolean cond) {
        onFire = cond;
    }
    public void setBurnt(boolean cond) {
        burnt = cond;
    }
}
