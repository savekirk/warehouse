package controllers;

import com.avaje.ebean.Page;
import com.google.common.io.Files;
import models.Product;
import models.StockItem;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import views.html.catalog;
import views.html.products.list;
import views.html.products.details;
import static play.mvc.Http.MultipartFormData;


import java.io.File;
import java.io.IOException;
import java.util.List;

@With(CatchAction.class)
public class Products extends Controller {
    private static final Form<Product> productForm = Form.form(Product.class);

    public static Result index() {
        return redirect(routes.Products.list(0));
    }

    public static Result list(Integer page){
        Page<Product> products = Product.find(page);
        return ok(catalog.render(products));
    }

    public static Result newProduct(){
        return ok(details.render(productForm));
    }

    public static Result details(Product product) {
        /*//final Product product = Product.findByEan(ean);
        if(product == null) {
            return notFound(String.format("Product %s not found", ean));
        }*/

        Form<Product> filledForm = productForm.fill(product);

        return ok(details.render(filledForm));
    }

    public static Result delete(String ean) {
        final Product product = Product.findByEan(ean);
        if(product == null) {
            return notFound("Product %s not found", ean);
        }

        product.delete();
        return redirect(routes.Products.list(0));
    }

    public static Result save() {
        Form<Product> boundForm = productForm.bindFromRequest();
        if(boundForm.hasErrors()) {
            flash("error", "Please correct the form below");
            return badRequest(details.render(boundForm));
        }
        Product product = boundForm.get();
        MultipartFormData body = request().body().asMultipartFormData();
        MultipartFormData.FilePart part = body.getFile("picture");

        if(part != null) {
            File picture = part.getFile();
            try {
                product.picture = Files.toByteArray(picture);
            } catch (IOException e) {
                return internalServerError("Error reading file upload");
            }
        }

        StockItem item = new StockItem();
        item.quantity = 0L;
        item.product = product;
        if(product.id == null) {
        product.save();
        } else {
            product.update();
        }
        item.save();
        flash("success", String.format("Successfully added product %s", product));

        return redirect(routes.Products.list(0));
    }

    public static Result picture(String ean) {
        final Product product = Product.findByEan(ean);
        if(product == null) return notFound();
        return ok(product.picture);
    }




}
