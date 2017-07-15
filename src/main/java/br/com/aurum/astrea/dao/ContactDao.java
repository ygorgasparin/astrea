package br.com.aurum.astrea.dao;

import br.com.aurum.astrea.domain.Contact;
import com.googlecode.objectify.ObjectifyService;

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

    public void delete(Long contactId) {
        ObjectifyService.ofy().delete().type(Contact.class).id(contactId);
    }
}
