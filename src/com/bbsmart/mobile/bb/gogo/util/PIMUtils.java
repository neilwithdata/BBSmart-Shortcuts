package com.bbsmart.mobile.bb.gogo.util;

public class PIMUtils {
}

//System.out.println("field id = " + id);
//System.out.println("field label = " + c.getPIMList().getFieldLabel(id));
//System.out.println("countValues = " + c.countValues(id));
//if (c.getPIMList().getFieldDataType(id) == Contact.STRING) {
//  System.out.println("STRING");
//  for (int j = 0; j < c.countValues(id); j++) {
//      String value = c.getString(id, j);
//      System.out.println("Has " + c.getAttributes(id, j) + " attrs");
//      System.out.println(c.getPIMList().getFieldLabel(id) + "=" + value);
//      
//      int[] supportedAttrs = c.getPIMList().getSupportedAttributes(id);
//      for (int k = 0; k < supportedAttrs.length; k++) {
//          System.out.println("supported attr = " + c.getPIMList().getAttributeLabel(supportedAttrs[k]));
//      }
//  }
//}
//else if (c.getPIMList().getFieldDataType(id) == Contact.STRING_ARRAY) {
//  System.out.println("STRING_ARRAY values = " + c.countValues(id));
//  for (int j = 0; j < c.countValues(id); j++) {
//      int attrs = c.getAttributes(id, j);
//      System.out.println("YYY: " + attrs);
//      String[] value = c.getStringArray(id, j);
//      if (id == Contact.NAME) {
//          System.out.println("LASTNAME = " + value[Contact.NAME_FAMILY]);
//      }
//      for (int k = 0; k < value.length; k++) {
//          System.out.println("ZZZ: "
//                  + c.getPIMList().getFieldLabel(id)
//                  + "=" + value[k]);
//      }
//  }
//}
