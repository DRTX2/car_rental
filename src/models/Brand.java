package models;

/**
 *
 * @author PC
 */
public class Brand {
    private Integer id;
    private String name;

    public Brand(String name) {
        this.name = name;
    }
    
    public Brand(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
}
