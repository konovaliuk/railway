package com.liashenko.app.web.controller.commands;

import com.liashenko.app.service.ServiceFactory;
import com.liashenko.app.service.ServiceTestFactoryImpl;
import com.liashenko.app.web.controller.manager.PageManagerConf;
import com.liashenko.app.web.controller.utils.HttpParser;
import com.liashenko.app.web.controller.utils.SessionAttrInitializer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SignOutCommandTest {
    private ServiceFactory serviceFactory = ServiceTestFactoryImpl.getInstance();
    private ICommand command = new SignOutCommand(serviceFactory);

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    public void invalidatesSessionIfItIsNotNew() throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(Boolean.TRUE)).thenReturn(session);
        when(session.isNew()).thenReturn(Boolean.FALSE);
        command.execute(request, response);
        verify(session).invalidate();
    }
}
