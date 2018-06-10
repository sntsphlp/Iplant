package puc.iot.com.iplant;

import java.util.Date;

class Plant {

    public final static String _ID ="id";
    public final static int DRY =40, NORMAL = 200 , HUMID=400;
    public static final String NAME = "name";
    public static final String HUMIDITY = "HUMIDITY";
    public static final String IS_OPEN_TAP = "is_open_tap";
    public static final int WATERING_DRY = -1, WATERING_NORMAL =-2,WATERING_HUMID=-3 ;

    private String id, name, type,species,imageUrl;
    private Date lastWater;
    private float humidity;
    private boolean open_tap;

    public Plant() {}

    public Plant(String id) {
        this.id = id;
    }
    public Plant(String id, String name) {
        this.id = id;
        this.name = name;
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

    public boolean isOpen_tap() {
        return open_tap;
    }

    public void setOpen_tap(boolean open_tap) {
        this.open_tap = open_tap;
    }

    @Override
    public boolean equals(Object obj) {
        Plant plant = (Plant) obj;
        return getId()==plant.getId();
    }
    public int getStatus() {
        if (isOpen_tap()&&getHumidity()>HUMID){
            return WATERING_HUMID;
        }else if (isOpen_tap()&&getHumidity()<DRY){
            return WATERING_DRY;
        }else if (isOpen_tap()){
            return WATERING_NORMAL;
        }else if (getHumidity()<DRY){
            return DRY;
        }else if (getHumidity()>HUMID){
            return HUMID;
        } else
            return NORMAL;
    }
    public void update(Plant newPlant) {
        id = newPlant.getId();
        name = newPlant.getName();
        type = newPlant.getType();
        species = newPlant.getSpecies();
        imageUrl = newPlant.getImageUrl();
        lastWater = newPlant.getLastWater();
        humidity= newPlant.getHumidity();
        open_tap = newPlant.isOpen_tap();
    }
}
