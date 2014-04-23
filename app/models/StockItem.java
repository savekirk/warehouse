package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StockItem extends Model {

    @Id
    public Long id;

    @ManyToOne
    public Warehouse warehouse;

    @ManyToOne
    public Product product;
    public Long quantity;

    public static Finder<Long, StockItem> find() {
        return new Finder<>(Long.class, StockItem.class);
    }
    public String toString() {
        return String.format("StockItem %d - %dx product %s \n",id, quantity, product == null ? null : product.id);
    }
}
