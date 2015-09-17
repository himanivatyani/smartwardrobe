package com.smartwardrobe.object;

import java.io.Serializable;

/**
 * Created by leechunhoe on 12/9/15.
 */
public class Cloth implements Serializable
{
    public static final String KEY_SELF = "cloth";
    public static final String KEY_ID = "id";
    public static final String KEY_RFID = "rfid";

    public static final String CLOTH_TYPE_TSHIRT = "tshirt";
    public static final String CLOTH_TYPE_SHIRT = "shirt";
    public static final String CLOTH_TYPE_DRESS = "dress";
    public static final String CLOTH_TYPE_SHORT = "short";
    public static final String CLOTH_TYPE_PANT = "pant";
    public static final String CLOTH_TYPE_JACKET = "jacket";

    public static final String[] CLOTH_TYPES = { CLOTH_TYPE_TSHIRT, CLOTH_TYPE_SHIRT, CLOTH_TYPE_PANT, CLOTH_TYPE_SHORT, CLOTH_TYPE_DRESS, CLOTH_TYPE_JACKET };

    int id = 0;
    int frequency = 0;
    String rfid = null;
    byte[] image = null;
    String imagePath = null;
    String name = null;
    String type = null;
    String createdAt = null;
    String lastUpdatedAt = null;
    String action = null;

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(String createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getLastUpdatedAt()
    {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(String lastUpdatedAt)
    {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public int getFrequency()
    {
        return frequency;
    }

    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getRfid()
    {
        return rfid;
    }

    public void setRfid(String rfid)
    {
        this.rfid = rfid;
    }

    public byte[] getImage()
    {
        return image;
    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
