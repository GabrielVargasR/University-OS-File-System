package com.example.proj3os.views.files;

import com.example.proj3os.controllers.FileController;
import com.example.proj3os.model.*;
import com.example.proj3os.views.MainLayout;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.grid.contextmenu.GridSubMenu;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import net.minidev.json.JSONArray;

import java.util.ArrayList;

import static com.example.proj3os.helper.IConstants.DIRECTORY;
import static com.example.proj3os.helper.IConstants.ROOT;


@PageTitle("Files")
@Route(value = "files", layout = MainLayout.class)
public class FilesView extends VerticalLayout {
    private final Grid<FileSystemElement> grid;
    private final MenuBar menuBar;
    private final GridContextMenu<FileSystemElement> gridContextMenu;

    public FilesView() {
        SessionInfo session = SessionInfo.getInstance();
        setSizeFull();

        this.grid = new Grid<>(FileSystemElement.class, false);
        this.gridContextMenu = grid.addContextMenu();

        initGridContextMenu();


        grid.addComponentColumn(item -> {
            Icon icon;
            if (item.getType().equals(DIRECTORY)) {
                icon = new Icon(VaadinIcon.FOLDER);
            } else {
                icon = new Icon(VaadinIcon.FILE_TEXT_O);
            }
            return icon;
        }).setFlexGrow(0);

        grid.addColumn(FileSystemElement::getName).setHeader("Name");


        grid.addColumn(FileSystemElement::getExtension).setHeader("Extension");
        grid.addColumn(FileSystemElement::getCreation).setHeader("Creation Date");
        grid.addColumn(FileSystemElement::getModified).setHeader("Last Modified");
        grid.addColumn(FileSystemElement::getSizeString).setHeader("Size");

        //grid.getColumns().forEach(gridContext::setTarget);

        updateGrid();
        grid.addItemDoubleClickListener(event -> {
            if (event.getItem().getType().equals(DIRECTORY)) {
                String dirName = event.getItem().getName().trim();
                if(session.getCurrentDirectory().equals(ROOT)){
                    session.setCurrentDirectory(session.getCurrentDirectory()+dirName);
                } else {
                    session.setCurrentDirectory(session.getCurrentDirectory()+"/"+dirName);
                }

                session.getBreadCrumbs().add(new Breadcrumb(dirName, session.getCurrentDirectory(), session.getBreadCrumbs().size()));
                updateMenuBar();
                updateGrid();
            } else {
                add(new Paragraph("Aquí (FilesView.java) iría el código para abrir el archivo"));
            }
        });

        this.menuBar = new MenuBar();
        session.getBreadCrumbs().clear();
        session.getBreadCrumbs().add(new Breadcrumb(ROOT, session.getCurrentDirectory(), session.getBreadCrumbs().size()));
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
                if(index < breadCrumbsCurrent.size()){
                    SessionInfo.getInstance().setBreadCrumbs(new ArrayList<>(SessionInfo.getInstance().getBreadCrumbs().subList(0, index+1)));
                    updateMenuBar();
                    SessionInfo.getInstance().setCurrentDirectory(breadCrumb.getPathForDir());
                    updateGrid();
                }
            };
            this.menuBar.addItem(dirName, event);
        }
    }

    public void initGridContextMenu(){
        SessionInfo session = SessionInfo.getInstance();
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

        //GridMenuItem<FileSystemElement> copyMenu = gridContextMenu.addItem("Copy To");
        //GridSubMenu<FileSystemElement> copySubMenu = copyMenu.getSubMenu();
        //initSubMenus();
    }

    public void initSubMenus(){

    }
}
