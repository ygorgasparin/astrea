package br.com.aurum.astrea.dao;

import br.com.aurum.astrea.domain.Contact;
import com.googlecode.objectify.ObjectifyService;

import java.util.ArrayList;
import java.util.List;

public class ContactDao {

    static {
        ObjectifyService.register(Contact.class);
    }

    public void save(Contact contact) {
        // TODO: É preciso pesquisar como se usa o Objectify para armazenar a entidade contato no banco de dados.
        ObjectifyService.ofy().save().entity(contact).now();
    }

    public List<Contact> list() {
        // TODO: É preciso pesquisar como se usa o Objectify para listar as entidades de contato.
        return new ArrayList<>();
    }

    public void delete(Long contactId) {
        // TODO: É preciso pesquisar como se usa o Objectify para deletar entidades do banco de dados.
    }
}
