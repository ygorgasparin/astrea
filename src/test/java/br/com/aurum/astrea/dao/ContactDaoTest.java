package br.com.aurum.astrea.dao;

import br.com.aurum.astrea.domain.Contact;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cache.AsyncCacheFilter;
import com.googlecode.objectify.util.Closeable;
import org.junit.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ContactDaoTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    protected Closeable session;
    protected ContactDao DAO;

    @BeforeClass
    public static void setUpBeforeClass() {
        ObjectifyService.setFactory(new ObjectifyFactory());
        ObjectifyService.register(Contact.class);
    }

    @Before
    public void setUp() throws Exception {
        this.session = ObjectifyService.begin();
        this.helper.setUp();
        this.DAO = new ContactDao();
    }

    @After
    public void tearDown() {
        AsyncCacheFilter.complete();
        this.session.close();
        this.helper.tearDown();
    }

    @Test
    public void save() throws Exception {
        String expected = "Ygor";
        Contact c = new Contact();
        c.setName(expected);
        DAO.save(c);

        Contact result = ofy().load().type(Contact.class).filter("name", expected).first().now();

        Assert.assertNotNull(result);
        Assert.assertEquals(expected, result.getName());
    }

    @Test
    public void save_update() throws Exception {
        String first = "Ygor";
        String expected = "Tiago";
        Contact c = new Contact();
        c.setName(first);
        DAO.save(c);

        c.setName(expected);
        DAO.save(c);

        Contact obtain1 = ofy().load().type(Contact.class).filter("name", first).first().now();
        Assert.assertNull(obtain1);

        Contact obtain2 = ofy().load().type(Contact.class).filter("name", expected).first().now();
        Assert.assertNotNull(obtain2);
        Assert.assertEquals(expected, obtain2.getName());
    }

    @Test
    public void list() throws Exception {
        int size_expected = 10;
        saveRandom(size_expected);
        List<Contact> list = DAO.list();
        Assert.assertNotNull(list);
        Assert.assertEquals(size_expected, list.size());
    }

    @Test
    public void list_filterName() throws Exception {
        saveRandom(10);
        String name = "Name";
        Contact c = new Contact();
        c.setName(name);
        DAO.save(c);

        List<Contact> list = DAO.list(name);
        Assert.assertNotNull(list);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(name, list.get(0).getName());
    }

    @Test
    public void list_filterCPF() throws Exception {
        saveRandom(10);
        String cpf = "123";
        Contact c = new Contact();
        c.setName("Name");
        c.setCpf(cpf);
        DAO.save(c);

        List<Contact> list = DAO.list(cpf);
        Assert.assertNotNull(list);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(cpf, list.get(0).getCpf());
    }

    @Test
    public void list_filterEmail() throws Exception {
        saveRandom(10);
        String email = "123";
        Contact c = new Contact();
        c.setName("Name");
        c.setEmails(Arrays.asList(email));
        DAO.save(c);

        List<Contact> list = DAO.list(email);
        Assert.assertNotNull(list);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(email, list.get(0).getEmails().get(0));
    }

    @Test
    public void delete() throws Exception {
        String expected = "Ygor";
        Contact c = new Contact();
        c.setName(expected);
        DAO.save(c);

        Contact result = ofy().load().type(Contact.class).filter("name", expected).first().now();
        Assert.assertNotNull(result);

        DAO.delete(c.getId());

        result = ofy().load().type(Contact.class).filter("name", expected).first().now();
        Assert.assertNull(result);
    }

    private List<Contact> saveRandom(int size) {
        List<Contact> contacts = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Contact c = new Contact();
            c.setName(new BigInteger(130, new SecureRandom()).toString(32));
            DAO.save(c);
        }
        return contacts;
    }

}