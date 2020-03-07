package com.bbsmart.mobile.bb.gogo.runtime;

import java.util.Vector;

import com.bbsmart.mobile.bb.gogo.model.CodeModule;

import net.rim.device.api.collection.util.SortedReadableList;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.util.Comparator;

public class ToolbarListFieldCallback implements ListFieldCallback {

    private static class ShortcutComparator implements Comparator {
        public int compare(Object arg0, Object arg1) {
            CodeModule codeModule1 = (CodeModule) arg0;
            CodeModule codeModule2 = (CodeModule) arg1;
            
            String name1 = codeModule1.getAppName();
            String name2 = codeModule2.getAppName();

            return name1.compareTo(name2);
        }
    }

    private Vector items = new Vector();
    private SortedReadableList sortedItems = new SortedReadableList(new ShortcutComparator());
    
    public void drawListRow(ListField listField, Graphics graphics, int index, int y, int width) {
        CodeModule codeModule = (CodeModule) sortedItems.getAt(index);
        Bitmap icon = codeModule.getIcon();
        
        int xOffset = 2;
        int yOffsetIcon = y + (listField.getRowHeight() - icon.getHeight()) / 2;
        int yOffsetText = y + (listField.getRowHeight() - Font.getDefault().getHeight()) / 2;

        graphics.drawBitmap(xOffset, yOffsetIcon, icon.getWidth(), icon.getHeight(), icon, 0, 0);
        
        xOffset += icon.getWidth() + 4;
        
        String name = codeModule.getAppName();
        graphics.drawText(name, xOffset, yOffsetText, 0, width);
    }

    public Object get(ListField listField, int index) {
        CodeModule codeModule = (CodeModule) sortedItems.getAt(index);

        return codeModule;
    }

    public int getPreferredWidth(ListField listField) {
        return Display.getWidth();
    }

    public int indexOfList(ListField listField, String prefix, int start) {
        int j = sortedItems.size();

        for (int i = start; i < j; i++) {
            CodeModule codeModule = (CodeModule) sortedItems.getAt(i);

            if (codeModule.getAppName().toLowerCase().startsWith(prefix)) {
                return i;
            }
        }
        
        // Wrap it around...
        for (int i = 0; i < start; i++) {
            CodeModule codeModule = (CodeModule) sortedItems.getAt(i);

            if (codeModule.getAppName().toLowerCase().startsWith(prefix)) {
                return i;
            }
        }

        return listField.getSelectedIndex();
    }

    public void insert(int i, CodeModule codeModule) {
        items.insertElementAt(codeModule, i);
    }
    
    public void sort() {
        sortedItems.loadFrom(items.elements());
    }
}
