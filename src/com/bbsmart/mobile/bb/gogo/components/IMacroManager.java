package com.bbsmart.mobile.bb.gogo.components;

import com.bbsmart.mobile.bb.gogo.runtime.MacroRegistry;

public interface IMacroManager {

    public boolean isActive();
    public void setFlippable(boolean isFlippable);
    public void setModuleName(String moduleName);
	public void setRegistry(MacroRegistry registry);
	public void setTitle(String title);
	 
}
