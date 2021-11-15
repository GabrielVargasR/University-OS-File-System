package com.example.proj3os.views.files;

import com.example.proj3os.model.FsFile;
import com.example.proj3os.controllers.*;
import com.example.proj3os.model.SessionInfo;
import com.example.proj3os.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;



@PageTitle("Files")
@Route(value = "files", layout = MainLayout.class)
public class FilesView extends VerticalLayout {
    private String user;
    private String route;

    public FilesView() {
        SessionInfo session = SessionInfo.getInstance();
        user = session.getUsername();
        route = session.getCurrentDirectory();

        setSizeFull();

        Grid<FsFile> grid = new Grid<>(FsFile.class, false);
        grid.addComponentColumn(item -> {
            Icon icon;
            if (item.isDirectory()) {
                icon = new Icon(VaadinIcon.FOLDER);
            } else {
                icon = new Icon(VaadinIcon.FILE_TEXT_O);
            }
            return icon;
        }).setFlexGrow(0);
        grid.addColumn(FsFile::getName).setHeader("Name");
        grid.addColumn(FsFile::getExtension).setHeader("Extension");
        grid.addColumn(FsFile::getCreation).setHeader("Creation Date");
        grid.addColumn(FsFile::getModification).setHeader("Last Modification");
        grid.addColumn(FsFile::getSize).setHeader("Size");
        FileController fc = new FileController();

        grid.setItems(fc.getFiles(user, route));
        grid.addItemDoubleClickListener(event -> {
            if (event.getItem().isDirectory()) {
                add(new Paragraph("Aquí (FilesView.java) iría el código para cambiar los items del grid"));
            } else {
                add(new Paragraph("Aquí (FilesView.java) iría el código para abrir el archivo"));
            }
        });

        add(grid);
    }
}
