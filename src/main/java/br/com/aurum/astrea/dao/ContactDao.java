package br.com.aurum.astrea.dao;

import br.com.aurum.astrea.domain.Contact;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;


/**
 * Responsavel por executar as operacoes CRUD do objeto contact
 */
public class ContactDao {

    static {
        ObjectifyService.register(Contact.class);
    }

    /**
     * Salva o objeto passado como parametro,
     * Se o id for igual a zero ou null: Sera criado um novo objeto
     * Se o id for diferente de zero e null: O id em questão será atualizado
     *
     * @param contact
     */
    public void save(Contact contact) {
        ofy().save().entity(contact).now();
    }

    /**
     * Retorna todos os objetos do tipo Contact existente no banco de dados
     *
     * @return
     */
    public List<Contact> list() {
        return ofy().load().type(Contact.class).list();
    }

    /**
     * Faz uma busca do tipo OR para as propriedades 'name','emails' e 'cpf', utilizando como filtro o argumento
     * passado como parametro
     *
     * @param filter
     * @return
     */
    public List<Contact> list(String filter) {

        Query<Contact> query = ofy().load().type(Contact.class);

        List<Contact> names = query.filter("name >=", filter).filter("name <", filter + "\uFFFD").list();
        List<Contact> emails = query.filter("emails >=", filter).filter("emails <", filter + "\uFFFD").list();
        List<Contact> cpfs = query.filter("cpf >=", filter).filter("cpf <", filter + "\uFFFD").list();

        HashSet<Contact> set = new HashSet<>(names);
        set.addAll(emails);
        set.addAll(cpfs);

        List merges = Arrays.asList(set.toArray());

        return merges;
    }

    /**
     * Exclui o objeto contact com o respectivo id;
     *
     * @param contactId
     */
    public void delete(Long contactId) {
        ofy().delete().type(Contact.class).id(contactId);
    }
}
