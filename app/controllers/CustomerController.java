package controllers;

import contants.ModeConst;
import models.Customer;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.customer.info;
import views.html.customer.list;

import javax.inject.Inject;
import java.util.List;

public class CustomerController extends Controller {
    private static Logger.ALogger logger = Logger.of(CustomerController.class);

    @Inject
    public FormFactory formFactory;

    public Result list(){
        List<Customer> customerList = Customer.findAll();
        return ok(list.render(customerList));
    }

    public Result add(){
        Form<Customer> form = formFactory.form(Customer.class).bindFromRequest();
        return ok(info.render(form, ModeConst.ADD));
    }

    public Result save(String mode){
        Form<Customer> form = formFactory.form(Customer.class).bindFromRequest();

        if (form.hasErrors()){
            logger.debug("Customer form has erros");
            return badRequest(info.render(form, mode));
        }

        Customer customer = form.get();
        if (customer != null){
            if (ModeConst.ADD.equals(mode)){
                customer.save();
                return  redirect(routes.CustomerController.add());
            }else if (mode.startsWith(ModeConst.EDIT)){
                customer.update();
                return redirect(routes.CustomerController.edit(customer.id));
            }
        }
        return badRequest(info.render(form,mode));
    }

    public Result edit(Long id){
        Customer customer = Customer.retrieve(id);
        Form<Customer> form = formFactory.form(Customer.class).fill(customer);
        return ok(info.render(form,ModeConst.EDIT + ":" + customer.name));
    }

    public Result delete(Long id){
        Customer customer = Customer.retrieve(id);
        if (customer != null){
            customer.delete();
        }

        return redirect(routes.CustomerController.list());
    }
}
