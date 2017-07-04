package model;

/**
 * Created by Kang on 7/4/2017.
 */
public enum Rarity {
    Normal ("Normal"),
    Magic ("Magic"),
    Rare ("Rare"),
    Unique ("Unique"),
    Currency ("Currency");

    private String rarity;

    Rarity(String rarity) {
        this.rarity = rarity;
    }

    @Override
    public String toString() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    /**
     * parses rarity from first line of clipboard
     * @param rarity
     * @throws Exception
     */
    public void parseRarity(String rarity) throws Exception {
        String[] str = rarity.split(" ");
        if (str[0].equals("Rarity:")) {
            this.rarity = str[1];
        } else {
            throw new Exception("This item is invalid for parsing.");
        }
    }

}
