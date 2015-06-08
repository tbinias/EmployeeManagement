package de.biniasonline.employeemgmt.ui;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TwinColSelect;

import de.biniasonline.employeemgmt.EmployeeManagementUI;
import de.biniasonline.employeemgmt.databinding.MultiSelectConverterFix;
import de.biniasonline.employeemgmt.model.Employee;
import de.biniasonline.employeemgmt.model.Skill;


@SuppressWarnings("serial")
public class EmployeeEditor extends CustomComponent {

	private transient FieldGroup _fieldGroup;
	private transient EntityItem<Employee> _item;


	public EmployeeEditor(EntityItem<Employee> item) {
		_item = item;
		
		FormLayout layout = new FormLayout();
		
		// use a generic FieldGroup but add a BeanValidator for each field
		_fieldGroup = new FieldGroup(item) {
			@Override
			protected void configureField(Field<?> field) {
				super.configureField(field);
				field.addValidator(new BeanValidator(Employee.class, getPropertyId(field).toString()));
			}
		};

		layout.addComponent(_fieldGroup.buildAndBind("Firstname", "firstname"));
		layout.addComponent(_fieldGroup.buildAndBind("Lastname", "lastname"));
		layout.addComponent(_fieldGroup.buildAndBind("Birthday", "birthday"));
		
		
		JPAContainer<Skill> skills = JPAContainerFactory.makeReadOnly(Skill.class, EmployeeManagementUI.PERSISTENCE_UNIT);		
		TwinColSelect multiSkillsSelect = new TwinColSelect("Skills", skills);
		multiSkillsSelect.setItemCaptionPropertyId("title");
		multiSkillsSelect.setConverter(new MultiSelectConverterFix(multiSkillsSelect));
		_fieldGroup.bind(multiSkillsSelect, "skills");
		layout.addComponent(multiSkillsSelect);
		
		setCompositionRoot(layout);	
	}
	
	public void commit() throws CommitException {
		_fieldGroup.commit();
	}
	
	public void setValidationVisible(boolean visible) {
		for (Field<?> field : _fieldGroup.getFields()) {
			if (field instanceof AbstractField<?>) {
				((AbstractField<?>)field).setValidationVisible(visible);
			}
		}
	}

	public boolean isModified() {
		return _fieldGroup.isModified();
	}
	
	public void discard() {
		_fieldGroup.discard();
	}

	public EntityItem<Employee> getItem() {
		return _item;
	}
	
}
