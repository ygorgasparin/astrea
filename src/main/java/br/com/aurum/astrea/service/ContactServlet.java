package br.com.aurum.astrea.service;

import br.com.aurum.astrea.dao.ContactDao;
import br.com.aurum.astrea.domain.Contact;
import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
public class ContactServlet extends HttpServlet {

    private static final ContactDao DAO = new ContactDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        //TODO: Adicionar verificacoes

        Contact contact = new Gson().fromJson(req.getReader(), Contact.class);
        DAO.save(contact);
        resp.setStatus(202);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // TODO: Implementar um método que irá listar todas as entidades do tipo 'Contato' e devolver para o client essa listagem.
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // TODO: Implementar um método que irá deletar uma entidade do tipo 'Contato', dado parâmetro de identificação.
    }
}
