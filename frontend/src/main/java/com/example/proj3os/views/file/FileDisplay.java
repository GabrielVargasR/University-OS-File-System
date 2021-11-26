package com.example.proj3os.views.file;

import java.util.Date;

import com.example.proj3os.controllers.FileController;
import com.example.proj3os.helper.IConstants;
import com.example.proj3os.model.File;
import com.example.proj3os.model.SessionInfo;
import com.example.proj3os.views.MainLayout;
import com.example.proj3os.views.files.FilesView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("FileView")
@Route(value = "fileView", layout = MainLayout.class)
public class FileDisplay extends VerticalLayout implements BeforeEnterObserver {
    
    private File file;
    private SessionInfo session;

    private TextArea mainTextArea;
    
    public FileDisplay(){
        session = SessionInfo.getInstance();
    }

    private void setup() {
        setTextArea();
        setButtons();
    }

    private void setTextArea() {
        mainTextArea = new TextArea(file.getName());
        mainTextArea.setValue(file.getContents());
        mainTextArea.setPlaceholder("Write here ...");
        mainTextArea.getStyle().set("maxHeight", "550px");
        mainTextArea.getStyle().set("minHeight", "150px");
        add(mainTextArea);
    }

    private void setButtons() {
        Button saveButton = new Button(new Icon(VaadinIcon.CHECK_CIRCLE));
        saveButton.addClickListener(event ->{
            saveFile();
            session.setCurrentDirectory(IConstants.ROOT);
            UI.getCurrent().navigate(FilesView.class);
        });

        add(saveButton);
    }

    private void saveFile() {
        String content = mainTextArea.getValue();
        String size = Integer.toString(content.length());
        FileController.modifyFile(file.getName(), session.getUsername(), session.getCurrentDirectory(), file.getCreation(), new Date().toString() , file.getExtension(), size, content);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        file = SessionInfo.getFileToOpen();
        setup();   
    }
}
