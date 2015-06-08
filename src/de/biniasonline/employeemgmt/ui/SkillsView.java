package de.biniasonline.employeemgmt.ui;


import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

import de.biniasonline.employeemgmt.EmployeeManagementUI;
import de.biniasonline.employeemgmt.model.Skill;
import de.biniasonline.employeemgmt.service.StatisticService;
import de.biniasonline.employeemgmt.utils.DefaultButtonFactory;

@SuppressWarnings("serial")
public class SkillsView extends BasicView implements View  {

	public static final String VIEW_NAME = "skills";
	
	private transient JPAContainer<Skill> _container;

	public SkillsView() {
		_container = JPAContainerFactory.makeReadOnly(Skill.class, EmployeeManagementUI.PERSISTENCE_UNIT);
		initLayout();
	}
	
	private void initLayout() {
		getHeader().addComponent(new Label("Skills"));				
		
		final Table skillsTable = new Table();
		skillsTable.setSizeFull();		
		skillsTable.setContainerDataSource(_container);
		skillsTable.addGeneratedColumn("count", new ColumnGenerator() {		
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				EntityItem<Skill> skillItem = (EntityItem<Skill>) source.getItem(itemId);
				skillItem.refresh();
				return skillItem.getEntity().getEmployees().size();				
			}
		});
		skillsTable.addGeneratedColumn("knowledge", new ColumnGenerator() {		
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {				
				EntityItem<Skill> skillItem = (EntityItem<Skill>) source.getItem(itemId);
				StatisticService statisticService = ((EmployeeManagementUI)getUI()).getStatisticService();
				ProgressBar bar = new ProgressBar(statisticService.getKnowledge(skillItem.getEntity())/100);
				bar.setSizeFull();
				return bar;
			}
		});				
		skillsTable.setVisibleColumns("title", "count", "knowledge");
		skillsTable.setColumnHeaders("Title", "No. of employees", "Knowledge");
		skillsTable.setColumnAlignment("count", Align.RIGHT);
		skillsTable.setColumnExpandRatio("title", 5f);		
		skillsTable.setColumnExpandRatio("count", 2f);
		skillsTable.setColumnExpandRatio("knowledge", 3f);
				
		setContent(skillsTable);
		
		getFooter().addComponent(DefaultButtonFactory.createButton(DefaultButtonFactory.Action.EDIT, new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo(SkillsEditorView.VIEW_NAME);				
			}			
		}));
	}

	@Override
	public void enter(ViewChangeEvent event) {		
	}

}
