package com.example.proj3os.views.files;

import com.example.proj3os.controllers.FileController;
import com.example.proj3os.model.Breadcrumb;
import com.example.proj3os.model.FsFile;
import com.example.proj3os.model.SessionInfo;
import com.example.proj3os.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;

import static com.example.proj3os.helper.IConstants.ROOT;


@PageTitle("Files")
@Route(value = "files", layout = MainLayout.class)
public class FilesView extends VerticalLayout {
    private Grid<FsFile> grid;
    private MenuBar menuBar;


    public FilesView() {
        SessionInfo session = SessionInfo.getInstance();
        setSizeFull();

        this.grid = new Grid<>(FsFile.class, false);
        GridContextMenu<FsFile> gridContextMenu = grid.addContextMenu();
        gridContextMenu.addItem("New File", event -> {
            if(FileController.createFile("New File", session.getUsername(), session.getCurrentDirectory())){
                Notification.show("File Created");
                updateGrid();
            } else {
                Notification.show("Could not create file");
            }
        });

        gridContextMenu.addItem("New Folder", event -> {
            if(FileController.createDirectory("New Folder", session.getUsername(), session.getCurrentDirectory())){
                Notification.show("Folder Created");
                updateGrid();
            } else {
                Notification.show("Could not create folder");
            }
        });


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
        grid.addColumn(FsFile::getSizeString).setHeader("Size");

        //grid.getColumns().forEach(gridContext::setTarget);

        updateGrid();
        grid.addItemDoubleClickListener(event -> {
            if (event.getItem().isDirectory()) {
                String dirName = event.getItem().getName().trim();
                session.setCurrentDirectory(session.getCurrentDirectory()+"/"+dirName);
                session.getBreadCrumbs().add(new Breadcrumb(dirName, session.getBreadCrumbs().size()));
                updateMenuBar();
                updateGrid();
            } else {
                add(new Paragraph("Aquí (FilesView.java) iría el código para abrir el archivo"));
            }
        });

        this.menuBar = new MenuBar();
        session.getBreadCrumbs().add(new Breadcrumb(ROOT, session.getBreadCrumbs().size()));
        updateMenuBar();
        add(menuBar, grid);
    }

    public void updateGrid(){
        this.grid.setItems(FileController.getFiles(SessionInfo.getInstance().getUsername(), SessionInfo.getInstance().getCurrentDirectory()));
    }

    public void updateMenuBar(){
        ArrayList<Breadcrumb> breadCrumbs = SessionInfo.getInstance().getBreadCrumbs();
        menuBar.removeAll();

        for (Breadcrumb breadCrumb : breadCrumbs) {
            String dirName = breadCrumb.getDirName();
            int index = breadCrumb.getIndex();
            ComponentEventListener<ClickEvent<MenuItem>> event = e -> {
                ArrayList<Breadcrumb> breadCrumbsCurrent = SessionInfo.getInstance().getBreadCrumbs();
                if(index <   breadCrumbsCurrent.size()){
                    breadCrumbsCurrent.subList(index+1, breadCrumbsCurrent.size()).clear();
                }
            };
            this.menuBar.addItem(dirName, event);
        }
    }
}
