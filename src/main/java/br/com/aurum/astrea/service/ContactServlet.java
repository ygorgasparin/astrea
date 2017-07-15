package br.com.aurum.astrea.service;

import br.com.aurum.astrea.dao.ContactDao;
import br.com.aurum.astrea.domain.Contact;
import com.google.appengine.repackaged.com.google.common.base.StringUtil;
import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class ContactServlet extends HttpServlet {

    private static final ContactDao DAO = new ContactDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Contact contact = new Gson().fromJson(req.getReader(), Contact.class);

        if (StringUtil.isEmptyOrWhitespace(contact.getName())) {
            resp.setStatus(400);
            writeJson(resp, new ErrorResult("É obrigatório informar ao menos o nome do contato"));
            return;
        }

        try {
            DAO.save(contact);
            resp.setStatus(202);
        } catch (Exception ex) {
            resp.setStatus(500);
            writeJson(resp, new ErrorResult("Não foi possível salvar o contato"));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/json");
            writeJson(resp, DAO.list());
        } catch (Exception ex) {
            resp.setStatus(500);
            writeJson(resp, new ErrorResult("Não foi possível obter a lista de contatos"));
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");

        if (pathParts.length != 2) {
            resp.setStatus(400);
            writeJson(resp, new ErrorResult("Deve ser informado um id válido na url ex: /contacts/{id}"));
            return;
        }

        try {
            String part1 = pathParts[1];
            DAO.delete(Long.parseLong(part1));
        } catch (Exception ex) {
            resp.setStatus(500);
            writeJson(resp, new ErrorResult("Erro ao deletar contato"));
        }
    }

    protected void writeJson(HttpServletResponse resp, Object o) throws IOException {
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(o));
        out.flush();
    }
}
