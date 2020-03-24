package muuttaa.myohemmin.realistisenelamansimulaattori;

public enum FontStyle {
    SmallBlack(R.style.FontStyle_Small_Black, "SmallBlack"),
    SmallRed(R.style.FontStyle_Small_Red, "SmallRed"),
    SmallGreen(R.style.FontStyle_Small_Green, "SmallGreen"),
    SmallBlue(R.style.FontStyle_Small_Blue, "SmallBlue"),
    MediumBlack(R.style.FontStyle_Medium_Black, "MediumBlack"),
    MediumRed(R.style.FontStyle_Medium_Red, "MediumRed"),
    MediumGreen(R.style.FontStyle_Medium_Green, "MediumGreen"),
    MediumBlue(R.style.FontStyle_Medium_Blue, "MediumBlue"),
    LargeBlack(R.style.FontStyle_Large_Black, "LargeBlack"),
    LargeRed(R.style.FontStyle_Large_Red, "LargeRed"),
    LargeGreen(R.style.FontStyle_Large_Green, "LargeGreen"),
    LargeBlue(R.style.FontStyle_Large_Blue, "LargeBlue");

    private int resId;
    private String title;

    public int getResId() {
        return resId;
    }

    public String getTitle() {
        return title;
    }

    FontStyle(int resId, String title) {
        this.resId = resId;
        this.title = title;
    }
}
