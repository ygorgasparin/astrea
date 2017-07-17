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


/**
 * API para processamento do Contact, aceita os seguintes metodos http:
 * POST     /contacts
 * GET      /contacts
 * GET      /contacts?filter={filter}
 * DELETE   /contacts/{id}
 */
public class ContactServlet extends HttpServlet {

    private static final ContactDao DAO = new ContactDao();

    static final String JSON_INVALIDO = "Formato do JSON invalido";
    static final String NOME_OBRIGATORIO = "É obrigatório informar ao menos o nome do contato";
    static final String ERROR_POST = "Nao foi possivel salvar o contato";
    static final String ERROR_GET = "Nao foi possivel obter a lista de contatos";
    static final String ERROR_DELETE = "Erro ao deletar contato";
    static final String DELETE_PATH_ERROR = "Deve ser informado um id válido na url ex: /contacts/{id}";

    /**
     * Salva ou atualiza o objeto Contact enviado no body do request. O JSON enviado deve seguir o
     * formato padrao do model {@link Contact}. Caso contrario um erro sera retornado.
     * <p>
     * E obrigatorio que o objeto contact possua pelo menos a propriedade 'name' preenchida,
     * caso contrario um erro sera retornado.
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Contact contact = this.requestToContact(req);

        if (contact == null) {
            resp.setStatus(400);
            writeJson(resp, new ErrorResult(JSON_INVALIDO));
            return;
        }

        if (StringUtil.isEmptyOrWhitespace(contact.getName())) {
            resp.setStatus(400);
            writeJson(resp, new ErrorResult(NOME_OBRIGATORIO));
            return;
        }

        try {
            DAO.save(contact);
            resp.setStatus(202);
        } catch (Exception ex) {
            resp.setStatus(500);
            ex.printStackTrace();
            writeJson(resp, new ErrorResult(ERROR_POST));
        }
    }

    /**
     * Retorna uma lista dos objetos contact, para filtrar por name, cpf ou email deve ser enviado o parametro
     * filter na url, ex:
     * <p>
     * GET /contacts?filter=ygor
     * <p>
     * Se nao for informado o parametro 'filter' ira retornar todos os objetos do banco de dados
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/json");

            String filter = req.getParameter("filter");
            if (filter != null)
                writeJson(resp, DAO.list(filter));
            else
                writeJson(resp, DAO.list());

        } catch (Exception ex) {
            ex.printStackTrace();
            resp.setStatus(500);
            writeJson(resp, new ErrorResult(ERROR_GET));
        }

    }

    /**
     * Faz o delete do id passado na url, a url deve seguir o padrao rest:
     * DELETE /contacts/{id}
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");

        if (pathParts.length != 2) {
            resp.setStatus(400);
            writeJson(resp, new ErrorResult(DELETE_PATH_ERROR));
            return;
        }

        try {
            String part1 = pathParts[1];
            DAO.delete(Long.parseLong(part1));
        } catch (Exception ex) {
            resp.setStatus(500);
            ex.printStackTrace();
            writeJson(resp, new ErrorResult(ERROR_DELETE));
        }
    }

    /**
     * Verifica se existe um objeto Contact no request, se existir retorna este objeto, caso contrario
     * retorna null.
     *
     * @param req
     * @return
     */
    protected Contact requestToContact(HttpServletRequest req) {
        try {
            return new Gson().fromJson(req.getReader(), Contact.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Classe auxiliar para escrever o objeto passado como parametro no response em um formato JSON
     *
     * @param resp
     * @param o
     * @throws IOException
     */
    protected void writeJson(HttpServletResponse resp, Object o) throws IOException {
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(o));
        out.flush();
    }
}
