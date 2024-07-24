package com.amostriker.home_cloud.views;

import com.amostriker.home_cloud.events.StackModifiedEvent;
import com.amostriker.home_cloud.services.FileService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileGrid extends Grid<File> {


    File file = new File("/");
    FileService fileService;

    @Autowired
     public FileGrid(FileService fileService){
        // setup
        this.fileService = fileService;

        // column
        setGridColumn(this);

        //add content
        refreshItemList();

        //appearance
        setAppearance(this);

    }

    private void setAppearance(Grid<File> grid) {
        setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private void setGridColumn(Grid<File> grid){
        grid.addColumn(new ComponentRenderer<HorizontalLayout, File>(file -> {
            boolean isFile = file.isFile();
            Icon icon = isFile ? VaadinIcon.FILE.create() : VaadinIcon.FOLDER.create();
            Button text = new Button(file.getName());
            if (isFile) {
                text.addClickListener(event -> {
                    // maybe download functionality
                    // maybe streaming functionality
                });
            } else {
                text.addClickListener(event -> {
                    // open folder directory
                    // fileService.open(file)
                    // refreshItems()
                    fileService.open(file);
                    refreshItemList();
                });
            }

            return new HorizontalLayout(icon, text);
        })).setHeader("Name").setAutoWidth(true);
        // service to get size
        grid.addColumn(file -> file.length() + " bytes").setHeader("Size");

    }


    public void refreshItemList(){
        File file1 = fileService.getFile();
        setItems(file1.listFiles());
    }
    @EventListener
    public void onStackModified(StackModifiedEvent event) {
        refreshItemList();
    }
}
