package controllers;

import contants.ModeConst;
import models.Invoice;
import models.InvoiceDetail;
import models.InvoiceSearchForm;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.invoice.info;
import views.html.invoice.info_detail;
import views.html.invoice.list;
import views.html.invoice.list_detail;

import javax.inject.Inject;
import java.util.List;

public class InvoiceController extends Controller {

    private static Logger.ALogger logger = Logger.of(InvoiceController.class);

    @Inject
    public FormFactory formFactory;

    public Result list() {
        List<Invoice> invoices = Invoice.findAll();
        Form<InvoiceSearchForm> form = formFactory.form(InvoiceSearchForm.class);
        return ok(list.render(invoices, form));
    }

    public Result search() {
        Form<InvoiceSearchForm> form = formFactory.form(InvoiceSearchForm.class).bindFromRequest();
        if (form.hasErrors()) {
            List<Invoice> invoices = Invoice.findAll();
            return badRequest(list.render(invoices, form));
        }

        InvoiceSearchForm search = form.get();
        if (search.name != null && search.name.length() > 0) {
            List<Invoice> invoices = Invoice.findAll(search.name);
            return ok(list.render(invoices, form));
        } else {
            List<Invoice> invoices = Invoice.findAll();
            return ok(list.render(invoices, form));
        }
    }

    public Result add() {
        return ok(info.render(formFactory.form(Invoice.class), ModeConst.ADD));
    }

    public Result save(String mode) {
        Form<Invoice> form = formFactory.form(Invoice.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(info.render(form, mode));
        }

        Invoice invoice = form.get();
        if (invoice != null) {
            if (ModeConst.ADD.equals(mode)) {
                invoice.save();
                invoice.invoiceId = invoice.id.toString();
                invoice.update();
                return redirect(routes.InvoiceController.edit(invoice.id));
            } else if (ModeConst.EDIT.equals(mode)) {
                invoice.update();
                return redirect(routes.InvoiceController.edit(invoice.id));
            }
        }

        return badRequest(info.render(form, mode));
    }

    public Result edit(Long id) {
        Invoice invoice = Invoice.retrieve(id);
        Form<Invoice> form = formFactory.form(Invoice.class).fill(invoice);
        return ok(info.render(form, ModeConst.EDIT));
    }

    public Result delete(Long id) {
        Invoice invoice = Invoice.retrieve(id);
        if (invoice != null) {
            invoice.delete();
        }
        return redirect(routes.InvoiceController.list());
    }

    public Result listInvoiceDetail(Long invoiceId) {
        List<InvoiceDetail> invoiceDetails = InvoiceDetail.findAllyByInvoice(invoiceId);
        return ok(list_detail.render(invoiceDetails, invoiceId));
    }

    public Result addInvoiceDetail(Long invoiceId) {
        Form<InvoiceDetail> form = formFactory.form(InvoiceDetail.class);
        return ok(info_detail.render(form, invoiceId, ModeConst.ADD));
    }

    public Result editInvoiceDetail(Long invoiceId, Long invoiceDetailId) {
        InvoiceDetail invoiceDetail = InvoiceDetail.retrieve(invoiceDetailId);
        Form<InvoiceDetail> form = formFactory.form(InvoiceDetail.class).fill(invoiceDetail);
        return ok(info_detail.render(form, invoiceId, ModeConst.EDIT));
    }

    public Result saveInvoiceDetail(Long invoiceId, String mode) {
        Form<InvoiceDetail> form = formFactory.form(InvoiceDetail.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(info_detail.render(form, invoiceId, mode));
        }
        InvoiceDetail invoiceDetail = form.get();
        if (invoiceDetail != null) {
            if (ModeConst.ADD.equals(mode)) {
                invoiceDetail.invoice = Invoice.retrieve(invoiceId);
                invoiceDetail.save();
                return redirect(routes.InvoiceController.addInvoiceDetail(invoiceId));
            } else if (ModeConst.EDIT.equals(mode)) {
                invoiceDetail.invoice = Invoice.retrieve(invoiceId);
                invoiceDetail.update();
                return redirect(routes.InvoiceController.editInvoiceDetail(invoiceId, invoiceDetail.id));
            }
        }
        return badRequest(info_detail.render(form, invoiceId, mode));
    }

    public Result deleteInvoiceDetail(Long invoiceId, Long invoiceDetailId) {
        InvoiceDetail invoiceDetail = InvoiceDetail.retrieve(invoiceId);

        if (invoiceDetail != null) {
            invoiceDetail.delete();
        }

        return redirect(routes.InvoiceController.listInvoiceDetail(invoiceId));
    }
}
