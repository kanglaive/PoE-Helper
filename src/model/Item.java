package model;


/**
 * Created by Kang on 7/4/2017.
 */
public class Item {
    private String itemRarity;
    private String itemName;
    private String baseType;
    private String[] strArr;
    private String quantity;

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
        System.out.println(strArr[1]);
        System.out.println(firstLine[1]);
        if (firstLine[0].equals("Rarity:")) {
            itemRarity = firstLine[1];
        }
        switch (firstLine[1]) {
            case "Currency":
                createCurrency(firstLine[1]);
                break;
            default:
                break;
        }
    }

    /**
     * populate item as currency
     */
    public void createCurrency(String baseType) {
        itemName = strArr[1];
        String[] line4 = strArr[3].split(" ");
        String[] quant = line4[2].split("/");
        this.baseType = baseType;
        quantity = quant[0];
    }

    public String getQuantity() {
        return quantity;
    }

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
}
