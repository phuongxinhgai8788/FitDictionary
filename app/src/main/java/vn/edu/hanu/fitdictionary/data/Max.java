package vn.edu.hanu.fitdictionary.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Max {
    @SerializedName("max")
    @Expose
    private int max;

    public Max(){

    }

    public int getMax() {
        return max;
    }
}
