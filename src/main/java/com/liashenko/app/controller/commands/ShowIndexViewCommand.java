package com.liashenko.app.controller.commands;

import com.liashenko.app.controller.manager.PageManagerConf;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowIndexViewCommand implements ICommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return PageManagerConf.getInstance().getProperty(PageManagerConf.INDEX_PAGE_PATH);
    }
}
