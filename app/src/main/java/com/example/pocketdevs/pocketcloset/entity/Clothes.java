package com.example.pocketdevs.pocketcloset.entity;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Saif on 2017-10-25.
 */

public class Clothes extends SugarRecord {

    @Unique
    long id;

    int type;
    int subType;
    int colour;
    int material;

    String name;
    String photoId;

    public Clothes() {}

    public Clothes(int type, int subType, int material, int colour, String photoId) {
        this.type = type;
        this.subType = subType;
        this.material = material;
        this.colour = colour;
        this.photoId = photoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}