package com.amostriker.home_cloud.services;

import com.amostriker.home_cloud.events.NavigationModifiedEvent;
import com.amostriker.home_cloud.events.StackModifiedEvent;
import com.amostriker.home_cloud.views.FileGrid;
import com.vaadin.flow.component.grid.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Set;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {
    private Stack<File> stack;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    private FileService(ApplicationEventPublisher eventPublisher){
        this.eventPublisher = eventPublisher;
        stack = new Stack<>();
        stack.add(new File("/home"));
    }

    public File getFile() {
        return stack.peek();
    }

    private void toZip(ZipOutputStream zipOutputStream, File file, String parentDirectory) throws Exception {


        if(file.isFile()){
            zipOutputStream.putNextEntry(new ZipEntry(parentDirectory+ file.getName()));
            int length;
            byte[] buffer = new byte[1024];
            FileInputStream fileInputStream = new FileInputStream(file);
            while((length = fileInputStream.read(buffer))!=-1){
                zipOutputStream.write(buffer, 0, length);
            }
            zipOutputStream.closeEntry();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for(var item : files){
                toZip(zipOutputStream, item, parentDirectory+file.getName()+"/");
            }
        }

    }

    public void processZip(OutputStream stream, Set<File> selectionSet) {

        ZipOutputStream zipOutputStream = new ZipOutputStream(stream);
        for(File file : selectionSet){
            try {
                toZip(zipOutputStream, file, "");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(zipOutputStream!=null){
            try {
                zipOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



    }

    public void open(File file) {
        stack.push(file);
        eventPublisher.publishEvent(new NavigationModifiedEvent());
    }

    public Collection<File> getCollection() {
        return (Collection<File>) stack;
    }

    public void traverseBack(File file) {
        while(!stack.isEmpty() && !stack.peek().equals(file)){
            stack.pop();
        }
        eventPublisher.publishEvent(new StackModifiedEvent());
        eventPublisher.publishEvent(new NavigationModifiedEvent());
    }
}
