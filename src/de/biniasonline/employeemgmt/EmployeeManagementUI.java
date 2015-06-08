package de.biniasonline.employeemgmt;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.biniasonline.employeemgmt.service.StatisticService;
import de.biniasonline.employeemgmt.ui.DataModificationView;
import de.biniasonline.employeemgmt.ui.EmployeeView;
import de.biniasonline.employeemgmt.ui.EmployeesView;
import de.biniasonline.employeemgmt.ui.SkillsEditorView;
import de.biniasonline.employeemgmt.ui.SkillsView;
import de.biniasonline.employeemgmt.ui.WelcomeView;

@SuppressWarnings("serial")
@Theme("default")
public class EmployeeManagementUI extends UI implements ViewChangeListener {

	public static final String PERSISTENCE_UNIT = "EmployeeManagement";

	private CssLayout _content;

	private transient StatisticService _statisticService;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = EmployeeManagementUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit(PERSISTENCE_UNIT);
		
		_statisticService = new StatisticService(em);
		
		initLayout();

		Navigator navigator = new Navigator(this, _content);
		navigator.addView(WelcomeView.VIEW_NAME, new WelcomeView());

		// views with databinding should be discarded on each navigation state
		// change
		navigator.addView(EmployeesView.VIEW_NAME, EmployeesView.class);
		navigator.addView(EmployeeView.VIEW_NAME, EmployeeView.class);
		navigator.addView(SkillsView.VIEW_NAME, SkillsView.class);
		navigator.addView(SkillsEditorView.VIEW_NAME, SkillsEditorView.class);
		
		navigator.addViewChangeListener(this);
	}


	private void initLayout() {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		setContent(mainLayout);

		// init header
		HorizontalLayout header = new HorizontalLayout();
		header.setWidth("100%");
		header.addStyleName("header");

		Label headline = new Label("Employee Management");
		headline.addStyleName("headline");
		header.addComponent(headline);
		header.setExpandRatio(headline, 1);
		header.setComponentAlignment(headline, Alignment.MIDDLE_LEFT);

		ComboBox themePicker = new ComboBox("Theme");
		themePicker.addItems(Arrays.asList("default", "dark"));
		themePicker.setNullSelectionAllowed(false);
		themePicker.setNewItemsAllowed(false);
		themePicker.setValue(getTheme());

		themePicker.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				String theme = (String) event.getProperty().getValue();
				setTheme(theme);
			}
		});
		header.addComponent(themePicker);
		header.setComponentAlignment(themePicker, Alignment.MIDDLE_RIGHT);

		mainLayout.addComponent(header);

		// init content and nav
		HorizontalLayout contentAndNav = new HorizontalLayout();
		contentAndNav.setSizeFull();
		contentAndNav.addStyleName("content-and-nav");
		mainLayout.addComponent(contentAndNav);
		mainLayout.setExpandRatio(contentAndNav, 1); // expand to the remaining space														

		CssLayout nav = new CssLayout();
		nav.addStyleName("nav");
		nav.setSizeUndefined(); // done via CSS

		_content = new CssLayout();
		_content.addStyleName("content");
		_content.setSizeFull();

		contentAndNav.addComponent(nav);
		contentAndNav.addComponent(_content);
		contentAndNav.setExpandRatio(_content, 1);

		HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName("footer");
		footer.setSizeUndefined();
		mainLayout.addComponent(footer);

		Button navItem = new Button("Employees", new ThemeResource(
				"icons/people.png"));
		navItem.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getNavigator().navigateTo(EmployeesView.VIEW_NAME);
			}
		});
		navItem.addStyleName("nav-item");
		nav.addComponent(navItem);

		navItem = new Button("Skills", new ThemeResource("icons/skills.png"));
		navItem.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getNavigator().navigateTo(SkillsView.VIEW_NAME);
			}
		});
		navItem.addStyleName("nav-item");
		nav.addComponent(navItem);
	}
	

	public StatisticService getStatisticService() {
		return _statisticService;
	}


	@Override
	public boolean beforeViewChange(final ViewChangeEvent event) {
		if (event.getOldView() instanceof DataModificationView) {
			final DataModificationView dataModificationView = (DataModificationView)event.getOldView();
			if (dataModificationView.isDirty()) {				
				ConfirmDialog.show(this, "Discard changes?", new ConfirmDialog.Listener() {
					@Override
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							dataModificationView.discard();
							event.getNavigator().navigateTo(event.getViewName() + "/" + event.getParameters());
						}
					}				
				});
				return false;
			}
		}
		return true;
	}


	@Override
	public void afterViewChange(ViewChangeEvent event) {
		_content.setId("C_VIEW." + event.getViewName());
	}
}