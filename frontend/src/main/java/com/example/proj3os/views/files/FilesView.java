package com.example.proj3os.views.files;

import com.example.proj3os.controllers.DownloadLink;
import com.example.proj3os.controllers.FileController;
import com.example.proj3os.model.Breadcrumb;
import com.example.proj3os.model.File;
import com.example.proj3os.model.Directory;
import com.example.proj3os.model.FileSystemElement;
import com.example.proj3os.model.SessionInfo;
import com.example.proj3os.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.proj3os.helper.IConstants.*;


@PageTitle("Files")
@Route(value = "files", layout = MainLayout.class)
public class FilesView extends VerticalLayout {
    private final Grid<FileSystemElement> grid;
    private Grid<FileSystemElement> dialogGrid;
    private final MenuBar menuBar;
    private final Dialog dialog = new Dialog();

    MemoryBuffer memoryBuffer = new MemoryBuffer();
    Upload singleFileUpload = new Upload(memoryBuffer);
    DownloadLink downloadLink = new DownloadLink();


    public FilesView() {
        SessionInfo session = SessionInfo.getInstance();
        setSizeFull();
        this.menuBar = new MenuBar();
        this.grid = getGrid();

        setListeners(session);

        updateGrid(grid);

        dialog.add(createDialogueLayout(dialog));


        session.getBreadCrumbs().clear();
        session.getBreadCrumbs().add(new Breadcrumb(ROOT, session.getCurrentDirectory(), session.getBreadCrumbs().size()));
        updateMenuBar(grid, menuBar);

        HorizontalLayout horizontalLayout = new HorizontalLayout(singleFileUpload, createDownloadButton());

        add(menuBar, grid, horizontalLayout, dialog);
    }

    private Button createDownloadButton(){

        Button downloadButton = new Button(new Icon(VaadinIcon.DOWNLOAD_ALT));
        downloadButton.addClickListener(event ->{
            Set<FileSystemElement> selected = this.grid.asMultiSelect().getValue();
            System.out.println(selected.toString());
            for (FileSystemElement fileSystemElement : selected) {
                if (fileSystemElement.getType().equals(FILE)) {
                    try {
                        FileWriter myWriter = new FileWriter(fileSystemElement.getName(), false);
                        myWriter.write(((File) fileSystemElement).getContents());
                        myWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    } 
                    java.io.File file = new java.io.File(fileSystemElement.getName());
                    Anchor download = new Anchor(new DownloadLink().getStreamResource(file.getName(), file), "");
                    download.getElement().setAttribute("download", true);
                    download.removeAll();
                    Button dummyButton = new Button();
                    download.add(dummyButton);
                    download.setEnabled(true);
                    dummyButton.click();
                }

            }
            
        });

        return downloadButton;
    }

    public void updateGrid(Grid<FileSystemElement> grid){
        grid.setItems(FileController.getFiles(SessionInfo.getInstance().getUsername(), SessionInfo.getInstance().getCurrentDirectory()));
    }

    public void updateGridDirectoriesOnly(Grid<FileSystemElement> grid, String currentModalDirectory){
        List<FileSystemElement> fileSystemElements = FileController.getFiles(SessionInfo.getInstance().getUsername(), currentModalDirectory);
        grid.setItems(fileSystemElements.stream().filter(fileSystemElement -> fileSystemElement.getType().equals(DIRECTORY)).collect(Collectors.toList()));
    }

    public void updateMenuBar(Grid<FileSystemElement> grid, MenuBar menuBar){
        ArrayList<Breadcrumb> breadCrumbs = SessionInfo.getInstance().getBreadCrumbs();
        menuBar.removeAll();

        for (Breadcrumb breadCrumb : breadCrumbs) {
            String dirName = breadCrumb.getDirName();
            int index = breadCrumb.getIndex();
            ComponentEventListener<ClickEvent<MenuItem>> event = e -> {
                ArrayList<Breadcrumb> breadCrumbsCurrent = SessionInfo.getInstance().getBreadCrumbs();
                if(index < breadCrumbsCurrent.size()){
                    SessionInfo.getInstance().setBreadCrumbs(new ArrayList<>(SessionInfo.getInstance().getBreadCrumbs().subList(0, index+1)));
                    updateMenuBar(grid, menuBar);
                    SessionInfo.getInstance().setCurrentDirectory(breadCrumb.getPathForDir());
                    updateGrid(grid);
                }
            };
            menuBar.addItem(dirName, event);
        }
    }

