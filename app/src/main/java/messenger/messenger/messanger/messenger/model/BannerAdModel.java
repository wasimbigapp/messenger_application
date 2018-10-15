package messenger.messenger.messanger.messenger.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import app.common.models.TypeAwareModel;

/**
 * Created by bedprakash.r on 06/01/18.
 */

public abstract class BannerAdModel extends BaseItem implements UniqueEntity, TypeAwareModel, Serializable {

    @SerializedName("image_url")
    public String imageUrl;
    @SerializedName("landing_url")
    public String landingUrl;
    @SerializedName("type")
    public String type;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "BannerAdModel{" +
                "imageUrl='" + imageUrl + '\'' +
                ", landingUrl='" + landingUrl + '\'' +
                '}';
    }
}
