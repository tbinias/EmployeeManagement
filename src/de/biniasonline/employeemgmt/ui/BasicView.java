package de.biniasonline.employeemgmt.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class BasicView extends CustomComponent {
	
	private HorizontalLayout _header;
	private Panel _content;
	private HorizontalLayout _footer;

	public BasicView() {
		VerticalLayout layout = new VerticalLayout();
		_header = new HorizontalLayout();
		_header.addStyleName("view-header");
		layout.addComponent(_header);
		

		_content = new Panel();
		_content.setSizeFull();
		_content.addStyleName("view-content");
		layout.addComponent(_content);		
		
		_footer = new HorizontalLayout();
		_footer.addStyleName("view-footer");
		_footer.setSpacing(true);
		layout.addComponent(_footer);
		
		layout.setSizeFull();
		layout.setExpandRatio(_content, 1); // content should expand to remaining space

		setCompositionRoot(layout);
		setSizeFull();
	}

	public HorizontalLayout getHeader() {
		return _header;
	}

	public HorizontalLayout getFooter() {
		return _footer;
	}

	public void setContent(Component content) {
		_content.setContent(content);
	}

}