    public void initGridContextMenu(GridContextMenu<FileSystemElement> gridContextMenu, Grid<FileSystemElement> grid){
        SessionInfo session = SessionInfo.getInstance();
        gridContextMenu.addItem("New File", event -> {
            if(FileController.createFile("New File", session.getUsername(), session.getCurrentDirectory())){
                Notification.show("File Created");
                updateGrid(grid);
            } else {
                Notification.show("Could not create file");
            }
        });

        gridContextMenu.addItem("New Folder", event -> {
            if(FileController.createDirectory("New Folder", session.getUsername(), session.getCurrentDirectory())){
                Notification.show("Folder Created");
                updateGrid(grid);
            } else {
                Notification.show("Could not create folder");
            }
        });

        gridContextMenu.addItem("Copy To", event -> {
            FileSystemElement fileSystemElement = event.getItem().orElse(null);
            assert fileSystemElement!=null;
            session.setFileToCopy(fileSystemElement);
            session.setCurrentModalDirectory(ROOT);
            updateGridDirectoriesOnly(dialogGrid, session.getCurrentModalDirectory());
            dialog.open();
        });
    }

    public VerticalLayout createDialogueLayout(Dialog dialog){
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        Button saveButton = new Button("Save", e -> {
            SessionInfo sessionInfo = SessionInfo.getInstance();
            FileController.copyFile(sessionInfo.getFileToCopy(),sessionInfo.getCurrentDirectory(), sessionInfo.getCurrentModalDirectory());
            dialog.close();
        });
        cancelButton.setEnabled(true);
        saveButton.setEnabled(true);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        SessionInfo session = SessionInfo.getInstance();
        Grid<FileSystemElement> grid = getGrid();
        this.dialogGrid = grid;
        updateGridDirectoriesOnly(grid, session.getCurrentModalDirectory());
        grid.addItemDoubleClickListener(event -> {
            if (event.getItem().getType().equals(DIRECTORY)) {
                String dirName = event.getItem().getName().trim();
                if(session.getCurrentModalDirectory().equals(ROOT)){
                    session.setCurrentModalDirectory(session.getCurrentModalDirectory()+dirName);
                } else {
                    session.setCurrentModalDirectory(session.getCurrentModalDirectory()+"/"+dirName);
                }
                updateGridDirectoriesOnly(grid, session.getCurrentModalDirectory());
            }
        });

        VerticalLayout dialogLayout = new VerticalLayout(grid, buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "700px").set("max-width", "100%");
        return dialogLayout;
    }

    public Grid<FileSystemElement> getGrid(){
        Grid<FileSystemElement> grid = new Grid<>(FileSystemElement.class, false);
        GridContextMenu<FileSystemElement> gridContextMenu = grid.addContextMenu();
        initGridContextMenu(gridContextMenu, grid);
        grid.setSelectionMode(SelectionMode.MULTI);

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

        return grid;
    }

    private void setListeners(SessionInfo session){

        this.grid.addItemDoubleClickListener(event -> {
            if (event.getItem().getType().equals(DIRECTORY)) {
                String dirName = event.getItem().getName().trim();
                if(session.getCurrentDirectory().equals(ROOT)){
                    session.setCurrentDirectory(session.getCurrentDirectory()+dirName);
                } else {
                    session.setCurrentDirectory(session.getCurrentDirectory()+"/"+dirName);
                }

                session.getBreadCrumbs().add(new Breadcrumb(dirName, session.getCurrentDirectory(), session.getBreadCrumbs().size()));
                updateMenuBar(grid, this.menuBar);
                updateGrid(grid);
            } else {
                add(new Paragraph("Aquí (FilesView.java) iría el código para abrir el archivo"));
            }
        });
        
        singleFileUpload.addSucceededListener(event -> {
            // Get information about the uploaded file
            InputStream fileData = memoryBuffer.getInputStream();
            String fileName = event.getFileName();
            long contentLength = event.getContentLength();
            String mimeType = event.getMIMEType();

            // Do something with the file data
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                String text = new String(fileData.readAllBytes(), StandardCharsets.UTF_8);

                String[] file = fileName.split("\\.");
                assert file.length <= 2;
                fileName = file[0];
                String fileExtension = file[1];

                FileController.createFile(fileName,
                        session.getUsername(),
                        session.getCurrentDirectory(),
                        formatter.format(date),
                        formatter.format(date),
                        fileExtension,
                        String.valueOf(contentLength),
                        text);
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateGrid(grid);
            // processFile(fileData, fileName, contentLength, mimeType);
        });
    }
}
