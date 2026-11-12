package com.appregistros.proyectohibernate.controller;

import com.appregistros.proyectohibernate.dao.CategoriaDao;
import com.appregistros.proyectohibernate.model.Categoria;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CategoriasController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnCargar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private TableView<Categoria> tablaCategorias;

    @FXML
    private TableColumn<Categoria, Long> colId;

    @FXML
    private TableColumn<Categoria, String> colNombre;

    @FXML
    private TableColumn<Categoria, String> colDescripcion;

    private final CategoriaDao categoriaDao = new CategoriaDao();
    private final ObservableList<Categoria> categorias = FXCollections.observableArrayList();
    private Categoria categoriaSeleccionada;

    @FXML
    private void initialize() {
        txtNombre.setPromptText("Ej. Ciencia Ficción");
        txtDescripcion.setPromptText("Ej. Libros de ciencia ficción y futuros alternos");

        colId.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getId()));
        colNombre.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getNombre()));
        colDescripcion.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getDescripcion()));

        tablaCategorias.setItems(categorias);
        tablaCategorias.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            categoriaSeleccionada = newSelection;
            if (newSelection != null) {
                cargarCamposDesdeSeleccion(newSelection);
                btnModificar.setDisable(false);
                btnEliminar.setDisable(false);
            } else {
                btnModificar.setDisable(true);
                btnEliminar.setDisable(true);
            }
        });

        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);

        try {
            cargarCategorias();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error de conexión", "No se pudo conectar a PostgreSQL. Verifica DB_URL, DB_USER y DB_PASSWORD.\n" + causaRaiz(ex));
        }
    }

    @FXML
    private void guardarCategoria() {
        if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty() ||
                txtDescripcion.getText() == null || txtDescripcion.getText().trim().isEmpty()) {
            mostrarAlerta("Campos obligatorios", "Debes completar el nombre y la descripción de la categoría.");
            return;
        }

        try {
            Categoria categoria = new Categoria();
            categoria.setNombre(txtNombre.getText().trim());
            categoria.setDescripcion(txtDescripcion.getText().trim());

            categoriaDao.guardar(categoria);
            mostrarAlerta("Guardado", "La categoría se guardó correctamente.");
            limpiarCampos();
            cargarCategorias();
        } catch (RuntimeException e) {
            e.printStackTrace();
            mostrarAlerta("Error de conexión", "No se pudo guardar la categoría. Revisa PostgreSQL.\n" + causaRaiz(e));
        }
    }

    @FXML
    private void modificarCategoria() {
        if (categoriaSeleccionada == null) {
            mostrarAlerta("Selección requerida", "Selecciona una categoría de la tabla para modificarla.");
            return;
        }

        try {
            categoriaSeleccionada.setNombre(txtNombre.getText().trim());
            categoriaSeleccionada.setDescripcion(txtDescripcion.getText().trim());

            categoriaDao.actualizar(categoriaSeleccionada);
            mostrarAlerta("Actualización", "La categoría se actualizó correctamente.");
            limpiarCampos();
            cargarCategorias();
        } catch (RuntimeException e) {
            e.printStackTrace();
            mostrarAlerta("Error de conexión", "No se pudo actualizar la categoría. Revisa PostgreSQL.\n" + causaRaiz(e));
        }
    }

    @FXML
    private void eliminarCategoria() {
        if (categoriaSeleccionada == null) {
            mostrarAlerta("Selección requerida", "Selecciona una categoría para eliminarla.");
            return;
        }

        try {
            categoriaDao.eliminar(categoriaSeleccionada.getId());
            mostrarAlerta("Eliminado", "La categoría se eliminó correctamente.");
            limpiarCampos();
            cargarCategorias();
        } catch (RuntimeException e) {
            e.printStackTrace();
            mostrarAlerta("Error de conexión", "No se pudo eliminar la categoría. Revisa PostgreSQL.\n" + causaRaiz(e));
        }
    }

    @FXML
    private void cargarCategorias() {
        categorias.clear();
        categorias.addAll(categoriaDao.listar());
        tablaCategorias.setItems(categorias);
        tablaCategorias.refresh();
    }

    @FXML
    private void limpiarCampos() {
        categoriaSeleccionada = null;
        txtNombre.clear();
        txtDescripcion.clear();
        tablaCategorias.getSelectionModel().clearSelection();
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        txtNombre.requestFocus();
    }

    @FXML
    private void cancelar() {
        limpiarCampos();
    }

    private void cargarCamposDesdeSeleccion(Categoria categoria) {
        txtNombre.setText(categoria.getNombre());
        txtDescripcion.setText(categoria.getDescripcion());
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private Categoria obtenerCategoriaDesdeCampos() {
        Categoria categoria = new Categoria();
        categoria.setNombre(txtNombre.getText().trim());
        categoria.setDescripcion(txtDescripcion.getText().trim());
        return categoria;
    }

    private String causaRaiz(Throwable e) {
        Throwable t = e;
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return t.getClass().getSimpleName() + ": " + t.getMessage();
    }
}
