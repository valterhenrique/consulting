package models;


import com.avaje.ebean.Model;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static contants.ModeConst.ADD;

@Entity
@Table(name = "service")
public class Service extends Model {

    @Id
    @Constraints.Required(message = "required.message")
    public Long id;

    @Constraints.Required(message = "required.message")
    @Constraints.MaxLength(value = 50, message = "length.message")
    @Constraints.MinLength(value = 3, message = "length.message")
    public String description;

    @Constraints.Required(message = "required.message")
    public Double amount;

    @Transient
    public String mode;

    public static Finder<Long, Service> find = new Finder<Long, Service>(Service.class);

    public static List<Service> findAll() {
        return Service.find.orderBy("id").findList();
    }

    public static Service retrieve(Long id) {
        return find.ref(id);
    }

    public static boolean exists(Long id) {
        return (find.where().eq("id", id).findRowCount() == 1 ? true : false);
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        if (exists(id)) {
            if (ADD.equals(mode)) {
                errors.add(new ValidationError("id", "Duplicated ID"));
            }
        }
        return errors.isEmpty() ? null : errors;
    }

    public static Map<String, String> options() {
        Map<String, String> map = new LinkedHashMap<>();
        List<Service> services = findAll();
        for (Service service : services) {
            map.put(service.id.toString(), service.id.toString());
        }
        return map;
    }
}

