package uz.mehrojbek.appmagazinresiduebot.entity.enums;

public enum BranchTypeEnum {
    MAGAZINE("Do'kon"),
    WAREHOUSE("Sklad");
    public String nameUz;

    BranchTypeEnum(String nameUz) {
        this.nameUz = nameUz;
    }
}
