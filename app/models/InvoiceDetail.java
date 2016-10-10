package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

import static models.Service.find;

@Entity
@Table(name="invoice_detail")
public class InvoiceDetail extends Model{

    @Id
    public Long id;

    @Constraints.Required(message = "select.message")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    public Service service;

    @Constraints.Required(message = "required.message")
    @Constraints.MaxLength(value = 50, message = "length.message")
    @Constraints.MinLength(value = 3, message = "length.message")
    public String description;

    @Constraints.Required(message = "required.message")
    public double amount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    public Invoice invoice;

    private static Finder<String, InvoiceDetail> find = new Model.Finder<String, InvoiceDetail>(InvoiceDetail.class);

    public static List<InvoiceDetail> findAll(){
        return find.orderBy("id").findList();
    }

    public static List<InvoiceDetail> findAllyByInvoice(Long invoiceId){
        return find.fetch("invoice").where().eq("invoice.id",invoiceId).orderBy("id").findList();
    }

    public static InvoiceDetail retrieve(Long id){
        return find.byId(id.toString());
    }

    public static boolean exists(Long id){
        return (find.where().eq("id", id).findRowCount() == 1 ? true : false);
    }

}
