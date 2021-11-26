package com.example.proj3os.views.signup;

import com.example.proj3os.controllers.UserController;
import com.example.proj3os.views.about.AboutView;
import com.example.proj3os.views.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.http.HttpStatus;

@PageTitle("Sign Up")
@Route("signup")
public class SignUpView extends VerticalLayout {

    private int maxSize = 20;

    public SignUpView() {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("Sign up");
        i18n.getForm().setSubmit("Sign up");

        LoginForm signUp = new LoginForm();
        signUp.setForgotPasswordButtonVisible(false);
        signUp.setI18n(i18n);
        signUp.addLoginListener(event -> {
            UserController controller = new UserController();
            int status = controller.signup(event.getUsername(), event.getPassword(), getMaxSize());
            if (status == HttpStatus.CREATED.value()) {
                UI.getCurrent().navigate(LoginView.class);
                Notification.show("User created successfully! Log in to continue");
            } else if (status == HttpStatus.CONFLICT.value()) {
                event.getSource().setEnabled(true);
                Notification.show("A user already exists with that name. Please try again.");
            }
        });
        add(signUp);

        RouterLink logInLink = new RouterLink();
        logInLink.add("Already have an account?");
        logInLink.setRoute(LoginView.class);



        NumberField numberField = new NumberField("Max Space to Assign");
        numberField.setHasControls(true);
        numberField.setValue(1d);
        numberField.setMin(1);
        numberField.addInputListener(event ->{
            setMaxSize(numberField.getValue().intValue());
        });
        add(logInLink);
        add(numberField);
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }
}
