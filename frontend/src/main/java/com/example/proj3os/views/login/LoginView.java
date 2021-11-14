package com.example.proj3os.views.login;

import com.example.proj3os.controllers.UserController;
import com.example.proj3os.views.about.AboutView;
import com.example.proj3os.views.signup.SignUpView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;
import org.springframework.http.HttpStatus;


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
        login.addLoginListener(event -> {
            UserController controller = new UserController();
            int status = controller.login(event.getUsername(), event.getPassword());
            if (status == HttpStatus.ACCEPTED.value()) {
                UI.getCurrent().navigate(AboutView.class);
            } else if (status == HttpStatus.UNAUTHORIZED.value()) {
                event.getSource().setEnabled(true);
                Notification.show("Incorrect password");
            } else if (status == HttpStatus.NOT_FOUND.value()) {
                event.getSource().setEnabled(true);
                Notification.show("User does not exist");
            }
        });
        add(login);

        RouterLink signUpLink = new RouterLink();
        signUpLink.add("Don't have an account?");
        signUpLink.setRoute(SignUpView.class);
        add(signUpLink);
    }
}
