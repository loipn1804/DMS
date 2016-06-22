package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table BLOCK.
 */
public class Block {

    private Long id;
    private Integer dormitory_id;
    private String name;

    public Block() {
    }

    public Block(Long id) {
        this.id = id;
    }

    public Block(Long id, Integer dormitory_id, String name) {
        this.id = id;
        this.dormitory_id = dormitory_id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDormitory_id() {
        return dormitory_id;
    }

    public void setDormitory_id(Integer dormitory_id) {
        this.dormitory_id = dormitory_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
