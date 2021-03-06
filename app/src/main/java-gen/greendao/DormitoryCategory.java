package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table DORMITORY_CATEGORY.
 */
public class DormitoryCategory {

    private Long id;
    private Long dormitory_id;
    private Long category_id;

    public DormitoryCategory() {
    }

    public DormitoryCategory(Long id) {
        this.id = id;
    }

    public DormitoryCategory(Long id, Long dormitory_id, Long category_id) {
        this.id = id;
        this.dormitory_id = dormitory_id;
        this.category_id = category_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDormitory_id() {
        return dormitory_id;
    }

    public void setDormitory_id(Long dormitory_id) {
        this.dormitory_id = dormitory_id;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

}
