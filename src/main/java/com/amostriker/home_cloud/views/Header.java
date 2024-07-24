package com.amostriker.home_cloud.views;

import com.amostriker.home_cloud.services.FileService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.StreamResourceWriter;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class Header extends HorizontalLayout {

    private TextField searchField;
    private Button downloadButton;
    private Anchor anchor;
    private Grid<File> fileGrid; // Assuming File is a custom class representing files
    private FileService fileService;

    @Autowired
    public Header(Grid<File> fileGrid, FileService fileService) {
        this.fileGrid = fileGrid;
        this.fileService = fileService;
        // Initialize components
        initComponent();

        // setup appearance
        setAppearance(this);

        // Add components to the layout
        addComponent(this);

        // Anchor and Download Button Setup
        setAnchorAndDownloadButton();

        // Field Setup
        setSearchField();
    }

    private void setSearchField() {
        searchField.setWidth("50%");
    }

    private void setAnchorAndDownloadButton() {
        buttonImpl();
    }
    private void buttonImpl(){
        downloadButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
                LocalDateTime now = LocalDateTime.now();
                String zipFileName = "files_" + dtf.format(now) + ".zip";
                anchor.setHref(new StreamResource(zipFileName, new StreamResourceWriter() {
                    @Override
                    public void accept(OutputStream stream, VaadinSession session) throws IOException {
                        fileService.processZip(stream, fileGrid.getSelectedItems());
                    }
                }));
                anchor.getElement().setAttribute("download", true);
                anchor.getElement().callJsFunction("click");
            }
        });
    }

    private void addComponent(HorizontalLayout header) {
        header.add(searchField,downloadButton, anchor);
    }

    private void setAppearance(HorizontalLayout header) {
        header.setWidthFull();
        header.setAlignItems(Alignment.END);
    }

    private void initComponent() {
        searchField = new TextField("Search");
        downloadButton = new Button("Download");
        anchor = new Anchor();
    }
}
