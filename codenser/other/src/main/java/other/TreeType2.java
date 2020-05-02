class TreeType2 extends Tree {
    // booleans for tracking condition, just like type 1 but has extra since burns for an extra step
    private boolean normal;
    private boolean ignited;
    private boolean onFire;
    private boolean stillOnFire;
    private boolean burnt;
    private int type;

    //constructor, set normal to true and everything else to false
    public TreeType2() {
        normal = true;
        ignited = false;
        onFire = false;
        stillOnFire = false;
        burnt = false;
        type = 2;
    }

    //to string method for printing/debugging
    public String toString() {
        String s = "";

        s += "normal: " + normal + "\n";
        s += "ignited: " + ignited + "\n";
        s += "onFire: " + onFire + "\n";
        s += "stillOnFire: " + stillOnFire + "\n";
        s += "burnt: " + burnt + "\n";
        s += "type: " + type + "\n";
        return s;
    }

    // getters and setters for the conditions
    public boolean getNormal() {
        return normal;
    }
    public boolean getIgnited() {
        return ignited;
    }
    public boolean getOnFire() {
        return onFire;
    }
    public boolean getStillOnFire() {
        return stillOnFire;
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
    public void setStillOnFire(boolean cond) {
        stillOnFire = cond;
    }
    public void setBurnt(boolean cond) {
        burnt = cond;
    }
}