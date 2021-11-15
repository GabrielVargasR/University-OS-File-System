package com.example.proj3os.views.files;

import com.example.proj3os.model.FsFile;
import com.example.proj3os.controllers.*;
import com.example.proj3os.model.SessionInfo;
import com.example.proj3os.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
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
        Grid<FsFile> grid = new Grid<>(FsFile.class, false);
        grid.addColumn(FsFile::getName).setHeader("Name");
        grid.addColumn(FsFile::getExtension).setHeader("Extension");
        grid.addColumn(FsFile::getCreation).setHeader("Creation Date");
        grid.addColumn(FsFile::getModification).setHeader("Last Modification");
        grid.addColumn(FsFile::getSize).setHeader("Size");
        FileController fc = new FileController();

        grid.setItems(fc.getFiles(user, route));

        add(grid);
    }
}
