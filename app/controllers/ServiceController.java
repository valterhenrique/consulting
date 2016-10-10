package controllers;

import contants.ModeConst;
import models.Service;
import play.Logger;
import play.Routes;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.service.list;
import views.html.service.info;

import javax.inject.Inject;
import java.util.List;

public class ServiceController extends Controller {
    private static Logger.ALogger logger = Logger.of(ServiceController.class);

    @Inject
    public FormFactory formFactory;

    public Result list() {
        List<Service> allServices = Service.findAll();

        // to return as JSON format
        // return ok(Json.toJson(allServices));

        return ok(list.render(allServices));
    }

    public Result add() {
        return ok(info.render(formFactory.form(Service.class), ModeConst.ADD));
    }

    public Result save(String mode) {
        Form<Service> form = formFactory.form(Service.class).bindFromRequest();

        if (form.hasErrors()) {
            logger.debug("Form has error(s).");
            return badRequest(info.render(form, mode));
        }

        Service service = form.get();
        if (service != null) {
            if (ModeConst.ADD.equals(mode)) {
                if (Service.exists(service.id)) {
                    flash("errorsFound", "Service code already exists");
                    return badRequest(info.render(form, mode));
                }
                service.save();
                return redirect(routes.ServiceController.add());
            }

            if (mode.startsWith(ModeConst.EDIT)) {
                service.update();
                return redirect(routes.ServiceController.edit(service.id));
            }
        }

        flash("errorsFound", "Please fix the errors on the page");
        return badRequest(info.render(form, mode));
    }

    public Result jsServiceRoutes(){
        response().setContentType("text/javascript");
        return ok(Routes.javascriptRouter("jsServiceRoutes", routes.javascript.ServiceController.find()));
    }

    public Result find(Long id){
        Service service = Service.retrieve(id);
        if (service == null){
            return ok("{}");
        }

        return ok(Json.toJson(service));
    }

    public Result edit(Long id) {
        Service service = Service.retrieve(id);
        if (service == null) {
            return notFound(id + "was not found");
        }

        Form<Service> fillForm = formFactory.form(Service.class).fill(service);
        return ok(info.render(fillForm, ModeConst.EDIT + " : " + service.id));
    }

    public Result delete(Long id) {
        Service service = Service.retrieve(id);
        if (service == null) {
            return notFound(id + "was not found");
        }
        service.delete();
        return redirect(routes.ServiceController.list());
    }


}
