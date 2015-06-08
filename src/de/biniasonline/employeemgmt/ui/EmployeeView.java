package de.biniasonline.employeemgmt.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

import de.biniasonline.employeemgmt.EmployeeManagementUI;
import de.biniasonline.employeemgmt.model.Employee;
import de.biniasonline.employeemgmt.model.Skill;
import de.biniasonline.employeemgmt.utils.DefaultButtonFactory;


@SuppressWarnings("serial")
public class EmployeeView extends BasicCRUDView<Long> {

	private transient static final Logger LOG = Logger.getLogger(EmployeesView.class.getName());
	
	public static final String VIEW_NAME = "employee";

	private transient JPAContainer<Employee> _container;

	private transient EmployeeEditor _editor;

	public EmployeeView() {								
		_container = JPAContainerFactory.make(Employee.class, EmployeeManagementUI.PERSISTENCE_UNIT);
	}

	@Override
	protected void renderCreate() {
		getHeader().addComponent(new Label("Create Employee"));
		
		// find item to edit or create new one
		Employee employee = new Employee();
		employee.setFirstname("");
		employee.setLastname("");
		EntityItem<Employee> item = _container.createEntityItem(employee);

		// init editor
		_editor = new EmployeeEditor(item);
		_editor.setValidationVisible(false); // we will display validation errors after a commit failure
		setContent(_editor);

		getFooter().addComponent(DefaultButtonFactory.createButton(DefaultButtonFactory.Action.SAVE, new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				performSave();
			}
		}));
		getFooter().addComponent(DefaultButtonFactory.createButton(DefaultButtonFactory.Action.CANCEL, new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {					
				performCancel();
			}
		}));
		
	}

	protected void performSave() {
		try {
			_editor.commit();
			Object itemId = _editor.getItem().getItemId();
			if (!_editor.getItem().isPersistent()) {
				itemId = _container.addEntity(_editor.getItem().getEntity());
			}
			getUI().getNavigator().navigateTo(VIEW_NAME + "/" + MODE_READ + "/" + itemId);
		} catch (CommitException e) {
			if (e.getInvalidFields().size() > 0) {
				_editor.setValidationVisible(true);	
			} else {
				LOG.log(Level.WARNING, "Commit failed", e);
				Notification.show("Commit failed.", Notification.Type.ERROR_MESSAGE);
			}
		}	
	}
	
	protected void performCancel() {
		if (_editor.getItem().isPersistent()) {
			// mode update
			getUI().getNavigator().navigateTo(VIEW_NAME + "/" + MODE_READ + "/" + _editor.getItem().getItemId());
		} else {
			// mode create 
			getUI().getNavigator().navigateTo(EmployeesView.VIEW_NAME);
		}
	}	

	protected void performDelete(final Long entityId) {
		ConfirmDialog.show(UI.getCurrent(), "Delete employee?", new ConfirmDialog.Listener() {

			@Override
			public void onClose(ConfirmDialog dialog) {
				if (dialog.isConfirmed()) {
					_container.removeItem(entityId);
					getUI().getNavigator().navigateTo(EmployeesView.VIEW_NAME);
				}						
			}
			
		});
	}

	@Override
	protected void renderRead(final Long entityId) {						
		// find item 
		EntityItem<Employee> item = _container.getItem(entityId);
		
		// init header
		getHeader().addComponent(new Label("Employee - " + item.getEntity().getFirstname() + " " + item.getEntity().getLastname()));
								
		// build form
		Label firstname = new Label();
		firstname.setCaption("Firstname:");
		firstname.setPropertyDataSource(item.getItemProperty("firstname"));
		
		Label lastname = new Label();
		lastname.setCaption("Lastname:");
		lastname.setPropertyDataSource(item.getItemProperty("lastname"));
		
		Label birthday = new Label();
		birthday.setCaption("Birthday:");
		birthday.setPropertyDataSource(item.getItemProperty("birthday"));
		birthday.setConverter(new StringToDateConverter() {
			@Override
			protected DateFormat getFormat(Locale locale) {
				return SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, locale);
			}			
		});
		
		BeanItemContainer<Skill> skills = new BeanItemContainer<Skill>(Skill.class, item.getEntity().getSkills());
		
		Table skillsTable = new Table("Skills:", skills);
		skillsTable.setVisibleColumns("title");
		skillsTable.setColumnHeader("title", "Skill");
		skillsTable.setHeight(200, Unit.PIXELS);
		skillsTable.setWidth("100%");

		FormLayout form = new FormLayout();
		form.addComponent(firstname);
		form.addComponent(lastname);
		form.addComponent(birthday);
		form.addComponent(skillsTable);
								
		setContent(form);

		// init footer
		getFooter().addComponent(DefaultButtonFactory.createButton(DefaultButtonFactory.Action.EDIT, new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo(VIEW_NAME + "/" + MODE_UPDATE + "/" + entityId);
			}
		}));
		
		getFooter().addComponent(DefaultButtonFactory.createButton(DefaultButtonFactory.Action.DELETE, new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				performDelete(entityId);
			}
		}));		
	}

	@Override
	protected void renderUpdate(final Long entityId) {
		getHeader().addComponent(new Label("Edit Employee"));
		// find item to edit or create new one
		EntityItem<Employee> item = null;
		item = _container.getItem(entityId);
		
		// init editor
		_editor = new EmployeeEditor(item);
		setContent(_editor);
	
		getFooter().addComponent(DefaultButtonFactory.createButton(DefaultButtonFactory.Action.SAVE, new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				performSave();
			}
		}));
		getFooter().addComponent(DefaultButtonFactory.createButton(DefaultButtonFactory.Action.CANCEL, new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				performCancel();
			}
		}));			
			
	}

	@Override
	protected void renderDelete(Long entityId) {		
	}

	@Override
	protected Long parseEntityId(String pathFragment) {
		if (pathFragment != null) {
			return Long.parseLong(pathFragment);
		}
		return null;
	}

	@Override
	public boolean isDirty() {
		if (_editor != null) {
			return _editor.isModified() || _container.isModified();
		}
		return _container.isModified();
	}
	
	@Override
	public void discard() {
		if (_editor != null) {
			_editor.discard();
		}
		_container.discard();
	}
}
