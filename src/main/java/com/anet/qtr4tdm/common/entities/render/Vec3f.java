package com.anet.qtr4tdm.common.entities.render;

public class Vec3f {
    float x, y, z;

    public float getX () {return x;};
    public float getY () {return y;};
    public float getZ () {return z;};

    public Vec3f () {

    }

    public Vec3f (float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set (Vec3f v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public void set (float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
