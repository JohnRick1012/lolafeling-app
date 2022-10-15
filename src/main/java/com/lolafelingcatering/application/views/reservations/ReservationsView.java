package com.lolafelingcatering.application.views.reservations;

import com.lolafelingcatering.application.data.entity.Reservations;
import com.lolafelingcatering.application.data.service.ReservationsService;
import com.lolafelingcatering.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Reservations")
@Route(value = "reservations/:reservationsID?/:action?(edit)", layout = MainLayout.class)
public class ReservationsView extends Div implements BeforeEnterObserver {

    private final String RESERVATIONS_ID = "reservationsID";
    private final String RESERVATIONS_EDIT_ROUTE_TEMPLATE = "reservations/%s/edit";

    private final Grid<Reservations> grid = new Grid<>(Reservations.class, false);

    private TextField firstName;
    private TextField lastName;
    private TextField phoneNo;
    private DateTimePicker preferedSchedule;
    private TextField email;
    private TextField address;
    private TextField package_;
    private TextField paymentStatus;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Reservations> binder;

    private Reservations reservations;

    private final ReservationsService reservationsService;

    @Autowired
    public ReservationsView(ReservationsService reservationsService) {
        this.reservationsService = reservationsService;
        addClassNames("reservations-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("phoneNo").setAutoWidth(true);
        grid.addColumn("preferedSchedule").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("address").setAutoWidth(true);
        grid.addColumn("package_").setAutoWidth(true);
        grid.addColumn("paymentStatus").setAutoWidth(true);
        grid.setItems(query -> reservationsService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(RESERVATIONS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ReservationsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Reservations.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.reservations == null) {
                    this.reservations = new Reservations();
                }
                binder.writeBean(this.reservations);
                reservationsService.update(this.reservations);
                clearForm();
                refreshGrid();
                Notification.show("Reservations details stored.");
                UI.getCurrent().navigate(ReservationsView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the reservations details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> reservationsId = event.getRouteParameters().get(RESERVATIONS_ID).map(UUID::fromString);
        if (reservationsId.isPresent()) {
            Optional<Reservations> reservationsFromBackend = reservationsService.get(reservationsId.get());
            if (reservationsFromBackend.isPresent()) {
                populateForm(reservationsFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested reservations was not found, ID = %s", reservationsId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ReservationsView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        phoneNo = new TextField("Phone No");
        preferedSchedule = new DateTimePicker("Prefered Schedule");
        preferedSchedule.setStep(Duration.ofSeconds(1));
        email = new TextField("Email");
        address = new TextField("Address");
        package_ = new TextField("Package");
        paymentStatus = new TextField("Payment Status");
        formLayout.add(firstName, lastName, phoneNo, preferedSchedule, email, address, package_, paymentStatus);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Reservations value) {
        this.reservations = value;
        binder.readBean(this.reservations);

    }
}
