package de.biniasonline.employeemgmt.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;


@SuppressWarnings("serial")
public abstract class BasicCRUDView<IDType> extends BasicView implements View, DataModificationView {

	public static final String MODE_CREATE = "create";
	public static final String MODE_READ = "read";
	public static final String MODE_UPDATE = "update";
	public static final String MODE_DELETE = "delete";

	public BasicCRUDView() {	
	}
	
	protected abstract void renderCreate();
	protected abstract void renderRead(IDType entityId);	
	protected abstract void renderUpdate(IDType entityId);
	protected abstract void renderDelete(IDType entityId);
	
	protected abstract IDType parseEntityId(String pathFragment);

	@Override
	public void enter(ViewChangeEvent event) {	
		String[] path = event.getParameters().split("/");				
		String command = path[0];
		
		IDType entityId = null;
		if (path.length > 1) {
			entityId = parseEntityId(path[1]);
		}
		
		getHeader().removeAllComponents();
		getFooter().removeAllComponents();
		
		if (MODE_READ.equals(command)) {			
			renderRead(entityId);
		} else if (MODE_CREATE.equals(command)) {
			renderCreate();
		} else if (MODE_UPDATE.equals(command)) {
			renderUpdate(entityId);
		} else if (MODE_DELETE.equals(command)) {
			renderDelete(entityId);
		}
	}
}
