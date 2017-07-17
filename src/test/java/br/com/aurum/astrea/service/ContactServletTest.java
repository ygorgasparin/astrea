package br.com.aurum.astrea.service;

import br.com.aurum.astrea.domain.Contact;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ContactServletTest {

    @Mock
    HttpServletResponse httpServletResponseMock;

    @Mock
    HttpServletRequest httpServletRequestMock;

    @Mock
    ContactServlet contactServletMock;

    @Mock
    PrintWriter printWriterMock;

    ContactServlet servlet;

    @Before
    public void setUp() throws Exception {
        this.servlet = new ContactServlet();
    }

    @Test
    public void doPost_nullContact() throws Exception {
        doCallRealMethod().when(contactServletMock).doPost(any(HttpServletRequest.class),
                any(HttpServletResponse.class));
        mockWriteJson(getAnswerForErrorWriteJson(ContactServlet.JSON_INVALIDO), null);
        contactServletMock.doPost(httpServletRequestMock, httpServletResponseMock);
    }

    @Test
    public void doPost_emptyName() throws Exception {
        doCallRealMethod().when(contactServletMock).doPost(any(HttpServletRequest.class),
                any(HttpServletResponse.class));
        mockWriteJson(getAnswerForErrorWriteJson(ContactServlet.NOME_OBRIGATORIO), new Contact());
        contactServletMock.doPost(httpServletRequestMock, httpServletResponseMock);
    }

    @Test
    public void doPost_genericError() throws Exception {
        doCallRealMethod().when(contactServletMock).doPost(any(HttpServletRequest.class),
                any(HttpServletResponse.class));
        Contact contact = new Contact();
        contact.setName("Ygor");
        mockWriteJson(getAnswerForErrorWriteJson(ContactServlet.ERROR_POST), contact);
        contactServletMock.doPost(httpServletRequestMock, httpServletResponseMock);
    }

    @Test
    public void doGet_genericError() throws Exception {
        doCallRealMethod().when(contactServletMock).doGet(any(HttpServletRequest.class),
                any(HttpServletResponse.class));

        doAnswer(getAnswerForErrorWriteJson(ContactServlet.ERROR_GET)).when(contactServletMock)
                .writeJson(any(HttpServletResponse.class), Matchers.any());

        contactServletMock.doGet(httpServletRequestMock, httpServletResponseMock);
    }

    @Test
    public void doDelete_wrongPath() throws Exception {
        doCallRealMethod().when(contactServletMock).doDelete(any(HttpServletRequest.class),
                any(HttpServletResponse.class));

        when(httpServletRequestMock.getPathInfo()).thenReturn("");

        doAnswer(getAnswerForErrorWriteJson(ContactServlet.DELETE_PATH_ERROR)).when(contactServletMock)
                .writeJson(any(HttpServletResponse.class), Matchers.any());

        contactServletMock.doDelete(httpServletRequestMock, httpServletResponseMock);
    }

    @Test
    public void doDelete_genericError() throws Exception {
        doCallRealMethod().when(contactServletMock).doDelete(any(HttpServletRequest.class),
                any(HttpServletResponse.class));

        when(httpServletRequestMock.getPathInfo()).thenReturn("/1234");

        doAnswer(getAnswerForErrorWriteJson(ContactServlet.ERROR_DELETE)).when(contactServletMock)
                .writeJson(any(HttpServletResponse.class), Matchers.any());

        contactServletMock.doDelete(httpServletRequestMock, httpServletResponseMock);
    }

    @Test()
    public void requestToContact_error() throws Exception {
        String json = "asdf";
        when(httpServletRequestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        Assert.assertNull(servlet.requestToContact(httpServletRequestMock));
    }

    @Test()
    public void requestToContact_success() throws Exception {
        String expected_name = "Ygor";
        String json = "{\"name\":\"" + expected_name + "\"}";
        when(httpServletRequestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        Contact c = servlet.requestToContact(httpServletRequestMock);
        Assert.assertNotNull(c);
        Assert.assertEquals(expected_name, c.getName());
    }

    @Test
    public void writeJson() throws Exception {
        String message = "Error";
        ErrorResult errorResult = new ErrorResult(message);
        final String expectedJson = "{\"message\":\"" + message + "\"}";

        mockPrinterWriter(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Assert.assertEquals(expectedJson, args[0]);
                return null;
            }
        });

        servlet.writeJson(httpServletResponseMock, errorResult);
    }

//    private void mockWriteJsonErrorExpected(Contact contact, final String messageExpted) throws IOException {
//        mockWriteJson(getAnswerForErrorWriteJson(messageExpted), contact);
//    }

    private Answer getAnswerForErrorWriteJson(final String expected) {
        return new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Object arg = args[1];
                Assert.assertTrue((arg instanceof ErrorResult));
                Assert.assertEquals(expected, ((ErrorResult) arg).getMessage());
                return null;
            }
        };
    }


    private void mockWriteJson(Answer answer, Contact contact) throws IOException {
        when(contactServletMock.requestToContact(any(HttpServletRequest.class))).thenReturn(contact);
        doAnswer(answer).when(contactServletMock).writeJson(any(HttpServletResponse.class), Matchers.any());
    }

    private void mockPrinterWriter(Answer<Void> answer) throws IOException {
        doAnswer(answer).when(printWriterMock).print(anyString());
        when(httpServletResponseMock.getWriter()).thenReturn(printWriterMock);
    }

}