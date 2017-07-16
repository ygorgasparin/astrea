package br.com.aurum.astrea.dao;

import br.com.aurum.astrea.domain.Contact;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ContactDao {

    static {
        ObjectifyService.register(Contact.class);
    }

    public void save(Contact contact) {
        ObjectifyService.ofy().save().entity(contact).now();
    }

    public List<Contact> list() {
        return ObjectifyService.ofy().load().type(Contact.class).list();
    }

    public List<Contact> list(String filter) {

        Query<Contact> query = ObjectifyService.ofy().load().type(Contact.class);

        List<Contact> names = query.filter("name >=", filter).filter("name <", filter + "\uFFFD").list();
        List<Contact> emails = query.filter("emails >=", filter).filter("emails <", filter + "\uFFFD").list();
        List<Contact> cpfs = query.filter("cpf >=", filter).filter("cpf <", filter + "\uFFFD").list();

        HashSet<Contact> set = new HashSet<>(names);
        set.addAll(emails);
        set.addAll(cpfs);

        List merges = Arrays.asList(set.toArray());

        return merges;
    }

    public void delete(Long contactId) {
        ObjectifyService.ofy().delete().type(Contact.class).id(contactId);
    }
}
