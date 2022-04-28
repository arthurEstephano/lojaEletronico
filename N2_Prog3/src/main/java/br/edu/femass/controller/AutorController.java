package br.edu.femass.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

import br.edu.femass.dao.AutorDao;
import br.edu.femass.model.Autor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AutorController implements Initializable {
    private final AutorDao autorDao = new AutorDao();

    @FXML
    private ListView<Autor> LstAutores;

    @FXML
    private Button BtnIncluir;

    @FXML
    private Button BtnExcluir;

    @FXML
    private Button BtnGravar;

    @FXML
    private Button BtnCancelar;

    @FXML
    private TextField TxtNome;

    @FXML
    private TextField TxtSobrenome;


    private void limparTela() {
        TxtSobrenome.setText("");
        TxtNome.setText("");
    }
    private void habilitarInterface(Boolean incluir) {
        TxtNome.setDisable(!incluir);
        TxtSobrenome.setDisable(!incluir);
        BtnGravar.setDisable(!incluir);
        BtnCancelar.setDisable(!incluir);
        BtnExcluir.setDisable(incluir);
        BtnIncluir.setDisable(incluir);
        LstAutores.setDisable(incluir);
    }

    private void exibirAutor() {
        Autor autor = LstAutores.getSelectionModel().getSelectedItem();
        if (autor==null) return;
        TxtNome.setText(autor.getNome());
        TxtSobrenome.setText(autor.getSobreNome());

    }

    @FXML
    private void LstAutores_MouseClicked(MouseEvent evento) {
        exibirAutor();
    }

    @FXML
    private void LstAutores_KeyPressed(KeyEvent evento) {
        exibirAutor();
    }

    @FXML
    private void BtnIncluir_Action(ActionEvent evento) {
        atualizarLista();
        habilitarInterface(true);
        limparTela();
        TxtNome.requestFocus();
    }

    @FXML
    private void BtnExcluir_Action(ActionEvent evento) {
        Autor autor = LstAutores.getSelectionModel().getSelectedItem();

        if (autor==null) return;

        try {
            autorDao.excluir(autor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        atualizarLista();

    }
    @FXML
    private void BtnGravar_Action(ActionEvent evento) {
        Autor autor = new Autor();
        autor.setSobreNome(TxtSobrenome.getText());
        autor.setNome(TxtNome.getText());
        if (TxtSobrenome.getText() == "" || TxtNome.getText() == ""){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.show();
        }
        else {
            try {
                autorDao.gravar(autor);
            } catch (Exception e) {
                e.printStackTrace();
            }
            atualizarLista();
            habilitarInterface(false);
        }
    }
    @FXML
    private void BtnCancelar_Action(ActionEvent evento) {
        habilitarInterface(false);
    }


    private void atualizarLista() {
        List<Autor> autores;
        try {
            autores = autorDao.listar();
        } catch (Exception e) {
            autores = new ArrayList<>();
        }
        ObservableList<Autor> autoresOb = FXCollections.observableArrayList(autores);
        LstAutores.setItems(autoresOb);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atualizarLista();
    }
}
