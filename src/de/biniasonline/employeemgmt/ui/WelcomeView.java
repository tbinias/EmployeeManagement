package de.biniasonline.employeemgmt.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class WelcomeView extends VerticalLayout implements View {
	
	public static final String VIEW_NAME = "";
	
	public static final String ID_LABEL_WELCOME = WelcomeView.class.getName() + ".WELCOME";

	public WelcomeView() {
		setMargin(true);
		Label lbl = new Label("<h1>Welcome</h1>", ContentMode.HTML);
		lbl.setId(ID_LABEL_WELCOME);
		addComponent(lbl);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}

}
