package de.biniasonline.employeemgmt.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

import de.biniasonline.employeemgmt.EmployeeManagementUI;
import de.biniasonline.employeemgmt.model.Employee;
import de.biniasonline.employeemgmt.utils.DefaultButtonFactory;


@SuppressWarnings("serial")
public class EmployeesView extends BasicView implements View {

	public static final String VIEW_NAME = "employees";

	private transient JPAContainer<Employee> _container;

	public EmployeesView() {						
		_container = JPAContainerFactory.make(Employee.class, EmployeeManagementUI.PERSISTENCE_UNIT);		
		initLayout();
	}


	private void initLayout() {		
		getHeader().addComponent(new Label("Employees"));

		Table table = new Table();
		table.setSizeFull();
		table.setContainerDataSource(_container);		
		table.addGeneratedColumn("details", new ColumnGenerator() {			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button btnDetails = new Button(new ThemeResource("icons/btn_details.png"));
				btnDetails.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						getUI().getNavigator().navigateTo(EmployeeView.VIEW_NAME + "/" + EmployeeView.MODE_READ + "/" + itemId);
						
					}
				});
				return btnDetails; 									
			}
		});				
		table.setVisibleColumns("lastname", "firstname", "birthday", "details");
		table.setColumnHeaders("Lastname", "Firstname", "Birthday", "Details");		
		table.setConverter("birthday", new StringToDateConverter() {
			@Override
			protected DateFormat getFormat(Locale locale) {
				return SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, locale);
			}			
		});		
		
		setContent(table);

		getFooter().addComponent(DefaultButtonFactory.createButton(DefaultButtonFactory.Action.ADD, new ClickListener() {		
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo(EmployeeView.VIEW_NAME + "/" + EmployeeView.MODE_CREATE);			
			}
		}));
				
	}	
	

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
