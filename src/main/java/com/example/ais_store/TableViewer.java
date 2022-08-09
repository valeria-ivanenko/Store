package com.example.ais_store;

import com.example.ais_store.Commands.Commands;
import com.example.ais_store.DB.DbUtils;
import com.example.ais_store.Models.Category;
import com.example.ais_store.Models.Product;
import com.example.ais_store.Packet.Packet;
import com.example.ais_store.Packet.PacketInfo;
import com.example.ais_store.TCP.ClientTCP;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TableViewer implements Initializable {

    //region Fields
    private ClientTCP client;

    private String allString = "Всі";

    @FXML
    private TableView<Product> tableProduct;
    @FXML
    private TableColumn<Product, Integer> idProductColumn;
    @FXML
    private TableColumn<Product, Integer> pr_idCategoryColumn;
    @FXML
    private TableColumn<Product, String> nameProductColumn;
    @FXML
    private TableColumn<Product, Double> priceProductColumn;
    @FXML
    private TableColumn<Product, Integer> numberProductColumn;
    @FXML
    public TableColumn<Product, Double> sumPriceProductColumn;
    @FXML
    public TextField categoryIdOfProduct;
    @FXML
    public TextField nameOfProduct;
    @FXML
    public TextField priceOfProduct;
    @FXML
    public TextField productNumberOfProduct;
    @FXML
    public Button addProduct;

    @FXML
    private TableView<Category> tableCategory;
    @FXML
    private TableColumn<Category, Integer> idCategoryColumn;
    @FXML
    private TableColumn<Category, String> nameCategoryColumn;
    @FXML
    public TableColumn<Category, Double> sumPriceCategoryColumn;

    @FXML
    public ComboBox<String> categoriesComboBox;
    @FXML
    public TextField filteredId;

    @FXML
    public TextField categoryName;
    //endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = new ClientTCP();
        try {
            client.startConnection(8888);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        tableProduct.setOnKeyPressed(keyEvent -> {
            final Product selectedItem = tableProduct.getSelectionModel().getSelectedItem();

            if ( selectedItem != null )
            {
                if ( keyEvent.getCode().equals( KeyCode.DELETE ) )
                {
                    try {
                        client.send(Commands.DELETE_PRODUCT_BY_ID.ordinal(), String.valueOf(selectedItem.getIdProduct()));
                        updateProducts();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    updateProducts();
                }
            }
        });

        tableCategory.setOnKeyPressed(keyEvent -> {
            final Category selectedItem = tableCategory.getSelectionModel().getSelectedItem();

            if ( selectedItem != null )
            {
                if ( keyEvent.getCode().equals( KeyCode.DELETE ) )
                {
                    try {
                        client.send(Commands.DELETE_CATEGORY_BY_ID.ordinal(), String.valueOf(selectedItem.getIdCategory()));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    updateCategories();
                    updateProducts();
                }
            }
        });

        filteredId.setOnKeyPressed(keyEvent -> {
            if ( keyEvent.getCode().equals( KeyCode.ENTER ) )
            {
                try {
                    if (filteredId.getText().isEmpty() || filteredId.getText().isBlank()) {
                        updateProducts();
                    } else {
                        updateProducts(filteredId.getText());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        tableProduct.setEditable(true);
        tableCategory.setEditable(true);

        setupProductTableColumns();
        setupCategoryTableColumns();

        updateProducts();
        updateCategories();
        updateComboBoxCategories();
    }

    private void setupProductTableColumns() {
        idProductColumn.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        idProductColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        idProductColumn.setOnEditCommit(event -> {
            try {
                client.send(Commands.UPDATE_PRODUCT_ID.ordinal(), event.getNewValue() + " " + event.getOldValue());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            updateProducts();
        });

        pr_idCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("idCategory"));
        pr_idCategoryColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        pr_idCategoryColumn.setOnEditCommit(event -> {
            try {
                PacketInfo p = client.send(Commands.UPDATE_PRODUCT_CATEGORY.ordinal(), event.getRowValue().getIdProduct() + " " + event.getNewValue());
                if (new String(p.bMsg.message).equals("0")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("This category does`t exist");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                } else {
                    updateProducts();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        nameProductColumn.setCellValueFactory(new PropertyValueFactory<>("nameProduct"));
        nameProductColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameProductColumn.setOnEditCommit(event -> {
            try {
                client.send(Commands.UPDATE_PRODUCT_NAME.ordinal(), event.getRowValue().getIdProduct() + "$" + event.getNewValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            updateProducts();
        });

        priceProductColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceProductColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceProductColumn.setOnEditCommit(event -> {
            try {
                client.send(Commands.UPDATE_PRODUCT_PRICE.ordinal(), event.getRowValue().getIdProduct() + " " + event.getNewValue());
                //DbUtils.updatePrice(event.getNewValue(), event.getRowValue().getIdProduct());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            updateProducts();
            updateCategories();
        });

        numberProductColumn.setCellValueFactory(new PropertyValueFactory<>("productNumber"));
        numberProductColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        numberProductColumn.setOnEditCommit(event -> {
            try {
                client.send(Commands.UPDATE_PRODUCT_NUMBER.ordinal(), event.getRowValue().getIdProduct() + " " + event.getNewValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            updateProducts();
            updateCategories();
        });

        sumPriceProductColumn.setCellValueFactory(new PropertyValueFactory<>("sumPrice"));
    }

    private void setupCategoryTableColumns() {
        idCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("idCategory"));
        idCategoryColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        idCategoryColumn.setOnEditCommit(event ->
        {
            try {
                client.send(Commands.UPDATE_CATEGORY_ID.ordinal(), event.getRowValue().getIdCategory() + " " + event.getNewValue());
                //DbUtils.updateIdCategory(event.getNewValue(), event.getRowValue().getIdCategory());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            updateCategories();
            updateProducts();
            updateComboBoxCategories();
        });

        nameCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCategoryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCategoryColumn.setOnEditCommit(event ->
        {
            try {
                client.send(Commands.UPDATE_CATEGORY_NAME.ordinal(), event.getRowValue().getIdCategory() +
                        "$" + event.getNewValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            updateCategories();
            updateComboBoxCategories();
        });

        sumPriceCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("sumPrice"));
    }

    private void updateProducts() {
        ObservableList<Product> data = FXCollections.observableArrayList(
                DbUtils.showAllProducts());
        tableProduct.setItems(data);
    }

    private void updateProducts(int id_category) {
        ObservableList<Product> data = FXCollections.observableArrayList(DbUtils.showAllProductsFromCategory(id_category));

        tableProduct.setItems(data);
    }

    private void updateProducts(String id) {
        ObservableList<Product> data = FXCollections.observableArrayList(DbUtils.showProductByID(Integer.parseInt(id)));

        tableProduct.setItems(data);
    }

    private void updateCategories() {
        ObservableList<Category> data = FXCollections.observableArrayList(
                DbUtils.showAllCategories());
        tableCategory.setItems(data);
    }

    private void updateComboBoxCategories() {
        ObservableList<String> data = FXCollections.observableArrayList(DbUtils.getCategoryNames());
        categoriesComboBox.setItems(data);
    }

    public void addProductButtonClicked(ActionEvent event) throws Exception {
        PacketInfo p = client.send(Commands.ADD_PRODUCT.ordinal(),
                nameOfProduct.getText() + "$"
                        + categoryIdOfProduct.getText() + "$"
                        + priceOfProduct.getText() + "$"
                        + productNumberOfProduct.getText());

        if (new String(p.bMsg.message).equals("0")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("This category does`t exist");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        } else {
            updateProducts();
        }
    }

    public void selectedCategoryChanged(ActionEvent event) {
        String name = categoriesComboBox.getSelectionModel().getSelectedItem();
        if (name.isEmpty() || name.equals("Всі")) {
            updateProducts();
        } else {
            updateProducts(DbUtils.getCategoryIdByName(name));
        }
    }

    public void addCategoryButtonClicked(ActionEvent event) throws Exception {
        client.send(Commands.ADD_CATEGORY.ordinal(), categoryName.getText());
        updateCategories();
        updateComboBoxCategories();
    }
}
