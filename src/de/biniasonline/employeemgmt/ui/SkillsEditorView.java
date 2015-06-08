package de.biniasonline.employeemgmt.ui;

import java.util.Set;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.biniasonline.employeemgmt.EmployeeManagementUI;
import de.biniasonline.employeemgmt.model.Skill;
import de.biniasonline.employeemgmt.utils.DefaultButtonFactory;

@SuppressWarnings("serial")
public class SkillsEditorView extends BasicView implements View, DataModificationView  {

	public static final String VIEW_NAME = "skills/edit";
	
	private transient JPAContainer<Skill> _container;

	public SkillsEditorView() {
		// create a batchable container - we want to be able to discard all changes in the skills table
		_container = JPAContainerFactory.makeBatchable(Skill.class, EmployeeManagementUI.PERSISTENCE_UNIT);
		_container.setAutoCommit(false);
		initLayout();
	}
	
	private void initLayout() {
		// init header
		getHeader().addComponent(new Label("Skills"));				
		
		// init editable table
		final Table skillsTable = new Table();		
		skillsTable.setEditable(true);
		skillsTable.setSizeFull();
		skillsTable.setContainerDataSource(_container);
		skillsTable.addGeneratedColumn("count", new ColumnGenerator() {		
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				EntityItem<Skill> skillItem = (EntityItem<Skill>) source.getItem(itemId);
				return skillItem.getEntity().getEmployees().size();
			}
		});
		skillsTable.addGeneratedColumn("action", new ColumnGenerator() {		
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				Button btn = DefaultButtonFactory.createButton(DefaultButtonFactory.Action.DELETE, new Button.ClickListener() {					
					@Override
					public void buttonClick(ClickEvent event) {
						_container.removeItem(itemId);						
					}
				});
				btn.setCaption("");
				EntityItem<Skill> skillItem = (EntityItem<Skill>) source.getItem(itemId);
				if (skillItem.getEntity().getEmployees().size() > 0) {
					btn.setEnabled(false);
				}
				return btn;
			}
		});
		skillsTable.setVisibleColumns("title","count", "action");
		skillsTable.setColumnHeaders("Title", "No. of employees", "Delete");
		skillsTable.setColumnAlignment("count", Align.RIGHT);
		skillsTable.setColumnAlignment("action", Align.RIGHT);
		skillsTable.setColumnExpandRatio("title", 6f);		
		skillsTable.setColumnExpandRatio("count", 2f);
		skillsTable.setColumnExpandRatio("action", 2f);
		
		// init control buttons
		Button btnAdd = new Button("+", new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				Skill skill = new Skill();
				skill.setTitle("<new skill>");
				_container.addEntity(skill);
			}
		});
		btnAdd.setWidth(50, Unit.PIXELS);
		btnAdd.setHeight(50, Unit.PIXELS);
		
		// controls layout
		VerticalLayout controls = new VerticalLayout();
		controls.setWidth(50, Unit.PIXELS);
		controls.setSpacing(true);
		controls.addComponent(btnAdd);
		
		// main view layout
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		layout.setSpacing(true);
		
		layout.addComponent(skillsTable);						
		layout.addComponent(controls);
		layout.setExpandRatio(skillsTable, 1);
				
		setContent(layout);
		
		// init footer
		getFooter().addComponent(DefaultButtonFactory.createButton(DefaultButtonFactory.Action.SAVE, new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				_container.commit();
				getUI().getNavigator().navigateTo(SkillsView.VIEW_NAME);
			}
		}));
		
		getFooter().addComponent(DefaultButtonFactory.createButton(DefaultButtonFactory.Action.CANCEL, new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo(SkillsView.VIEW_NAME);				
			}
		}));		
	}

	@Override
	public void enter(ViewChangeEvent event) {		
	}

	@Override
	public boolean isDirty() {
		return _container.isModified();
	}
	
	@Override
	public void discard() {
		_container.discard();
	}

}
