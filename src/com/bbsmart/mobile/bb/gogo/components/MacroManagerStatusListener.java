package com.bbsmart.mobile.bb.gogo.components;

public interface MacroManagerStatusListener {

    void onMacroAdded(MacroManager manager);
    void onMacroRemoved(MacroManager manager);
    void onMacroManagerEmpty(MacroManager manager);

}
