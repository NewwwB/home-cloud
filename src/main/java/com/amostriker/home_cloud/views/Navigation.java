package com.amostriker.home_cloud.views;

import com.amostriker.home_cloud.events.NavigationModifiedEvent;
import com.amostriker.home_cloud.services.FileService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;

@Component
public class Navigation extends Div {
    FileService fileService;
    // add directory button in stack
    // service.get(fileStack);
    @Autowired
    public Navigation(FileService fileService){
        this.fileService = fileService;
        refreshNavigation();
    }

    private void refreshNavigation() {
        removeAll();
        Collection<File> fileCollection = fileService.getCollection();
        for(var item : fileCollection){
            add(new Span("/"));
            add(new Button(item.getName(), new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> event) {
                    fileService.traverseBack(item);
                }
            }));
        }
    }
    @EventListener
    public void onNavigationModified(NavigationModifiedEvent event){
        refreshNavigation();
    }
}
