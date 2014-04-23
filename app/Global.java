import com.avaje.ebean.Ebean;
import models.Product;
import models.Tag;
import play.Application;
import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.data.format.Formatters;
import play.libs.Yaml;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application) {
        super.onStart(application);
        InitialData.insert(application);
        Formatters.register(Date.class, new Product.AnnotationDateFormatter());
    }
    static class InitialData {
        public static void insert(Application app) {
            if(Ebean.find(Tag.class).findRowCount() == 0) {
                Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml.load("initial-data.yml");
                Ebean.save(all.get("tags"));
            }
        }
    }

   /* public <T extends EssentialFilter> Class<T>[] filters() {
        Class[] filters = {CSRFFilter.class};
        return filters;
    }*/
}
