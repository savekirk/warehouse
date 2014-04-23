package models;

import com.avaje.ebean.Page;
import controllers.Products;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import play.data.format.Formatters;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.libs.F;
import play.mvc.PathBindable;
import utils.DateFormat;
import utils.EAN;

import javax.persistence.*;
import javax.validation.ConstraintValidator;

@Entity
public class Product extends Model implements PathBindable<Product>{





    public static Product findByEan(String ean) {
        return find.where().eq("ean",ean).findUnique();
    }




    @Id
    public Long id;

    @EAN( message = "Please enter a valid EAN")
    public String ean;
    @Constraints.Required
    public String name;
    public String description;
    @Transient
    public byte[] picture;

    @ManyToMany
    public List<Tag> tags;

    @OneToMany(mappedBy = "product")
    public List<StockItem> items;

    @DateFormat("MM-dd-yyyy")
    public Date date = new Date();

    public Product(){}

    public Product(String ean, String name, String description) {
        this.ean = ean;
        this.name = name;
        this.description = description;
    }

    public String toString() {
        return String.format("%s = %s", ean, name);
    }

    @Override
    public Product bind(String key, String value) {
        return findByEan(value);
    }

    @Override
    public String unbind(String key){
        return this.ean;
    }

    @Override
    public String javascriptUnbind() {
        return this.ean;
    }

    public static class EanValidator extends Constraints.Validator<String>
            implements ConstraintValidator<EAN, String> {
        final static public String message = "error.invalid.ean";
        public EanValidator(){}


        @Override
        public F.Tuple<String, Object[]> getErrorMessageKey() {
            return new F.Tuple<String, Object[]>(message,new Object[]{});
        }

        @Override
        public void initialize(EAN constraintAnnotation) {}

        @Override
        public boolean isValid(String value) {
            String pattern = "^[0-9]{13}$";
            return value != null && value.matches(pattern);
        }
    }

    public static Finder<Long, Product> find = new Finder<>(Long.class, Product.class);

    public static Page<Product> find(int page) {
        return
                find.where()
                .orderBy("id asc")
                .findPagingList(10)
                .setFetchAhead(false)
                .getPage(page);
    }

    public static class AnnotationDateFormatter
        extends Formatters.AnnotationFormatter<DateFormat, Date> {

        public Date parse(DateFormat annotation, String text, Locale locale)
            throws ParseException {
            if(text == null || text.trim().isEmpty()) {
                return null;
            }

            SimpleDateFormat sdf = new SimpleDateFormat(annotation.value(), locale);
            sdf.setLenient(false);
            return sdf.parse(text);
        }

        public String print(DateFormat annotation, Date value, Locale locale) {
            if(value == null) {
                return "";
            }

            return new SimpleDateFormat(annotation.value(), locale).format(value);
        }

    }

}
