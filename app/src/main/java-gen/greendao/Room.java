package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ROOM.
 */
public class Room {

    private Long id;
    private Integer level_id;
    private String name;

    public Room() {
    }

    public Room(Long id) {
        this.id = id;
    }

    public Room(Long id, Integer level_id, String name) {
        this.id = id;
        this.level_id = level_id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel_id() {
        return level_id;
    }

    public void setLevel_id(Integer level_id) {
        this.level_id = level_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}