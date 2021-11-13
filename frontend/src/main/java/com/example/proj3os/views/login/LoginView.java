package com.example.proj3os.views.login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;

import javax.swing.text.html.parser.ContentModel;

@PageTitle("Login")
@Route("login")
@RouteAlias("")
public class LoginView extends VerticalLayout {
    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        LoginForm login = new LoginForm();
        login.setForgotPasswordButtonVisible(false);
        // agregar listener
        add(login);

        RouterLink signupLink = new RouterLink();
        signupLink.add("Don't have an account?");
        // agregar routing
        add(signupLink);
    }
}
