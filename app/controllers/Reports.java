package controllers;

import models.Report;
import play.libs.Akka;
import play.libs.F.Promise;
import play.libs.F.Function;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.report;

import java.util.List;
import java.util.concurrent.Callable;

import static play.libs.Akka.future;

/**
 * Created by sakirk on 4/21/14.
 */
public class Reports extends Controller {

    public static Result index() {

        Promise<Report> promiseOfKPIReport =
                future(
                        new Callable<Report>() {
                            public Report call() {
                                return intensiveKPIReport();
                            }
                        });
        Promise<Report> promiseOfETAReport =
                future(
                        new Callable<Report>() {
                            public Report call() {
                                return intensiveETAReport();
                            }
                        });
        Promise<List<Report>> promises =
                Promise.waitAll(promiseOfKPIReport, promiseOfETAReport);
        return async(
                promises.map(
                        new Function<List<Report>, Result>() {
                            @Override
                            public Result apply(List<Report> reports) throws Throwable {
                                return ok(report.render(reports));
                            }
                        }
                )
        );
    }

    public static Report intensiveKPIReport() {
        Report r = new Report("KPI report");
        r.execute();
        return r;
    }

    public static Report intensiveETAReport() {
        Report r = new Report("ETA report");
        r.execute();
        return r;
    }
}
