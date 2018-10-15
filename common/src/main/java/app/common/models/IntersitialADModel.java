package app.common.models;

public class IntersitialADModel implements TypeAwareModel {

    public String adUnitId;

    @Override
    @ViewType
    public int getType() {
        return ViewType.INTERSTITIAL_AD;
    }
}
