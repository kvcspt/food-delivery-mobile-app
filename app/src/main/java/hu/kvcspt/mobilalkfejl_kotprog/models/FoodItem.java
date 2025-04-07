package hu.kvcspt.mobilalkfejl_kotprog.models;

public class FoodItem {
    private String id;
    private String description;
    private String imageUrl;
    private String name;
    private Long price;

    public FoodItem() {
    }

    public FoodItem(String id, String description, String imageUrl, String name, Long price) {
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
