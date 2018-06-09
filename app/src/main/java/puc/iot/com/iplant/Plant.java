package puc.iot.com.iplant;

import java.util.Date;

class Plant {

    public final static String ID="id";
    public final static float DRY=40.0f, NORMAL = 200.0f , HUMID=400.0f, VERY_HUMID=500.0f;

    private String id, name, type,species,imageUrl;
    private Date lastWater;
    private float humidity;

    public Plant() { }

    public Plant(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Plant(String id, String name, String type, String species, String imageUrl, Date lastWater) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.species = species;
        this.imageUrl = imageUrl;
        this.lastWater = lastWater;
    }

    public Plant(String id, String name, String imageUrl, float humidity) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.humidity = humidity;
    }

    public String getId() {
        return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Date getLastWater() {
        return lastWater;
    }

    public void setLastWater(Date lastWater) {
        this.lastWater = lastWater;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    @Override
    public boolean equals(Object obj) {
        Plant plant = (Plant) obj;
        return getId()==plant.getId();
    }
}
