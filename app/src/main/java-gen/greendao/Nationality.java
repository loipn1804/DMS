package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table NATIONALITY.
 */
public class Nationality {

    private String code;
    private String slug;
    private String name;
    private Integer order;

    public Nationality() {
    }

    public Nationality(String code) {
        this.code = code;
    }

    public Nationality(String code, String slug, String name, Integer order) {
        this.code = code;
        this.slug = slug;
        this.name = name;
        this.order = order;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

}
