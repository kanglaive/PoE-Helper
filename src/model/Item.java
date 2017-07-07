package model;


/**
 * Created by Kang on 7/4/2017.
 */
public class Item {
    private String itemRarity, itemName, baseType, quantity, implicit;
    private String[] strArr, prefix, suffix;
    private int lvlReq, ilvl;
    private boolean valid = true;

    /** default constructor
     */
    public Item() {}

    /**
     * instantiates item
     * @param string
     */
    public Item(String string) {
        // split item string by lines
        strArr = string.split("\n");
        // parse rarity on first line
        String[] firstLine = strArr[0].split(" ");
        if (firstLine[0].equals("Rarity:")) {
            itemRarity = firstLine[1];
        }
        switch (firstLine[1]) {
            case "Currency":
                createCurrency();
                break;
            case "Normal":
                createNormal();
                break;
            case "Magic":
                createMagic();
                break;
            default:
                valid = false;
                break;
        }
    }

    /**
     * populate item as currency
     */
    public void createCurrency() {
        itemName = strArr[1];
        String[] line4 = strArr[3].split(" ");
        String[] quant = line4[2].split("/");
        this.baseType = strArr[1];
        quantity = quant[0];
    }

    /**
     * populate item as normal item
     */
    private void createNormal() {
        itemName = strArr[1];
        String[] line2 = strArr[1].split(" ");
        // get last word in name
        this.baseType = line2[line2.length - 1];
        switch(baseType) {
            case "Amulet":
            case "Ring":
            case "Belt":
                String[] line5 = strArr[4].split(" ");
                lvlReq = Integer.parseInt(line5[1]);
                String[] line7 = strArr[6].split(" ");
                ilvl = Integer.parseInt(line7[2]);
                implicit = strArr[7];
                break;
            case "Sash":
                String[] line4 = strArr[3].split(" ");
                ilvl = Integer.parseInt(line4[2]);
                implicit = strArr[5];
                break;
            default:
                break;
        }
    }

    private void createMagic() {
        itemName = strArr[1];
    }

    /**
     * returns quantity (only on currency)
     * @return
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * returns item's base type as given by command
     * @return base type string
     */
    public String getBaseType() {
        return baseType;
    }

    /**
     * returns rarity as string
     * @return rarity parsed as string
     */
    public String getRarity() {
        return itemRarity;
    }

    /**
     * returns name of item
     * @return item name string
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * returns whether this item is valid
     * @return
     */
    public boolean isValid() {
        return valid;
    }

}
