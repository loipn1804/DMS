package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SUB_CATEGORY.
 */
public class SubCategory {

    private Long id;
    private Long category_id;
    private String name;
    private String description;

    public SubCategory() {
    }

    public SubCategory(Long id) {
        this.id = id;
    }

    public SubCategory(Long id, Long category_id, String name, String description) {
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
