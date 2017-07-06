package model;

/**
 * Created by Kang on 7/4/2017.
 */
public enum Rarity {
    Normal ("Normal"),
    Magic ("Magic"),
    Rare ("Rare"),
    Unique ("Unique"),
    Gem ("Gem"),
    Currency ("Currency");

    private String rarity;

    Rarity(String rarityRep) {
        rarity = rarityRep;
    }

    @Override
    public String toString() {
        return rarity;
    }

    public void setRarity(String rarityRep) {
        rarity = rarityRep;
    }

    /**
     * parses rarity from first line of clipboard
     * @param rarity
     * @throws Exception
     */
    public void parseRarity(String rarity) throws Exception {
        String[] str = rarity.split(" ");
        System.out.println(str[0] + " and " + str[1]);
        if (str[0].equals("Rarity:")) {
            this.rarity = str[1];
        } else {
            throw new Exception("This item is invalid for parsing.");
        }
    }

}
