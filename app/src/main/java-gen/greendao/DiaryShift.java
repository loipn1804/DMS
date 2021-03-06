package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table DIARY_SHIFT.
 */
public class DiaryShift {

    private Long id;
    private Long diary_type_id;
    private Long user_id;
    private String name;
    private String content;
    private String enter_time;
    private String exit_time;
    private Integer is_exited;
    private Long created_by;

    public DiaryShift() {
    }

    public DiaryShift(Long id) {
        this.id = id;
    }

    public DiaryShift(Long id, Long diary_type_id, Long user_id, String name, String content, String enter_time, String exit_time, Integer is_exited, Long created_by) {
        this.id = id;
        this.diary_type_id = diary_type_id;
        this.user_id = user_id;
        this.name = name;
        this.content = content;
        this.enter_time = enter_time;
        this.exit_time = exit_time;
        this.is_exited = is_exited;
        this.created_by = created_by;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDiary_type_id() {
        return diary_type_id;
    }

    public void setDiary_type_id(Long diary_type_id) {
        this.diary_type_id = diary_type_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEnter_time() {
        return enter_time;
    }

    public void setEnter_time(String enter_time) {
        this.enter_time = enter_time;
    }

    public String getExit_time() {
        return exit_time;
    }

    public void setExit_time(String exit_time) {
        this.exit_time = exit_time;
    }

    public Integer getIs_exited() {
        return is_exited;
    }

    public void setIs_exited(Integer is_exited) {
        this.is_exited = is_exited;
    }

    public Long getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Long created_by) {
        this.created_by = created_by;
    }

}
