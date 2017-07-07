package model;

/**
 * Created by Kang on 7/4/2017.
 */
public enum Base {
    BodyArmour ("BodyArmour"),
    Boots ("Boots"),
    Bow ("Bow"),
    Claw ("Claw"),
    Dagger ("Dagger"),
    Gloves ("Gloves"),
    Helmet ("Helmet"),
    Jewelry ("Jewelry"),
    Map ("Map"),
    OneHand ("OneHand"),
    Shield ("Shield"),
    Staff ("Staff"),
    TwoHand ("TwoHand"),
    Wand ("Wand");

    private String baseType;

    Base(String baseType) {
        this.baseType = baseType;
    }

    @Override
    public String toString() {
        return baseType;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }
}
