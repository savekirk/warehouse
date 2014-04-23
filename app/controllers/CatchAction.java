package controllers;

import play.mvc.SimpleResult;
import play.libs.F;
import play.mvc.Action;
import utils.ExceptionMailer;
import play.mvc.Http;

public class CatchAction extends Action.Simple{
    public F.Promise<SimpleResult> call(Http.Context ctx) {
        try {
            return delegate.call(ctx);
        } catch(Throwable e) {
            ExceptionMailer.send(e);
            throw new RuntimeException(e);
        }
    }
}
