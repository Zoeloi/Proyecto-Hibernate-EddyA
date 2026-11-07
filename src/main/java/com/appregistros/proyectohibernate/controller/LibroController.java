package com.appregistros.proyectohibernate.controller;

import com.appregistros.proyectohibernate.dao.LibroDao;
import com.appregistros.proyectohibernate.model.Libro;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class LibroController {

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextField txtAutor;

    @FXML
    private TextField txtAnio;

    @FXML
    private TextField txtPrecio;

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
    private TableView<Libro> tablaLibros;

    @FXML
    private TableColumn<Libro, Long> colId;

    @FXML
    private TableColumn<Libro, String> colTitulo;

    @FXML
    private TableColumn<Libro, String> colAutor;

    @FXML
    private TableColumn<Libro, Integer> colAnio;

    @FXML
    private TableColumn<Libro, Double> colPrecio;

    private final LibroDao libroDao = new LibroDao();
    private final ObservableList<Libro> libros = FXCollections.observableArrayList();
    private Libro libroSeleccionado;

    @FXML
    private void initialize() {
        txtTitulo.setPromptText("Ej. El Quijote");
        txtAutor.setPromptText("Ej. Miguel de Cervantes");
        txtAnio.setPromptText("2024");
        txtPrecio.setPromptText("149.99");

        colId.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getId()));
        colTitulo.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTitulo()));
        colAutor.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getAutor()));
        colAnio.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getAnio_publicacion()));
        colPrecio.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPrecio()));

        tablaLibros.setItems(libros);
        tablaLibros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            libroSeleccionado = newSelection;
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
            cargarLibros();
        } catch (RuntimeException ex) {
            mostrarAlerta("Error de conexión", "No se pudo conectar a PostgreSQL. Verifica DB_URL, DB_USER y DB_PASSWORD.\n" + ex.getMessage());
        }
    }

    @FXML
    private void guardarLibro() {
        if (txtTitulo.getText() == null || txtTitulo.getText().trim().isEmpty() ||
                txtAutor.getText() == null || txtAutor.getText().trim().isEmpty() ||
                txtAnio.getText() == null || txtAnio.getText().trim().isEmpty() ||
                txtPrecio.getText() == null || txtPrecio.getText().trim().isEmpty()) {
            mostrarAlerta("Campos obligatorios", "Debes completar todos los campos.");
            return;
        }

        try {
            Libro libro = new Libro();
            libro.setTitulo(txtTitulo.getText().trim());
            libro.setAutor(txtAutor.getText().trim());
            libro.setAnio_publicacion(Integer.parseInt(txtAnio.getText().trim()));
            libro.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));

            libroDao.guardar(libro);
            mostrarAlerta("Guardado", "El libro se guardó correctamente.");
            limpiarCampos();
            cargarLibros();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Año y precio deben ser valores numéricos válidos.");
        } catch (RuntimeException e) {
            mostrarAlerta("Error de conexión", "No se pudo guardar el libro. Revisa PostgreSQL.\n" + e.getMessage());
        }
    }

    @FXML
    private void modificarLibro() {
        if (libroSeleccionado == null) {
            mostrarAlerta("Selección requerida", "Selecciona un libro de la tabla para modificarlo.");
            return;
        }

        try {
            libroSeleccionado.setTitulo(txtTitulo.getText().trim());
            libroSeleccionado.setAutor(txtAutor.getText().trim());
            libroSeleccionado.setAnio_publicacion(Integer.parseInt(txtAnio.getText().trim()));
            libroSeleccionado.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));

            libroDao.actualizar(libroSeleccionado);
            mostrarAlerta("Actualización", "El libro se actualizó correctamente.");
            limpiarCampos();
            cargarLibros();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Año y precio deben ser valores numéricos válidos.");
        } catch (RuntimeException e) {
            mostrarAlerta("Error de conexión", "No se pudo actualizar el libro. Revisa PostgreSQL.\n" + e.getMessage());
        }
    }

    @FXML
    private void eliminarLibro() {
        if (libroSeleccionado == null) {
            mostrarAlerta("Selección requerida", "Selecciona un libro para eliminarlo.");
            return;
        }

        try {
            libroDao.eliminar(libroSeleccionado.getId());
            mostrarAlerta("Eliminado", "El libro se eliminó correctamente.");
            limpiarCampos();
            cargarLibros();
        } catch (RuntimeException e) {
            mostrarAlerta("Error de conexión", "No se pudo eliminar el libro. Revisa PostgreSQL.\n" + e.getMessage());
        }
    }

    @FXML
    private void cargarLibros() {
        libros.clear();
        libros.addAll(libroDao.listar());
        tablaLibros.setItems(libros);
        tablaLibros.refresh();
    }

    @FXML
    private void limpiarCampos() {
        libroSeleccionado = null;
        txtTitulo.clear();
        txtAutor.clear();
        txtAnio.clear();
        txtPrecio.clear();
        tablaLibros.getSelectionModel().clearSelection();
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        txtTitulo.requestFocus();
    }

    @FXML
    private void cancelar() {
        limpiarCampos();
    }

    private void cargarCamposDesdeSeleccion(Libro libro) {
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtAnio.setText(String.valueOf(libro.getAnio_publicacion()));
        txtPrecio.setText(String.valueOf(libro.getPrecio()));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private Libro obtenerLibroDesdeCampos(){
        Libro libro = new Libro();
        libro.setTitulo(txtTitulo.getText().trim());
        libro.setAutor(txtAutor.getText().trim());
        libro.setAnio_publicacion(Integer.parseInt(txtAnio.getText().trim()));
        libro.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
        return libro;

    }
}
