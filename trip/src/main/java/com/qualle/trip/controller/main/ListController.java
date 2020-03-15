package com.qualle.trip.controller.main;

import com.qualle.trip.config.ViewHolder;
import com.qualle.trip.controller.AbstractController;
import com.qualle.trip.controller.edit.EditAllowanceController;
import com.qualle.trip.controller.edit.EditEmployeeController;
import com.qualle.trip.controller.edit.EditTicketController;
import com.qualle.trip.controller.edit.EditTripController;
import com.qualle.trip.model.dto.AllowanceDto;
import com.qualle.trip.model.dto.EmployeeSimpleDto;
import com.qualle.trip.model.dto.TicketDto;
import com.qualle.trip.model.dto.TripSimpleDto;
import com.qualle.trip.service.AllowanceService;
import com.qualle.trip.service.EmployeeService;
import com.qualle.trip.service.TicketService;
import com.qualle.trip.service.TripService;
import com.qualle.trip.service.enums.PageType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.UnsupportedEncodingException;

import static com.qualle.trip.controller.util.ControllerUtil.*;

public class ListController implements AbstractController {

    private PageType type;

    @Autowired
    @Qualifier("allowanceEdit")
    private ViewHolder allowanceEditView;

    @Autowired
    @Qualifier("ticketEdit")
    private ViewHolder ticketEditView;

    @Autowired
    @Qualifier("tripEdit")
    private ViewHolder tripEditView;

    @Autowired
    @Qualifier("employeeEdit")
    private ViewHolder employeeEditView;

    @Autowired
    private AllowanceService allowanceService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TripService tripService;

    @Autowired
    private EmployeeService employeeService;

    @FXML
    private ListView<Object> list;

    @FXML
    private TextField search;

    @FXML
    private Label title;

    @FXML
    private Button report;

    @FXML
    private Button add;

    @FXML
    public void add(ActionEvent event) {

        switch (type) {
            case ALLOWANCE:
                openWindow(allowanceEditView, getStage(event));
                break;

            case TICKET:
                openWindow(ticketEditView, getStage(event));
                break;

            case TRIP:
                openWindow(tripEditView, getStage(event));
                break;

            case EMPLOYEE:
                openWindow(employeeEditView, getStage(event));
                break;
        }
    }

    @FXML
    public void doSearch(KeyEvent event) {

        switch (type) {
            case ALLOWANCE:
                list.setItems(FXCollections.observableArrayList(search.getText() == null
                        ? allowanceService.getAllDto()
                        : allowanceService.getDtoByCountry(search.getText().trim())));
                break;

            case TICKET:
                list.setItems(FXCollections.observableArrayList(search.getText() == null
                        ? ticketService.getAllDto()
                        : ticketService.getDtoByLocation(search.getText().trim())));
                break;

            case TRIP:
                list.setItems(FXCollections.observableArrayList(search.getText() == null
                        ? tripService.getAllSimpleDto()
                        : tripService.getSimpleDtoByTitle(search.getText().trim())));
                break;

            case EMPLOYEE:
                list.setItems(FXCollections.observableArrayList(search.getText() == null
                        ? employeeService.getAllDto()
                        : employeeService.getSimpleDtoByName(search.getText().trim())));
                break;
        }
    }

    @FXML
    public void delete(ActionEvent event) {

        switch (type) {
            case ALLOWANCE:
                allowanceService.delete(((AllowanceDto) list.getSelectionModel().getSelectedItem()).getId());
                break;

            case TICKET:
                ticketService.delete(((TicketDto) list.getSelectionModel().getSelectedItem()).getId());
                break;

            case TRIP:
                tripService.delete(((TripSimpleDto) list.getSelectionModel().getSelectedItem()).getId());
                break;

            case EMPLOYEE:
                employeeService.delete(((EmployeeSimpleDto) list.getSelectionModel().getSelectedItem()).getId());
                break;
        }
        list.getItems().remove(list.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void getItem(MouseEvent click) {

        if (click.getClickCount() == 2 && list.getSelectionModel().getSelectedItem() != null) {

            switch (type) {
                case ALLOWANCE:
                    EditAllowanceController allowanceController = (EditAllowanceController) allowanceEditView.getController();
                    allowanceController.setId(((AllowanceDto) list.getSelectionModel().getSelectedItem()).getId());
                    openWindow(allowanceEditView, getStage(click));
                    break;

                case TICKET:
                    EditTicketController ticketController = (EditTicketController) ticketEditView.getController();
                    ticketController.setId(((TicketDto) list.getSelectionModel().getSelectedItem()).getId());
                    openWindow(ticketEditView, getStage(click));
                    break;

                case TRIP:
                    EditTripController tripController = (EditTripController) tripEditView.getController();
                    tripController.setId(((TripSimpleDto) list.getSelectionModel().getSelectedItem()).getId());
                    openWindow(tripEditView, getStage(click));
                    break;

                case EMPLOYEE:
                    EditEmployeeController employeeController = (EditEmployeeController) employeeEditView.getController();
                    employeeController.setId(((EmployeeSimpleDto) list.getSelectionModel().getSelectedItem()).getId());
                    openWindow(employeeEditView, getStage(click));
                    break;
            }
        }
    }

    @Override
    public void onShow() {

        report.setDisable(true);
        add.setDisable(false);

        switch (type) {
            case ALLOWANCE:
                title.setText("Размеры возмещения расходов");
                list.setItems(FXCollections.observableArrayList(allowanceService.getAllDto()));
                break;

            case TICKET:
                title.setText("Список всех билетов");
                list.setItems(FXCollections.observableArrayList(ticketService.getAllDto()));
                break;

            case TRIP:
                title.setText("Список всех командировок");
                report.setDisable(false);
                add.setDisable(true);
                list.setItems(FXCollections.observableArrayList(tripService.getAllSimpleDto()));
                break;

            case EMPLOYEE:
                title.setText("Список всех сотрудников");
                list.setItems(FXCollections.observableArrayList(employeeService.getAllSimpleDto()));
                break;
        }
    }

    @Override
    public void onClose() {
        search.setText(null);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @FXML
    public void doReport(ActionEvent event) {

        long id = ((TripSimpleDto) list.getSelectionModel().getSelectedItem()).getId();

        if (type.equals(PageType.TRIP) && id != 0) {

            try {
                tripService.report(id);

            } catch (Exception e) {
                openModal(getStage(event), "Ошибка!");
            }
        }
    }

    public void setType(PageType type) {
        this.type = type;
    }
}
