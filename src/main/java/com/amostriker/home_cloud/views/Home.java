package com.amostriker.home_cloud.views;


import com.amostriker.home_cloud.services.FileService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

@Route("/")
public class Home extends VerticalLayout {
    //horizontal layout -> search field, download button
    //navigation Div -> directory Span
    //grid
    Grid<File> fileGrid;
    Header header;
    Navigation navigation;

    @Autowired
    public Home(Grid<File> fileGrid, Header header, Navigation navigation){
        this.fileGrid = fileGrid;
        this.header = header;
        this.navigation = navigation;

        add(header);
        add(navigation);
        add(fileGrid);


        setAppearance(this);
    }

    private void setAppearance(VerticalLayout layout) {
        layout.setHeightFull();
    }

}