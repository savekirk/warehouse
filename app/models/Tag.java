package models;


import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Tag extends Model {

    @Id
    public Long id;
    public String name;

    @ManyToMany(mappedBy = "tags")
    public List<Product> products;

    public static Finder<Long, Tag> find(){
        return new Finder<>(Long.class, Tag.class);
    }
    public static Tag findById(Long id) {
        return find().byId(id);
    }
}
