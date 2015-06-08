package de.biniasonline.employeemgmt.utils;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;

public class DefaultButtonFactory {
	
	public static enum Action {
		SAVE, ADD, EDIT, DELETE, CANCEL
	}
	
	public static final Map<Enum<Action>,Resource> ICONS_BY_ACTION = new HashMap<Enum<Action>,Resource>();
	static {
		ICONS_BY_ACTION.put(Action.SAVE, new ThemeResource("icons/btn_save.png"));
		ICONS_BY_ACTION.put(Action.ADD, new ThemeResource("icons/btn_add.png"));
		ICONS_BY_ACTION.put(Action.EDIT, new ThemeResource("icons/btn_edit.png"));
		ICONS_BY_ACTION.put(Action.DELETE, new ThemeResource("icons/btn_delete.png"));
		ICONS_BY_ACTION.put(Action.CANCEL, new ThemeResource("icons/btn_cancel.png"));
	}
	
	public static final Map<Enum<Action>,String> CAPTIONS_BY_ACTION = new HashMap<Enum<Action>,String>();
	static {
		CAPTIONS_BY_ACTION.put(Action.SAVE, "save");
		CAPTIONS_BY_ACTION.put(Action.ADD, "add");
		CAPTIONS_BY_ACTION.put(Action.EDIT, "edit");
		CAPTIONS_BY_ACTION.put(Action.DELETE, "delete");
		CAPTIONS_BY_ACTION.put(Action.CANCEL, "cancel");
	}
	
	public static Button createButton(Enum<Action> action) {
		return createButton(action, null);
	}
	
	public static Button createButton(Enum<Action> action, Button.ClickListener clickListener) {
		Button button = new Button(CAPTIONS_BY_ACTION.get(action), ICONS_BY_ACTION.get(action));
		if (clickListener != null) {
			button.addClickListener(clickListener);
		}
		return button;
	}

}
