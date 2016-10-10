package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dev on 14/10/2015.
 */

@Entity
@Table(name = "customer")
public class Customer extends Model {

    @Id
    public Long id;

    @Constraints.Required(message = "required.message")
    public String name;

    public String address;

    @Constraints.Required(message = "required.message")
    public String city;

    @Constraints.Required(message = "required.message")
    public String state;

    @Constraints.Required(message = "required.message")
    public String postcode;

//    @Constraints.Pattern("")
    @Constraints.Required(message = "required.message")
    public String phone;

    //    @Constraints.Email
    @Constraints.Required(message = "required.message")
    public String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    public List<Invoice> invoices;

    private static Model.Finder<String, Customer> find =
            new Model.Finder<String, Customer>(Customer.class);

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String PHONE_PATTERN = "^([(]{1}\\d{3}[)]{1} ?\\d{3}-{1}\\d{4})$";

    public static List<Customer> findAll() {
        return find.orderBy("name").findList();
    }

    public static Customer retrieve(Long id) {
        return find.byId(id.toString());
    }

    public static boolean exists(Long id) {
        return (find.where().eq("id", id).findRowCount() == 1) ? true : false;
    }

    public static Map<String, String> options() {
        Map<String, String> map = new LinkedHashMap<String,String>();

        List<Customer> list = findAll();
        for(Customer c: list) {
            map.put(c.id.toString(), c.name);
        }

        return map;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        if(!phone.matches(PHONE_PATTERN)) {
            errors.add(new ValidationError("phone", "Invalid phone number. Use format (999) 999-9999"));
        }

        if(!email.matches(EMAIL_PATTERN)) {
            errors.add(new ValidationError("email", "Invalid email address"));
        }

        return errors.isEmpty() ? null : errors;
    }

}
























