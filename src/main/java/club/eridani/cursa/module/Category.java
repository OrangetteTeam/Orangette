package club.eridani.cursa.module;

public enum Category {

    COMBAT("Combat", true),
    MISC("Misc", true),
    MOVEMENT("Movement", true),
    PLAYER("Player", true),
    RENDER("Render", true),
    EXPLOIT("Exploit",true),
    CLIENT("Client", true),

    HIDDEN("Hidden", false),
    HUD("HUD" , false);

    public String categoryName;
    public boolean visible;

    Category(String categoryName, boolean visible) {
        this.categoryName = categoryName;
        this.visible = visible;
    }

}
