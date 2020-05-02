class Tree {
    // tree types need to extend a parent so they can be defined generically and then specified later on

    // getters and setters for the conditions need to be initiliazed here and then get overridden
    public boolean getNormal() {
        return false;
    }

    public boolean getIgnited() {
        return false;
    }

    public boolean getOnFire() {
        return false;
    }

    public boolean getStillOnFire() {
        return false;
    }

    public boolean getBurnt() {
        return false;
    }

    public String toString(){
        return "";
    }

    public int getType() {
        return 0;
    }

    public void setNormal(boolean cond) {
    }
    public void setIgnited(boolean cond) {
    }
    public void setOnFire(boolean cond) {
    }
    public void setStillOnFire(boolean cond) {
    }
    public void setBurnt(boolean cond) {
    }
    public void setType(int x){
    }

}